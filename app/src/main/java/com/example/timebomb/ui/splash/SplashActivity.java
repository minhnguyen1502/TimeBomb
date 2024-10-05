package com.example.timebomb.ui.splash;

import static com.ads.sapp.util.GoogleMobileAdsConsentManager.getConsentResult;

import android.app.Application;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.ads.CommonAdCallback;
import com.ads.sapp.ads.wrapper.ApAdError;
import com.ads.sapp.funtion.AdCallback;
import com.ads.sapp.funtion.BannerCallback;
import com.ads.sapp.util.CheckAds;
import com.ads.sapp.util.GoogleMobileAdsConsentManager;
import com.example.timebomb.MyApplication;
import com.example.timebomb.R;
import com.example.timebomb.ads.AdsModel;
import com.example.timebomb.ads.ApiService;
import com.example.timebomb.ads.ConstantIdAds;
import com.example.timebomb.ads.ConstantRemote;
import com.example.timebomb.ads.IsNetWork;
import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivitySplashBinding;
import com.example.timebomb.ui.language.LanguageStartActivity;
import com.example.timebomb.util.EventTracking;
import com.example.timebomb.util.SPUtils;
import com.example.timebomb.util.SharePrefUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends BaseActivity<ActivitySplashBinding> {

    AdCallback adCallback;
    GoogleMobileAdsConsentManager googleMobileAdsConsentManager;

    @Override
    public ActivitySplashBinding getBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        SPUtils.setInt(this, SPUtils.CLICK, 0);
        EventTracking.logEvent(this, "splash_open");
        SharePrefUtils.increaseCountOpenApp(this);
    }

    private void callRemoteConfig() {
        ConstantRemote.initRemoteConfig(task -> {
            if (task.isSuccessful()) {
                ConstantRemote.banner_splash = ConstantRemote.getRemoteConfigBoolean("banner_splash");
                ConstantRemote.open_splash = ConstantRemote.getRemoteConfigBoolean("open_splash");
                ConstantRemote.inter_splash = ConstantRemote.getRemoteConfigBoolean("inter_splash");
                ConstantRemote.appopen_resume = ConstantRemote.getRemoteConfigBoolean("appopen_resume");
                ConstantRemote.native_language = ConstantRemote.getRemoteConfigBoolean("native_language");
                ConstantRemote.native_intro = ConstantRemote.getRemoteConfigBoolean("native_intro");
                ConstantRemote.inter_intro = ConstantRemote.getRemoteConfigBoolean("inter_intro");
                ConstantRemote.native_permission = ConstantRemote.getRemoteConfigBoolean("native_permission");
                ConstantRemote.banner_all = ConstantRemote.getRemoteConfigBoolean("banner_all");
                ConstantRemote.collapse_banner = ConstantRemote.getRemoteConfigBoolean("collapse_banner");
                ConstantRemote.inter_home = ConstantRemote.getRemoteConfigBoolean("inter_home");
                ConstantRemote.inter_all = ConstantRemote.getRemoteConfigBoolean("inter_all");
                ConstantRemote.inter_back = ConstantRemote.getRemoteConfigBoolean("inter_back");
                ConstantRemote.native_setting = ConstantRemote.getRemoteConfigBoolean("native_setting");

                ConstantRemote.rate_aoa_inter_splash = ConstantRemote.getRemoteConfigOpenSplash("rate_aoa_inter_splash");
                ConstantRemote.interval_interstitial_from_start_old = ConstantRemote.getRemoteConfigLong("interval_interstitial_from_start");
                ConstantRemote.interstitial_from_start = System.currentTimeMillis();
                ConstantRemote.interval_between_interstitial = ConstantRemote.getRemoteConfigLong("interval_between_interstitial");

                Log.e("call_api", "banner_splash: " + ConstantRemote.banner_splash);
                Log.e("call_api", "open_splash: " + ConstantRemote.open_splash);
                Log.e("call_api", "inter_splash: " + ConstantRemote.inter_splash);
                Log.e("call_api", "appopen_resume: " + ConstantRemote.appopen_resume);
                Log.e("call_api", "native_language: " + ConstantRemote.native_language);
                Log.e("call_api", "native_intro: " + ConstantRemote.native_intro);
                Log.e("call_api", "inter_intro: " + ConstantRemote.inter_intro);
                Log.e("call_api", "native_permission: " + ConstantRemote.native_permission);
                Log.e("call_api", "banner_all: " + ConstantRemote.banner_all);
                Log.e("call_api", "collapse_banner: " + ConstantRemote.collapse_banner);
                Log.e("call_api", "inter_home: " + ConstantRemote.inter_home);
                Log.e("call_api", "inter_all: " + ConstantRemote.inter_all);
                Log.e("call_api", "inter_back: " + ConstantRemote.inter_back);
                Log.e("call_api", "native_setting: " + ConstantRemote.native_setting);
                Log.e("call_api", "rate_aoa_inter_splash: " + ConstantRemote.rate_aoa_inter_splash);
                Log.e("call_api", "interval_interstitial_from_start: " + ConstantRemote.interval_interstitial_from_start_old);
                Log.e("call_api", "interval_between_interstitial: " + ConstantRemote.interval_between_interstitial);

            }
        });
    }

    private void callUMP() {
        if (IsNetWork.haveNetworkConnection(this)) {
            if (ConstantRemote.show_ump) {
                switch (SharePrefUtils.getInt("CONSENT_CHECK", 0)) {
                    case 0:
                        callConsent();
                        break;
                    case 1:
                        Application application = getApplication();
                        ((MyApplication) application).initAds();
                        new Handler().postDelayed(this::callApi, 2000);
                        break;
                    case 2:
                        new Handler().postDelayed(this::startNextActivity, 2000);
                        break;
                }
            } else {
                Application application = getApplication();
                ((MyApplication) application).initAds();
                new Handler().postDelayed(this::callApi, 2000);
            }
        } else {
            binding.rlBanner.setVisibility(View.GONE);
            new Handler().postDelayed(this::startNextActivity, 2000);
        }

    }

    private void callConsent() {
        googleMobileAdsConsentManager = GoogleMobileAdsConsentManager.getInstance(getApplicationContext());
        googleMobileAdsConsentManager.setSetTagForUnderAge(false);
        googleMobileAdsConsentManager.gatherConsent(this, complete -> {
            if (complete && googleMobileAdsConsentManager.canRequestAds()) {
                EventTracking.logEvent(this, "consent_click");
                Application application = getApplication();
                ((MyApplication) application).initAds();
            }

            if (getConsentResult(this)) {
                SharePrefUtils.putInt("CONSENT_CHECK", 1);
            } else {
                SharePrefUtils.putInt("CONSENT_CHECK", 2);
            }

            if (getConsentResult(this) && googleMobileAdsConsentManager.canRequestAds()) {
                callApi();
            } else {
                binding.rlBanner.setVisibility(View.GONE);
                new Handler().postDelayed(this::startNextActivity, 1500);
            }
        });
    }

    private void callApi() {
        ConstantIdAds.listIDAdsBannerSplash = new ArrayList<>();
        ConstantIdAds.listIDAdsOpenSplash = new ArrayList<>();
        ConstantIdAds.listIDAdsInterSplash = new ArrayList<>();
        ConstantIdAds.listIDAdsNativeLanguage = new ArrayList<>();
        ConstantIdAds.listIDAdsNativeIntro = new ArrayList<>();
        ConstantIdAds.listIDAdsInterIntro = new ArrayList<>();
        ConstantIdAds.listIDAdsNativePermission = new ArrayList<>();
        ConstantIdAds.listIDAdsBannerAll = new ArrayList<>();
        ConstantIdAds.listIDAdsCollapseBanner = new ArrayList<>();
        ConstantIdAds.listIDAdsInterHome = new ArrayList<>();
        ConstantIdAds.listIDAdsInterAll = new ArrayList<>();
        ConstantIdAds.listIDAdsInterBack = new ArrayList<>();
        ConstantIdAds.listIDAdsNativeSetting = new ArrayList<>();
        ConstantIdAds.listDriveID = new ArrayList<>();

        if (IsNetWork.haveNetworkConnectionUMP(this)) {
            try {
                ApiService.apiService.callAdsSplash().enqueue(new Callback<List<AdsModel>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<AdsModel>> call, @NonNull Response<List<AdsModel>> response) {
                        try {
                            if (response.body() != null) {
                                if (!response.body().isEmpty()) {
                                    for (AdsModel ads : response.body()) {
                                        switch (ads.getName()) {
                                            case "banner_splash":
                                                ConstantIdAds.listIDAdsBannerSplash.add(ads.getAds_id());
                                                break;
                                            case "open_splash":
                                                ConstantIdAds.listIDAdsOpenSplash.add(ads.getAds_id());
                                                break;
                                            case "inter_splash":
                                                ConstantIdAds.listIDAdsInterSplash.add(ads.getAds_id());
                                                break;
                                            case "native_language":
                                                ConstantIdAds.listIDAdsNativeLanguage.add(ads.getAds_id());
                                                break;
                                            case "native_intro":
                                                ConstantIdAds.listIDAdsNativeIntro.add(ads.getAds_id());
                                                break;
                                            case "inter_intro":
                                                ConstantIdAds.listIDAdsInterIntro.add(ads.getAds_id());
                                                break;
                                            case "native_permission":
                                                ConstantIdAds.listIDAdsNativePermission.add(ads.getAds_id());
                                                break;
                                            case "banner_all":
                                                ConstantIdAds.listIDAdsBannerAll.add(ads.getAds_id());
                                                break;
                                            case "collapse_banner":
                                                ConstantIdAds.listIDAdsCollapseBanner.add(ads.getAds_id());
                                                break;
                                            case "inter_home":
                                                ConstantIdAds.listIDAdsInterHome.add(ads.getAds_id());
                                                break;
                                            case "inter_all":
                                                ConstantIdAds.listIDAdsInterAll.add(ads.getAds_id());
                                                break;
                                            case "inter_back":
                                                ConstantIdAds.listIDAdsInterBack.add(ads.getAds_id());
                                                break;
                                            case "native_setting":
                                                ConstantIdAds.listIDAdsNativeSetting.add(ads.getAds_id());
                                                break;
                                            case "drive_id_test":
                                                ConstantIdAds.listDriveID.add(ads.getAds_id());
                                                break;
                                        }
                                    }
                                    Log.e("call_api", "banner_splash: " + ConstantIdAds.listIDAdsBannerSplash);
                                    Log.e("call_api", "open_splash: " + ConstantIdAds.listIDAdsOpenSplash);
                                    Log.e("call_api", "inter_splash: " + ConstantIdAds.listIDAdsInterSplash);
                                    Log.e("call_api", "native_language: " + ConstantIdAds.listIDAdsNativeLanguage);
                                    Log.e("call_api", "native_intro: " + ConstantIdAds.listIDAdsNativeIntro);
                                    Log.e("call_api", "inter_intro: " + ConstantIdAds.listIDAdsInterIntro);
                                    Log.e("call_api", "native_permission: " + ConstantIdAds.listIDAdsNativePermission);
                                    Log.e("call_api", "banner_all: " + ConstantIdAds.listIDAdsBannerAll);
                                    Log.e("call_api", "collapse_banner: " + ConstantIdAds.listIDAdsCollapseBanner);
                                    Log.e("call_api", "inter_home: " + ConstantIdAds.listIDAdsInterHome);
                                    Log.e("call_api", "inter_all: " + ConstantIdAds.listIDAdsInterAll);
                                    Log.e("call_api", "inter_back: " + ConstantIdAds.listIDAdsInterBack);
                                    Log.e("call_api", "native_setting: " + ConstantIdAds.listIDAdsNativeSetting);
                                    Log.e("call_api", "drive_id_test: " + ConstantIdAds.listDriveID);

//                                    ConstantIdAds.listDriveID.add("5ca6df621cd2aa1b");

                                    loadBanner();
                                } else {
                                    new Handler().postDelayed(() -> startNextActivity(), 2000);
                                }
                            } else {
                                new Handler().postDelayed(() -> startNextActivity(), 2000);
                            }
                        } catch (Exception e) {
                            new Handler().postDelayed(() -> startNextActivity(), 2000);
                        }

                    }

                    @Override
                    public void onFailure(Call<List<AdsModel>> call, Throwable t) {
                        Toast.makeText(SplashActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(() -> startNextActivity(), 2000);

                    }
                });
            } catch (Exception e) {
                Toast.makeText(this, "Catch", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(this::startNextActivity, 2000);
            }
        } else {
            findViewById(R.id.rlBanner).setVisibility(View.GONE);
            new Handler().postDelayed(this::startNextActivity, 2000);
        }
    }

    private void loadBanner(){
        //banner splash
        new Thread(() -> {
            runOnUiThread(() -> {
                if (!ConstantRemote.banner_splash) {
                    binding.rlBanner.setVisibility(View.GONE);
                    // false -> true khi test ok
                    CheckAds.getInstance().init(this, ConstantIdAds.listDriveID, false);
                    if (isShowAppOpenOrInter()) {
                        showAppOpenSplash();
                    } else {
                        showAppInterSplash();
                    }

                } else {
                    RelativeLayout layout = (RelativeLayout) LayoutInflater.from(this).inflate(com.ads.sapp.R.layout.layout_banner_control, null, false);
                    binding.rlBanner.addView(layout);

                    if (!ConstantIdAds.listIDAdsBannerSplash.isEmpty()) {
                        BannerCallback bannerCallback = new BannerCallback() {
                            @Override
                            public void onCheckComplete() {
                                super.onCheckComplete();
                                // xoá khi test ok
                                CheckAds.checkAd = false;
                                if (isShowAppOpenOrInter()) {
                                    showAppOpenSplash();
                                } else {
                                    showAppInterSplash();
                                }
                            }
                        };
                        Admob.getInstance().loadBannerSplash(this, ConstantIdAds.listIDAdsBannerSplash, ConstantIdAds.listDriveID, bannerCallback, 2000);
                        binding.rlBanner.setVisibility(View.VISIBLE);
                    } else {
                        binding.rlBanner.setVisibility(View.GONE);
                    }
                }
            });
        }).start();
    }

    private boolean isShowAppOpenOrInter() {
        String aoaOpenSplashValue = ConstantRemote.rate_aoa_inter_splash.get(0);

        // Kiểm tra giá trị trước khi chuyển đổi
        if (aoaOpenSplashValue == null || aoaOpenSplashValue.isEmpty()) {
            Log.e("Splash ads", "remote_aoa_openSplash is empty or null");
        }

        int appOpenRate;
        try {
            appOpenRate = Integer.parseInt(aoaOpenSplashValue);
        } catch (NumberFormatException e) {
            Log.e("Splash ads", "remote_aoa_openSplash is not a valid number: " + aoaOpenSplashValue);
            return false;
        }

        int random = new Random().nextInt(99) + 1;
        Log.e("Splash ads", String.valueOf(random));
        return random <= appOpenRate;
    }

    private void showAppInterSplash() {
        if (ConstantIdAds.listIDAdsInterSplash.size() != 0 && ConstantRemote.inter_splash) {
            CommonAd.getInstance().loadSplashInterstitialAdsCheck(SplashActivity.this, ConstantIdAds.listIDAdsInterSplash, 15000, 3500, new CommonAdCallback() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    startNextActivity();
                }

                @Override
                public void onAdFailedToLoad(ApAdError adError) {
                    super.onAdFailedToLoad(adError);
                    Log.e("Splash ads", "inter failed");
                    startNextActivity();
                }

                @Override
                public void onAdFailedToShow(ApAdError adError) {
                    super.onAdFailedToShow(adError);
                    Log.e("Splash ads", "inter failed");
                    startNextActivity();
                }
            });
        } else {
            startNextActivity();
        }
    }

    private void showAppOpenSplash() {
        if (ConstantIdAds.listIDAdsOpenSplash.size() != 0 && ConstantRemote.open_splash) {
            adCallback = new AdCallback() {
                @Override
                public void onNextAction() {
                    super.onNextAction();
                    Log.e("Splash ads", "open success");
                    startNextActivity();
                }
            };

            AppOpenManager.getInstance().loadOpenAppAdSplashFloorCheck(SplashActivity.this, ConstantIdAds.listIDAdsOpenSplash, true, adCallback);
        } else {
            Log.e("Splash ads", "open failed");
            startNextActivity();
        }
    }

    public void startNextActivity() {
        if (ConstantRemote.appopen_resume) {
            AppOpenManager.getInstance().enableAppResume();
        } else {
            AppOpenManager.getInstance().disableAppResume();
        }

        startNextActivity(LanguageStartActivity.class, null);
        finishAffinity();
    }

    @Override
    public void bindView() {
        callUMP();
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity.class);
        AppOpenManager.getInstance().disableAppResume();
        callRemoteConfig();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adCallback != null) {
            AppOpenManager.getInstance().onCheckShowSplashWhenFail(this, adCallback, 1000);
        }
    }

    @Override
    public void onBack() {

    }

    @Override
    protected void onDestroy() {
        AppOpenManager.getInstance().removeFullScreenContentCallback();
        super.onDestroy();
    }

}
