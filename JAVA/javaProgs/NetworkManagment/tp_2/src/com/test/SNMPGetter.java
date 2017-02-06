
/**
* autor : VADIM 
* descr : class use snmp4j lybrary methods to send request and obtain responce 
* 		: from remote host 
*/
import java.io.IOException;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;



/**
* OID - .1.3.6.1.2.1.1.1.0 			=> SysDec
* OID - .1.3.6.1.2.1.1.5.0 			=> SysName
* OID - .1.3.6.1.2.1.2.2.1.  		=> ifEntry
*
* OID - .1.3.6.1.2.1.2.1.0 			=> ifNumber
* OID - .1.3.6.1.2.1.2.2.1.6.	 	=> ifPhysAddress 
* OID - .1.3.6.1.2.1.2.2.1.3.	 	=> ifType 
* OID - .1.3.6.1.2.1.2.2.1.8. 		=> ifOperStatus
* OID - .1.3.6.1.2.1.2.2.1.10. 		=> ifInOctets
* OID - .1.3.6.1.2.1.2.2.1.16. 		=> ifOutOctets
* 
*/


public class SNMPGetter
{
	// Class Variables 
	Snmp snmp = null;
	String address = null;

	public SNMPGetter(String add){
		/**
		* Port 161 is used for Read and Other operations
		* Port 162 is used for the trap generation
		* "udp:127.0.0.1/161"
		*/
		address = add ;
		
	}

	/**
	* Start the Snmp session. If you forget the listen() method you will not
	* get any answers because the communication is asynchronous
	* and the listen() method listens for answers.
	* @throws IOException
	* @SuppressWarnings("unchecked")
	*/
	@SuppressWarnings("unchecked")
	public void start() throws IOException {
		TransportMapping transport = new DefaultUdpTransportMapping();
		snmp = new Snmp(transport);
	// Do not forget this line!
		transport.listen();
	}

	/**
	* Method which takes a single OID and returns the response from the agent as a String.
	* @param oid
	* @return
	* @throws IOException
	*/
	public String getAsString(String ns ) throws IOException {
		ResponseEvent event = get(new OID[] { new OID(ns) });
		return event.getResponse().get(0).getVariable().toString();
	}

	/**
	* This method is capable of handling multiple OIDs
	* @param oids
	* @return
	* @throws IOException
	*/
	public ResponseEvent get(OID oids[]) throws IOException {
		PDU pdu = new PDU();
		for (OID oid : oids) {
			pdu.add(new VariableBinding(oid));
		}
		pdu.setType(PDU.GET);
		ResponseEvent event = snmp.send(pdu, getTarget(), null);
		if(event != null) {
			return event;
		}
		throw new RuntimeException("GET timed out");
	}

	/**
	* This method returns a Target, which contains information about
	* where the data should be fetched and how.
	* @return
	*/
	private Target getTarget() {
		Address targetAddress = GenericAddress.parse(address);
		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString("public"));
		target.setAddress(targetAddress);
		target.setRetries(2);
		target.setTimeout(1500);
		target.setVersion(SnmpConstants.version2c);
		return target;
	}

}