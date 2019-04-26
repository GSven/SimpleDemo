package com.gsven.library.base.callback;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.gsven.library.base.app.AppManager;
import com.gsven.library.utils.KLog;

/**
*
*@author Gsven
*@Date 2019/4/26 17:37
*@Desc 生命周期管理类
*/
public class BaseLifecycleCallback implements Application.ActivityLifecycleCallbacks{


    public static BaseLifecycleCallback getInstance() {
        return HolderClass.INSTANCE;
    }


    private final static class HolderClass {
        private final static BaseLifecycleCallback INSTANCE = new BaseLifecycleCallback();
    }


    private BaseLifecycleCallback() {}

    /**
     * 必须在 Application 的 onCreate 方法中调用
     */
    public void init(Application application) {
        application.registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        KLog.e("Activity生命周期","onActivityCreated");
        AppManager.getAppManager().addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        KLog.e("Activity生命周期","onActivityStarted");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        KLog.e("Activity生命周期","onActivityResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        KLog.e("Activity生命周期","onActivityPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        KLog.e("Activity生命周期","onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        KLog.e("Activity生命周期","onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        KLog.e("Activity生命周期","onActivityDestroyed");
        //将当前Activity移除到容器
        AppManager.getAppManager().removeActivity(activity);
    }

}
