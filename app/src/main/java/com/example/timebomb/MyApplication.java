package com.example.timebomb;

import android.app.Application;

import com.example.timebomb.util.SharePrefUtils;

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        SharePrefUtils.init(this);

    }

}

