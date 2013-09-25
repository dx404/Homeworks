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

const char ruler[]="1=========2=========3=========4==ruler==5=========6=========7=========8=========";

int main (int argc, const char * argv[]) {
	char processingTank[2 * OUTPUT_SIZE];
	int tankLevel = 0;
	while (1) {
		for (tankLevel = 0; tankLevel < OUTPUT_SIZE; tankLevel++) {
			//withdrawalCirBuf(&processingTank[tankLevel], p->buffer_C);
			//fscanf(stdin, "%1c", &processingTank[tankLevel]) == EOF
			
			if(fscanf(stdin, "%1c", &processingTank[tankLevel]) == EOF){
				processingTank[tankLevel] = EOF;
			}
			
			if (processingTank[tankLevel]==EOF) {
				tankLevel++;
				break;
			}
		}
		processingTank[tankLevel]='\0';
		if (processingTank[tankLevel-1]==EOF) {
			break;
		}
		if (tankLevel>=OUTPUT_SIZE) {
			printf("Output: \n%s\n%s\n%s\n",ruler, processingTank, ruler);
			tankLevel = 0;
			continue;
		}
		printf("ERROR:===IMPOSSIBLE TO BE HERE\n");
	}
	return 0;
}
