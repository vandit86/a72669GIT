/*

1-2 oblivious transfer protocol, the sender has two messages m0 and m1, and the receiver 
has a bit b, and the receiver wishes to receive mb, without the sender learning b, while the sender wants 
to ensure that the receiver receives only one of the two messages. 
can be instantiated using DH encryption as follows.

Alice has 2 msg to transfer 
Bob select one of them 

*/

import java.math.BigInteger;
import java.util.Arrays;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

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



class Alice {

	private final static String TAG = "ALICE : ";
	private final String ALGORITM_CIPHER = "AES/CTR/NoPadding";
	private static final int DHP_SIZE = 512;
	private ObjectOutputStream oos;
	private ObjectInputStream ois; 

	static public void main(String []args) throws Exception {

		String msg1 = "mensagem1.......";
		String msg2 = "mensagem2.......";

		Alice alice = new Alice();
		DHParameterSpec dhSpec;
		BigInteger bigG;
		BigInteger bigP;
		
		// para não enviar valores com tamanhos diferents 
		do{
			dhSpec =  alice.getDHParameterSpec();	// get P and G 
			bigG = dhSpec.getG();
			bigP = dhSpec.getP();
		}while (bigG.bitLength() != DHP_SIZE || bigP.bitLength() != DHP_SIZE);	

		// ALICE generate numbers
		BigInteger a = alice.genExponent(dhSpec.getL());	// get a (exponent)
		BigInteger bigA = bigG.modPow(a, bigP);				// get A (g^a)
		
		// open connection whith "BOB"
		alice.openConnection(); 
		System.out.println (TAG+"open connection: OK");

		// send init values [ G (512) + P(512) + A (?) ]
		byte [] buff = concat (concat (bigG.toByteArray(), bigP.toByteArray()) , bigA.toByteArray());
		alice.oos.writeObject(buff);

		// recive  B calc keys and 
		buff = (byte []) alice.ois.readObject();
		SecretKey key1 = alice.getAESKey(new BigInteger(buff).modPow(a, bigP).toByteArray());  // B^a mod p 
		SecretKey key2 = alice.getAESKey(new BigInteger(buff).divide(bigA).modPow(a, bigP).toByteArray()); // (B/A)^a mod p

		// encrypt and send msgs => [ IV + Encr(MSG) ]
		alice.encryptAndSend (key1, msg1);
		alice.encryptAndSend (key2, msg2);

		// close connections 
		alice.oos.close();
		alice.ois.close();
		
		//printByteArrayToHex(key.getEncoded());
	}

	// Generate diffie-helman algorithm parameters (P & G)
	private DHParameterSpec getDHParameterSpec () throws Exception {
		AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DH");
		paramGen.init(DHP_SIZE);

		AlgorithmParameters params = paramGen.generateParameters();
		DHParameterSpec dhSpec = (DHParameterSpec) params.getParameterSpec(DHParameterSpec.class);
		return dhSpec;
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


	// encrypt and send message whith the specific key 
	// send to the open previusly ObjectOutpusStrem (oos) 
	private void encryptAndSend(SecretKey key, String msg) throws Exception {
		
		// generate random salt
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		
		// init cipher whith encrypt_mode
		Cipher cipher = Cipher.getInstance(ALGORITM_CIPHER);
		IvParameterSpec iv = new IvParameterSpec(salt);
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);

		// make packetge [ IV + Encr(MSG) ]
		byte[] buff = cipher.doFinal(msg.getBytes());
		buff = concat(salt, buff);
		// send pack
		oos.writeObject(buff); 
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

	// generate a(private value) => exponent  
	private BigInteger genExponent (int size){
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[size/8];
		random.nextBytes(bytes);
		return new BigInteger (bytes);
	}
}