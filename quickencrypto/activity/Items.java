package com.cev.albin.quickencrypto.activity;

public class Items {
    private String title, date, size, path;
    int position;

    public Items() {
    }

    public Items(String title, String date, String size, String path) {
        this.title = title;
        this.date = date;
        this.size = size;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
