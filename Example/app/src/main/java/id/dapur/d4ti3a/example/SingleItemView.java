package id.dapur.d4ti3a.example;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class SingleItemView extends AppCompatActivity {
    // Declare Variables
    String name;
    String locationname;
    String latitude;
    String longitude;
    String date;
    String flag;
    ImageLoader imageLoader = new ImageLoader(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.singleitemview);

        Intent i = getIntent();
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar1);
        toolbar1.setTitle("ON THE WAY");
        // Get the result of rank
        name = i.getStringExtra("name");
        // Get the result of country
        locationname = i.getStringExtra("locationname");
        // Get the result of population
        latitude = i.getStringExtra("latitude");
        // Get the result of population
        longitude = i.getStringExtra("longitude");
        date = i.getStringExtra("date");
        // Get the result of flag

        // Locate the TextViews in singleitemview.xml
        TextView txtname = (TextView) findViewById(R.id.name);
        TextView txtlocationname = (TextView) findViewById(R.id.locationname);
        TextView txtlatitude = (TextView) findViewById(R.id.latitude);
        TextView txtlongitude = (TextView) findViewById(R.id.longitude);
        TextView txtdate = (TextView) findViewById(R.id.date);


        // Locate the ImageView in singleitemview.xml
        ImageView imgflag = (ImageView) findViewById(R.id.flag);

        // Set results to the TextViews
        txtname.setText(name);
        txtlocationname.setText(locationname);
        txtlatitude.setText(latitude);
        txtlongitude.setText(longitude);
        txtdate.setText(date);

        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class
        imageLoader.DisplayImage(flag, imgflag);



    }

    public void getlokasi (View v) {

        String latitude = ((TextView) findViewById(R.id.latitude)).getText().toString();
        String longitude = ((TextView) findViewById(R.id.longitude)).getText().toString();

        boolean installedMaps = false;

        // CHECK IF GOOGLE MAPS IS INSTALLED
        PackageManager pkManager = getPackageManager();
        try {
            @SuppressWarnings("unused")
            PackageInfo pkInfo = pkManager.getPackageInfo("com.google.android.apps.maps", 0);
            installedMaps = true;
        } catch (Exception e) {
            e.printStackTrace();
            installedMaps = false;
        }

        // SHOW THE MAP USING CO-ORDINATES FROM THE CHECKIN
        if (installedMaps == true) {
            String geoCode = "geo:0,0?q=" + latitude + ","
                    + longitude;
            Intent sendLocationToMap = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(geoCode));
            startActivity(sendLocationToMap);
        } else if (installedMaps == false) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    SingleItemView.this);

            // SET THE ICON
            alertDialogBuilder.setIcon(R.mipmap.ic_launcher);

            // SET THE TITLE
            alertDialogBuilder.setTitle("Google Maps Not Found");

            // SET THE MESSAGE
            alertDialogBuilder
                    .setMessage("No Map Installed")
                    .setCancelable(false)
                    .setNeutralButton("Got It",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.dismiss();
                                }
                            });

            // CREATE THE ALERT DIALOG
            AlertDialog alertDialog = alertDialogBuilder.create();

            // SHOW THE ALERT DIALOG
            alertDialog.show();
        }
    }
}