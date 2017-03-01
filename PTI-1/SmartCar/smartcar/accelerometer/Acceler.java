package com.uminho.pti.smartcar.Accelerometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by VAD on 11/10/2016.
 * version 1.1
 */

public class Acceler {

    private final  String TAG = "myTag";
    private final int PACK_SIZE = 50;               // package size in use
    private final int MILLISECOND_TO_WAIT = 10;     // 100 Hz frequency of data reading
    private final int FIRST_PACKS = 10;              // num of first packs (2 sec) to get basis
    private final float TIME = 0.5f;                // 0.5 sec
    private final int MAX_SPEED = 35 ;              // 120 km / h maximum speed
    private SensorManager sensorManager;    //

    private long lastEventTime;
    public final static byte EVENT_Emergency_Stop = 1 ;          // Events that can bee
    public final static byte EVENT_Transit = 2 ;                 //
    public final static byte EVENT_Hole = 3 ;                    //
    public final static byte EVENT_Max_Speed = 4;                // vel maxima atengida

    private Context context;
    private List <AccEventListner> listnersList = new ArrayList<>();
    private Sensor sensorAccel;             // object accelerometer

    public Acceler(Context c){
        this.context = c;

        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    private float[] valuesAccel = new float[3];     // acceleration values from sensor
    private float[] pack_x = new float[PACK_SIZE];
    private float[] pack_y = new float[PACK_SIZE];
    private float[] pack_z = new float[PACK_SIZE];

    private float BASIS = 0.0f;                      // central acceleration
    private int pck_counter = 0 ;                    // packege counter
    private float velocity;                                  // instant velocity of motion
    private Timer timer;                                     // timer show acceleration data
    private final float GRAVITY = 9.8f;                      // gravity
    private boolean baisiSet = false ;               // if basis has bean seated
    private boolean eventFlag = false ;              // flag of some event probably happen
    private int PACK_NUM = 0;                        // num of packege to pck_counter usage



    /**********************INIT MOTORIZING ACCELERATION MOTION   **********************************/
    public void startAcc() {

        sensorManager.registerListener(listener, sensorAccel,
                SensorManager.SENSOR_DELAY_NORMAL);


        timer = new Timer();
        TimerTask task = new TimerTask() {
            int i = 0;
            @Override
            public void run() {
                        // every MILLISECOND_TO_WAIT read data from sensor
                        // then filter with low pass butterword filter ***OR NOT***
                        // make packege
                        //Log.d (TAG, valuesAccel[0]+" "+valuesAccel[1]+" "+valuesAccel[2]);

                        if ((++i) >= PACK_SIZE ){       // send pack´s
                            i = 0;
                            dataMovAnalise (pack_y);    // send mov data to analise
                        }

                        pack_x[i] = valuesAccel[0];
                        pack_y[i] = valuesAccel[1];
                        pack_z[i] = valuesAccel[2];
            }
        };
        timer.schedule(task, 0, MILLISECOND_TO_WAIT);
    }

    public void stopAcc() {
        sensorManager.unregisterListener(listener);
        timer.cancel();
    }

    /*
   * Sensor Event Listner ********************************************************************************
   * */
    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //  Log.d(TAG, "onAccuracyChanged");
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // Log.d(TAG, "onSensorChanged");

            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    for (int i = 0; i < 3; i++) {
                        valuesAccel[i] = event.values[i];

                        // IMPLEMENT FILTER HEAR IF NECESSARY
//                        valuesAccelGravity[i] = (float) (0.1 * event.values[i] + 0.9 * valuesAccelGravity[i]);
//                        valuesAccelMotion[i] = event.values[i]
//                                - valuesAccelGravity[i];
                    }
                    break;
            }
        }
    };


    // *********************************ANALISE MOVEMENT DIRECTION*****************************************************************
    private void dataMovAnalise (float data []){

        if (data == null) return;
        float max = maxValue(data);
        float min = minValue(data);
        float acc = (max+min)/2;          // acc acceleration on pack
        float ptp = max - min ;             // point-to-point variable
        float instant_velocity;


        // basis calculation
        // just use this part of code fore first packs
        // need for error reading calculation
        if (!baisiSet){
            pck_counter++;
            if (pck_counter <= FIRST_PACKS){        // basis calculation
                BASIS += acc;
                return;
            }
            BASIS = BASIS/FIRST_PACKS;
            baisiSet = true;                        // set basis set flag ON
            Log.d(TAG, "Accelerometer : BASIS SET "+BASIS);
            pck_counter = 0;
        }

        acc -= BASIS;                         // correction acc
        if (Math.abs(acc)< 0.1 ) acc =0;

        instant_velocity = acc*TIME;                    // velocity on pack

        if (instant_velocity != 0)
            velocity = velocity +instant_velocity;          // add velocity
        else
            velocity -= 0.7;                                // car lost velocity without acceleration

        if (velocity < 0.1)  // control of valid
        {
            velocity = 0;
        }

        //Log.d (TAG, "VEL = "+ velocity);
        //Log.d (TAG, "ACC = "+ acc);

        if (velocity > MAX_SPEED) {
            long agora = System.currentTimeMillis();
            if (agora  > (lastEventTime+5000) ) {
                setEvent(EVENT_Max_Speed);         // send event maximum speed passed
                lastEventTime = agora;
            }
        }

        // emergency STOP (probably)
        if (acc < 0 && velocity > 0){
            if (!eventFlag){
                PACK_NUM = (int)velocity / 2 +1;        // limit num of pack to consider that stop has emergency character
                eventFlag = true;
            }
            pck_counter++;

        }else if (velocity == 0 && eventFlag){          // car stop with event monitoring
            if (pck_counter < PACK_NUM && PACK_NUM > 0) {
                setEvent(EVENT_Emergency_Stop);         // send event of emergency stop
                eventFlag = false;

            }else {
                setEvent(EVENT_Transit);                // send event of transito
                eventFlag = false;
            }
            pck_counter =0;
            PACK_NUM =0;

        }else {                                         //  other case (acc > 0 && vel > 0  )
//            if (eventFlag && PACK_NUM > 0){
//                if (pck_counter < PACK_NUM && PACK_NUM > 0) {
//
//                }else {
//
//                }
//                pck_counter =0;
//            }
            eventFlag = false ;
            PACK_NUM =0;
        }

    }


    /************   ANALISE Z DIRECTION ****************************************************/

