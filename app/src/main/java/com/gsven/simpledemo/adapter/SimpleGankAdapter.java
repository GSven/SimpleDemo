package com.gsven.simpledemo.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gsven.library.utils.ImageLoadUtils;
import com.gsven.simpledemo.R;
import com.gsven.simpledemo.bean.GankBeauty;

import java.util.List;

public class SimpleGankAdapter extends BaseQuickAdapter<GankBeauty, BaseViewHolder> {


    public SimpleGankAdapter(@Nullable List<GankBeauty> data) {
        super(R.layout.app_item_simple_gank, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GankBeauty item) {
        ImageView ivMeizi = helper.itemView.findViewById(R.id.ivMeizi);
        ImageLoadUtils.load(mContext, item.getUrl(), ivMeizi);
    }
}
