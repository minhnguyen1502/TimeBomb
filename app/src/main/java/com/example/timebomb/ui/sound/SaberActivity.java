package com.example.timebomb.ui.sound;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.timebomb.R;
import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivityPlaySoundLightSaberBinding;
import com.example.timebomb.databinding.DialogBackgroundBinding;
import com.example.timebomb.ui.background.BackgroundAdapter;
import com.example.timebomb.ui.background.BackgroundModel;
import com.example.timebomb.util.SPUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class SaberActivity extends BaseActivity<ActivityPlaySoundLightSaberBinding>{

    private MediaPlayer mediaPlayer;
    private boolean isHold = false;
    private boolean isTouch = false;
    List<BackgroundModel> backgroundList;
    int background;
    boolean isVibrate, isSound, isFlash;
    private boolean isShow = false;

    @Override
    public ActivityPlaySoundLightSaberBinding getBinding() {
        return ActivityPlaySoundLightSaberBinding.inflate(getLayoutInflater());

    }

    @Override
    public void initView() {
        isVibrate = SPUtils.getBoolean(this, SPUtils.IS_VIBRATE, true);
        isSound = SPUtils.getBoolean(this, SPUtils.IS_SOUND, true);
        isFlash = SPUtils.getBoolean(this, SPUtils.IS_FLASH, true);
        background = SPUtils.getInt(this, SPUtils.BG_SABER, R.drawable.bg_04);
        binding.background.setBackgroundResource(background);
        Intent i = getIntent();
        int img1 = i.getIntExtra("img_1", -1);
        int img2 = i.getIntExtra("img_2", -1);
        int sound = i.getIntExtra("sound", -1);
        if (img1 != -1) {
            binding.img1.setImageResource(img1);
        } else {
            binding.img1.setVisibility(View.INVISIBLE);
        }
        if (img2 != -1) {
            binding.img2.setImageResource(img2);
        } else {
            binding.img2.setVisibility(View.INVISIBLE);
        }
        if (sound != -1) {
            mediaPlayer = MediaPlayer.create(this, sound);
            mediaPlayer.setOnCompletionListener(mp -> {
                stopVibrate();
                stopFlash();
                binding.img1.setVisibility(View.INVISIBLE);
                binding.ctlFunction.setVisibility(View.VISIBLE);

            });
        } else {
            Toast.makeText(this, getString(R.string.no_sound), Toast.LENGTH_SHORT).show();
        }
        backgroundList = new ArrayList<>();
        backgroundList.add(new BackgroundModel(R.drawable.bg_01));
        backgroundList.add(new BackgroundModel(R.drawable.bg_02));
        backgroundList.add(new BackgroundModel(R.drawable.bg_03));
        backgroundList.add(new BackgroundModel(R.drawable.bg_04));
        backgroundList.add(new BackgroundModel(R.drawable.bg_05));
        backgroundList.add(new BackgroundModel(R.drawable.bg_06));
        backgroundList.add(new BackgroundModel(R.drawable.bg_07));
        backgroundList.add(new BackgroundModel(R.drawable.bg_08));
        backgroundList.add(new BackgroundModel(R.drawable.bg_09));
        backgroundList.add(new BackgroundModel(R.drawable.bg_10));
        backgroundList.add(new BackgroundModel(R.drawable.bg_11));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void bindView() {
        binding.icHold.setImageResource(isHold ? R.drawable.ic_select : R.drawable.ic_n_select);
        binding.icTouch.setImageResource(isHold ? R.drawable.ic_select : R.drawable.ic_n_select);

        binding.icTouch.setOnClickListener(v -> {
            isHold = false;
            binding.icHold.setImageResource(R.drawable.ic_n_select);
            isTouch = !isTouch;
            if (isTouch) {
                binding.icTouch.setImageResource(R.drawable.ic_select);
            } else {
                binding.icTouch.setImageResource(R.drawable.ic_n_select);
            }
        });

        binding.icHold.setOnClickListener(v -> {
            binding.icTouch.setImageResource(R.drawable.ic_n_select);
            isTouch = false;
            isHold = !isHold;
            if (isHold) {
                binding.icHold.setImageResource(R.drawable.ic_select);
            } else {
                binding.icHold.setImageResource(R.drawable.ic_n_select);
            }
        });

        binding.img2.setOnClickListener(v -> {
            if (isTouch) {
                if (isSound) {
                    playSound();
                } else {
                    playSoundNoVolumn();
                }
                if (isVibrate) {
                    startVibrate();
                }
                if (isFlash) {
                    startFlash();
                }

                binding.img1.setVisibility(View.VISIBLE);

                binding.img1.setClipBounds(new Rect(0, 0, binding.img1.getWidth(), binding.img1.getHeight()));

                int fullHeight = binding.img1.getHeight();

                ValueAnimator animator = ValueAnimator.ofInt(0, fullHeight);
                animator.setDuration(2000);
                animator.setInterpolator(new DecelerateInterpolator());

                animator.addUpdateListener(animation -> {
                    int currentHeight = (int) animation.getAnimatedValue();
                    binding.img1.setClipBounds(new Rect(0, fullHeight - currentHeight, binding.img1.getWidth(), fullHeight));
                });

                animator.start();
            }
        });


        binding.img2.setOnTouchListener((v, event) -> {
            if (isHold) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (isSound) {
                            playSound();
                        } else {
                            playSoundNoVolumn();
                        }
                        if (isVibrate){
                            startVibrate();
                        }
                        if (isFlash){
                            startFlash();
                        }
                        binding.ivBack.setClickable(false);
                        binding.ivBackground.setClickable(false);
                        mediaPlayer.setLooping(true);
                        binding.img1.setVisibility(View.VISIBLE);
                        binding.img1.setClipBounds(new Rect(0, 0, binding.img1.getWidth(), binding.img1.getHeight()));
                        int fullHeight = binding.img1.getHeight();
                        ValueAnimator animator = ValueAnimator.ofInt(0, fullHeight);
                        animator.setDuration(2000);
                        animator.setInterpolator(new DecelerateInterpolator());
                        animator.addUpdateListener(animation -> {
                            int currentHeight = (int) animation.getAnimatedValue();
                            binding.img1.setClipBounds(new Rect(0, fullHeight - currentHeight, binding.img1.getWidth(), fullHeight));
                        });
                        animator.start();
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        stopSound();
                        stopFlash();
                        mediaPlayer.setLooping(false);
                        binding.img1.setVisibility(View.INVISIBLE);
                        binding.ivBack.setClickable(true);
                        binding.ivBackground.setClickable(true);
                        stopVibrate();
                        return true;
                }
            }
            return false;
        });

        binding.ivBack.setOnClickListener(v -> onBack());

        binding.ivBackground.setOnClickListener(v -> {
            if (!isShow){
                dialogBackground();
            }
        });
    }

    private void dialogBackground() {
        isShow = true;
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        DialogBackgroundBinding dialogBinding = DialogBackgroundBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        int currentBackground = SPUtils.getInt(this, SPUtils.BG_SABER, -1);

        BackgroundAdapter adapter = getBackgroundAdapter(currentBackground);
        dialogBinding.rcvBackground.setAdapter(adapter);
        dialogBinding.rcvBackground.setLayoutManager(new GridLayoutManager(this, 2));
        dialogBinding.ivBack.setOnClickListener(v -> {
            dialog.dismiss();
            isShow = false;
        });

        if (dialog.isShowing()) {
            dialog.dismiss();
            isShow = false;
        }

        dialog.setOnDismissListener(dialog1 -> isShow = false);
        dialog.show();

    }

    @NonNull
    private BackgroundAdapter getBackgroundAdapter(int currentBackground) {
        int selectedPosition = 3;
        for (int i = 0; i < backgroundList.size(); i++) {
            if (backgroundList.get(i).getImg() == currentBackground) {
                selectedPosition = i;
                break;
            }
        }

        return new BackgroundAdapter(this, backgroundList,selectedPosition, (position, backgroundModel) -> {
            binding.background.setBackgroundResource(backgroundModel.getImg());
            SPUtils.setInt(this, SPUtils.BG_SABER, backgroundModel.getImg());
        });
    }

    private void playSound() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
        binding.ctlFunction.setVisibility(View.GONE);

    }
    private void playSoundNoVolumn() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            mediaPlayer.setVolume(0, 0);
        }
        binding.ctlFunction.setVisibility(View.GONE);

    }

    @SuppressLint("SetTextI18n")
    private void stopSound() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }
        binding.ctlFunction.setVisibility(View.VISIBLE);

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
    private boolean isBlinking = false;
    private CameraManager cameraManager;
    private String cameraId;
    private void toggleFlashlight(boolean turnOn) {
        try {
            if (cameraManager == null) {
                cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                cameraId = cameraManager.getCameraIdList()[0];
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, turnOn);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void startFlash() {
        isBlinking = true;
        new Thread(() -> {
            while (isBlinking) {
                toggleFlashlight(true);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                toggleFlashlight(false);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void stopFlash() {
        isBlinking = false;
        toggleFlashlight(false);
    }

    private boolean wasPlaying = false;

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                wasPlaying = true;
                mediaPlayer.pause();
                stopVibrate();
                stopFlash();
            } else {
                wasPlaying = false;
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && wasPlaying) {
            if (isSound){
                mediaPlayer.start();
            }
            if (isVibrate) {
                startVibrate();
            }
            if (isFlash){
                startFlash();
            }
        }

    }

    @Override
    public void onBack() {
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