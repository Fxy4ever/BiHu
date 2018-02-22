package com.example.mac.bihu;

import android.app.Application;

/**
 * Created by mac on 2018/2/11.
 */

public class mUser extends Application{
    private String username;
    private String avatar;
    private String token;
    private int totalItem;

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public int getTotalItem() {

        return totalItem;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getToken() {
        return token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
