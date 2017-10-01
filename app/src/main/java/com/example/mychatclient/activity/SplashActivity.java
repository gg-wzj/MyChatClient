package com.example.mychatclient.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.mychatclient.R;
import com.example.mychatclient.util.SPUtil;

/**
 * Created by wzj on 2017/9/20.
 */

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegister;
    private Button btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initListener();

        String user = SPUtil.getString("user");
        String pwd = SPUtil.getString("pwd");
        if(!TextUtils.isEmpty(user) && !TextUtils.isEmpty(pwd)){
            //账号 密码 均不为空 直接登录
            Intent intent = new Intent(this,LoginActivity.class);
            intent.putExtra(LoginActivity.USER,user);
            intent.putExtra(LoginActivity.MD5,pwd);
            startActivity(intent);
            finish();
        }
    }

    private void initView() {
        //设置状态栏
        if(Build.VERSION.SDK_INT >21 ){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_splash);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);
    }

    private void initListener() {
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.btnRegister:
                intent.setClass(this,RegisterActivity.class);
                break;
            case R.id.btnLogin:
                intent.setClass(this,LoginActivity.class);
                break;
        }
        startActivity(intent);
        finish();
    }
}
