package com.uminho.pti.smartcar.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uminho.pti.smartcar.GPSLocation.GPSLocation;
import com.uminho.vad.smartcar.R;




public class MapFragment extends android.support.v4.app.Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    GPSLocation gps;
    private final String TAG = "myTag";
    private static MapFragment instance = null;



    public static MapFragment newInstance() {
        return new MapFragment();
    }

    public static MapFragment getInstance(){
        if(instance == null){
            instance = new MapFragment();
        }
        return instance;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_fragment,container,false);
        gps = new GPSLocation(getActivity().getApplicationContext());
        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                LatLng myPos = new LatLng(gps.getLatitude(), gps.getLongitude());

                float zoom = (float) 13.0;

                googleMap=mMap;
                Log.d(TAG, "onMapReady" + googleMap.toString());


                // if exists permission to get positions
                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onMapReady : permissions manifest error");
                    return;
                }


                mMap.setMyLocationEnabled(true);
                UiSettings ui = googleMap.getUiSettings();      // get UI for settings
                ui.setMyLocationButtonEnabled(true);            // button of my location
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, zoom));
            }
        });

        return v;
    }


    public void putMarker(final String type,final double lat,final double lon,final int img){
        (getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon))
                        .title(type)
                        .anchor(0.5F,0.5F)
                        .draggable(false)
                        .icon(BitmapDescriptorFactory.fromResource(img)));

                EraseMarker erase = new EraseMarker(marker);
                new Thread(erase).start();
            }
        });
    }

    public void eraseMarker(final Marker m) {
        (getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                m.remove();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    public class EraseMarker implements Runnable{

        Marker marker;

        public EraseMarker(Marker m){
            this.marker=m;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            eraseMarker(marker);
        }
    }
}
