#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>

int my_getchar (void);

main ( int argc, char* argv[]){
	
	
	char c;
	while ( (c = my_getchar()) != EOF){
		write (1, &c, 1);
	}

	return 0;
}


/*Function is identical to standart getchar*/
int my_getchar (void){
	char c ;
	if (read (0, &c, 1 ) == 1) return  c;
	else return EOF;
}