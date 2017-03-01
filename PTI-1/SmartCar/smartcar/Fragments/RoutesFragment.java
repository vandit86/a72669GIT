package com.uminho.pti.smartcar.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

/**
 * Fragment that list all routes done by the user
 *
 */

public class RoutesFragment extends android.support.v4.app.ListFragment {

    ListView list;
    Context mContext;
    RequestQueue queue;
    public static RoutesFragment newInstance() {
        return new RoutesFragment();
    }

    public RoutesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.routes_fragment,container,false);
        list = (ListView) v.findViewById(android.R.id.list);
        mContext = getActivity().getApplicationContext();
        queue = Volley.newRequestQueue(mContext);
        Bundle extras =getArguments();
        String email = extras.getString("email");
        ArrayList<Route> route_list = getRoutes(email,queue);
        ArrayList<String> distance = new ArrayList<String>();
        ArrayList<String> vel_med = new ArrayList<String>();
        ArrayList<String> ids = new ArrayList<String>();
        ids.add("1");
        ids.add("2");
        ids.add("3");


        ArrayAdapter<String> adapter_id =new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, ids);
        setListAdapter(adapter_id);
        /**
        if(route_list.isEmpty()){

        }else{
            for(int i=0;i<route_list.size();i++){
                ids.add(String.valueOf(route_list.get(i).getRouteId()));
                distance.add(String.valueOf(route_list.get(i).getDistance()));
                vel_med.add(String.valueOf(route_list.get(i).getVel_med()));
            }

            ArrayAdapter<String> adapter =new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, ids);
            setListAdapter(adapter);

        }**/

        return v;
    }


    /**** método que pede as rotas do utilizador ***/
    private ArrayList<Route> getRoutes(final String email, final RequestQueue queue) {
        Log.d("UPDATE","Getting user routes from server");
        final ArrayList<Route> rotas = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.GET, DBI.ROUTE.concat("/"+email),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject curr = jsonArray.getJSONObject(i);
                                Route x =new Route(curr.optInt("id"),0,0,0,curr.optString("user_email"),
                                        Long.parseLong(curr.optString("start_stamp")),Long.parseLong(curr.optString("end_stamp")));

                                rotas.add(x);
                            }
                            Log.d("Response",String.valueOf(rotas.size()));
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
                params.put("email", email);
                return params;
            }
        };
        queue.add(postRequest);
        return rotas;
    }
}
