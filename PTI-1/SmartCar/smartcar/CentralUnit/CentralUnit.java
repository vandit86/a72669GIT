package com.uminho.pti.smartcar.CentralUnit;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import com.android.volley.RequestQueue;

import com.uminho.pti.smartcar.Data.Route;
import com.uminho.pti.smartcar.Data.User;
import com.uminho.pti.smartcar.Fragments.MapFragment;
import com.uminho.pti.smartcar.GPSLocation.GPSLocation;
import com.uminho.pti.smartcar.GPSLocation.LocEventListner;
import com.uminho.pti.smartcar.Local_DB.SmartCarDBHelper;
import com.uminho.pti.smartcar.ServerCom.ServerCom;
import com.uminho.pti.smartcar.ServerCom.Worker_Threads.CheckInternetWorker;
import com.uminho.pti.smartcar.Accelerometer.AccEventListner;
import com.uminho.pti.smartcar.Accelerometer.Acceler;

import com.uminho.pti.smartcar.BluetoothServerCom.BTListener;
import com.uminho.pti.smartcar.BluetoothServerCom.BT_Protocol;
import com.uminho.pti.smartcar.BluetoothServerCom.Bluetooth;
import com.uminho.vad.smartcar.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * CentralUnit é o class para juntar todas as nossas partes
 * contem os metodos necessarios e é editavel pelos todos particopantes neste projeto
 */

public class CentralUnit {

    private final String TAG = "myTag";
    private Acceler acc;
    private GPSLocation gps;
    private ServerCom server;
    private Context context;
    private User user;
    private Route route;

    private Bluetooth bluetooth;
    private BT_Protocol protocol;
    private LocationListner locationListner;
    private EventListener eventListener;

    private double pre_lat;
    private double pre_lon;
    private String pre_type;


    public CentralUnit(Context c, Acceler a, GPSLocation g, SmartCarDBHelper dbHelper, SQLiteDatabase local_db, User user, Route route, CheckInternetWorker checkInternet, RequestQueue queue, Bluetooth bluetooth, BT_Protocol BTProtocol) {
        this.context = c;
        this.acc = a;
        this.gps = g;

        this.bluetooth = bluetooth;
        this.protocol = BTProtocol;

        eventListener = new EventListener();
        locationListner = new LocationListner();

        this.server = new ServerCom(local_db, dbHelper, c, user, route, checkInternet, queue);

        acc.addListner(eventListener);         // add listener to events
        gps.addListener(locationListner);     // add listener to locations
        startBluetoothConnection();

        this.user = user;
        this.route = route;
    }
    /*********************VAD****************************************/

