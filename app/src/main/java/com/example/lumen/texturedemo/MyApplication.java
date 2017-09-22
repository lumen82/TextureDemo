package com.example.lumen.texturedemo;

import android.app.Application;

/**
 * Created by lumen on 2017/9/22.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HttpProxyUtils.init(this);
    }
}
