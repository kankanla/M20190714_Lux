package com.kankanla.e560.m20190714_lux;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements LIGHT_Sensor.LIGHT_SensorCallBack, TextView.OnClickListener {
    private final String T = "###  Main2Activity";
    private TextView textView, textViewA, textViewB;
    private boolean booleanViewA, booleanViewB, booleanMax;
    private TextView ButtonA, ButtonB, ButtonMax, ButtonHold, iconHDA, iconHDB, menuMax, menuDate;
    private LIGHT_Sensor light_sensor;
    private static final int REQUEST_MEDIA_PROJECTION = 1001;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 2001;
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private ScreenShot screenShot;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().hide();
        light_sensor = new LIGHT_Sensor(this, this);
        light_sensor.registerListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        List<TextView> views = new ArrayList<>();
        List<TextView> views2 = new ArrayList<>();

        textView = findViewById(R.id.text);
        views.add(textView);

        textViewA = findViewById(R.id.textA);
        views.add(textViewA);
        textViewB = findViewById(R.id.textB);
        views.add(textViewB);

        ButtonA = findViewById(R.id.ButtonA);
        views.add(ButtonA);
        ButtonA.setOnClickListener(this);
        ButtonB = findViewById(R.id.ButtonB);
        views.add(ButtonB);
        ButtonB.setOnClickListener(this);
        ButtonMax = findViewById(R.id.ButtonMax);
        views.add(ButtonMax);
        ButtonMax.setOnClickListener(this);
        ButtonHold = findViewById(R.id.ButtonHold);
        views.add(ButtonHold);
        ButtonHold.setOnClickListener(this);

        iconHDA = findViewById(R.id.iconHDA);
        views.add(iconHDA);
        iconHDA.setTextColor(getResources().getColor(R.color.colorGlay2));
        iconHDB = findViewById(R.id.iconHDB);
        views.add(iconHDB);
        iconHDB.setTextColor(getResources().getColor(R.color.colorGlay2));

        menuDate = findViewById(R.id.menuDate);
        views.add(menuDate);

        menuMax = findViewById(R.id.menuMax);
        views.add(menuMax);

        booleanViewA = true;
        booleanViewB = true;
        booleanMax = false;

        for (TextView v : views) {
            v.setTypeface(Typeface.createFromAsset(getAssets(), "Nouveau_IBM.ttf"));
        }

        for (TextView v : views2) {
            v.setTypeface(Typeface.createFromAsset(getAssets(), "Nouveau_IBM.ttf"));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ButtonA:
                if (booleanViewA) {
                    booleanViewA = false;
                    iconHDA.setTextColor(getResources().getColor(R.color.colorWhite));
                } else {
                    booleanViewA = true;
                    iconHDA.setTextColor(getResources().getColor(R.color.colorGlay2));
                }
                break;
            case R.id.ButtonB:
                if (booleanViewB) {
                    booleanViewB = false;
                    iconHDB.setTextColor(getResources().getColor(R.color.colorWhite));
                } else {
                    booleanViewB = true;
                    iconHDB.setTextColor(getResources().getColor(R.color.colorGlay2));
                }
                break;
            case R.id.ButtonMax:
                if (booleanMax) {
                    booleanMax = false;
                    menuMax.setTextColor(getResources().getColor(R.color.colorGlay2));
                } else {
                    booleanMax = true;
                    menuMax.setTextColor(getResources().getColor(R.color.colorWhite));
                }
                break;
            case R.id.ButtonHold:
                StorageShow();
                if (mediaProjectionManager == null) {
                    ScreenShow();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                screenShot.getScreenshot();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                break;
        }
    }

    private void StorageShow() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                Intent intent = new Intent();
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//                intent.setData(Uri.parse("package:com.kankanla.e560.m20190714_lux"));
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 99);
                Toast.makeText(this, "ファイルの保存権限を与えてください", Toast.LENGTH_LONG).show();
            }
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
            //            //2019-07-28 02:23:45.512 1424-7165/? I/ActivityManager: START u0 {act=android.settings.APPLICATION_DETAILS_SETTINGS dat=package:com.kankanla.e560.m20190714_lux flg=0x10008000 cmp=com.android.settings/.applications.InstalledAppDetails bnds=[326,1545][986,1713]} from uid 10021
            //            //intent.setData(Uri.parse("package:" + packageName));
        } else {
            // Permission has already been granted
//            screenShot.getScreenshot();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void ScreenShow() {
        mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        if (mediaProjectionManager != null) {
            startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "ファイルの書き込み権限がありません", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Toast.makeText(this, "キャプチャファイルの保存できます", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_MEDIA_PROJECTION:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(this, "User cancelled", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mediaProjection == null) {
                    mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
                    screenShot = new ScreenShot(this, mediaProjection);
                    screenShot.get();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        light_sensor.unregisterListener();
    }

    int Max = 0;

    @Override
    public void SensorVal(int val, String time) {
        if (booleanMax) {
            if (val > Max) {
                Max = val;
                textView.setText(String.valueOf(Max));
                menuDate.setText(time);
            }
        } else {
            Max = val;
            textView.setText(String.valueOf(Max));
            menuDate.setText(time);
        }

        if (booleanViewA) {
            textViewA.setText(String.valueOf(val));
        }
        if (booleanViewB) {
            textViewB.setText(String.valueOf(val));
        }
    }

    @Override
    public void SensorVal(float val, String time) {

    }

    private void help() {
//        https://www.fonts4free.net/nouveau-ibm-font.html
//        Nouveau_IBM.ttf
//        https://www.fonts4free.net/secret-code-font.html
//        SECRCODE.TTF
    }
}
