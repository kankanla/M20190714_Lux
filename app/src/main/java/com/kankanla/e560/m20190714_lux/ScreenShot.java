package com.kankanla.e560.m20190714_lux;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ScreenShot extends Main2Activity {
    private final String T = "###  ScreenShot";
    private Context context;
    private MediaProjection mediaProjection;
    private ImageReader imageReader;
    private DisplayMetrics dm;
    private ScreenCallback callback;
    private Handler handler;
    private HandlerThread handlerThread;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ScreenShot(Context context, MediaProjection mediaProjection, ScreenCallback callback) {
        this.context = context;
        this.mediaProjection = mediaProjection;
        this.callback = callback;
        WorkThread();
    }

    public ScreenShot(Context context, MediaProjection mediaProjection) {
        this.context = context;
        this.mediaProjection = mediaProjection;
    }

    private void WorkThread() {
        handlerThread = new HandlerThread("WorkThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper(), new Handler.Callback() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean handleMessage(Message msg) {
                getScreenshot2();
                Log.i(T, Thread.currentThread().getName() + " handlerThread hhh");
                return false;
            }
        });
    }

    protected void getScreenshot() {
        handler.sendEmptyMessage(22);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void get() {
        dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        imageReader = ImageReader.newInstance(dm.widthPixels, dm.heightPixels, PixelFormat.RGBA_8888, 2);
        VirtualDisplay virtualDisplay =
                mediaProjection.createVirtualDisplay("name", dm.widthPixels, dm.heightPixels, dm.densityDpi,
                        DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, imageReader.getSurface(), null, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void getScreenshot2() {
        // ImageReaderから画面を取り出す
        callback.work1();
        Log.d("debug", "getScreenshot");
        Log.d(T, Thread.currentThread().getName() + "    hhh");
        Log.i(T, imageReader.getHeight() + "   jjjj   " + imageReader.getWidth());
        Image image = imageReader.acquireLatestImage();
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();

        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * dm.widthPixels;

        // バッファからBitmapを生成
        Bitmap bitmap = Bitmap.createBitmap(dm.widthPixels + rowPadding / pixelStride, dm.heightPixels, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        image.close();

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        long l = System.currentTimeMillis();
        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/LUX/");
        if (!path.isDirectory()) {
            path.mkdir();
        }
        File file = new File(path, "IMG_" + l + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes.toByteArray());
            fileOutputStream.flush();
            fileOutputStream.close();
            Thread.sleep(100);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            callback.work2();
        }
    }

    interface ScreenCallback {
        void work1();

        void work2();
    }
}
