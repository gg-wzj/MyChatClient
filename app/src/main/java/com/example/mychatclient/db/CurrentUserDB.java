package com.example.mychatclient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wzj on 2017/9/22.
 */

public class CurrentUserDB extends SQLiteOpenHelper {

    public static final String TABLE_RECENT_MESSAGE = "RecentMessage";
    public static final String TABLE_FRIENDSHIP = "Friendship";
    public static final String TABLE_CHATRECORD = "ChatRecord";

    public static class RecentMessageColum{
        public static final String FRIEND_PHONE = "friendPhone";
        public static final String REMARK = "remark";
        public static final String MESSAGE = "message";
        public static final String WDCOUNT = "wdCount";
        public static final String TIME = "time";
    }

    public static class FriendshipColum{
        public static final String PHONE = "phone";
        public static final String NICK = "nick";
        public static final String AREA = "area";
        public static final String REMARK = "remark";
        public static final String SEX = "sex";
    }
    public static class ChatRecordColum{
        public static final String PHONE = "phone";
        public static final String TYPE = "type";
        public static final String MESSAGE = "message";
        public static final String HASSEND = "hasSend";
        public static final String TIME = "time";
    }
    //创建最近消息数据库表
    private static final String CREATE_RECENTMESSAGE = "create table " +TABLE_RECENT_MESSAGE+"(" +
            "id integer primary key autoincrement," +
            "friendPhone varchar," +
            "remark varchar," +
            "message text," +
            "wdCount integer," +
            "time integer)";
    //创建朋友列表数据库表
    private static final String CREATE_FRIENDSHIP = "create table " +TABLE_FRIENDSHIP+"(" +
            "id integer primary key autoincrement," +
            "phone varchar," +
            "nick varchar," +
            "area varchar," +
            "remark varchar," +
            "sex integer(1))";
    private static final String CREATE_CHATRECORD = "create table " +TABLE_CHATRECORD+"(" +
            "id integer primary key autoincrement," +
            "phone varchar," +
            "type integer(1)," +
            "message text," +
            "hasSend integer(1)," +
            "time integer)";


    public CurrentUserDB(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RECENTMESSAGE);
        db.execSQL(CREATE_FRIENDSHIP);
        db.execSQL(CREATE_CHATRECORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
