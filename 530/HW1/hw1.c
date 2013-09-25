#include<stdio.h>
int main(){
   char ruler[]="1=========2=========3=========4==ruler==5=========6=========7=========8=========";
   //80-character ruler, for check correctness only;
   printf("%s\n%s\n%s\n%s\n",ruler,"Welcome to Duo's 80-character Output Homework\n(Up to 1024 characters, Press Ctrl+Z to terminate)",ruler,"Please enter characters:");
   char inChar;//For temporary input storage. 
   char outArray[1024];//Maximum input size, large enough
   int k=0,minLength=80,outputSize=80;
	while(1){ 
		// a loop which read 80 charcters from input
		// compress ** into ^, recode \n as white space
		scanf("%1c",&inChar);
		if(inChar=='\n')
		{
			outArray[k++]=' ';
			if(k>minLength)
				break;
		}
		else if(k!=0&&inChar=='*'&&outArray[k-1]=='*')
		{
			outArray[k-1]='^';
		}
		else{ 	
			outArray[k++]=inChar;	
	      	}
	     }
	int num=(int)(k/outputSize);//Calculate the number of characters for output
	int i,j;
	for(i=0;i<num;i++){
		printf("\nOutput--80-character Line: %d Ctrl+Z to terminate\n",i);
                printf("%s\n",ruler);//This Line is basically for debugging
		for(j=0;j<outputSize;j++){
                	printf("%c",outArray[i*outputSize+j]);
		}
		printf("\n");
                printf("%s\n",ruler);//This Line is basically for debugging
	}
       return 0;
}
