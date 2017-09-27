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

public class MeFragment extends Fragment implements View.OnClickListener {

    private int[] ids = new int[]{
        R.id.option_money,R.id.option_collection,R.id.option_photo,R.id.option_cardPack,R.id.option_face,R.id.option_setting
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
            "钱包","收藏","相册","卡包","表情","设置"
    };
    private List<View> optionViews;

    private LinearLayout ll_me_money;
    private LinearLayout ll_me_fun;
    private LinearLayout ll_me_setting;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me,null);
        ll_me_money = view.findViewById(R.id.ll_me_money);
        ll_me_fun = view.findViewById(R.id.ll_me_fun);
        ll_me_setting = view.findViewById(R.id.ll_me_setting);
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
    }

    @Override
    public void onClick(View v) {

    }
}
