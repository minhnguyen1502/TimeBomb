package com.example.timebomb.ui.sound;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.timebomb.R;
import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivityCrackScreenBinding;
import com.example.timebomb.ui.sound.service.CrackScreen;

public class CrackScreenActivity extends BaseActivity<ActivityCrackScreenBinding> {
    private int screen;

    @Override
    public ActivityCrackScreenBinding getBinding() {
        return ActivityCrackScreenBinding.inflate(getLayoutInflater());

    }

    @Override
    public void initView() {
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
        binding.ivBack.setOnClickListener(v -> onBack());
        binding.btnApply.setOnClickListener(v -> {
                Intent svc = new Intent(CrackScreenActivity.this, CrackScreen.class);
                svc.putExtra("image_resource", screen);
                startService(svc);
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
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