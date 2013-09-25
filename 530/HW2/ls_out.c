#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <stdarg.h>
#include <string.h>
#include <ctype.h>
//#include "apue.h"


#define maxArraySize 2048 //array size for input lenghOfInput

#define ERR_RETURN -1
int parseString(int inputLength, char *lineInput, char ***argv)
{
	int argc = 0;
	char *formattedBuffer = (char*) malloc((inputLength+1) * sizeof(char));	
	(*argv) = (char**) malloc((maxArraySize+1) * sizeof(char**)); //subject to adjust
	
	int inputIndex = 0,formattedIndex = 0;	
	if (!isspace(lineInput[0])) 
	{
		formattedBuffer[0] = lineInput[0];
		(*argv)[argc++] = & formattedBuffer[0];
		formattedIndex = 1;
	}
	for(inputIndex = 1; inputIndex < inputLength;inputIndex++)
	{
		if (!isspace(lineInput[inputIndex]))
		{
			formattedBuffer[formattedIndex] = lineInput[inputIndex];
			if (isspace(lineInput[inputIndex-1]))
			{
				(*argv)[argc++] = &formattedBuffer[formattedIndex];
			}
			formattedIndex++;
		}
		else {
			if (!isspace(lineInput[inputIndex-1])){
				formattedBuffer[formattedIndex++] = '\0';
			}
		}
	}
	formattedBuffer[formattedIndex]='\0';
	argv[argc] = NULL;
	return argc;
}

	
	char	*env_init[] = { "USER=unknown", "PATH=/tmp", NULL };
	
int main()
{
	char *args[] = {"ls", "-lrt", NULL};
	execvp(args[0], args);
	exit(0);
	return 0;
}
