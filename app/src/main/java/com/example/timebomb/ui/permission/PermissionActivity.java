package com.example.timebomb.ui.permission;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.RelativeLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.timebomb.R;
import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivityPermissionBinding;
import com.example.timebomb.databinding.DialogPermissionBinding;
import com.example.timebomb.ui.main.MainActivity;
import com.example.timebomb.util.SPUtils;

public class PermissionActivity extends BaseActivity<ActivityPermissionBinding> {


    @Override
    public ActivityPermissionBinding getBinding() {
        return ActivityPermissionBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

    }
    public boolean checkOverlayPermission() {
        return Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(PermissionActivity.this);
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

        bindingPer.tvStay.setOnClickListener(v -> dialog.dismiss());
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
                if (Settings.canDrawOverlays(PermissionActivity.this)) {
                } else {
                }
            } else {
            }
        }
    });
    @Override
    protected void onResume() {
        super.onResume();
        if (checkOverlayPermission()) {
                binding.swOverlay.setImageResource(R.drawable.img_switch_s);
            } else {
                binding.swOverlay.setImageResource(R.drawable.img_switch_ns);
            }

    }

    @Override
    public void bindView() {
        binding.tvContinue.setOnClickListener(v -> {

            startNextActivity(MainActivity.class, null);
            finishAffinity();
        });

        binding.swOverlay.setOnClickListener(v -> {
            if (!checkOverlayPermission()) {
                showDialogGotoSetting();
            }
        });
        
    }

    @Override
    public void onBack() {
        finishAffinity();
    }
}
