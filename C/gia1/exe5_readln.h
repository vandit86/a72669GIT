#include <unistd.h>
#include <stdlib.h>
#include <sys/wait.h>

main (){
	int fd [2];
	int pid_status;

	if (pipe(fd) == -1){
		// error to create pipe 
	}

	pid_t pid = fork();

	if (!pid){
		// dentro processo filho 
		close(fd[0]);
		dup2(fd[1], 1); // ligar a sua saida ao pipe

		// executar grep com parametros dentro do filho 
		execlp ("grep", "grep","-v", "^#", "/etc/password", NULL);
	}

	dup2(fd[0],0); // ler do pipe 
	close (fd[1]); // nao vamos escrever nada 

	if (waitpid(pid, &pid_status, 0) == -1 ){
		// error de espera do processo filho 
	}

	if (WEXITSTATUS(pid_status)){
		// terminou com status != 0
	}


}
