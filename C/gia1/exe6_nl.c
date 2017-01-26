#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>


main (int argc, char * argv[]){

	
	int fd,d;
	
	fd = open ("/tmp/del.txt",O_RDWR | O_CREAT ,0777);
	//open('path',O_WRONLY|O_CREAT,0640);

	printf ("fd = %d\n", fd); 
	
	d = dup (1);

	printf ("dup = %d\n", d);

	d = dup2(d, fd);

	 printf ("dup2 =%d\n", d);

	
	//  d = dup2 (fd, d);

	//  //d = dup(1);
	// printf ("%d\n", d);
	
	// d = dup(1);
	// printf ("%d\n", d);

	// close (d);

	puts ("string1");
	
}