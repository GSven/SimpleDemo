package com.gsven.simpledemo.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gsven.simpledemo.R;

import java.util.List;

public class SimpleTestAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public SimpleTestAdapter(List<String> datas) {
        super(R.layout.app_item_simple_text, datas);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tvName, item);
    }
}
