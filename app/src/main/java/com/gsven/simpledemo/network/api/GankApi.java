package com.gsven.simpledemo.network.api;

import com.gsven.simpledemo.bean.GankBeautyResult;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GankApi {
    @GET("data/福利/{number}/{page}")
    io.reactivex.Observable<GankBeautyResult> getBeauties(@Path("number") int number, @Path("page") int page);
}
