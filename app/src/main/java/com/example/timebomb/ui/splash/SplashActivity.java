package com.example.timebomb.ui.splash;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.example.timebomb.R;
import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivitySplashBinding;
import com.example.timebomb.ui.intro.IntroActivity;
import com.example.timebomb.ui.language.LanguageStartActivity;
import com.example.timebomb.util.SharePrefUtils;
import com.example.timebomb.util.Utils;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;


public class SplashActivity extends BaseActivity<ActivitySplashBinding> {


    @Override
    public ActivitySplashBinding getBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        binding.background.setBackgroundResource(R.drawable.bg_home);
        SharePrefUtils.increaseCountOpenApp(this);

        new Handler(Looper.getMainLooper()).postDelayed(this::runAfterFinish,3000);


    }

    @Override
    public void bindView() {
    }
    public void runAfterFinish() {

        if (!isFinishing() && !isDestroyed()) {

            if (!Utils.isLanguageSelected()) {
                Intent intent = new Intent(this, LanguageStartActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(this, IntroActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
    @Override
    public void onBack() {

    }
}
