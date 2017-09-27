package com.example.mychatclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mychatclient.R;
import com.example.mychatclient.TypeConstant;
import com.example.mychatclient.net.OneTimeConn;
import com.example.mychatclient.net.bean.NetBean;
import com.example.mychatclient.net.bean.SendMessageBean;
import com.example.mychatclient.util.SPUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wzj on 2017/9/26.
 */

public class VerificationActivity extends BaseActivity implements View.OnClickListener {

    public static final String TOUSER = "toUser";
    private String toUser;

    private TextView tvToolbarTitle;
    private TextView et_verification_message;
    private TextView et_verification_remark;

    private Button btn_verification;

    @Override
    protected void init() {
        super.init();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null){
            toUser = extras.getString(TOUSER);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_verification);
        tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("验证申请");

        et_verification_message = (TextView) findViewById(R.id.et_verification_message);
        et_verification_remark = (TextView) findViewById(R.id.et_verification_remark);

        btn_verification = (Button) findViewById(R.id.btn_verification);
    }

    @Override
    protected void initListener() {
        super.initListener();
        btn_verification.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String json = getJson();
        requsetVerification(json);
    }



    private String getJson() {
        String remark = et_verification_remark.getText().toString();
        String message = et_verification_message.getText().toString();

        String user = SPUtil.getString("user");
        //  用/来分割message和remark,方便在服务端区分 添加到两个不同的表中
        String sendMessage = message + "/" + remark;
        SendMessageBean bean = new SendMessageBean(user,toUser,1,sendMessage,System.currentTimeMillis());
        NetBean<SendMessageBean> netBean = new NetBean<>(TypeConstant.REQUEST_VERIFICATION,bean);
        return new Gson().toJson(netBean, NetBean.class);
    }

    private void requsetVerification( String json) {
        new Thread(new OneTimeConn(json, new OneTimeConn.OnRespondListener() {
            @Override
            public void onSuccess(String respond) {
                try {
                    JSONObject jsonObject = new JSONObject(respond);
                    String data = jsonObject.optString("data");
                    if("ok".equals(data))
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(VerificationActivity.this,"好友申请以发送",Toast.LENGTH_SHORT).show();
                            }
                        });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        })).start();
    }
}
