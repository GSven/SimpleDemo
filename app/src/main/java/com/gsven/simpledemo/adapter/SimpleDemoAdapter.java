package com.gsven.simpledemo.adapter;

import android.content.pm.ActivityInfo;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gsven.simpledemo.R;

import java.util.List;

public class SimpleDemoAdapter extends BaseQuickAdapter<ActivityInfo, BaseViewHolder> {


    public SimpleDemoAdapter(@Nullable List<ActivityInfo> data) {
        super(R.layout.app_item_simple_demo, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActivityInfo item) {
        if (!TextUtils.isEmpty(item.name)) {
            String names[] = item.name.split("\\.");
            helper.setText(R.id.tvName, names[names.length - 1]);
        }
        helper.setText(R.id.tvPath, item.name);
    }
}
