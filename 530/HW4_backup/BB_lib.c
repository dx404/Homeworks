#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include "st.h"  /* required to compile with the threads library */
#include "BB.h"  /* required to compile with the bounded buffer */


static char the_buffer[MAX_BUFFER];
static int limit;
static int put_index;
static int get_index;
static int used;
static st_cond_t not_full;
static st_cond_t not_empty;


int BB_init(int size)
{
  if ((size < 1) ||
      (size > MAX_BUFFER))
    return (-1);

  not_full = st_cond_new();  
  not_empty = st_cond_new();  

  limit = size;   /* size of buffer */
  put_index = 0;  /* index of put character */
  get_index = 0;  /* index of get character */
  used = 0;
  return (0);
}

void BB_put(char *s)
{
  if (used == limit)
     st_cond_wait(not_full);
  the_buffer[put_index] = *s;
  put_index = (put_index + 1) % limit;
  used = used + 1;
  st_cond_signal(not_empty);
  st_sleep(0); 
}

void BB_get(char *s)
{
  if (used == 0)
     st_cond_wait(not_empty);
  *s = the_buffer[get_index];
  get_index = (get_index + 1) % limit;
  used = used - 1;
  st_cond_signal(not_full);
  st_sleep(0); 
}
