package com.gsven.simpledemo.network;

import com.gsven.library.utils.KLog;
import com.gsven.simpledemo.network.api.GankApi;

import java.net.URLDecoder;
import java.net.URLEncoder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    private static final String BASE_URL_GANK = "http://gank.io/api/";
    private static GankApi gankApi;
    private static OkHttpClient okHttpClient;
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();

    static {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                try {
                    String text = URLDecoder.decode(message, "utf-8");
                    KLog.e(text);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    public static GankApi getGankApi() {
        if (gankApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL_GANK)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            gankApi = retrofit.create(GankApi.class);
        }
        return gankApi;
    }

}
