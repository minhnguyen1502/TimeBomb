package com.example.timebomb.ui.main.model;

public class ItemModel {
    private int name;
    private int img;

    public ItemModel(int name, int img) {
        this.name = name;
        this.img = img;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