    /**
     * START BT connection in Thread
     */
    private void startBluetoothConnection() {

        bluetooth.setBTListener(new MyBTListener());

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    bluetooth.startBluetoothConnection();
                } catch (IOException e) {
                    Log.d(TAG, "BT ERROR :" + e.getMessage());
                }
            }
        };
        thread.start();
    }

    /*********************
     * VAD  *****  ACCELEROMETER
     **************************************/

    private class EventListener implements AccEventListner {

        @Override
        public synchronized void event(int ev) {
            switch (ev) {
                case Acceler.EVENT_Emergency_Stop:
                    Log.d(TAG, "Event : emergency stop");
                    break;
                case Acceler.EVENT_Transit:
                    Log.d(TAG, "Event : transito");
                    break;
                case Acceler.EVENT_Max_Speed:
                    Log.d(TAG, "Event : Max Velocidade");
                    break;
                case Acceler.EVENT_Hole:
                    Log.d(TAG, "Event : Obstaculo");
                    break;
                default:
                    Log.d(TAG, "Default event");
            }
            Log.d(TAG, " //");
            long time = System.currentTimeMillis();
            double lat = gps.getLatitude();
            double lon = gps.getLongitude();
            Log.d(TAG, "lat : " + lat);
            Log.d(TAG, "lon: " + lon);

            String msg = protocol.createObjectToSend(ev, time, lon, lat);
            SantosTrataDissoAgora(msg, true);   // send to ui level
            //SantosTrataDissoAgora(msg, false);   // comentar depois

            if (bluetooth.isConnected())
                bluetooth.sendPDU(msg.getBytes());
            else Log.d(TAG, "Not connected to server ");

//            Toast.makeText(getActivity(), "This is my Toast message!",
//                    Toast.LENGTH_LONG).show();

        }
    }

    /********************
     * VAD  *******  BLUETOOTH  **************************************
     * VAD :  BLUETOOTH server connection , receiving PDU's from another user's
     * class implements Interface BTListener that handel any incoming message or state changes of BT
     * communication
     */

    private class MyBTListener implements BTListener {

        @Override
        public void receivePDU(byte[] arr) {
            String str = new String(arr);
            //updateText(str);                   // show on UI screen
            if (protocol.parsingObject(str) == false)       // parsing json string  to obtain values ..
                SantosTrataDissoAgora(str, false);
        }

        @Override
        public void BTstate(int i) {

        }
    }


    /***********************
     * JSANTOS
     *****************************************/

    private class LocationListner implements LocEventListner {

        @Override
        public void location(Location loc) {
            double latitude = loc.getLatitude();
            double longitude = loc.getLongitude();
            long timestamp = System.currentTimeMillis();
            //Log.d(TAG, "Latitude: " + String.valueOf(latitude));
            //Log.d(TAG, "Longitude: " + String.valueOf(longitude));
            //Log.d(TAG, "Timestamp: " + String.valueOf(timestamp));
            server.sendRoutePosToServer(latitude, longitude, timestamp);

        }
    }

    public void endRoute() {
        server.endRouteToServer(0, 0, 0, user.getEmail(), route.getStart_stamp(), System.currentTimeMillis());
    }

    /**
     * isLocalEvent -> true se evento foi gerado no proprio dispositivo
     * -> false se vem de fora
     * <p>
     * jsonString -> json Object , podes fazer parsing se olhas para classe BT_POTOCOL
     * <p>
     * *
     */
    private void SantosTrataDissoAgora(String jsonString, boolean isLocalEvent) {
        try {
            Log.d(TAG, "SANTOS");
            JSONObject event = new JSONObject(jsonString);
            int type_int = (int) event.getLong(BT_Protocol.TYPE);
            String type = getEventTypeByCode(type_int);
            double lat = event.getDouble(BT_Protocol.LAT);
            double lon = event.getDouble(BT_Protocol.LON);
            Log.d(TAG, "...Lat: " + lat);
            Log.d(TAG, "...Lon: " + lon);

            if (isLocalEvent) {
                if(filterEvent(type,lat,lon,isLocalEvent)) {
                    server.sendEventToServer((int) event.getLong("type"), type, user.getEmail(), event.getLong("time"), event.getDouble("lat"), event.getDouble("lon"), 0);
                }
            } else {
                if (Acceler.EVENT_Hole == type_int || Acceler.EVENT_Transit == type_int || Acceler.EVENT_Emergency_Stop == type_int) {
                        RunonUserInterface(type);
                        MapFragment.getInstance().putMarker(type, lat, lon,getEventImage(type_int));
                    }
                server.sendEventToServer((int) event.getLong("type"), type, user.getEmail(), event.getLong("time"), event.optDouble("lat"), event.getDouble("lon"), 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean filterEvent(String type, double lat, double lon, boolean isLocalEvent){
        if(type.equals(pre_type) && lat == pre_lat && lon == pre_lon && isLocalEvent){
            return false;
        }else{
            pre_type=type;
            pre_lat=lat;
            pre_lon=lon;
            return true;
        }
    }

    private String getEventTypeByCode(int id) {
        switch (id) {
            case 1:
                return "brake";
            case 2:
                return "traffic";
            case 3:
                return "hole";
            case 4:
                return "speed";
            case 5:
                return "lomba";
        }
        return null;
    }

    private int getEventImage(int id) {
        switch (id) {
            case Acceler.EVENT_Emergency_Stop:
                return R.mipmap.brake_icon;
            case Acceler.EVENT_Hole:
                return R.mipmap.hole_icon ;
            case Acceler.EVENT_Max_Speed:
                return R.mipmap.vel_icon;
            case Acceler.EVENT_Transit:
                return R.mipmap.traffic_icon;
        }
        return 0;
    }

    private void RunonUserInterface(final String type) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText((Activity) context, "Atenção: ".concat(type), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
