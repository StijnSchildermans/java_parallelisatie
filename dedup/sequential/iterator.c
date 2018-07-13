#include "iterator.h"

Iterator * init_iterator(List * list){
  Iterator * iter = malloc(sizeof(Iterator));
  iter->list = list;
  iter->index = NULL;
  return iter;
}

Node * next_node(Iterator * iter){
  Node * n = iter->index;
  if (n == NULL) {
    n = iter->list->head;
    if (n==NULL) return NULL;
    else{
      iter->index = n;
      return n;
    }
  }
  Node * nn = n->next;
  if(nn==NULL) return NULL;
  iter->index = nn;
  return nn;
}
chunk_t * next(Iterator * iter){
  Node * n = next_node(iter);
  if (n == NULL) return NULL;
  return n->data;
};
void reset(Iterator * iter){
    iter->index = iter->list->head;
}
int hasNext(Iterator * iter){
  return (iter->index != iter->list->tail);
}
void destroy_iterator(Iterator * iter){
  free(iter);
}
