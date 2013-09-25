/* Author: Don Smith
 * This program runs as two ST threads in a single Linux process.
 * One of the threads is a "ping" (outputs a string) and the other
 * is a "pong" (outputs a string only after the ping thread has
 * output its string).  The ping thread executes a loop for a specified 
 * number of iterations.  At each iteration, the thread outputs its
 * "ping" message string to stdout and then pauses execution (ST 
 * library call to sleep) for a random number of seconds.  The pong
 * thread outputs its message string only when it receives a synchronization
 * indicator sent by the ping thread to the bounded buffer after sleeping.
 * Thus, the bounded buffer becomes a synchronization mechanism that
 * allows the ping and pong threads to coordinate their actions.  The 
 * bounded buffer is implemented as a small Abstract Data Type in a
 * pre-complied object file that is linked with this program (see the
 * lab 5 tutorial for more about abstract data types).
 *
 * When the ping thread has completed its specified number of loop 
 * iterations, it sets a global boolean variable and sends a synchronization
 * indicator to the pong thread through the bounded buffer.  It then
 * terminates.  The pong thread terminates when it receives from the
 * bounded buffer and finds the boolean variable set to true.
 * There are five positional parameters to this program in the following 
 * order:
 *  - the ping thread output string
 *  - the pong thread  output string
 *  - the number of loop iterations for ping thread
 *  - the maximum random sleep time (seconds) between loop interations
 *  - the size of the bounded buffer.
 */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>

/* includes for new C features not shown before
 * (boolean data type and assert macro)
 */

#include <stdbool.h>
#include <assert.h>


#include "st.h"  /* required to compile with the threads library */
#include "BB.h"  /* required to compile with the bounded buffer */

#define NUM_ARGS 6  /* program name + 5 positional parameters */
#define ERR_RETURN -1
#define MAX_SLEEP 60  /* don't allow sleeping for more than a minute */

bool quit = false; /* a global variable shared by threads */

/* type definition for a structure to be used to initialize
 * each thread
 */

typedef struct
{
  const char *my_call;
  unsigned int my_seed;
  int count;
  int max_sleep;
} thread_init;

void *ping(void *s); /* function prototypes for functions executed as threads */
void *pong(void *s);

int main(int argc, char *argv[])
{
  int loop_count;
  int sleep_secs;
  int buffer_size;

  const int ping_arg = 1;
  const int pong_arg = 2;
  const int loop_arg = 3;
  const int sleep_arg = 4;
  const int size_arg = 5;

  thread_init ping_init;
  thread_init pong_init;

  /* validity check parameter count */
  if (argc != NUM_ARGS)
    {
      printf("Bad parameter count %d\n", argc);
      printf("Positional parameters: ping & pong strings, loop count, sleep limit, buffer size\n");
      exit (ERR_RETURN);
    }

  /* the loop count is the third positional parameter */
  loop_count = atoi(argv[loop_arg]);  /* atoi converts string to integer */

  /* the sleep limit seconds is the fourth positional */
  sleep_secs = atoi(argv[sleep_arg]);  /* atoi converts string to integer */
  if (sleep_secs > MAX_SLEEP)
    {
      printf("Sleep of %d exceeds maximum %d\n", sleep_secs, MAX_SLEEP);
      exit (ERR_RETURN);
    }
 
  /* the buffer size is the fifth positional */
  buffer_size = atoi(argv[size_arg]);  /* atoi converts string to integer */

  /* This call is required to initialize the thread library */

  if (st_init() < 0) {
    perror("st_init");
    exit(1);
  }

  /* This call is required to initialize the bounded buffer.
   * The size (in characters) may be set to any value between
   * one and MAX_BUFFER (defined in BB.h).  The call must be
   * made AFTER the st_init() call and BEFORE any producers or
   * consumers using the bounded buffer are started. 
   */

  if (BB_init(buffer_size) != 0)
    {
     printf("Buffer size initializer %d not in valid range\n", buffer_size);
     exit(-1);
    } 
  
  /* Create a separate thread for each of ping and pong */

  /* create an initializer for pong thread */
  pong_init.my_call = argv[pong_arg];
  pong_init.my_seed = 0;  /* not used by pong */
  pong_init.count = 0;  /* not used by pong */
  pong_init.max_sleep = 0;  /* not used by pong */
  
  /* start it first so it will block first 
   * The first parameter is the initial function executed in 
   * the thread.  The second is the only allowed parameter to
   * the initial function and must be a pointer to any type.
   */

  if (st_thread_create(pong, &pong_init, 0, 0) == NULL) 
     {
      perror("st_thread_create");
      exit(1);
     }

  /* create an initializer for ping thread */
  ping_init.my_call = argv[ping_arg];
  ping_init.my_seed = (unsigned int) getpid();  /* unique random seed each run */
  ping_init.count = loop_count;
  ping_init.max_sleep = sleep_secs;

  if (st_thread_create(ping, &ping_init, 0, 0) == NULL) 
     {
      perror("st_thread_create");
      exit(1);
     }
  /* causes the main thread to exit with others still running */
  printf("Main thread exiting\n");
  fflush(stdout);
  st_thread_exit(NULL);
}


/* The initial function executed in a thread must have 
 * a function declaration like the following -- note
 * the use of void * (pointer to any type).
 */

void *ping(void *s)
{
  thread_init *p = s;  /* p has the proper type instead of void */
  int i;
  int sleep_time;
  char sync[1] = {'S'};
  unsigned int rand_seed = p->my_seed;

  for (i = 0; i < p->count; i++)
     {
      sleep_time = 1 + (rand_r(&rand_seed) % p->max_sleep);
      printf("%s (Delay %d seconds before & after Pong thread)\n", p->my_call, sleep_time);
      st_sleep(sleep_time);
      BB_put(sync);
      st_sleep(sleep_time);   /* give pong a chance to run */ 
     }
  quit = true;
  printf("Ping exiting\n");
  BB_put(sync);
  fflush(stdout);
  st_thread_exit(NULL);
}

void *pong(void *s)
{
  thread_init *p = s;  /* p has the proper type instead of void */
  char sync_char[1];
  bool forever = true;

  while (forever)
     {
      BB_get(sync_char);

      if (quit)
         break;

      assert(sync_char[0] == 'S');

      printf("%s\n", p->my_call);
     }
  printf("Pong exiting\n");
  fflush(stdout);
  st_thread_exit(NULL);
}

