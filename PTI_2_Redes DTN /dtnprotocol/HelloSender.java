package com.test.dtnprotocol ; 

import java.io.IOException;
import java.io.*;
import java.net.*;
import java.util.*;
import static java.lang.System.out;

/*
	send to Broadcast address udp HELLO packet's every HELLO_INTERVAL time 

*/ 

class HelloSender implements Runnable {

	public void run (){
		DatagramSocket serverSocket = null;
		try {
			//serverSocket = new DatagramSocket();
			serverSocket = DTN.getDatagramSocket();
			try {
				
				int i = 64; 
				//byte[] sendData = new byte [DTN.BUFFER_SIZE];
				//Arrays.fill (sendData, (byte)i++);
				
				while (true) {
					byte[] sendData = {DTN.HELLO};
					//byte[] sendData = DTN.TAG_HELLO.getBytes();
					DatagramPacket sendPacket = 
						new DatagramPacket(sendData, sendData.length, DTN.GROUP, DTN.MULTICAST_PORT);
					serverSocket.send(sendPacket);
					Thread.sleep(DTN.HELLO_INTERVAL);
				}
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