import java.math.BigInteger;
import java.util.Arrays;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Bob {
	private final static int SIZE = 512; 
	private static final String TAG = "BOB : ";
	private final String ALGORITM_CIPHER = "AES/CTR/NoPadding";
	
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
		byte [] valP = new byte [size];
		byte [] valG = new byte [size];
		byte [] valA = new byte [buff.length - 2*size];
		System.arraycopy(buff, 0, valG, 0, size);
		System.arraycopy(buff, size, valP, 0, size);
		System.arraycopy(buff, 2*size, valA, 0, buff.length - 2*size);

		BigInteger bigG = new BigInteger(valG);	// G
		BigInteger bigP = new BigInteger(valP);	// P
		BigInteger bigA = new BigInteger(valA); // A
		BigInteger bigB;
		BigInteger b = bob.genExponent (SIZE);
		
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
		bigB = bigG.modPow(b, bigP);
		if (i == 2 ) bigB = bigB.multiply(bigA);

		// calcul key 
		SecretKey key = bob.getAESKey (bigA.modPow(b, bigP).toByteArray());
		oos.writeObject(bigB.toByteArray());

		// recive 2 msgs and decript selected (always decryp other to show that can read nothing)
		buff = bob.decriptMessage(key ,(byte []) ois.readObject());
		System.out.println (new String (buff));

		buff = bob.decriptMessage(key ,(byte []) ois.readObject());
		System.out.println (new String (buff));

		//printByteArrayToHex(buff);
		// close connection 
		ois.close();
		oos.close();
		s.close();   


	}

	// generate secret key for AES cipher algorithm 
	// Key whit 128 bit length , (can modify this)
	private SecretKey getAESKey(byte [] key) throws NoSuchAlgorithmException {
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		key = sha.digest(key);
		key = Arrays.copyOf(key, 16); // use only first 128 bit

		SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
		return secretKeySpec;
	}

	// decrypt msg with the spesific kay 
	private byte [] decriptMessage (SecretKey key, byte[] msg  ) throws Exception{
		// get salt 
		byte[] salt = new byte[16];
		byte[] cipherMsg = new byte [msg.length - 16];
		System.arraycopy (msg, 0, salt, 0, 16);
		System.arraycopy (msg, 16, cipherMsg, 0, msg.length-16);
		
		// init cipher whith decrypt_mode
		Cipher cipher = Cipher.getInstance(ALGORITM_CIPHER);
		IvParameterSpec iv = new IvParameterSpec(salt);
		cipher.init(Cipher.DECRYPT_MODE, key, iv);

		// decrypt msg 
		return cipher.doFinal(cipherMsg);
		
	}

	// generate a(private value) => exponent
	// recive parameter in bits   
	private BigInteger genExponent (int size){
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[size/8];
		random.nextBytes(bytes);
		return new BigInteger (bytes);
	}

	private static void printByteArrayToHex(byte[] array){
		for(byte x : array){
			System.out.printf("0x%02X ", x);
		}
		System.out.print("\n\n");
	}


}