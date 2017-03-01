package com.uminho.pti.smartcar.Local_DB.Worker_Threads;


import android.database.sqlite.SQLiteDatabase;

import com.uminho.pti.smartcar.Local_DB.SmartCarDBHelper;

public class InsertLombaWorker implements Runnable {
    private SmartCarDBHelper dbHelper;
    private SQLiteDatabase local_DB;
    private double lat;
    private double lng;
    private String user_email;
    private long time_stamp;
    private int neigh;


    public InsertLombaWorker(SmartCarDBHelper dbHelper, SQLiteDatabase local_DB){
        this.dbHelper=dbHelper;
        this.local_DB=local_DB;
        this.lat=0.0;
        this.lng=0.0;
        this.time_stamp=0;
        this.neigh=0;
    }

    public void setValues(String user_email,double lat,double lng,long time_stamp,int neigh){
        this.user_email=user_email;
        this.lat=lat;
        this.lng=lng;
        this.time_stamp=time_stamp;
        this.neigh=neigh;
    }

    @Override
    public void run() {
        this.dbHelper.insertLombaData(local_DB,user_email,time_stamp,lat,lng,neigh);
    }
}
