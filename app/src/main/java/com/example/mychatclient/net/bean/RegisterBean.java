package com.example.mychatclient.net.bean;

/**
 * Created by wzj on 2017/9/23.
 */

public class RegisterBean {
    String phone;
    String area;
    String nick;
    String pwd;
    int sex;

    public RegisterBean(String phone, String area, String nick, String pwd, int sex) {
        this.phone = phone;
        this.area = area;
        this.nick = nick;
        this.pwd = pwd;
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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }


}
