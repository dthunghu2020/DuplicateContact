package com.hungdt.test.model;

public class GameItem {
    private int id;
    private String name;
    private String pathImage;
    private String stateInstall;
    private String packageName;
    private boolean isLive;

    public GameItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public String getStateInstall() {
        return stateInstall;
    }

    public void setStateInstall(String stateInstall) {
        this.stateInstall = stateInstall;
    }
}
