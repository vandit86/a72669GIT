/*
 * file      : UnpredictableFriendly.java
 * autor     : VADIM  
 * data      : 02-01-2017
 *
 * Class represent interface betwen Controller and MIB.. 
 * has methods to create new objects and get and set values 
 * class use Unpredictable.java generated with AgentPro for snmp4j lybrary use 
 *
 */ 
package com.test.friendlySnmp.mib;

import org.friendlysnmp.AgentWorker;
import org.friendlysnmp.FException;
import org.friendlysnmp.FScalar;
import org.friendlysnmp.FColumn;
import org.friendlysnmp.FTable;
import org.friendlysnmp.mib.BaseMib;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOServer;
import org.snmp4j.agent.mo.DefaultMOFactory;
import org.snmp4j.smi.OctetString;

public class UnpredictableFriendly extends BaseMib {

    private Unpredictable  mibORIG;

    // Scalars
    private FScalar unpred_N;
    private FScalar unpred_D;
    private FScalar unpred_R;
    private FScalar unpred_CMD;
    
      // Tables
    private FTable urlEntry;

    // Columns for table urlEntry
    public final static FColumn COLUMN_RundomNUM = 
    new FColumn("Unpredictable_RND",
        Unpredictable.idxRndNumber, 
        Unpredictable.colRndNumber);


    public UnpredictableFriendly () {
        super();
    } //

    @Override
    public void init(AgentWorker agent) throws FException {
        super.init(agent);
        mibORIG = new Unpredictable(DefaultMOFactory.getInstance());
        // Scalars
        unpred_D = new FScalar("unpred_D", mibORIG.getUnpredictableD(), agent);
        addNode(unpred_D);
        unpred_N = new FScalar("unpred_N", mibORIG.getUnpredictableN(), agent);
        addNode(unpred_N);
        unpred_R = new FScalar("unpred_R", mibORIG.getUnpredictableR(), agent);
        addNode(unpred_R);
        unpred_CMD = new FScalar("unpred_CMD", mibORIG.getUnpredictableRCmd(), agent);
        addNode(unpred_CMD);
        
       // Tables
        urlEntry = new FTable("urlEntry", mibORIG.getUnpredictableEntry(), agent,
            COLUMN_RundomNUM);
        addNode(urlEntry);

    } // init()

    @Override
    public void registerMOs(MOServer server, OctetString context)
    throws DuplicateRegistrationException
    {
        mibORIG.registerMOs(server, context);
    } // registerMOs()

    @Override
    public void unregisterMOs(MOServer server, OctetString context) {
        mibORIG.unregisterMOs(server, context);
    } // unregisterMOs()

    public FScalar getUnpred_N() {
        return unpred_N;
    } // getTicksCount()

    public FScalar getUnpred_R() {
        return unpred_R;
    } // getUserAge()

    public FScalar getUnpred_D() {
        return unpred_D;
    } // getUserDir()

    public FScalar getUnpred_CMD() {
        return unpred_CMD;
    } // getUserLevel()

    public FTable getUnpred_Entry() {
        return urlEntry;
    } // getUrlEntry()



} // class DemoScalarRoMibFriend
