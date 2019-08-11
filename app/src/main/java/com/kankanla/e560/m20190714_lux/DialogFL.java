package com.kankanla.e560.m20190714_lux;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.DialogFragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.IOException;

public class DialogFL extends DialogFragment {
    private final String T = "### DialogFL";
    private File file;

    public void setFile(File file) {
        Log.i(T, "setFile");
        this.file = file;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(T, "onCreateDialog");
        LinearLayout dialog_ll = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_fl, null);

        Bitmap bitmap = null;
        if (file == null) {
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getAssets().open("rect947.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            bitmap = BitmapFactory.decodeFile(file.getPath());
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        ImageView imageView = dialog_ll.findViewById(R.id.dialog_imageView);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageBitmap(bitmap);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AdView adView = new AdView(getActivity());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.setAdUnitId));
        AdRequest.Builder builder1 = new AdRequest.Builder();
        adView.loadAd(builder1.build());
        dialog_ll.addView(adView);
        builder.setView(dialog_ll);
        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(T, "onResume");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(600);
                    dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }).start();
    }
}
