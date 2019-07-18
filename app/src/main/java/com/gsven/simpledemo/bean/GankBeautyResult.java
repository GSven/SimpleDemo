package com.gsven.simpledemo.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GankBeautyResult {
    public boolean error;
    public @SerializedName("results") List<GankBeauty> beautyList;
}
