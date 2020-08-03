package com.example.cattlecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.cattlecare.MainActivity;
import com.example.cattlecare.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final launcherManager launcherManager = new launcherManager(this);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (launcherManager.isFirstTime()) {
                    launcherManager.setFirstLaunch(false);
                    startActivity(new Intent(getApplicationContext(), sliderActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }

        }, 2000);







    }
}
