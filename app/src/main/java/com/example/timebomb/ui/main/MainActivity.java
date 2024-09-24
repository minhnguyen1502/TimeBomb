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
        binding.viewTop.tvToolBar.setText("MainActivity");
        binding.viewTop.ivCheck.setVisibility(View.INVISIBLE);

        binding.viewPager.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager(), getLifecycle());
        binding.viewPager.setAdapter(mainAdapter);
        binding.viewPager.setUserInputEnabled(false);
    }

    @Override
    public void bindView() {
        binding.viewTop.ivBack.setOnClickListener(v -> onBack());

        binding.rlFragmentA.setOnClickListener(view -> setPage(view, 0));

        binding.rlFragmentB.setOnClickListener(view -> setPage(view, 1));
    }

    @Override
    public void onBack() {
        finishThisActivity();
    }

    public void setPage(View view, int pos){
        binding.viewPager.setCurrentItem(pos, false);

        switch (pos){
            case 0:

                break;

            case 1:

                break;
        }

    }
}
