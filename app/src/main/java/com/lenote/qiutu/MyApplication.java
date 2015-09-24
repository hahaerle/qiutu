package com.lenote.qiutu;

import android.app.Application;

/**
 * Created by lenote on 2015/9/8.
 */
public class MyApplication extends Application {
    static MyApplication sInstance;
    public static MyApplication getInstance(){
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance=this;
    }

    public void logout() {
        //TODO 退出登录
    }
}
