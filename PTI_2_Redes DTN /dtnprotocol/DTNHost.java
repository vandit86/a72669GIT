package com.test.dtnprotocol ; 
import java.net.*;

/*
	Class that represent information about DTN host in network 
*/
class DTNHost {
	private String ipAddr;
	private InetAddress inetAddr;

	DTNHost (String ipAddr){
		this.ipAddr = ipAddr;
	}

	DTNHost (InetAddress add){
		inetAddr = add; 
	}

	// return ipAddr of this DTN host 
	public String getIpAddr (){
		return this.ipAddr; 
	}

	public InetAddress getInetAddr (){
		return this.inetAddr; 
	}
	

}