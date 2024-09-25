package com.example.timebomb.ui.main;

import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {


    @Override
    public ActivityMainBinding getBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

    }

    @Override
    public void bindView() {

    }

    @Override
    public void onBack() {
        finishThisActivity();
    }

}
