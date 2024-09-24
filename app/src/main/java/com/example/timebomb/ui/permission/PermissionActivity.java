package com.example.timebomb.ui.permission;

import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivityPermissionBinding;
import com.example.timebomb.ui.main.MainActivity;

public class PermissionActivity extends BaseActivity<ActivityPermissionBinding> {


    @Override
    public ActivityPermissionBinding getBinding() {
        return ActivityPermissionBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

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
