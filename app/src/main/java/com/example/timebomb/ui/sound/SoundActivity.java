package com.example.timebomb.ui.sound;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.timebomb.R;
import com.example.timebomb.base.BaseActivity;
import com.example.timebomb.databinding.ActivitySoundBinding;
import com.example.timebomb.ui.sound.adapter.SaberAdapter;
import com.example.timebomb.ui.sound.adapter.SoundAdapter;
import com.example.timebomb.ui.sound.model.Saber;
import com.example.timebomb.ui.sound.model.SoundModel;

import java.util.ArrayList;
import java.util.List;

public class SoundActivity extends BaseActivity<ActivitySoundBinding> {

    private String type;
    private List<SoundModel> bomb;
    private List<SoundModel> taser;
    private List<Saber> saber;
    private List<SoundModel> chainsaw;
    private List<SoundModel> flame;
    private List<SoundModel> screen;

    @Override
    public ActivitySoundBinding getBinding() {
        return ActivitySoundBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {

        Intent i = getIntent();
        type = i.getStringExtra("type");
        binding.rcvSound.setLayoutManager(new GridLayoutManager(this, 2));
        initData();
        if (type != null) {
            switch (type) {
                case "bomb":
                    binding.title.setText("Time Bomb");
                    binding.rcvSound.setAdapter(new SoundAdapter(bomb, this, (position, soundModel) -> {
                        Intent i1 = new Intent(this, BombActivity.class);
                        i1.putExtra("img", soundModel.getImg_play());
                        i1.putExtra("sound", soundModel.getSound());
                        startActivity(i1);
                    }));
                    break;
                case "taser":
                    binding.title.setText("Taser Gun");

                    binding.rcvSound.setAdapter(new SoundAdapter(taser, this, (position, soundModel) -> {
                        Intent i1 = new Intent(this, TaserActivity.class);
                        i1.putExtra("position", position);
                        i1.putExtra("img", soundModel.getImg_play());
                        i1.putExtra("sound", soundModel.getSound());
                        startActivity(i1);
                    }));
                    break;
                case "chainsaw":
                    binding.title.setText("Chainsaw");

                    binding.rcvSound.setAdapter(new SoundAdapter(chainsaw, this, (position, soundModel) -> {
                        Intent i1 = new Intent(this, ChainsawActivity.class);
                        i1.putExtra("img", soundModel.getImg_play());
                        i1.putExtra("sound", soundModel.getSound());
                        startActivity(i1);
                    }));
                    break;
                case "flame":
                    binding.title.setText("Flamethrower");

                    binding.rcvSound.setAdapter(new SoundAdapter(flame, this, (position, soundModel) -> {
                        Intent i1 = new Intent(this, FlameActivity.class);
                        i1.putExtra("img", soundModel.getImg_play());
                        i1.putExtra("sound", soundModel.getSound());
                        startActivity(i1);
                    }));
                    break;
                case "crack":
                    binding.title.setText("Crack Screen");

                    binding.rcvSound.setAdapter(new SoundAdapter(screen, this, (position, soundModel) -> {
                        Intent i1 = new Intent(this, CrackScreenActivity.class);
                        i1.putExtra("img", soundModel.getImg());
                        i1.putExtra("screen", soundModel.getImg_play());
                        i1.putExtra("sound", soundModel.getSound());
                        startActivity(i1);
                    }));
                    break;
                case "saber":
                    binding.title.setText("Light Saber");

                    binding.rcvSound.setAdapter(new SaberAdapter(saber, this, (position, saber) -> {
                        Intent i1 = new Intent(this, SaberActivity.class);
                        i1.putExtra("img_1", saber.getImg_play_1());
                        i1.putExtra("img_2", saber.getImg_play_2());
                        i1.putExtra("sound", saber.getSound());
                        startActivity(i1);
                    }));
                    break;
                default:
                    Toast.makeText(this, "Invalid type", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
        }

        binding.ivBack.setOnClickListener(v -> onBack());

    }

    @Override
    public void bindView() {

    }

    @Override
    public void onBack() {
        finish();
    }

    public void initData() {
        bomb = new ArrayList<>();
        bomb.add(new SoundModel(R.drawable.img_bomb_1, R.drawable.img_bomb_play_1, R.raw.bomb_1));
        bomb.add(new SoundModel(R.drawable.img_bomb_2, R.drawable.img_bomb_play_2, R.raw.bomb_2));
        bomb.add(new SoundModel(R.drawable.img_bomb_3, R.drawable.img_bomb_3, R.raw.bomb_3));
        bomb.add(new SoundModel(R.drawable.img_bomb_4, R.drawable.img_bomb_4, R.raw.bomb_4));
        bomb.add(new SoundModel(R.drawable.img_bomb_5, R.drawable.img_bomb_5, R.raw.bomb_5));
        bomb.add(new SoundModel(R.drawable.img_bomb_6, R.drawable.img_bomb_6, R.raw.bomb_6));
        bomb.add(new SoundModel(R.drawable.img_bomb_7, R.drawable.img_bomb_7, R.raw.bomb_7));
        bomb.add(new SoundModel(R.drawable.img_bomb_8, R.drawable.img_bomb_8, R.raw.bomb_8));

        taser = new ArrayList<>();
        taser.add(new SoundModel(R.drawable.img_taser_gun_1, R.drawable.img_taser_gun_play_1, R.raw.taser_1));
        taser.add(new SoundModel(R.drawable.img_taser_gun_2, R.drawable.img_taser_gun_play_2, R.raw.taser_2));
        taser.add(new SoundModel(R.drawable.img_taser_gun_3, R.drawable.img_taser_gun_play_3, R.raw.taser_3));
        taser.add(new SoundModel(R.drawable.img_taser_gun_4, R.drawable.img_taser_gun_play_4, R.raw.taser_4));
        taser.add(new SoundModel(R.drawable.img_taser_gun_5, R.drawable.img_taser_gun_5, R.raw.taser_5));
        taser.add(new SoundModel(R.drawable.img_taser_gun_6, R.drawable.img_taser_gun_6, R.raw.taser_6));
        taser.add(new SoundModel(R.drawable.img_taser_gun_7, R.drawable.img_taser_gun_7, R.raw.taser_7));
        taser.add(new SoundModel(R.drawable.img_taser_gun_8, R.drawable.img_taser_gun_8, R.raw.taser_8));

        chainsaw = new ArrayList<>();
        chainsaw.add(new SoundModel(R.drawable.img_chainsaw_1, R.drawable.img_chainsaw_play_1, R.raw.chainsaw_1));
        chainsaw.add(new SoundModel(R.drawable.img_chainsaw_2, R.drawable.img_chainsaw_play_2, R.raw.chainsaw_2));
        chainsaw.add(new SoundModel(R.drawable.img_chainsaw_3, R.drawable.img_chainsaw_play_3, R.raw.chainsaw_3));
        chainsaw.add(new SoundModel(R.drawable.img_chainsaw_4, R.drawable.img_chainsaw_play_4, R.raw.chainsaw_4));
        chainsaw.add(new SoundModel(R.drawable.img_chainsaw_5, R.drawable.img_chainsaw_play_5, R.raw.chainsaw_5));
        chainsaw.add(new SoundModel(R.drawable.img_chainsaw_6, R.drawable.img_chainsaw_play_6, R.raw.chainsaw_6));
        chainsaw.add(new SoundModel(R.drawable.img_chainsaw_7, R.drawable.img_chainsaw_play_7, R.raw.chainsaw_7));
        chainsaw.add(new SoundModel(R.drawable.img_chainsaw_8, R.drawable.img_chainsaw_play_8, R.raw.chainsaw_8));

        flame = new ArrayList<>();
        flame.add(new SoundModel(R.drawable.img_flame_1, R.drawable.img_flame_thrower_play_1, R.raw.flamethrower_1));
        flame.add(new SoundModel(R.drawable.img_flame_2, R.drawable.img_flame_thrower_play_2, R.raw.flamethrower_2));
        flame.add(new SoundModel(R.drawable.img_flame_3, R.drawable.img_flame_thrower_play_3, R.raw.flamethrower_3));
        flame.add(new SoundModel(R.drawable.img_flame_4, R.drawable.img_flame_thrower_play_4, R.raw.flamethrower_4));
        flame.add(new SoundModel(R.drawable.img_flame_5, R.drawable.img_flame_thrower_play_5, R.raw.flamethrower_5));
        flame.add(new SoundModel(R.drawable.img_flame_6, R.drawable.img_flame_thrower_play_6, R.raw.flamethrower_6));
        flame.add(new SoundModel(R.drawable.img_flame_7, R.drawable.img_flame_thrower_play_8, R.raw.flamethrower_7));
        flame.add(new SoundModel(R.drawable.img_flame_8, R.drawable.img_flame_thrower_play_7, R.raw.flamethrower_8));

        screen = new ArrayList<>();
        screen.add(new SoundModel(R.drawable.img_phone_1, R.drawable.img_screen_1, R.raw.crack_screen));
        screen.add(new SoundModel(R.drawable.img_phone_2, R.drawable.img_screen_2, R.raw.crack_screen));
        screen.add(new SoundModel(R.drawable.img_phone_3, R.drawable.img_screen_3, R.raw.crack_screen));
        screen.add(new SoundModel(R.drawable.img_phone_4, R.drawable.img_screen_4, R.raw.crack_screen));
        screen.add(new SoundModel(R.drawable.img_phone_5, R.drawable.img_screen_5, R.raw.crack_screen));
        screen.add(new SoundModel(R.drawable.img_phone_6, R.drawable.img_screen_6, R.raw.crack_screen));
        screen.add(new SoundModel(R.drawable.img_phone_7, R.drawable.img_screen_7, R.raw.crack_screen));
        screen.add(new SoundModel(R.drawable.img_phone_8, R.drawable.img_screen_8, R.raw.crack_screen));

        saber = new ArrayList<>();
        saber.add(new Saber(R.drawable.img_saber_1, R.drawable.img_light_saber_1_1, R.drawable.img_light_saber_1_2, R.raw.lightsaber_1));
        saber.add(new Saber(R.drawable.img_saber_2, R.drawable.img_light_saber_2_1, R.drawable.img_light_saber_2_2, R.raw.lightsaber_1));
        saber.add(new Saber(R.drawable.img_saber_3, R.drawable.img_light_saber_3_1, R.drawable.img_light_saber_3_2, R.raw.lightsaber_1));
        saber.add(new Saber(R.drawable.img_saber_4, R.drawable.img_light_saber_4_1, R.drawable.img_light_saber_4_2, R.raw.lightsaber_1));
        saber.add(new Saber(R.drawable.img_saber_5, R.drawable.img_light_saber_5_1, R.drawable.img_light_saber_5_2, R.raw.lightsaber_1));
        saber.add(new Saber(R.drawable.img_saber_6, R.drawable.img_light_saber_6_1, R.drawable.img_light_saber_6_2, R.raw.lightsaber_1));
        saber.add(new Saber(R.drawable.img_saber_7, R.drawable.img_light_saber_7_1, R.drawable.img_light_saber_7_2, R.raw.lightsaber_1));
        saber.add(new Saber(R.drawable.img_saber_8, R.drawable.img_light_saber_8_1, R.drawable.img_light_saber_8_2, R.raw.lightsaber_1));

    }
}