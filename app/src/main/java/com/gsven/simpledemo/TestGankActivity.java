package com.gsven.simpledemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.gsven.library.arounter.ARouterConstant;
import com.gsven.library.base.BaseActivity;
import com.gsven.library.utils.Toasty;
import com.gsven.simpledemo.adapter.SimpleGankAdapter;
import com.gsven.simpledemo.bean.GankBeauty;
import com.gsven.simpledemo.bean.GankBeautyResult;
import com.gsven.simpledemo.network.Network;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@Route(path = ARouterConstant.ACTIVITY_APP_ACTIVITY_TEST_GANK)
public class TestGankActivity extends BaseActivity {
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private SimpleGankAdapter gankAdapter;
    private List<GankBeauty> list;

    private int currentNumber = 10;
    private int currentPage = 1;

    @Override
    public int getContentView() {
        return R.layout.activity_gank;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(_mActivity));
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(_mActivity));
        smartRefreshLayout.setEnableAutoLoadMore(true);
    }

    @Override
    protected void initViews() {
        list = new ArrayList<>();
        gankAdapter = new SimpleGankAdapter(list);
        recyclerView.setAdapter(gankAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        request();
    }


    @Override
    protected void initData() {
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                currentPage++;
                request();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                currentPage = 1;
                request();
            }
        });
    }

    private void request() {
        Network.getGankApi()
                .getBeauties(currentNumber, currentPage)
                .map(new Function<GankBeautyResult, List<GankBeauty>>() {
                    @Override
                    public List<GankBeauty> apply(GankBeautyResult gankBeautyResult) throws Exception {
                        return gankBeautyResult.beautyList;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<GankBeauty>>() {
                    @Override
                    public void accept(List<GankBeauty> gankBeauties) throws Exception {
                        if (currentPage == 1) {
                            gankAdapter.addData(gankBeauties);
                            gankAdapter.notifyDataSetChanged();
                            smartRefreshLayout.finishRefresh();
                        } else {
                            if (gankBeauties.size() >= currentNumber) {
                                List<GankBeauty> oldList = gankAdapter.getData();
                                int start = oldList.size();
                                int size = gankBeauties.size();
                                oldList.addAll(gankBeauties);
                                gankAdapter.addData(gankBeauties);
//                                gankAdapter.addData(oldList);
//                                gankAdapter.notifyItemRangeChanged(start, size);
                                smartRefreshLayout.finishLoadMore();
                            } else {
                                List<GankBeauty> oldList = gankAdapter.getData();
                                int start = oldList.size();
                                int size = gankBeauties.size();
                                oldList.addAll(gankBeauties);
                                gankAdapter.addData(gankBeauties);
//                                gankAdapter.addData(oldList);
//                                gankAdapter.notifyItemRangeChanged(start, size);
                                smartRefreshLayout.finishLoadMore();
//                                smartRefreshLayout.finishLoadMoreWithNoMoreData();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        smartRefreshLayout.finishRefresh();
                        smartRefreshLayout.finishLoadMore();
                        Toasty.normal(_mActivity, "异常了=" + throwable.getMessage());
                    }
                });


    }


    @Override
    protected void setListeners() {

    }
}
