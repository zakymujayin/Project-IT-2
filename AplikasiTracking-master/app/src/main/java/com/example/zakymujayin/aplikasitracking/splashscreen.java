package com.example.zakymujayin.aplikasitracking;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;


public class splashscreen extends AppCompatActivity {

    ProgressBar progressBar;
    int progressStatus = 0;
    TextView  status;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        status = (TextView) findViewById(R.id.status);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100)
                {
                    progressStatus += 1;
                    handler.post(new Runnable()
                    {
                        public void run()
                        {
                            progressBar.setProgress(progressStatus);
                            status.setText(progressStatus + "%");
                        }
                    });
                    try
                    {
                        Thread.sleep(26);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                if (progressStatus==100)
                {
                    Intent i = new Intent(splashscreen.this, Login.class);
                    startActivity(i);
                }
            }
        }).start();
    }}