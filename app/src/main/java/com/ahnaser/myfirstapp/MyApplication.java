package com.ahnaser.myfirstapp;

import android.app.Application;
import android.content.Context;

/**
 * Created by root on 23/05/15.
 */
public class MyApplication extends Application {
    public static final String API_KEY_SOUQ="EB008DQ5bnzmSZty8fyp";
    public static final String CLIENT_ID="38607576";
    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance=this;
    }

    public static MyApplication getInstance(){
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }
}
