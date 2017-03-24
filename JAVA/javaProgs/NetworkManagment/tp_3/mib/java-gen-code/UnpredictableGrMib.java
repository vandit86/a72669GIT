 

//--AgentGen BEGIN=_BEGIN
//--AgentGen END

import org.snmp4j.smi.*;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.*;
import org.snmp4j.agent.mo.snmp.*;
import org.snmp4j.agent.mo.snmp.smi.*;
import org.snmp4j.agent.request.*;
import org.snmp4j.log.LogFactory;
import org.snmp4j.log.LogAdapter;
import org.snmp4j.agent.mo.snmp.tc.*;



//--AgentGen BEGIN=_IMPORT
//--AgentGen END

public class UnpredictableGrMib implements MOGroup{

  private static final LogAdapter LOGGER = 
      LogFactory.getLogger(UnpredictableGrMib.class);

//--AgentGen BEGIN=_STATIC
//--AgentGen END

  // Factory
  private MOFactory moFactory = 
    DefaultMOFactory.getInstance();

  // Constants 

  /**
   * OID of this MIB module for usage which can be 
   * used for its identification.
   */
  public static final OID oidUnpredictableGrMib =
    new OID(new int[] { 1,3,6,1,4,1,3333 });

  // Identities
  // Scalars
  public static final OID oidUnpredictableN = 
    new OID(new int[] { 1,3,6,1,4,1,3333,1,1,1,0 });
  public static final OID oidUnpredictableR = 
    new OID(new int[] { 1,3,6,1,4,1,3333,1,1,2,0 });
  public static final OID oidUnpredictableD = 
    new OID(new int[] { 1,3,6,1,4,1,3333,1,1,3,0 });
  public static final OID oidUnpredictableRCmd = 
    new OID(new int[] { 1,3,6,1,4,1,3333,1,1,4,0 });
  // Tables

  // Notifications

  // Enumerations




  // TextualConventions

  // Scalars
  private MOScalar<UnsignedInteger32> unpredictableN;
  private MOScalar<UnsignedInteger32> unpredictableR;
  private MOScalar<UnsignedInteger32> unpredictableD;
  private MOScalar<OctetString> unpredictableRCmd;

  // Tables
  public static final OID oidUnpredictableEntry = 
    new OID(new int[] { 1,3,6,1,4,1,3333,1,2,1 });

  // Index OID definitions
  public static final OID oidRndIndex =
    new OID(new int[] { 1,3,6,1,4,1,3333,1,2,1,1 });

  // Column TC definitions for unpredictableEntry:
    
  // Column sub-identifier definitions for unpredictableEntry:
  public static final int colRndNumber = 2;

  // Column index definitions for unpredictableEntry:
  public static final int idxRndNumber = 0;

  private MOTableSubIndex[] unpredictableEntryIndexes;
  private MOTableIndex unpredictableEntryIndex;
  
  private MOTable<UnpredictableEntryRow,
                  MOColumn,
                  MOTableModel<UnpredictableEntryRow>> unpredictableEntry;
  private MOTableModel<UnpredictableEntryRow> unpredictableEntryModel;


//--AgentGen BEGIN=_MEMBERS
//--AgentGen END

  /**
   * Constructs a UnpredictableGrMib instance without actually creating its
   * <code>ManagedObject</code> instances. This has to be done in a
   * sub-class constructor or after construction by calling 
   * {@link #createMO(MOFactory moFactory)}. 
   */
  protected UnpredictableGrMib() {
//--AgentGen BEGIN=_DEFAULTCONSTRUCTOR
//--AgentGen END
  }

  /**
   * Constructs a UnpredictableGrMib instance and actually creates its
   * <code>ManagedObject</code> instances using the supplied 
   * <code>MOFactory</code> (by calling
   * {@link #createMO(MOFactory moFactory)}).
   * @param moFactory
   *    the <code>MOFactory</code> to be used to create the
   *    managed objects for this module.
   */
  public UnpredictableGrMib(MOFactory moFactory) {
  	this();
    createMO(moFactory);
//--AgentGen BEGIN=_FACTORYCONSTRUCTOR
//--AgentGen END
  }

//--AgentGen BEGIN=_CONSTRUCTORS
//--AgentGen END

