package com.example.timebomb.ui.sound.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.timebomb.R;
import com.example.timebomb.util.SPUtils;

public class CrackScreen extends Service {
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "CrackScreenChannel";

    private ImageView mImage;
    private View overlayView;
    private int imageResId;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private boolean isSound, isVibrate;

    @Override
    public IBinder onBind(Intent paramIntent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isSound = SPUtils.getBoolean(this, SPUtils.IS_SOUND, true);
        isVibrate = SPUtils.getBoolean(this, SPUtils.IS_VIBRATE, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "Permission required to show overlay", Toast.LENGTH_SHORT).show();
                stopSelf();
                return;
            }
        }

        // Create the overlay view
        mImage = new ImageView(this);
        mImage.setScaleType(ImageView.ScaleType.FIT_XY);

        overlayView = new View(this);
        overlayView.setBackgroundColor(0x00000000);

        WindowManager.LayoutParams paramsOverlay = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        paramsOverlay.gravity = Gravity.TOP | Gravity.LEFT;

        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(overlayView, paramsOverlay);
        mediaPlayer = MediaPlayer.create(this, R.raw.crack_screen);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        overlayView.setOnClickListener(v -> showCrackEffect());

        // Start the service as a foreground service
        startForegroundService();
    }

    @SuppressLint("ForegroundServiceType")
    private void startForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Tạo notification channel
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Crack Screen Service", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.noti);

        Intent stopIntent = new Intent(this, StopReceiver.class);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationLayout.setOnClickPendingIntent(R.id.btn_stop, stopPendingIntent);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.img_logo).setStyle(new NotificationCompat.DecoratedCustomViewStyle()).setCustomContentView(notificationLayout).setPriority(NotificationCompat.PRIORITY_LOW).build();

        startForeground(NOTIFICATION_ID, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        imageResId = intent.getIntExtra("image_resource", R.drawable.img_screen_1);
        return START_STICKY;
    }

    private void showCrackEffect() {
        mImage.setImageResource(imageResId);
        WindowManager.LayoutParams paramsImage = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, PixelFormat.TRANSLUCENT);

        paramsImage.gravity = Gravity.CENTER;

        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(mImage, paramsImage);
        if (isSound) {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        }
        if (isVibrate) {
            if (vibrator != null) {
                vibrator.vibrate(500);
            }
        }

        Toast.makeText(this, "vibrate: "+isVibrate, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "sound: "+isSound, Toast.LENGTH_SHORT).show();
        overlayView.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (mImage != null) windowManager.removeView(mImage);
        if (overlayView != null) windowManager.removeView(overlayView);
        if (mediaPlayer != null) mediaPlayer.release();
    }
}