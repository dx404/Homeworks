#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <stdarg.h>
#include <string.h>
#include <ctype.h>
//ARG_MAX 	http://www.in-ulm.de/~mascheck/various/argmax/

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

int main (int argc, const char * argv[]) {
    // insert code here...
	pid_t cpid, term_pid;
	int child_status;	
	//char commmmmmand[]="ls -lrt";
	
	char welcomeWords[] = "Welcome to Duo's HW2";
	char outputPromptWords[] = "Please enter the file name to be executed followd by any necessary parameters to facilitate the execution: \n  (Up to 1024 characters)";
	char lineOfInput[maxArraySize+1] = "";
	int lenghOfInput = 0;
	int flagEOF = 0;// O is non EOF line, 1 is EOF line. 
	
	printf("%s\n",welcomeWords);
	printf("FILENAME_MAX = %d\n",FILENAME_MAX);
	
	//getline(lineOfInput,stdin);
	
	int i=0;
	while (!flagEOF) {
		i=0;
		lenghOfInput = 0;
		printf("\n--0-- %s\n",outputPromptWords);
		printf("--0-- Current process: %d is serving you: \n", getpid());
		while (i < maxArraySize) {
			if (fscanf(stdin,"%1c",&lineOfInput[i])==EOF) {
				flagEOF = 1; //1 for EOF line;
				break;
			}
			if (lineOfInput[i]=='\n') {
				flagEOF = 0; //0 for non-EOF line;
				break;
			}
			i++;
		}
		//fgets(lineOfInput, sizeof(lineOfInput), stdin);		
		if (i >= maxArraySize) {
			printf("== Friendly Reminder 1==\n Maxium array size is reached. This line of input has been DISCARDED and WON'T be forward for execution\n");
			lineOfInput[0] = 0;
			i = 0;
			lenghOfInput = 0;
			continue;
		}		
		lineOfInput[i]=0; //stop at length
		if (flagEOF) {
			printf("== Friendly Reminder 2==\n EOF is reached at Position %d in this line\n This line of input --\"%s\"--\n has been DISCARDED and WON'T be forward for execution\n Program Quit\n", i,lineOfInput);
			lineOfInput[0] = '\0';
			i = 0;
			lenghOfInput = 0;
			break;
		}
		else {
			lenghOfInput = i;	
		}
		if (lenghOfInput == 0) {
			printf("== Friendly Reminder 3==\n No input, Please re-enter\n");
			continue;
		}		
		printf("\n --1-- You just input %d characters. They are:\n%s\nThe above characters will be forwarded to a child process. \n",lenghOfInput,lineOfInput);
				
		cpid = fork(); //fork 
		
		if (cpid == -1) {
			perror("fork");
			exit(ERR_RETURN);
		}
		if (cpid == 0) { //child process
			printf("--2-- Welcome your arrival at child process, my PID is %d, my parent is %d\n",getpid(),getppid());
			char **formattedArgv;
			int formattedArgvWordLength = parseString(lenghOfInput, lineOfInput, &formattedArgv);
			printf("Hello, I have parsed %d word(s), please check the following: \n",formattedArgvWordLength);
			for(i = 0 ; i<formattedArgvWordLength; i++){
				printf("--4-- Word %d: %s \n",i, formattedArgv[i]);
			}						
			execvp(formattedArgv[0], formattedArgv);
			_exit(0);
		}
		else { //parent process
			term_pid = waitpid(cpid, &child_status,0);
			if (term_pid == -1) {
				perror("======= Waitpid Failed ==========");
			}
			else {
				printf("--5-- Child Complete, Back to parent process: %d \n", getpid());
			}

		}

	}
	
		
    
    return 0;
}