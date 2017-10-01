package com.example.mychatclient.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.List;

/**
 * Created by wzj on 2017/9/25.
 */

public class AddFriendActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_addfriend;
    private Button btn_addfriend;
    private String user;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dialog.dismiss();
            switch (msg.what){
                case 1:
                    Toast.makeText(AddFriendActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(AddFriendActivity.this, "该用户不存在", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };
    private ProgressDialog dialog;
    private String dbName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);

        et_addfriend = (EditText) findViewById(R.id.et_addfriend);
        btn_addfriend = (Button) findViewById(R.id.btn_addfriend);

        btn_addfriend.setOnClickListener(this);

        dbName = "a_" + SPUtil.getString("user");
    }

    @Override
    public void onClick(View v) {


        user = et_addfriend.getText().toString();
        if (TextUtils.isEmpty(user)) {
            Toast.makeText(this, "搜索的账号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("正在搜索");
        dialog.show();
        FriendshipDao dao = new FriendshipDao(dbName);


        FriendshipBean bean = dao.getInfoWithPhone(user);
        if (bean != null) {
            Intent intent = new Intent(this, DetailActivity.class);
            SearchBean searchBean = new SearchBean(bean.getPhone(), bean.getArea(), bean.getNick(), bean.getSex());
            intent.putExtra(DetailActivity.USER, searchBean);
            intent.putExtra(DetailActivity.TYPE, DetailActivity.FRIEND);
            startActivity(intent);
            return;
        }

        String json = getSearchJson(TypeConstant.REQUEST_SEARCH);
        requestSearchData(json);

    }

    private String getSearchJson(String type) {
        JSONObject jsonObject = new JSONObject();
        String json = "";
        try {
            jsonObject.put("type", type);
            jsonObject.put("data", user);
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
                //先判断查找的用户是否存在
                try {
                    JSONObject jsonObject = new JSONObject(respond);
                    String data = jsonObject.optString("data");
                    if("no exist".equals(data)){
                        mHandler.sendEmptyMessage(2);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Type type = new TypeToken<NetBean<SearchBean>>() {
                }.getType();
                Gson gson = new Gson();
                NetBean<SearchBean> netBean = gson.fromJson(respond, type);
                SearchBean bean = netBean.getData();
                if (bean != null) {
                    Intent intent = new Intent(AddFriendActivity.this, DetailActivity.class);
                    SearchBean searchBean = new SearchBean(bean.getPhone(), bean.getArea(), bean.getNick(), bean.getSex());
                    dialog.dismiss();
                    intent.putExtra(DetailActivity.USER, searchBean);
                    intent.putExtra(DetailActivity.TYPE, DetailActivity.NOMAL);
                    startActivity(intent);
                }else {
                    mHandler.sendEmptyMessage(1);
                }

            }

            @Override
            public void onFailure(Exception e) {
                mHandler.sendEmptyMessage(1);
            }
        })).start();
    }
}
