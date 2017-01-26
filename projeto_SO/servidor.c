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


#define FIFO_PATH_READ "/tmp/fifo0001.1"
#define FIFO_PATH_WRITE "/tmp/fifo0002.1"

 /***********************************COMANDOS *************************************/
#define CMD_EXEC "exec"
#define CMD_TIME_PIPE "tempo-inac"
#define CMD_TIME_EXEC "tempo-exec"
#define CMD_KILL "terminar"
#define CMD_HISTORY "historico"
#define CMD_LIST "listar"
#define CMD_LIST_EXEC "listar-exec"
#define CMD_HELP "ajuda"

/***********************************************************************************/

#define BUFF_SIZE 256
#define ALARM_EXIT_STATUS 127



/*
  SERVER :: INPUT FROM CLIENT 
  SERVER :: OUTPUT TO CLIENT 
  
*/

enum Status {IN_EXE, DONE, MAX_PIPE, MAX_EXE};

struct Task
{

  pid_t pid;              // PID of task
  int num;                // number of task
  char name[BUFF_SIZE];   // name of the task 
  Status s;               // status of task ()
  Task *next;             // pointer to the next task in list 

};


/***********************FUNCTION DECLARATIONS*******************************/

int newTask (char * body, pid_t pid);
void getList (int);
const char * getStatus (Status s);
void freeList (void);
void handler(int signal);
void alarmHandler (int siglal);
int setStatus (pid_t pid, Status status);


/************************CONSTATS AND GLOBAL VARIABLES************************/

char v_str [BUFF_SIZE];
const char * empty_list = "List is empty";
int fd_fifo_write, fd_fifo_read;          /*descriptor FIFO*/
pid_t proc_pid;                           // pid of child process
sigset_t mask;
Task *root = NULL;                        // root node of task's list 


/**********************************MAIN FUNCTION*******************************/

int main()

