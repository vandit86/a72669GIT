/**
* autor : VADIM 
* descr : manipulate with data obtained from SNMPGetter class 
*
*/


import java.io.IOException;
import java.util.ArrayList;
import java.util.List; 

import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.OID;

public class SNMPManager {

	private final int DATA_SIZE = 30;
	public static final String [] status = {"", "UP", "DOWN", "TESTING"};  
	
	ArrayList<Interface> ifList = null; 
	SNMPGetter snmpGetter = null ;  // use snmp4j lybrary to send request to agents 

	/**
	* Constructor
	* @param snmpGetter 
	*/

	public SNMPManager (SNMPGetter snmpGetter){
		this.snmpGetter = snmpGetter;
	}


	/**
	**********************************************************************************************************
	*/

	// make snmp-get request for one interface 
	// i 	=> interface 
	// push => push to arrys or not (firs value maust be ignored)
	public void updateData ( Interface inf ) {
		
		String desc = "";
		desc += inf.getIndex() + " : "; 

		// forming request for agent 
		OID [] oidsReq = new OID [3] ; 
		oidsReq [0] = new OID(Interface.OPER_STATUS+ inf.getIndex());  // ifOperStartus
		oidsReq [1] = new OID(Interface.IN_OCTETS  + inf.getIndex());	// ifInOctets 
		oidsReq [2] = new OID(Interface.OUT_OCTETS + inf.getIndex());	// ifOutOctets 

		ResponseEvent responceArr = null;
		int ifOperStatus;
		long ifInOctets, ifOutOctets;
		double ifIn=0.0, ifOut=0.0;

		try {
			responceArr = snmpGetter.get(oidsReq);
		}catch (IOException ex){
			// EXCEPTION WHEN TRY TO GET VALUES
			System.err.println("EXCEPTION WHEN TRY TO GET VALUES" + ex.getMessage());
		}

		ifOperStatus = 	responceArr.getResponse().get(0).getVariable().toInt();
		ifInOctets =  responceArr.getResponse().get(1).getVariable().toLong();
		ifOutOctets =  responceArr.getResponse().get(2).getVariable().toLong();

		// calculation 
		inf.setOperStatus(ifOperStatus); // set interface Operate status 

		if (ifOperStatus == 1){	// if oper satus == UP 

			ifIn = (double) (ifInOctets - inf.getPrevInOctets()); 
			inf.setPrevInOctets(ifInOctets);

			ifOut = (double) (ifOutOctets - inf.getPrevOutOctets()); 
			inf.setPrevOutOctets(ifOutOctets);

			// convert to kByte
			ifIn /= 1024;
			ifOut /= 1024;
		}

		// push new values to arrays
		pushToArray(inf.getInData(), ifIn);
		pushToArray(inf.getOutData(), ifOut);
		
		// forming string representation of values 
		desc += status[ifOperStatus]+" ";
		desc += String.valueOf(ifIn)+" ";
		desc += String.valueOf(ifOut)+" ";
		System.out.println(desc);
	}

	// push new value to array 
	private void pushToArray (double [] origin, double val){
		for (int i =0; i< origin.length -1 ; i ++ ){
			origin[i] = origin [i+1];
		}
		origin [origin.length-1] = val;
	}



/**
*  initiate array of interface's whith respective values 
*	can use filter to remove some unnecessary interface's 
*/
public void initInterfaceList (int ifNum, boolean isLoopback, boolean isAllInterfaces){
	ifList = new ArrayList<>(); 

	ResponseEvent responceArr = null;
	int ifOperStatus, ifIndex, ifType;
	long ifInOctets, ifOutOctets;
	String ifPhysAddress; 
	boolean samePhysAddress = false ; 

	// look into all interfaces , and filter them (if necessary) with cmd passed params   
	for (int i = 1 ; i<= ifNum; i++){
		String desc = i+": ";
		samePhysAddress = false ;

				// forming OID array
		OID [] oidsReq = new OID [6] ; 
		oidsReq [0] = new OID(Interface.OPER_STATUS+i);  // ifOperStartus
		oidsReq [1] = new OID(Interface.IN_OCTETS+i);	// ifInOctets 
		oidsReq [2] = new OID(Interface.OUT_OCTETS+i);	// ifOutOctets 
		oidsReq [3] = new OID(Interface.INDEX+i);		// ifIndex 
		oidsReq [4] = new OID(Interface.TYPE+i);		// ifType 
		oidsReq [5] = new OID(Interface.PHYS_ADDRESS+i);// ifPhysAddress

		try {
			responceArr = snmpGetter.get(oidsReq);
		}catch (IOException ex){
					// EXCEPTION WHEN TRY TO GET VALUES
			System.err.println("EXCEPTION WHEN TRY TO GET VALUES" + ex.getMessage());
		}

				// parsing responce from agent 
		ifOperStatus = 	responceArr.getResponse().get(0).getVariable().toInt();
		ifInOctets =  responceArr.getResponse().get(1).getVariable().toLong();
		ifOutOctets =  responceArr.getResponse().get(2).getVariable().toLong();
		ifIndex = 	responceArr.getResponse().get(3).getVariable().toInt();
		ifType = 	responceArr.getResponse().get(4).getVariable().toInt();
		ifPhysAddress =	responceArr.getResponse().get(5).getVariable().toString();

		// control to don't add loopback interface to the ifList 
		if (ifType == Interface.LOOPBACK && isLoopback == false) continue;
		
		//control to don't add down interfaces into list 
		if (ifOperStatus == Interface.STATUS_DOWN && isAllInterfaces == false) continue;

		// controll of repiting phys addresess 
		for (Interface _i : ifList){
			if ( _i.getPhysAddress().equals (ifPhysAddress)){
				samePhysAddress = true ; 
				break ;
			}
		}
		if (samePhysAddress) continue;

		// create new interfece object and add to array
		Interface _interface = new Interface(ifIndex, ifType, ifOperStatus, ifInOctets, ifOutOctets, ifPhysAddress);
		ifList.add( _interface );

		// forming string representation of values 
		desc += "Index: "+ifIndex+"; ";
		desc += "OPER STATUS: "+status[ifOperStatus]+"; ";
		//desc += "IN_OCTETS: "+ifInOctets+"; ";
		//desc += "OUT_OCTETS: "+ifOutOctets+"; ";
		desc += "TYPE: "+ifType+"; ";
		desc += "PHYS_ADDRESS: "+ifPhysAddress+"; ";
		System.out.println(desc);
	}

	System.out.println ("ADDED "+ ifList.size()+ " interfaces");
}

/**  
*	return list of interfeces 
*/
public List<Interface> getIfList (){
	return this.ifList;
}
}

/*****************************************************************************************************/

