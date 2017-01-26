import java.lang.*;

public class InterruptThreadDemo implements Runnable {

   Thread t;

   InterruptThreadDemo() {

      t = new Thread(this);
      System.out.println("Executing " + t.getName());
      // this will call run() fucntion
      t.start();
           
      // tests whether this thread has been interrupted
      if (!t.isInterrupted()) {
      t.interrupt();
      }
      // block until other threads finish
      try {  
         t.join();
      } catch(InterruptedException e) {}
   }

   public void run() {
   try {       
   while (true) {
   Thread.sleep(1000);
   }
   } catch (InterruptedException e) {
   System.out.print(t.getName() + " interrupted:");
   System.out.println(e.toString());
   }
   }

   public static void main(String args[]) {
   new InterruptThreadDemo();
   new InterruptThreadDemo();
   }
} 