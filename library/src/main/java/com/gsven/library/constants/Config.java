package com.gsven.library.constants;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.Utils;
import com.gsven.library.base.callback.BaseLifecycleCallback;


/**
 * @author Gsven
 * @Date 2019/4/26 14:31
 * @Desc 基础配置常量
 */
public class Config {
    private static Config instance;

    private Config() {
    }

    public static Config getInstance() {
        if (instance == null) {
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config();
                }
            }
        }
        return instance;
    }


    public static boolean DEBUG = true;//是否debug模式

    public void initConfig(Application application) {
        Utils.init(application);
        BaseLifecycleCallback.getInstance().init(application);
        initARouter();
    }


    private void initARouter() {
        if (DEBUG) {
            //打印日志
            ARouter.openLog();
            //开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
        }
        //推荐在Application中初始化
        ARouter.init(Utils.getApp());
    }

}
