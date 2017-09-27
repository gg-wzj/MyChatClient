package com.example.mychatclient.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.mychatclient.app.MyApplication;

/**
 * Created by wzj on 2017/9/23.
 */

public class SPUtil {


    public static boolean putString(String key, String data) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, data);
        boolean result = edit.commit();
        return result;
    }

    public static String getString(String key){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        return sharedPreferences.getString(key, "");
    }
}
