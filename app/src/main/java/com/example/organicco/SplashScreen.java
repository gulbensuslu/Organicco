package com.example.organicco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    ProgressBar progress_bar;
    int p;
    TextView applicationName;
    ImageView logo;
    Handler h;
    Runnable r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_secreen);
        progress_bar = findViewById(R.id.progressBar_id);
        applicationName = findViewById(R.id.ApplicationName_id);
        logo=findViewById(R.id.logo_id);

        h = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, Registration.class);
                startActivity(intent);
                finish();
            }
        };
        progressProcess();
    }

    public void  progressProcess() {
        p = progress_bar.getProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (p < 70) {
                    p = p + 1;
                    h.post((new Runnable() {
                        @Override
                        public void run() {
                            progress_bar.setProgress(p);
                            if (p == 23) {
                                logo.setVisibility(View.VISIBLE);
                            }
                            if (p == 43) {
                                applicationName.setVisibility(View.VISIBLE);
                             }
                        }
                    }));
                    try {
                        Thread.sleep(50);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                h.postDelayed(r, 50);
            }
        }).start();
    }
}