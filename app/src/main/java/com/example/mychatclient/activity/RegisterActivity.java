package com.example.mychatclient.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mychatclient.R;
import com.example.mychatclient.TypeConstant;
import com.example.mychatclient.net.OneTimeConn;
import com.example.mychatclient.net.bean.RegisterBean;
import com.example.mychatclient.net.bean.NetBean;
import com.example.mychatclient.util.SPUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wzj on 2017/9/20.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText etNick;
    private TextView tvArea;
    private EditText etPhone;
    private EditText etPwd;

    private Button btnRegister;
    private boolean[] finishEt = new boolean[3];
    private int currentEt;
    private boolean finish;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_register);
        TextView toolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
        toolbarTitle.setText("请填写账号");

        etNick = (EditText) findViewById(R.id.etNick);
        tvArea = (TextView) findViewById(R.id.tvArea);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etPwd = (EditText) findViewById(R.id.etPwd);
        btnRegister = (Button) findViewById(R.id.btnRegister);

    }

    /**
     * 通过OnFocusChangeListener配合 实现实时监听3个editText是否完成填写
     */
    private View.OnFocusChangeListener focusListner = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            EditText editText = (EditText) v;
            if (hasFocus) {
                switch (v.getId()) {
                    case R.id.etNick:
                        currentEt = 0;
                        break;
                    case R.id.etPhone:
                        currentEt = 1;
                        break;
                    case R.id.etPwd:
                        currentEt = 2;
                        break;
                }
                editText.addTextChangedListener(watcher);
            }else {
                editText.removeTextChangedListener(watcher);
            }
        }
    };

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length() > 0 ){
                finishEt[currentEt] = true;
            }else {
                finishEt[currentEt] = false;
            }
            finish = true;
            for(int i = 0 ;i<3;i++){
                finish = finishEt[i];
            }

            btnRegister.setBackgroundColor(finish ? getResources().getColor(R.color.green)
                    :getResources().getColor(R.color.registerGray));
        }
    };

    @Override
    protected void initListener() {
        etNick.setOnFocusChangeListener(focusListner);
        etPhone.setOnFocusChangeListener(focusListner);
        etPwd.setOnFocusChangeListener(focusListner);

        /*btnRegister.setFocusable(true);
        btnRegister.setFocusableInTouchMode(true); 在设置了获取焦点后，第一次点击获取焦点，第二次才执行点击事件*/
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(!finish){
            return;
        }
        String nick = etNick.getText().toString();
        final String phone = etPhone.getText().toString();
        final String pwd = etPwd.getText().toString();

        final String json ;
        if(nick.length()>10){
            Toast.makeText(this,"昵称长度不能大于10",Toast.LENGTH_SHORT).show();
            return;
        }else{
            RegisterBean registerBean = new RegisterBean(phone, "中国", nick, pwd, 0);
            NetBean<RegisterBean> request = new NetBean<>(TypeConstant.REQUSET_REGISTER,registerBean);
            Gson gson = new Gson();
            json = gson.toJson(request, NetBean.class);
        }

        //向服务器提交注册申请 展示正在注册的progressDialog
        ProgressDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        final android.app.AlertDialog dialog = builder.setMessage("正在申请注册").setCancelable(false).create();

        new Thread(new OneTimeConn(json, new OneTimeConn.OnRespondListener() {
            @Override
            public void onSuccess(String respond) {
                try {
                    dialog.dismiss();
                    JSONObject jsonObject = new JSONObject(respond);
                    if(jsonObject.optString("type").equals(TypeConstant.RESPOND_REGISTER)){
                        String result = jsonObject.optString("data");
                        if(result.equals("ok"))
                            regiserSucceed(phone,pwd);
                        else registerFail();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e) {
                dialog.dismiss();
            }
        })).start();
    }


    /**
     * 注册成功是调用此方法
     */
    private void regiserSucceed(final String phone, final String pwd) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //成功后弹窗 决定是否登录
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("注册成功").setMessage("是否现在登录")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //点击确定 写入sp 缓存 登录 当有缓存的时候不显示splash界面
                                SPUtil.putString("user",phone);
                                SPUtil.putString("pwd",pwd);
                                Log.e("wzj","sp finish=============\n"+phone);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    private void registerFail() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this,"该手机号已经注册，请更换号码",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
