package com.example.mychatclient.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mychatclient.app.MyApplication;
import com.example.mychatclient.db.CurrentUserDB;
import com.example.mychatclient.db.bean.FriendshipBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzj on 2017/9/22.
 */

public class FriendshipDao {

    private  CurrentUserDB dbHelper;
    private static final  String TABLENAME = CurrentUserDB.TABLE_FRIENDSHIP;

    public FriendshipDao(String name) {
        dbHelper = new CurrentUserDB(MyApplication.getContext(),name);
    }

    public boolean addFriendship(FriendshipBean bean){
        if(bean == null)
            return false;
        FriendshipBean oldBean = getInfoWithPhone(bean.getPhone());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(oldBean != null){
            db.delete(TABLENAME,"phone = ? ",new String[]{bean.getPhone()});
        }
        ContentValues values = new ContentValues();
        values.put(CurrentUserDB.FriendshipColum.PHONE,bean.getPhone());
        values.put(CurrentUserDB.FriendshipColum.AREA,bean.getArea());
        values.put(CurrentUserDB.FriendshipColum.NICK,bean.getNick());
        values.put(CurrentUserDB.FriendshipColum.REMARK,bean.getRemark());
        values.put(CurrentUserDB.FriendshipColum.SEX,bean.getSex());
        long result = db.insert(TABLENAME, null, values);
        db.close();
        return result != -1;
    }


    public FriendshipBean getInfoWithPhone(String phone){
        if(phone == null)
            return null;
        FriendshipBean bean =null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLENAME + " where phone =?", new String[]{phone});
        while (cursor.moveToNext()){
            String phoneNume = cursor.getString(1);
            String nick = cursor.getString(2);
            String area = cursor.getString(3);
            String remark = cursor.getString(4);
            int sex = cursor.getInt(5);
            bean = new FriendshipBean(phoneNume,nick,area,remark,sex);
        }
        cursor.close();
        db.close();
        return bean;
    }

    public List<FriendshipBean> findAll (){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        FriendshipBean bean =null;
        List<FriendshipBean> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLENAME, null);
        while (cursor.moveToNext()){
            String phoneNume = cursor.getString(1);
            String nick = cursor.getString(2);
            String area = cursor.getString(3);
            String remark = cursor.getString(4);
            int sex = cursor.getInt(5);
            bean = new FriendshipBean(phoneNume,nick,area,remark,sex);
            list.add(bean);
        }
        cursor.close();
        db.close();
        return list;
    }
}
