import java.util.Random;
import java.util.concurrent.locks.*;

class Mover implements Runnable {
  Bank b;
  public Mover(Bank b) {
    this.b = b;
  }
  public void run() {
	Random rand = new Random();
	int slots=b.slots();
	int f;
	int t, tries;
   for(tries=0; tries<1000000; tries++)
	{ 
		f=rand.nextInt(slots); // get one
		while((t=rand.nextInt(slots))==f); // get a distinct other

			b.take(f,10);
			b.put(t,10);		
	}
   }
}

class Adder implements Runnable {
  Bank b;
  public Adder(Bank b) {
    this.b = b;
  }
  public void run() {
	int slots=b.slots();
	int sum, i, tries, lsum=0;
   for(tries=0; tries<1000000; tries++)
	{ 

		sum=b.sum();

		if (lsum!=sum) System.out.println("Total "+sum);
		lsum=sum;
	}
   }
}

class Transfer implements Runnable {
  Bank b;
  public Transfer(Bank b) {
    this.b = b;
  }
  public void run() {
	Random rand = new Random();
	int slots=b.slots();
	int f;
	int t, tries;
   for(tries=0; tries<1000000; tries++)
	{ 
		f=rand.nextInt(slots); // get one
		while((t=rand.nextInt(slots))==f); // get a distinct other

			b.transferir(f,t,10);
			
	}
   }
}



class  ExBankTransf3 {
	public static void main(String[] args) {
		int N = 10; // Number of accounts
		Bank b = new Bank(N);
		Mover m = new Mover(b);
		Adder c = new Adder(b);
		Transfer t = new Transfer(b);

		new Thread(m).start();
		new Thread(c).start();
		new Thread(t).start();
	}
}


// real	0m0.654s
// user	0m0.904s
// sys	0m0.147s

// class Bank {
	
// 	private int [] contas;
// 	int N =0;
// 	Bank (int N){
// 		this.N = N ;
// 		contas = new int[N];
// 	}

// 	final Lock lock = new ReentrantLock();

// 	public  int sum (){
// 		int s =0;
// 		lock.lock();
// 		for (int i =0; i < N ; i++){
// 			s+= contas[i];
// 		}
// 		lock.unlock();
// 		return s;
// 	}

// 	public void take (int i, int val){
// 		lock.lock();
// 		contas [i] -= val;

// 	}

// 	public void put (int i, int val){
// 		contas [i] += val;
// 		lock.unlock();
// 	}

// 	public  void transferir (int a , int b , int val){
			
// 		take (a, val);
// 		put  (b, val);
// 	}

// 	public int slots (){
// 		return N;
// 	}


// }


// version whithout ReentrantLock

class Bank {
	
	private int [] contas;
	int N =0;
	boolean endTramsaction ;
	Bank (int N){
		this.N = N ;
		contas = new int[N];
	}


	public synchronized int sum (){
		int s =0;
		while (!endTramsaction){
			try {
				wait();
			}catch (InterruptedException e){

			}
		}

		for (int i =0; i < N ; i++){
			s+= contas[i];
		}
		return s;
	}

	public synchronized void take (int i, int val){
		contas [i] -= val;
		endTramsaction = false;
	}

	public synchronized void put (int i, int val){
		contas [i] += val;
		endTramsaction = true;
		notifyAll();
	}

	public synchronized void transferir (int a , int b , int val){
			while (!endTramsaction){
			try {
				wait();
			}catch (InterruptedException e){

			}
		}
		take (a, val);
		put  (b, val);
	}

	public int slots (){
		return N;
	}


}

