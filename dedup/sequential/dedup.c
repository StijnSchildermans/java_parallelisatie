#ifndef _GNU_SOURCE
#define _GNU_SOURCE
#endif
#ifndef __USE_GNU
#define __USE_GNU
#endif

#include <unistd.h>
#include <string.h>
#include <stdio.h>
#include <execinfo.h>
#include <signal.h>
#include <stdlib.h>
#include <ucontext.h>

#include "util.h"
#include "debug.h"
#include "dedupdef.h"
#include "encoder.h"
#include "decoder.h"
#include "config.h"
#include "queue.h"

#ifdef ENABLE_DMALLOC
#include <dmalloc.h>
#endif //ENABLE_DMALLOC

#ifdef ENABLE_PTHREADS
#include <pthread.h>
#endif //ENABLE_PTHREADS

#ifdef ENABLE_PARSEC_HOOKS
#include <hooks.h>
#endif //ENABLE_PARSEC_HOOKS




config_t * conf;



/*--------------------------------------------------------------------------*/
static void
usage(char* prog)
{
  printf("usage: %s [-cusfvh] [-w gzip/bzip2/none] [-i file] [-o file] [-t number_of_threads]\n",prog);
  printf("-c \t\t\tcompress\n");
  printf("-u \t\t\tuncompress\n");
  printf("-p \t\t\tpreloading (for benchmarking purposes)\n");
  printf("-w \t\t\tcompression type: gzip/bzip2/none\n");
  printf("-i file\t\t\tthe input file\n");
  printf("-o file\t\t\tthe output file\n");
  printf("-t \t\t\tnumber of threads per stage \n");
  printf("-v \t\t\tverbose output\n");
  printf("-h \t\t\thelp\n");
}

/* This structure mirrors the one found in /usr/include/asm/ucontext.h */
typedef struct _sig_ucontext {
 unsigned long     uc_flags;
 struct ucontext   *uc_link;
 stack_t           uc_stack;
 struct sigcontext uc_mcontext;
 sigset_t          uc_sigmask;
} sig_ucontext_t;

void crit_err_hdlr(int sig_num, siginfo_t * info, void * ucontext)
{
 void *             array[50];
 void *             caller_address;
 char **            messages;
 int                size, i;
 sig_ucontext_t *   uc;

 uc = (sig_ucontext_t *)ucontext;

 /* Get the address at the time the signal was raised */
#if defined(__i386__) // gcc specific
 caller_address = (void *) uc->uc_mcontext.eip; // EIP: x86 specific
#elif defined(__x86_64__) // gcc specific
 caller_address = (void *) uc->uc_mcontext.rip; // RIP: x86_64 specific
#else
#error Unsupported architecture. // TODO: Add support for other arch.
#endif

 fprintf(stderr, "signal %d (%s), address is %p from %p\n",
  sig_num, strsignal(sig_num), info->si_addr,
  (void *)caller_address);

 size = backtrace(array, 50);

 /* overwrite sigaction with caller's address */
 array[1] = caller_address;

 messages = backtrace_symbols(array, size);

 /* skip first stack frame (points here) */
 for (i = 1; i < size && messages != NULL; ++i)
 {
  fprintf(stderr, "[bt]: (%d) %s\n", i, messages[i]);
 }

 free(messages);

 exit(EXIT_FAILURE);
}

/*--------------------------------------------------------------------------*/
int main(int argc, char** argv) {
  struct sigaction sigact;

 sigact.sa_sigaction = crit_err_hdlr;
 sigact.sa_flags = SA_RESTART | SA_SIGINFO;

 if (sigaction(SIGSEGV, &sigact, (struct sigaction *)NULL) != 0)
 {
  fprintf(stderr, "error setting signal handler for %d (%s)\n",
    SIGSEGV, strsignal(SIGSEGV));

  exit(EXIT_FAILURE);
 }

#ifdef PARSEC_VERSION
#define __PARSEC_STRING(x) #x
#define __PARSEC_XSTRING(x) __PARSEC_STRING(x)
  //printf("PARSEC Benchmark Suite Version "__PARSEC_XSTRING(PARSEC_VERSION)"\n");
#else
  //printf("PARSEC Benchmark Suite\n");
#endif //PARSEC_VERSION
#ifdef ENABLE_PARSEC_HOOKS
        __parsec_bench_begin(__parsec_dedup);
#endif //ENABLE_PARSEC_HOOKS

  int32 compress = TRUE;

  //We force the sha1 sum to be integer-aligned, check that the length of a sha1 sum is a multiple of unsigned int
  assert(SHA1_LEN % sizeof(unsigned int) == 0);

  conf = (config_t *) malloc(sizeof(config_t));
  if (conf == NULL) {
    EXIT_TRACE("Memory allocation failed\n");
  }

  strcpy(conf->outfile, "");
  conf->compress_type = COMPRESS_GZIP;
  conf->preloading = 0;
  conf->nthreads = 1;
  conf->verbose = 0;

  //parse the args
  int ch;
  opterr = 0;
  optind = 1;
  while (-1 != (ch = getopt(argc, argv, "cupvo:i:w:t:h"))) {
    switch (ch) {
    case 'c':
      compress = TRUE;
      strcpy(conf->infile, "test.txt");
      strcpy(conf->outfile, "out.ddp");
      break;
    case 'u':
      compress = FALSE;
      strcpy(conf->infile, "out.ddp");
      strcpy(conf->outfile, "new.txt");
      break;
    case 'w':
      if (strcmp(optarg, "gzip") == 0)
        conf->compress_type = COMPRESS_GZIP;
      else if (strcmp(optarg, "bzip2") == 0)
        conf->compress_type = COMPRESS_BZIP2;
      else if (strcmp(optarg, "none") == 0)
        conf->compress_type = COMPRESS_NONE;
      else {
        fprintf(stdout, "Unknown compression type `%s'.\n", optarg);
        usage(argv[0]);
        return -1;
      }
      break;
    case 'o':
      strcpy(conf->outfile, optarg);
      break;
    case 'i':
      strcpy(conf->infile, optarg);
      break;
    case 'h':
      usage(argv[0]);
      return -1;
    case 'p':
      conf->preloading = TRUE;
      break;
    case 't':
      conf->nthreads = atoi(optarg);
      break;
    case 'v':
      conf->verbose = TRUE;
      break;
    case '?':
      fprintf(stdout, "Unknown option `-%c'.\n", optopt);
      usage(argv[0]);
      return -1;
    }
  }

  if (argc < 2) {
	  usage(argv[0]);
	  exit(EXIT_FAILURE);
  }

#ifndef ENABLE_BZIP2_COMPRESSION
 if (conf->compress_type == COMPRESS_BZIP2){
    printf("Bzip2 compression not supported\n");
    exit(1);
  }
#endif

#ifndef ENABLE_GZIP_COMPRESSION
 if (conf->compress_type == COMPRESS_GZIP){
    printf("Gzip compression not supported\n");
    exit(1);
  }
#endif

#ifndef ENABLE_STATISTICS
 if (conf->verbose){
    printf("Statistics collection not supported\n");
    exit(1);
  }
#endif

#ifndef ENABLE_PTHREADS
 if (conf->nthreads != 1){
    printf("Number of threads must be 1 (serial version)\n");
    exit(1);
  }
#endif

  if (compress) {
    Encode(conf);
  } else {
    Decode(conf);
  }

  free(conf);

#ifdef ENABLE_PARSEC_HOOKS
  __parsec_bench_end();
#endif

  return 0;
}
