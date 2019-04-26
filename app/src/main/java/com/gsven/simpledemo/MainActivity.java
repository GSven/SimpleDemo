package com.gsven.simpledemo;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.gsven.library.arounter.ARouterConstant;
import com.gsven.library.base.BaseActivity;
import com.gsven.library.utils.KLog;

@Route(path = ARouterConstant.ACTIVITY_ANDROID_ACTIVITY)
public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KLog.a("level a log");
        KLog.d("level d log");
        KLog.v("level v log");
        KLog.e("level e log");
    }
}
