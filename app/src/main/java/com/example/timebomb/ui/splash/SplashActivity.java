package com.example.timebomb.ui.splash;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.example.timebomb.R;
import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivitySplashBinding;
import com.example.timebomb.util.SharePrefUtils;

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

        binding.logo.setOnClickListener(v -> {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Đang thay đổi hình nền...");
            progressDialog.setCancelable(false); // Không cho phép người dùng hủy bằng cách nhấn ra ngoài
            progressDialog.show();

            // Chạy quá trình thay đổi hình nền trong một luồng nền
            new Thread(() -> {
                try {
                    // Giả lập quá trình chờ, bạn có thể bỏ qua đoạn này nếu không cần delay
                    Thread.sleep(2000);

                    // Thay đổi hình nền
                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                    wallpaperManager.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg_01));

                    // Quay lại luồng chính để ẩn ProgressDialog và hiển thị Toast
                    runOnUiThread(() -> {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Hình nền đã được thay đổi!", Toast.LENGTH_SHORT).show();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Lỗi khi thay đổi hình nền.", Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();
        });

//        new Handler().postDelayed(() -> {
//            startNextActivity(LanguageStartActivity.class, null);
//            finishAffinity();
//        }, 3000);

        binding.logo2.setOnClickListener(v -> {
            binding.imgSaber.setVisibility(View.VISIBLE);

            // Lấy kích thước ban đầu của ImageView
            binding.imgSaber.post(() -> {
                int fullHeight = binding.imgSaber.getHeight();

                // Animation cắt từ dưới lên (clip phần trên đi)
                ValueAnimator animator = ValueAnimator.ofInt(0, fullHeight);
                animator.setDuration(3000); // Thời gian 3 giây
                animator.setInterpolator(new DecelerateInterpolator());

                animator.addUpdateListener(animation -> {
                    // Lấy giá trị hiện tại của animator (phần ảnh được hiển thị)
                    int currentHeight = (int) animation.getAnimatedValue();

                    // Sử dụng clip để cắt phần hiển thị từ dưới lên
                    binding.imgSaber.setClipBounds(new Rect(0, fullHeight - currentHeight, binding.imgSaber.getWidth(), fullHeight));
                });

                animator.start();
            });
        });

        binding.logo3.setOnClickListener(v -> {
            binding.gif.setVisibility(View.VISIBLE);

            try {
                GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.gif_bomb);
                binding.gif.setImageDrawable(gifDrawable);

                // Phát ảnh GIF chỉ 1 lần
                gifDrawable.setLoopCount(1);
                gifDrawable.addAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationCompleted(int i) {
                        binding.gif.setVisibility(View.GONE);  // Ẩn ảnh sau khi phát xong

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    @Override
    public void bindView() {

    }

    @Override
    public void onBack() {

    }
}
