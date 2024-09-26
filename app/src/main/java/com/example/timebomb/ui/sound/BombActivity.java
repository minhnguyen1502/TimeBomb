package com.example.timebomb.ui.sound;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.View;

import com.example.timebomb.R;
import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivityPlaySoundTimeBombBinding;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;

public class BombActivity extends BaseActivity<ActivityPlaySoundTimeBombBinding>{
    private int minutes = 0;
    private int seconds = 0;
    private final int MAX_TIME = 10 * 60; // 10:00 in seconds
    private final int MIN_TIME = 0;
    private CountDownTimer countDownTimer;
    private boolean isCountingDown = false;

    @Override
    public ActivityPlaySoundTimeBombBinding getBinding() {
        return ActivityPlaySoundTimeBombBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        updateTimeDisplay();
    }

    @Override
    public void bindView() {
        binding.btnPlus.setOnClickListener(v -> increaseTime());
        binding.btnMinus.setOnClickListener(v -> decreaseTime());
        binding.btnStart.setOnClickListener(v -> startCountdown());
    }
    private void increaseTime() {
        int currentTimeInSeconds = minutes * 60 + seconds;
        if (currentTimeInSeconds < MAX_TIME) {
            currentTimeInSeconds++;
            minutes = currentTimeInSeconds / 60;
            seconds = currentTimeInSeconds % 60;
            updateTimeDisplay();
        }
    }

    private void decreaseTime() {
        int currentTimeInSeconds = minutes * 60 + seconds;
        if (currentTimeInSeconds > MIN_TIME) {
            currentTimeInSeconds--;
            minutes = currentTimeInSeconds / 60;
            seconds = currentTimeInSeconds % 60;
            updateTimeDisplay();
        }
    }

    private void updateTimeDisplay() {
        String time = String.format("%02d:%02d", minutes, seconds);
        binding.time.setText(time);

        int currentTimeInSeconds = minutes * 60 + seconds;

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
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.gif_bomb);
            binding.gif.setImageDrawable(gifDrawable);
            gifDrawable.setLoopCount(1);
            gifDrawable.addAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationCompleted(int i) {
                    binding.gif.setVisibility(View.GONE);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void startCountdown() {
        if (isCountingDown) return;  // Prevent multiple timers

        int totalTimeInSeconds = minutes * 60 + seconds;
        if (totalTimeInSeconds > 0) {
            isCountingDown = true;
            countDownTimer = new CountDownTimer(totalTimeInSeconds * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int currentTimeInSeconds = (int) millisUntilFinished / 1000;
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
    }
    @Override
    public void onBack() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        finish();
    }
}