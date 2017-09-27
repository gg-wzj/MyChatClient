package com.example.mychatclient.db.bean;

/**
 * Created by wzj on 2017/9/22.
 */

public class ChatRecordBean {

    String phone;
    int  type;
    String message;
    int hasSend;

    public ChatRecordBean(String phone, int type, String message, int hasSend, long time) {
        this.phone = phone;
        this.type = type;
        this.message = message;
        this.hasSend = hasSend;
        this.time = time;
    }

    public int getHasSend() {
        return hasSend;
    }

    public void setHasSend(int hasSend) {
        this.hasSend = hasSend;
    }

    long time;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ChatRecordBean{" +
                "phone='" + phone + '\'' +
                ", type=" + type +
                ", message='" + message + '\'' +
                ", time=" + time +
                '}';
    }
}
