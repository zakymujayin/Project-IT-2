package com.example.zakymujayin.aplikasitracking;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Login extends Activity {

    //Deklarasi
    Context context = this;
    EditText name, password;
    Button login;
    String nametxt, passwordtxt;
    List<NameValuePair> params;
    SharedPreferences pref;
    ServerRequest sr;
    static ConnectivityManager cm;
    AlertDialog dailog;
    AlertDialog.Builder build;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);// checking
        // internet
        build = new AlertDialog.Builder(Login.this); // connectivity

        if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)// if connection is
                // there screen goes
                // to next screen
                // else shows
                // message
                .isConnectedOrConnecting()
                || cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting()) {
            Log.e("cm value",
                    ""
                            + cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                            .isConnectedOrConnecting());
        } else {

            build.setMessage("Aplikasi ini membutuhkan paket data! aktifkan paket data ?");
            build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                    startActivity(new Intent(Settings.ACTION_SETTINGS));


                    finish();

                }
            });
            build.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    finish();
                    System.exit(0);
                }
            });

        }

        dailog = build.create();
        dailog.show();

    sr = new ServerRequest();
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.btlogin);
        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        login.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


                //Method POST Login
                nametxt = name.getText().toString();
                passwordtxt = password.getText().toString();
                params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", nametxt));
                params.add(new BasicNameValuePair("password", passwordtxt));
                ServerRequest sr = new ServerRequest();
                JSONObject json = sr.getJSON("http://10.0.0.3:8080/delivery/api/login", params);


                if (json != null) {
                    try {



                        String jsonstr = json.getString("response");
                        if (json.getBoolean("res")) {
                            SharedPreferences.Editor edit = pref.edit();
                            //Storing Data using SharedPreferences
                            edit.commit();

                            Intent i = new Intent(Login.this, MainActivity.class);
                            i.putExtra("name", nametxt);
                            startActivity(i);

                            finish();

                        }

                        Toast.makeText(getApplication(), jsonstr, Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();


    }
}


