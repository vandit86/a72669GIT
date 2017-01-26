/*
	multithriding server 
	return sum of numbers 
	whith and of conection return media 
*/

import java.net.*;
import java.io.*;


 class MyTread implements Runnable {
	
	Socket cs;	
	MyTread(Socket cs){
		this.cs = cs;		// socket 
	}

	public void run ()  {

		int f1=0, f=0, count = 0;
		String s = null;
		try {
			PrintWriter out = new PrintWriter (cs.getOutputStream(), true);

			BufferedReader in = new BufferedReader (
				new InputStreamReader(cs.getInputStream()));

			
			while ((s = in.readLine()) != null){
				f = Integer.parseInt(s);
				count ++;
				System.out.println (f);
				f1+= f; 
				out.println(f1);		
			}

			out.println((double)f1/count);  // enviar mendia depois de cliente fechar socket

			in.close();
			out.close();
			cs.close();
		}
		catch (IOException e){

		}
	}
}

public class Servidor{
public static void main(String[] args) throws IOException{
	
	ServerSocket ss = new ServerSocket (9999); 
	
	while (true){
		Socket cs = ss.accept();	
		new Thread (new MyTread(cs)).start();   // new Thread para cada cliente novo  
	}

	//ss.close();

}
}