package com.kankanla.e560.m20190714_lux;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements LIGHT_Sensor.LIGHT_SensorCallBack, TextView.OnClickListener {
    private final String T = "###  Main2Activity";
    private TextView textView, textViewA, textViewB, menuDate;
    private TextView ButtonA, ButtonB, ButtonMax, ButtonCapture;
    private TextView iconHDA, iconHDB, menuMax;
    private boolean booleanViewA, booleanViewB, booleanMax, booleanCAP;
    private static final int REQUEST_MEDIA_PROJECTION = 1001;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 2001;
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private LIGHT_Sensor light_sensor;
    private ScreenShot screenShot;
    private SharedPreferences sharedPreferences;
    private RelativeLayout relativeLayout;
    private AdView adView;
    private AdRequest.Builder builder;
    private long showTime = 1000 * 60 * 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(T, "onCreate");
        MobileAds.initialize(this, getString(R.string.initialize));
        setContentView(R.layout.activity_main2);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        sharedPreferences = getSharedPreferences("atime", Context.MODE_PRIVATE);
        if (sharedPreferences.getLong("atime", 0) == 0) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("atime", System.currentTimeMillis() + showTime);
            editor.apply();
        }
        setAdmob();

        light_sensor = new LIGHT_Sensor(this, this);
        light_sensor.registerListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(T, "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(T, "onStart");
        List<TextView> views = new ArrayList<>();
        List<TextView> views2 = new ArrayList<>();

        textView = findViewById(R.id.text);
        views.add(textView);
        textViewA = findViewById(R.id.textA);
        views.add(textViewA);
        textViewB = findViewById(R.id.textB);
        views.add(textViewB);

        ButtonA = findViewById(R.id.ButtonA);
        ButtonA.setOnClickListener(this);
        views.add(ButtonA);

        ButtonB = findViewById(R.id.ButtonB);
        ButtonB.setOnClickListener(this);
        views.add(ButtonB);

        ButtonMax = findViewById(R.id.ButtonMax);
        ButtonMax.setOnClickListener(this);
        views.add(ButtonMax);

        ButtonCapture = findViewById(R.id.ButtonCapture);
        ButtonCapture.setOnClickListener(this);
        views.add(ButtonCapture);

        iconHDA = findViewById(R.id.iconHDA);
        iconHDA.setTextColor(getResources().getColor(R.color.colorRED));
        views.add(iconHDA);

        iconHDB = findViewById(R.id.iconHDB);
        iconHDB.setTextColor(getResources().getColor(R.color.colorRED));
        views.add(iconHDB);

        menuDate = findViewById(R.id.menuDate);
        views.add(menuDate);

        menuMax = findViewById(R.id.menuMax);
        views.add(menuMax);

        booleanViewA = true;
        booleanViewB = true;
        booleanMax = false;
        booleanCAP = false;

        for (TextView v : views) {
            v.setTypeface(Typeface.createFromAsset(getAssets(), "Nouveau_IBM.ttf"));
        }

        for (TextView v : views2) {
            v.setTypeface(Typeface.createFromAsset(getAssets(), "Nouveau_IBM.ttf"));
        }
    }

    @Override
    public void onClick(View v) {
        Log.i(T, "onClick");
        switch (v.getId()) {
            case R.id.ButtonA:
                if (booleanViewA) {
                    booleanViewA = false;
                    iconHDA.setTextColor(getResources().getColor(R.color.colorWhite));
                    ButtonA.setTextColor(getResources().getColor(R.color.colorRED));
                } else {
                    booleanViewA = true;
                    iconHDA.setTextColor(getResources().getColor(R.color.colorRED));
                    ButtonA.setTextColor(getResources().getColor(R.color.colorWhite));
                }
                break;
            case R.id.ButtonB:
                if (booleanViewB) {
                    booleanViewB = false;
                    iconHDB.setTextColor(getResources().getColor(R.color.colorWhite));
                    ButtonB.setTextColor(getResources().getColor(R.color.colorRED));
                } else {
                    booleanViewB = true;
                    iconHDB.setTextColor(getResources().getColor(R.color.colorRED));
                    ButtonB.setTextColor(getResources().getColor(R.color.colorWhite));
                }
                break;
            case R.id.ButtonMax:
                if (booleanMax) {
                    booleanMax = false;
                    menuMax.setTextColor(getResources().getColor(R.color.colorGlay2));
                    ButtonMax.setTextColor(getResources().getColor(R.color.colorWhite));
                } else {
                    booleanMax = true;
                    menuMax.setTextColor(getResources().getColor(R.color.colorWhite));
                    ButtonMax.setTextColor(getResources().getColor(R.color.colorGlay2));
                }
                break;
            case R.id.ButtonCapture:
                if (!booleanCAP) {
                    try {
                        storageCheckPermission();
                        if (mediaProjectionManager == null) {
                            ScreenShow();
                        } else {
                            screenShot.getScreenshot();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    /**
     * intent.setData(Uri.parse("package:com.kankanla.e560.m20190714_lux"));
     */
    private void storageCheckPermission() {
        Log.i(T, "storageCheckPermission");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);

            }
            //ActivityManager: START u0 {act=android.settings.APPLICATION_DETAILS_SETTINGS dat=package:com.kankanla.e560.m20190714_lux flg=0x10008000 cmp=com.android.settings/.applications.InstalledAppDetails bnds=[326,1545][986,1713]} from uid 10021
            //intent.setData(Uri.parse("package:" + packageName));
        } else {
            // Permission has already been granted
//            screenShot.getScreenshot();
            Log.i(T, "Permission has already been granted");
        }
    }

    private void ScreenShow() {
        Log.i(T, "ScreenShow");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
            if (mediaProjectionManager != null) {
                startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
            }
        }
    }

    /**
     * WRITE_EXTERNAL_STORAGE 権限を要求
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(T, "onRequestPermissionsResult");
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                Log.i(T, "onRequestPermissionsResult >>> REQUEST_WRITE_EXTERNAL_STORAGE");
                if (grantResults.length > 0) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent();
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, REQUEST_WRITE_EXTERNAL_STORAGE);
                        Toast.makeText(this, getString(R.string.nowritePermission), Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Toast.makeText(this, getString(R.string.getwritePermission), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case 999:
                break;
        }
    }

    /**
     * 画面キャプチャの権限の取得
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(T, "onActivityResult");
        switch (requestCode) {
            case REQUEST_MEDIA_PROJECTION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (resultCode != RESULT_OK) {
                        Toast.makeText(this, "User cancelled", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (mediaProjection == null) {
                        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
                        screenShot = new ScreenShot(this, mediaProjection, new ScreenShot.ScreenCallback() {
                            @Override
                            public void work1() {
                                ButtonCapture.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        booleanCAP = true;
                                        ButtonCapture.setBackgroundColor(getResources().getColor(R.color.colorGlay));
                                        ButtonCapture.setShadowLayer(0, 0, 0, R.color.colorGlay);
                                        Log.i(T, "work1");
                                    }
                                });
                            }

                            @Override
                            public void work2(final File file) {
                                ButtonCapture.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        booleanCAP = false;
                                        ButtonCapture.setBackgroundColor(getResources().getColor(R.color.colorYellow));
                                        ButtonCapture.setShadowLayer(3, 3, 3, R.color.colorGlay3);
                                        DialogFL dialogFL = new DialogFL();
                                        dialogFL.setFile(file);
                                        dialogFL.show(getSupportFragmentManager(), "dialog");
                                        Log.i(T, "work2");
                                    }
                                });
                            }
                        });
                        screenShot.get();
                    }
                }
                break;
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                Log.i(T, "onActivityResult >>> REQUEST_WRITE_EXTERNAL_STORAGE");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(T, "onDestroy");
        light_sensor.unregisterListener();
    }

    int Max = 0;

    @Override
    public void SensorVal(int val, String time) {
        Log.i(T, "SensorVal");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
        if (val < 5) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        if (adView != null) {
            if (System.currentTimeMillis() - sharedPreferences.getLong("atime", 0) > showTime) {
                adView.loadAd(builder.build());
                adView.bringToFront();
            } else {
                relativeLayout.removeView(adView);
            }
        }
    }

    @Override
    public void SensorVal(float val, String time) {

    }

    private void setAdmob() {
        Log.i(T, "setAdmob");
        relativeLayout = findViewById(R.id.ac2admob);
        adView = findViewById(R.id.adview);
        builder = new AdRequest.Builder();
//        builder.addTestDevice("C9517774AEB25C5D4B40D8175F152E03");
//        builder.addTestDevice("78957C13BCC9AA1AC5D8462F2DEC083A");
        adView.loadAd(builder.build());
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.i(T, "onAdClosed");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.i(T, "onAdFailedToLoad");
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Log.i(T, "onAdLeftApplication");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.i(T, "onAdOpened");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("atime", System.currentTimeMillis());
                editor.apply();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.i(T, "onAdLoaded");
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Log.i(T, "onAdClicked");
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                Log.i(T, "onAdImpression");
            }
        });
    }
}
