package com.example.lumen.texturedemo;

import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

/**
 * Created by lumen on 2017/9/22.
 */

public class HttpProxyUtils {
    private static Context mContext;
    private static class ServerHolder{
        private static final HttpProxyCacheServer INSTANCE = new HttpProxyCacheServer(mContext);
    }

    public static HttpProxyCacheServer getServer(){
        return ServerHolder.INSTANCE;
    }

    public static void init(Context context){
        mContext = context;
    }
}
