package com.gsven.library.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gsven.library.R;

/**
 * @author Gsven
 * @Date 2019/5/9 16:36
 * @Desc 图片加载中转工具，方便以后替换图片加载框架
 */
public class ImageLoadUtils {
    //    public static void load(Context context, String url, int placeResId, int errorResId, ImageView view, CustomTarget customTarget) {
//        Glide.with(context)
//                .load(url)
//                .placeholder(placeResId)
//                .error(errorResId)
//                .into(new CustomTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                        ViewGroup.LayoutParams params = view.getLayoutParams();
//                        float width;
//                        float height;
//                        if (resource.getIntrinsicWidth() > 300) {
//                            if (resource.getIntrinsicWidth() >= ScreenUtil.getScreenWidth(context)) {
//                                float downScale = (float) resource.getIntrinsicWidth() / ScreenUtil.getScreenWidth(context);
//                                width = (float) resource.getIntrinsicWidth() / downScale;
//                                height = (float) resource.getIntrinsicHeight() / downScale;
//                            } else {
//                                float multiplier = ScreenUtil.getScreenWidth(context) / resource.getIntrinsicWidth();
//                                width = (float) resource.getIntrinsicWidth() * multiplier;
//                                height = (float) resource.getIntrinsicHeight() * multiplier;
//                            }
//                        } else {
//                            width = resource.getIntrinsicWidth();
//                            height = resource.getIntrinsicHeight();
//                        }
//                        params.width = (int) width;
//                        params.height = (int) height;
//                        if (resource instanceof GifDrawable) {
//                            GifDrawable gifDrawable = (GifDrawable) resource;
//                            view.setImageDrawable(gifDrawable);
//                            gifDrawable.setLoopCount(GifDrawable.LOOP_FOREVER);
//                            gifDrawable.start();
//                        } else {
//                            view.setImageDrawable(resource);
//                        }
//
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                    }
//
//                    @Override
//                    public void onLoadStarted(@Nullable Drawable placeholder) {
//                        super.onLoadStarted(placeholder);
//                        view.setImageDrawable(placeholder);
//                    }
//
//                    @Override
//                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                        super.onLoadFailed(errorDrawable);
//                        view.setImageDrawable(errorDrawable);
//                    }
//                })
//
//        ;
//    }
    public static void load(Context context, String url, int placeResId, int errorResId, CustomTarget customTarget) {
        Glide.with(context)
                .load(url)
                .placeholder(placeResId)
                .error(errorResId)
                .into(customTarget);
    }

    public static void load(Context context, String url, int placeResId, int errorResId, ImageView view) {
        Glide.with(context)
                .load(url)
                .placeholder(placeResId)
                .error(errorResId)
                .into(view);
    }

    public static void load(Context context, String url, ImageView imageView) {
        load(context, url, R.mipmap.lib_load_place, R.mipmap.lib_load_error, imageView);
    }

    public static void load(Context context, String url, CustomTarget<Drawable> customTarget) {
        load(context, url, R.mipmap.lib_load_place, R.mipmap.lib_load_error, customTarget);
    }
}
