package com.gsven.library.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.gsven.library.R;
import com.gsven.library.utils.KLog;
import com.gsven.library.utils.StatusBarUtil;

public abstract class BaseActivity extends ABaseActivity {

    protected Context _mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _mActivity = this;
        if (isDarkStatus()) {
            StatusBarUtil.setStatusBarMode(this,
                    true,
                    ContextCompat.getColor(this, R.color.white));
        }

        setContentView(getContentView());
        findViews();
        initViews();
        initData();
        setListeners();
    }

    /**
     * 返回一个用于显示界面的布局id
     *
     * @return 视图id
     */
    public abstract int getContentView();

    protected abstract void findViews();

    protected abstract void initViews();

    protected abstract void initData();

    protected abstract void setListeners();


    /**
     * 设置全屏滑动渐变的时候用到，参考ThinkiveTool.execToolBarOperation(...)方法
     */
    public void setTranslucentStatus() {
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarColor(this,
                ContextCompat.getColor(this, R.color.colorTransparent));
    }

    /**
     * 设置全屏滑动渐变的时候用到，参考ThinkiveTool.execToolBarOperation(...)方法
     */
    public void setDarkStatus() {
        StatusBarUtil.setStatusBarMode1(this, true);
    }

    /**
     * 默认是白色状态栏，暗色状态字体
     *
     * @return
     */
    protected boolean isDarkStatus() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 播放器横竖屏隐藏导航栏机型
     *
     * @return
     */
    protected boolean isStrangePhone() {
        boolean strangePhone = "mx5".equalsIgnoreCase(Build.DEVICE)
                || "Redmi Note2".equalsIgnoreCase(Build.DEVICE)
                || "Z00A_1".equalsIgnoreCase(Build.DEVICE)
                || "hwH60-L02".equalsIgnoreCase(Build.DEVICE)
                || "hermes".equalsIgnoreCase(Build.DEVICE)
                || ("V4".equalsIgnoreCase(Build.DEVICE) && "Meitu".equalsIgnoreCase(Build.MANUFACTURER))
                || ("m1metal".equalsIgnoreCase(Build.DEVICE) && "Meizu".equalsIgnoreCase(Build.MANUFACTURER));

        KLog.e("lfj1115 ", " Build.Device = " + Build.DEVICE + " , isStrange = " + strangePhone);
        return strangePhone;
    }

}
