package com.example.mychatclient.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mychatclient.R;
import com.example.mychatclient.activity.ActivityController;
import com.example.mychatclient.activity.SplashActivity;
import com.example.mychatclient.app.MyApplication;
import com.example.mychatclient.net.bean.SearchBean;
import com.example.mychatclient.service.ChatService;
import com.example.mychatclient.util.OptionViewUtil;
import com.example.mychatclient.util.SPUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzj on 2017/9/21.
 */

public class MeFragment extends Fragment implements View.OnClickListener {

    private int[] ids = new int[]{
        R.id.option_money,R.id.option_collection,R.id.option_photo,R.id.option_cardPack,R.id.option_face,R.id.option_exit
    };
    private int[] drawableIds = new int[]{
            R.mipmap.ic_wallet_blue1,
            R.mipmap.ic_sellection1,
            R.mipmap.ic_photo_blue1,
            R.mipmap.ic_card_pack1,
            R.mipmap.ic_emo_yellow,
            R.mipmap.ic_setting_blue
    };
    private String[] optionTexts  = new String[]{
            "钱包","收藏","相册","卡包","表情","退出"
    };
    private List<View> optionViews;

    private LinearLayout ll_me_money;
    private LinearLayout ll_me_fun;
    private LinearLayout ll_me_setting;
    private TextView tv_me_nick;
    private TextView tv_me_phone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me,null);
        ll_me_money = view.findViewById(R.id.ll_me_money);
        ll_me_fun = view.findViewById(R.id.ll_me_fun);
        ll_me_setting = view.findViewById(R.id.ll_me_setting);

        tv_me_nick = view.findViewById(R.id.tv_me_nick);
        tv_me_phone = view.findViewById(R.id.iv_me_phone);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        optionViews = new ArrayList<>();
        for(int i = 0 ;i<ids.length;i++){
            View optionView = OptionViewUtil.getOptionView(drawableIds[i], optionTexts[i], ids[i]);
            optionViews.add(optionView);
        }

        for (int i = 0 ;i< optionViews.size();i++){
            View v = optionViews.get(i);
            if(i == 0 ){
                ll_me_money.addView(v);
            }else if(i<=4){
                ll_me_fun.addView(v);
            }else {
                ll_me_setting.addView(v);
            }
        }

        for (View optionView : optionViews) {
            optionView.setOnClickListener(this);
        }

        String userInfo = SPUtil.getString("userInfo");
        SearchBean searchBean = new Gson().fromJson(userInfo, SearchBean.class);
        tv_me_nick.setText(searchBean.getNick());
        tv_me_phone.setText("微信号:"+searchBean.getPhone());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.option_exit:
                showExitDialog();
                break;
        }
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater  = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_exit, null);
        View ll_exitDialog_app = view.findViewById(R.id.ll_exitDialog_app);
        View ll_exitDialog_count = view.findViewById(R.id.ll_exitDialog_count);
        ll_exitDialog_app.setOnClickListener(dialogLisetner);
        ll_exitDialog_count.setOnClickListener(dialogLisetner);
        builder.setView(view);
        AlertDialog exitDialog = builder.create();
        exitDialog.show();
    }

    private View.OnClickListener dialogLisetner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentActivity activity = getActivity();
            switch (v.getId()){
                case R.id.ll_exitDialog_app:
                    //退出app
                    ActivityController.finishAll();
                    break;
                case R.id.ll_exitDialog_count:
                    //退出账号
                    //将user pwd缓存清除
                    SPUtil.putString("user","");
                    SPUtil.putString("pwd","");
                    SPUtil.putBoolean("friendship",false);
                    ActivityController.finishAll();
                    Intent intent  = new Intent(MyApplication.getContext(), SplashActivity.class);
                    startActivity(intent);
                    break;
            }
            //关闭服务
            Intent intent  = new Intent(activity, ChatService.class);
            activity.stopService(intent);
        }


    };
}
