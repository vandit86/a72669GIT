package com.uminho.pti.smartcar.Local_DB.Worker_Threads;


import android.database.sqlite.SQLiteDatabase;

import com.uminho.pti.smartcar.Local_DB.SmartCarDBHelper;

public class InsertUserWorker implements Runnable {

    private SmartCarDBHelper dbHelper;
    private SQLiteDatabase local_DB;
    private String email;
    private String name;
    private String birth_date;
    private String country;
    private String city;
    private String contact;
    private String user_name;
    private int log_state;
    private int access_level;
    private String password;
    private String address;
    private int user_type;

    public InsertUserWorker(SmartCarDBHelper dbHelper, SQLiteDatabase local_DB){
        this.dbHelper=dbHelper;
        this.local_DB=local_DB;
        this.email=null;
        this.name=null;
        this.birth_date=null;
        this.country=null;
        this.city=null;
        this.contact = null;
        this.user_name=null;
        this.log_state=0;
        this.access_level=0;
        this.password=null;
        this.address=null;
        this.user_type=0;
    }

    public void setValues(String name, String birth, String country, String city, String contact, String user_name,
                            int log_state,int access_level, String password,String address, int user_type, String email ){
        this.name=name;
        this.email=email;
        this.birth_date=birth;
        this.country=country;
        this.city=city;
        this.contact=contact;
        this.user_name=user_name;
        this.log_state=log_state;
        this.access_level=access_level;
        this.password=password;
        this.address=address;
        this.user_type=user_type;
    }

    @Override
    public void run() {
        this.dbHelper.insertUserData(this.local_DB,name,birth_date,country,city,contact,user_name,log_state,access_level,
                                    password,address,user_type,email);
    }
}
