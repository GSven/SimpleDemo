package com.gsven.library.base.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import com.bumptech.glide.Glide;
import com.gsven.library.arounter.ARouterUtils;
import com.gsven.library.constants.Config;
import com.gsven.library.utils.KLog;


/**
 * @author Gsven
 * @Date 2019/4/26 17:23
 * @Desc
 */
public class LibApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Config.getInstance().initConfig(this);
        //在子线程中初始化
        InitializeService.start(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        KLog.d("Application", "onTerminate");
        super.onTerminate();
        ARouterUtils.destroy();
    }


    /**
     * 低内存的时候执行
     */
    @Override
    public void onLowMemory() {
        KLog.d("Application", "onLowMemory");
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }


    /**
     * HOME键退出应用程序
     * 程序在内存清理的时候执行
     */
    @Override
    public void onTrimMemory(int level) {
        KLog.d("Application", "onTrimMemory");
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory();
        }
        Glide.get(this).trimMemory(level);
    }


    /**
     * onConfigurationChanged
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        KLog.d("Application", "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }


}
