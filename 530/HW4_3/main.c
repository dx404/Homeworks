


    // insert code here...
	
#include <stdio.h>
#include <string.h>
#include "st.h"

int main() {
	int i=0;
	char buffer[4096], byte;
	
	//fread(<#void *#>, <#size_t #>, <#size_t #>, <#FILE *#>)
	//st_read(<#st_netfd_t fd#>, <#void *buf#>, <#size_t nbyte#>, <#st_utime_t timeout#>)
	
	/*
	while ((fread(&byte,1,1,stdin))) {
		if (byte=='\n') break;
		buffer[i]=byte; 
		i++;
	}
	 */
	
	st_read(stdin, &byte, 1, 1000);
	
	buffer[i+1]='\0'; /* null terminate */
	printf("You entered: \"%s\"\n",buffer);
	return 0;
}

//ssize_t st_read(st_netfd_t fd, void *buf, size_t nbyte, st_utime_t timeout);

//st_read(stdin, &byte, 1, 1000)