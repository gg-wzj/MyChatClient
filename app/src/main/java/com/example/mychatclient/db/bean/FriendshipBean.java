package com.example.mychatclient.db.bean;

/**
 * Created by wzj on 2017/9/22.
 */

public class FriendshipBean {

    String phone;
    String nick;
    String area;
    String remark;
    int sex;

    public FriendshipBean(String phone, String nick, String area, String remark, int sex) {
        this.phone = phone;
        this.nick = nick;
        this.area = area;
        this.remark = remark;
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "FriendshipBean{" +
                "phone='" + phone + '\'' +
                ", nick='" + nick + '\'' +
                ", area='" + area + '\'' +
                ", remark='" + remark + '\'' +
                ", sex=" + sex +
                '}';
    }
}
