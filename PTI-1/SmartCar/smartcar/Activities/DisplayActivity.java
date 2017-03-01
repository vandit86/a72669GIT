package com.uminho.pti.smartcar.Activities;



import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;

import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.uminho.pti.smartcar.Data.LoadData;

import com.uminho.pti.smartcar.Accelerometer.Acceler;
import com.uminho.pti.smartcar.CentralUnit.*;
import com.uminho.pti.smartcar.Adapters.PageAdapter;

import com.uminho.pti.smartcar.GPSLocation.GPSLocation;


import com.uminho.pti.smartcar.Local_DB.SmartCarDBHelper;
import com.uminho.pti.smartcar.BluetoothServerCom.BT_Protocol;
import com.uminho.pti.smartcar.BluetoothServerCom.Bluetooth;
import com.uminho.vad.smartcar.R;
import com.uminho.pti.smartcar.ServerCom.Worker_Threads.CheckInternetWorker;

import java.util.UUID;


public class DisplayActivity extends AppCompatActivity{

    private static final String TAG = "myTag";
    public static final String EXTRAS_USER_INFO = "user";
    public static final String EXTRAS_VEHICLES_INFO = "vehicles";
    public static final String EXTRAS_ROUTE_INFO = "route";
    public static final String EXTRA_ROUTE_START = "start_stamp";
    //private Activity activity;

    //private View view;

    GPSLocation gps;
    Acceler acc;
    CentralUnit centralUnit;
    private SmartCarDBHelper mdbHelper;
    private SQLiteDatabase mlocal_db;
    private RequestQueue queue;
    private LoadData load;
    private CheckInternetWorker checkInternet;
    private Context myContext = this;

// VAD var?s 
    Bluetooth bluetooth;
    BT_Protocol protocol;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        //activity = this;

        /************************************ LOADING DATA ******************************/

        final Intent intent = getIntent();
        queue= Volley.newRequestQueue(getApplicationContext());
        mdbHelper = new SmartCarDBHelper(getApplicationContext());
        mlocal_db = mdbHelper.getWritableDatabase();

        /**** Start Internet Background Thread *****/
        checkInternet = new CheckInternetWorker(getApplicationContext(), mlocal_db, mdbHelper);
        new Thread(checkInternet).start();

        load =  new LoadData(mdbHelper,mlocal_db);
        load.startLoad(intent.getStringExtra(EXTRAS_USER_INFO),intent.getStringExtra(EXTRAS_VEHICLES_INFO),intent.getStringExtra(EXTRAS_ROUTE_INFO),intent.getStringExtra(EXTRA_ROUTE_START));


        /***************************** START ALL SERVICES *****************************************/
        gps = new GPSLocation(myContext);                    // create instance of GPSLocation object
        acc = new Acceler(getApplicationContext());                         // create instance of Acceler object
    	bluetooth = new Bluetooth(this);                 // bluetooth instance
        protocol = new BT_Protocol(UUID.randomUUID().toString()); // create protocol whith random UUID
	    centralUnit = new CentralUnit(this, acc, gps, mdbHelper, mlocal_db, load.getUser(), load.getRoute(), checkInternet,queue, bluetooth, protocol);   // create central unit instance


        /****************************** CREATING VIEWPAGER **********************/

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Mapa"));
        tabLayout.addTab(tabLayout.newTab().setText("Rotas"));
        tabLayout.addTab(tabLayout.newTab().setText("Eventos"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        Bundle bundle = new Bundle();
        bundle.putString("email",load.getUser().getEmail());
        bundle.putInt("route_id",load.getRoute().getRouteId());

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PageAdapter adapter = new PageAdapter
                (getSupportFragmentManager(),bundle);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

  /**
     * Using for BT monitoring of user interaction
     */
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        if (requestCode == Bluetooth.ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {

                bluetooth.initBluetoothUI();
            }
        }
        if (requestCode == Bluetooth.DISCOVERY_REQUEST) {
            if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "Discovery cancelled by user");
            }
        }
    }


    /**************************** ON RESUME ON PAUSE METHODS ***************************/

    @Override
    protected void onResume() {
        Log.d("Response","ON RESUME");
        super.onResume();
        acc.startAcc();         // start monitoring acceleration data
    }

    @Override
    protected void onPause() {
        Log.d("Response","ON PAUSE");
        super.onPause();
        acc.stopAcc();          // stop monitoring acc data
        centralUnit.endRoute(); //terminar rota
    }

    @Override
    protected void onDestroy() {
        Log.d("Response","DESTROY");
    	bluetooth.closeConnection();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case GPSLocation.PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    gps.setPermissionAndroid6(true);
                    gps.getLocation();

                } else {

                    gps.setPermissionAndroid6(false);
                }
                break;
        }
    }
}