//    private void dataZAnalise (float data []) {
//
//    }


    /***************  AXILLARY FUNCTIONS  **************************************************/

    /* RETURN MIN VALUE IN ARRAY*/
    private float minValue (float data[] ){
        float min = data[0];
        for (int i = 1; i < data.length; i++) {
            if (data[i] > min) {
                min = data[i];
            }
        }
        return min;
    }

    /*RETURN MAX VALUE IN ARRAY*/
    private float maxValue (float data[] ){
        float max = data[0];
        for (int i = 1; i < data.length; i++) {
            if (data[i] > max) {
                max = data[i];
            }
        }
        return max;
    }

    /******************LISTNER FUNCTIONS**************************/

    public void addListner (AccEventListner listner){
        listnersList.add(listner);
    }

    // integer for event classification
    private void setEvent (int event){
        for (AccEventListner l : listnersList){
            l.event(event);
        }
    }

//    // Show list of ALL SENSOR IN ANDROID DEVICE
//    public void onClickSensList(View v) {
//        Log.d(TAG, "OnClick");
//        // sensorManager.unregisterListener(listenerLight, sensorLight);
//        StringBuilder sb = new StringBuilder();
//
//        for (Sensor sensor : sensors) {
//            sb.append("name = ").append(sensor.getName())
//                    .append(", type = ").append(sensor.getType())
//                    .append("\nvendor = ").append(sensor.getVendor())
//                    .append(" ,version = ").append(sensor.getVersion())
//                    .append("\nmax = ").append(sensor.getMaximumRange())
//                    .append(", resolution = ").append(sensor.getResolution())
//                    .append("\n--------------------------------------\n");
//        }
//        tvText.setText(sb);
//        //return sb;
//    }
}


