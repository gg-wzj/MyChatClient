package com.example.mychatclient.db.bean;

/**
 * Created by wzj on 2017/9/22.
 */

public class RecentMessageBean {

    String friendPhone;
    String remark;


    String message;
    int wdCount;

    public int getWdCount() {
        return wdCount;
    }

    public void setWdCount(int wdCount) {
        this.wdCount = wdCount;
    }

    public RecentMessageBean(String friendPhone, String remark, String message, int wdCount, long time) {

        this.friendPhone = friendPhone;
        this.remark = remark;
        this.message = message;
        this.wdCount = wdCount;
        this.time = time;
    }

    long time;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public RecentMessageBean() {

    }


    public String getFriendPhone() {
        return friendPhone;
    }

    public void setFriendPhone(String friendPhone) {
        this.friendPhone = friendPhone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long tiem) {
        this.time = tiem;
    }

    @Override
    public String toString() {
        return "RecentMessageBean{" +
                "friendPhone='" + friendPhone + '\'' +
                ", remark='" + remark + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time +
                '}';
    }
}
