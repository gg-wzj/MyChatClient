package com.example.mychatclient.activity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzj on 2017/9/30.
 */

public class ActivityController {
    private static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void  finishAll(){
        for (Activity activity : activities) {
           activity.finish();
            activities.remove(activity);
        }
    }

    public static void finish(Activity activity){
        activity.finish();
        activities.remove(activity);
    }
}
