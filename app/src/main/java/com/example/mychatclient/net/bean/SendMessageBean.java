package com.example.mychatclient.net.bean;

/**
 * Created by wzj on 2017/9/25.
 */

public class SendMessageBean {

    String fromUser ;
    String toUser;
    int type;
    String message;
    long date;

    public SendMessageBean(String fromUser, String toUser, int type, String message, long date) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.type = type;
        this.message = message;
        this.date = date;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
