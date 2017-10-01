package com.example.mychatclient.net.bean;

import java.io.Serializable;

/**
 * Created by wzj on 2017/9/26.
 */

public class SearchBean implements Serializable{

    String phone;
    String area;
    String nick;
    int sex;

    public SearchBean(String phone, String area, String nick, int sex) {
        this.phone = phone;
        this.area = area;
        this.nick = nick;
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
