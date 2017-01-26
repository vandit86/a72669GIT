// Putter and getter concurent action with Box 
// Box object represent queue for store 

// exe 2 set time sleep to tread  
//    make P thread + C thead

import java.util.Random;

class Box {
  final int N;
  int stuff = 0;
  int maxNum, count;

  public Box(int n, int maxNum) { 
    this.N = n; 
    this.maxNum = maxNum ;  // max number of things to passed by 
 }

  public synchronized void put() throws InterruptedException {
    while (stuff == N)
       wait();
    stuff++;
    
    //System.out.println("Size inc to "+stuff);
    notifyAll();
  }

  public synchronized void get() throws InterruptedException {
    if (maxNum == 0) System.exit(0);
    while (stuff == 0)
       wait();
    stuff--;
    maxNum --;

    //System.out.println("Size dec to "+stuff);
    notifyAll();
  }

  public void setMaxNum (int i ){

  }
}

class Putter implements Runnable {
  Box b;
  int time;
  int z; 
  public Putter(Box b, int time) {
    this.b = b;
    this.time = time; // time in mili-sec to wait until put some thing
  }
  public void run() {
	Random rand = new Random();
	int i = 0;
    try {
    for ( ; ; ) { 
      //System.out.print("Puting "+i);
      //Thread.sleep(rand.nextInt(500));
      Thread.sleep(time);
      b.put();
    }
   } catch (InterruptedException e) {
     //System.out.println("Interrupted");
   }
  }
}

class Getter implements Runnable {
  Box b;
  int time;
  public Getter(Box b, int time) {
    this.b = b;
    this.time = time; // time in milisecinds wait to get some thing from stack
  }
  public void run() {
	Random rand = new Random();
	int i = 0;
    try {
    for ( ; ; ) { 
      //System.out.print("Getting ");
      //System.out.println(i);
      //Thread.sleep(rand.nextInt(500));
      Thread.sleep(time);
      b.get();
    }
   } catch (InterruptedException e) {
     //System.out.println("Interrupted");
   }
  }
}


class SizedBox {
  public static void main(String[] args) {
	
  final int N = 10;              // size of Box
  final int time = 100;         // time to wait in tread
  final int maxNum = 50;        // maximum stacks object passed throu BOX
  final int clients = 5;        // num gettes
  final int producer = 5;       // num putters
    
    Box b = new Box(N, maxNum); // make BOX object 
    
    Thread tg = null;                  // thread getter
    Thread tp = null;                  // therad putter 

  for (int i =0; i< producer ; i++){            // Start threads for put 
    tp = new Thread(new Putter(b,time));
    tp.start();
  }
   
  for (int i =0 ; i< clients; i++){             // Start thread for get 
    tg = new Thread(new Getter(b,time));
    tg.start();
  }  
    
    try{
      tp.join();
      tg.join();
    }catch (InterruptedException e){
      System.out.println ("Interrupted main");
    }
    System.out.println ("end");
  }
}