package com.example.mychatclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mychatclient.R;
import com.example.mychatclient.TypeConstant;
import com.example.mychatclient.net.OneTimeConn;
import com.example.mychatclient.net.bean.NetBean;
import com.example.mychatclient.net.bean.SearchBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by wzj on 2017/9/25.
 */

public class DetailActivity extends BaseActivity implements View.OnClickListener {

    public static final String USER = "user";
    private String user;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SearchBean bean = (SearchBean) msg.obj;
            tv_detail_name.setText(bean.getNick());
            iv_detail_sex.setImageResource(bean.getSex() == 0 ? R.mipmap.ic_gender_male:R.mipmap.ic_gender_female);

        }
    };
    private TextView tv_detail_name;
    private ImageView iv_detail_header;
    private Button btn_detil;
    private ImageView iv_detail_sex;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!= null){
            user = extras.getString(USER);
        }

        tv_detail_name = (TextView) findViewById(R.id.tv_detail_name);
        iv_detail_header = (ImageView) findViewById(R.id.iv_detail_header);
        btn_detil = (Button) findViewById(R.id.btn_detail);
        iv_detail_sex = (ImageView) findViewById(R.id.iv_detail_sex);


        String json = getSearchJson(TypeConstant.REQUEST_SEARCH);

        requestSearchData(json);
    }

    private String getSearchJson(String type) {
        JSONObject jsonObject = new JSONObject();
        String json = "";
        try {
            jsonObject.put("type", type);
            jsonObject.put("data",user);
            json = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private void requestSearchData(String json) {
        new Thread(new OneTimeConn(json, new OneTimeConn.OnRespondListener() {
            @Override
            public void onSuccess(String respond) {
                Type type = new TypeToken<NetBean<SearchBean>>() {
                }.getType();
                Gson gson = new Gson();
                NetBean<SearchBean> netBean = gson.fromJson(respond, type);
                SearchBean data = netBean.getData();
                Message message = mHandler.obtainMessage();
                message.obj =data;
                mHandler.sendMessage(message);
            }

            @Override
            public void onFailure(Exception e) {

            }
        })).start();
    }

    @Override
    protected void initListener() {
        super.initListener();
        btn_detil.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,VerificationActivity.class);
        intent.putExtra(VerificationActivity.TOUSER,user);
        startActivity(intent);
    }

}
