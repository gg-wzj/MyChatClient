package com.example.mychatclient.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;

import com.example.mychatclient.app.MyApplication;
import com.example.mychatclient.db.CurrentUserDB;
import com.example.mychatclient.db.bean.RecentMessageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzj on 2017/9/22.
 */

public class RecentMessageDao {

    private final CurrentUserDB dbHelper;
    private static final String TABLENAME = CurrentUserDB.TABLE_RECENT_MESSAGE;

    public RecentMessageDao(String name) {
        dbHelper = new CurrentUserDB(MyApplication.getContext(), name);
    }

    public List<RecentMessageBean> findAll() {
        List<RecentMessageBean> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLENAME, new String[]{
                CurrentUserDB.RecentMessageColum.FRIEND_PHONE,
                CurrentUserDB.RecentMessageColum.REMARK,
                CurrentUserDB.RecentMessageColum.MESSAGE,
                CurrentUserDB.RecentMessageColum.WDCOUNT,
                CurrentUserDB.RecentMessageColum.TIME
        }, null, null, null, null, "time desc");

        while (cursor.moveToNext()) {
            String phone = cursor.getString(0);
            String remark = cursor.getString(1);
            String message = cursor.getString(2);
            int wdCount = cursor.getInt(3);
            long time = cursor.getLong(4);
            RecentMessageBean bean = new RecentMessageBean();
            bean.setRemark(remark);
            bean.setFriendPhone(phone);
            bean.setMessage(message);
            bean.setWdCount(wdCount);
            bean.setTime(time);
            list.add(bean);
        }
        cursor.close();
        db.close();
        return list;
    }

    public boolean addMessage(RecentMessageBean bean) {
        if (bean == null)
            return false;
        //有重复的先删除
        if(findWithPhone(bean.getFriendPhone())!=null)
            deleteWithPhone(bean.getFriendPhone());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CurrentUserDB.RecentMessageColum.FRIEND_PHONE, bean.getFriendPhone());
        values.put(CurrentUserDB.RecentMessageColum.MESSAGE, bean.getMessage());
        values.put(CurrentUserDB.RecentMessageColum.REMARK, bean.getRemark());
        values.put(CurrentUserDB.RecentMessageColum.WDCOUNT, bean.getWdCount());
        values.put(CurrentUserDB.RecentMessageColum.TIME, bean.getTime());
        long result = db.insert(TABLENAME, null, values);
        db.close();
        return result != -1;
    }

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLENAME, null, null);
        db.close();
    }

    public RecentMessageBean findWithPhone(String phone){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLENAME + " where friendPhone = ?", new String[]{phone});
        while (cursor.moveToNext()){
            String friendPhone = cursor.getString(1);
            String remark = cursor.getString(2);
            String message = cursor.getString(3);
            int wdCount = cursor.getInt(4);
            long time = cursor.getLong(5);
            cursor.close();
            db.close();
            return new RecentMessageBean(friendPhone,remark,message,wdCount,time);
        }
        cursor.close();
        db.close();
        return null;
    }

    public void deleteWithPhone(String phone){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLENAME,"friendPhone = ?" ,new String[]{phone});
        db.close();
    }
}
