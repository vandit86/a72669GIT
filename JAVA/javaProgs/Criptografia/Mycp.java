
import java.io.*;
import java.security.*;
import javax.crypto.*;


public class Mycp {
	
	public static void main (String [] args)  throws Exception{

	if (args.length != 3) {    
      System.err.println("Input arg error");
      System.exit(1);   
    }
    
    // Define the size of buffer that will be read at a time.
    int kBufferSize = 8192;


        BufferedReader inputStream = null;
        PrintWriter outputStream = null;

        try {
            inputStream = new BufferedReader(new FileReader(args[0]));
            outputStream = new PrintWriter(new FileWriter(args[1]));

            String l;
            while ((l = inputStream.readLine()) != null) {
                outputStream.println(l);
            }
            System.out.println("DONE!");
        }catch (IOException e){
        	System.err.println("Error: Canot copy file ");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }

	}
}

// import java.io.*;
// import java.security.*;
// import javax.crypto.*;

// public class  Test_RC4
// {
//      public static void main(String[] args) 
//      {
//     
//           try
//           {
//           KeyGenerator generator = KeyGenerator.getInstance( "RC4" );
//           generator.init(128);
//           SecretKey key = generator.generateKey();
//           ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream( "SecretKey.ser" ) );
//           out.writeObject(key);
//           out.close();
//         Cipher cipher = Cipher.getInstance( "RC4" );
//         if ( args[0].indexOf( "e" ) != -1 )
//         {
//             // Initialisation of cipher object for Encryption.
//             cipher.init( Cipher.ENCRYPT_MODE, key );
//         }
//         // If -d given on command line initialize the cipher object for Decrypt mode.
//         else
//         {
//             // Initialisation of cipher object for Decryption.
//             cipher.init( Cipher.DECRYPT_MODE, key );
//         }

// // Input stream for the file to be encrypted or decrypted.
//         FileInputStream   inputStream = new FileInputStream( args[1] );
//         // Output stream for output the file storing encrypted or decrypted data.
//         FileOutputStream outputStream = new FileOutputStream( args[2] );

//         // CipherOutputStream takes care of writing the file in encrypted or decrypted mode
//         // using cipher object according to the mode the cipher object is initialised to.
//         CipherOutputStream cipherOutputStream = new CipherOutputStream( outputStream, cipher );

//         byte[] buffer = new byte[kBufferSize];
//         int length;

//         while ( ( length = inputStream.read( buffer ) ) != -1 )
//         {
//             cipherOutputStream.write(buffer, 0, length);
//         }
//         // close all the streams.
//         inputStream.close();
//         cipherOutputStream.close();
//         outputStream.close();
//           }
//           catch (Exception ex)
//           {
//                ex.printStackTrace();
//           }
//      }
// }