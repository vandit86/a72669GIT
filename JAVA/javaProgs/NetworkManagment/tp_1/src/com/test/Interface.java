

/**
*  Class interface represent each interface detected on remoute host 
*  and all related parameters with this interface , lyke : 
*
*
* OID - .1.3.6.1.2.1.2.2.1.1(.*)  	=> ifIndex
* OID - .1.3.6.1.2.1.2.2.1.  		=> ifEntry
* OID - .1.3.6.1.2.1.2.1.0 			=> ifNumber
* OID - .1.3.6.1.2.1.2.2.1.6.	 	=> ifPhysAddress 
* OID - .1.3.6.1.2.1.2.2.1.3.	 	=> ifType 
* OID - .1.3.6.1.2.1.2.2.1.8. 		=> ifOperStatus
* OID - .1.3.6.1.2.1.2.2.1.10. 		=> ifInOctets
* OID - .1.3.6.1.2.1.2.2.1.16. 		=> ifOutOctets
*
* types :
*  6 - ethernet 
* 24 - loopback
* 71 - wifi 
* 
*/

public class Interface {

	public static final String [] STATUS = {"", "UP", "DOWN", "TESTING"};
	public static final int DATA_SIZE = 30;

	public static final int LOOPBACK = 24;

	public static final String INDEX 	  =	".1.3.6.1.2.1.2.2.1.1.";
	public static final String TYPE 	  =	".1.3.6.1.2.1.2.2.1.3.";
	public static final String OPER_STATUS= ".1.3.6.1.2.1.2.2.1.8.";
	public static final String IN_OCTETS  = ".1.3.6.1.2.1.2.2.1.10.";
	public static final String OUT_OCTETS = ".1.3.6.1.2.1.2.2.1.16.";
	public static final String PHYS_ADDRESS = ".1.3.6.1.2.1.2.2.1.6.";


	double [] yInData , yOutData;	// in and out octets 
	int ifIndex;				// index in MIB table 
	int ifType ; 				// type of interface 
	String ifPhysAddress ;		// fisical address
	int ifOperStatus ; 			// ifOperStatus [1,2,3]

	long prevInOctets , prevOutOctets; 	// values previus 


	/**
	* constructor resive number of ifIndex in MIB table to be monitoring 
	*/

	public Interface (int ifIndex, int type, int status, long inOct, long outOct, String ifPhysAddress){
		yInData = new double [DATA_SIZE];
		yOutData = new double [DATA_SIZE];
		this.ifIndex = ifIndex;
		this.ifType = type;
		this.ifOperStatus = status;
		this.prevInOctets = inOct;
		this.prevOutOctets = outOct;
		this.ifPhysAddress = ifPhysAddress;

	}

	/********************** GETTERS AND SETTERS ***********************************************************/

	public int getIndex (){
		return ifIndex;
	}

	public String getPhysAddress (){
		return this.ifPhysAddress;
	}


	public String getTypeAsString(){
		switch (this.ifType){
			case 6  : return "ethernet";
			case LOOPBACK : return "Loopback";
			case 71 : return "WiFi";
			default : return "Ucknow type :"+ifType; 
		}
	}
	
	public String getOperStatusAsString (){
		return STATUS[ifOperStatus];
	}

	public int getOperStatus (){
		return this.ifOperStatus;
	}

	public double [] getInData (){
		return yInData;
	}

	public double [] getOutData (){
		return yOutData;
	}

	public void setOperStatus (int ifOperStatus){
		this.ifOperStatus = ifOperStatus;
	}

	public long getPrevInOctets (){
		return this.prevInOctets;
	}

	public long getPrevOutOctets (){
		return this.prevOutOctets;
	}

	public void setPrevInOctets (long i){
		this.prevInOctets = i;
	}

	public void setPrevOutOctets (long i){
		this.prevOutOctets = i;
	}

}