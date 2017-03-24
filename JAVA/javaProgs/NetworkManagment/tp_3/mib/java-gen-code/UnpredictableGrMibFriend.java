import org.friendlysnmp.AgentWorker;
import org.friendlysnmp.FColumn;
import org.friendlysnmp.FException;
import org.friendlysnmp.FScalar;
import org.friendlysnmp.FTable;
import org.friendlysnmp.mib.BaseMib;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOServer;
import org.snmp4j.agent.mo.DefaultMOFactory;
import org.snmp4j.smi.OctetString;

public class UnpredictableGrMibFriend extends BaseMib {

    private UnpredictableGrMib mibORIG;

    // Scalars
    private FScalar unpredictableD;
    private FScalar unpredictableN;
    private FScalar unpredictableR;
    private FScalar unpredictableRCmd;

    // Tables
    // (not generated with trial license)
    private FTable unpredictableEntry;

    public UnpredictableGrMibFriend() {
        super();
    } // UnpredictableGrMibFriend()

    @Override
    public void init(AgentWorker agent) throws FException {
        super.init(agent);
        mibORIG = new UnpredictableGrMib(DefaultMOFactory.getInstance());
        // Scalars
        unpredictableD = new FScalar("unpredictableD", mibORIG.getUnpredictableD(), agent);
        addNode(unpredictableD);
        unpredictableN = new FScalar("unpredictableN", mibORIG.getUnpredictableN(), agent);
        addNode(unpredictableN);
        unpredictableR = new FScalar("unpredictableR", mibORIG.getUnpredictableR(), agent);
        addNode(unpredictableR);
        unpredictableRCmd = new FScalar("unpredictableRCmd", mibORIG.getUnpredictableRCmd(), agent);
        addNode(unpredictableRCmd);
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

    public FScalar getUnpredictableD() {
        return unpredictableD;
    } // getUnpredictableD()

    public FScalar getUnpredictableN() {
        return unpredictableN;
    } // getUnpredictableN()

    public FScalar getUnpredictableR() {
        return unpredictableR;
    } // getUnpredictableR()

    public FScalar getUnpredictableRCmd() {
        return unpredictableRCmd;
    } // getUnpredictableRCmd()

    public FTable getUnpredictableEntry() {
        return unpredictableEntry;
    } // getUnpredictableEntry()

} // class UnpredictableGrMibFriend
