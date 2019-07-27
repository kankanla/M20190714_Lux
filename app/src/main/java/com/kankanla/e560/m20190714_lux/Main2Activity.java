package com.kankanla.e560.m20190714_lux;


import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements LIGHT_Sensor.LIGHT_SensorCallBack, TextView.OnClickListener {
    private final String T = "###  Main2Activity";
    private TextView textView, textViewA, textViewB;
    private boolean booleanViewA, booleanViewB, booleanMax;
    private TextView ButtonA, ButtonB, ButtonMax, ButtonHold, iconHDA, iconHDB, menuMax, menuDate;
    private LIGHT_Sensor light_sensor;


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
