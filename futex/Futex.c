#include <pthread.h>
#include <stdio.h>


#define NUMTHREADS 256


void* Work(void* args){
  pthread_mutex_t * lock = (pthread_mutex_t*) args;
  int j = 0;
  for (int i = 0; i<1000000;i++){
    if (pthread_mutex_lock(lock) != 0) j++;
    pthread_mutex_unlock(lock);
  }
  printf("Klaar! j= %d\n", j);
  return NULL;
}

int main(){
  pthread_mutex_t lock;
  pthread_mutex_init(&lock, NULL);
  pthread_t threads[NUMTHREADS];

  for (int i = 0; i<NUMTHREADS;i++) pthread_create(&threads[i], NULL, Work, &lock);
  for (int i = 0; i<NUMTHREADS;i++) pthread_join(threads[i], NULL);

  pthread_mutex_destroy(&lock);
  return 0;
}
