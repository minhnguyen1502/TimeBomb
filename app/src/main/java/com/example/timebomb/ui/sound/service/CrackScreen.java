package com.example.timebomb.ui.sound.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.LayoutInflater;
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
    WindowManager windowManager2;

    private ImageView mImage;
    private int imageResId;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private boolean isSound, isVibrate;

    WindowManager.LayoutParams params;
    View view;

    @Override
    public IBinder onBind(Intent paramIntent) {
        return null;
    }

    @SuppressLint("ClickableViewAccessibility")
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

        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.brokenwindow, null);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            this.params = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    2002, 56, 1);
        } else {
            this.params = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    2038, 56, 1);
        }
        params.x = 0;
        params.y = 0;
        params.alpha = 0.8f;

        this.mImage = view.findViewById(R.id.brokenwindow);

        windowManager2 = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager2.addView(view, params);
        mediaPlayer = MediaPlayer.create(this, R.raw.crack_screen);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

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
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this, 0, stopIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        notificationLayout.setOnClickPendingIntent(R.id.btn_stop, stopPendingIntent);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.img_logo).setStyle(new NotificationCompat.DecoratedCustomViewStyle()).setCustomContentView(notificationLayout).setPriority(NotificationCompat.PRIORITY_LOW).build();

        startForeground(NOTIFICATION_ID, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        imageResId = intent.getIntExtra("image_resource", R.drawable.img_screen_1);
        mImage.setBackgroundResource(imageResId);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (windowManager2 != null) {
            this.windowManager2.removeView(view);
            windowManager2 = null;
        } else {
            windowManager2 = (WindowManager) getSystemService(WINDOW_SERVICE);
            windowManager2.removeView(view);
            windowManager2 = null;
        }
        if (mediaPlayer != null) mediaPlayer.release();
    }
}