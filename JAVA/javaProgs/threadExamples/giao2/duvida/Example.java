class Example {
	public static void main (String args[]) throws Exception {
		Some b = new Some();
		Thread t1 = new Thread(new T1(b));
		Thread t2 = new Thread(new T2(b));
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		System.out.println (b.c);			
	}
}

class T1 implements Runnable {
	Some n;
	T1(Some n){
		this.n = n;
	}

	public void run (){
		for (int i =0 ; i< 1000000; i++){
			synchronized (n){
				n.c++;
			}
		}
	}
}

class T2 implements Runnable {
	Some n;
	T2(Some n){
		this.n = n;
	}

	public void run (){
		for (int i =0 ; i< 1000000; i++){
			synchronized (n){
				n.c--;
			}
		}
	}		
}

class Some{
	public int c;
}