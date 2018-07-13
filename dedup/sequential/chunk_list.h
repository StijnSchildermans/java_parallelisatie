#ifndef LINKEDLIST_HEADER
#define LINKEDLIST_HEADER

#include <stdio.h>
#include <stdlib.h>
#include "dedupdef.h"
//#include "iterator.h"

typedef struct node{
  chunk_t * data;
  struct node * next;
} Node;

typedef struct list {
  Node * head;
  Node * tail;
} List;

List * emptylist();
void add(chunk_t * elem, List * list);
chunk_t * get(int index, List * list);
void add_node(Node * node, List * list);
void add_nodes(Node * head, Node * tail, List * list);
int length(List * list);
void destroy(List * list);
List ** split(int n, List * list);
List ** split_mod(int n, List * list);
List * zip(int n, List** lists);
List * merge(List * l1, List * l2);
void destroy_soft(List * list);
List * sorted_merge(List * l1, List * l2);

#endif