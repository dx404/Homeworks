#include <sys/types.h>
#include <sys/wait.h>
#include <sys/stat.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <stdarg.h>
#include <string.h>
#include <ctype.h>
#include <sys/types.h> 
#include <signal.h>

#define maxArraySize 1024 //array size for input lenghOfInput //ARG_MAX 	
#define ERR_RETURN -1

static void handler(int signal);
int parseString(int inputLength, char *lineInput, char ***argv);
int isPath(char *pathName);
int isFileExistInPaths(char *fileName, char **pathPool, char *fullFath);
int parseEnvPATH2PathPool(char *envPATH, char ***parsedpathPool);

int main (int argc, const char * argv[]) {
    char ruler[]="1=========2=========3=========4==ruler==5=========6=========7=========8=========";
	char welcomeWords[] = "Welcome to Duo's HW2 (duo.zhao@unc.edu)";
	char outputPromptWords[] = "Please enter the file/Path name to be executed\n followd by any necessary parameters to facilitate the execution:\n";
	
	pid_t cpid, term_pid;
	int child_status;		
	char lineOfInput[maxArraySize+1] = "";
	int lenghOfInput = 0;
	int flagEOF = 0;// O is non EOF line, 1 is EOF line. 
	char bufferToBeFlushed = '\0'; // to be used to flush buffer
	
	int i=0;
	while (!flagEOF) {
		if (signal(SIGINT, SIG_DFL)==SIG_ERR) { //Ctrl+C
			perror("Failed to install SIGINT default handler");
			exit(3); //why 3?
		}
		if (signal(SIGTSTP, SIG_DFL)==SIG_ERR) {//Ctrl+Z
			perror("Failed to install SIGTSTP default handler");
			exit(3); //why 3?
		}
		//Any time enter the loop, the default signal response is recovered. 		
		lenghOfInput = 0;i=0; //input stream index 		
		printf("\n%s\n%s\nCurrent process: %d is serving you:(Look at my PID, I am the parent)\n--P0-- %s(up to %d characters)\n(Duo's Shell)Please type $:",ruler, welcomeWords, getpid(), outputPromptWords, maxArraySize);		
		
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
		//equivalent to fgets(lineOfInput, sizeof(lineOfInput), stdin);	
		//getline(lineOfInput,stdin); not supported by standard libraray. 
		if (i >= maxArraySize) {
			printf("\nP1== Friendly Reminder 1==\n Maxium array size is reached.\n This line of input has been DISCARDED and FlUSHED and WON'T be forward for execution\n");
			lineOfInput[0] = '\0';
			i = 0;
			lenghOfInput = 0;
			while ((bufferToBeFlushed = getchar()) != '\n' && bufferToBeFlushed != EOF);
			//flush the remaining buffer before restart the process. 
			continue;
		}		
		lineOfInput[i] = '\0'; // Mark the end of input. 
		if (flagEOF) {
			printf("\nP2== Friendly Reminder 2==\n EOF is reached at Position %d in this line\n This line of input --\"%s\"--\n has been DISCARDED and WON'T be forward for execution\n Program Quit\n", i,lineOfInput);
			lineOfInput[0] = '\0';
			i = 0;
			lenghOfInput = 0;
			break;
		}
		else {
			lenghOfInput = i;	
		}
		if (lenghOfInput == 0) {
			printf("\nP3== Friendly Reminder 3==\n No input, Please re-enter\n");
			continue;
		}		
		printf("--P4-- You just input %d characters. They are:\n%s\nThe above characters will be forwarded to a child process. \n",lenghOfInput,lineOfInput);
				
		cpid = fork(); //fork 
		
		if (cpid == -1) {
			perror("Fork ERROR");
			exit(ERR_RETURN);
		}
		if (cpid == 0) { //child process
			int sleepLength = 5; //for testing the signal interrupt
			struct stat buf;     //File structure for test if a file exists
			char **formattedArgv; //for the storage of parsedArray
			
			if (signal(SIGINT, handler)==SIG_ERR) { 
				perror("Failed to install SIGINT default handler");
				exit(3);  //Install the signal handler Ctrl + C
			}
			if (signal(SIGTSTP, handler)==SIG_ERR) {
				perror("Failed to install SIGTSTP default handler");
				exit(3);  //Install the signal handler Ctrl + Z
			}
			
			printf("\n%s\n--C1-- Welcome your arrival at child process, my PID is %d, my parent is %d\n",ruler, getpid(),getppid());
			
			int formattedArgvWordLength = parseString(lenghOfInput, lineOfInput, &formattedArgv);
			//Key function in the child process, parse the string int arrays of string
			printf("--C2-- Dear User, I have parsed %d word(s) successfully, please check the following: \n",formattedArgvWordLength);
			for(i = 0 ; i<formattedArgvWordLength; i++){
				printf("--C3-- Word %d: %s \n",i, formattedArgv[i]);
			}
			printf("--C4-- End of parsed words\n");
			printf("--C5-- I will have a sleep of %d second(s) before executing, Please have your patience. \n (type Ctrl+C OR Ctrl+Z to return to parent process)\n", sleepLength);
			
			sleep(sleepLength);
			if (0==strcmp(formattedArgv[0], "exit")) { //enter the "exit" to quit. 
				kill(getppid(),SIGQUIT); //Stop the parent
				return 0;
			}
			//=========execv() function Begin========
			if (isPath(formattedArgv[0])){ 
				if(stat(formattedArgv[0],&buf)<0){
					printf("--C6-- The file indicated by the path does not not exist\n");
					printf("--C6-- Found failuare for path: %s\n", formattedArgv[0]);
					_exit(0);
				}
				else{
					printf("--C7--Found via given path name\nFile Location: %s\n", formattedArgv[0]);
					execv(formattedArgv[0],formattedArgv);
				}
			}
			else{
				char **searchPool; //for storage of the enviroment path 
				char fullFath[maxArraySize]= "";
				parseEnvPATH2PathPool(getenv("PATH"),&searchPool); 
				//get searchpool, perspective locations
				if (isFileExistInPaths(formattedArgv[0],searchPool,fullFath)) {
					formattedArgv[0] = fullFath; //redirect the pointer to full path. 
					printf("--C8-- Found via Search in PATH environment name\n");
					printf("--C8-- File found at location: %s\n", formattedArgv[0]);
					if (execv(formattedArgv[0],formattedArgv)<0) {
						printf("--C9--exev() Falied \n");
					}
				}
				else {
					printf("--C10--File \"%s\"is not found in any enviroment paths\n",formattedArgv[0]);
					_exit(0);
				}							
			}
			//=========execv() function End =======
			
			//=========Alternatively, execvp() function=======
		/*
			if(execvp(formattedArgv[0], formattedArgv)<0){
			     printf("--C9--exevp() Falied \n");
			} //not necessary to use PATH enviroment. 
		*/
			//=========Alternatively, execvp() function=======
			_exit(0); //exit(0)
		}
		else { //parent process
			if (signal(SIGINT, SIG_IGN)==SIG_ERR) {
				perror("--P5--Failed to install SIGINT ignore handler");
				exit(3);
			}
			if (signal(SIGTSTP, SIG_IGN)==SIG_ERR) {
				perror("--P6Failed to install SIGTSTP ignore handler");
				exit(3);
			} //The parent ignore the signal generated from Ctrl+C and Ctrl+Z at this moment
			term_pid = waitpid(cpid, &child_status,0);
			if (term_pid == -1) {
				perror("\n--P7--======= Waitpid Failed ==========");
			}
			else {
				printf("--P8-- Child Complete, Back to parent process: %d \n", getpid());
			}
		}
	}
    return 0;
}

