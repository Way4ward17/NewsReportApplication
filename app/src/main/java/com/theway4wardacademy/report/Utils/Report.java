package com.theway4wardacademy.report.Utils;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;



public class Report extends Application {
    private HttpProxyCacheServer proxy;

    @Override
    public void onCreate() {
        super.onCreate();


      }

    public static HttpProxyCacheServer getProxy(Context context) {
        Report app = (Report) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);

    }


}

