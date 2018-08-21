#include<stdlib.h>
#include <time.h>
#include <stdio.h>
 #include <sys/mman.h>

int main(){
  srand(time(NULL));
  for(int i = 0; i < 1000; i++){
    void * c = malloc(5000);



    for(int j = 0; j<500;j++) j = j++;

    madvise(c, 5000, MADV_NORMAL);


  }
}
