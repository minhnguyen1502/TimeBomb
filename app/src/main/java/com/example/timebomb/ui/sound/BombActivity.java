package com.example.timebomb.ui.sound;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import com.example.timebomb.R;
import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivityPlaySoundTimeBombBinding;
import com.example.timebomb.util.SPUtils;

import pl.droidsonroids.gif.GifDrawable;

public class BombActivity extends BaseActivity<ActivityPlaySoundTimeBombBinding>{
    private int minutes = 0;
    private int seconds = 0;
    private final int MAX_TIME = 10 * 60;
    private final int MIN_TIME = 0;
    private CountDownTimer countDownTimer;
    private boolean isCountingDown = false;
    private MediaPlayer mediaPlayer;
    boolean isVibrate, isSound, isFlash;

    @Override
    public ActivityPlaySoundTimeBombBinding getBinding() {
        return ActivityPlaySoundTimeBombBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        isVibrate = SPUtils.getBoolean(this, SPUtils.IS_VIBRATE, false);
        isSound = SPUtils.getBoolean(this, SPUtils.IS_SOUND, false);
        isFlash = SPUtils.getBoolean(this, SPUtils.IS_FLASH, false);
        Intent i = getIntent();
        int img = i.getIntExtra("img", -1);
        int sound = i.getIntExtra("sound", -1);
        if (img != -1) {
            binding.ivImg.setImageResource(img);
        } else {
            binding.ivImg.setVisibility(View.INVISIBLE);
        }
        if (sound != -1) {
            mediaPlayer = MediaPlayer.create(this, sound);
            mediaPlayer.setOnCompletionListener(mp -> stopVibrate());
        } else {
            Toast.makeText(this, "No sound", Toast.LENGTH_SHORT).show();
        }
        updateTimeDisplay();
    }

    @Override
    public void bindView() {
        binding.btnPlus.setOnClickListener(v -> increaseTime());
        binding.btnMinus.setOnClickListener(v -> decreaseTime());
        binding.btnStart.setOnClickListener(v -> startCountdown(currentTimeInSeconds));
        binding.ivBack.setOnClickListener(v -> onBack());

    }
    private boolean wasPlaying = false;

    @Override
    protected void onPause() {
        super.onPause();
        if (isCountingDown) {
            // Cancel the countdown timer
            countDownTimer.cancel();
            isCountingDown = false; // Update the state
        }
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                wasPlaying = true;
                mediaPlayer.pause();
                stopVibrate();
            } else {
                wasPlaying = false;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentTimeInSeconds > 0 && !isCountingDown) {
            // Restart the countdown timer with the remaining time
            startCountdown(currentTimeInSeconds);
        }
        if (mediaPlayer != null && wasPlaying) {
            if (isSound){
                mediaPlayer.start();
            }
            if (isVibrate) {
                startVibrate();
            }

        }
    }

    private void startCountdown(int remainingTime) {
        isCountingDown = true;
        countDownTimer = new CountDownTimer(remainingTime * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentTimeInSeconds = (int) millisUntilFinished / 1000;
                minutes = currentTimeInSeconds / 60;
                seconds = currentTimeInSeconds % 60;
                updateTimeDisplay();
                binding.btnPlus.setVisibility(View.INVISIBLE);
                binding.btnMinus.setVisibility(View.INVISIBLE);
                binding.btnStart.setVisibility(View.INVISIBLE);
                binding.time.setTextColor(android.graphics.Color.parseColor("#DB0202"));
                binding.time.setBackgroundResource(R.drawable.bg_start_countdown);
            }

            @Override
            public void onFinish() {
                isCountingDown = false;
                minutes = 0;
                seconds = 0;
                updateTimeDisplay();
                bomb();
                binding.btnPlus.setVisibility(View.VISIBLE);
                binding.btnMinus.setVisibility(View.VISIBLE);
                binding.btnStart.setVisibility(View.VISIBLE);
                binding.time.setTextColor(android.graphics.Color.parseColor("#FFFFFF"));
                binding.time.setBackgroundResource(0);
            }
        }.start();
    }

    int currentTimeInSeconds = 0;
    private void increaseTime() {
        currentTimeInSeconds = minutes * 60 + seconds;
        if (currentTimeInSeconds < MAX_TIME) {
            currentTimeInSeconds++;
            minutes = currentTimeInSeconds / 60;
            seconds = currentTimeInSeconds % 60;
            updateTimeDisplay();
        }
    }

    private void decreaseTime() {
        currentTimeInSeconds = minutes * 60 + seconds;
        if (currentTimeInSeconds > MIN_TIME) {
            currentTimeInSeconds--;
            minutes = currentTimeInSeconds / 60;
            seconds = currentTimeInSeconds % 60;
            updateTimeDisplay();
        }
    }

    private void updateTimeDisplay() {
        @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d", minutes, seconds);
        binding.time.setText(time);

        currentTimeInSeconds = minutes * 60 + seconds;

        if (currentTimeInSeconds <= MIN_TIME) {
            binding.btnMinus.setEnabled(false);
            binding.btnMinus.setImageResource(R.drawable.ic_minus_disable);
        } else {
            binding.btnMinus.setEnabled(true);
            binding.btnMinus.setImageResource(R.drawable.ic_minus_enable);

        }

        if (currentTimeInSeconds >= MAX_TIME) {
            binding.btnPlus.setEnabled(false);
            binding.btnPlus.setImageResource(R.drawable.ic_plus_disable);
        } else {
            binding.btnPlus.setEnabled(true);
            binding.btnPlus.setImageResource(R.drawable.ic_plus_enable);
        }
    }

    private void bomb(){
        binding.gif.setVisibility(View.VISIBLE);
        try {
            if (isSound){
                playSound();
            }
            if (isVibrate){
                startVibrate();
            }

            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.gif_bomb);
            binding.gif.setImageDrawable(gifDrawable);
            gifDrawable.setLoopCount(1);
            gifDrawable.addAnimationListener(i -> {
                binding.gif.setVisibility(View.GONE);
                stopSound();
                stopVibrate();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void playSound() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @SuppressLint("SetTextI18n")
    private void stopSound() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }

    }
    public void stopVibrate(){
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    public void startVibrate(){
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                long[] pattern = {0, 500, 500};
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0));
            }
        }
    }
    @Override
    public void onBack() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        finish();
    }
    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}