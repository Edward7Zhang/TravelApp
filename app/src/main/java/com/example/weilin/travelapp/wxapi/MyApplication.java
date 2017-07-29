package com.example.weilin.travelapp.wxapi;

import android.app.Application;

import org.xutils.BuildConfig;
import org.xutils.x;

/**
 * Created by hulinfa on 2017/1/4.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 开启debug会影响性能
    }
}
