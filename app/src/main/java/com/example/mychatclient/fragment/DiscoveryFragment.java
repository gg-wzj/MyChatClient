package com.example.mychatclient.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mychatclient.R;
import com.example.mychatclient.util.OptionViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzj on 2017/9/21.
 */

public class DiscoveryFragment extends Fragment implements View.OnClickListener {

    private String[] optionTexts = new String[]{
            "朋友圈", "摇一摇", "扫一扫", "附近的人", "漂流瓶", "购物", "游戏"
    };
    private int[] optionDrawable = new int[]{
            R.mipmap.ic_friend_circle,
            R.mipmap.ic_shake_blue,
            R.mipmap.ic_scan_blue,
            R.mipmap.ic_around_blue,
            R.mipmap.ic_around_blue,
            R.mipmap.ic_shop_red,
            R.mipmap.ic_game
    };
    private int[] optionId = new int[]{
            R.id.option_friendCircle, R.id.option_shake, R.id.option_scan, R.id.option_around,
            R.id.option_plp, R.id.option_shop, R.id.option_game
    };
    private List<View> optionViews;

    private LinearLayout ll_discovery_find;
    private LinearLayout ll_discovery_check;
    private LinearLayout ll_discovery_friend;
    private LinearLayout ll_discovery_fun;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery, null);
        ll_discovery_find = view.findViewById(R.id.ll_discovery_find);
        ll_discovery_check = view.findViewById(R.id.ll_discovery_check);
        ll_discovery_friend = view.findViewById(R.id.ll_discovery_friend);
        ll_discovery_fun = view.findViewById(R.id.ll_discovery_fun);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        optionViews = new ArrayList<>();
        //创建optionView 并添加到列表中 方便管理
        for (int i = 0; i < optionId.length; i++) {
            View optionView = OptionViewUtil.getOptionView(optionDrawable[i], optionTexts[i], optionId[i]);
            optionViews.add(optionView);
        }
        //把optionView放到对应的父容器中
        for (int i = 0; i < optionViews.size(); i++) {
            View view = optionViews.get(i);
            if (i == 0) {
                ll_discovery_friend.addView(view);
            } else if (i<=2){
                ll_discovery_check.addView(view);
            }else if(i<=4) {
                ll_discovery_find.addView(view);
            }else {
                ll_discovery_fun.addView(view);
            }
        }
        //为optionView设置点击事件
        for (View optionView : optionViews) {
            optionView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
