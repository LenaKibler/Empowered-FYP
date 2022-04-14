package com.example.empowered;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {


    //time for splash screen to wait before appearing
    private final int splashDelay = 3500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#358fa3")));
        getSupportActionBar().setTitle("");

        new Handler().postDelayed(() -> {
            /* Create an Intent that will start the Menu-Activity. */
            Intent mainIntent = new Intent(SplashScreen.this,RegistrationPage.class);
            SplashScreen.this.startActivity(mainIntent);
            SplashScreen.this.finish();
        }, splashDelay);

    }
}