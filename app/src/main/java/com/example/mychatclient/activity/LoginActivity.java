package com.example.mychatclient.activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wzj on 2017/9/21.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText etPhone;
    private EditText etPwd;
    private Button btnLogin;

    private Handler mHnadler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(LoginActivity.this,"账号或密码错误，请重新输入",Toast.LENGTH_SHORT).show();
            etPwd.setText("");
        }
    };
    private String phone;
    private String pwd;
    private ProgressDialog progressDialog;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_login);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etPwd = (EditText) findViewById(R.id.etPwd);
        btnLogin = (Button) findViewById(R.id.btnLogin);
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

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在登陆");
        progressDialog.setCancelable(false);
        progressDialog.show();

        LoginBean bean = new LoginBean(phone, pwd);
        NetBean<LoginBean> netBean = new NetBean<>(TypeConstant.REQUEST_LOGIN,bean);
        Gson gson = new Gson();
        final String json = gson.toJson(netBean, NetBean.class);

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
                    SPUtil.putString("user",phone);
                    progressDialog.dismiss();
                    finish();
                }else {
                    mHnadler.sendEmptyMessage(0);
                    progressDialog.dismiss();
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
