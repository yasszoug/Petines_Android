package com.example.rishan.rishan.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.rishan.rishan.Activites.MainActivity;
import com.example.rishan.rishan.Model.Tags;
import com.example.rishan.rishan.Activites.NavigationActivity;
import com.example.rishan.rishan.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=1000;
    String sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash2);

        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {

                SharedPreferences sharedPreferences=getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
                sp= sharedPreferences.getString("username","");


                if(sp.length()==0){

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(SplashActivity.this, NavigationActivity.class);
                    startActivity(intent);
                }

            }
        },SPLASH_TIME_OUT);




    }





}
