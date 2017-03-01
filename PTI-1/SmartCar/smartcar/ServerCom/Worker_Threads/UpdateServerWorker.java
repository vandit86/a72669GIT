package com.uminho.pti.smartcar.ServerCom.Worker_Threads;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.uminho.pti.smartcar.Local_DB.Schema;
import com.uminho.pti.smartcar.Local_DB.SmartCarDBHelper;


public class UpdateServerWorker implements Runnable{

    private Context context;
    private SQLiteDatabase local_db;
    private SmartCarDBHelper helper;

    //TODO: submit local db to server

    public UpdateServerWorker(Context context, SQLiteDatabase local_db, SmartCarDBHelper helper){
        this.context = context;
        this.local_db = local_db;
        this.helper = helper;
    }

    @Override
    public void run() {
        updateServer();
    }

    public synchronized void updateServer() {

        Cursor user_Cursor = helper.getTotalTableData(local_db,Schema.user);
        Cursor route_Cursor = helper.getTotalTableData(local_db,Schema.route);
        Cursor route_pos_Cursor = helper.getTotalTableData(local_db,Schema.route_position);
        Cursor brake_Cursor = helper.getTotalTableData(local_db,Schema.brake);
        Cursor hole_Cursor = helper.getTotalTableData(local_db,Schema.hole);
        Cursor traffic_Cursor = helper.getTotalTableData(local_db,Schema.traffic);
        Cursor speed_Cursor = helper.getTotalTableData(local_db,Schema.speed);
        Cursor lomba_Cursor = helper.getTotalTableData(local_db,Schema.lomba);

        Log.d("UPDATE","ready to UPDATE server");

        //passar conteudo dos cursores para JSONARRAY E MANDAR COM POST REQUEST





        // apagar base de dados
        //TODO: WARNING ON DELETE (TER CUIDADO)
        //helper.onUpgrade(local_db, Schema.DB_VERSION,Schema.DB_VERSION+1);
        //}
    }
}

