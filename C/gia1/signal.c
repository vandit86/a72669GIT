#include <stdlib.h>
#include <signal.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/wait.h>
#include <stdio.h>

void handler(int signal);

int main (){
	
	signal(SIGALRM, handler);	
	signal(SIGINT, handler);	
	signal(SIGQUIT, handler);
	
	alarm (1);
	while(1){
		pause();
	}	

	return 0;
}


void handler(int signal){
	static int num_seq =0;
	static int num_control=0;
	switch (signal){
		case SIGALRM :
			num_seq ++;
			alarm(1);
			break;
		case SIGINT:
			num_control ++ ;
			printf("numseq =%d\n",num_seq);
			break;
		case SIGQUIT:
			printf("num_control %d\n",num_control);
			exit(0);
	}
}