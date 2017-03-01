package com.uminho.pti.smartcar.Local_DB;


import android.provider.BaseColumns;

public class Schema {

    // empty constructor
    public Schema(){}

    //NOME BD
    public static final String DB_name = "smartcar_lite";

    // TABLES NAMES
    public static final String user="user";
    public static final String route_position="route_position";
    public static final String route="route";
    public static final String brake="brake";
    public static final String hole="hole";
    public static final String traffic="traffic";
    public static final String speed="speed";
    public static final String lomba="lomba";

    public static final String route_brake = "route_brake";
    public static final String route_speed = "route_speed";
    public static final String route_hole = "route_hole";
    public static final String route_lomba = "route_lomba";
    public static final String route_traffic = "route_trafic";

    // COLLUMN NAMES
    public static final String id = "id";
    public static final String user_name = "user_name";
    public static final String birth_date = "birth_date";
    public static final String email = "email";
    public static final String country = "country";
    public static final String city = "city";
    public static final String contact = "contact";
    public static final String nome = "nome";
    public static final String log_state = "log_state";
    public static final String access_level = "access_level";
    public static final String password = "password";
    public static final String address = "address";
    public static final String user_type = "user_type";
    public static final String lat = "lat";
    public static final String lng = "long";
    public static final String route_id = "route_id";
    public static final String time_stamp = "time_stamp";
    public static final String distance = "distance";
    public static final String vel_med = "vel_med";
    public static final String vel_max = "vel_max";
    public static final String start_stamp = "start_stamp";
    public static final String end_stamp = "end_stamp";
    public static final String user_email = "user_email";
    public static final String neigh = "neigh";
    public static final String brake_id = "brake_id";
    public static final String speed_id = "speed_id";
    public static final String hole_id = "hole_id";
    public static final String lomba_id = "lomba_id";
    public static final String traffic_id = "traffic_id";


    // TABLES
    public static final String DB_NAME = DB_name;
    public static final int DB_VERSION = 1;

    public static final String USER_TABLE = user;
    public static final String ROUTE_POS_TABLE = route_position ;
    public static final String ROUTE_TABLE = route;
    public static final String BRAKE_TABLE = brake;
    public static final String HOLE_TABLE = hole;
    public static final String TRAFFIC_TABLE = traffic;
    public static final String SPEED_TABLE = speed;
    public static final String LOMBA_TABLE = lomba;

    public static final String ROUTE_BRAKE = route_brake;
    public static final String ROUTE_SPEED= route_speed;
    public static final String ROUTE_HOLE = route_hole;
    public static final String ROUTE_LOMBA = route_lomba;
    public static final String ROUTE_TRAFFIC = route_traffic;





    public abstract class UserTable implements BaseColumns {
        public static final String NAME = nome;
        public static final String USER_NAME = user_name;
        public static final String BIRTH_DATE = birth_date;
        public static final String CONTACT = contact;
        public static final String CITY = city;
        public static final String COUNTRY = country;
        public static final String EMAIL =  email;
        public static final String LOG_STATE = log_state;
        public static final String ACCESS_LVL = access_level;
        public static final String PASSWORD = password;
        public static final String ADRESS = address;
        public static final String USER_TYPE = user_type;
    }

    public abstract  class RoutePositionTable implements BaseColumns{
        public static final String LAT = lat;
        public static final String LONG = lng;
        public static final String TIME_STAMP = time_stamp;
        public static final String ROUTE_ID = route_id;
    }

    public abstract  class RouteTable implements BaseColumns{
        public static final String DISTANCE = distance;
        public static final String VEL_MED = vel_med;
        public static final String VEL_MAX = vel_max;
        public static final String USER_EMAIL = user_email;
        public static final String START_STAMP = start_stamp;
        public static final String END_STAMP = end_stamp;
    }

    public abstract class BrakeTable implements BaseColumns{
        public static final String TIME_STAMP = time_stamp;
        public static final String LAT = lat;
        public static final String LONG = lng;
        public static final String USER_EMAIL = user_email;
        public static final String NEIGH = neigh;
    }

    public abstract class SpeedTable implements BaseColumns{
        public static final String TIME_STAMP = time_stamp;
        public static final String LAT = lat;
        public static final String LONG = lng;
        public static final String USER_EMAIL = user_email;
        public static final String NEIGH = neigh;
    }

    public abstract class HoleTable implements  BaseColumns{
        public static final String TIME_STAMP = time_stamp;
        public static final String LAT = lat;
        public static final String LONG = lng;
        public static final String USER_EMAIL = user_email;
        public static final String NEIGH = neigh;
    }

    public abstract class LombaTable implements BaseColumns{
        public static final String TIME_STAMP = time_stamp;
        public static final String LAT = lat;
        public static final String LONG = lng;
        public static final String USER_EMAIL = user_email;
        public static final String NEIGH = neigh;
    }

    public abstract class TrafficTable implements BaseColumns{
        public static final String TIME_STAMP = time_stamp;
        public static final String LAT = lat;
        public static final String LONG = lng;
        public static final String USER_EMAIL = user_email;
        public static final String NEIGH = neigh;
    }

    public abstract class RouteBrakeTable implements BaseColumns{
        public static final String BRAKE_ID = brake_id;
        public static final String ROUTE_ID = route_id;
    }

    public abstract class RouteSpeedTable implements BaseColumns{
        public static final String SPEED_ID = speed_id;
        public static final String ROUTE_ID = route_id;
    }
    public abstract class RouteHoleTable implements BaseColumns{
        public static final String HOLE_ID = hole_id;
        public static final String ROUTE_ID = route_id;
    }
    public abstract class RouteLombaTable implements BaseColumns{
        public static final String LOMBA_ID = lomba_id;
        public static final String ROUTE_ID = route_id;
    }
    public abstract class RouteTrafficTable implements BaseColumns{
        public static final String TRAFFIC_ID = traffic_id;
        public static final String ROUTE_ID = route_id;
    }


}
