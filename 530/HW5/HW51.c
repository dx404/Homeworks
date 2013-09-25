#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <stdarg.h>
#include <ctype.h>
#include <stdbool.h>

#define READ 0
#define WRITE 1
#define OUTPUT_SIZE 80

int main (int argc, const char * argv[]) {
	char inputReceiver = 0;
	while (1) {
		if (fscanf(stdin, "%1c", &inputReceiver) == EOF) {
			inputReceiver = EOF;
		}
		if (fprintf(stdout, "%c", inputReceiver) < 0) {
			perror("inputProcessing Error: \n");
			break;
		}		
		if (inputReceiver==EOF) {
			break;
		}
		
		if (inputReceiver=='\n') {
			continue;
		}
	}	
    return 0;
}