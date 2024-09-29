package com.example.timebomb.ui.sound;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.timebomb.R;
import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivityCrackScreenBinding;

public class CrackScreenActivity extends BaseActivity<ActivityCrackScreenBinding>{

    @Override
    public ActivityCrackScreenBinding getBinding() {
        return ActivityCrackScreenBinding.inflate(getLayoutInflater());

    }

    @Override
    public void initView() {
        binding.ivBack.setOnClickListener(v -> onBack());

    }

    @Override
    public void bindView() {

    }

    @Override
    public void onBack() {
        finish();
    }
}