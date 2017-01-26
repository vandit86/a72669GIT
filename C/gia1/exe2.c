#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#define FILE_SIZE (1024*1024) // 10Mbyte
#define ERROR "error get file descriptor"
#define PERMS 0666

main (int argc, char * argv[]){

	char conteudo [FILE_SIZE];	
	
	if ( argc != 2){
		printf("Chamada incorreta : falta o nome do fichero\n");
		return 1;
	}

	// criar file com o nome recebido
	// receber o file descriptor do novo file 
	
	int fd = creat (argv[1],PERMS);

	// se por algum motovo o file não foi criado 
	 if (fd == -1 ) {
	 	printf("%s\n", ERROR );
	 	return 1;
	 }

	 printf("descriptor = %i\n", fd );

	 write (fd, conteudo, FILE_SIZE);
	 close (fd);

	 printf ("fichero criado com nome %s e tamanho %i\n", argv[1], FILE_SIZE);
	 return 0;
}
