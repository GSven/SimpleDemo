package com.gsven.simpledemo;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gsven.library.arounter.ARouterConstant;
import com.gsven.library.arounter.ARouterUtils;
import com.gsven.library.base.BaseTitleActivity;
import com.gsven.library.utils.KLog;
import com.gsven.simpledemo.adapter.SimpleDemoAdapter;
import com.gsven.simpledemo.adapter.SimpleTestAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = ARouterConstant.ACTIVITY_APP_ACTIVITY_DEMO)
public class DemoActivity extends BaseTitleActivity {
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private List<ActivityInfo> datas;
    private SimpleDemoAdapter adapter;

    @Override
    public int getContentView() {
        return R.layout.activity_demo;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setEnableLoadMore(false);
    }

    @Override
    protected void initViews() {
        datas = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                for (ActivityInfo activityInfo : packageInfo.activities) {
                    Class<?> clazz = Class.forName(activityInfo.name);
//                    KLog.e("clazz" + clazz + ";name=" + activityInfo.name + ";packn=" + activityInfo.packageName);
                    if (!TextUtils.isEmpty(activityInfo.name) && !TextUtils.isEmpty(activityInfo.packageName)) {
                        if (activityInfo.name.contains(activityInfo.packageName)) {
                            datas.add(activityInfo);
                        }
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        adapter = new SimpleDemoAdapter(datas);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!TextUtils.isEmpty(datas.get(position).name)) {
                    String[] names = datas.get(position).name.split("\\.");
                    ARouterUtils.navigation(ARouterConstant.ACTIVITY_APP_ACTIVITY + names[names.length - 1]);
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListeners() {

    }
}
