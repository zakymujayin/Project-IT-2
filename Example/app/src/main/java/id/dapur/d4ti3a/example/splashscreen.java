package id.dapur.d4ti3a.example;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

public class splashscreen extends Activity {
    static ConnectivityManager cm;
    AlertDialog dailog;
    AlertDialog.Builder build;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);



        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);// checking
        // internet
        build = new Builder(splashscreen.this); // connectivity

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
            Thread mythread = new Thread() {
                public void run() {
                    try {

                        sleep(5000);

                    } catch (Exception e) {
                    } finally {
                        Intent intent = new Intent(splashscreen.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            };
            mythread.start();
        } else {

            build.setMessage("Aplikasi ini membutuhkan paket data! aktifkan paket data ?");
            build.setPositiveButton("Yes", new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                    startActivity(new Intent(Settings.ACTION_SETTINGS));


                    finish();

                }
            });
            build.setNegativeButton("No", new OnClickListener() {

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
        }

    }

