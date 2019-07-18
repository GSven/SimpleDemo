package com.gsven.simpledemo.utils;

import com.gsven.simpledemo.bean.GankBeauty;
import com.gsven.simpledemo.bean.GankBeautyResult;

import java.util.List;

import io.reactivex.functions.Function;

public class GankBeautyResultToItemsMapper implements Function<GankBeautyResult, List<GankBeauty>> {
    @Override
    public List<GankBeauty> apply(GankBeautyResult gankBeautyResult) {
        List<GankBeauty> gankBeauties = gankBeautyResult.beautyList;
        return gankBeauties;
    }
}
