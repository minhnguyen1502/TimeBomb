package com.example.timebomb.ads;

import android.os.Handler;

import com.example.timebomb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.ArrayList;
import java.util.Arrays;

public class ConstantRemote {
    public static boolean show_ump = true;
    public static boolean banner_splash = true;
    public static boolean open_splash = true;
    public static boolean inter_splash = true;
    public static boolean appopen_resume = true;
    public static boolean native_language = true;
    public static boolean native_intro = true;
    public static boolean inter_intro = true;
    public static boolean native_permission = true;
    public static boolean banner_all = true;
    public static boolean collapse_banner = true;
    public static boolean inter_home = true;
    public static boolean inter_all = true;
    public static boolean inter_back = true;
    public static boolean native_setting = true;

    public static long interstitial_from_start = 0;
    public static long interval_interstitial_from_start_old = 0;
    public static long interval_between_interstitial = 0;
    public static long time_interval_old = 0;

    public static ArrayList<String> rate_aoa_inter_splash = new ArrayList<>(Arrays.asList("10", "90"));

    public static void initRemoteConfig(OnCompleteListener listener) {
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.reset();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(3600).build();
        new Handler().postDelayed(() -> {
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
            mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
            mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(listener);
        }, 2000);
    }


    public static boolean getRemoteConfigBoolean(String adUnitId) {
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        return mFirebaseRemoteConfig.getBoolean(adUnitId);
    }

    public static ArrayList<String> getRemoteConfigString(String adUnitId) {
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        String object = mFirebaseRemoteConfig.getString(adUnitId);
        String[] arStr = object.split(",");
        return new ArrayList<>(Arrays.asList(arStr));
    }

    public static long getRemoteConfigLong(String adUnitId) {
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        return mFirebaseRemoteConfig.getLong(adUnitId);
    }

    public static ArrayList<String> getRemoteConfigOpenSplash(String adUnitId) {
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        String object = mFirebaseRemoteConfig.getString(adUnitId);
        String[] arStr = object.split("_");
        return new ArrayList<>(Arrays.asList(arStr));
    }
}
