package com.example.mychatclient.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mychatclient.R;
import com.example.mychatclient.app.MyApplication;
import com.example.mychatclient.db.bean.FriendshipBean;
import com.example.mychatclient.util.OptionViewUtil;

import java.util.List;

/**
 * Created by wzj on 2017/9/26.
 */

public class ContactAdapter extends BaseAdapter {

    private List<FriendshipBean> contentList;
    private int[] titleHeader = new int[]{
            R.mipmap.ic_new_friend,
            R.mipmap.ic_group_cheat,
            R.mipmap.ic_tag,
            R.mipmap.ic_offical
    };
    private String[] titleDescribe = new String[]{
            "新的朋友","群聊","标签","公众号"
    };

    public ContactAdapter(List<FriendshipBean> contentList) {
        this.contentList = contentList;
    }

    @Override
    public int getCount() {
        return contentList!= null? contentList.size()+5:5;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position == 4){
            Context context = MyApplication.getContext();
            TextView tv = new TextView(context);
            tv.setBackgroundColor(context.getResources().getColor(R.color.gray1));
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) dp2px(20));
            tv.setLayoutParams(layoutParams);
            return tv;
        }

        ViewHolder holder ;
        if(convertView != null && convertView instanceof LinearLayout){
            //convertView = OptionViewUtil.getOptionView(R.mipmap.avatar_def,"",R.id.option_listview);
            holder = (ViewHolder) convertView.getTag();
        }else {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact,null);
            holder = new ViewHolder();
            holder.iv_contact_header = convertView.findViewById(R.id.iv_contact_header);
            holder.tv_contact_name = convertView.findViewById(R.id.tv_contact_name);
            convertView.setTag(holder);
        }


        if(position < 4){
            holder.iv_contact_header.setImageResource(titleHeader[position]);
            holder.tv_contact_name.setText(titleDescribe[position]);
        }else {
            position = position -5;
            FriendshipBean bean = contentList.get(position);

            holder.iv_contact_header.setImageResource(R.mipmap.avatar_def);
            holder.tv_contact_name.setText(bean.getRemark());
        }

        return convertView;
    }

    class ViewHolder{
        ImageView iv_contact_header;
        TextView tv_contact_name;

    }

    public static float dp2px(float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,MyApplication.getContext().getResources().getDisplayMetrics());
    }

}
