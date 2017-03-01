package com.uminho.pti.smartcar.Local_DB.Worker_Threads;


import android.database.sqlite.SQLiteDatabase;

import com.uminho.pti.smartcar.Local_DB.SmartCarDBHelper;

public class InsertRouteWorker implements Runnable {

    private SmartCarDBHelper dbHelper;
    private SQLiteDatabase local_DB;
    private float distance;
    private float vel_med;
    private float vel_max;
    private String user_email;
    private long start_stamp;
    private long end_stamp;


    public InsertRouteWorker(SmartCarDBHelper dbHelper, SQLiteDatabase local_DB){
        this.dbHelper=dbHelper;
        this.local_DB=local_DB;
        this.distance=0;
        this.vel_med=0;
        this.vel_max=0;
        this.user_email=null;
        this.start_stamp=0;
        this.end_stamp=0;
    }

    public void setValues( float distance, float vel_med, float vel_max, String user_email, long start_stamp,long end_stamp){
        this.distance=distance;
        this.vel_med=vel_med;
        this.vel_max=vel_max;
        this.user_email=user_email;
        this.start_stamp=start_stamp;
        this.end_stamp=end_stamp;

    }

    @Override
    public void run() {
        this.dbHelper.insertRouteData(local_DB,distance,vel_med,vel_max,user_email,start_stamp,end_stamp);
    }
}
