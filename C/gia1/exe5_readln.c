#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include "exe5_readln.h"




main (int argc, char *args[]){
	int size;	
	while(1){
		char buf [BUFFSIZE];
		size = readln(0, buf, sizeof(buf));

		if (size == 1 && (buf[0] == '\n')) return 0 ; // if empty line stop programm 
		
		write (1,buf,size);
	}
}

/*	read line from file ou standart input 
	colling read () function whitout buffering 
*/
ssize_t readln (int fildes, void * buf, ssize_t nbuf){
	
	if (fildes<0 || buf == NULL || sizeof(buf)<=0) return -1 ;
	char * buffer = (char *)buf;
	char ch;
	int num = nbuf; // number of must be readed characters;
	int n,num_readed =0; // num readed characters  
	
	while ( (n = read (fildes, &ch, 1))==1 && num-->0 ){
		*buffer++ = ch;
		num_readed ++;
		if (ch == '\n') break; // end of line 
	}	
	return (ssize_t)num_readed;
}

