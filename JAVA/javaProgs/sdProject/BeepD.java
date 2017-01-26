

// contem main() method 
// todos os utilizadores que querem participar no serviço BeeP tem correr este 
// progmamma 

// Argumentos que pode receber :
//        tempo enter beep's (só para servidor) 
//       

import java.nio.*;
import java.net.*;
import java.io.*;
import java.util.*;




// class principal 
public class BeepD {



  public static void main(String[] args) throws Exception {

    long startTime = 0, endTime =0;
    long interval = 0;          /*intervalo entre beeps so para servidor*/
    int PORT = BeepProtocol.DEFAULT_PORT;            // port to connect 
    String HOST = BeepProtocol.DEFAULT_HOST;     // host to connect 
    String s = null;

    PrintWriter out;          // socket out
    BufferedReader input;     // socket in 
    Socket cs = null;
    BeepThread bt = null;
    Thread t;

    if (args.length > 0) {    
      try {
                interval = Long.parseLong(args[0]);                       // argument - intervalo do tempo
                if (interval < 0) throw new NumberFormatException();
              } catch (NumberFormatException e) {
                System.err.println("Argument must be an positive integer.");
                System.exit(1);
              }
            }





          // get my ip OBTER O MEU ENDEREÇO IP
            String myIP = getIP();                    
         System.out.println(myIP);

          
            //List<String> lines = new ArrayList<String>();
            // LER LINHAS DUM FICHEIRO DEFINIDO EM BEEpPROTOCOL
            
          String line;
            try (
              InputStream fis = new FileInputStream(BeepProtocol.SERVER_LIST);
              InputStreamReader isr = new InputStreamReader(fis);
              BufferedReader br = new BufferedReader(isr);
              ) {
              while ((line = br.readLine()) != null) {
                // Deal with the line


              }
            }

            // for (String str : lines){
            //   System.out.println (str);
            // }


        bt = new BeepThread (interval);                           // start BeepThread
        t = new Thread (bt);
        t.start();



     // CLIENT CASE :
        if (interval == 0){

    while (true){

        try {
           cs = new Socket(HOST, PORT);                                     // try to connect to server 
         }catch (IOException io){
          System.out.println ("Can't connect to server : "+ HOST +" on port: "+PORT);

          System.exit(1);
        }
        
        out = new PrintWriter (cs.getOutputStream(), true);       // open in & out stream 
        input = new BufferedReader (
          new InputStreamReader(cs.getInputStream()));

        // 1)
        out.println(BeepProtocol.TRANS_TIME);                    // send request to the server 
        s = input.readLine();// get packege
        System.out.println("time: "+ s );
        bt.setDelay( Long.parseLong(s)) ; 
        
        // wait for flag is comming
        while ( ( bt.getDelayFlag()) == true) {
        };                                 
        
        // give time for client to enter in sleep with new delay 
        try{
          //Thread.sleep(bt.getDelay() / 2 );
          Thread.sleep(BeepProtocol.WAIT);
        }catch (Exception e){

        }

        // 2)
        long time;
        do{
          startTime = System.currentTimeMillis();                 // get start time 
          out.println(BeepProtocol.TRANS_DELAY);                   // send request for delay
          s = input.readLine();// get packege
          endTime = System.currentTimeMillis();                   // get end time 
        }while ((startTime - endTime)/2 >= (time = Long.parseLong(s)) );
        
        bt.setAtraso (time - (endTime - startTime)/2) ; // atraso 
        

        // 3)
        out.println(BeepProtocol.TRANS_END);                    // close connection 
        input.close();
        out.close();
        cs.close(); 

        Thread.sleep(BeepProtocol.RECONECT_TIME);

      }

      // while ((in.readLine()) != null);
      // t.interrupt();
      // t.join();

    }

     // SERVER CASE:

    else {


      ServerSocket ss = new ServerSocket (PORT); 

      new Thread (new ServerComLine(bt)).start();   // new Thread para cada cliente novo  
      
      while (true){
        cs = ss.accept();  
        new Thread (new GetConection( cs, bt)).start();   // new Thread para cada cliente novo  
      }
    }


  }

  // server command line thread 
  private static class ServerComLine implements Runnable {

      BeepThread bt ;
      ServerComLine(BeepThread bt){
        this.bt = bt;
      }
        public void run() {
          String s = null;
          BufferedReader in;        // teclado 
          try{
            in = new BufferedReader (             // open System.in stream 
            new InputStreamReader(System.in));

            while ((s = in.readLine()) != null){
              bt.setDelay(Long.parseLong(s));      // set new delay
            }
          }catch (Exception e){

        }

      }
  }


  // return last one ip addres of last interface
  public static String getIP() throws SocketException {
    String s = null;
    Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
    for (NetworkInterface netint : Collections.list(nets)){
        // filters out 127.0.0.1 and inactive interfaces
      if (netint.isLoopback() || !netint.isUp())
        continue;
      Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
      for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            //out.printf("InetAddress: %s\n", inetAddress);
        s = inetAddress.getHostAddress();
      }
    }
    return s;
  }
}

