package com.example.mychatclient.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mychatclient.R;
import com.example.mychatclient.activity.ChatActivity;
import com.example.mychatclient.activity.NewFriendActivity;
import com.example.mychatclient.adapter.ContactAdapter;
import com.example.mychatclient.db.bean.FriendshipBean;
import com.example.mychatclient.db.dao.FriendshipDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzj on 2017/9/21.
 */

public class ContactFragment extends Fragment {

    private ListView lv_contact;
    private ContactAdapter adapter;
    private List<FriendshipBean> mList;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.notifyDataSetChanged();
        }
    };

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
        mList = new ArrayList<>();
        adapter = new ContactAdapter(mList);
        lv_contact.setAdapter(adapter);

        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Intent intent = new Intent(getActivity(), NewFriendActivity.class);
                    startActivity(intent);
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
        updateData();
    }

    public void updateTop(){
        View childAt = lv_contact.getChildAt(0);
        View viewById = childAt.findViewById(R.id.tv_contact_point);
        viewById.setVisibility(View.VISIBLE);
        //updateData();
    }

    public void updateList(){
        updateData();
    }

    private void updateData() {
        new Thread(){
            @Override
            public void run() {
                FriendshipDao dao = new FriendshipDao("wzj");
                List<FriendshipBean> list = dao.findAll();
                mList.clear();
                mList.addAll(list);
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }


}
