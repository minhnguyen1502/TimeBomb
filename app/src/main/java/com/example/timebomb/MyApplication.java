package com.example.timebomb;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.ads.CommonAdConfig;
import com.ads.sapp.application.AdsMultiDexApplication;
import com.example.timebomb.ads.AppViewModel;
import com.example.timebomb.ui.splash.SplashActivity;
import com.example.timebomb.util.SharePrefUtils;

public class MyApplication extends AdsMultiDexApplication {
    private static Context appContext;
    private static AppViewModel viewModel;

    public static Context getAppContext() {
        return appContext;
    }

    public static AppViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        viewModel = new AppViewModel(this);
        SharePrefUtils.init(appContext);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            builder.detectFileUriExposure();
            StrictMode.setVmPolicy(builder.build());
        }
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity.class);
        Admob.getInstance().setNumToShowAds(0);
    }
    public void initAds() {
        commonAdConfig.setMediationProvider(CommonAdConfig.PROVIDER_ADMOB);
        commonAdConfig.setVariant(true);
        commonAdConfig.setIdAdResume(getString(R.string.resume));
        commonAdConfig.setListDeviceTest(listTestDevice);
        commonAdConfig.setMediationFloor(CommonAdConfig.WARTER_FALL);

        CommonAd.getInstance().init(this, commonAdConfig, false);
        Admob.getInstance().setOpenActivityAfterShowInterAds(true);
        Admob.getInstance().setDisableAdResumeWhenClickAds(true);
    }
}