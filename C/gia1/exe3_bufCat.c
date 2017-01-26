#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
main (int argc, char * argv[] ){

	unsigned int buf;
	if (argc != 2){
		printf("Input Buffer Size\n");
		return 1;
	}

	int res = atoi (argv[1]);

	if (res == 0){
		printf("Numero incorreto\n");
		return 1;
	}


	/* Initial memory allocation */
   char *bufer = (char *) malloc(res);
   
   while ((res = read(0, bufer, sizeof(buf)))>0 ){
   		write(1, bufer, res);
   }

   free(bufer);
   return 0;
}