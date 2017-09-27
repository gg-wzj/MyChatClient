package com.example.mychatclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.mychatclient.net.LongTimeConn;

/**
 * Created by wzj on 2017/9/24.
 */

public class ChatService extends Service {

    private LongTimeConn mLongTimeConn;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    class MyBinder extends Binder implements IOpenConn{
        public void openLongTimeConn(String requset, LongTimeConn.LoginLisener lisener){
            openConn(requset,lisener);
        }
    }

    private void openConn(String requset, LongTimeConn.LoginLisener lisener){
        mLongTimeConn = new LongTimeConn(requset, lisener);
        new Thread(mLongTimeConn).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mLongTimeConn!= null)
            mLongTimeConn.closeLongConn();
    }

    public interface IOpenConn{
        void openLongTimeConn(String requset, LongTimeConn.LoginLisener lisener);
    }
}
