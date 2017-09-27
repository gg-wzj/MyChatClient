package com.example.mychatclient.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by wzj on 2017/9/20.
 */

public class MyApplication extends Application {

    public static String mHost = "192.168.155.2";
    public static final String MESSAGE_BROADCAST = "com.example.mychatclient.MY_MESSAGE_BROADCAST";
    public static final String VERIFICATION_BROADCAST = "com.example.mychatclient.MY_VERIFICATION_BROADCAST";
    public static final String OK_BROADCAST= "com.example.mychatclient.OK_BROADCAST";
    public static final String REFUSE_BROADCAST= "com.example.mychatclient.REFUSE_BROADCAST";
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }

    public static String getHost() {
        return mHost;
    }

    public static void setHost(String host) {
        mHost = host;
    }
}
