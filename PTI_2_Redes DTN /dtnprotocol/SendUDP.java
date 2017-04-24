package com.test.dtnprotocol ; 

import java.io.IOException;
import java.io.*;
import java.net.*;
import java.util.*;
import static java.lang.System.out;

/*
  	send UDP pack to spesific address 
	create thread to send data for ipv6 address port (Multicast groupe port)
*/
class SendUDP implements Runnable {
	InetAddress inetAddress;
	int port;
	byte[] sendData;

	SendUDP (InetAddress inetAddress, int port, byte[] sendData){
		this.inetAddress = inetAddress;
		this.port = port;
		this.sendData = sendData;
	}

	public void run (){
		DatagramSocket serverSocket = null;
		try {
			serverSocket = DTN.getDatagramSocket();
			try {
				DatagramPacket sendPacket = 
				new DatagramPacket(sendData, sendData.length, inetAddress, port);
				serverSocket.send(sendPacket);
				
			} catch (Exception e) {
				//LOGGER.error(null, e);
				e.printStackTrace();
			}
		} catch (Exception e) {
			//LOGGER.error(null, e);
			e.printStackTrace();
		}

	}
}