// all functions other than main. 

static void handler(int signal){ //for both candidates of Ctrl+C and Ctrl+Z
	printf("\n--6-- Starting self defined Signal Handler\n The orignal signal is");
	printf("Received:-------");
	psignal(signal, "signal: ");	
	printf("Current Process: %d \n", getpid());
	printf("Return to The parent Process: %d \n", getppid());	
	//kill(getpid(),SIGINT);
	//kill(getppid(), SIGINT);
	_exit(0); 
	// This handler is for info. 
}

int parseString(int inputLength, char *lineInput, char ***argv)
{
	int argc = 0;
	char *formattedBuffer = (char*) malloc((inputLength+1) * sizeof(char));	
	(*argv) = (char**) malloc((maxArraySize+1) * sizeof(char**)); 
	
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
	(*argv)[argc] = NULL;
	return argc;
}

int isPath(char *pathName){ //1 for path, 0 for file, contains / or not
	int pathLength = strlen(pathName); 
	int i =0;
	for (i = 0; i<pathLength; i++) {
		if (pathName[i]=='/') {return 1;}
	}
	return 0;	
}

int isFileExistInPaths(char *fileName, char **pathPool, char *fullFath){
	//char fullFath[maxArraySize] = "";
	struct stat buf;	
	int i = 0;
	for (i = 0; pathPool[i]!=NULL; i++) {
		strcpy(fullFath, pathPool[i]);
		strcat(fullFath, "/");
		strcat(fullFath, fileName);
		if (stat(fullFath, &buf)==0) {
			fullFath[1 + strlen(pathPool[i]) + strlen(fileName)]='\0';
			return 1;   //Exist
		} 
	}
	fullFath[0]='\0';
	return 0; //DOES not exist
}
int parseEnvPATH2PathPool(char *envPATH, char ***parsedpathPool){	
	int lengthOfenvPath = strlen(envPATH), index_envPATH = 0;
	int rowIndex_parsedpathPool = 0;
	char *formattedPathLevel2 = malloc((lengthOfenvPath+2)*sizeof(char*)); //intermediate
	// strcpy(formattedPathLevel2, envPATH);
	(*parsedpathPool) = (char**) malloc((lengthOfenvPath+2)*sizeof(char**));
	(*parsedpathPool)[rowIndex_parsedpathPool++] = &formattedPathLevel2[0];
	for (index_envPATH = 0; index_envPATH<lengthOfenvPath; index_envPATH++) {
		if(envPATH[index_envPATH]!=':'){
			formattedPathLevel2[index_envPATH] = envPATH[index_envPATH];
		}
		else {
			formattedPathLevel2[index_envPATH] = '\0';
			(*parsedpathPool)[rowIndex_parsedpathPool++] = &formattedPathLevel2[index_envPATH+1];
		}
	}
	formattedPathLevel2[lengthOfenvPath]='\0';
	(*parsedpathPool)[rowIndex_parsedpathPool] = NULL;
	return 1;
}