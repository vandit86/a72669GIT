#include <stdlib.h>
#include <signal.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/wait.h>
#include <stdio.h>
#include <fcntl.h>
#include <string.h>
#include <pthread.h>

#define BUFF_SIZE 256
#define CMD_EXIT "exit"

#define FIFO_PATH_READ "/tmp/fifo0002.1"
#define FIFO_PATH_WRITE "/tmp/fifo0001.1"



/*
  ;;
  commandos :
	"exit"          : para sair do programma
  "exec"          : executar tarefa 
  "tempo-inac"    : definir tempo maximo de inactividade na comunicação entre servidor e cliente
  "tempo-exec"    : definir tempo maximo de execução duma tarefa 
  "terminar"      : terminar tarefa em execução 
  "historico"     : mostrar tarefas já terminadas
  "listar"        : todas as tarefas
  "listar-exec"   : tarefas em execução
  "ajuda"         : ajuda 

*/
	void handler(int signal);
  //void * listener (void * a);
  int fd_fifo_write, fd_fifo_read; /*descriptor FIFO*/
  pid_t pid_listner;                // listner fork pid

int main ( int argc, char *argv[]){
	 
  	char string[BUFF_SIZE];
    string[0] = '\0';
  	char* buf = string;
  	int res;
  	int pid;
    int i;
    
    signal(SIGINT , handler);


  	/*Abrir Fifo só para write */

  if((fd_fifo_write=open(FIFO_PATH_WRITE, O_WRONLY)) == - 1)
  {
    perror("Open FIFO Write");
    //fprintf(stderr, "Impossivel Abrir fifo\n");
    exit(0);
  }

  /*Abrir Fifo só para read */

  if((fd_fifo_read=open(FIFO_PATH_READ, O_RDONLY)) == - 1)
  {
    perror("Open FIFO Read");
    exit(1);
  }

  //CRIETE THREAD FOR LISTNER FUNCTION
    // pthread_t t;
    // if(pthread_create(&t,NULL,listener,NULL) == -1){
    //   perror("Criate Listner Thread");
    //   exit(1);
    // } 

  // criete process to read from pipe 

  pid_listner = fork();

  if (pid_listner == -1){
    perror ("fork");
    exit(1);
  }

  // CHILD PROCESS
  if (!pid_listner){
      int res;
      char buf [BUFF_SIZE];

      while (( res = read(fd_fifo_read, buf,BUFF_SIZE)) > 0){
        write(1,buf,res);
      }

      exit(0);
  }  


if (argc == 1){
  /*Ler do teclado */
  while ((res = read(0, buf, BUFF_SIZE))>0 ){
  	
  	buf[res-1] = '\0';
  	
    // if command "exit" is entered 
  	if (strcmp (buf, CMD_EXIT)==0) break;
  	
  	/*Write to Fifo pipe */
     if ( (res = write ( fd_fifo_write, buf, res)) == -1 ){
      perror ("Write to SERVER");
     }

   }
 }

 else if (argc > 1){
  
  // concatenate all  string arguments
  for (i=1; i<argc; i++){
    buf = strncat(buf, argv[i], BUFF_SIZE - strlen(buf) -1);
    buf = strncat(buf, " ", BUFF_SIZE - strlen(buf) -1);
  }
    
    /*Write to Fifo pipe */
     if ( (res = write ( fd_fifo_write, buf, strlen(buf))) == -1 ){
      perror ("Write to SERVER");
     }  
 }

   // close descriptors 
  close (fd_fifo_read);
  close (fd_fifo_write);
  kill(pid_listner, SIGTERM);
  return 0;
 
}

void handler(int signal){
 
  kill (pid_listner, SIGTERM);
  exit(0);

}

// void * listener (void * a){
//   int res;
//   char buf [BUFF_SIZE];

//   while (( res = read(fd_fifo_read, buf,BUFF_SIZE)) > 0){
//     write(1,buf,res);
//   }
  
// }