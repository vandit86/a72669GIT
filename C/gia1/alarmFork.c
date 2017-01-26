#include <signal.h>
#include <sys/types.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/wait.h>
#include <stdio.h>
#include <sys/stat.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>



pid_t p_pid;

void h_alarm(int s){
	puts ("h_alarm");
	exit(0);
}

void h_child (int s){
	puts ("h_child");
	int pid_status;
	pid_t pid = wait(&pid_status);

	printf ("Status :: %d\n", WEXITSTATUS(pid_status));


}



void procALRM(int s){
	puts("procALRM");
	kill(p_pid, SIGTERM); // kill child
	//sleep(2);
	exit(1); // exit with status 1
}

// void procSIGCHLD(int s){
// 	puts("procSIGCHLD");
// 	exit(0);
// }




main (){

	signal(SIGALRM, h_alarm);
	signal (SIGCHLD, h_child);

	alarm(5);

		if (!fork()){
			
			signal(SIGALRM, procALRM);
			//signal(SIGCHLD, procSIGCHLD);
			signal(SIGCHLD,SIG_IGN);
			alarm (0);

					if( !(p_pid =fork())) {
						//execlp("more", "more",  "/home/vad/Documentos/test.txt", NULL); 
							//alarm(0); // cansel alarm
					 	execl("/home/vad/Documentos/cExemples/gia1/pause", "pause", NULL, NULL);
					 }
			alarm(2);
			wait(NULL);
			exit (5);

		}

	 pause();
}