package com.example.mychatclient.net;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.mychatclient.R;
import com.example.mychatclient.TypeConstant;
import com.example.mychatclient.app.MyApplication;
import com.example.mychatclient.db.bean.ChatRecordBean;
import com.example.mychatclient.db.bean.FriendshipBean;
import com.example.mychatclient.db.bean.RecentMessageBean;
import com.example.mychatclient.db.dao.ChatRecordDao;
import com.example.mychatclient.db.dao.FriendshipDao;
import com.example.mychatclient.db.dao.RecentMessageDao;
import com.example.mychatclient.net.bean.NetBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.Socket;

/**
 * Created by wzj on 2017/9/23.
 */

public class LongTimeConn implements Runnable {

    private String requset;
    private LoginLisener mListener;
    private Socket socket;
    private String friendPhone;

    public LongTimeConn(String requset, LoginLisener loginLisener) {
        this.requset = requset;
        mListener = loginLisener;
    }


    @Override
    public void run() {
        try {
            socket = new Socket(MyApplication.getHost(), 30000);

            //开启长连接
            OutputStreamWriter ops = new OutputStreamWriter(socket.getOutputStream());
            BufferedWriter writer = new BufferedWriter(ops);

            writer.write(requset);
            writer.newLine();
            writer.flush();
            //保持监听
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            String data;
            while ((data = reader.readLine()) != null) {
                Log.e("wzj",data);
                try {
                    JSONObject object = new JSONObject(data);
                    String type = object.getString("type");
                    if (TypeConstant.RESPOND_LOGIN.equals(type)) {
                        //来自登录的回应
                        if (mListener != null)
                            mListener.onLoginRespond(data);
                    } else if(TypeConstant.RECEIVE_MESSAGE.equals(type)){
                        //收到信息
                        //保存收到的信息
                        saveMessageRecord(data);
                        //发送广播
                        sendMessageBroadCast();
                    }else if(TypeConstant.RECEIVE_VERIFICATION.equals(type)){
                        //接收到好友申请
                        saveRecord(data);
                        sendVerificationBroadCast();
                    }else if(TypeConstant.OK_VERIFICATION.equals(type)){
                        //发出的好友申请被接受
                        //保存到数据库
                        saveFriend(data);
                        //发送广播
                        sendOkBroadCast();
                    }else if(TypeConstant.REFUSE_VERIFICATION.equals(type)){
                        //发出的好友申请被拒绝
                        //发出通知
                        sendRefuseNotification(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void saveMessageRecord(String data) {
        ChatRecordBean chatRecordBean = saveRecord(data);

        //更新recentMessage表
        RecentMessageDao r_dao = new RecentMessageDao("wzj");
        RecentMessageBean recentMessageBean = r_dao.findWithPhone(chatRecordBean.getPhone());
        int wdCount = 0;
        if(recentMessageBean!= null){
            //最近联系人中有该用户 更新wdCount
            wdCount= recentMessageBean.getWdCount();
        }
        wdCount++;

        friendPhone = chatRecordBean.getPhone();
        long time = chatRecordBean.getTime();
        String message = chatRecordBean.getMessage();
        RecentMessageBean newBean = new RecentMessageBean(friendPhone,"郑琪琪",message,wdCount,time);
        r_dao.addMessage(newBean);
    }



    private ChatRecordBean saveRecord(String data) {
        //解析json
        Gson gson = new Gson();
        Type type = new TypeToken<NetBean<ChatRecordBean>>() {
        }.getType();
        NetBean<ChatRecordBean> netBean = gson.fromJson(data, type);
        ChatRecordBean chatRecordBean = netBean.getData();
        //存储到ChatRecord表中
        ChatRecordDao dao = new ChatRecordDao("wzj");
        if(chatRecordBean.getType() == 2)
            dao.addVertificationRecord(chatRecordBean);
        else dao.addChatRecord(chatRecordBean);
        return chatRecordBean;
    }

    private void sendMessageBroadCast() {
        Intent intent = new Intent(MyApplication.MESSAGE_BROADCAST);
        intent.putExtra("phone",friendPhone);
        MyApplication.getContext().sendBroadcast(intent);
    }

    private void sendVerificationBroadCast() {
        Intent intent = new Intent(MyApplication.VERIFICATION_BROADCAST);
        intent.putExtra("phone",friendPhone);
        MyApplication.getContext().sendBroadcast(intent);
    }

    private void saveFriend(String data) {
        Gson gson = new Gson();
        Type type = new TypeToken<NetBean<FriendshipBean>>() {
        }.getType();
        NetBean<FriendshipBean> netBean = gson.fromJson(data, type);
        FriendshipBean friendshipBean = netBean.getData();

        FriendshipDao dao = new FriendshipDao("wzj");
        dao.addFriendship(friendshipBean);
    }

    private void sendOkBroadCast() {
        Intent intent = new Intent(MyApplication.OK_BROADCAST);
        MyApplication.getContext().sendBroadcast(intent);
    }


    private void sendRefuseNotification(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String name = jsonObject.optString("data");
            Intent intent = new Intent(MyApplication.REFUSE_BROADCAST);
            Context context = MyApplication.getContext();
            PendingIntent pi = PendingIntent.getBroadcast(context,0,intent,0);
            NotificationManager manager  = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle(name + "拒绝了您的好友请求")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setContentIntent(pi)
                    .build();
            manager.notify(1,notification);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public interface LoginLisener {
        void onLoginRespond(String respond);
    }

    public void closeLongConn() {
        if (socket != null)
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
