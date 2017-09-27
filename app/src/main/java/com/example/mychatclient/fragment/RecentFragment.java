package com.example.mychatclient.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mychatclient.R;
import com.example.mychatclient.activity.ChatActivity;
import com.example.mychatclient.adapter.RecentMessageAdapter;
import com.example.mychatclient.db.bean.RecentMessageBean;
import com.example.mychatclient.db.dao.RecentMessageDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzj on 2017/9/21.
 */

public class RecentFragment extends Fragment {

    private View rootView;
    private ListView lv_recent;

    private RecentMessageDao dao = new RecentMessageDao("wzj");
    private List<RecentMessageBean> mList;

    private RecentMessageAdapter adapter;

    private static final int SETADAPTER = 1;
    private static final int NOTIFYADAPTER = 2;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SETADAPTER:
                    lv_recent.setAdapter(adapter);
                    break;
                case NOTIFYADAPTER:
                    adapter.notifyDataSetChanged();
                    break;
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_recent, null);
            lv_recent = rootView.findViewById(R.id.lv_recent);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mList != null)
            return;
       // moniDatas();
        mList = new ArrayList<>();
        adapter = new RecentMessageAdapter(mList);
        Message message = mHandler.obtainMessage();
        message.what = SETADAPTER;
        mHandler.sendMessage(message);
        lv_recent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecentMessageBean bean = mList.get(position);
                String remark = bean.getRemark();
                String friendPhone = bean.getFriendPhone();
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(ChatActivity.REMARK, remark);
                intent.putExtra(ChatActivity.FRIEND_PHONE, friendPhone);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        new Thread() {
            @Override
            public void run() {
                super.run();
                updateList();
            }
        }.start();
    }

    public void update() {
        updateList();
    }

    private void updateList() {
        List<RecentMessageBean> list = dao.findAll();
        mList.clear();
        mList.addAll(list);
        Message message = mHandler.obtainMessage();
        message.what = NOTIFYADAPTER;
        mHandler.sendMessage(message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //dao.deleteAll();
    }
}
