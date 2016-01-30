package com.example.zakymujayin.aplikasitracking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class MainActivity extends AppCompatActivity implements LocationListener {

    Context context = this;
    LocationManager locationManager;
    String provider;
    Location location;
    public TextView nama;
    public TextView Lat;
    public TextView Long;
    public TextView Simpan;
    public TextView flag;
    public TextView date;
    public LinearLayout layerBawah;
    String lattxt, longtxt, locnametxt, nametxt, flagtxt, datetxt;
    List<NameValuePair> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

        ////Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Geo Mobile");

        ////Cek Ponsel
        cekDrive();

        ////Deklarasi Parameter
        nama = (TextView) findViewById(R.id.name);

        //Extra Intent dari Login
        Intent intentname = getIntent();
        String name = (String) intentname.getSerializableExtra("name");
        nama.setText(name);

        ///inisialisasi
        Lat = (TextView) findViewById(R.id.lat_view);
        Long = (TextView) findViewById(R.id.long_view);
        Simpan = (TextView) findViewById(R.id.nama_lokasi);
        flag = (TextView) findViewById(R.id.flag);

        ///Tanggal
        date = (TextView) findViewById(R.id.date);
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("d/M/yyyy h:m:s a");
        String formattedDate = df.format(c.getTime());
        date.setText(formattedDate);

        //Deklarasi Layer Bawah
        layerBawah = (LinearLayout) findViewById(R.id.layerbawah);
        hideLayerBawah();


        //Cek GPS
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage("GPS Tidak Aktif, Aktifkan ?");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int which) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int which) {
                    // TODO Auto-generated method stub

                    finish();
                    System.exit(0);

                }
            });
            dialog.show();
        }



        ///Get Lokasi
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (provider != null && !provider.equals("")) {
            location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 20000, 1, this);
            if (location != null)
                onLocationChanged(location);
        }


    }


    ////Menu Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    ///Menu Bar Dipilih
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case R.id.activity_logout:
            SharedPreferences preferences =getSharedPreferences("AppPref",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            finish();
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
                Toast.makeText(getBaseContext(), "Berhasil Logout", Toast.LENGTH_SHORT).show();
            return true;

            case R.id.action_settings:
            return true;

            case R.id.action_save:
                String simpan = Simpan.getText().toString();
                writeToFile(simpan);
             return true;


            default:
            return super.onOptionsItemSelected(item);
        }
    }



    public void hideLayerBawah() {
        layerBawah.setVisibility(View.VISIBLE);
    }

    public void showLayerBawah() {
        layerBawah.setVisibility(View.VISIBLE);
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Long.setText(String.valueOf(location.getLongitude()));
        Lat.setText(String.valueOf(location.getLatitude()));
    }

    public void getLokasi(View v) {
        Long.setText(String.valueOf(location.getLongitude()));
        Lat.setText(String.valueOf(location.getLatitude()));
        showLayerBawah();
    }

    public void getMap(View v) {

        Intent i = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(i);
    }


    ////Action Tombol Back
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton("No", null).show();
    }


    ///Checking HP

    public void cekDrive() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/Proyek2");
        boolean success;
        if (!folder.exists()) {
            success = folder.mkdir();
            if (success) {
                Toast.makeText(getApplicationContext(), "Phone is Ready for storing data", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Phone is not Ready for storing data", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Phone is Ready!", Toast.LENGTH_LONG).show();
        }
    }


    //Menyimpan File
    public void writeToFile(String data) {
        String filex = data + "\nLatitude: " + Lat.getText() + "\nLongitude: " + Long.getText();
        File root = new File(Environment.getExternalStorageDirectory(), "Proyek2");
        String namaFile = data + ".txt";
        try {
            File gpxfile = new File(root, namaFile);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(filex);
            writer.flush();
            writer.close();
            Toast.makeText(getApplicationContext(), "Data " + data + " sudah Tersimpan", Toast.LENGTH_LONG).show();
            clear();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void clear() {
        Lat.setText("0.0");
        Long.setText("0.0");
        Simpan.setText("");
        hideLayerBawah();
    }

    public void sendLokasi(View v) {
        lattxt = Lat.getText().toString();
        longtxt = Long.getText().toString();
        locnametxt = Simpan.getText().toString();
        nametxt = nama.getText().toString();
        flagtxt = flag.getText().toString();
        datetxt = date.getText().toString();

        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("latitude", lattxt));
        params.add(new BasicNameValuePair("longitude", longtxt));
        params.add(new BasicNameValuePair("locationname", locnametxt));
        params.add(new BasicNameValuePair("name", nametxt));
        params.add(new BasicNameValuePair("flag", flagtxt));
        params.add(new BasicNameValuePair("date", datetxt));
        ServerRequest sr = new ServerRequest();
        JSONObject json = sr.getJSON("http://10.0.0.3:8080/delivery/api/send", params);
        //JSONObject json = sr.getJSON("http://192.168.56.1:8080/register",params);

        if (json != null) {
            try {
                String jsonstr = json.getString("response");

                Toast.makeText(getApplicationContext(), jsonstr, Toast.LENGTH_LONG).show();
                Log.d("Data Terkirim", jsonstr);
                bersih();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateLokasi(View v) {
        lattxt = Lat.getText().toString();
        longtxt = Long.getText().toString();
        locnametxt = Simpan.getText().toString();
        nametxt = nama.getText().toString();
        flagtxt = flag.getText().toString();
        datetxt = date.getText().toString();

        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("latitude", lattxt));
        params.add(new BasicNameValuePair("longitude", longtxt));
        params.add(new BasicNameValuePair("locationname", locnametxt));
        params.add(new BasicNameValuePair("name", nametxt));
        params.add(new BasicNameValuePair("flag", flagtxt));
        params.add(new BasicNameValuePair("date", datetxt));
        ServerRequest sr = new ServerRequest();
        JSONObject json = sr.getJSON("http://10.0.0.3:8080/delivery/api/update", params);
        //JSONObject json = sr.getJSON("http://192.168.56.1:8080/register",params);

        if (json != null) {
            try {
                String jsonstr = json.getString("respon");

                Toast.makeText(getApplicationContext(), jsonstr, Toast.LENGTH_LONG).show();
                Log.d("Lokasi Terupdate", jsonstr);
                bersih();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void bersih() {

        Lat.setText("0.0");
        Long.setText("0.0");
        Simpan.setText("");
        hideLayerBawah();
    }


}






