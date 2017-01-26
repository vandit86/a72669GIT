/**
* class use lybrary to parce command line input arguments 
*/

import org.apache.commons.cli.*; 

public class CmdLineParser {

	private boolean loopback = false;
	private String ip_port = "udp:";		// ip add and port of snmp agent  
	private int updateTime = 1000; 
	// constructor 
	CmdLineParser(String[] args){
		parse (args);
	}

	public void parse (String[] args){
		// create Options object
		Options options = new Options();

		// add lo option
		options.addOption("lo", false, "set this flag if you whont see loopback interface");

		options.addOption("t", true, "time in milliseconds to send request and refresh chart (must be >= 1000)");

		// add a option thet requaire parameters of this type : "127.0.0.1/161"
		options.addOption("a", true, "address and port of snmp-agent");

		try {
			// cmd line default parser  
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse( options, args);
			
			if(cmd.hasOption("lo")) {
				loopback = true ; 
			}
			
			// get "a" option value
			String address_port = cmd.getOptionValue("a");

			if(address_port == null) {		// null if no any argument value is specified on the command line 
    			// put default port and address
				ip_port = "udp:127.0.0.1/161";
			}
			else {
				ip_port += address_port;
			}

			String time = cmd.getOptionValue("t");
			if (time != null){
				updateTime = Integer.parseInt(time);
				//System.out.println ("time = "+ updateTime);
				if (updateTime < 1000) updateTime = 1000; 
			}
			else {			
				//System.out.println ("time is default = "+ updateTime);
			}

		}catch (ParseException ex){
			System.err.println (ex);
		}
	}

	// return true if user wont see loopback interface chart 
	public boolean isLoopback (){
		return this.loopback; 
	}

	public String getAddressPort (){
		return this.ip_port;
	}

	public int getUpdateTime (){
		return this.updateTime; 
	}

}