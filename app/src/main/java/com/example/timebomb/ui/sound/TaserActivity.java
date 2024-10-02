package com.example.timebomb.ui.sound;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.timebomb.R;
import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivityPlaySoundTaserGunBinding;
import com.example.timebomb.databinding.DialogBackgroundBinding;
import com.example.timebomb.ui.background.BackgroundAdapter;
import com.example.timebomb.ui.background.BackgroundModel;
import com.example.timebomb.util.SPUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class TaserActivity extends BaseActivity<ActivityPlaySoundTaserGunBinding> implements SensorEventListener {
    private int position;
    private MediaPlayer mediaPlayer;
    private boolean isHold = false;
    private boolean isTouch = false;
    private boolean isShake = false;
    List<BackgroundModel> backgroundList;
    int background;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float accelVal;
    private float accelLast;
    private float shake;
    boolean isVibrate, isSound, isFlash;
    private boolean isShow = false;

    @Override
    public ActivityPlaySoundTaserGunBinding getBinding() {
        return ActivityPlaySoundTaserGunBinding.inflate(getLayoutInflater());

    }

    @Override
    public void initView() {
        isVibrate = SPUtils.getBoolean(this, SPUtils.IS_VIBRATE, true);
        isSound = SPUtils.getBoolean(this, SPUtils.IS_SOUND, true);
        isFlash = SPUtils.getBoolean(this, SPUtils.IS_FLASH, true);

        background = SPUtils.getInt(this, SPUtils.BG_TASER, R.drawable.bg_04);
        binding.background.setBackgroundResource(background);

        Intent i = getIntent();
        int img = i.getIntExtra("img", -1);
        int sound = i.getIntExtra("sound", -1);
        position = i.getIntExtra("position", -1);

        if (img != -1) {
            if (position >= 0 && position <= 3) {
                binding.img2.setImageResource(img);
                binding.img.setVisibility(View.GONE);
            } else {
                binding.img.setImageResource(img);
                binding.img2.setVisibility(View.GONE);
            }
        } else {
            binding.img.setVisibility(View.INVISIBLE);
        }

        if (position != -1) {
            Log.e("position", "" + position);
        } else {
            binding.img.setVisibility(View.GONE);
            binding.img2.setVisibility(View.GONE);

        }

        if (sound != -1) {
            mediaPlayer = MediaPlayer.create(this, sound);
            mediaPlayer.setOnCompletionListener(mp -> {
                stopVibrate();
                binding.imgAnim.setVisibility(View.GONE);
                binding.imgAnim2.setVisibility(View.GONE);
                binding.imgAnim3.setVisibility(View.GONE);
                stopFlash();
                binding.ctlFunction.setVisibility(View.VISIBLE);

            });
        } else {
            Toast.makeText(this, getString(R.string.no_sound), Toast.LENGTH_SHORT).show();
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        accelVal = SensorManager.GRAVITY_EARTH;
        accelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;

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
        binding.icSingle.setImageResource(isHold ? R.drawable.ic_select : R.drawable.ic_n_select);
        binding.icShake.setImageResource(isHold ? R.drawable.ic_select : R.drawable.ic_n_select);

        binding.icSingle.setOnClickListener(v -> {
            isHold = false;
            binding.icHold.setImageResource(R.drawable.ic_n_select);
            isShake = false;
            binding.icShake.setImageResource(R.drawable.ic_n_select);
            isTouch = !isTouch;
            if (isTouch) {
                binding.icSingle.setImageResource(R.drawable.ic_select);
            } else {
                binding.icSingle.setImageResource(R.drawable.ic_n_select);
            }
        });

        binding.icShake.setOnClickListener(v -> {
            isHold = false;
            binding.icHold.setImageResource(R.drawable.ic_n_select);
            isTouch = false;
            binding.icSingle.setImageResource(R.drawable.ic_n_select);
            isShake = !isShake;
            if (isShake) {
                binding.icShake.setImageResource(R.drawable.ic_select);
            } else {
                binding.icShake.setImageResource(R.drawable.ic_n_select);
            }
        });

        binding.icHold.setOnClickListener(v -> {
            binding.icSingle.setImageResource(R.drawable.ic_n_select);
            isTouch = false;
            isShake = false;
            binding.icShake.setImageResource(R.drawable.ic_n_select);
            isHold = !isHold;
            if (isHold) {
                binding.icHold.setImageResource(R.drawable.ic_select);
            } else {
                binding.icHold.setImageResource(R.drawable.ic_n_select);
            }
        });

        binding.img.setOnClickListener(v -> {
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

                if (position >= 0 && position <= 3) {
                    binding.imgAnim2.setVisibility(View.VISIBLE);
                    binding.imgAnim.setVisibility(View.GONE);
                    binding.imgAnim3.setVisibility(View.GONE);
                } else if (position == 7) {
                    binding.imgAnim2.setVisibility(View.GONE);
                    binding.imgAnim.setVisibility(View.GONE);
                    binding.imgAnim3.setVisibility(View.VISIBLE);
                } else {
                    binding.imgAnim.setVisibility(View.VISIBLE);
                    binding.imgAnim2.setVisibility(View.GONE);
                    binding.imgAnim3.setVisibility(View.GONE);

                }
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
                if (position >= 0 && position <= 3) {
                    binding.imgAnim2.setVisibility(View.VISIBLE);
                    binding.imgAnim.setVisibility(View.GONE);
                    binding.imgAnim3.setVisibility(View.GONE);
                } else if (position == 7) {
                    binding.imgAnim2.setVisibility(View.GONE);
                    binding.imgAnim.setVisibility(View.GONE);
                    binding.imgAnim3.setVisibility(View.VISIBLE);
                } else {
                    binding.imgAnim.setVisibility(View.VISIBLE);
                    binding.imgAnim2.setVisibility(View.GONE);
                    binding.imgAnim3.setVisibility(View.GONE);

                }
            }
        });

        binding.img.setOnTouchListener((v, event) -> {
            if (isHold) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (isSound) {
                            if (mediaPlayer != null) {
                                mediaPlayer.start();
                            }
                            binding.ctlFunction.setVisibility(View.GONE);
                        } else {
                            if (mediaPlayer != null) {
                                mediaPlayer.start();
                                mediaPlayer.setVolume(0, 0);
                            }
                            binding.ctlFunction.setVisibility(View.GONE);
                        }
                        if (isVibrate) {
                            startVibrate();
                        }

                        if (isFlash) {
                            startFlash();
                        }
                        binding.ivBack.setClickable(false);
                        binding.ivBackground.setClickable(false);
                        mediaPlayer.setLooping(true);
                        if (position >= 0 && position <= 3) {
                            binding.imgAnim2.setVisibility(View.VISIBLE);
                            binding.imgAnim.setVisibility(View.GONE);
                            binding.imgAnim3.setVisibility(View.GONE);
                        } else if (position == 7) {
                            binding.imgAnim2.setVisibility(View.GONE);
                            binding.imgAnim.setVisibility(View.GONE);
                            binding.imgAnim3.setVisibility(View.VISIBLE);
                        } else {
                            binding.imgAnim.setVisibility(View.VISIBLE);
                            binding.imgAnim2.setVisibility(View.GONE);
                            binding.imgAnim3.setVisibility(View.GONE);

                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        stopSound();
                        stopFlash();
                        binding.imgAnim.setVisibility(View.GONE);
                        binding.imgAnim2.setVisibility(View.GONE);
                        binding.imgAnim3.setVisibility(View.GONE);
                        mediaPlayer.setLooping(false);
                        stopVibrate();
                        binding.ivBack.setClickable(true);
                        binding.ivBackground.setClickable(true);
                        return true;
                }
            }
            return false;
        });

        binding.img2.setOnTouchListener((v, event) -> {
            if (isHold) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (isSound) {
                            if (mediaPlayer != null) {
                                mediaPlayer.start();
                            }
                            binding.ctlFunction.setVisibility(View.GONE);
                        } else {
                            if (mediaPlayer != null) {
                                mediaPlayer.start();
                                mediaPlayer.setVolume(0, 0);
                            }
                            binding.ctlFunction.setVisibility(View.GONE);
                        }
                        if (isVibrate) {
                            startVibrate();
                        }
                        if (isFlash) {
                            startFlash();
                        }
                        mediaPlayer.setLooping(true);
                        if (position >= 0 && position <= 3) {
                            binding.imgAnim2.setVisibility(View.VISIBLE);
                            binding.imgAnim.setVisibility(View.GONE);
                            binding.imgAnim3.setVisibility(View.GONE);
                        } else if (position == 7) {
                            binding.imgAnim2.setVisibility(View.GONE);
                            binding.imgAnim.setVisibility(View.GONE);
                            binding.imgAnim3.setVisibility(View.VISIBLE);
                        } else {
                            binding.imgAnim.setVisibility(View.VISIBLE);
                            binding.imgAnim2.setVisibility(View.GONE);
                            binding.imgAnim3.setVisibility(View.GONE);

                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        stopSound();
                        stopFlash();
                        mediaPlayer.setLooping(false);
                        binding.imgAnim.setVisibility(View.GONE);
                        binding.imgAnim2.setVisibility(View.GONE);
                        binding.imgAnim3.setVisibility(View.GONE);

                        stopVibrate();
                        return true;
                }
            }
            return false;
        });

        binding.ivBack.setOnClickListener(v -> onBack());

        binding.ivBackground.setOnClickListener(v -> {
            if (!isShow) {
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
        int currentBackground = SPUtils.getInt(this, SPUtils.BG_TASER, -1);

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

        return new BackgroundAdapter(this, backgroundList, selectedPosition, (position1, backgroundModel) -> {
            binding.background.setBackgroundResource(backgroundModel.getImg());
            SPUtils.setInt(this, SPUtils.BG_TASER, backgroundModel.getImg());
        });
    }

    @SuppressLint("SetTextI18n")
    private void playSound() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            new android.os.Handler().postDelayed(() -> {
                stopSound();
                stopFlash();
                stopVibrate();
                binding.imgAnim.setVisibility(View.GONE);
                binding.imgAnim2.setVisibility(View.GONE);
                binding.imgAnim3.setVisibility(View.GONE);
            }, 500);
        }
        binding.ctlFunction.setVisibility(View.GONE);

    }

    private void playSoundNoVolumn() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            mediaPlayer.setVolume(0, 0);
            new android.os.Handler().postDelayed(() -> {
                stopSound();
                stopFlash();
                stopVibrate();
                binding.imgAnim.setVisibility(View.GONE);
                binding.imgAnim2.setVisibility(View.GONE);
                binding.imgAnim3.setVisibility(View.GONE);
            }, 500);
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        accelLast = accelVal;
        accelVal = (float) Math.sqrt((x * x) + (y * y) + (z * z));
        float delta = accelVal - accelLast;
        shake = shake * 0.9f + delta;

        if (shake > 6) {
            if (isShake) {
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
                if (position >= 0 && position <= 3) {
                    binding.imgAnim2.setVisibility(View.VISIBLE);
                    binding.imgAnim.setVisibility(View.GONE);
                    binding.imgAnim3.setVisibility(View.GONE);
                } else if (position == 7) {
                    binding.imgAnim2.setVisibility(View.GONE);
                    binding.imgAnim.setVisibility(View.GONE);
                    binding.imgAnim3.setVisibility(View.VISIBLE);
                } else {
                    binding.imgAnim.setVisibility(View.VISIBLE);
                    binding.imgAnim2.setVisibility(View.GONE);
                    binding.imgAnim3.setVisibility(View.GONE);

                }
            }
        } else if (shake == 0) {
            stopSound();
            stopVibrate();
            binding.imgAnim.setVisibility(View.GONE);
            binding.imgAnim2.setVisibility(View.GONE);
            binding.imgAnim3.setVisibility(View.GONE);
            stopFlash();
            binding.ctlFunction.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
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
    public void onBack() {
        sensorManager.unregisterListener(this);
        stopSound();
        stopFlash();
        stopVibrate();
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        sensorManager.unregisterListener(this);
        stopSound();
        stopFlash();
        stopVibrate();
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        if (mediaPlayer != null && wasPlaying) {
            if (isSound) {
                mediaPlayer.start();
            }
            if (isVibrate) {
                startVibrate();
            }
            if (isFlash) {
                startFlash();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

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


}