package com.example.timebomb.ui.sound;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.timebomb.R;
import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivityPlaySoundTimeBombBinding;
import com.example.timebomb.util.SPUtils;

import pl.droidsonroids.gif.GifDrawable;

public class BombActivity extends BaseActivity<ActivityPlaySoundTimeBombBinding> {
    private long minutes = 0;
    private long seconds = 0;
    private final long MAX_TIME = 10 * 60;
    private final long MIN_TIME = 0;
    private CountDownTimer countDownTimer;
    private boolean isCountingDown = false;
    private MediaPlayer mediaPlayer;
    boolean isVibrate, isSound, isFlash;
    long currentTimeInSeconds = 0;
    private boolean isBom = false;
    private boolean isPlay = false;

    @Override
    public ActivityPlaySoundTimeBombBinding getBinding() {
        return ActivityPlaySoundTimeBombBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        currentTimeInSeconds = 5;
        minutes = currentTimeInSeconds / 60;
        seconds = currentTimeInSeconds % 60;
        isVibrate = SPUtils.getBoolean(this, SPUtils.IS_VIBRATE, true);
        isSound = SPUtils.getBoolean(this, SPUtils.IS_SOUND, true);
        isFlash = SPUtils.getBoolean(this, SPUtils.IS_FLASH, true);
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
        binding.btnStart.setOnClickListener(v -> startCountdown(currentTimeInSeconds+1));
        binding.ivBack.setOnClickListener(v -> onBack());
        binding.gif.setOnClickListener(v -> {
            if (!isBom){
                binding.gif.setVisibility(View.GONE);
            }
        });
    }


    private void startCountdown(long remainingTime) {
        isCountingDown = true;
        isPlay = true;
        currentTimeInSeconds = remainingTime;
        minutes = remainingTime / 60;
        seconds = remainingTime % 60;
        @SuppressLint("DefaultLocale") String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        binding.time.setText(timeFormatted);

        countDownTimer = new CountDownTimer(remainingTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                minutes = (millisUntilFinished / 1000) / 60;
                seconds = (millisUntilFinished / 1000) % 60;
                @SuppressLint("DefaultLocale") String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                binding.time.setText(timeFormatted);
                binding.btnPlus.setVisibility(View.INVISIBLE);
                binding.btnMinus.setVisibility(View.INVISIBLE);
                binding.btnStart.setVisibility(View.INVISIBLE);
                binding.time.setTextColor(Color.parseColor("#DB0202"));
                binding.time.setBackgroundResource(R.drawable.bg_start_countdown);
                currentTimeInSeconds = millisUntilFinished / 1000;
                Log.e("time: ", currentTimeInSeconds + "");
            }

            @Override
            public void onFinish() {
                isPlay = false;
                isCountingDown = false;
                currentTimeInSeconds = 5;
                minutes = currentTimeInSeconds / 60;
                seconds = currentTimeInSeconds % 60;
                updateTimeDisplay();
                bomb();
                binding.btnPlus.setVisibility(View.VISIBLE);
                binding.btnMinus.setVisibility(View.VISIBLE);
                binding.btnStart.setVisibility(View.VISIBLE);
                binding.time.setTextColor(Color.parseColor("#FFFFFF"));
                binding.time.setBackgroundResource(0);
            }
        }.start();
    }


    private void increaseTime() {
        currentTimeInSeconds = minutes * 60L + seconds;
        if (currentTimeInSeconds < MAX_TIME) {
            currentTimeInSeconds += 5;
            minutes = currentTimeInSeconds / 60;
            seconds = currentTimeInSeconds % 60;
            updateTimeDisplay();
        }
    }

    private void decreaseTime() {
        currentTimeInSeconds = minutes * 60L + seconds;
        if (currentTimeInSeconds > MIN_TIME) {
            currentTimeInSeconds -= 5;
            minutes = currentTimeInSeconds / 60;
            seconds = currentTimeInSeconds % 60;
            updateTimeDisplay();
        }
    }

    private void updateTimeDisplay() {
        @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d", minutes, seconds);
        binding.time.setText(time);

        currentTimeInSeconds = minutes * 60L + seconds;

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

    private void bomb() {
        isBom = true;
        binding.gif.setVisibility(View.VISIBLE);
        try {
            if (isSound) {
                playSound();
            } else {
                playSoundNoVolumn();
            }
            if (isVibrate) {
                startVibrate();
            }

            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.gif_bomb);
            binding.gif.setImageDrawable(gifDrawable);
            gifDrawable.setLoopCount(1);
            gifDrawable.addAnimationListener(i -> {
                isBom = false;
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

    private void playSoundNoVolumn() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            mediaPlayer.setVolume(0, 0);
        }
    }

    @SuppressLint("SetTextI18n")
    private void stopSound() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }

    }

    public void stopVibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    public void startVibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                long[] pattern = {0, 500, 500};
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0));
            }
        }
    }

    private boolean wasPlaying = false;
    private boolean pause = false;

    @Override
    protected void onPause() {
        super.onPause();
        if (isCountingDown) {
            countDownTimer.cancel();
            isCountingDown = false;
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
        if (currentTimeInSeconds > 0 && !isCountingDown && isPlay) {
            startCountdown(currentTimeInSeconds);
        }
        if (mediaPlayer != null && wasPlaying) {
            if (isSound) {
                mediaPlayer.start();
            }
            if (isVibrate) {
                startVibrate();
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