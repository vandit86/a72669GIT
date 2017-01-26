
class Example {

	public static void main (String args[]){
		int N = 1;
		int I = 1;  							// default value 

		if (args.length == 2){
			try{
				N = Integer.parseInt (args[0]);
				I = Integer.parseInt (args[1]);
			}catch (NumberFormatException e){
				System.err.println ("numero incorreto");
				System.exit(1);
			}
		}else {
			System.err.println ("Input error");
			System.exit(1);
		}

		Thread arr[] = new Thread[N];
		Counter c = new Counter ();

		for (int i =0; i < N ; i ++){
			arr[i] = new Thread(new Work(I, c));
		}

		for (int i =0 ; i< N ; i ++){
			arr[i].start();
		}

		for (int i =0 ; i< N ; i ++){
			try {
				arr[i].join();
			}catch (InterruptedException e){

			}
		}

		System.out.println (c.getVar());
	}
}


class Work implements Runnable {
	int N;
	Counter c;
	Work(int n , Counter c){
		this.N = n;
		this.c = c;
	}
	public void run (){
		for (int i =0; i< N ; i++ ) {
			//c.var ++;
			c.increment();
		}
		//Counter.increment();
	}
}


class Counter {

	private int var = 0;

	public synchronized void increment (){
		var ++; 
	}

	public  int getVar(){
		return this.var;
	}

	
}