{
  
  char buf[BUFF_SIZE]; // 
  char cmd[BUFF_SIZE]; // command 
  char str[BUFF_SIZE]; // 
  pid_t pid;  
  int res, num;
  int fd[2];

  int timerPIPE = 100; // timer to control named pipe communication
  int timerTASK = 5; // timer to control task execution
  
    
  // handler for SIGNALS
  signal (SIGCHLD, handler);
  signal (SIGALRM, handler);

  /*se o file com este nome existir - apagamos-lo*/

  unlink(FIFO_PATH_WRITE);
  unlink(FIFO_PATH_READ);

  /*Criar FIFO to WRITE*/

  if((mkfifo(FIFO_PATH_WRITE, 0666)) == -1)

  {

    perror ("server mkfifo");
    exit(0);

  }

  /*Criar FIFO to READ*/

  if((mkfifo(FIFO_PATH_READ, 0666)) == -1)

  {

    perror("Server mkfifo");
    exit(0);

  }

  /*Abrir Fifo para read  */

  if((fd_fifo_read=open( FIFO_PATH_READ, O_RDONLY)) == - 1)
  {

    perror("server open :");
    exit(0);

  }

  /*Abrir Fifo para read  */

  if((fd_fifo_write=open( FIFO_PATH_WRITE, O_WRONLY)) == - 1)
  {

    perror("server open");
    exit(0);

  }

  // SET MASK OF SIGNALS TO BLOCK in HANDLER FUNCTION
    sigemptyset (&mask);
    sigaddset (&mask, SIGCHLD);
    sigaddset (&mask, SIGALRM);
  

  // Out from while cycle just when client close FIFO pipe to server 
  // WHEN CLIENT CLOSE PORT  READ RETURN 0 -> END OF USER SESSION 
    
    while ((res =read(fd_fifo_read, buf, sizeof(buf))) > 0){

        
      // START TIMER PIPE COMUNICATION
      alarm (timerPIPE);

     // GET THE COMMAND STRING 
      if ( sscanf (buf, "%s", cmd) != 1 ){
        continue;
      }

      // GET COMMAND BODY
      strcpy(str, &buf[strlen(cmd)]);

            // SWITCH COMMAN STRING 
            if (strcmp(cmd,"exec") == 0)
            { 
                                   
                      /************************ CHILD PROCESS *************** */ 
                      if ((pid = fork()) == 0)
                      {
                        
                        // SET DIFERENT HANDLER FOR CHILD-PROCESS 
                        
                        signal(SIGALRM, alarmHandler);
                        signal(SIGCHLD, SIG_IGN);        
                        alarm(0); // cansel ANY TIMER  

                        
                        proc_pid = fork(); // NEW PROC
                        
                        if ( proc_pid == -1) {
                          perror("Fork process");
                          exit(1);
                          } 

                          /*CHILD PROCESS***************************************************/ 
                          if (!proc_pid){
                        /*TROCA O "ECHO" POR NOME DA PROGRAMMA
                          Que RECEBA COMO ARGUMENTO UMA STRING
                          COM OS COMMANDOS PARA EXECUTAR E DEVOLVA RESULTADO PARA 
                          SDTOUT OU STDERR 
                        */
                        execl("/usr/bin/echo", "echo", str, NULL);
                        //execlp("more", "more",  "/home/vad/Documentos/test.txt", NULL); 
                        //execl ("/usr/bin/cut", "cut", "-c-2","/home/vad/Documentos/test.txt" ,NULL);
                        //perror ("atntes ");
                        //execl("/home/vad/Documentos/cExemples/gia1/pause", "pause", NULL, NULL);
                        //perror ("depois ");
                        exit(1);
                      }
                      /**********************************************************************/

                      // START TASK TIMER 
                      alarm(timerTASK);
                      
                      wait(NULL); // wait for child process terminate 
                      exit(0); // exit with 0 status (GOOD);



                      }


          /********** MAIN PROCESS****************/
                  
                // criete new Task
                if (newTask(str, pid)<0) {
                    perror("newTask fail");
                }

            }
            
            else if (strcmp(cmd,CMD_LIST) == 0){
              // get all task
              getList(-1);  
            }

            else if  (strcmp (cmd, CMD_HISTORY) == 0){
              //get list of history
              getList(1);
            }

            else if (strcmp (cmd, CMD_LIST_EXEC) == 0){
              // get tasks em exec
              getList (0);
            }

            else if (strcmp (cmd, CMD_TIME_EXEC) == 0){
              if (sscanf (str, "%d" , &num) > 0 && num > 0)
              timerTASK = num;
              printf("New task time :: %d\n", timerTASK);
            }

            else if (strcmp (cmd, CMD_TIME_PIPE) == 0){
              if (sscanf (str, "%d" , &num) > 0 && num > 0)
                timerPIPE = num;
                printf("New pipe time :: %d\n", timerPIPE);
            }

            else if (strcmp (cmd, CMD_KILL) == 0){
              
              if (sscanf (str, "%d" , &num) > 0 && num > 0){
                  
                  Task* k;
                  for (k=root; k; k = (k->next)){
                    if ((k->num) == num && (k->s) == IN_EXE){
                      kill(k->pid, SIGTERM);
                    }
                  }
                }
            }

            else if (strcmp (cmd, CMD_HELP) == 0){
              puts ("HELP");
            }

    }

    // DELETE ALL FIFO PIPES 
    unlink(FIFO_PATH_WRITE);
    unlink(FIFO_PATH_READ);

    // FREE MEMMORY
    freeList();

  return 0;

}


/********************************************FUNCTIONS****************************************************/

/*
  CRIATE A NEW TASK AND ADD TO THE TASK'S LIST
  ROOT -> IS THE ROOT NODE ON LIST 
  BODY -> BODY OF TASK (NAME)
*/

int newTask (char* body, pid_t pid){
  static int num=0;
  Task * k;

  // create new task
  Task * t = (Task*) malloc (sizeof(Task));
    if ( t == NULL) return -1;
    
    t->pid = pid;
    t->num = ++num;
    t->s = IN_EXE;
    strncpy(t->name,body,BUFF_SIZE-1);
    t->next = NULL;
  
  // if the firs element of list 
  if (!root) {
    root = t; 
  }

  else {
    for (k=root; (k->next)!=NULL; k=(k->next));
    (k->next)=t;
  }

      // Send responds to client 
      sprintf (v_str, "\tNew Task #%d \n", num);
      write (fd_fifo_write, v_str, strlen(v_str));

  return 0;
}


