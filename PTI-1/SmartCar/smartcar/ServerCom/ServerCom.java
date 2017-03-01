package com.uminho.pti.smartcar.ServerCom;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.uminho.pti.smartcar.Data.Route;
import com.uminho.pti.smartcar.Data.User;
import com.uminho.pti.smartcar.Local_DB.DBI;
import com.uminho.pti.smartcar.Local_DB.Schema;
import com.uminho.pti.smartcar.Local_DB.SmartCarDBHelper;
import com.uminho.pti.smartcar.Local_DB.Worker_Threads.InsertBrakeWorker;
import com.uminho.pti.smartcar.Local_DB.Worker_Threads.InsertHoleWorker;
import com.uminho.pti.smartcar.Local_DB.Worker_Threads.InsertLombaWorker;
import com.uminho.pti.smartcar.Local_DB.Worker_Threads.InsertRoutePosWorker;
import com.uminho.pti.smartcar.Local_DB.Worker_Threads.InsertRouteWorker;
import com.uminho.pti.smartcar.Local_DB.Worker_Threads.InsertSpeedWorker;
import com.uminho.pti.smartcar.Local_DB.Worker_Threads.InsertTrafficWorker;
import com.uminho.pti.smartcar.ServerCom.Worker_Threads.CheckInternetWorker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


/**
 * classe responsável por enviar os dados recolhidos para o servidor
 * método para enviar eventos
 * método para enviar locs
 * método para enviar routes
 */

public class ServerCom {

    private final SQLiteDatabase local_db;
    private SmartCarDBHelper dbHelper;
    private final RequestQueue queue;
    private Context context;
    private User user;
    private Route route;
    public CheckInternetWorker checkInternet;


    public ServerCom(SQLiteDatabase local, SmartCarDBHelper helper, Context t,User user,Route route, CheckInternetWorker checkInternet, RequestQueue queue){
        this.local_db=local;
        this.dbHelper=helper;
        this.context=t;
        this.queue = queue;
        this.user=user;
        this.route = route;
        this.checkInternet=checkInternet;
    }


    /** método que vai enviar os eventos para o servidor **/
    public void sendEventToServer(int event_type, String type,String email,long timestamp,double lat, double lng, int neigh){
        /** BRAKE EVENT ******/
        if(event_type==1) {
            final JSONObject jsonObject = new JSONObject();
            //new worker thread
            InsertBrakeWorker worker_brake = new InsertBrakeWorker(this.dbHelper, this.local_db);

            //built map from received data
            try{
                jsonObject.put("user_email", email);
                jsonObject.put("type",type);
                jsonObject.put("time_stamp", String.valueOf(timestamp));
                jsonObject.put("lat", String.valueOf(lat));
                jsonObject.put("lng", String.valueOf(lng));
                jsonObject.put("neigh", String.valueOf(neigh));
            } catch (JSONException e) {
                // handle exception (not supposed to happen)
            }

            if(checkInternet.NETWORK_AVAILABLE) {
                //VolleyRequest to server
                this.VolleyStringRequestPost(this.queue, DBI.EVENT, jsonObject);
                Log.d("UPDATE","mandou BRAKE para o server");
            }else {
                // Insert into local database
                worker_brake.setValues(email, lat, lng, timestamp, neigh);
                (new Thread(worker_brake)).start();
                Log.d("UPDATE","inseriu BRAKE na base de dados local");
            }
        }
        /*** TRANSIT EVENT ****/
        if(event_type==2){
            //new worker thread
            final JSONObject jsonObject = new JSONObject();
            InsertTrafficWorker worker_transit = new InsertTrafficWorker(this.dbHelper, this.local_db);

            //built map from received data
            try{
                jsonObject.put("user_email", email);
                jsonObject.put("type",type);
                jsonObject.put("time_stamp", String.valueOf(timestamp));
                jsonObject.put("lat", String.valueOf(lat));
                jsonObject.put("lng", String.valueOf(lng));
                jsonObject.put("neigh", String.valueOf(neigh));
            } catch (JSONException e) {
                // handle exception (not supposed to happen)
            }


            if(checkInternet.NETWORK_AVAILABLE) {
                //VolleyRequest to server
                this.VolleyStringRequestPost(this.queue, DBI.EVENT, jsonObject);
                Log.d("UPDATE","mandou TRANSIT para o server");
            }else {
                // Insert into local database
                worker_transit.setValues(email, lat, lng, timestamp, neigh);
                (new Thread(worker_transit)).start();
                Log.d("UPDATE","inseriu TRANSIT na base de dados local");
            }
        }
        /**** HOLE EVENT *****/
        if(event_type==3){
            //new worker thread
            InsertHoleWorker worker_hole = new InsertHoleWorker(this.dbHelper, this.local_db);
            final JSONObject jsonObject = new JSONObject();
            //built map from received data
            try{
                jsonObject.put("user_email", email);
                jsonObject.put("type",type);
                jsonObject.put("time_stamp", String.valueOf(timestamp));
                jsonObject.put("lat", String.valueOf(lat));
                jsonObject.put("lng", String.valueOf(lng));
                jsonObject.put("neigh", String.valueOf(neigh));
            } catch (JSONException e) {
                // handle exception (not supposed to happen)
            }

            if(checkInternet.NETWORK_AVAILABLE) {
                //VolleyRequest to server
                this.VolleyStringRequestPost(this.queue, DBI.EVENT, jsonObject);
                Log.d("UPDATE","mandou HOLE para o server");
            }else {
                // Insert into local database
                worker_hole.setValues(email, lat, lng, timestamp, neigh);
                (new Thread(worker_hole)).start();
                Log.d("UPDATE","inseriu HOLE na base de dados local");
            }
        }
        /*** SPEED EVENT ******/
        if(event_type==4){
            //new worker thread
            final JSONObject jsonObject = new JSONObject();
            InsertSpeedWorker worker_speed= new InsertSpeedWorker(this.dbHelper, this.local_db);

            //built map from received data
            try{
                jsonObject.put("user_email", email);
                jsonObject.put("type",type);
                jsonObject.put("time_stamp", String.valueOf(timestamp));
                jsonObject.put("lat", String.valueOf(lat));
                jsonObject.put("lng", String.valueOf(lng));
                jsonObject.put("neigh", String.valueOf(neigh));
            } catch (JSONException e) {
                // handle exception (not supposed to happen)
            }

            if(checkInternet.NETWORK_AVAILABLE) {
                //VolleyRequest to server
                this.VolleyStringRequestPost(this.queue, DBI.EVENT, jsonObject);
                Log.d("UPDATE","mandou SPEED para o server");
            }else {
                // Insert into local database
                worker_speed.setValues(email, lat, lng, timestamp, neigh);
                (new Thread(worker_speed)).start();
                Log.d("UPDATE","inseriu SPEED na base de dados local");
            }
        }
        /*** LOMBA EVENT ***/
        if(event_type==5){
            //new worker thread
            InsertLombaWorker worker_lomba= new InsertLombaWorker(this.dbHelper, this.local_db);
            final JSONObject jsonObject = new JSONObject();
            //built map from received data
            try{
                jsonObject.put("user_email", email);
                jsonObject.put("type",type);
                jsonObject.put("time_stamp", String.valueOf(timestamp));
                jsonObject.put("lat", String.valueOf(lat));
                jsonObject.put("lng", String.valueOf(lng));
                jsonObject.put("neigh", String.valueOf(neigh));
            } catch (JSONException e) {
                // handle exception (not supposed to happen)
            }

            if(checkInternet.NETWORK_AVAILABLE) {
                //VolleyRequest to server
                this.VolleyStringRequestPost(this.queue, DBI.EVENT,jsonObject);
                Log.d("UPDATE","mandou LOMBA para o server");
            }else {
                // Insert into local database
                worker_lomba.setValues(email, lat, lng, timestamp, neigh);
                (new Thread(worker_lomba)).start();
                Log.d("UPDATE","inseriu LOMBA na base de dados local");
            }
        }

    }

