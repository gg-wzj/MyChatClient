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
import com.example.mychatclient.db.bean.FriendshipBean;
import com.example.mychatclient.db.dao.FriendshipDao;
import com.example.mychatclient.net.OneTimeConn;
import com.example.mychatclient.net.bean.NetBean;
import com.example.mychatclient.net.bean.SearchBean;
import com.example.mychatclient.util.SPUtil;
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
    public static final String TYPE = "type";
    private SearchBean user;

    public static final int FRIEND =1;
    public static final int NOMAL = 2;
    private int type;


    private TextView tv_detail_name;
    private ImageView iv_detail_header;
    private Button btn_detail_chat;
    private ImageView iv_detail_sex;
    private Button btn_detial_add;
    private String dbName;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!= null){
            user = (SearchBean) extras.getSerializable(USER);
            type = extras.getInt(TYPE);
        }

        tv_detail_name = (TextView) findViewById(R.id.tv_detail_name);
        iv_detail_header = (ImageView) findViewById(R.id.iv_detail_header);
        btn_detail_chat = (Button) findViewById(R.id.btn_detail_chat);
        btn_detial_add = (Button) findViewById(R.id.btn_detail_add);
        iv_detail_sex = (ImageView) findViewById(R.id.iv_detail_sex);

    }

    @Override
    protected void initDats() {
        super.initDats();

        dbName = "a_"+SPUtil.getString("user");
        tv_detail_name.setText(this.user.getNick());
        iv_detail_sex.setImageResource(this.user.getSex() == 0 ? R.mipmap.ic_gender_male:R.mipmap.ic_gender_female);

        if(type == FRIEND){
            btn_detial_add.setVisibility(View.GONE);
            btn_detail_chat.setVisibility(View.VISIBLE);
        }else {
            btn_detial_add.setVisibility(View.VISIBLE);
            btn_detail_chat.setVisibility(View.GONE);
        }

    }

    @Override
    protected void initListener() {
        super.initListener();
        btn_detail_chat.setOnClickListener(this);
        btn_detial_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_detail_add:
                intent.setClass(this,VerificationActivity.class);
                intent.putExtra(VerificationActivity.TOUSER,user.getPhone());
                startActivity(intent);
                break;
            case R.id.btn_detail_chat:
                intent.setClass(this,ChatActivity.class);
                FriendshipDao dao = new FriendshipDao(dbName);
                FriendshipBean bean = dao.getInfoWithPhone(user.getPhone());
                intent.putExtra(ChatActivity.REMARK,bean.getRemark());
                intent.putExtra(ChatActivity.FRIEND_PHONE,bean.getPhone());
                break;
        }

        startActivity(intent);

    }

}