  /**
   * Create the ManagedObjects defined for this MIB module
   * using the specified {@link MOFactory}.
   * @param moFactory
   *    the <code>MOFactory</code> instance to use for object 
   *    creation.
   */
  protected void createMO(MOFactory moFactory) {
    addTCsToFactory(moFactory);
    unpredictableN = 
      moFactory.createScalar(oidUnpredictableN,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new UnsignedInteger32());
    unpredictableR = 
      moFactory.createScalar(oidUnpredictableR,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new UnsignedInteger32());
    unpredictableD = 
      moFactory.createScalar(oidUnpredictableD,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY), 
                             new UnsignedInteger32());
    unpredictableRCmd = 
      new UnpredictableRCmd(oidUnpredictableRCmd, 
                            moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_WRITE));
    unpredictableRCmd.addMOValueValidationListener(new UnpredictableRCmdValidator());
    createUnpredictableEntry(moFactory);
  }

  public MOScalar<UnsignedInteger32> getUnpredictableN() {
    return unpredictableN;
  }
  public MOScalar<UnsignedInteger32> getUnpredictableR() {
    return unpredictableR;
  }
  public MOScalar<UnsignedInteger32> getUnpredictableD() {
    return unpredictableD;
  }
  public MOScalar<OctetString> getUnpredictableRCmd() {
    return unpredictableRCmd;
  }


  public MOTable<UnpredictableEntryRow,MOColumn,MOTableModel<UnpredictableEntryRow>> getUnpredictableEntry() {
    return unpredictableEntry;
  }


  @SuppressWarnings(value={"unchecked"})
  private void createUnpredictableEntry(MOFactory moFactory) {
    // Index definition
    unpredictableEntryIndexes = 
      new MOTableSubIndex[] {
      moFactory.createSubIndex(oidRndIndex, 
                               SMIConstants.SYNTAX_INTEGER, 1, 1)    };

    unpredictableEntryIndex = 
      moFactory.createIndex(unpredictableEntryIndexes,
                            false,
                            new MOTableIndexValidator() {
      public boolean isValidIndex(OID index) {
        boolean isValidIndex = true;
     //--AgentGen BEGIN=unpredictableEntry::isValidIndex
     //--AgentGen END
        return isValidIndex;
      }
    });

    // Columns
    MOColumn[] unpredictableEntryColumns = new MOColumn[1];
    unpredictableEntryColumns[idxRndNumber] = 
      moFactory.createColumn(colRndNumber, 
                             SMIConstants.SYNTAX_GAUGE32,
                             moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_ONLY));
    // Table model
    unpredictableEntryModel = 
      moFactory.createTableModel(oidUnpredictableEntry,
                                 unpredictableEntryIndex,
                                 unpredictableEntryColumns);
    ((MOMutableTableModel<UnpredictableEntryRow>)unpredictableEntryModel).setRowFactory(
      new UnpredictableEntryRowFactory());
    unpredictableEntry = 
      moFactory.createTable(oidUnpredictableEntry,
                            unpredictableEntryIndex,
                            unpredictableEntryColumns,
                            unpredictableEntryModel);
  }



  public void registerMOs(MOServer server, OctetString context) 
    throws DuplicateRegistrationException 
  {
    // Scalar Objects
    server.register(this.unpredictableN, context);
    server.register(this.unpredictableR, context);
    server.register(this.unpredictableD, context);
    server.register(this.unpredictableRCmd, context);
    server.register(this.unpredictableEntry, context);
//--AgentGen BEGIN=_registerMOs
//--AgentGen END
  }

  public void unregisterMOs(MOServer server, OctetString context) {
    // Scalar Objects
    server.unregister(this.unpredictableN, context);
    server.unregister(this.unpredictableR, context);
    server.unregister(this.unpredictableD, context);
    server.unregister(this.unpredictableRCmd, context);
    server.unregister(this.unpredictableEntry, context);
//--AgentGen BEGIN=_unregisterMOs
//--AgentGen END
  }

  // Notifications

  // Scalars
  public class UnpredictableRCmd extends MOScalar<OctetString> {
    UnpredictableRCmd(OID oid, MOAccess access) {
      super(oid, access, new OctetString());
//--AgentGen BEGIN=unpredictableRCmd
//--AgentGen END
    }

    public int isValueOK(SubRequest request) {
      Variable newValue =
        request.getVariableBinding().getVariable();
      int valueOK = super.isValueOK(request);
      if (valueOK != SnmpConstants.SNMP_ERROR_SUCCESS) {
      	return valueOK;
      }
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 0) && (os.length() <= 255)))) {
        valueOK = SnmpConstants.SNMP_ERROR_WRONG_LENGTH;
      }
     //--AgentGen BEGIN=unpredictableRCmd::isValueOK
     //--AgentGen END
      return valueOK; 
    }

    public OctetString getValue() {
     //--AgentGen BEGIN=unpredictableRCmd::getValue
     //--AgentGen END
      return super.getValue();    
    }

    public int setValue(OctetString newValue) {
     //--AgentGen BEGIN=unpredictableRCmd::setValue
     //--AgentGen END
      return super.setValue(newValue);    
    }

     //--AgentGen BEGIN=unpredictableRCmd::_METHODS
     //--AgentGen END

  }


  // Value Validators
  /**
   * The <code>UnpredictableRCmdValidator</code> implements the value
   * validation for <code>UnpredictableRCmd</code>.
   */
  static class UnpredictableRCmdValidator implements MOValueValidationListener {
    
    public void validate(MOValueValidationEvent validationEvent) {
      Variable newValue = validationEvent.getNewValue();
      OctetString os = (OctetString)newValue;
      if (!(((os.length() >= 0) && (os.length() <= 255)))) {
        validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
        return;
      }
     //--AgentGen BEGIN=unpredictableRCmd::validate
     //--AgentGen END
    }
  }


  // Rows and Factories

  public class UnpredictableEntryRow extends DefaultMOMutableRow2PC {

     //--AgentGen BEGIN=unpredictableEntry::RowMembers
     //--AgentGen END

    public UnpredictableEntryRow(OID index, Variable[] values) {
      super(index, values);
     //--AgentGen BEGIN=unpredictableEntry::RowConstructor
     //--AgentGen END
    }
    
    public UnsignedInteger32 getRndNumber() {
     //--AgentGen BEGIN=unpredictableEntry::getRndNumber
     //--AgentGen END
      return (UnsignedInteger32) super.getValue(idxRndNumber);
    }  
    
    public void setRndNumber(UnsignedInteger32 newColValue) {
     //--AgentGen BEGIN=unpredictableEntry::setRndNumber
     //--AgentGen END
      super.setValue(idxRndNumber, newColValue);
    }
    
    public Variable getValue(int column) {
     //--AgentGen BEGIN=unpredictableEntry::RowGetValue
     //--AgentGen END
      switch(column) {
        case idxRndNumber: 
        	return getRndNumber();
        default:
          return super.getValue(column);
      }
    }
    
    public void setValue(int column, Variable value) {
     //--AgentGen BEGIN=unpredictableEntry::RowSetValue
     //--AgentGen END
      switch(column) {
        case idxRndNumber: 
        	setRndNumber((UnsignedInteger32)value);
        	break;
        default:
          super.setValue(column, value);
      }
    }

     //--AgentGen BEGIN=unpredictableEntry::Row
     //--AgentGen END
  }
  
  class UnpredictableEntryRowFactory 
        implements MOTableRowFactory<UnpredictableEntryRow>
  {
    public synchronized UnpredictableEntryRow createRow(OID index, Variable[] values)
        throws UnsupportedOperationException 
    {
      UnpredictableEntryRow row = 
        new UnpredictableEntryRow(index, values);
     //--AgentGen BEGIN=unpredictableEntry::createRow
     //--AgentGen END
      return row;
    }
    
    public synchronized void freeRow(UnpredictableEntryRow row) {
     //--AgentGen BEGIN=unpredictableEntry::freeRow
     //--AgentGen END
    }

     //--AgentGen BEGIN=unpredictableEntry::RowFactory
     //--AgentGen END
  }


//--AgentGen BEGIN=_METHODS
//--AgentGen END

  // Textual Definitions of MIB module UnpredictableGrMib
  protected void addTCsToFactory(MOFactory moFactory) {
  }


//--AgentGen BEGIN=_TC_CLASSES_IMPORTED_MODULES_BEGIN
//--AgentGen END

  // Textual Definitions of other MIB modules
  public void addImportedTCsToFactory(MOFactory moFactory) {
  }


//--AgentGen BEGIN=_TC_CLASSES_IMPORTED_MODULES_END
//--AgentGen END

//--AgentGen BEGIN=_CLASSES
//--AgentGen END

//--AgentGen BEGIN=_END
//--AgentGen END
}


