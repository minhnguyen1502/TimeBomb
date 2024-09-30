package com.example.timebomb.ui.sound;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.timebomb.R;
import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivityCrackScreenBinding;
import com.example.timebomb.databinding.DialogPermissionBinding;
import com.example.timebomb.ui.sound.service.CrackScreen;

public class CrackScreenActivity extends BaseActivity<ActivityCrackScreenBinding> {
    private int img, screen, sound;
    private final String TAG = "CheckData";

    @Override
    public ActivityCrackScreenBinding getBinding() {
        return ActivityCrackScreenBinding.inflate(getLayoutInflater());

    }

    @Override
    public void initView() {
        Intent i = getIntent();
        img = i.getIntExtra("img", -1);
        screen = i.getIntExtra("screen", -1);
        sound = i.getIntExtra("sound", -1);
        if (img != -1) {
            binding.img.setImageResource(img);
        } else {
            binding.img.setVisibility(View.INVISIBLE);
        }
        if (screen != -1) {
            Log.d(TAG, "" + screen);
        } else {
            Log.d(TAG, "" + screen);
        }
        if (sound != -1) {
            Log.d(TAG, "" + sound);

        } else {
            Toast.makeText(this, "No sound", Toast.LENGTH_SHORT).show();
        }
        binding.img.setImageResource(img);
        binding.ivBack.setOnClickListener(v -> onBack());
        binding.btnApply.setOnClickListener(v -> {
            if (checkOverlayPermission()) {
                Intent svc = new Intent(CrackScreenActivity.this, CrackScreen.class);
                svc.putExtra("image_resource", screen);
                startService(svc);
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                finishAffinity();
            } else {
                showDialogGotoSetting();
            }

        });

    }

    public boolean checkOverlayPermission() {
        return Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(this);
    }

    private void showDialogGotoSetting() {

        Dialog dialog = new Dialog(this);
        DialogPermissionBinding bindingPer = DialogPermissionBinding.inflate(getLayoutInflater());
        dialog.setContentView(bindingPer.getRoot());
        if (dialog.getWindow() != null) {
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        bindingPer.tvStay.setOnClickListener(v -> {
            dialog.dismiss();
        });
        bindingPer.tvAgree.setOnClickListener(v -> {
            Intent intent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            }
            louncherOverlay.launch(intent);
            dialog.dismiss();
        });

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private final ActivityResultLauncher<Intent> louncherOverlay = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                } else {
                }
            } else {
            }
        }
    });

    @Override
    public void bindView() {

    }

    @Override
    public void onBack() {
        finish();
    }
}