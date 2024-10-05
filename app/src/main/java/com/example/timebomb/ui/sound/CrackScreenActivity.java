package com.example.timebomb.ui.sound;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.timebomb.R;
import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivityCrackScreenBinding;
import com.example.timebomb.ui.sound.service.CrackScreen;
import com.example.timebomb.util.EventTracking;

public class CrackScreenActivity extends BaseActivity<ActivityCrackScreenBinding> {
    private int screen;

    @Override
    public ActivityCrackScreenBinding getBinding() {
        return ActivityCrackScreenBinding.inflate(getLayoutInflater());

    }

    @Override
    public void initView() {
        EventTracking.logEvent(this, "crackscreen_preview_view");

        Intent i = getIntent();
        int img = i.getIntExtra("img", -1);
        screen = i.getIntExtra("screen", -1);
        int sound = i.getIntExtra("sound", -1);
        if (img != -1) {
            binding.img.setImageResource(img);
        } else {
            binding.img.setVisibility(View.INVISIBLE);
        }
        String TAG = "CheckData";
        if (screen != -1) {
            Log.d(TAG, "" + screen);
        } else {
            Log.d(TAG, "" + screen);
        }
        if (sound != -1) {
            Log.d(TAG, "" + sound);

        } else {
            Toast.makeText(this, getString(R.string.no_sound), Toast.LENGTH_SHORT).show();
        }
        binding.img.setImageResource(img);
        binding.ivBack.setOnClickListener(v -> {
            EventTracking.logEvent(this, "crackscreen_preview_back_click");

            onBack();
        });
        binding.btnApply.setOnClickListener(v -> {
            EventTracking.logEvent(this, "crackscreen_preview_apply_click");

                Intent svc = new Intent(CrackScreenActivity.this, CrackTouchScreenActivity.class);
                svc.putExtra("image_resource", screen);
                startActivity(svc);
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                startActivity(intent);
                finishAffinity();

        });

    }


    @Override
    public void bindView() {

    }

    @Override
    public void onBack() {
        finish();
    }
}