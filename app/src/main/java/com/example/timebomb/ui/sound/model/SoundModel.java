package com.example.timebomb.ui.sound.model;

public class SoundModel {
    private int img;
    private int img_play;
    private int sound;

    public SoundModel(int img, int img_play, int sound) {
        this.img = img;
        this.img_play = img_play;
        this.sound = sound;
    }

    public int getImg_play() {
        return img_play;
    }

    public void setImg_play(int img_play) {
        this.img_play = img_play;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }
}
