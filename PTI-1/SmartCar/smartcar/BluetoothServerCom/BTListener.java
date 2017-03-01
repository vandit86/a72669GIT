package com.uminho.pti.smartcar.BluetoothServerCom;

/**
 * Created by VAD on 11/01/2017.
 */

public interface BTListener {
    void receivePDU(byte [] arr);   // synchronized  method that receive all PDU from another clients
    void BTstate (int i);           // signaling of BT state
}
