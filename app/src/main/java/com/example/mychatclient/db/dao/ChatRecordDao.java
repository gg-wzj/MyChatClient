package com.example.mychatclient.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.example.mychatclient.app.MyApplication;
import com.example.mychatclient.db.CurrentUserDB;
import com.example.mychatclient.db.bean.ChatRecordBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzj on 2017/9/22.
 */

public class ChatRecordDao {

    private  CurrentUserDB dbHelper;
    private static String mPhone = "12345";
    private static final String TABLENAME = CurrentUserDB.TABLE_CHATRECORD;
    public ChatRecordDao(String name) {
        dbHelper = new CurrentUserDB(MyApplication.getContext(),name);
    }


    public boolean addChatRecord(ChatRecordBean bean){
        if(bean == null)
            return false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CurrentUserDB.ChatRecordColum.PHONE,bean.getPhone());
        values.put(CurrentUserDB.ChatRecordColum.TYPE,bean.getType());
        values.put(CurrentUserDB.ChatRecordColum.MESSAGE,bean.getMessage());
        values.put(CurrentUserDB.ChatRecordColum.HASSEND,bean.getHasSend());
        values.put(CurrentUserDB.ChatRecordColum.TIME,bean.getTime());
        long result = db.insert(TABLENAME, null, values);
        db.close();
        return result != -1;
    }

    public List<ChatRecordBean> findPart(String phone, int offset, int count){
        List<ChatRecordBean> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
       /*select * from (select * from ChatRecord where phone = "12345" order by time desc)
            order by time desc limit 2,1 */
        Cursor cursor = db.rawQuery("select * from (select * from ChatRecord where phone = ? order by id desc) order by id  limit ?,?",
                new String[]{phone, (offset-1) + "", count + ""});

        ChatRecordBean bean;
        while (cursor.moveToNext()){
            bean = getFromCursor(cursor);
            list.add(bean);
        }
        cursor.close();
        db.close();
        return list;

    }

    public List<ChatRecordBean> findAll (String phone){
        List<ChatRecordBean> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLENAME+" where phone = ? ",
                new String[]{phone});

        ChatRecordBean bean;
        while (cursor.moveToNext()){
            bean = getFromCursor(cursor);
            list.add(bean);
        }
        cursor.close();
        db.close();
        return list;
    }

    public void deleteAll(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLENAME,null,null);
        db.close();
    }

    public boolean updateSendStatus(ChatRecordBean bean ){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CurrentUserDB.ChatRecordColum.HASSEND,1);
        int update = db.update(TABLENAME, values, "phone = ? and type = ? and message = ? and time = ?",
                new String[]{bean.getPhone(), bean.getType() + "", bean.getMessage(), bean.getTime() + ""});
        db.close();
        return update == 1;
    }

    public List<ChatRecordBean> findVerificationRecord(){
        List<ChatRecordBean> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLENAME + " where type = 2", null);
        ChatRecordBean bean;
        while (cursor.moveToNext()){
            bean = getFromCursor(cursor);
            list.add(bean);
        }
        cursor.close();
        db.close();
        return list;
    }

    @NonNull
    private ChatRecordBean getFromCursor(Cursor cursor) {
        ChatRecordBean bean;
        String ph = cursor.getString(1);
        int type = cursor.getInt(2);
        String message = cursor.getString(3);
        int hasSend = cursor.getInt(4);
        int time = cursor.getInt(5);
        bean = new ChatRecordBean(ph,type,message,hasSend,time);
        return bean;
    }

    public boolean addVertificationRecord(ChatRecordBean bean){

        ChatRecordBean oldBean = findVerificationRecord(bean);
        if(oldBean!= null)
            deleteVerifcation(bean);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CurrentUserDB.ChatRecordColum.PHONE,bean.getPhone());
        values.put(CurrentUserDB.ChatRecordColum.TYPE,bean.getType());
        values.put(CurrentUserDB.ChatRecordColum.MESSAGE,bean.getMessage());
        values.put(CurrentUserDB.ChatRecordColum.HASSEND,bean.getHasSend());
        values.put(CurrentUserDB.ChatRecordColum.TIME,bean.getTime());
        long result = db.insert(TABLENAME, null, values);
        db.close();
        return result != -1;

    }

    public ChatRecordBean findVerificationRecord(ChatRecordBean bean){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLENAME + " where type = 2 and phone = ?", new String[]{bean.getPhone()});
        ChatRecordBean resultBean = null;
        while (cursor.moveToNext()){
            resultBean = getFromCursor(cursor);
        }
        cursor.close();
        db.close();
        return resultBean;
    }

    public void deleteVerifcation(ChatRecordBean bean){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLENAME,"phone = ? and type = 2",new String[]{bean.getPhone()});
        db.close();
    }
}
