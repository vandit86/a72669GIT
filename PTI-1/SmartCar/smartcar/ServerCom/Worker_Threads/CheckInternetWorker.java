package com.uminho.pti.smartcar.ServerCom.Worker_Threads;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.uminho.pti.smartcar.Local_DB.SmartCarDBHelper;

public class CheckInternetWorker implements Runnable {

    final private static int NETWORK_CHECK_INTERVAL = 5000; // Sleep interval between checking network
    public boolean NETWORK_AVAILABLE = false;
    private Context context;
    private SQLiteDatabase localdb;
    private SmartCarDBHelper helper;

    public CheckInternetWorker(Context context, SQLiteDatabase localdb, SmartCarDBHelper helper){
        this.context=context;
        this.localdb = localdb;
        this.helper = helper;
    }

    public synchronized boolean isNetworkAvailable(){
        ConnectivityManager cn=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf=cn.getActiveNetworkInfo();
        if(nf != null && nf.isConnected()){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void run() {
        while(true) {
            if (isNetworkAvailable()) {
                //Internet status changed from off to on
                if (!NETWORK_AVAILABLE) {
                    Log.d("UPDATE", "INTERNET status changed");
                    //if status changed from off to on update remote db
                    UpdateServerWorker updateServer = new UpdateServerWorker(context, localdb, helper);
                    new Thread(updateServer).start();
                }
                NETWORK_AVAILABLE = true;
                Log.d("UPDATE", "there is INTERNET");
            } else {
                NETWORK_AVAILABLE = false;
                Log.d("UPDATE", "there is no INTERNET");
            }

            try {
                // Sleep after finish checking network
                Thread.sleep(NETWORK_CHECK_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


