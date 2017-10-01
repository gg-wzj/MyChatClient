package com.example.mychatclient.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mychatclient.R;
import com.example.mychatclient.app.MyApplication;
import com.example.mychatclient.db.bean.RecentMessageBean;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by wzj on 2017/9/22.
 */

public class RecentMessageAdapter extends BaseAdapter {

    List<RecentMessageBean> mDatas ;

    public RecentMessageAdapter(List<RecentMessageBean> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas!=null?mDatas.size():0;
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
        ViewHolder viewHolder ;
        if(convertView == null){
            convertView = LayoutInflater.from(MyApplication.getContext())
                    .inflate(R.layout.item_recent_message,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.iv= convertView.findViewById(R.id.iv_recent_header);
            viewHolder.tv_name= convertView.findViewById(R.id.tv_recent_name);
            viewHolder.tv_message = convertView.findViewById(R.id.tv_recent_message);
            viewHolder.tv_time = convertView.findViewById(R.id.tv_recent_time);
            viewHolder.tv_point = convertView.findViewById(R.id.tv_recent_point);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RecentMessageBean bean = mDatas.get(position);

        //设置时间
        Date date = new Date(bean.getTime());
        DateFormat format =  DateFormat.getDateInstance();
       String str =  format.format(date);
        String finalFormat = str.substring(str.indexOf('年') + 1);
        viewHolder.tv_time.setText(finalFormat);
        //设置备注
        viewHolder.tv_name.setText(bean.getRemark());
        //设置内容信息
        viewHolder.tv_message.setText(bean.getMessage());
        //设置红点
        int wdCount = bean.getWdCount();
        viewHolder.tv_point.setText(wdCount+"");
        viewHolder.tv_point.setVisibility(wdCount == 0? View.GONE:View.VISIBLE);

        return convertView;
    }

    class ViewHolder{
        ImageView iv ;
        TextView tv_name;
        TextView tv_message;
        TextView tv_time;
        TextView tv_point;
    }
}
