package com.example.mychatclient.activity;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mychatclient.R;
import com.example.mychatclient.TypeConstant;
import com.example.mychatclient.db.bean.ChatRecordBean;
import com.example.mychatclient.db.bean.FriendshipBean;
import com.example.mychatclient.db.dao.ChatRecordDao;
import com.example.mychatclient.db.dao.FriendshipDao;
import com.example.mychatclient.net.OneTimeConn;
import com.example.mychatclient.net.bean.NetBean;
import com.example.mychatclient.net.bean.SendMessageBean;
import com.example.mychatclient.util.SPUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by wzj on 2017/9/26.
 */

public class NewFriendActivity extends BaseActivity {

    private LinearLayout ll_new_friend;

    private static final int TOAST = 1;
    private static final int REMOVE =2;
    private static final int REQUEST_FAILURE =3;

    private int childCount;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TOAST:
                    Toast.makeText(NewFriendActivity.this,"好友请求已拒绝",Toast.LENGTH_SHORT).show();
                    break;
                case REMOVE:
                    View view = (View) msg.obj;
                    ll_new_friend.removeView(view);
                    Toast.makeText(NewFriendActivity.this,"结果已发送",Toast.LENGTH_SHORT).show();
                    //更新孩子数
                    childCount = ll_new_friend.getChildCount();

                    //setResult在必须在finish之前，onStop,onDestroy,onPause都可能会失败
                    Intent intent = new Intent();
                    intent.putExtra("data_return",childCount);
                    setResult(RESULT_OK,intent);
                    break;
                case REQUEST_FAILURE:
                    Toast.makeText(NewFriendActivity.this,"网络请求失败",Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };
    private String dbName;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_newfriend);
        ll_new_friend = (LinearLayout) findViewById(R.id.ll_new_friend);

        TextView tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("新的朋友");

    }

    @Override
    protected void initDats() {
        super.initDats();
        dbName ="a_"+ SPUtil.getString("user");
        ChatRecordDao dao = new ChatRecordDao(dbName);
        List<ChatRecordBean> verificationRecords = dao.findVerificationRecord();
        for (ChatRecordBean verificationRecord : verificationRecords) {
            ll_new_friend.addView(getItem(verificationRecord));
        }
    }

   private View  getItem(ChatRecordBean bean){
       LayoutInflater inflater  = LayoutInflater.from(this);
       View view = inflater.inflate(R.layout.item_new_friend,null);

       Button btn_newfriend_confirm = view.findViewById(R.id.btn_newfriend_confirm);
       setConfirmLisetener( btn_newfriend_confirm, bean);

       Button btn_newfriend_cancel = view.findViewById(R.id.btn_newfriend_confuse);
       setRefuseListener(btn_newfriend_cancel,bean);

       TextView tv_newfriend_name = view.findViewById(R.id.tv_newfriend_name);
       TextView  tv_newfriend_messge= view.findViewById(R.id.tv_newfriend_messge);
       String message = bean.getMessage();
       String name = message.substring(0, message.lastIndexOf("/"));
       String trueMessage = message.substring(message.lastIndexOf("/") + 1);
       tv_newfriend_name.setText(name);
       tv_newfriend_messge.setText(trueMessage);
       return view;
   }



    private void setConfirmLisetener(final Button btn, final ChatRecordBean bean) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = getJson(2,bean);
                new Thread(new OneTimeConn(json, new OneTimeConn.OnRespondListener() {
                    @Override
                    public void onSuccess(String respond) {
                        //同意好友申请 服务器将返回该用户的信息
                        Type type = new TypeToken<NetBean<FriendshipBean>>() {
                        }.getType();
                        NetBean<FriendshipBean> netBean = new Gson().fromJson(respond, type);
                        FriendshipBean data = netBean.getData();
                        FriendshipDao f_dao = new FriendshipDao(dbName);
                        //保存好友信息到数据库
                        f_dao.addFriendship(data);
                        //删除 好友申请记录
                        ChatRecordDao c_dao = new ChatRecordDao(dbName);
                        c_dao.deleteVerifcation(bean);
                        //更新ui取消掉这个
                        removeOptionView(btn);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Message message = mHandler.obtainMessage();
                        message.what = REQUEST_FAILURE;
                        mHandler.sendMessage(message);
                    }
                })).start();
            }
        });
    }



    private void setRefuseListener(final Button btn, final ChatRecordBean bean) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = getJson(3, bean);
                new Thread(new OneTimeConn(json, new OneTimeConn.OnRespondListener() {
                    @Override
                    public void onSuccess(String respond) {
                        //拒绝好友申请 服务器将返回ok
                        try {
                            JSONObject jsonObject = new JSONObject(respond);
                            String data = jsonObject.optString("data");
                            if("ok".equals(data)){
                                mHandler.sendEmptyMessage(0);
                            }
                            //删除 好友申请记录
                            ChatRecordDao c_dao = new ChatRecordDao(dbName);
                            c_dao.deleteVerifcation(bean);
                            //更新ui取消掉这个
                            removeOptionView(btn);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Message message = mHandler.obtainMessage();
                        message.what = REQUEST_FAILURE;
                        mHandler.sendMessage(message);
                    }
                })).start();
            }
        });
    }

    private void removeOptionView(Button btn) {
        //更新ui取消掉这个
        Message message = mHandler.obtainMessage();
        View parent = (View) btn.getParent();
        View trueParent = (View) parent.getParent();
        message.obj = trueParent;
        message.what = REMOVE;
        mHandler.sendMessage(message);
    }
    private String getJson(int result,ChatRecordBean bean) {
        String user = SPUtil.getString("user");
        SendMessageBean messageBean = new SendMessageBean(user,bean.getPhone(),result,bean.getMessage(),System.currentTimeMillis());
        NetBean<SendMessageBean> netBean = new NetBean<>(TypeConstant.HANDLE_VERIFICATION,messageBean);
        return new Gson().toJson(netBean, NetBean.class);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
