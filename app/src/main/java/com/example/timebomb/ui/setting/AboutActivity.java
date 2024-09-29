package com.example.timebomb.ui.setting;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.timebomb.R;
import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivityAboutBinding;
import com.example.timebomb.ui.policy.PolicyActivity;

public class AboutActivity extends BaseActivity<ActivityAboutBinding> {

    @Override
    public ActivityAboutBinding getBinding() {
        return ActivityAboutBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
//        binding.txt.setText(getString(R.string.version) + " " + BuildConfig.VERSION_NAME);

        binding.ivBack.setOnClickListener(v -> onBack());

        binding.tvPolicy.setOnClickListener(v -> {
            startActivity(new Intent(AboutActivity.this, PolicyActivity.class));
        });
    }

    @Override
    public void bindView() {

    }

    @Override




    public void onBack() {
        finish();
    }
}