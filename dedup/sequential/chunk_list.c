#include<stdlib.h>
#include<string.h>
#include "chunk_list.h"
#include "iterator.h"


Node * createnode(chunk_t * data){
  Node * newNode = malloc(sizeof(Node));
  newNode->data = data;
  newNode->next = NULL;
  return newNode;
}

List * emptylist(){
  List * list = malloc(sizeof(List));
  list->head = NULL;
  list->tail = NULL;
  return list;
}

void add(chunk_t * elem, List * list){
  if(list->head == NULL){
    list->head = createnode(elem);
    list->tail = list->head;
  }
  else{
    Node * last = list->tail;
    Node * new = createnode(elem);
    last->next = new;
    list->tail = new;
  }
}
void add_node(Node * node, List * list){
  if (list->head == NULL) list->head = node;
  else list->tail->next = node;
  list->tail = node;
}
void add_nodes(Node * head, Node * tail, List * list){
  if (list->head == NULL) list->head = head;
  else list->tail->next = head;
  list->tail = tail;
}
int length(List * list){
  int i = 1;
  Iterator * iter = init_iterator(list);
  while(next_node(iter)!=list->tail) i++;
  destroy_iterator(iter);
  return i;
}

chunk_t * get(int index, List * list){
  int i = 0;
  Node * n;
  if (list->head == NULL) return NULL;
  else n = list->head;
  while (i<index){
    n = n->next;
    if (n==NULL) return NULL;
    i++;
  }
  return n->data;
}

void destroy(List * list){
  Node * current = list->head;
  Node * next = current;
  while(current != NULL){
    next = current->next;
    free(current->data);
    free(current);
    current = next;
  }
  free(list);
}
void destroy_soft(List * list){
  Node * current = list->head;
  Node * next = current;
  while(current != NULL){
    next = current->next;
    free(current);
    current = next;
  }
  free(list);
}
//Splits a list in n sublists of sequential elements.
List ** split(int n, List * list){
  int size = length(list)/n;
  List ** lists = malloc(n * sizeof(List*));
  for (int q = 0; q<n;q++) lists[q] = emptylist();

  int i = 0;
  Iterator * iter = init_iterator(list);
  while(hasNext(iter)){
    int l;
    if(i/size < n) l = (i/size);
    else l = (n-1);
    add(next(iter),lists[l]);
    i++;
  }
  destroy_soft(list);
  destroy_iterator(iter);
  return lists;
}
void compare(List * l1, List* l2){
  Iterator * iter1 = init_iterator(l1);
  Iterator* iter2;
  while(hasNext(iter1)){
      iter2 = init_iterator(l2);
      while(hasNext(iter2)){
        if(next(iter2) == next(iter1)) printf("Found double chunk!\n");
      }
      destroy_iterator(iter2);
  }
}
//Splits a list in n sublists
// with each m'th element of the sublist being the (n*m)'th e
//lement of the original list
List ** split_mod(int n, List * list){
  List ** lists = malloc(n * sizeof(List*));
  for (int q = 0; q<n;q++) lists[q] = emptylist();
  int i = 0;
  Iterator * iter = init_iterator(list);
  while(hasNext(iter)){
    Node * nn = next_node(iter);
    add_node(nn,lists[i]);
    if(i == (n - 1)) i = 0;
    else i++;
  }
  for (int q = 0; q < n; q++) lists[q]->tail->next = NULL;
  free(list);
  destroy_iterator(iter);
  return lists;
}

List * merge(List * l1, List * l2){
  if (l1 == NULL) return l2;
  else if (l1->head == NULL){
    free(l1);
    return l2;
  }
  else if (l2 == NULL) return l1;
  else if (l2->head == NULL){
    free(l2);
    return l1;
  }
  l1->tail->next = l2->head;
  l1->tail = l2->tail;
  free(l2);
  return l1;
}
//Zips n lists that were split using split_mod.
List * zip(int n, List ** lists){
  List * output = emptylist();
  Iterator ** iters = malloc(n * sizeof(Iterator *));
  int i;
  for (i = 0; i < n; i++) iters[i] = init_iterator(lists[i]);

  while(hasNext(iters[0])){
    for(i = 0; i < n; i++) {
      if(hasNext(iters[i])) add(next(iters[i]), output);
    }
  }
  for(i=0;i<n;i++){
    destroy_iterator(iters[i]);
    destroy_soft(lists[i]);
  }
  free(iters);
  return output;
}
void check_sequence(List * list){
  Iterator * iter = init_iterator(list);

  int l1num = 0;
  int l2num = 0;

  while(hasNext(iter)){
    chunk_t* c = next(iter);
    if (c->sequence.l1num < l1num || (c->sequence.l2num < l2num && c->sequence.l1num == l1num) || c->sequence.l2num == l2num){
      printf("Error in sequence: old l1num = %d, old l2num = %d, neuw l1num = %d, neuw l2num = %d\n",l1num, l2num, c->sequence.l1num,c->sequence.l2num);
    }
    l1num = c->sequence.l1num;
    l2num = c->sequence.l2num;
  }

  destroy_iterator(iter);
}

/*
* Takes 2 lists of chunks that are sorted by l1num and l2number
* Returns a new list that is sorted by l1num and l2num
* Cleans up the input lists
*/
List * sorted_merge(List * l1, List * l2){
      //If one of the lists is empty, simply return the other list
      //Clean up the empty list
      if(l1 == NULL) return l2;
      else if(l1->head == NULL){
        free(l1);
        return l2;
      }
      else if (l2 == NULL) return l1;
      else if (l2->head == NULL){
        free(l2);
        return l1;
      }
      //Create output list and temporary variables
      List * output = emptylist();
      Iterator * iter1 = init_iterator(l1);
      Iterator * iter2 = init_iterator(l2);
      Node * node1 = next_node(iter1);
      Node * node2 = next_node(iter2);
      chunk_t * chunk1 = node1->data;
      chunk_t * chunk2 = node2->data;

      //While both lists are not empty, 'zip' the lists by comparing elements
      int cont = 1;
      while(cont){
        if (output->tail == node1){
          node1 = next_node(iter1);
          chunk1 = node1->data;
        }
        else if (output->tail == node2){
          node2 = next_node(iter2);
          chunk2 = node2->data;
        }
        //Instead of moving chunks, we move entire nodes for efficiency.
        if(chunk1->sequence.l1num < chunk2->sequence.l1num
          || (chunk1->sequence.l1num == chunk2->sequence.l1num
          && chunk1->sequence.l2num < chunk2->sequence.l2num)) {
            add_node(node1,output);
            if(!hasNext(iter1)){
              cont = 0;
              add_node(node2,output);
            }
          }
        else {
          add_node(node2,output);
          if(!hasNext(iter2)){
            cont = 0;
            add_node(node1,output);
          }
        }
      }
      //Add the entire remainder of the non-empty list at once.
      if (hasNext(iter1)) add_nodes(next_node(iter1), l1->tail, output);
      else if (hasNext(iter2)) add_nodes(next_node(iter2), l2->tail, output);

      destroy_iterator(iter2);
      destroy_iterator(iter1);
      free(l1);
      free(l2);
      return output;
  }
