package com.example.mychatclient.net.bean;

/**
 * Created by wzj on 2017/9/23.
 */

public class NetBean<T> {
    String type;
    T data;

    public NetBean(String type, T data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NetBean{" +
                "type='" + type + '\'' +
                ", data=" + data +
                '}';
    }
}
