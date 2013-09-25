/* 
 * Author: Don Smith
 * This program consists of three processes (the parent process
 * and two child processes created by the parent).  The parent
 * process reads stdin and produces characters that it passes 
 * through a pipe to the first child process which will process 
 * the characters (convert to uppper case and replace newline with 
 * blanks).  The first child then passes its output characters 
 * through a pipe to the second child process which outputs 
 * only lines of LINE_SIZE characters to stdout.
 */

#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <stdarg.h>
#include <ctype.h>
#include <stdbool.h>

#define READ 0   /* pipe descriptor 0 is for reading */
#define WRITE 1  /* pipe descriptor 1 is for writing */
#define LINE_SIZE 10 /* the number of character in output lines */

void do_child1(void);
void do_child2(void);

int pipe1[2]; /* from parent to child #1 */
int pipe2[2]; /* from child #1 to child #2 */

int main(int argc, char *argv[])
{
  pid_t child1_id, child2_id, term_pid;
  int chld_status; 
  int c;
  char parent_data;

  /* create the pipe that connects the parent process to
   * child prcess 1
   */
  if (pipe(pipe1) == -1)
     { 
      perror("pipe 1:"); 
      exit(EXIT_FAILURE);
     }

  /* create the pipe that connects child process 1 to
   * child process 2
   */
  if (pipe(pipe2) == -1)
     { 
      perror("pipe 2:"); 
      exit(EXIT_FAILURE);
     }

  /* create the first child; it will handle upper case and newline */
  child1_id = fork();
  if (child1_id == -1) 
     {
      perror("fork for child 1 process:"); 
      exit(-1); 
     }
  if (child1_id == 0) 
     {            
      /* Code executed by child #1 */
      do_child1();
     }      
  else 
     {
      /* Code executed by parent */
      /* create the second child; it will handle fixed line size output */
      child2_id = fork();
      if (child2_id == -1) 
         {
          perror("fork for child 2 process:"); 
          exit(-1); 
         }
      if (child2_id == 0) 
         {            
          /* Code executed by child #2 */
	  do_child2();
	  }
       else
	  {
	   /* the parent process reads stdin and 
            * passes characters to the child 1 process.
            */

           /* close unused duplicates of pipe descriptors left after fork */
	   close(pipe1[READ]); /* only writes to pipe 1 */
           close(pipe2[READ]); /* does not use pipe 2 */
           close(pipe2[WRITE]); 
 
           while ((c = getc(stdin)) != EOF)
              {
               parent_data = (char) c;
               if (write(pipe1[WRITE], &parent_data, 1) == -1)
               /* for pipe writes, a return of -1 indicates error, otherwise success */
		  {
		   perror("Parent pipe write:");
                   break;
                  }
              }
          close(pipe1[WRITE]); /* close on write end gives EOF at read end */

          /* wait for the child processes to terminate and determine exit status */
          /* this is necessary to prevent the accumulation of "Zombie" processes */

          term_pid = waitpid(child1_id, &chld_status, 0);
          if (term_pid == -1) 
              perror("waitpid"); 
          else
	     {
              if (WIFEXITED(chld_status)) 
	         printf("PID %d exited, status = %d\n", child1_id, WEXITSTATUS(chld_status));
              else
	         printf("PID %d did not exit normally\n", child1_id);
             }

          term_pid = waitpid(child2_id, &chld_status, 0);
          if (term_pid == -1) 
              perror("waitpid"); 
          else
	     {
              if (WIFEXITED(chld_status)) 
	         printf("PID %d exited, status = %d\n", child2_id, WEXITSTATUS(chld_status));
              else
	         printf("PID %d did not exit normally\n", child2_id);
             }

          printf("Parent process terminating.\n");
          exit(0);
	 } 
     }
  return(0);
}

void do_child1(void)
{
 char child1_data;
 int rc;

/* this child process will be handle uppercase and newline replacement 
 * and pass its output on to the child 2 process. 
 */

 /* close unused duplicates of pipe descriptors left after fork */
 close(pipe1[WRITE]); /* only reads pipe 1 */
 close(pipe2[READ]);  /* only writes pipe 2 */

 while ((rc = read(pipe1[READ], &child1_data, 1)) > 0)
     /* for pipe reads, return of -1 indicates error, 0 indicates EOF, and
      * > 0 is the number of bytes read.
      */
   {
    if (isalpha(child1_data))
	child1_data = (char)toupper((int)child1_data);
    else
       if (child1_data == '\n')
	   child1_data = ' ';

    if (write(pipe2[WRITE], &child1_data, 1) == -1)
       /* for pipe writes, a return of -1 indicates error, otherwise success */
       {
	perror("Child 1 pipe write:");
        break;
       }
   } /* end while on pipe reading */

 if (rc == -1)  /* error instead of EOF */
    perror("Child 1 pipe read:");

 /* EOF or error, close on write end of pipe gives EOF at read end */
 close(pipe2[WRITE]); 
 exit (0);
}

void do_child2(void)
{
 int i, rc;
 bool forever = true;
 char child2_data;
 char out_chars[LINE_SIZE + 1];

 /* this child will handle outputing only LINE_SIZE character lines.
  */
 
 /* close unused duplicates of pipe descriptors left after fork */
 close(pipe2[WRITE]); /* only reads pipe 2 */
 close(pipe1[READ]);  /* does not use pipe 1 */
 close(pipe1[WRITE]);
 
 while (forever)
    {
     for (i = 0; i < LINE_SIZE; i++)
        {
         if ((rc = read(pipe2[READ], &child2_data, 1)) < 1)
	    /* for pipe reads, return of -1 indicates error, 0 indicates EOF, and
             * > 0 is the number of bytes read.
             */
            {
             if (rc == -1)
	         perror("Child 2 pipe read:");
             exit (0);
            }
         else
            out_chars[i] = child2_data;
	}
     out_chars[LINE_SIZE] = '\0';
     fputs(out_chars, stdout);
     fputs("\n", stdout);
    }
 exit(0);
}
