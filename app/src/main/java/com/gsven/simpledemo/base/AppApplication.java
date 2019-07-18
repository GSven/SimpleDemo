package com.gsven.simpledemo.base;

import com.gsven.library.base.app.LibApplication;
import com.gsven.library.constants.Config;

public class AppApplication extends LibApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //每一个都组件都需要调用
        Config.getInstance().initConfig(this);
    }
}
