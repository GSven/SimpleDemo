package com.gsven.simpledemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gsven.simpledemo.R;

import java.util.ArrayList;
import java.util.List;

public class SimpleTestAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context _mActivity;
    private List<String> list;

    public SimpleTestAdapter1(Context context) {
        this._mActivity = context;
    }

    public void setDatas(List<String> list) {
        if (list == null) {
            this.list = new ArrayList<>();
        } else {
            this.list = list;
        }
    }

    public List<String> getDatas() {
        return this.list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TypeStringViewHolder(
                LayoutInflater.from(_mActivity).inflate(
                        R.layout.app_item_simple_text, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof TypeStringViewHolder) {
            TypeStringViewHolder holder = (TypeStringViewHolder) viewHolder;
            holder.tvName.setText(this.list.get(i));
        }
    }

    class TypeStringViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public TypeStringViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }

    @Override
    public int getItemCount() {
        return this.list == null ? 0 : this.list.size();
    }
}
