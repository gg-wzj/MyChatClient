package com.example.mychatclient.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mychatclient.R;
import com.example.mychatclient.TypeConstant;
import com.example.mychatclient.activity.ChatActivity;
import com.example.mychatclient.activity.MainActivity;
import com.example.mychatclient.activity.NewFriendActivity;
import com.example.mychatclient.adapter.ContactAdapter;
import com.example.mychatclient.app.MyApplication;
import com.example.mychatclient.db.bean.FriendshipBean;
import com.example.mychatclient.db.dao.FriendshipDao;
import com.example.mychatclient.net.OneTimeConn;
import com.example.mychatclient.net.bean.NetBean;
import com.example.mychatclient.util.SPUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzj on 2017/9/21.
 */

public class ContactFragment extends Fragment {

    private ListView lv_contact;
    private ContactAdapter adapter;
    private List<FriendshipBean> mList = new ArrayList<>();

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    Toast.makeText(MyApplication.getContext(),"获取朋友列表失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private String dbName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_contact,null);
        lv_contact = view.findViewById(R.id.lv_contact);
       return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new ContactAdapter(mList);
        lv_contact.setAdapter(adapter);
        dbName ="a_"+ SPUtil.getString("user");

        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Activity activity = getActivity();
                    Intent intent = new Intent(activity, NewFriendActivity.class);
                    activity.startActivityForResult(intent,1);
                    return;
                }

                if(position >=5){
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    FriendshipBean bean = mList.get(position - 5);
                    intent.putExtra(ChatActivity.FRIEND_PHONE,bean.getPhone());
                    intent.putExtra(ChatActivity.REMARK,bean.getRemark());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //检查数据库是否有朋友列表
        boolean hasFriend = SPUtil.getBoolean("friendship");
        if(!hasFriend){
            //数据库中没有列表 网络请求
            requestFriendList();
        }else {
            updateList();
        }
    }

    public void updateTop(int count){
        View childAt = lv_contact.getChildAt(0);
        View viewById = childAt.findViewById(R.id.tv_contact_point);
        if(count == 0){
            viewById.setVisibility(View.GONE);
        }else {
            viewById.setVisibility(View.VISIBLE);
        }
        //updateData();
    }

    public void updateList(){
        updateData();
    }

    private void updateData() {
        new Thread(){
            @Override
            public void run() {
                FriendshipDao dao = new FriendshipDao(dbName);
                List<FriendshipBean> list = dao.findAll();
                mList.clear();
                mHandler.sendEmptyMessage(0);
                mList.addAll(list);
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void requestFriendList() {
        JSONObject jsonObject = new JSONObject();
        final String user = SPUtil.getString("user");
        String json ="";
        try {
            jsonObject.put("type", TypeConstant.REQUEST_FRIENDSHIP);
            jsonObject.put("data",user);
            json = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread(new OneTimeConn(json, new OneTimeConn.OnRespondListener() {
            @Override
            public void onSuccess(String respond) {
                Gson gson = new Gson();
                Type type = new TypeToken<NetBean<List<FriendshipBean>>>() {
                }.getType();
                NetBean<List<FriendshipBean>> netBean = gson.fromJson(respond, type);
                List<FriendshipBean> data = netBean.getData();
                if(data!=null){
                    for (FriendshipBean bean : data) {
                        FriendshipDao dao = new FriendshipDao(dbName);
                        dao.addFriendship(bean);
                    }
                }
                SPUtil.putBoolean("friendship",true);

                //由于网络请求异步操作，fragment onStart可能已经执行，要重新刷新数据
                updateList();
            }

            @Override
            public void onFailure(Exception e) {
                mHandler.sendEmptyMessage(1);
            }
        })).start();
    }
}
