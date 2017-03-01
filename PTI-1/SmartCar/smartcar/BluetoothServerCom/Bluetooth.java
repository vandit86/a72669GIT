package com.uminho.pti.smartcar.BluetoothServerCom;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

/**
 * Created by VAD on 06/12/2016.
 * <p>
 * this class provides bluetooth client-server communication between this application (client)
 * and server running on Laptop (special java program)
 * <p>
 * !!! Before start communication this devise must be PAIRED to server (Laptop) !!!!
 * <p>
 * Bluetooth class use BTListener object to notify CentralUnit class that this client receive PDU
 * from another client with the same application running on android-smatrfone, and send another information
 * <p>
 * Class start one Thread that control Input and Output stream
 * <p>
 * The same UUID for this Android and server application "04c6093b00001000800000805f9b34fb"
 * MAC serverAddress 18CF5E698849 of my acer-server
 * <p>
 * this parameters can be modified before calling startBluetoothConnection() method
 * <p>
 * getAdress() -> return String representation of MAC adress of this device,
 * can be used to fom uniq PDU sender
 * <p>
 * getSet_Of_PairedDevices() -> method return set of paired devices, so user can operate with their MAC´s and names
 * just if you wont to show list of paired devices to the user
 */

public class Bluetooth {
    private final String TAG = "myTag";
    public static final int ENABLE_BLUETOOTH = 1;
    public static final int DISCOVERY_REQUEST = 2;

    private Activity activity = null;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket btSocket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private byte[] mdata;

    // Well known SPP UUID
    private UUID MY_UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");
    ////////UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Insert your server's MAC serverAddress
    //private String serverAddress = "18:CF:5E:69:88:49"; // VAD
    private String serverAddress = "B8:86:87:4F:C3:A2";     // joni
    //B886874FC3A2

    private String myName;

    private BTListener btListener;      // call methods on CentralUnit

