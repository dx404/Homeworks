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

const char ruler[]="1=========2=========3=========4==ruler==5=========6=========7=========8=========";

int inputProcessing(pid_t child_pid);
int carriageToSpace(pid_t child_pid);
int starsToCaret(pid_t child_pid);
int outputProcessing(void); 

int pipe1[2];
int pipe2[2];
int pipe3[2];

int main (int argc, const char * argv[]) {
	pid_t inputProcessing_id, carriageToSpace_id, starsToCaret_id, outputProcessing_id;
	
	if (pipe(pipe1) == -1) {
		perror("pipe 1 Error: \n");
		exit(EXIT_FAILURE);
	}
	if (pipe(pipe2) == -1) {
		perror("pipe 2 Error: \n");
		exit(EXIT_FAILURE);
	}
	if (pipe(pipe3) == -1) {
		perror("pipe 3 Error: \n");
		exit(EXIT_FAILURE);
	}
	
	inputProcessing_id = getpid();
	
	carriageToSpace_id = fork();
	if (carriageToSpace_id == -1) {
		perror("fork for carriageToSpace process failed\n");
		exit(-1);
	}
	if (carriageToSpace_id != 0) { //parent process
		inputProcessing(carriageToSpace_id);
	}
	else { //child process
		starsToCaret_id = fork();
		if (starsToCaret_id == -1) {
			perror("fork for starsToCaret process failed\n");
			exit(-1);
		}
		if (starsToCaret_id != 0) {
			carriageToSpace(starsToCaret_id);
		}
		else {
			outputProcessing_id = fork();
			if (outputProcessing_id == -1) {
				perror("fork for outputProcessing process failed\n");
				exit(-1);
			}
			if (outputProcessing_id != 0) {
				starsToCaret(outputProcessing_id);
			}
			else {
				outputProcessing();
			}
		}		
	}	
    return 0;
}

int inputProcessing(pid_t child_pid){
	printf("PID: %d Started\n", getpid());
	pid_t term_pid;
	int child_status;
	
	close(pipe1[READ]);
	//close(pipe1[WRITE]);
	close(pipe2[READ]);
	close(pipe2[WRITE]);
	close(pipe3[READ]);
	close(pipe3[WRITE]);
	
	char inputReceiver = 0;
	printf("%s\n%s\n%s\n",ruler,"Welcome to Duo's HW5 (duo.zhao@unc.edu) ",ruler);	
	printf("Please Enter: \n");
	
	while (1) {
		if (fscanf(stdin, "%1c", &inputReceiver) == EOF) {
			inputReceiver = EOF;
		}
		if (write(pipe1[WRITE], &inputReceiver, 1) == -1) {
			perror("inputProcessing Error: \n");
			break;
		}
		if (inputReceiver==EOF) {
			break;
		}
		if (inputReceiver=='\n') {
			printf("Please Continue to Enter: \n");
		}
	}	
	close(pipe1[WRITE]);
	term_pid = waitpid(child_pid, &child_status, 0);
	if (term_pid == -1) {
		perror("waitpid");
	}
	else {
		if (WIFEXITED(child_status)) 
		{printf("PID %d exited, status = %d\n", child_pid, WEXITSTATUS(child_status));}
		else
		{printf("PID %d did not exit normally\n", child_pid);}
	}
	printf("PID %d exited, status = %d\n", getpid(), 0); //for adjusting
	exit(0);	
	return 0;
}
int carriageToSpace(pid_t child_pid){
	printf("PID: %d Started\n", getpid());
	//close(pipe1[READ]);
	close(pipe1[WRITE]);
	close(pipe2[READ]);
	//close(pipe2[WRITE]);
	close(pipe3[READ]);
	close(pipe3[WRITE]);
	
	pid_t term_pid;
	int child_status;
	
	char processingTank;
	
	while (1) {
		if(read(pipe1[READ], &processingTank, 1) == 0){
			processingTank = EOF;
		}
		if (processingTank =='\n') {
			processingTank = ' ';
		}
		if (write(pipe2[WRITE], &processingTank, 1) == -1) {
			perror("carriageToSpace Error: \n");
			break;
		}
		if (processingTank==EOF) {
			break;
		}
	}
	close(pipe1[READ]);
	close(pipe2[WRITE]);
	
	term_pid = waitpid(child_pid, &child_status, 0);
	if (term_pid == -1) {
		perror("waitpid");
	}
	else {
		if (WIFEXITED(child_status)) 
		{printf("PID %d exited, status = %d\n", child_pid, WEXITSTATUS(child_status));}
		else
		{printf("PID %d did not exit normally\n", child_pid);}
	}	
	exit(0);	
	return 0;
}

int starsToCaret(pid_t child_pid){
	printf("PID: %d Started\n", getpid());
	close(pipe1[READ]);
	close(pipe1[WRITE]);
	//close(pipe2[READ]);
	close(pipe2[WRITE]);
	close(pipe3[READ]);
	//close(pipe3[WRITE]);
	bool isStarLast = false;
	char processingTank;
	const char star = '*';
	
	pid_t term_pid;
	int child_status;
	
	while (1) {
		if(read(pipe2[READ], &processingTank, 1) == 0){
			processingTank = EOF;
		}
		
		if (isStarLast) { //Last input is star
			if (processingTank == '*') {
				processingTank = '^';
			}
			else {
				if (write(pipe3[WRITE], &star, 1) == -1) { //42 == '*'
					perror("starsToCaret Error -01: \n");
					break;
				}
			}
			isStarLast = false;
		}
		else if(processingTank == '*') {
			isStarLast = true;
			continue;
		}
		if (write(pipe3[WRITE], &processingTank, 1) == -1) {
			perror("starsToCaret Error -2: \n");
			break;
		}
		if (processingTank == EOF) {
			break;
		}	
	}
	
	close(pipe2[READ]);
	close(pipe3[WRITE]);
	
	term_pid = waitpid(child_pid, &child_status, 0);
	if (term_pid == -1) {
		perror("waitpid");
	}
	else {
		if (WIFEXITED(child_status)) 
		{printf("PID %d exited, status = %d\n", child_pid, WEXITSTATUS(child_status));}
		else
		{printf("PID %d did not exit normally\n", child_pid);}
	}	
	exit(0);
	return 0;
}
int outputProcessing(void){
	printf("PID: %d Started\n", getpid());
	close(pipe1[READ]);
	close(pipe1[WRITE]);
	close(pipe2[READ]);
	close(pipe2[WRITE]);
	//close(pipe3[READ]);
	close(pipe3[WRITE]);
	
	char processingTank[2 * OUTPUT_SIZE];
	int tankLevel = 0;
	while (1) {
		for (tankLevel = 0; tankLevel < OUTPUT_SIZE; tankLevel++) {
			if(read(pipe3[READ], &processingTank[tankLevel], 1) == 0){
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
	close(pipe3[READ]);	
	exit(0);
	return 0;
}
