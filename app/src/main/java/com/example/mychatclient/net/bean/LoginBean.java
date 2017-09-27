package com.example.mychatclient.net.bean;

/**
 * Created by wzj on 2017/9/24.
 */

public class LoginBean {
    String phone;
    String pwd;

    public LoginBean(String phone, String pwd) {
        this.phone = phone;
        this.pwd = pwd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
