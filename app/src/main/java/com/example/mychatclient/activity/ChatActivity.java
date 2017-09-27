package com.example.mychatclient.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mychatclient.R;
import com.example.mychatclient.TypeConstant;
import com.example.mychatclient.adapter.ChatAdapter;
import com.example.mychatclient.adapter.MyLinearlayoutManager;
import com.example.mychatclient.app.MyApplication;
import com.example.mychatclient.db.bean.ChatRecordBean;
import com.example.mychatclient.db.bean.RecentMessageBean;
import com.example.mychatclient.db.dao.ChatRecordDao;
import com.example.mychatclient.db.dao.RecentMessageDao;
import com.example.mychatclient.net.OneTimeConn;
import com.example.mychatclient.net.bean.NetBean;
import com.example.mychatclient.net.bean.SendMessageBean;
import com.example.mychatclient.util.SPUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wzj on 2017/9/21.
 */

public class ChatActivity extends BaseActivity implements View.OnClickListener {

    public static final String REMARK = "remark";
    public static final String FRIEND_PHONE = "friendPhone";

    private AppBarLayout mAppBAR;
    private ImageView iv_toolbar_session;
    private TextView tvToolbarTitle;
    private String friendPhone;
    private ChatRecordDao dao = new ChatRecordDao("wzj");

    private List<ChatRecordBean> mList = new ArrayList<>();
    private RecyclerView rv_chat;
    private ChatAdapter adapter;


    private static final int SETADAPTER = 1;
    private static final int NOTIFYADAPTER = 2;
    private Handler mHandler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SETADAPTER:
                    MyLinearlayoutManager linearLayoutManager = new MyLinearlayoutManager(ChatActivity.this);
                    rv_chat.setLayoutManager(linearLayoutManager);
                    adapter = new ChatAdapter(mList);
                    rv_chat.setAdapter(adapter);
                    rv_chat.scrollToPosition(mList.size()-1);
                    break;
                case NOTIFYADAPTER:
                    adapter.notifyDataSetChanged();
                    rv_chat.scrollToPosition(mList.size()-1);
            }

        }
    };
    private ImageView iv_chat_add;
    private Button btn_chat_send;
    private EditText et_chat;
    private String remark;
    private ChatReceiver receiver;

    @Override
    protected void init() {
        super.init();
        receiver = new ChatReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyApplication.MESSAGE_BROADCAST);
        registerReceiver(receiver,filter);
    }

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_chat);

        //初始化标题栏
        mAppBAR = (AppBarLayout) findViewById(R.id.appBar);
        iv_toolbar_session = mAppBAR.findViewById(R.id.iv_toolbar_session);
        iv_toolbar_session.setVisibility(View.VISIBLE);
        tvToolbarTitle = mAppBAR.findViewById(R.id.tvToolbarTitle);
        //初始化recyclerView
        rv_chat = (RecyclerView) findViewById(R.id.rv_chat);

        //初始化信息编辑栏
        et_chat = (EditText) findViewById(R.id.et_chat);
        iv_chat_add = (ImageView) findViewById(R.id.iv_chat_add);
        btn_chat_send = (Button) findViewById(R.id.btn_chat_send);
    }


    @Override
    protected void initDats() {

        //设置标题栏(对朋友的备注)
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            remark = extras.getString(REMARK);
            tvToolbarTitle.setText(remark);
            friendPhone = extras.getString(FRIEND_PHONE);
        }

        new Thread(){
            @Override
            public void run() {
                super.run();
                List<ChatRecordBean> list = dao.findAll(friendPhone);
                mList.addAll(list);
                Message message = mHandler.obtainMessage();
                message.what = SETADAPTER;
                mHandler.sendMessage(message);
            }
        }.start();


    }


    @Override
    protected void initListener() {
        et_chat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>0){
                    iv_chat_add.setVisibility(View.GONE);
                    btn_chat_send.setVisibility(View.VISIBLE);
                }else {
                    iv_chat_add.setVisibility(View.VISIBLE);
                    btn_chat_send.setVisibility(View.GONE);
                }
            }
        });

        btn_chat_send.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //dao.deleteAll();
        unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_chat_send:
                sendMessage();
                break;
        }
    }

    private void sendMessage() {
        final String message = et_chat.getText().toString();
        et_chat.setText("");
        final ChatRecordBean oldBean = new ChatRecordBean(friendPhone,1,message,0,System.currentTimeMillis());
        //向数据库添加一条 hasSend为0 的数据 更新ui
        new Thread(){
            @Override
            public void run() {
                super.run();
                //添加到 chatRecord中
                dao.addChatRecord(oldBean);
                //更新recentMessage表
                RecentMessageDao r_dao = new RecentMessageDao("wzj");
                RecentMessageBean recentMessageBean = new RecentMessageBean(friendPhone,remark,oldBean.getMessage(),0,System.currentTimeMillis());
                r_dao.addMessage(recentMessageBean);
                updateDatas();

                sendToServer(message, oldBean);
            }
        }.start();



    }

    private void sendToServer(String message, final ChatRecordBean oldBean) {
        String user = SPUtil.getString("user");
        SendMessageBean sendMessageBean = new SendMessageBean(user, friendPhone, 0, message, System.currentTimeMillis());
        NetBean<SendMessageBean> netBean = new NetBean<>(TypeConstant.REQUEST_SENDMESSAGE,sendMessageBean);
        final String json = new Gson().toJson(netBean, NetBean.class);
        new Thread(new OneTimeConn(json, new OneTimeConn.OnRespondListener() {
            @Override
            public void onSuccess(String respond) {
                try {
                    JSONObject jsonObject = new JSONObject(respond);
                    String data = jsonObject.optString("data");
                    if("ok".equals(data))//成功发送 取消正在发送的progressBar
                        canelSendding(oldBean);
                    else showFailTip();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Exception e) {

            }
        })).start();
    }

    private void updateDatas() {
        mList.clear();
        mList.addAll(dao.findAll(friendPhone));
        Message handleMessage = mHandler.obtainMessage();
        handleMessage.what = NOTIFYADAPTER;
        mHandler.sendMessage(handleMessage);
    }

    private void canelSendding(ChatRecordBean oldBean) {
        //更新数据库的数据 重新查找 更新ui
        dao.updateSendStatus(oldBean);
        updateDatas();
    }

    private void showFailTip() {

    }

    class ChatReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(MyApplication.MESSAGE_BROADCAST.equals(intent.getAction())){
                Bundle extras = intent.getExtras();
                String phone = extras.getString("phone");
                if(friendPhone.equals(phone)){
                    //说明是正在聊天的朋友发的消息 更新ui
                    updateDatas();
                }
            }
        }
    }
}
