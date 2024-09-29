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


    @Override
    public ActivityPermissionBinding getBinding() {
        return ActivityPermissionBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

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

    }

    @Override
    public void bindView() {
        binding.tvContinue.setOnClickListener(v -> {

            startNextActivity(MainActivity.class, null);
            finishAffinity();
        });
        
    }

    @Override
    public void onBack() {
        finishAffinity();
    }
}
