package com.example.mychatclient.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.example.mychatclient.R;
import com.example.mychatclient.app.MyApplication;

/**
 * Created by wzj on 2017/9/21.
 */

public class OptionViewUtil {
    public static View getOptionView(int resourceId,String text,int id){
        Context context = MyApplication.getContext();
        View view = View.inflate(context,R.layout.item_option,null);
       setOptionViewData(view,resourceId,text,id);
        return view;
    }

    public static View setOptionViewData(View view,int resourceId,String text,int id){
        Context context = MyApplication.getContext();
        TextView tv =  view.findViewById(R.id.tv_option);
        Drawable drawable = context.getResources().getDrawable(resourceId);
        drawable.setBounds(0,0,(int)dp2px(25),(int)dp2px(25));
        tv.setCompoundDrawables(drawable,null,null,null);
        tv.setText(text);
        view.setId(id);
        return view;
    }


    public static float dp2px(float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,MyApplication.getContext().getResources().getDisplayMetrics());
    }
}
