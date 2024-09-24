package com.example.timebomb.ui.intro;

import android.view.View;
import android.widget.ImageView;

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

    @Override
    public ActivityIntroBinding getBinding() {
        return ActivityIntroBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        dots = new ImageView[]{binding.ivCircle01, binding.ivCircle02, binding.ivCircle03};

        introAdapter = new IntroAdapter(this);

        binding.viewPager2.setAdapter(introAdapter);

        binding.viewPager2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        binding.btnNext.setOnClickListener(view -> binding.viewPager2.setCurrentItem(binding.viewPager2.getCurrentItem() + 1));

        binding.btnStart.setOnClickListener(view -> goToNextScreen());
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
        if (SharePrefUtils.getCountOpenApp(this) > 1) {
            startNextActivity(MainActivity.class, null);
        } else {
            startNextActivity(PermissionActivity.class, null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        changeContentInit(binding.viewPager2.getCurrentItem());
    }
}