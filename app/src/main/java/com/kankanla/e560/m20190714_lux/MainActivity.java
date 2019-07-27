package com.kankanla.e560.m20190714_lux;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    protected final String T = "### MainActivity";
    protected Lux_SurfaceView lux_surfaceView;

    protected void onCreate(Bundle savedInstanceState) {
        Log.i(T, "onCreate 1");
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_main);*/
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
