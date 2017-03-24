/**
* file 		: SNMPAgent.java
* autor 	: VADIM  
* data 		: 30-01-2017
* 
* Main class to start snmp agent 
* used for parsing commnad line and get initial params from configuration file
* configure and start Agent - snmp 
* set and pass properties for ControllerMIB that initialize MIB scalar and tables 
* 
*/

package com.test.friendlySnmp;

import java.util.Enumeration;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.friendlysnmp.event.FExceptionListener;
import org.friendlysnmp.FriendlyAgent;
import org.friendlysnmp.FException;
import org.friendlysnmp.event.UncaughtExceptionListener;

public class SNMPAgent {
	
	private final String USER_DIR = System.getProperty("user.dir");
	private final String CONFIG_FILE = USER_DIR.concat("/conf/unpredictable-conf.txt");
	private final String IF_ADD ="0.0.0.0/";	// get-set requests from all outside on port (161)
	public final static String RESET_CMD = "RESET_CMD"; 

	// Key : COMMUNITY-STRING, Value : public
	// Key : UDP-PORT, Value : 161

	public void initAgent (String reset_cmd) throws FException {

		Properties propApp = getAppProprieties();	// get config information from unpredictable-conf.txt 
		Properties prop = new Properties();			// this prop for Agent config
		ControllerMIB cMib = new ControllerMIB ();	// controller of operation with MIB

		// set proprieties 
		// prop.getProperty("database");
		// prop.setProperty("database", "localhost");

		propApp.setProperty(RESET_CMD, reset_cmd);	// add reset_cmd propriety for MIB integration

		prop.put("snmp.address.get-set", IF_ADD.concat(propApp.getProperty("UDP-PORT")));
		prop.put("snmp.address.send-notification", "127.0.0.1/162");
		prop.put("snmp.v2.community", propApp.getProperty("COMMUNITY-STRING"));
		//prop.put("snmp.plugin.log4j", "org.friendlysnmp.plugin.log4j.PluginLog4j");

		FriendlyAgent agentSNMP = new FriendlyAgent("Unpredictable", "v1.0", prop);
		agentSNMP.addUncaughtExceptionListener(new UncaughtExceptionListener() {
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println ("UncaughtExceptionListener");
				//e.printStackTrace();
			}
		});

		cMib.initSNMP(agentSNMP, propApp);		// set hendlers and listner's for agent
		agentSNMP.init();
		agentSNMP.start();
		System.out.println ("Agent start ...");

		 // Add exception listener *after* successful agent init
		agentSNMP.addFExceptionListener(new FExceptionListener() {
			public void exceptionThrown(String msg, FException e) {
				System.out.println ("FException:: "+msg + e.getMessage());
			}
		});

	}


	// get Proprieties from config file whith path defined hardcoded
	private Properties getAppProprieties (){
		
		Properties prop = new Properties();	
		InputStream input = null;
		try {

			input = new FileInputStream(CONFIG_FILE);
		// load a properties file
			prop.load(input);

			// print values 
			// Enumeration<?> e = prop.propertyNames();
			// while (e.hasMoreElements()) {
			// 	String key = (String) e.nextElement();
			// 	String value = prop.getProperty(key);
			// 	System.out.println("Key : " + key + ", Value : " + value);
			// }

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


	public static void main (String [] args){

		//String add = "0.0.0.0/161";		// get-set requests from all outside on port 161
		if (args.length != 1 ){
			System.out.println ("Miss params : reset_cmd");
			return;
		}

		SNMPAgent snmpAgent = new SNMPAgent ();
		try{
			snmpAgent.initAgent(args[0]);
		}
		catch (FException fe){
			fe.printStackTrace();
		}
	}

}