    /**
     * CONSTRUCTOR OF THIS CLASS *
     * must have BTListener object to sent received PDU bytes from server
     * server just transmit events for all clients, so we can receive aur awn PDU
     */
//    Bluetooth(Context c, BTListener btListener) {
//        this.activity = (Activity) c;
//        this.btListener = btListener;
//    }
    public Bluetooth(Context c) {
        this.activity = (Activity) c;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.d(TAG, "FATAL ERROR: Device does not support Bluetooth");
        }
        myName = bluetoothAdapter.getName();

    }

    /**
     * Set BT listener to receive events data from all user's
     */
    public void setBTListener(BTListener b) {
        this.btListener = b;
    }

    /**
     * start connection with  BT server on laptop
     * using well now UUID and MAC of server
     */
    public void startBluetoothConnection() throws IOException {

        if (bluetoothAdapter == null) {
            // Device does not support Bluetooth
            Log.d(TAG, "// Device does not support Bluetooth");
            throw new IOException("// Device does not support Bluetooth");
        }
        //  if bluetoothAdapter is not ON , enable user to set them on;
        if (!bluetoothAdapter.isEnabled()) {
            // Спросите пользователя, хочет ли он его включить.
            // ask user if want on
            Intent intent = new Intent
                    (BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(intent, ENABLE_BLUETOOTH);
            // method onActivityResult() is in MainActivity class
            // is starts initBluetoothUI() method if result is RESULT_OK ;
        } else {
            // Bluetooth-устройство включено.
            // Инициализируйте пользовательский интерфейс.
            initBluetoothUI();
        }
    }


    /**
     * show our addres in logs that confirm that bluetooth is OK
     * get Input and Output Stream in thread
     * and start Thread to listen BT port on incoming data
     */
    public void initBluetoothUI() {
        if (bluetoothAdapter == null) return;
        Log.d(TAG, "init bluetoothAdapter ui  : ");
        // can see serverAddress and name of bluetoothAdapter
        String address = bluetoothAdapter.getAddress(); // always return 02:00:00:00:00
        String name = bluetoothAdapter.getName();
        Log.d(TAG, "bluetoothAdapter Name : " + name);
        Log.d(TAG, "bluetoothAdapter serverAddress : " + address);
        // can use setName to change name of bluetoothAdapter adapter
        //bluetoothAdapter.setName("Blackfang");

        new Thread(listenServerTread).start();
    }

    /**
     * initiate thread for listening incoming data from server
     */
    private Runnable listenServerTread = new Runnable() {
        @Override
        public void run() {
            // Set up a pointer to the remote node using it's serverAddress.
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(serverAddress);
            // Two things are needed to make a connection:
            //   A MAC serverAddress, which we got above.
            //   A Service ID or UUID.  In this case we are using the
            //     UUID for SPP.
            try {
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);

            } catch (IOException e) {
                Log.d(TAG, "listing BT Server Tread 1 :" + e.getMessage());
            }
            // Discovery is resource intensive.  Make sure it isn't going on
            // when you attempt to connect and pass your message.
            if (bluetoothAdapter.isDiscovering())
                bluetoothAdapter.cancelDiscovery();

            // Establish the connection.  This will block until it connects.
            try {
                btSocket.connect();
            } catch (IOException e) {
                try {
                    btSocket.close();
                } catch (IOException e2) {
                    Log.d(TAG, "In BT ListenServerThread :  and unable to close socket during connection failure" + e2.getMessage() + ".");
                }
            }
            try {
                outputStream = btSocket.getOutputStream();
                inputStream = btSocket.getInputStream();
            } catch (IOException e) {
                Log.d(TAG, "Bluetooth : Cannot get Streams" + e.getMessage());
            }
            byte[] buffer = new byte[512];
            int num;
            try {
                while ((num = inputStream.read(buffer)) > 0) {
                    // send data to user Central Unit using BT Listenetr
                    if (btListener != null)
                        btListener.receivePDU(Arrays.copyOf(buffer, num));
                    else Log.d(TAG, "Bluetooth : no listener to receive data PDU");
                    //clearArray(buffer);
                }
            } catch (IOException e) {
                Log.d(TAG, "Bluetooth : Listen Server tread : cannot read from server:" + e.getMessage());
            }
        }
    };

    /**
     * Send PDU packet to server working in separate thread
     */

    public boolean sendPDU(byte[] data) {
        if (outputStream == null) return false;
        mdata = data;
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    outputStream.write(mdata);
                } catch (IOException e) {
                    Log.d(TAG, "In sendPDU() and an exception occurred during write: " + e.getMessage());
                }
            }
        };

        thread.start();
        return true;
    }

    /**
     * Return state of actual connection to server
     */
    public boolean isConnected() {
        return (btSocket.isConnected());
    }

    public void closeConnection() {
        if (btSocket != null) {
            try {
                btSocket.close();
            } catch (IOException e) {
                Log.d(TAG, "Bluetooth : Cannot close socket connection" + e.getMessage());
            }
        }
    }

    //  4
//          Show to user the devices hi already know
//        Прежде чем приступать к поиску устройств вокруг имеет смысл показать пользователю список уже
//        известных системе устройств. Вполне возможно, что требуемый телефон окажется в этом списке.
//        Метод getBondedDevices() возвращает множество (Set) устройств BluetoothDevice,
//        с которыми уже происходило соединение. Вы можете показать пользователю этот список,
//        например с помощью ArrayAdapter:

    public Set<BluetoothDevice> getSet_Of_PairedDevices() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        // Если список спаренных устройств не пуст
        if (pairedDevices.size() > 0) {
            // проходимся в цикле по этому списку
            for (BluetoothDevice device : pairedDevices) {
                // Добавляем имена и адреса в mArrayAdapter, чтобы показать
                // через ListView
                //mArrayAdapter.add(device.getName()+"\n"+ device.getServerAddress());
                Log.d(TAG, "paired device : " + device.getName() + "\t add : " + device.getAddress());
            }
        }
        return pairedDevices;
    }

    /**
     * set MAC adress of laptop BT server
     * must be in this for "00:00:00:00:00:00"
     */
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }


    /**
     * We nead MAC addres to sign PDU so we need call
     * this public method wen forming PDU
     */
    public String getServerAddress() {
        return serverAddress;
    }


    public String getMyName() {
        return myName;
    }
}


