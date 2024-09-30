package com.example.timebomb.ui.main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.timebomb.R;
import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivityMainBinding;
import com.example.timebomb.databinding.DialogPermissionBinding;
import com.example.timebomb.ui.main.adapter.ItemAdapter;
import com.example.timebomb.ui.main.model.ItemModel;
import com.example.timebomb.ui.setting.SettingActivity;
import com.example.timebomb.ui.sound.SoundActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private List<ItemModel> listItems;
    private ItemAdapter adapter;

    @Override
    public ActivityMainBinding getBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        binding.recycleView.setLayoutManager(new GridLayoutManager(this, 2));

        listItems = new ArrayList<>();
        listItems.add(new ItemModel(R.string.time_bomb, R.drawable.img_time_bomb));
        listItems.add(new ItemModel(R.string.taser_gun, R.drawable.img_taser));
        listItems.add(new ItemModel(R.string.light_saber, R.drawable.img_light_saber));
        listItems.add(new ItemModel(R.string.chainsaw, R.drawable.img_chainsaw));
        listItems.add(new ItemModel(R.string.flame_thrower, R.drawable.img_flame_thrower));
        listItems.add(new ItemModel(R.string.crack_screen, R.drawable.img_crack_screen));

        adapter = new ItemAdapter(listItems, position -> {
            switch (position){
                case 0:
                    clickItem("bomb");
                    break;
                case 1:
                    clickItem("taser");
                    break;
                case 2:
                    clickItem("saber");
                    break;
                case 3:
                    clickItem("chainsaw");
                    break;
                case 4:
                    clickItem("flame");
                    break;
                case 5:
                    if (checkOverlayPermission()) {
                        clickItem("crack");

                    } else {
                        showDialogGotoSetting();
                    }
                    break;
            }
        },this);

        binding.recycleView.setAdapter(adapter);

    }

    private void clickItem(String type) {
        Intent i = new Intent(this, SoundActivity.class);
        i.putExtra("type",type);
        startActivity(i);
    }
    public boolean checkOverlayPermission() {
        return Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(this);
    }

    private void showDialogGotoSetting() {

        Dialog dialog = new Dialog(this);
        DialogPermissionBinding bindingPer = DialogPermissionBinding.inflate(getLayoutInflater());
        dialog.setContentView(bindingPer.getRoot());
        if (dialog.getWindow() != null) {
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        bindingPer.tvStay.setOnClickListener(v -> {
            dialog.dismiss();
        });
        bindingPer.tvAgree.setOnClickListener(v -> {
            Intent intent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            }
            louncherOverlay.launch(intent);
            dialog.dismiss();
        });

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private final ActivityResultLauncher<Intent> louncherOverlay = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                } else {
                }
            } else {
            }
        }
    });

    @Override
    public void bindView() {
        binding.ivSetting.setOnClickListener(v -> startActivity(new Intent(this, SettingActivity.class)));

    }

    @Override
    public void onBack() {
        if (!isShow) {
            confirmQuitApp();
        }
    }
    private boolean isShow = false;

    private void confirmQuitApp() {
        isShow = true;
        Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.dialog_exit_app);

        Objects.requireNonNull(dialog.getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RelativeLayout cancel = dialog.findViewById(R.id.btn_cancel_quit_app);
        RelativeLayout quit = dialog.findViewById(R.id.btn_quit_app);
        quit.setOnClickListener(v -> {
            finishAffinity();
            dialog.dismiss();
            isShow = false;
        });

        cancel.setOnClickListener(v -> {
            dialog.dismiss();
            isShow = false;
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


}
