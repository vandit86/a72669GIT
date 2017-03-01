package com.uminho.pti.smartcar.BluetoothServerCom;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by VAD on 22/11/2016.
 * <p>
 * BT_Protocol class is responcebel for forming and sending JSON object
 * and create new PDU unit when data is comming
 */

public class BT_Protocol {

    private static final String TAG = "myTag";
    private String mac;  // the mac is added to the all sanded pdu
    public static final String TYPE = "type";
    public static final String MAC = "mac";
    public static final String LON = "lon";
    public static final String LAT = "lat";
    public static final String TIME = "time";


    public BT_Protocol(String mac) {
        this.mac = mac;
    }

    /**
     * method forming json object and return this string representation ready to send to server
     * recives all types
     */
    public String createObjectToSend(long type, long time, double lon, double lat) {
        JSONObject stu = new JSONObject();
        try {
            stu.put(MAC, mac);
            stu.put(TYPE, type);
            stu.put(LON, lon);
            stu.put(LAT, lat);
            stu.put(TIME, time);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return stu.toString();

    }

    /**
     * return true if this message is was send by own user
     * controlling MAC camp
     */
    public boolean parsingObject(String jsonStr) {
        Log.d(TAG, "Response from BT server: " + jsonStr);
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                String jmac = jsonObj.getString(MAC);

                if (mac.compareTo(jmac)==0) {
                    return true;
                }
                return false;
//                long type = (long)jsonObj.getInt(TYPE);
//                double lat = jsonObj.getDouble(LAT);
//                double lon = jsonObj.getDouble(LON);
//                long time = jsonObj.getLong(TIME);

                // tmp hash map for single contact
                //HashMap<String, String> contact = new HashMap<>();

                // adding each child node to HashMap key => value
//                    contact.put("id", id);
//                    contact.put("name", name);
//                    contact.put("email", email);
//                    contact.put("mobile", mobile);

            } catch (final JSONException e) {
                Log.e(TAG, "BT Json parsing error: " + e.getMessage());
            }
        }
        return false;
    }
}