import java.util.concurrent.locks.*;
import java.util.Random;

class Bar {

	final ReentrantLock lock = new ReentrantLock();
	final Condition full = lock.newCondition();		// if full 
	final Condition allOut = lock.newCondition();

	int num = 3;
	int count = 0;
	int countOUT = 0;  
	boolean flag = true ;
	public void esperar () throws InterruptedException{
		
		lock.lock();
		try{

			count ++;
			//System.out.println ("in: "+count);
			while (count < num)
				full.await();		// waiting for condition full
			
			if (count == num && flag == true) {
				//System.out.println("signal");
				full.signalAll();	
				countOUT = num;
				flag = false;

			}

			//System.out.println ("out: "+countOUT);
			countOUT --;
			while (countOUT != 0)
				allOut.await();

			if (countOUT == 0 && flag == false ){
				System.out.println("");
				allOut.signalAll();
				count = 0;
				flag = true;
			}
			
		}finally{
			lock.unlock();
		}
	}
}

class Work implements Runnable {
	Bar b;
	int time = 5000;
	Work (Bar b ){
		this.b = b;
	}

	Random rand = new Random();
	public void run(){
		for (;;){
			try{
				Thread.sleep(rand.nextInt(time));
				b.esperar();
			}catch (InterruptedException e){
 0)
				allOut.await()
			}
			//System.out.println("o");
			System.out.println(Thread.currentThread().getId());
		}
	}
}

class Barreira{
	public static void main (String argv[]) throws Exception{
		int num = 6;
		Thread t = null;
		Bar b = new Bar();
		for (int i =0; i< num; i++){
			t = new Thread(new Work(b));
			t.start();
		}

		t.join();
	}
}