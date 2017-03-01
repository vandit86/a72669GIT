package com.uminho.pti.smartcar.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.uminho.pti.smartcar.Data.Route;
import com.uminho.pti.smartcar.Local_DB.DBI;
import com.uminho.vad.smartcar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/********************** FRAGMENT CLASS QUE VAI MOSTRAR OS EVENTOS AO USER *******************************/
public class EventsFragment extends android.support.v4.app.Fragment {

    RequestQueue queue;

    public static EventsFragment newInstance() {
        return new EventsFragment();
    }

    public EventsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.events_fragment,container,false);
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        Bundle extras =getArguments();
        int route_id = extras.getInt("route_id");
        getRouteEvents(route_id,queue);

        return v;
    }

    /**** método que pede as rotas do utilizador ***/
    private void getRouteEvents(final int route_id, final RequestQueue queue) {
        Log.d("UPDATE","Getting route events from server");
        StringRequest postRequest = new StringRequest(Request.Method.GET, DBI.EVENT.concat("/route/"+route_id),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject curr = jsonArray.getJSONObject(i);
                               //MapFragment.getInstance().putMarkerEvents(curr.optString("type"),curr.optDouble("lat"),curr.optDouble("lng)"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("route_id",String.valueOf(route_id));
                return params;
            }
        };
        queue.add(postRequest);
    }

}
