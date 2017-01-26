#include <unistd.h>
#include <stdlib.h>
#include <sys/wait.h>

main (){
	
	pid_t pid; 
	int fd [2];
	int pid_status;

	if (pipe(fd) == -1){
		// error to create pipe 
	}

	pid = fork();

	if (!pid){
		// dentro processo filho 
		close(fd[0]);
		dup2(fd[1], 1); // ligar a sua saida ao pipe

		// executar grep com parametros dentro do filho 
		execlp ("grep", "grep","-v", "^#", "/etc/passwd", NULL);
	}

	dup2(fd[0],0); // ler do pipe 
	close (fd[1]); // nao vamos escrever nada 

	if (waitpid(pid, &pid_status, 0) == -1 ){
		// error de espera do processo filho 
	}

	if (WEXITSTATUS(pid_status)){
		// terminou com status != 0
	}






	/* Initial memory allocation */
   char *bufer = (char *) malloc(1024);
   int res;
   while ((res = read(0, bufer, sizeof(bufer)))>0 ){
   		write(1, bufer, res);
   }

    free(bufer);



}
