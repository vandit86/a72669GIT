/*

1-2 oblivious transfer protocol, the sender has two messages m0 and m1, and the receiver 
has a bit b, and the receiver wishes to receive mb, without the sender learning b, while the sender wants 
to ensure that the receiver receives only one of the two messages. 
can be instantiated using RSA encryption as follows.

Alice has 2 msg to transfer 
Bob select one of them 

*/

import java.math.BigInteger;
import java.util.Arrays;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.KeyPairGenerator;
import java.security.KeyPair;



class Alice {

	private final static String TAG = "ALICE : ";
	private static final int RSA_SIZE = 512;
	private ObjectOutputStream oos;
	private ObjectInputStream ois; 

	static public void main(String []args) throws Exception {

		String msg1 = "message 1 ...";
		String msg2 = "message 2 ...";

		Alice alice = new Alice();
		BigInteger bigN;
		BigInteger bigE;
		BigInteger bigD;
		BigInteger bigV, x1, x2, key1, key2;  

		KeyPairGenerator key_gen = KeyPairGenerator.getInstance("RSA");
		key_gen.initialize(RSA_SIZE);
		
		// para não enviar valores com tamanhos diferents 
		do{
			KeyPair keypair = key_gen.genKeyPair();
			RSAPublicKey pub = (RSAPublicKey) keypair.getPublic();
			RSAPrivateKey priv = (RSAPrivateKey) keypair.getPrivate();
			bigN = pub.getModulus();
			bigE = pub.getPublicExponent();
			bigD = priv.getPrivateExponent(); 
		}while (bigN.bitLength() != RSA_SIZE || bigD.bitLength() != RSA_SIZE);

		// generate rundom num 
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		
		// open connection whith "BOB"
		alice.openConnection(); 
		System.out.println (TAG+"open connection: OK");

		// send init values [ N[64] + E[3] + X1[16]  + X2[16] ]
		byte [] buff = concat (bigN.toByteArray(), bigE.toByteArray());
		random.nextBytes(salt);
		x1 = new BigInteger (salt);
		buff = concat (buff, salt);

		random.nextBytes(salt);
		x2 = new BigInteger (salt);
		buff = concat (buff, salt);

		// write initial params 
		alice.oos.writeObject(buff);

		// recive  V calc keys and 
		buff = (byte []) alice.ois.readObject();
		bigV = new BigInteger(buff);

		// 2 key calculation [ key = (v-x)^d mod N ] 
		key1 = bigV.subtract(x1).mod(bigN).modPow(bigD, bigN);
		key2 = bigV.subtract(x2).mod(bigN).modPow(bigD, bigN);

		// encript and send [ msg + key ]
		alice.oos.writeObject ( new BigInteger(msg1.getBytes()).add(key1).toByteArray());
		alice.oos.writeObject ( new BigInteger(msg2.getBytes()).add(key2).toByteArray());

		// close connections 
		alice.oos.close();
		alice.ois.close();
		
	}
	

	// open conection (can pass IP and Port for the non test program)
	private void openConnection () throws IOException {
		Socket s = new Socket("localhost",4567);
		oos = new ObjectOutputStream(s.getOutputStream());
		ois = new ObjectInputStream(s.getInputStream());
		
	}

	/**************************   AUXILAR METHODS *******************************************/

	public static byte[] concat(byte[] first, byte[] second) {
		byte[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	private static void printByteArrayToHex(byte[] array){
		for(byte x : array){
			System.out.printf("0x%02X ", x);
		}
		System.out.print("\n\n");
	}
}