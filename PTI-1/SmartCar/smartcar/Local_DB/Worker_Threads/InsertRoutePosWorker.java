package com.uminho.pti.smartcar.Local_DB.Worker_Threads;


import android.database.sqlite.SQLiteDatabase;

import com.uminho.pti.smartcar.Local_DB.SmartCarDBHelper;

public class InsertRoutePosWorker implements Runnable {

    private SmartCarDBHelper dbHelper;
    private SQLiteDatabase local_DB;
    private double lat;
    private double lng;
    private long timestamp;
    private int route_id;

    public InsertRoutePosWorker(SmartCarDBHelper h, SQLiteDatabase db){
        this.dbHelper = h;
        this.local_DB = db;
        this.route_id = 0;
        this.lat = 0.0;
        this.lng = 0.0;
        this.timestamp=0;
    }

    public void setValues(int route_id, double lat, double lng,long timestamp){
        this.route_id = route_id;
        this.lat=lat;
        this.lng=lng;
        this.timestamp=timestamp;
    }

    @Override
    public void run() {
        this.dbHelper.insertRoutePosData(this.local_DB,lat,lng,timestamp,route_id);
    }
}
