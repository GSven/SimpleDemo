package com.gsven.simpledemo;

import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.gsven.library.arounter.ARouterConstant;
import com.gsven.library.base.BaseActivity;
import com.gsven.library.base.BaseTitleActivity;
import com.gsven.library.utils.StatusBarUtil;
import com.gsven.library.utils.Toasty;
import com.gsven.simpledemo.adapter.SimpleTestAdapter;
import com.gsven.simpledemo.adapter.SimpleTestAdapter1;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.OnTwoLevelListener;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.header.TwoLevelHeader;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gsven
 * @Date 2019/4/26 19:45
 * @Desc 淘宝二楼实现
 */
@Route(path = ARouterConstant.ACTIVITY_APP_ACTIVITY_TAOBAO_TWO)
public class TaoBaoLevelTwoActivity extends BaseActivity {
    SmartRefreshLayout smartRefreshLayout;
    TwoLevelHeader header;
    TextView pullrefreshBg;
    LinearLayout twoLevelContent;
    RecyclerView recyclerview;
    ClassicsHeader classics;
    Toolbar toolbar;


    @Override
    public int getContentView() {
        return R.layout.app_activity_taobao_level_two;
    }

    @Override
    protected void findViews() {
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        header = findViewById(R.id.header);
        pullrefreshBg = findViewById(R.id.pullrefreshBg);
        twoLevelContent = findViewById(R.id.twoLevelContent);
        recyclerview = findViewById(R.id.recyclerview);
        classics = findViewById(R.id.classics);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void initViews() {

    }

    List<String> datas;
//    SimpleTestAdapter1 adapter;
    SimpleTestAdapter adapter;

    private List<String> getList() {
        List<String> list = new ArrayList<>();
        for (int i = (page - 1) * size + 1; i <= page * size; i++) {
            list.add("test" + i);
        }
        return list;
    }

    @Override
    protected void initData() {
        datas = new ArrayList<>();
        datas.clear();
        datas.addAll(getList());
//        adapter = new SimpleTestAdapter1(_mActivity);
//        adapter.setDatas(datas);

        adapter = new SimpleTestAdapter(datas);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayout.VERTICAL, false));
    }

    private int page = 1;
    private int size = 20;

    @Override
    protected void setListeners() {
        smartRefreshLayout.setEnableAutoLoadMore(true);
        smartRefreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                Toasty.normal(_mActivity, "触发加载事件").show();
                refreshLayout.finishLoadMore(100);
                page++;
//                datas.addAll(getList());
//                adapter.notifyItemRangeChanged(adapter.getDatas().size(), size);

                int start = adapter.getData().size();
                adapter.addData(getList());
                adapter.notifyItemRangeChanged(start,size);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Toasty.normal(_mActivity, "触发刷新事件").show();
                refreshLayout.finishRefresh(100);
                page = 1;
                datas.clear();
                datas.addAll(getList());
//                adapter.setDatas(datas);

                adapter.replaceData(datas);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                toolbar.setAlpha(1 - Math.min(percent, 1));
                pullrefreshBg.setTranslationY(Math.min(offset - pullrefreshBg.getHeight() + toolbar.getHeight(),
                        smartRefreshLayout.getLayout().getHeight() - pullrefreshBg.getHeight()));
            }
        });


        header.setOnTwoLevelListener(new OnTwoLevelListener() {
            @Override
            public boolean onTwoLevel(@NonNull RefreshLayout refreshLayout) {
                Toasty.normal(_mActivity, "触发二楼事件").show();
                twoLevelContent.animate().alpha(1).setDuration(100);
//                refreshLayout.getLayout().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        header.finishTwoLevel();
//                        twoLevelContent.animate().alpha(0).setDuration(100);
//                    }
//                }, 1000);
                return true;//true 将会展开二楼状态 false 关闭刷新
            }
        });
    }


}
