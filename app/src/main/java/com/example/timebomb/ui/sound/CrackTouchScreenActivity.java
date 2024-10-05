package com.example.timebomb.ui.sound;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.timebomb.R;
import com.example.timebomb.ui.sound.service.CrackScreen;
import com.example.timebomb.util.SystemUtil;

public class CrackTouchScreenActivity extends Activity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideFullNavigation();
        setContentView(R.layout.activity_crack_touch);
        SystemUtil.setLocale(this);

        findViewById(R.id.wwcontainer).setOnTouchListener((view, motionEvent) -> {
            Intent intent1;
            intent1 = new Intent(CrackTouchScreenActivity.this, CrackScreen.class);
            intent1.putExtra("image_resource", getIntent().getIntExtra("image_resource", R.drawable.img_screen_1));
            startService(intent1);
            finish();
            return true;
        });
    }

    public void hideFullNavigation() {
        try {
            int flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            getWindow().getDecorView().setSystemUiVisibility(flags);

            View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(flags);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
