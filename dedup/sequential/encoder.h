#include "chunk_list.h"

#ifndef _ENCODER_H_
#define _ENCODER_H_ 1

typedef struct{
  List * compress;
  List * send;
}Dedup_q;



void Encode(config_t * conf);

#endif /* !_ENCODER_H_ */
