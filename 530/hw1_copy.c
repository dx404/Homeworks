#include<stdio.h>
int main(){
   char ruler[]="1=========2=========3=========4==ruler==5=========6=========7=========8=========";
	printf("%s\n%s\n%s\n",ruler,"Welcome to Duo's 80-character Output Homework\n(Up to 1024 characters, Press Control+Z to terminate)",ruler);
	printf("Please enter characters:\n");
	char inChar;//For temporary store the input.
	char outArray[1024];
	int k=0,minLength=80,outputSize=80;
	while(1){ 
		// a loop which read 80 charcters from input
		// compress ** into ^, recode \n as white space
		scanf("%1c",&inChar);
		if(inChar=='\n'){
			outArray[k++]=' ';
			if(k>minLength)
				break;
		}
		else if(k!=0&&inChar=='*'&&outArray[k-1]=='*'){
			outArray[k-1]='^';
		  }
		else{
		 	outArray[k++]=inChar;
		    }
			
	     }
	int num=(int)(k/outputSize);
	int i,j;
	for(i=0;i<num;i++){
		printf("\nOutput--80-character Line: %d Ctrl+Z to terminate\n",i);
                printf("%s\n",ruler);

		for(j=0;j<outputSize;j++)
                	printf("%c",outArray[i*outputSize+j]);
                printf("\n%s\n\n",ruler);

	}
       return 0;
}
