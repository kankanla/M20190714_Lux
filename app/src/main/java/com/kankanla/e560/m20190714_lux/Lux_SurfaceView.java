package com.kankanla.e560.m20190714_lux;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Lux_SurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable, LIGHT_Sensor.LIGHT_SensorCallBack {
    private final String T = "### Lux_SurfaceView";
    private Context context;
    private boolean booleanT = false;
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private int scrX, scrY;
    private LIGHT_Sensor light_sensor;
    private int senValI = 0;
    private float senValF = 0;
    private Bitmap bitmapBK;

    public int getSenValI() {
        return senValI;
    }

    public float getSenValF() {
        return senValF;
    }

    public Lux_SurfaceView(Context context) {
        super(context);
        this.context = context;
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        bitmapBK = BitmapFactory.decodeResource(getResources(), R.mipmap.rect8152);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        booleanT = true;
        scrX = getWidth();
        scrY = getHeight();
        light_sensor = new LIGHT_Sensor(context, this);
        light_sensor.registerListener();
        Thread threadRun = new Thread(this);
        threadRun.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (light_sensor.isSensorREG()) {
            light_sensor.unregisterListener();
        }
        booleanT = false;
    }

    private void Mylog() {


    }

    private void BackBitmap(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
//        paint.setAlpha(190);
        Matrix matrix = new Matrix();
        RectF B = new RectF();
        matrix.mapRect(B);
        float xc = (float) scrX / (float) bitmapBK.getWidth();
        float yc = (float) scrY / (float) bitmapBK.getHeight();
        matrix.setScale(xc, yc);
        canvas.drawBitmap(bitmapBK, matrix, paint);
    }

    private void MyDraw() {
        canvas = surfaceHolder.lockCanvas();
        if (canvas != null) {
            try {
                canvas.drawColor(Color.DKGRAY);
                BackBitmap(canvas);
                Paint paint = new Paint();
                paint.setTextSize(90);
                canvas.drawText(getSenValI() + "   VAL", 200, 200, paint);
            } catch (Exception e) {
            } finally {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public void run() {
        int sleepTime = 50;
        while (booleanT) {
            long s = System.currentTimeMillis();
            MyDraw();
            long e = System.currentTimeMillis();
            Log.i(T, "run Error61");
            if (e - s < sleepTime) {
                try {
                    Thread.sleep(sleepTime - (e - s));
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void SensorVal(int val ,String time) {
        senValI = val;
    }

    @Override
    public void SensorVal(float val ,String time) {
        senValF = val;
    }

}
