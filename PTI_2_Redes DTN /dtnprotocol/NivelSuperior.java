package com.test.dtnprotocol ; 

// class do nivel superior que vai efetuar as chamasdas ao protocolo
// e receber packotes do nivel inferior 

public class NivelSuperior {
	
	//@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		
		// start protocol flow information, discower neighbors, 		
		// DTN dtn = new DTN ();
		// dtn.setListener (new DTNListener());
		
		// init and set listener 
		DTN dtn = new DTN (new DTNListener());
		try {
			dtn.init();
		}catch (Exception e){
			System.out.println ("Can't start DTN protocol");
			System.exit(1);
		}
		byte [] req = new byte [] {'1', 'r','q'};

		// send request retirn some integer to identify one request from anouther 
		dtn.sendRequest(req); 
	}



	// PRIVATE CLASS 
	// implement DNT interface for low leayar comunication 
	private static class DTNListener implements DTNInterface {
		
		// guardar data 
		public void storeData (byte [] data, DTNHost dtnHost){

		}

		// return null if no data is avalible for this request  
		public byte[] getedRequest (byte [] data){
			return null; 

		}

		// resposta recebida 
		public void getedResponse (byte [] data  , int idREquest ) {

		}
	}

}