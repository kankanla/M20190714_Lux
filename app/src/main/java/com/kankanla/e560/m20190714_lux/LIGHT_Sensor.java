package com.kankanla.e560.m20190714_lux;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.util.Calendar;
import android.os.Build;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LIGHT_Sensor implements SensorEventListener {
    private final String T = "###  LIGHT_Sensor";
    private Context context;
    private SensorManager sensorManager;
    private Sensor sensor_LIGTH;
    private boolean sensorREG = false;
    private LIGHT_SensorCallBack callBack;
    private SimpleDateFormat simpleDateFormat;

    public LIGHT_Sensor(Context context, LIGHT_SensorCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
        simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");
    }

    public boolean isSensorREG() {
        return sensorREG;
    }

    public boolean registerListener() {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        for (Sensor sensor : sensorManager.getSensorList(Sensor.TYPE_LIGHT)) {
            if (sensor.getType() == Sensor.TYPE_LIGHT) {
                sensor_LIGTH = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
                sensorREG = sensorManager.registerListener(this, sensor_LIGTH, SensorManager.SENSOR_DELAY_NORMAL);
                return sensorREG;
            }
        }
        return false;
    }

    public void unregisterListener() {
        if (sensorREG) {
            sensorManager.unregisterListener(this, sensor_LIGTH);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i(T, " onSensorChanged");
        switch (event.sensor.getType()) {
            case Sensor.TYPE_LIGHT:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    callBack.SensorVal(event.values[0], String.valueOf(simpleDateFormat.format(Calendar.getInstance().getTime())));
                    callBack.SensorVal((int) event.values[0], String.valueOf(simpleDateFormat.format(Calendar.getInstance().getTime())));
                } else {
                    callBack.SensorVal(event.values[0], String.valueOf(simpleDateFormat.format(new Date().getTime())));
                    callBack.SensorVal((int) event.values[0], String.valueOf(simpleDateFormat.format(new Date().getTime())));
                }
                break;
            case 99:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i(T, " onAccuracyChanged61");
    }

    interface LIGHT_SensorCallBack {
        void SensorVal(int val, String time);

        void SensorVal(float val, String time);
    }
}
