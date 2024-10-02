package com.example.timebomb.ui.intro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.timebomb.R;
import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivityIntroBinding;
import com.example.timebomb.ui.main.MainActivity;
import com.example.timebomb.ui.permission.PermissionActivity;
import com.example.timebomb.util.SharePrefUtils;

public class IntroActivity extends BaseActivity<ActivityIntroBinding> {
    ImageView[] dots = null;
    IntroAdapter introAdapter;
    String[] content;
    ViewPager viewPager;

    @Override
    public ActivityIntroBinding getBinding() {
        return ActivityIntroBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        viewPager = binding.viewPager2;

        content = new String[]{getString(R.string.surprise_shock_with_explosive_sounds), getString(R.string.choose_your_warzone_background), getString(R.string.many_unique_bomb_sound_to_prank)};

        dots = new ImageView[]{binding.ivCircle01, binding.ivCircle02, binding.ivCircle03};

        introAdapter = new IntroAdapter(this);

        viewPager.setAdapter(introAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeContentInit(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void bindView() {
        binding.btnNext.setOnClickListener(view -> {
            if (viewPager.getCurrentItem() == 0) {
            } else if (viewPager.getCurrentItem() == 1) {
            }
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        });

        binding.btnStart.setOnClickListener(view -> {

            goToNextScreen();
        });
    }

    @Override
    public void onBack() {
        finishAffinity();
    }

    private void changeContentInit(int position) {

        for (int i = 0; i < 3; i++) {
            if (i == position) dots[i].setImageResource(R.drawable.ic_intro_s);
            else dots[i].setImageResource(R.drawable.ic_intro_sn);
        }

        switch (position) {
            case 0:

                binding.viewBottom.setGravity(Gravity.CENTER);
            case 1:

                binding.btnNext.setVisibility(View.VISIBLE);
                binding.btnStart.setVisibility(View.GONE);
                break;
            case 2:

                binding.btnNext.setVisibility(View.GONE);
                binding.btnStart.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void goToNextScreen() {
//        if(checkOverlayPermission()){
//            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
            Intent intent = new Intent(IntroActivity.this, PermissionActivity.class);
            startActivity(intent);

//        }
    }

    public boolean checkOverlayPermission() {
        return Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(IntroActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        changeContentInit(viewPager.getCurrentItem());
    }
}