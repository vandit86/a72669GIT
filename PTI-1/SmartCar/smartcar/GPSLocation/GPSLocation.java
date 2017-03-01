package com.uminho.pti.smartcar.GPSLocation;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;


public class GPSLocation {

    private static final String TAG = "myTag";
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000 * 30; // in Milliseconds 10min
    private static final float MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 0;      // in Meters
    public static final int PERMISSION_REQUEST_CODE = 1;

    private List<LocEventListner> listenersList = new ArrayList<>();
    private LocationManager locationManager;
    private MyLocationListener locationListener;
    private Context my_context;
    private Location location;
    private double latitude;
    private double longitude;

    public void setPermissionAndroid6(boolean permissionAndroid6) {
        isPermissionAndroid6 = permissionAndroid6;
    }

    private boolean isPermissionAndroid6 = false;
    // flag for GPS Status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS Tracking is enabled
    boolean isGPSTrackingEnabled = false;

    private String provider_info;

    public GPSLocation(Context x) {
        my_context = x;
        getLocation();
    }


    public void getLocation() {

        try {

                if (!isPermissionAndroid6) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Log.d (TAG, "SDK>M...");
                        ActivityCompat.requestPermissions((Activity)my_context,
                                new String[]{
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                },
                                PERMISSION_REQUEST_CODE);
                    }
                }

            if (ContextCompat.checkSelfPermission(my_context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(my_context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "PERMISSION GRANTED");
                locationManager = (LocationManager) my_context.getSystemService(LOCATION_SERVICE);

                //getting GPS status
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                //getting network status
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                // Try to get location if you GPS Service is enabled
                if (isGPSEnabled) {
                    this.isGPSTrackingEnabled = true;

                    Log.d(TAG, "Application use GPS Service");

                /*
                 * This provider determines location using
                 * satellites. Depending on conditions, this provider may take a while to return
                 * a location fix.
                 */

                    provider_info = LocationManager.GPS_PROVIDER;

                } else if (isNetworkEnabled) { // Try to get location if you Network Service is enabled
                    this.isGPSTrackingEnabled = true;

                    Log.d(TAG, "Application use Network State to get GPS coordinates");

                /*
                 * This provider determines location based on
                 * availability of cell tower and WiFi access points. Results are retrieved
                 * by means of a network lookup.
                 */
                    provider_info = LocationManager.NETWORK_PROVIDER;

                }

                if (provider_info == null) return;

                // Application can use GPS or Network Provider
                if (!provider_info.isEmpty()) {
                    if (ActivityCompat.checkSelfPermission(my_context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(my_context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "NO PERMISSIONS");
                        return;
                    }
                    locationListener = new MyLocationListener();
                    locationManager.requestLocationUpdates(provider_info, MINIMUM_TIME_BETWEEN_UPDATES, MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, locationListener);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(provider_info);
                        updateGPSCoordinates();
                    } else {
                        Log.d(TAG, "location manager is null");
                    }
                }

            } else {
                Log.d(TAG, "PERMISSION DENIED");

            }

        } catch (Exception e) {
            //e.printStackTrace();
            Log.d(TAG, "Impossible to connect to LocationManager", e);
        }
    }

    public void updateGPSCoordinates() {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        return latitude;
    }


    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    /**************************
     * LOCATION LISTENER CLASS
     *********************************/
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            //Log.d(TAG,"ON location changed");
            setLocation(loc);

        }

        @Override
        public void onProviderDisabled(String provider) {
            Context context = my_context;
            CharSequence text = "Localização desativada.\nPor favor ligue a localização no menu de notificações";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Context context = my_context;
            CharSequence text = "Sinal para localização adquirido.";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    public void addListener(LocEventListner listener) {
        listenersList.add(listener);
    }

    // Location
    private void setLocation(Location loc) {
        location = loc;
        //Log.d(TAG,"lat "+loc.getLatitude()+"long "+loc.getLongitude());

        for (LocEventListner l : listenersList) {
            l.location(loc);

        }
    }


}