/********************************************************************************************************/

//  FORMING A STRING WITH 
// arg = -1  ALL TASK
// arg = 0  task in exec
// arg = 1 tesk terminated

void getList (int arg){
      Task *node;
      char s[BUFF_SIZE];  
        
      if(!root)  {
        write(fd_fifo_write, empty_list, strlen(empty_list));
        return;
      }

      for (node = root; node ; node = (node->next)){
          
        switch (arg){ 
          case 0  : if (node->s == IN_EXE) break; else continue;
          case 1 : if (node->s != IN_EXE) break ; else continue;
          case -1 : break;
          default : continue;

        }
          sprintf(s,"\t#%d: %s :%s\n",node->num,getStatus (node->s),node->name );
          write(fd_fifo_write, s, strlen(s));
      }

}

/********************************************************************************************************/

// RETURN STRING REPRESENTATION OF TASK STATUS 
const char * getStatus (Status s){

   const char *stat[] = {
                    "IN EXEC",
                    "DONE",
                    "MAX_PIPE",
                    "MAX_EXE"
                  }; 

  return stat [s];
}

/********************************************************************************************************/

//  FREE MEMMORY OF TASK LIST 
void freeList (){
  if (!root) return;
  Task * t;
  for (t = root ;root != NULL ; t = root){
      root = root->next;
      free(t);
  }
}

/********************************************************************************************************/

// SIGNAL HANDLER FOR THE MAIN PROCESS
void handler(int siglal){
    pid_t pid;
    
    int pid_status; // status of termination of process

    //BLOCK SIGNALS (SIGALRM && SIGCHLD)
    if (sigprocmask(SIG_BLOCK, &mask, NULL) < 0) {
      perror ("sigprocmask");
    }

    if (siglal == SIGCHLD)
    {
      
       pid = wait(&pid_status);
 
          if (!WEXITSTATUS(pid_status)){
              //puts("EXIT OK");

              if (setStatus (pid, DONE) < 0){
                perror ("setStatus");
              }

          }
          else {
              //puts ("EXIT :: TIME");
              if (setStatus (pid, MAX_EXE) < 0){
                perror ("setStatus");
              }
          }
    }

  // ALARM SIGNAL
  if(siglal == SIGALRM){
    puts("MAIN ALARM SIGNAL");

    // change status of all process in execution and KILL process
    if (setStatus (0, MAX_PIPE) < 0){
      perror ("setStatus");
    }

  }

  // UNBLOCK SIGNALS
  if (sigprocmask(SIG_UNBLOCK, &mask, NULL) < 0) {
    perror ("sigprocmask");
  }
}

/********************************************************************************************************/

// ALARM HANDLER FOR CHILD'S PROCESSES
void alarmHandler (int siglal){

    puts(" ALARM TASK\n");
    kill(proc_pid, SIGTERM);
    exit(ALARM_EXIT_STATUS); // NON ZERO VALUE TO RETURN IF ALARM 

}


/********************************************************************************************************/

// SET  EXIT STATUS TO PROCESS IN LIST  
// pid == 0 :: set status for all list members 
// pid : pid of process
// error :: return -1
// ok :: return 0

int setStatus (pid_t pid, Status status){
  if (!root) return -1;

  Task *task;
  
  if (! pid){ // change status for all in list
    for ( task = root; (task)!=NULL ; task = (task->next)){
          if (task->s == IN_EXE){
            kill(task->pid, SIGTERM); // kill process in exec
            task->s = status;
          }
            
    }
    return 0;
  }
  else {
    for ( task = root; (task)!=NULL ; task = (task->next)){
          if ((task->pid) == pid){
            if ((task->s) == IN_EXE){
              task->s = status;
              return 0;
            }else return 0; // process is already dead ... RIP=)
          }
    }
    return -1; // retrun err if no sach pid in list
  }
}

/********************************************************************************************************/