    /** método que vai enviar rotas para o servidor */
    public void endRouteToServer(float distance,float vel_med,float vel_max,String user_email,long start_stamp, long end_stamp){
        InsertRouteWorker worker_route= new InsertRouteWorker(this.dbHelper, this.local_db);
        final JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("id",String.valueOf(route.getRouteId()));
            jsonObject.put("distance",String.valueOf(-20));
            jsonObject.put("vel_med", String.valueOf(-10));
            jsonObject.put("end_stamp", String.valueOf(end_stamp));
        } catch (JSONException e) {
            // handle exception (not supposed to happen)
        }

        if(checkInternet.NETWORK_AVAILABLE) {
            //VolleyRequest to server
            this.VolleyStringRequestPut(this.queue, DBI.ROUTE, jsonObject);
            Log.d("UPDATE","mandou ROUTE para o server");
        }else {
            // Insert into local database
            worker_route.setValues(distance, vel_med, vel_max, user_email, start_stamp, end_stamp);
            (new Thread(worker_route)).start();
            Log.d("UPDATE","inseriu ROUTE na base de dados local");
        }
    }


    /** método que vai enviar várias coordenadas das rotas para o servidor ***/
    public void sendRoutePosToServer(double lat,double lng,long timestamp){
        // new worker thread
        InsertRoutePosWorker worker_route_pos = new InsertRoutePosWorker(this.dbHelper, this.local_db);
        int route_id;
        final JSONObject jsonObject = new JSONObject();

        if(checkInternet.NETWORK_AVAILABLE){

            route_id = route.getRouteId();
            try {

                jsonObject.put("route_id",String.valueOf(route_id));
                jsonObject.put("lat",String.valueOf(lat));
                jsonObject.put("lng",String.valueOf(lng));
                jsonObject.put("time_stamp",String.valueOf(timestamp));
            } catch (JSONException e) {
                // handle exception (not supposed to happen)
            }

            //VolleyRequest to server
            this.VolleyStringRequestPost(this.queue, DBI.POSITION, jsonObject);
            Log.d("UPDATE","mandou ROUTEPOS para o server");

        }else{
            //get route_id from local_db (last id in table +1)
            Cursor last = dbHelper.getLastRoute(local_db, Schema.route);
            last.moveToFirst();

            if(last.getCount()==0 || last==null){
                route_id=-1;
            }else {
                route_id = last.getInt(0) -1;
            }

            // Insert into local database
            worker_route_pos.setValues(route_id, lat, lng, timestamp);
            (new Thread(worker_route_pos)).start();
            Log.d("UPDATE","inseriu ROUTEPOS na base de dados local");
        }
    }


    private void VolleyStringRequestPost(final RequestQueue queue, final String php,
                                     final JSONObject jsonObject){
        StringRequest postRequest = new StringRequest(Request.Method.POST, php,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                try {
                    return jsonObject.toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    // not supposed to happen
                    return null;
                }
            }
        };
        queue.add(postRequest);

    }

    private void VolleyStringRequestPut(final RequestQueue queue, final String php,
                                        final JSONObject jsonObject) {
        StringRequest postRequest = new StringRequest(Request.Method.PUT, php,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                try {
                    return jsonObject.toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    // not supposed to happen
                    return null;
                }
            }
        };
        queue.add(postRequest);
    }
}
