package com.uminho.pti.smartcar.Local_DB;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SmartCarDBHelper extends SQLiteOpenHelper {

    //STATIC VARIABLES AND QUERIES
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String DOUBLE_TYPE = " REAL";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String PRIMARY = "PRIMARY KEY";
    private static final String AUTOINC = " INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String FOREIGN = "FOREIGN KEY";
    private static final String REFERENCES = "REFERENCES ";
    private static final String OPEN = "(";
    private static final String CLOSE = ")";
    private static final String END = "))";
    private static final String COMMA = ",";


    //CREATE TABLES
    private static final String CREATE_USER_TABLE =
            CREATE_TABLE+ Schema.USER_TABLE + OPEN +
                    Schema.UserTable.NAME + TEXT_TYPE + COMMA +
                    Schema.UserTable.USER_NAME + TEXT_TYPE + COMMA +
                    Schema.UserTable.BIRTH_DATE + TEXT_TYPE + COMMA +
                    Schema.UserTable.EMAIL + TEXT_TYPE + COMMA +
                    Schema.UserTable.CONTACT + TEXT_TYPE + COMMA +
                    Schema.UserTable.CITY + TEXT_TYPE + COMMA +
                    Schema.UserTable.ADRESS + TEXT_TYPE + COMMA +
                    Schema.UserTable.COUNTRY + TEXT_TYPE + COMMA +
                    Schema.UserTable.PASSWORD + TEXT_TYPE + COMMA +
                    Schema.UserTable.LOG_STATE + INT_TYPE + COMMA +
                    Schema.UserTable.ACCESS_LVL + INT_TYPE + COMMA +
                    Schema.UserTable.USER_TYPE + INT_TYPE + COMMA +
                    PRIMARY + OPEN + Schema.UserTable.EMAIL +END;

    private static final String CREATE_ROUTE_TABLE =
            CREATE_TABLE+ Schema.ROUTE_TABLE + OPEN +
                    Schema.RouteTable._ID + AUTOINC + COMMA +
                    Schema.RouteTable.DISTANCE + DOUBLE_TYPE + COMMA +
                    Schema.RouteTable.VEL_MAX + DOUBLE_TYPE + COMMA +
                    Schema.RouteTable.VEL_MED + DOUBLE_TYPE + COMMA +
                    Schema.RouteTable.START_STAMP+ TEXT_TYPE + COMMA +
                    Schema.RouteTable.END_STAMP + TEXT_TYPE + COMMA +
                    Schema.RouteTable.USER_EMAIL + TEXT_TYPE + COMMA +
                    FOREIGN + OPEN + Schema.RouteTable.USER_EMAIL + CLOSE +
                    REFERENCES + Schema.USER_TABLE + OPEN + Schema.UserTable.EMAIL + END;

    private static final String CREATE_ROUTE_POS_TABLE =
            CREATE_TABLE+ Schema.ROUTE_POS_TABLE + OPEN +
                    Schema.RoutePositionTable.LAT + TEXT_TYPE + COMMA +
                    Schema.RoutePositionTable.LONG +TEXT_TYPE + COMMA +
                    Schema.RoutePositionTable.ROUTE_ID + INT_TYPE + COMMA +
                    Schema.RoutePositionTable.TIME_STAMP + TEXT_TYPE + COMMA +
                    PRIMARY + OPEN + Schema.RoutePositionTable.TIME_STAMP + COMMA + Schema.RoutePositionTable.ROUTE_ID + CLOSE + COMMA +
                    FOREIGN + OPEN + Schema.RoutePositionTable.ROUTE_ID + CLOSE +
                    REFERENCES + Schema.ROUTE_TABLE + OPEN + Schema.RouteTable._ID + END;

    private static final String CREATE_BRAKE_TABLE =
            CREATE_TABLE + Schema.BRAKE_TABLE + OPEN +
                    Schema.BrakeTable._ID + AUTOINC + COMMA+
                    Schema.BrakeTable.TIME_STAMP + TEXT_TYPE + COMMA +
                    Schema.BrakeTable.LAT + TEXT_TYPE + COMMA +
                    Schema.BrakeTable.LONG + TEXT_TYPE + COMMA +
                    Schema.BrakeTable.NEIGH + INT_TYPE + COMMA +
                    Schema.BrakeTable.USER_EMAIL + TEXT_TYPE + COMMA +
                    FOREIGN + OPEN + Schema.BrakeTable.USER_EMAIL + CLOSE +
                    REFERENCES + Schema.ROUTE_TABLE + OPEN + Schema.RouteTable.USER_EMAIL + END;

    private static final String CREATE_SPEED_TABLE =
            CREATE_TABLE + Schema.SPEED_TABLE + OPEN +
                    Schema.SpeedTable._ID + AUTOINC + COMMA+
                    Schema.SpeedTable.TIME_STAMP + TEXT_TYPE + COMMA +
                    Schema.SpeedTable.LAT + TEXT_TYPE + COMMA +
                    Schema.SpeedTable.LONG + TEXT_TYPE + COMMA +
                    Schema.SpeedTable.NEIGH + INT_TYPE + COMMA +
                    Schema.SpeedTable.USER_EMAIL + TEXT_TYPE + COMMA +
                    FOREIGN + OPEN + Schema.SpeedTable.USER_EMAIL + CLOSE +
                    REFERENCES + Schema.ROUTE_TABLE + OPEN + Schema.RouteTable.USER_EMAIL + END;

    private static final String CREATE_HOLE_TABLE =
            CREATE_TABLE + Schema.HOLE_TABLE + OPEN +
                    Schema.HoleTable._ID + AUTOINC + COMMA+
                    Schema.HoleTable.TIME_STAMP + TEXT_TYPE + COMMA +
                    Schema.HoleTable.LAT + TEXT_TYPE + COMMA +
                    Schema.HoleTable.LONG + TEXT_TYPE + COMMA +
                    Schema.HoleTable.NEIGH + INT_TYPE + COMMA +
                    Schema.HoleTable.USER_EMAIL + TEXT_TYPE + COMMA +
                    FOREIGN + OPEN + Schema.HoleTable.USER_EMAIL + CLOSE +
                    REFERENCES + Schema.ROUTE_TABLE + OPEN + Schema.RouteTable.USER_EMAIL + END;

    private static final String CREATE_LOMBA_TABLE =
            CREATE_TABLE + Schema.LOMBA_TABLE + OPEN +
                    Schema.LombaTable._ID + AUTOINC + COMMA+
                    Schema.LombaTable.TIME_STAMP + TEXT_TYPE + COMMA +
                    Schema.LombaTable.LAT + TEXT_TYPE + COMMA +
                    Schema.LombaTable.LONG + TEXT_TYPE + COMMA +
                    Schema.LombaTable.NEIGH + INT_TYPE + COMMA +
                    Schema.LombaTable.USER_EMAIL + TEXT_TYPE + COMMA +
                    FOREIGN + OPEN + Schema.LombaTable.USER_EMAIL + CLOSE +
                    REFERENCES + Schema.ROUTE_TABLE + OPEN + Schema.RouteTable.USER_EMAIL + END;

    private static final String CREATE_TRAFFIC_TABLE =
            CREATE_TABLE + Schema.TRAFFIC_TABLE + OPEN +
                    Schema.TrafficTable._ID + AUTOINC + COMMA+
                    Schema.TrafficTable.TIME_STAMP + TEXT_TYPE + COMMA +
                    Schema.TrafficTable.LAT + TEXT_TYPE + COMMA +
                    Schema.TrafficTable.LONG + TEXT_TYPE + COMMA +
                    Schema.TrafficTable.NEIGH + INT_TYPE + COMMA +
                    Schema.TrafficTable.USER_EMAIL + TEXT_TYPE + COMMA +
                    FOREIGN + OPEN + Schema.TrafficTable.USER_EMAIL + CLOSE +
                    REFERENCES + Schema.ROUTE_TABLE + OPEN + Schema.RouteTable.USER_EMAIL + END;

    private static final String CREATE_ROUTE_BRAKE_TABLE =
            CREATE_TABLE + Schema.ROUTE_BRAKE + OPEN +
                    Schema.RouteBrakeTable.ROUTE_ID + INT_TYPE + COMMA +
                    Schema.RouteBrakeTable.BRAKE_ID + INT_TYPE + COMMA +
                    PRIMARY + OPEN + Schema.RouteBrakeTable.BRAKE_ID + COMMA + Schema.RouteBrakeTable.ROUTE_ID + CLOSE + COMMA +
                    FOREIGN + OPEN + Schema.RouteBrakeTable.BRAKE_ID + CLOSE +
                    REFERENCES + Schema.BRAKE_TABLE + OPEN + Schema.BrakeTable._ID + CLOSE + COMMA +
                    FOREIGN + OPEN + Schema.RouteBrakeTable.ROUTE_ID + CLOSE +
                    REFERENCES + Schema.ROUTE_TABLE + OPEN + Schema.RouteTable._ID + END;

    private static final String CREATE_ROUTE_SPEED_TABLE =
            CREATE_TABLE + Schema.ROUTE_SPEED + OPEN +
                    Schema.RouteSpeedTable.ROUTE_ID + INT_TYPE + COMMA +
                    Schema.RouteSpeedTable.SPEED_ID + INT_TYPE + COMMA +
                    PRIMARY + OPEN + Schema.RouteSpeedTable.SPEED_ID + COMMA + Schema.RouteSpeedTable.ROUTE_ID + CLOSE + COMMA +
                    FOREIGN + OPEN + Schema.RouteSpeedTable.SPEED_ID + CLOSE +
                    REFERENCES + Schema.SPEED_TABLE + OPEN + Schema.SpeedTable._ID + CLOSE + COMMA +
                    FOREIGN + OPEN + Schema.RouteSpeedTable.ROUTE_ID + CLOSE +
                    REFERENCES + Schema.ROUTE_TABLE + OPEN + Schema.RouteTable._ID + END;

    private static final String CREATE_ROUTE_HOLE_TABLE =
            CREATE_TABLE + Schema.ROUTE_HOLE + OPEN +
                    Schema.RouteHoleTable.ROUTE_ID + INT_TYPE + COMMA +
                    Schema.RouteHoleTable.HOLE_ID + INT_TYPE + COMMA +
                    PRIMARY + OPEN + Schema.RouteHoleTable.HOLE_ID + COMMA + Schema.RouteHoleTable.ROUTE_ID + CLOSE + COMMA +
                    FOREIGN + OPEN + Schema.RouteHoleTable.HOLE_ID + CLOSE +
                    REFERENCES + Schema.HOLE_TABLE + OPEN + Schema.HoleTable._ID + CLOSE + COMMA +
                    FOREIGN + OPEN + Schema.RouteHoleTable.ROUTE_ID + CLOSE +
                    REFERENCES + Schema.ROUTE_TABLE + OPEN + Schema.RouteTable._ID + END;

    private static final String CREATE_ROUTE_LOMBA_TABLE =
            CREATE_TABLE + Schema.ROUTE_LOMBA + OPEN +
                    Schema.RouteLombaTable.ROUTE_ID + INT_TYPE + COMMA +
                    Schema.RouteLombaTable.LOMBA_ID + INT_TYPE + COMMA +
                    PRIMARY + OPEN + Schema.RouteLombaTable.LOMBA_ID + COMMA + Schema.RouteLombaTable.ROUTE_ID + CLOSE + COMMA +
                    FOREIGN + OPEN + Schema.RouteLombaTable.LOMBA_ID + CLOSE +
                    REFERENCES + Schema.BRAKE_TABLE + OPEN + Schema.LombaTable._ID + CLOSE + COMMA +
                    FOREIGN + OPEN + Schema.RouteLombaTable.ROUTE_ID + CLOSE +
                    REFERENCES + Schema.ROUTE_TABLE + OPEN + Schema.RouteTable._ID + END;

    private static final String CREATE_ROUTE_TRAFFIC_TABLE =
            CREATE_TABLE + Schema.ROUTE_TRAFFIC + OPEN +
                    Schema.RouteTrafficTable.ROUTE_ID + INT_TYPE + COMMA +
                    Schema.RouteTrafficTable.TRAFFIC_ID + INT_TYPE + COMMA +
                    PRIMARY + OPEN + Schema.RouteTrafficTable.TRAFFIC_ID + COMMA + Schema.RouteTrafficTable.ROUTE_ID + CLOSE + COMMA +
                    FOREIGN + OPEN + Schema.RouteTrafficTable.TRAFFIC_ID + CLOSE +
                    REFERENCES + Schema.BRAKE_TABLE + OPEN + Schema.TrafficTable._ID + CLOSE + COMMA +
                    FOREIGN + OPEN + Schema.RouteTrafficTable.ROUTE_ID + CLOSE +
                    REFERENCES + Schema.ROUTE_TABLE + OPEN + Schema.RouteTable._ID + END;

    // DROP
    private static final String DROP_USER = DROP_TABLE +Schema.USER_TABLE;
    private static final String DROP_ROUTE_POS = DROP_TABLE +Schema.ROUTE_POS_TABLE;
    private static final String DROP_ROUTE =  DROP_TABLE +Schema.ROUTE_TABLE;
    private static final String DROP_BRAKE = DROP_TABLE +Schema.BRAKE_TABLE;
    private static final String DROP_HOLE = DROP_TABLE +Schema.HOLE_TABLE;
    private static final String DROP_TRAFFIC =  DROP_TABLE +Schema.TRAFFIC_TABLE;
    private static final String DROP_SPEED = DROP_TABLE +Schema.SPEED_TABLE;
    private static final String DROP_LOMBA = DROP_TABLE +Schema.LOMBA_TABLE;
    private static final String DROP_ROUTE_BRAKE = DROP_TABLE +Schema.ROUTE_BRAKE;
    private static final String DROP_ROUTE_SPEED = DROP_TABLE +Schema.ROUTE_SPEED;
    private static final String DROP_ROUTE_HOLE = DROP_TABLE +Schema.ROUTE_HOLE;
    private static final String DROP_ROUTE_TRAFFIC = DROP_TABLE +Schema.ROUTE_TRAFFIC;
    private static final String DROP_ROUTE_LOMBA = DROP_TABLE +Schema.ROUTE_LOMBA;

    // CONSTRUCTOR
    public SmartCarDBHelper(Context context){
        super(context, Schema.DB_NAME, null, Schema.DB_VERSION);
    }

    // ONCREATE AND ONUPGRADE METHODS
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_ROUTE_TABLE);
        db.execSQL(CREATE_ROUTE_POS_TABLE);
        db.execSQL(CREATE_BRAKE_TABLE);
        db.execSQL(CREATE_SPEED_TABLE);
        db.execSQL(CREATE_HOLE_TABLE);
        db.execSQL(CREATE_LOMBA_TABLE);
        db.execSQL(CREATE_TRAFFIC_TABLE);
        db.execSQL(CREATE_ROUTE_BRAKE_TABLE);
        db.execSQL(CREATE_ROUTE_SPEED_TABLE);
        db.execSQL(CREATE_ROUTE_HOLE_TABLE);
        db.execSQL(CREATE_ROUTE_LOMBA_TABLE);
        db.execSQL(CREATE_ROUTE_TRAFFIC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("DATABASE", oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL(DROP_USER);
        db.execSQL(DROP_ROUTE);
        db.execSQL(DROP_ROUTE_POS);
        db.execSQL(DROP_BRAKE);
        db.execSQL(DROP_HOLE);
        db.execSQL(DROP_TRAFFIC);
        db.execSQL(DROP_SPEED);
        db.execSQL(DROP_LOMBA);
        db.execSQL(DROP_ROUTE_BRAKE);
        db.execSQL(DROP_ROUTE_SPEED);
        db.execSQL(DROP_ROUTE_HOLE);
        db.execSQL(DROP_ROUTE_TRAFFIC);
        db.execSQL(DROP_ROUTE_LOMBA);
        onCreate(db);
    }

    //FAZER INSERTS
    public synchronized void insertRoutePosData(SQLiteDatabase db, double lat, double lng, long time_stamp, int route_id){
        ContentValues values = new ContentValues();
        values.put(Schema.RoutePositionTable.LAT, String.valueOf(lat));
        values.put(Schema.RoutePositionTable.LONG, String.valueOf(lng));
        values.put(Schema.RoutePositionTable.TIME_STAMP, String.valueOf(time_stamp));
        values.put(Schema.RoutePositionTable.ROUTE_ID, route_id);
        db.insert(Schema.ROUTE_POS_TABLE, null, values);
    }

    public synchronized void insertUserData(SQLiteDatabase db, String name, String birth, String country, String city, String contact,
                                            String user_name, int log_state, int access_lvl, String password, String adress,
                                            int user_type, String email){
        ContentValues values = new ContentValues();
        values.put(Schema.UserTable.EMAIL, email);
        values.put(Schema.UserTable.NAME, name);
        values.put(Schema.UserTable.BIRTH_DATE, birth);
        values.put(Schema.UserTable.COUNTRY, country);
        values.put(Schema.UserTable.CITY,city);
        values.put(Schema.UserTable.CONTACT, contact);
        values.put(Schema.UserTable.USER_NAME, user_name);
        values.put(Schema.UserTable.LOG_STATE, log_state);
        values.put(Schema.UserTable.ACCESS_LVL, access_lvl);
        values.put(Schema.UserTable.PASSWORD, password);
        values.put(Schema.UserTable.ADRESS, adress);
        values.put(Schema.UserTable.USER_TYPE, user_type);
        db.insert(Schema.USER_TABLE,null, values);
    }

    public synchronized void insertRouteData(SQLiteDatabase db, float distance, float vel_med, float vel_max,
                                             String user_email, long start_stamp, long end_stamp){
        ContentValues values = new ContentValues();
        values.put(Schema.RouteTable.DISTANCE, distance);
        values.put(Schema.RouteTable.VEL_MED, vel_med);
        values.put(Schema.RouteTable.VEL_MAX, vel_max);
        values.put(Schema.RouteTable.USER_EMAIL, user_email);
        values.put(Schema.RouteTable.START_STAMP, String.valueOf(start_stamp));
        values.put(Schema.RouteTable.END_STAMP, String.valueOf(end_stamp));
        db.insert(Schema.ROUTE_TABLE,null, values);
    }

    public synchronized void insertBrakeData(SQLiteDatabase db, String user_email, long time_stamp, double lat,
                                             double lng, int neigh){
        ContentValues values = new ContentValues();
        values.put(Schema.BrakeTable.USER_EMAIL,user_email);
        values.put(Schema.BrakeTable.TIME_STAMP,String.valueOf(time_stamp));
        values.put(Schema.BrakeTable.LAT,String.valueOf(lat));
        values.put(Schema.BrakeTable.LONG,String.valueOf(lng));
        values.put(Schema.BrakeTable.NEIGH,String.valueOf(neigh));
        db.insert(Schema.BRAKE_TABLE,null, values);
    }

    public synchronized void insertSpeedData(SQLiteDatabase db, String user_email, long time_stamp, double lat,
                                             double lng, int neigh){
        ContentValues values = new ContentValues();
        values.put(Schema.SpeedTable.USER_EMAIL,user_email);
        values.put(Schema.SpeedTable.TIME_STAMP,String.valueOf(time_stamp));
        values.put(Schema.SpeedTable.LAT,String.valueOf(lat));
        values.put(Schema.SpeedTable.LONG,String.valueOf(lng));
        values.put(Schema.SpeedTable.NEIGH,String.valueOf(neigh));
        db.insert(Schema.SPEED_TABLE,null, values);
    }

    public synchronized void insertHoleData(SQLiteDatabase db, String user_email, long time_stamp, double lat,
                                             double lng, int neigh){
        ContentValues values = new ContentValues();
        values.put(Schema.HoleTable.USER_EMAIL,user_email);
        values.put(Schema.HoleTable.TIME_STAMP,String.valueOf(time_stamp));
        values.put(Schema.HoleTable.LAT,String.valueOf(lat));
        values.put(Schema.HoleTable.LONG,String.valueOf(lng));
        values.put(Schema.HoleTable.NEIGH,String.valueOf(neigh));
        db.insert(Schema.HOLE_TABLE,null, values);
    }


    public synchronized void insertLombaData(SQLiteDatabase db, String user_email, long time_stamp, double lat,
                                             double lng, int neigh){
        ContentValues values = new ContentValues();
        values.put(Schema.LombaTable.USER_EMAIL,user_email);
        values.put(Schema.LombaTable.TIME_STAMP,String.valueOf(time_stamp));
        values.put(Schema.LombaTable.LAT,String.valueOf(lat));
        values.put(Schema.LombaTable.LONG,String.valueOf(lng));
        values.put(Schema.LombaTable.NEIGH,String.valueOf(neigh));
        db.insert(Schema.LOMBA_TABLE,null, values);
    }

    public synchronized void insertTrafficData(SQLiteDatabase db, String user_email, long time_stamp, double lat,
                                             double lng, int neigh){
        ContentValues values = new ContentValues();
        values.put(Schema.TrafficTable.USER_EMAIL,user_email);
        values.put(Schema.TrafficTable.TIME_STAMP,String.valueOf(time_stamp));
        values.put(Schema.TrafficTable.LAT,String.valueOf(lat));
        values.put(Schema.TrafficTable.LONG,String.valueOf(lng));
        values.put(Schema.TrafficTable.NEIGH,String.valueOf(neigh));
        db.insert(Schema.TRAFFIC_TABLE,null, values);
    }

    public synchronized boolean checkUserExistence(SQLiteDatabase db, String email){
        boolean exists = false;
        String query = "SELECT * FROM " +Schema.USER_TABLE +" WHERE " +Schema.UserTable.EMAIL +" = " +"\"" +email +"\"";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() > 0)
            exists = true;
        cursor.close();
        Log.i("USER CHECK"," "+exists);
        return exists;
    }

    public Cursor getTotalTable(SQLiteDatabase db, String table, String email){
        String query = "SELECT * FROM " +table +" WHERE email = " +"\"" +email +"\"";
        return db.rawQuery(query, null);
    }

    public Cursor getTotalTableData(SQLiteDatabase db, String table){
        String query = "SELECT * FROM " +table;
        return db.rawQuery(query,null);
    }

    public Cursor getLastRoute(SQLiteDatabase db, String table){
        String query = "SELECT * FROM "  +table + " WHERE _ID = (SELECT MAX(_ID))";
        return db.rawQuery(query, null);
    }

}
