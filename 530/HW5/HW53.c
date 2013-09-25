#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <stdarg.h>
#include <ctype.h>
#include <stdbool.h>
#include <signal.h>

#define READ 0
#define WRITE 1
#define OUTPUT_SIZE 80
#define WAIT_TIME 1

int main (int argc, const char * argv[]) {
	bool isStarLast = false;
	char processingTank;
	const char star = '*';
	
	while (1) {
		if(fscanf(stdin, "%1c", &processingTank) == EOF){
			processingTank = EOF;
		}
		if (isStarLast) { //Last input is starLast
			if (processingTank == '*') {
				processingTank = '^';
			}
			else {
				if (fprintf(stdout, "%c", star) < 0) {
					perror("starsToCaret Error -1: \n");
					break;
				}
			}
			isStarLast = false;
		}
		else if(processingTank == '*') {
			isStarLast = true;
			continue;
		}
		if (fprintf(stdout, "%c", processingTank) < 0) {
			perror("starsToCaret Error -2: \n");
			break;
		}
		if (processingTank == EOF) {
			break;
		}	
	}
    return 0;
}