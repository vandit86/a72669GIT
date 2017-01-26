
import java.util.concurrent.locks.*;
import java.util.Random;

class BoundedBuffer {

	public static void main (String args[]){
		int n = 5,m = 6,size = 15, max = 10;
		if (args.length > 0 ){
			try {
				n = Integer.parseInt (args[0]);
				m = Integer.parseInt (args[1]);
			}catch (Exception e){

			}
		}

		Thread tp = null;
		Thread tg = null;
		int producer = n;
		int clients = m;
		Buffer b = new Buffer (size, max);
		
		for (int i =0; i< producer ; i++){            // Start threads for put 
			tp = new Thread(new Putter(b));
			tp.start();
		}

		  for (int i =0 ; i< clients; i++){             // Start thread for get 
		  	tg = new Thread(new Getter(b));
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

class Buffer {
	final int N;		// dimention of buffer
	int stuff = 0;
	int maxNum, count;
	final ReentrantLock lock = new ReentrantLock();
	final Condition notFull = lock.newCondition();
	final Condition notEmpty = lock.newCondition();

	public Buffer(int n, int maxNum) { 
		this.N = n; 
	    this.maxNum = maxNum ;  // max number of things to passed by 
	}

	public void put() throws InterruptedException {
		lock.lock(); 
		try{
			while (stuff == N)
				notFull.await(); // waiting for condition not notFull
			stuff++;
			notEmpty.signalAll();
		}finally { 
			lock.unlock();
		}
	}

	public void get() throws InterruptedException {
		lock.lock();
		if (maxNum == 0) System.exit(0);
		
		try {
			while (stuff == 0)
				notEmpty.await();
			stuff--;
			maxNum --;
			notFull.signalAll();

		} finally {
			lock.unlock();
		}
	   
	}

}


class Putter implements Runnable {
	Buffer b;
	int time = 1000;
	int z; 
	public Putter(Buffer b) {
		this.b = b;
	}
	public void run() {
		Random rand = new Random();
		int i = 0;
		try {
			for ( ; ; ) { 

				Thread.sleep(rand.nextInt(time));
				b.put();
			}
		} catch (InterruptedException e) {
     //System.out.println("Interrupted");
		}
	}
}

class Getter implements Runnable {
	Buffer b;
	int time = 1000;
	public Getter(Buffer b) {
		this.b = b;
	}
	public void run() {
		Random rand = new Random();
		int i = 0;
		try {
			for ( ; ; ) { 

				Thread.sleep(rand.nextInt(time));
				b.get();
			}
		} catch (InterruptedException e) {
		}
	}
}
