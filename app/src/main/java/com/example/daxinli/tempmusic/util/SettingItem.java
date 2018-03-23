package com.example.daxinli.tempmusic.util;

/**
 * Created by Daxin Li on 2018/3/8.
 */

public class SettingItem {
    private int imageId;
    private String text;
    private String desrip;
    private int type;

    public SettingItem(int imageId, String text, String descip, int type) {
        this.imageId = imageId;
        this.text = text;
        this.type = type;
        this.desrip = descip;
    }
    public int getImageId() { return this.imageId; }
    public String getText() { return this.text; }
    public int getType() { return this.type; }
    public String getDesrip() { return this.desrip; }
}
