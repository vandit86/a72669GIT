import java.io.IOException;


/**
*	start main class 
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
		client.initInterfaceList(ifNum, lineParser.isLoopback()); // initialize list of interfaces 

		// grafical representtion 
		SNMPGraff snmpGraff = new SNMPGraff (client, lineParser.getUpdateTime());
		snmpGraff.start(); 
		
	}
}