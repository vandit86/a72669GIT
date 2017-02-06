import java.io.IOException;


/**
* autor : VADIM 
* descr : start main class initiate all sub classes and treads 
* 		: parsing cmd line argunents and set params to init depending processes
* 		: 
* version : 1.0
*/

public class SNMPMain {

	//@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		CmdLineParser lineParser = new CmdLineParser (args); // parsing cli arguments 
		int ifNum =0;
		SNMPGetter snmpGetter = new SNMPGetter (lineParser.getAddressPort()); // change port number 
		try{
			// start connection 
			snmpGetter.start();
			
			// get number of interfaces
			ifNum = Integer.parseInt (snmpGetter.getAsString(".1.3.6.1.2.1.2.1.0"));

		} catch (IOException exp){
			System.err.println ("Cannot get interfaces number " + exp);
			System.exit(1); 
		}

		if (ifNum == 0) System.exit(1);

		// initialise manager 
		SNMPManager client = new SNMPManager(snmpGetter);
		// initialize list of interfaces conform cmd line params 
		client.initInterfaceList(ifNum, lineParser.isLoopback(), lineParser.isAllInterfaces()); 

		// grafical representtion 
		SNMPGraff snmpGraff = new SNMPGraff (client, lineParser.getUpdateTime());
		snmpGraff.start(); 
		
	}
}