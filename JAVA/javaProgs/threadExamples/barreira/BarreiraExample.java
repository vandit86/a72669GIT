import java.util.Random;

class Barrier {
  int N;                            // Barrier size 
  int pos = 0 ;                     // position in array 
  Object [] list ;                  // list Objects
  boolean joy;
  boolean [] flags;

  Barrier(int n){                                   // constructor 
    this.N = n-1;
    list = new Object [N];
     for (int i =0 ; i< list.length ; i++){         // inicialize Objects 
        list[i] = new Object();
      } 
    joy = false ;
    flags = new boolean [N]; 
  }

public void barrier (){
  Object obj;
  int n;
  synchronized(this){
      n = pos;
    if ( this.pos == N){
      pos = 0;
      joy = true ;
      for (int i =0 ; i < list.length ; i++){
        obj = list[i];
        synchronized(obj){
          obj.notify();
        }
      }
      while (getState());
      return;
    }
    
    obj = list[pos];
    flags [pos] = true;  //  o thread numero (pos) está a correr 
    pos ++;
    joy = false;
  }
    
    synchronized (obj){
      while (!joy){
        try{
          obj.wait();
        }catch(InterruptedException e){}
      }
    }

    flags [n] = false;  //  Thread terminated 
}

// return true if at lest one thread  steel in process
private boolean getState (){
  for (int i =0; i< flags.length ; i++){
    if (flags[i] == true ) return true;
  }
  return false ; 
}
  
}

class Client implements Runnable {
 Barrier b;
 int n;
 public Client(Barrier b, int n) {
   this.b = b;
   this.n = n;
 }
 public void run() {
   Random rand = new Random();
   int i = 0;
   try {
   while(true) { 
     i++;
     //System.out.println("Thread " + n + " started stage " + i);
     Thread.sleep(rand.nextInt(1));
     
     b.barrier();                           // must wait for all anouther threads to end 
     
     System.out.println("Thread " + n + " finished stage " + i);
   }
  } catch (InterruptedException e) {
   System.out.println("Interrupted");
 }
 }
}

class BarreiraExample {
 public static void main(String[] args) {
   final int N = 5;
   Barrier b = new Barrier(N);
   for (int i = 0; i < N; i++)
     new Thread(new Client(b, i)).start();
 }
}