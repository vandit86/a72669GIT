

package com.test.dtnprotocol ; 

import java.io.IOException;
import java.io.*;
import java.net.*;
import java.util.*;
import static java.lang.System.out;

// CLASS receive from broadcats addres UDP pack's 

class ReceiverUDP implements Runnable {

	public void run (){
		MulticastSocket multicastSocket = null;
		
		try {
			multicastSocket = new MulticastSocket(DTN.MULTICAST_PORT);
			multicastSocket.joinGroup(DTN.GROUP);
			int i =0; 
			byte[] receiveData = new byte[DTN.BUFFER_SIZE];
			while (true) {
				try {
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					
					//block until receive some UDP package 
					multicastSocket.receive(receivePacket);

					// thread to hendle incoming massages 
					ReceiverUDPHendler rcHendler = new ReceiverUDPHendler (receivePacket); 
					rcHendler.start();

				} catch (Exception e) {
					//LOGGER.error(null, e);
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			//LOGGER.error(null, e);
			e.printStackTrace();
		} finally {
			multicastSocket.close();
		}
	}

	/*
	* 	Thread to hanlde UDP package's from multicast address
	*/

	private class ReceiverUDPHendler extends Thread  {
		private DatagramPacket receivePacket;
		private byte[] hello_responce = {DTN.HELLO_R};

		ReceiverUDPHendler (DatagramPacket receivePacket){
			this.receivePacket = receivePacket;
		}

		public void run (){

			InetAddress inetAddress = receivePacket.getAddress();

		//*** skip my own hello-msg *** 
		//if (inetAddress.getHostAddress().equals(DTN.IPV6)) continue ; 

			int port = receivePacket.getPort();
			int dataLength = receivePacket.getLength(); 
			byte [] data = Arrays.copyOfRange(receivePacket.getData(), 0, dataLength);
			String type = DTN.TYPE[data[0]];

			out.println("from : " + inetAddress.getHostAddress() + ", " 
				+ type + "\tlength : "+dataLength + "  port : "+port );
			
			// send responce if this is HELLO 
			if (type.equals("HELLO")){
				//new Thread(new SendUDP (inetAddress, DTN.MULTICAST_PORT, hello_responce )).start();
				DTN.sendData(inetAddress, hello_responce);	// sending but not in separete thread 
			}

		}

	}

}

