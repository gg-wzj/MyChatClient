package com.example.mychatclient.net;

import android.util.Log;

import com.example.mychatclient.app.MyApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by wzj on 2017/9/20.
 */

public class OneTimeConn implements Runnable {

    private String request;
    private OnRespondListener mListener;
    //默认10秒超时
    private int timeOut = 1000;

   public OneTimeConn(String requset ,OnRespondListener listener){
        this.request = requset;
        mListener = listener;

    }

    //为连接设置超时
    public void setTimeOut(int timeOut){
        this.timeOut = timeOut;
    }

    @Override
    public void run() {
        try {
            Socket socket   = new Socket(MyApplication.getHost(),30000);
            socket.setSoTimeout(timeOut);

            //把请求的数据发送给服务器
            OutputStreamWriter ops = new OutputStreamWriter(socket.getOutputStream());
            BufferedWriter writer = new BufferedWriter(ops);
            writer.write(request);
            writer.newLine();
            writer.flush();

            //读取服务器的响应，并关闭此次连接
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            String respond = reader.readLine();


            reader.close();
            writer.close();
            socket.close();

            //成功获取响应，回调
            if(mListener!= null){
                mListener.onSuccess(respond);
            }
        } catch (IOException e) {
            //请求失败
            e.printStackTrace();
            if(mListener!=null){
                mListener.onFailure(e);
            }
        }
    }

    public interface OnRespondListener{
        void onSuccess(String respond);
        void onFailure(Exception e);
    }
}
