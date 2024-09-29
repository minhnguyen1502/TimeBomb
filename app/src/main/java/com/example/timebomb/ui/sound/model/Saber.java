package com.example.timebomb.ui.sound.model;

public class Saber {
    private int img;
    private int img_play_1;
    private int img_play_2;
    private int sound;

    public Saber(int img, int img_play_1, int img_play_2, int sound) {
        this.img = img;
        this.img_play_1 = img_play_1;
        this.img_play_2 = img_play_2;
        this.sound = sound;
    }

    public int getImg_play_2() {
        return img_play_2;
    }

    public void setImg_play_2(int img_play_2) {
        this.img_play_2 = img_play_2;
    }

    public int getImg_play_1() {
        return img_play_1;
    }

    public void setImg_play_1(int img_play_1) {
        this.img_play_1 = img_play_1;
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
