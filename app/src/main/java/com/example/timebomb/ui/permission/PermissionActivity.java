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


    private final int REQUEST_CODE_MEDIA_PERMISSION = 128;
    private int countMedia = 0;

    @Override
    public ActivityPermissionBinding getBinding() {
        return ActivityPermissionBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        countMedia = SPUtils.getInt(this, SPUtils.MEDIA, 0);

    }

    private boolean checkMediaPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
            }
            return false;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void checkSwMedia() {

        if (checkMediaPermission()) {
            binding.swNoti.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.img_switch_s));
        } else {
            binding.swNoti.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.img_switch_ns));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_MEDIA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkSwMedia();
            }

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                checkSwMedia();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!shouldShowRequestPermissionRationale(permissions[0])) {
                        countMedia++;
                        SPUtils.setInt(this, SPUtils.MEDIA, countMedia);
                        if (countMedia > 1) {
                            showDialogGotoSetting();
                        }
                    }
                }
            }
        }
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

            dialog.dismiss();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        });

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSwMedia();

    }

    @Override
    public void bindView() {
        binding.tvContinue.setOnClickListener(v -> {

            startNextActivity(MainActivity.class, null);
            finishAffinity();
        });

        binding.swNoti.setOnClickListener(view -> {

            if (!checkMediaPermission()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE_MEDIA_PERMISSION);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_MEDIA_PERMISSION);
                }
            }
        });
    }

    @Override
    public void onBack() {
        finishAffinity();
    }
}
