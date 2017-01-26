import java.net.*;
import java.io.*;

public class Client{

private final static int PORT = 9999;
private final static String LOCALHOST = "127.0.0.1";

public static void main(String[] args) throws IOException{
	
	String s = null;
	Socket cs = new Socket(LOCALHOST, PORT);

	PrintWriter out = new PrintWriter (cs.getOutputStream(), true);

	BufferedReader input = new BufferedReader (
		new InputStreamReader(cs.getInputStream()));

	
	BufferedReader in = new BufferedReader (
		new InputStreamReader(System.in));

	
	while ((s = in.readLine()) != null){
		out.println(s);
		System.out.println(input.readLine());
	}

	cs.shutdownOutput(); 									// didable output for this socket
	System.out.println("Media = "+input.readLine());		// print media 

	in.close();
	out.close();
	cs.close();

}
}