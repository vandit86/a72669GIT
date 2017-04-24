
package com.test.dtnprotocol ; 

// outher imports 
import java.io.IOException;
import java.io.*;
import java.net.*;
import java.util.*;
import static java.lang.System.out;


/**
* autor : VADIM 
* descr : start main class initiate all sub classes and treads 
* 		: Delay Tolerant Network (DTN) algorithm main class 
* 		: 
* version : 1.0
*/

class DTN {

	// constuctor's 
	public DTN (){}
	public DTN (DTNInterface listener){
		this.listener = listener; 
	}

	// potocol values from config file  
	public static int MULTICAST_PORT;	// port for multicats receiving 			[9876]
	public static int HELLO_INTERVAL;	// interval beetwean send HELLO package		[3000]
	public static String MCAST_ADDR;	// Site-Local Scope Multicast Addresses 	[FF05:0:0:0:0:0:0:2]
	public static String IPV6; 			// my IPV6 address
	
	//public static final String MCAST_ADDR = "FF02:0:0:0:0:0:0:2"; // Link-Local Scope Multicast Addresses
	//public static final String MCAST_ADDR = "FF05:0:0:0:0:0:0:2";	// Site-Local Scope Multicast Addresses
	
	// protocol static variables 
	private static DatagramSocket dataSocket;  		// datagram socket opened for send data 
	public static InetAddress GROUP;	// multicast group 

	
	// protocol message id's
	public static final String[] TYPE = {"" , "HELLO" , "REQUEST", "HELLO_R"};	

	// protocol constants 
	public static final int HELLO = 1;		// para descobrir vizinhos 
	public static final int REQUEST = 2;	// pedido dos vizinhos 
	public static final int HELLO_R = 3;	// resposta para hello dos vizinhos 
	
	public static final int BUFFER_SIZE = 65504;	// maximum value for reciving or sending data 


	// if run programm not from java directory 
	// private final String CONFIG_FILE ="/home/vad/Documentos/PTI_2/java/conf/conf.txt";
	// cd /home/vad/Documentos/PTI_2/java/  
	private static final String CONFIG_FILE ="conf/conf.txt";
	private static final String DATA_DIR ="data/";

	// private vars : interface for upper leyear 
	private DTNInterface listener = null; 
	private int requestID = 0; 	// incremet when getData methos is colled 


	// outher values 
	//private final String USER_DIR = System.getProperty("user.dir"); // /tmp/pycore.38896/n1.conf
	//private static final Logger log = Logger.getLogger(SNMPAgent.class);
	
	/* 
	// Colled from Upper layer to send data , all data is send to main server
	// and retransmit with first gateway.
	// @return unique ID for all request's arrived from upper layer, or (-1) if some 
	// error is ocurred. When responce is come, this ID num is used for distinguis
	// request's  
	*/

	public int sendRequest (byte [] data ){
	 	// separar em partes co BUFFER_SIZE
	 	// start transmission 

		return ++requestID ; 
	}



	/* 
	**********************************************************************
	ENTER POINT TO START PROTOCOL WORCKING
	start send HELLO packs, neighbor discovering and packege 
	receiving 
	**********************************************************************
	*/

	public void init () throws Exception{

		//out.println ("user dir = " + USER_DIR);

		Properties prop = getAppProprieties();
		GROUP = InetAddress.getByName(MCAST_ADDR);	// create Group with multicats address

		// A datagram socket is the sending or receiving point for a packet delivery service.
		// once created used for all classe for send packs
		dataSocket = new DatagramSocket();	

		//getIP();
		
		Thread sender = new Thread (new HelloSender());
		Thread receiver = new Thread (new ReceiverUDP());
		
		sender.start();		// start send HELLO pack to multicast add 
		receiver.start();	// block until receive some UDP pack 

	}

	// set Listener of upper leyar 
	public void setListener (DTNInterface in){
		this.listener = in ; 
	}

	// return an opened socket 
	public static DatagramSocket getDatagramSocket (){
		return dataSocket; 
	}

	/*
	************************************************************************* 
		send data for specific DTNHost and well now common port usin this 
		datagram socket
	**************************************************************************
	*/
	public static synchronized void sendData (InetAddress inetAddress, byte[] data){
		try {
			DatagramPacket sendPacket = 
			new DatagramPacket(data, data.length, inetAddress, MULTICAST_PORT);
			dataSocket.send(sendPacket);

		} catch (Exception e) {
				//LOGGER.error(null, e);
			e.printStackTrace();
		}
	}

	/* 
	*****************************************************************
	Load Proprieties from config file in same directory 
	conf/config.txt
	*****************************************************************
	*/
	private Properties getAppProprieties (){
		
		Properties prop = new Properties();	
		InputStream input = null;
		try {

			input = new FileInputStream(CONFIG_FILE);
			// load a properties file
			prop.load(input);

			// //print values 
			// Enumeration<?> e = prop.propertyNames();
			// while (e.hasMoreElements()) {
			// 	String key = (String) e.nextElement();
			// 	String value = prop.getProperty(key);
			// 	System.out.println("Key : " + key + ", Value : " + value);
			// }

			MULTICAST_PORT = Integer.parseInt (prop.getProperty("MULTICAST-PORT"));
			HELLO_INTERVAL = Integer.parseInt (prop.getProperty("HELLO-INTERVAL")); 
			MCAST_ADDR = prop.getProperty("MCAST-ADDR");
			IPV6 = prop.getProperty("IPV6");

			// out.println (MULTICAST_PORT);
			// out.println (HELLO_INTERVAL);
			// out.println (MCAST_ADDR);
			// out.println (IPV6);


		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}


	/* *********************************************************************
	Show all interfaces and ip addres and alsow some aditional information 
	return last one ip addres of last interface
	 *********************************************************************
	*/
	private String getIP() throws SocketException {
		String s = null;
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
		for (NetworkInterface netint : Collections.list(nets)){
        // filters out 127.0.0.1 and inactive interfaces
			if (netint.isLoopback() || !netint.isUp())
				continue;
			out.printf("Display name: %s\n", netint.getDisplayName());
			out.printf("Name: %s\n", netint.getName());
			Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
			for (InetAddress inetAddress : Collections.list(inetAddresses)) {
				if (inetAddress instanceof Inet6Address)
					out.printf("InetAddress: %s\n", inetAddress);
			}

			// currentAddress instanceof Inet6Address

			// out.printf("Up? %s\n", netint.isUp());
			// out.printf("Loopback? %s\n", netint.isLoopback());
			// out.printf("PointToPoint? %s\n", netint.isPointToPoint());
			// out.printf("Supports multicast? %s\n", netint.supportsMulticast());
			// out.printf("Virtual? %s\n", netint.isVirtual());
			// out.printf("Hardware address: %s\n",
			// 	Arrays.toString(netint.getHardwareAddress()));
			// out.printf("MTU: %s\n", netint.getMTU());
			out.printf("\n");
		}
		return s;
	}


}






