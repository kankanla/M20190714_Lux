package com.kankanla.e560.m20190714_lux;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    protected final String T = "### MainActivity";
    protected Lux_SurfaceView lux_surfaceView;

    protected void onCreate(Bundle savedInstanceState) {
        Log.i(T, "onCreate 1");
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        MobileAds.initialize(this, getString(R.string.initialize));
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            Intent intent = new Intent(this, Main2Activity.class);
            startActivityForResult(intent, 99);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(T, "onActivityResult");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(T, "onStart 1");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(T, "onResume 1");
    }

    protected void show1() {
        Log.i(T, "show1");
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        lux_surfaceView = new Lux_SurfaceView(this);
        setContentView(lux_surfaceView);
    }
}
