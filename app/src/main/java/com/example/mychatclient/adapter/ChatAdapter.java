package com.example.mychatclient.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mychatclient.R;
import com.example.mychatclient.app.MyApplication;
import com.example.mychatclient.db.bean.ChatRecordBean;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by wzj on 2017/9/23.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {


    private final List<ChatRecordBean> mDatas;

    public ChatAdapter(List<ChatRecordBean> datas) {
        mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.item_chat,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatRecordBean bean = mDatas.get(position);
        long time = bean.getTime();
        Date date = new Date(time);
        DateFormat format = DateFormat.getTimeInstance();
        String formatTime = format.format(date);
        int type = bean.getType();
        holder.tv_time.setVisibility(View.GONE);
        if(type == 0){
            holder.ll_right.setVisibility(View.GONE);
            holder.ll_left.setVisibility(View.VISIBLE);
            holder.tv_left_message.setText(bean.getMessage());
            holder.tv_time.setText(formatTime);
        }else {
            holder.ll_right.setVisibility(View.VISIBLE);
            holder.ll_left.setVisibility(View.GONE);
            holder.tv_right_message.setText(bean.getMessage());
            holder.tv_time.setText(formatTime);
        }

        int hasSend = bean.getHasSend();
        if(hasSend == 1){
            holder.tv_send_status.setVisibility(View.GONE);
        }else if(hasSend == 0){
            holder.tv_send_status.setVisibility(View.VISIBLE);
        }else if(hasSend == 2){
            //发送失败
            holder.tv_send_status.setVisibility(View.VISIBLE);
            holder.tv_send_status.setText("请求超时,发送失败");
        }
    }

    @Override
    public int getItemCount() {
        return mDatas!= null?mDatas.size():0;
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout ll_left ;
        LinearLayout ll_right;
        ImageView iv_left_header ;
        TextView tv_left_message;
        TextView tv_right_message;
        ImageView iv_right_header;
        TextView tv_time ;
        TextView tv_send_status;
        public ViewHolder(View itemView) {
            super(itemView);
            ll_left = itemView.findViewById(R.id.ll_chat_left);
            ll_right = itemView.findViewById(R.id.ll_chat_right);
            iv_right_header = itemView.findViewById(R.id.iv_right_header);
            iv_left_header = itemView.findViewById(R.id.iv_left_header);
            tv_left_message = itemView.findViewById(R.id.tv_left_message);
            tv_right_message = itemView.findViewById(R.id.tv_right_message);
            tv_time = itemView.findViewById(R.id.tv_chat_time);
            tv_send_status = itemView.findViewById(R.id.tv_send_status);
        }
    }
}
