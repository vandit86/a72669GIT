 import java.net.*;
import java.io.*;

 class GetConection implements Runnable {
	BeepThread bt;
	Socket cs;	
	long time;

	GetConection(Socket cs, BeepThread bt){			// constructor
		this.cs = cs;		// socket 
		this.bt = bt;
	}

	public void run ()  {

		int  cmd=0;			// some command (para usu futuro)
		try {
			PrintWriter out = new PrintWriter (cs.getOutputStream(), true);
			BufferedReader in = new BufferedReader (
				new InputStreamReader(cs.getInputStream()));

			while ( (cmd = Integer.parseInt(in.readLine())) != BeepProtocol.TRANS_END )	{// ler num de pedido do cliente 
				switch (cmd){
					case BeepProtocol.TRANS_TIME :
						out.println(bt.getDelay());	// enviar para cliente interval
					break;
					case BeepProtocol.TRANS_DELAY:
						while ((time = bt.getRestTime()) <= 0); 
						out.println(time);	// enviar para cliente tempo que falta 
					break;			

				}
			}
			//out.println((double)f1/count);  // enviar mendia depois de cliente fechar socket

			in.close();
			out.close();
			cs.close();
		}
		catch (IOException e){
			System.out.println ("GetConection exception");
		}
	}
}