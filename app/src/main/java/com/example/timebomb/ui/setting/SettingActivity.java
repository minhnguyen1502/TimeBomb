package com.example.timebomb.ui.setting;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.timebomb.R;
import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivitySettingBinding;
import com.example.timebomb.dialog.rate.IClickDialogRate;
import com.example.timebomb.dialog.rate.RatingDialog;
import com.example.timebomb.ui.language.LanguageActivity;
import com.example.timebomb.util.SPUtils;
import com.example.timebomb.util.SharePrefUtils;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

public class SettingActivity extends BaseActivity<ActivitySettingBinding> {

    boolean isVibrate, isSound, isFlash;

    @Override
    public ActivitySettingBinding getBinding() {
        return ActivitySettingBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        isVibrate = SPUtils.getBoolean(this, SPUtils.IS_VIBRATE,false);
        isSound = SPUtils.getBoolean(this, SPUtils.IS_SOUND,false);
        isFlash = SPUtils.getBoolean(this, SPUtils.IS_FLASH,false);
    }

    @Override
    public void bindView() {
        binding.swVibrate.setImageResource(isVibrate ? R.drawable.img_switch_s : R.drawable.img_switch_ns);
        binding.swSound.setImageResource(isSound ? R.drawable.img_switch_s : R.drawable.img_switch_ns);
        binding.swFlash.setImageResource(isFlash ? R.drawable.img_switch_s : R.drawable.img_switch_ns);

        binding.swVibrate.setOnClickListener(v -> {
            isVibrate = !isVibrate;
            if (isVibrate) {
                SPUtils.setBoolean(this, SPUtils.IS_VIBRATE, isVibrate);
                binding.swVibrate.setImageResource(R.drawable.img_switch_s);
            } else {
                SPUtils.setBoolean(this, SPUtils.IS_VIBRATE, isVibrate);
                binding.swVibrate.setImageResource(R.drawable.img_switch_ns);
            }
        });

        binding.swSound.setOnClickListener(v -> {
            isSound = !isSound;
            if (isSound) {
                SPUtils.setBoolean(this, SPUtils.IS_SOUND, isSound);
                binding.swSound.setImageResource(R.drawable.img_switch_s);
            } else {
                SPUtils.setBoolean(this, SPUtils.IS_SOUND, isSound);
                binding.swSound.setImageResource(R.drawable.img_switch_ns);
            }
        });

        binding.swFlash.setOnClickListener(v -> {
            isFlash = !isFlash;
            if (isFlash) {
                SPUtils.setBoolean(this, SPUtils.IS_FLASH, isFlash);
                binding.swFlash.setImageResource(R.drawable.img_switch_s);
            } else {
                SPUtils.setBoolean(this, SPUtils.IS_FLASH, isFlash);
                binding.swFlash.setImageResource(R.drawable.img_switch_ns);
            }
        });

        binding.btnAbout.setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));
        binding.btnLanguage.setOnClickListener(v -> startActivity(new Intent(this, LanguageActivity.class)));
        binding.btnRate.setOnClickListener(v -> rate());
        binding.btnShare.setOnClickListener(v -> share());
        binding.ivBack.setOnClickListener(v -> onBack());
    }
    private void share() {
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        intentShare.putExtra(Intent.EXTRA_TEXT, "Download application :" + "https://play.google.com/store/apps/details?id=" + getPackageName());
        startActivity(Intent.createChooser(intentShare, "Share with"));
    }

    ReviewInfo reviewInfo;
    ReviewManager manager;

    private void rate() {

        RatingDialog ratingDialog = new RatingDialog(SettingActivity.this, true);
        ratingDialog.init(new IClickDialogRate() {
            @Override
            public void send() {
                binding.btnRate.setVisibility(View.GONE);
                ratingDialog.dismiss();
                String uriText = "mailto:" + SharePrefUtils.email + "?subject=" + "Review for " + SharePrefUtils.subject + "&body=" + SharePrefUtils.subject + "\nRate : " + ratingDialog.getRating() + "\nContent: ";
                Uri uri = Uri.parse(uriText);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                try {
                    startActivity(Intent.createChooser(sendIntent, getString(R.string.Send_Email)));
                    SharePrefUtils.forceRated(SettingActivity.this);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(SettingActivity.this, getString(R.string.There_is_no), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void rate() {
                manager = ReviewManagerFactory.create(SettingActivity.this);
                Task<ReviewInfo> request = manager.requestReviewFlow();
                request.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        reviewInfo = task.getResult();
                        Task<Void> flow = manager.launchReviewFlow(SettingActivity.this, reviewInfo);
                        flow.addOnSuccessListener(result -> {
                            binding.btnRate.setVisibility(View.GONE);
                            SharePrefUtils.forceRated(SettingActivity.this);
                            ratingDialog.dismiss();
                        });
                    } else {

                        ratingDialog.dismiss();
                    }
                });
            }

            @Override
            public void later() {

                ratingDialog.dismiss();
            }

        });
        try {
            ratingDialog.show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBack() {
        finish();
    }
}