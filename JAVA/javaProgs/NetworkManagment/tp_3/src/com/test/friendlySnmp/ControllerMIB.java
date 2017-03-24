/*
 * File     : ControllerMIB.java
 * Autor    : VADIM 
 * Data     : 30-01-2016
 *
 * Class provid interface betwean Unpradictable_MIB and Agent 
 * control Thread's and objects in MIB 
 * set initial params for mib scalar objects 
 * controll all request's from clients
 * make values updateing, input control and threads controll
 *  
 */ 
package com.test.friendlySnmp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.ArrayList;

import org.friendlysnmp.FException;
import org.friendlysnmp.FHandler;
import org.friendlysnmp.FScalar;
import org.friendlysnmp.FTable;
import org.friendlysnmp.FID;
import org.friendlysnmp.FriendlyAgent;
import org.friendlysnmp.event.FScalarGetListener;
import org.friendlysnmp.event.FScalarSetListener;
import org.friendlysnmp.event.FExceptionListener;

import com.test.friendlySnmp.mib.*;

//@SuppressWarnings("serial")
public class ControllerMIB  {


 // Key : TABLE-SIZE, Value : 256
 // Key : NUMBER-SIZE, Value : 8
 // Key : REFRESH-RATE, Value : 1
 // Key : FIRST-SEED, Value : /usr/share/unpredictable/1st-seed

    // work with integers java 8
    // public static int parseUnsignedInt(String s,
    //                                int radix)
    //                         throws NumberFormatException

    // public static String toHexString(int i)
    // Returns a string representation of the integer argument as an unsigned integer in base 16.

    // UnpredictableInterface


    //private TickerThread threadTicker;
	private UnpredictableFriendly mib;
	private Properties prop;
	private FTable table;
    private ArrayList<String> numsInString ;

    // Constructor 
    public ControllerMIB () { 
    }


    // init MIB variavels and set listeners for objects 
    public void initSNMP(FriendlyAgent agent , Properties propApp) throws FException {

      prop = propApp;
      numsInString = new ArrayList<>();

        mib = new UnpredictableFriendly();      // load MIB to java interface  
        mib.addHandler(new ThisHandler());      // add hendler to the MIB  
        agent.addMIB(mib);                      // add my MIB to agent 

        table = mib.getUnpred_Entry();
        try {
        	table.setDefaultValues(0);
        } catch (FException e) {
        	e.printStackTrace();
        }


        //example of  GET listener of Scalar object 
        mib.getUnpred_CMD().addGetListener(new FScalarGetListener() {
        	@Override
        	public void get(FScalar scalar) {
        		System.out.println("GET_CMD request");
        	}
        });

        // set listener for 
        mib.getUnpred_CMD().addSetListener(new FScalarSetListener () {
            @Override
            public void set(FScalar scalar) {
                String cmd = prop.getProperty(SNMPAgent.RESET_CMD);
                String val = (String)scalar. getValue();
                System.out.println("SET_CMD request with value : "+ val);
                
                // compare string with reset command  
                if (val.equals(cmd)){
                    // scalar.setValueEx(val);
                    // send interrupt (stop refresh tread) 
                    // setRowsFromSeed() // load numbers from seed 
                    // start new refresh tread  
                }
                else {
                    scalar.setValueEx(cmd); 

                }
            }
        });

    } // initSNMP()


    // hendler for this MIB start , init , stop method invocation 
    private class ThisHandler extends FHandler {
        /**
        *The method is called by SNMP agent when it's initialized. Default implementation does nothing. 
        *used to initialize static scalars and tables.
        */
        @Override
        public void init() {

            // set values in MIB vith prop recived from agent
        	System.out.println("Hendler init");
        	mib.getUnpred_CMD().setValueEx(prop.getProperty(SNMPAgent.RESET_CMD));
        	mib.getUnpred_N().setValueEx(prop.getProperty("TABLE-SIZE"));
        	mib.getUnpred_D().setValueEx(prop.getProperty("NUMBER-SIZE"));
        	mib.getUnpred_R().setValueEx(prop.getProperty("REFRESH-RATE"));

            setRowsFromSeed();  // set rows 


        } // init()
        
        /**
        *The method is called by SNMP agent when it's started or restarted. Default implementation does nothing.
        *used to start threads.
        */
        @Override
        public void start(AgentStartType startType) {
        	System.out.println("Hendler start");
            // threadTicker = new TickerThread();
            // threadTicker.start();
        } // start()

        /**
        * The method is called by SNMP agent when it's stopped. Default implementation does nothing. 
        * Override this method in derived class, for example, to stop threads.
        */
        @Override
        public void stop() {
        	System.out.println("Hendler stop");
            // if (threadTicker != null) {
            //     threadTicker.shutdown = true;
            //     threadTicker.interrupt();
            //     threadTicker = null;
            // }
        } // stop()
        
        @Override
        public void shutdown() {
        	System.out.println("Hendler shutdown");
        	stop();
        } // shutdown()

    } // inner class ThisHandler


    // this method used for initialize rows vales from seed file 
    // alsow when user send restart command 
    private void setRowsFromSeed()
    {
        try (BufferedReader br = new BufferedReader(new FileReader(prop.getProperty("FIRST-SEED"))))
        {
            numsInString.clear();
            table.deleteAll();  // Deletes all rows.
            String line;
            while ((line = br.readLine()) != null) {
                numsInString.add(line);                                         // add to string list
            	//lNum = 0xffffffffL & Integer.parseUnsignedInt(line, 16);        // convert to long
                // Load data directly into the agent
                FID idRow = table.addRowNext();                                 //  make and add row to table  
                table.setValueAt( line, idRow, UnpredictableFriendly.COLUMN_RundomNUM );
            }
            System.out.println("Rows created = " +  table.getRowCount());                       // print num 

        } catch (Exception e)
        {
            System.out.println("Can't open file :"+prop.getProperty("FIRST-SEED"));
            e.printStackTrace();
        }
    }   // setRowsfromSeed()


    /**
    * Class implement interface betwen refresh class and controller 
    * handle event from thread that refresh table "numbers"
    * and put new values to MIB table  
    */
    public class RefreshHandler implements UnpredictableInterface {

        public void refreshTable(String[] arr){
            System.out.println ("Table refresh call");
            try {
                //table.getRowCount();
                table.deleteAll();
                for(String num : arr){
                     FID idRow = table.addRowNext();   //  make and add row to table  
                     table.setValueAt( num, idRow, UnpredictableFriendly.COLUMN_RundomNUM );
                 }
             }
             catch (FException e) {
                e.printStackTrace();
            }
        }
    }

} // class ControllerMIB




    // /**
    // * Some thread to execute tascks in background
    // */

    // private class TickerThread extends Thread {
    //     private volatile boolean shutdown;
    //     public void run() {
    //         startCount++;
    //         System.out.println ("TickerThread: START");
    //         while (!shutdown) {
    //             try {
    //                 Thread.sleep(1000);
    //             } catch (InterruptedException e) {
    //             }
    //             if (!shutdown) {
    //                 ticks++;

    //             }
    //         }
    //     }
    // } // inner class TickerThread     



// private enum UserLevel { 
//         BEGINNER    (DemoUserLevelTC.beginner), 
//         INTERMEDIATE(DemoUserLevelTC.intermediate), 
//         EXPERIENCED (DemoUserLevelTC.experienced), 
//         GURU        (DemoUserLevelTC.guru);

//         int level;
//         UserLevel(int level) {
//             this.level = level;
//         }
//         public String toString() {
//             return PaneBase.toCamelCase(super.toString());
//         }
//     } // enum UserLevel

