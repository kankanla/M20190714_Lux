package com.kankanla.e560.m20190714_lux;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    protected final String T = "### MainActivity";
    protected Lux_SurfaceView lux_surfaceView;

    protected void onCreate(Bundle savedInstanceState) {
        Log.i(T, "onCreate 1");
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_main);*/
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

    protected void show1() {
        Log.i(T, "show1");
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getWindow().getAttributes().screenOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
        lux_surfaceView = new Lux_SurfaceView(this);
        setContentView(lux_surfaceView);
    }
}
