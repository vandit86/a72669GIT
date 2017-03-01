package com.uminho.pti.smartcar.Data;



import android.database.sqlite.SQLiteDatabase;

import android.util.Log;

import com.uminho.pti.smartcar.Local_DB.SmartCarDBHelper;

import org.json.JSONArray;

import org.json.JSONObject;



public class LoadData {

    private User user;
    private Route route;
    private Veiculo veiculo;

    private SmartCarDBHelper mdbHelper;
    private SQLiteDatabase mlocal_db;


    public LoadData(SmartCarDBHelper mdbHelper, SQLiteDatabase mlocal_db) {
        this.mdbHelper = mdbHelper;
        this.mlocal_db = mlocal_db;
    }

    public void startLoad(String user_resposta, String veiculos_resposta, String route_resposta, String start_stamp) {


        try {
            //TODO: esconder dados do user
            JSONArray res_user = new JSONArray(user_resposta);
            JSONObject response = res_user.getJSONObject(0);
            user = new User(response.optString("email"), response.optString("name"), response.optString("birth_date"),
                    response.optString("country"), response.optString("city"), response.optString("contact"),
                    response.optString("user_name"), response.optInt("log_state"), response.optInt("acess_lvl"),
                    response.optString("password"), response.optString("adress"), response.optInt("user_type"));

            // check user existence
            if (!mdbHelper.checkUserExistence(mlocal_db, response.optString("email"))) {
                mdbHelper.insertUserData(mlocal_db, response.optString("name"), response.optString("birth_date"),
                        response.optString("country"), response.optString("city"), response.optString("contact"),
                        response.optString("user_name"), response.optInt("log_state"), response.optInt("acess_lvl"),
                        response.optString("password"), response.optString("adress"), response.optInt("user_type"),
                        response.optString("email"));
            }

            JSONArray res_vei = new JSONArray(veiculos_resposta);
            JSONObject response2 = res_vei.getJSONObject(0);
            veiculo = new Veiculo(response2.optString("plate"),response2.optString("brand"),response2.optString("model"),
                    response2.optLong("kms"),response2.optDouble("l_100km"));


            JSONArray res_rota = new JSONArray(route_resposta);
            JSONObject response3 = res_rota.getJSONObject(0);
            route = new Route(user.getEmail());
            route.setStart_stamp(Long.valueOf(start_stamp));
            Log.d("Response","ID:"+(String.valueOf(response3.optInt("id"))));
            route.setId(response3.optInt("id"));

        } catch (Throwable t) {
            Log.d("Response",t.getMessage());
            Log.i("ERROR", "JSON");
        }
    }

    public User getUser(){
        return user;
    }

    public Route getRoute(){
        return route;
    }

    public Veiculo getVeiculo(){return veiculo;}

}
