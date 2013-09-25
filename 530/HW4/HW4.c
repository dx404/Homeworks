#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <stdio.h>
#include <stdbool.h>
#include <assert.h>

#include "st.h"
#include "semaphore.h"

#include "buffer.h"

#define BUFFER_SIZE 30
#define OUTPUT_SIZE 80
#define WAIT_TIME 1

const char ruler[]="1=========2=========3=========4==ruler==5=========6=========7=========8=========";

// used to initialize Producer and consumer 
typedef struct
{
	circularBuffer *buffer_C; //Pointer To Consumer
	circularBuffer *buffer_P; //Pointer To Producer 
}thread_Producer_Consumer_init; 

//Thread Function Handlers 
void *inputProcessing(void *s);
void *carriageToSpace(void *s);
void *starsToCaret(void *s);
void *outputProcessing(void *s);

int main (int argc, const char * argv[]) {
	thread_Producer_Consumer_init inputProcessing_init;
	thread_Producer_Consumer_init carriageToSpace_init;
	thread_Producer_Consumer_init starsToCaret_init;
	thread_Producer_Consumer_init outputProcessing_init;
	if (st_init()<0) {
		perror("st_init");
		exit(1);
	}
	
	circularBuffer buf1, buf2, buf3;
	newBuffer(&buf1, BUFFER_SIZE); //for buffer inputProcessing & carriageToSpace
	newBuffer(&buf2, BUFFER_SIZE); //for buffer carriageToSpace & starsToCaret
	newBuffer(&buf3, BUFFER_SIZE); //for buffer starsToCaret & outputProcessing
		
	inputProcessing_init.buffer_C = NULL;
	inputProcessing_init.buffer_P = &buf1;
	
	carriageToSpace_init.buffer_C = &buf1;
	carriageToSpace_init.buffer_P = &buf2;	
	
	starsToCaret_init.buffer_C = &buf2;
	starsToCaret_init.buffer_P = &buf3;
	
	outputProcessing_init.buffer_C = &buf3;
	outputProcessing_init.buffer_P = NULL;
	
	if (st_thread_create(inputProcessing, &inputProcessing_init, 0, 0)==NULL) {
		perror("st_thread_create \n");
		exit(1);
	}
	
	if (st_thread_create(carriageToSpace, &carriageToSpace_init, 0, 0)==NULL) {
		perror("st_thread_create \n");
		exit(1);
	}
	
	if (st_thread_create(starsToCaret, &starsToCaret_init, 0, 0)==NULL) {
		perror("st_thread_create \n");
		exit(1);
	}
	
	if (st_thread_create(outputProcessing, &outputProcessing_init, 0, 0)==NULL) {
		perror("st_thread_create \n");
		exit(1);
	}
	
	fflush(stdout);
	st_thread_exit(NULL);
	
    return 0;	
}

void *inputProcessing(void *s){
	thread_Producer_Consumer_init *p = s;
	char inputReceiver = 0;
	printf("%s\n%s\n%s\n",ruler,"Welcome to Duo's HW4 (duo.zhao@unc.edu) ",ruler);	
	printf("Thread_1: inputProcessing \n");
	printf("Please Enter: \n");
	
	while (1) {
		if (fscanf(stdin, "%1c", &inputReceiver)==EOF) {
			inputReceiver = EOF;
		}
		depositCirBuf(p->buffer_P, inputReceiver); //Semaphore and mutex included
		if (inputReceiver==EOF) {
			break;
		}
		if (inputReceiver=='\n') {
			st_sleep(WAIT_TIME);
			printf("Please Continue to Enter: \n");
		}
	}
	printf("Thread 1: inputProcessing Existing\n");
	st_thread_exit(NULL);	
}

void *carriageToSpace(void *s){
	printf("Thread_2: carriageToSpace \n");
	thread_Producer_Consumer_init *p = s;
	char processingTank;
	while (1) {
		withdrawalCirBuf(&processingTank, p->buffer_C);
		if (processingTank =='\n') {
			processingTank = ' ';
		}
		depositCirBuf(p->buffer_P, processingTank);
		if (processingTank==EOF) {
			break;
		}
	}
	printf("Thread 2: carriageToSpace Existing\n");
	st_thread_exit(NULL);
}

void *starsToCaret(void *s){
	printf("Thread_3: starsToCaret \n");
	thread_Producer_Consumer_init *p = s;
	bool isStarLast = false;
	char processingTank;
	
	while (1) {
		withdrawalCirBuf(&processingTank, p->buffer_C);
		if (isStarLast) { //Last input is sta
			if (processingTank == '*') {
				processingTank = '^';
			}
			else {
				depositCirBuf(p->buffer_P, '*');
			}
			isStarLast = false;
		}
		else if(processingTank == '*') {
				isStarLast = true;
				continue;
		}
		depositCirBuf(p->buffer_P, processingTank);
		if (processingTank == EOF) {
			break;
		}	
	}
	printf("Thread 3: starsToCaret Existing\n");
	st_thread_exit(NULL);	
}

void *outputProcessing(void *s){
	printf("Thread_4: outputProcessing \n");
	thread_Producer_Consumer_init *p = s;
	char processingTank[2 * OUTPUT_SIZE];
	int tankLevel = 0;
	while (1) {
		for (tankLevel = 0; tankLevel < OUTPUT_SIZE; tankLevel++) {
			withdrawalCirBuf(&processingTank[tankLevel], p->buffer_C);
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
	printf("Thread 4: outputProcessing Existing\n");
	st_thread_exit(NULL);
}
