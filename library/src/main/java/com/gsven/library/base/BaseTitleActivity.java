package com.gsven.library.base;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gsven.library.R;
import com.gsven.library.interfaces.IBackListener;
import com.gsven.library.utils.KeyboardUtil;
import com.gsven.library.utils.ScreenUtil;
import com.gsven.library.utils.StatusBarUtil;
import com.gsven.library.utils.TextSpanUtil;
import com.gsven.library.utils.Toasty;
import com.gsven.library.utils.enums.Direction;

public abstract class BaseTitleActivity extends ABaseActivity {
    protected Context _mActivity;
    LinearLayout llRootView;//总布局
    Toolbar tb_base_invest;//标题栏总布局
    LinearLayout fl_tb_back;//返回按钮区域
    ImageView iv_tb_back;//返回按钮
    TextView tv_tb_back;//副返回
    TextView tv_tb_title;//主标题
    TextView tv_tb_sub_title;//副标题
    TextView tv_tb_right;//右侧文字描述
    ImageView iv_tb_right;//右侧图片按钮
    protected int activityNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(LayoutInflater.from(this).inflate(R.layout.include_activity_title_bar, null));
        StatusBarUtil.setStatusBarMode(this, true, ContextCompat.getColor(this, R.color.white));
        getActivityNum();
        initTitleView();
        _mActivity = this;
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
     * 白色标题栏
     *
     * @return
     */
    protected boolean darkToolbarText() {
        return false;
    }


    private void initTitleView() {
        llRootView = (LinearLayout) findViewById(R.id.llRootView);
        tb_base_invest = (Toolbar) findViewById(R.id.tb_base_invest);
        fl_tb_back = (LinearLayout) findViewById(R.id.fl_tb_back);
        iv_tb_back = (ImageView) findViewById(R.id.iv_tb_back);
        tv_tb_back = (TextView) findViewById(R.id.tv_tb_back);
        tv_tb_title = (TextView) findViewById(R.id.tv_tb_title);
        tv_tb_sub_title = (TextView) findViewById(R.id.tv_tb_sub_title);
        tv_tb_right = (TextView) findViewById(R.id.tv_tb_right);
        iv_tb_right = (ImageView) findViewById(R.id.iv_tb_right);


        //将toolbar背景设置为白色，其他标题，返回键等等设置为暗色样式
        if (darkToolbarText()) {
            if (tb_base_invest != null) {
                tb_base_invest.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                iv_tb_back.setImageResource(R.mipmap.lib_toolbar_back); //返回键
                tv_tb_title.setTextColor(ContextCompat.getColor(this, R.color.black9)); //主标题
                tv_tb_sub_title.setTextColor(ContextCompat.getColor(this, R.color.black9));   //副标题
                tv_tb_right.setTextColor(ContextCompat.getColor(this, R.color.black9)); //右侧完成
            }
        }
        addBackHome();
        if (darkToolbarText()) {
            updateBackHomeIcon(R.mipmap.lib_x_gray);
        }


        iv_tb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tv_tb_title.setText(setActiviyTitle());
    }

    protected CharSequence setActiviyTitle() {
        return "";
    }

    /**
     * 获取标题栏模块
     *
     * @return
     */
    protected Toolbar getToolBar() {
        return tb_base_invest;
    }

    /**
     * 更新一键关闭按钮图标
     * 有黑色样式x R.mipmap.lib_x_gray
     *
     * @param resId 图片地址
     */
    public void updateBackHomeIcon(int resId) {
        if (activityNum >= 3) {
            if (tv_tb_back != null) {
                TextSpanUtil.setTextDrawable(tv_tb_back, ContextCompat.getDrawable(this, resId), 0);
                tv_tb_back.setWidth((int) ScreenUtil.dpToPx(this, 30));
                tv_tb_back.setHeight((int) ScreenUtil.dpToPx(this, 30));
            }
        }
    }

    /**
     * 任务栈满3级需要在标题栏添加一键返回按钮
     * 全屏的的交给子类
     */

    public void addBackHome() {
        if (activityNum >= 3) {
            //添加一键返回操作
            if (tv_tb_back != null) {
                TextSpanUtil.setTextDrawable(tv_tb_back, ContextCompat.getDrawable(this, R.mipmap.lib_toolbar_one_back), Direction.LEFT);
                tv_tb_back.setWidth((int) ScreenUtil.dpToPx(this, 35));
                tv_tb_back.setHeight((int) ScreenUtil.dpToPx(this, 35));
                tv_tb_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        KeyboardUtil.hideSystemKeyBoard(BaseTitleActivity.this);
                        Toasty.normal(BaseTitleActivity.this, "一键返回到主页面").show();
                        if (mBackListener != null) {
                            mBackListener.onBackListener(v);
                        }
                    }
                });
            }
        }
    }

    private IBackListener mBackListener;

    public void setmBackListener(IBackListener mBackListener) {
        this.mBackListener = mBackListener;
    }

    private void getActivityNum() {
        // 获取activity任务栈
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        activityNum = info.numActivities;
    }


    /**
     * 此方法重要
     *
     * @param layoutResID
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        this.setContentView(view);
    }

    /**
     * 此方法重要
     *
     * @param view
     */
    @Override
    public void setContentView(View view) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        if (null != llRootView) {
            llRootView.addView(view, lp);
        }
    }


}
