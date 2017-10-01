package com.example.mychatclient.activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mychatclient.R;
import com.example.mychatclient.TypeConstant;
import com.example.mychatclient.net.LongTimeConn;
import com.example.mychatclient.net.OneTimeConn;
import com.example.mychatclient.net.bean.LoginBean;
import com.example.mychatclient.net.bean.NetBean;
import com.example.mychatclient.service.ChatService;
import com.example.mychatclient.util.SPUtil;
import com.example.mychatclient.util.StringUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wzj on 2017/9/21.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    public static final String USER = "user";
    public static final String PWD = "pwd";
    public static final String MD5 = "md5";


    private EditText etPhone;
    private EditText etPwd;
    private Button btnLogin;

    private Handler mHnadler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDialog.dismiss();
            switch (msg.what){
                case 0:
                    Toast.makeText(LoginActivity.this,"账号或密码错误，请重新输入",Toast.LENGTH_SHORT).show();
                    etPwd.setText("");
                    break;
                case 1:
                    break;
            }

        }
    };
    private String phone;
    private String pwd;
    private ProgressDialog progressDialog;
    private String md5;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_login);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etPwd = (EditText) findViewById(R.id.etPwd);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null){
            phone = extras.getString(USER);
            pwd = extras.getString(PWD);
            md5 = extras.getString(MD5);
            etPhone.setText(phone);
            etPwd.setText(pwd);
        }

        if(!TextUtils.isEmpty(md5)){
            //说明是splash界面传过来的 直接登录
            login();
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        phone = etPhone.getText().toString();
        pwd = etPwd.getText().toString();
        if(TextUtils.isEmpty(phone) | TextUtils.isEmpty(pwd)){
            Toast.makeText(this,"账号或密码为空",Toast.LENGTH_SHORT).show();
            return;
        }
        md5 = StringUtil.getMd5(pwd);

        login();
    }

    public  void login() {
        //显示progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在登陆");
        progressDialog.setCancelable(false);
        progressDialog.show();

        SPUtil.putString("user",phone);
        LoginBean bean = new LoginBean(phone, md5);

        //获取登陆请求的json数据
        NetBean<LoginBean> netBean = new NetBean<>(TypeConstant.REQUEST_LOGIN,bean);
        Gson gson = new Gson();
        final String json = gson.toJson(netBean, NetBean.class);

        //开启服务，通过服务开启长连接
        Intent intent = new Intent(this, ChatService.class);
        startService(intent);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ChatService.IOpenConn iOpenConn = (ChatService.IOpenConn) service;
                iOpenConn.openLongTimeConn(json,lisener);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        },BIND_AUTO_CREATE);
    }

    private LongTimeConn.LoginLisener lisener = new LongTimeConn.LoginLisener() {
        @Override
        public void onLoginRespond(String respond) {
            try {
                JSONObject jsonObject = new JSONObject(respond);
                String data = jsonObject.optString("data");
                if("ok".equals(data)){
                    //登录成功
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    SPUtil.putString("pwd",md5);
                    finish();
                }else {
                    mHnadler.sendEmptyMessage(0);

                    //登陆失败 重置user缓存
                    SPUtil.putString("user","");
                    //关闭服务
                    Intent intent = new Intent(LoginActivity.this,ChatService.class);
                    stopService(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
