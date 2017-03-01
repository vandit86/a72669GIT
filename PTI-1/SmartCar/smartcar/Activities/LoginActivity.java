package com.uminho.pti.smartcar.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.uminho.pti.smartcar.Data.Route;
import com.uminho.pti.smartcar.Local_DB.DBI;
import com.uminho.pti.smartcar.Local_DB.Schema;
import com.uminho.pti.smartcar.Local_DB.SmartCarDBHelper;
import com.uminho.vad.smartcar.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    private final static String VALID = "granted";
    private static final int PERMISSION_REQUEST_CODE = 1;
    ProgressDialog dialog;
    private SmartCarDBHelper mdbHelper;
    private SQLiteDatabase mlocal_db;
    String user_resposta;
    String veiculos_resposta;
    String route_resposta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mdbHelper = new SmartCarDBHelper(getApplicationContext());
        mlocal_db = mdbHelper.getWritableDatabase();


        final EditText l_email = (EditText) findViewById(R.id.login_email);
        final EditText l_pass = (EditText) findViewById(R.id.login_pass);

        final RequestQueue queue = Volley.newRequestQueue(this);

        Button login = (Button) findViewById(R.id.btn_login);
        if (login == null) {
            throw new AssertionError();
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                //final String email = l_email.getText().toString();
                //final String pass = l_pass.getText().toString();
                final String email = "jsantos@mail.com";
                final String pass = "20304050";
                // TODO: encriptação PDKF(AES256) na password
                String message = "";
                if (email == null || !email.contains("@")) {
                    message += "No email detected.\n";
                    flag = false;
                }
                if (pass == null || (pass.length() < 6 || pass.length() > 15)) {
                    message += "Password is not valid.";
                    flag = false;
                }
                if (!flag) {
                    AlertDialog.Builder badinput_builder = new AlertDialog.Builder(LoginActivity.this);
                    badinput_builder.setTitle(R.string.wrong_input)
                            .setMessage(message);
                    badinput_builder.setCancelable(true);
                    AlertDialog denied = badinput_builder.create();
                    denied.show();
                } else {
                    if (isNetworkAvailable()) {
                        /** vai buscar o user **/
                        StringRequest postRequest = new StringRequest(Request.Method.GET, DBI.USER.concat("/"+email),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String user_response) {
                                        Log.d("Response", user_response);
                                        user_resposta=user_response;
                                        try {
                                            JSONArray res = new JSONArray(user_response);
                                            JSONObject resposta = res.getJSONObject(0);
                                            getUserVeiculos(resposta.optString("email"),queue);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        /**getUser(email,queue,dialog);

                                        if (response.equals(VALID)) {
                                            getUser(email, queue, dialog);
                                        } else {
                                            dialog.cancel();
                                            dialog.hide();
                                            AlertDialog.Builder denied_builder = new AlertDialog.Builder(LoginActivity.this);
                                            denied_builder.setTitle(R.string.access_denied)
                                                    .setMessage(response);
                                            denied_builder.setCancelable(true);
                                            AlertDialog denied = denied_builder.create();
                                            denied.show();
                                        }**/
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // error
                                        dialog.cancel();
                                        dialog.hide();
                                        AlertDialog.Builder denied_builder = new AlertDialog.Builder(LoginActivity.this);
                                        denied_builder.setTitle("ERROR!")
                                                .setMessage(error.toString());
                                        denied_builder.setCancelable(true);
                                        AlertDialog denied = denied_builder.create();
                                        denied.show();
                                        Log.d("Error.Response", error.toString());
                                    }
                                }
                        ){
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("email", email);
                                params.put("password", pass);
                                return params;
                            }
                        };
                        queue.add(postRequest);
                        dialog = new ProgressDialog(LoginActivity.this);
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.setMessage("Checking Login. Please wait...");
                        dialog.setIndeterminate(true);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        Handler cancel_dialog = new Handler();
                        cancel_dialog.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog.isShowing()) {
                                    dialog.cancel();
                                    dialog.hide();
                                    AlertDialog.Builder denied_builder = new AlertDialog.Builder(LoginActivity.this);
                                    denied_builder.setTitle("ERROR!")
                                            .setMessage("Connection Time Expired!");
                                    denied_builder.setCancelable(true);
                                    AlertDialog denied = denied_builder.create();
                                    denied.show();
                                }
                            }
                        }, 10000);
                    } else {
                        if (mdbHelper.checkUserExistence(mlocal_db, email)) {
                            // Buscar dados do utilizador à BD local e começar activity
                            Cursor user= mdbHelper.getTotalTable(mlocal_db, Schema.user,email);
                            user.moveToFirst();
                            if(user != null && user.getCount()>0)
                            {
                                if(user.getString(user.getColumnIndex(Schema.UserTable.PASSWORD)).equals(pass)) {
                                    JSONObject item = new JSONObject();
                                    try {
                                        user.moveToFirst();
                                        item.put(Schema.UserTable.NAME, user.getString(user.getColumnIndex(Schema.UserTable.NAME)));
                                        item.put(Schema.UserTable.USER_NAME, user.getString(user.getColumnIndex(Schema.UserTable.USER_NAME)));
                                        item.put(Schema.UserTable.BIRTH_DATE, user.getString(user.getColumnIndex(Schema.UserTable.BIRTH_DATE)));
                                        item.put(Schema.UserTable.EMAIL, user.getString(user.getColumnIndex(Schema.UserTable.EMAIL)));
                                        item.put(Schema.UserTable.CONTACT, user.getString(user.getColumnIndex(Schema.UserTable.CONTACT)));
                                        item.put(Schema.UserTable.CITY, user.getString(user.getColumnIndex(Schema.UserTable.CITY)));
                                        item.put(Schema.UserTable.ADRESS, user.getString(user.getColumnIndex(Schema.UserTable.ADRESS)));
                                        item.put(Schema.UserTable.COUNTRY, user.getString(user.getColumnIndex(Schema.UserTable.COUNTRY)));
                                        item.put(Schema.UserTable.PASSWORD, user.getString(user.getColumnIndex(Schema.UserTable.PASSWORD)));
                                        item.put(Schema.UserTable.LOG_STATE, user.getString(user.getColumnIndex(Schema.UserTable.LOG_STATE)));
                                        item.put(Schema.UserTable.ACCESS_LVL, user.getString(user.getColumnIndex(Schema.UserTable.ACCESS_LVL)));
                                        item.put(Schema.UserTable.USER_TYPE, user.getString(user.getColumnIndex(Schema.UserTable.USER_TYPE)));


                                        String local_user = item.toString();

                                        final Intent i = new Intent(LoginActivity.this, DisplayActivity.class);
                                        i.putExtra(DisplayActivity.EXTRAS_USER_INFO, local_user);
                                        startActivity(i);
                                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                    } catch (JSONException e) {
                                        Log.d("Exception", e.getMessage());
                                    }
                                } else{
                                    AlertDialog.Builder wrong_pass_builder = new AlertDialog.Builder(LoginActivity.this);
                                    wrong_pass_builder.setTitle("ERROR!")
                                            .setMessage("Wrong password!");
                                    wrong_pass_builder.setCancelable(true);
                                    AlertDialog denied = wrong_pass_builder.create();
                                    denied.show();
                                }
                            }

                        } else {
                            AlertDialog.Builder no_user_builder = new AlertDialog.Builder(LoginActivity.this);
                            no_user_builder.setTitle("ERROR!")
                                    .setMessage("Non-existence user, please turn on wifi!");
                            no_user_builder.setCancelable(true);
                            AlertDialog denied = no_user_builder.create();
                            denied.show();
                        }
                    }
                }
            }
        });
    }
    /** método que vai buscar os veiculos **/
    private void getUserVeiculos(final String email, final RequestQueue queue) {

        StringRequest postRequest = new StringRequest(Request.Method.GET, DBI.VEICULOS.concat("/"+email),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String veiculos_response) {
                        Log.d("Response", veiculos_response);
                        veiculos_resposta=veiculos_response;
                        try {
                            ArrayList<String> matriculas = new ArrayList<String>();
                            JSONArray jsonArray = new JSONArray(veiculos_response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject curr = jsonArray.getJSONObject(i);
                                matriculas.add(curr.optString("plate"));
                            }
                            //TODO: mostrar veiculos
                            //showVeiculosDialog(matriculas);
                            beginRoute(email, System.currentTimeMillis(), matriculas.get(0),queue);
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
    }

    /** método que inicia uma rota **/
    private void beginRoute(final String email, final long start_stamp, final String matricula, final RequestQueue queue){
        Log.d("Response","Começou rota");

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_email",email);
            jsonObject.put("start_stamp",String.valueOf(start_stamp));
            jsonObject.put("vehicle_plate",matricula);
        } catch (JSONException e) {
            // handle exception (not supposed to happen)
        }

        StringRequest postRequest = new StringRequest(Request.Method.POST, DBI.ROUTE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String route_response) {
                        Log.d("Response", route_response);
                        route_resposta=route_response;
                        final Intent i = new Intent(LoginActivity.this, DisplayActivity.class);
                        i.putExtra(DisplayActivity.EXTRAS_USER_INFO, user_resposta);
                        i.putExtra(DisplayActivity.EXTRAS_VEHICLES_INFO, veiculos_resposta);
                        i.putExtra(DisplayActivity.EXTRAS_ROUTE_INFO, route_resposta);
                        i.putExtra(DisplayActivity.EXTRA_ROUTE_START,String.valueOf(start_stamp));
                        dialog.hide();
                        dialog.cancel();
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }
                        Log.d("Error.Response", error.toString());
                    }
                }
        ){
            @Override
            public byte[] getBody() {
                try {
                    return jsonObject.toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    // not supposed to happen
                    return null;
                }
            }
        };

        queue.add(postRequest);
    }

    /**
    private void getUser(final String email, final RequestQueue queue, final ProgressDialog dialog) {

        StringRequest postRequest = new StringRequest(Request.Method.GET, DBI.USER.concat("/"+email),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        final Intent i = new Intent(LoginActivity.this, DisplayActivity.class);
                        i.putExtra(DisplayActivity.EXTRAS_USER_INFO, response);
                        dialog.hide();
                        dialog.cancel();
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
    } **/


    private void showVeiculosDialog(ArrayList<String> mats){

        final CharSequence items[] = new CharSequence[mats.size()];

        for(int i=0; i<mats.size(); i++){
            items[i]=mats.get(i);
        }

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Escolha o seu veículo");
        builder.setSingleChoiceItems(items,-1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Toast.makeText(getApplicationContext(), items[item].toString(), Toast.LENGTH_SHORT)
                                .show();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        alert.dismiss();
    }

    public boolean isNetworkAvailable(){
        ConnectivityManager cn=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf=cn.getActiveNetworkInfo();
        if(nf != null && nf.isConnected()){
            return true;
        }else{
            return false;
        }
    }

}

