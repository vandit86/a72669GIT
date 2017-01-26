import java.math.BigInteger;
import java.util.Arrays;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;

import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;



class Bob {
	private final static int SIZE = 512; 
	private static final String TAG = "BOB : ";

	public static void main(String []args) throws Exception {
		System.out.println (TAG+"conection: Open");
		Bob bob = new Bob ();

			// accept connection from Alice 
		ServerSocket ss = new ServerSocket(4567);
		Socket s = ss.accept();

		System.out.println (TAG+"accepted");

		ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

			// get init values from Alice and parce buff to values 
		byte [] buff = (byte []) ois.readObject();
		int size = SIZE/8+1;
		byte [] valN = new byte [size];
		byte [] valE = new byte [3];
		byte [] valX1 = new byte [16];
		byte [] valX2 = new byte [16];

		System.arraycopy(buff, 0, valN, 0, size);
		System.arraycopy(buff, size, valE, 0, 3);
		System.arraycopy(buff, size+3, valX1, 0, 16);
		System.arraycopy(buff, size+3+16, valX2, 0, 16);

		BigInteger bigN = new BigInteger(valN);	// n
		BigInteger bigE = new BigInteger(valE);	// e
		BigInteger bigX1 = new BigInteger(valX1); // x1
		BigInteger bigX2 = new BigInteger(valX2); // x2
		BigInteger bigK, bigX = null; 

		// generate rundom num 
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[32];
		 do{
		 	random.nextBytes(salt);
		 	bigK = new BigInteger (salt);
		 }while (bigK.compareTo (bigX1) <= 0 || bigK.compareTo (bigX2) <= 0);

		bigX = bigK; 
		bigK = bigK.modPow(bigE, bigN);  //  		k = k^e mod N

		// select msg to resive {0,1}
		int i = 0 ;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while ( i<1 || i>2){
			System.out.print(TAG+"Enter msg to receve {1 ou 2} =>  ");
			try {
				i = Integer.parseInt(in.readLine());
			}catch (NumberFormatException e) {}
		}

		// calcul B and send to Alice 
		if (i == 1 ) bigK = bigK.add(bigX1); 	// k = k + x
		if (i == 2 ) bigK = bigK.add(bigX2);

		// send Response [V]  					v = (k+x)mod N
		oos.writeObject(bigK.mod(bigN).toByteArray());

		// recive 2 msgs and decript selected (always decryp other to show that can read nothing)
		buff = bob.decriptMessage( bigX,(byte []) ois.readObject());
		System.out.println (new String (buff));

		buff = bob.decriptMessage( bigX,(byte []) ois.readObject());
		System.out.println (new String (buff));

		// close connection 
		ois.close();
		oos.close();
		s.close();   
	}

	private byte [] decriptMessage (BigInteger x, byte [] buff){
		return new BigInteger(buff).subtract(x).toByteArray();
	}
	
	private static void printByteArrayToHex(byte[] array){
		for(byte x : array){
			System.out.printf("0x%02X ", x);
		}
		System.out.print("\n\n");
	}

}