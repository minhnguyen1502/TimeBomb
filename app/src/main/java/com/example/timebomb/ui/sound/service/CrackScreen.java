package com.example.timebomb.ui.sound.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.timebomb.R;

public class CrackScreen extends Service {
    private ImageView mImage;
    private View overlayView;
    private int imageResId;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    @Override
    public IBinder onBind(Intent paramIntent) {
        return null;
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate() {
        super.onCreate();

        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Permission required to show overlay", Toast.LENGTH_SHORT).show();
            stopSelf();
            return;
        }

        mImage = new ImageView(this);
        mImage.setScaleType(ImageView.ScaleType.FIT_XY);

        overlayView = new View(this);
        overlayView.setBackgroundColor(0x00000000);

        WindowManager.LayoutParams paramsOverlay = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                        ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                        : WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        paramsOverlay.gravity = Gravity.TOP | Gravity.LEFT;

        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(overlayView, paramsOverlay);
        mediaPlayer = MediaPlayer.create(this, R.raw.crack_screen);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        overlayView.setOnClickListener(v -> showCrackEffect());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        imageResId = intent.getIntExtra("image_resource", R.drawable.img_bg_screen_1);
        return START_STICKY;
    }

    private void showCrackEffect() {
        mImage.setImageResource(imageResId);
        WindowManager.LayoutParams paramsImage = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                        ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                        : WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT
        );

        paramsImage.gravity = Gravity.CENTER;

        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(mImage, paramsImage);
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }

        // Rung
        if (vibrator != null) {
            vibrator.vibrate(500); // Rung trong 500ms
        }
        overlayView.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (mImage != null) windowManager.removeView(mImage);
        if (overlayView != null) windowManager.removeView(overlayView);
    }
}
