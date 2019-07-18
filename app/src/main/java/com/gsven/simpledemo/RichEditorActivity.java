package com.gsven.simpledemo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gsven.library.arounter.ARouterConstant;
import com.gsven.library.base.BaseActivity;
import com.gsven.library.base.app.PermissionManager;
import com.gsven.library.utils.ImageLoadUtils;
import com.gsven.library.utils.KLog;
import com.gsven.library.utils.Toasty;
import com.gsven.library.utils.image.Glide4Engine;
import com.gsven.library.utils.image.ImageUtils;
import com.gsven.simpledemo.base.ConstantsCode;
import com.gsven.simpledemo.utils.FlipCardAnimation;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.listener.OnCheckedListener;
import com.zhihu.matisse.listener.OnSelectedListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

@Route(path = ARouterConstant.ACTIVITY_APP_ACTIVITY_RICHEDITOR)
public class RichEditorActivity extends BaseActivity {
    @BindView(R.id.ivInsertImage)
    ImageView ivInsertImage;
    @BindView(R.id.ivTest)
    ImageView ivTest;


    @Override
    public int getContentView() {
        return R.layout.activity_rich_editor;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {


    }


    FlipCardAnimation flipCardAnimation;

    @Override
    protected void initData() {

        ivInsertImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation(flipCardAnimation, ivTest);
            }
        });
    }

    private void startAnimation(FlipCardAnimation animation, View view) {
        if (animation != null) {
            animation.setCanContentChange();
            view.startAnimation(animation);
        } else {
            int width = view.getWidth() / 2;
            int height = view.getHeight();
            animation = new FlipCardAnimation(0, 90, width, height);
            animation.setInterpolator(new AnticipateOvershootInterpolator());
            animation.setDuration(3000);
            animation.setFillAfter(false);
//            animation.setRepeatCount(-1);       //循环翻转
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
//                    ((FlipCardAnimation)animation).setCanContentChange();   //循环翻转

                }

            });
            view.startAnimation(animation);
        }
    }


    @Override
    protected void setListeners() {
//        ivInsertImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RxPermissions rxPermissions = new RxPermissions((FragmentActivity) _mActivity);
//                rxPermissions.request(PermissionManager.WRITE_EXTERNAL_STORAGE)
//                        .safeSubscribe(new Observer<Boolean>() {
//                            @Override
//                            public void onSubscribe(Disposable d) {
//
//                            }
//
//                            @Override
//                            public void onNext(Boolean aBoolean) {
//                                if (aBoolean) {
//                                    Matisse.from((Activity) _mActivity)
//                                            .choose(MimeType.ofAll(), false)
//                                            .theme(R.style.Matisse_Dracula)
//                                            .countable(true)
//                                            .capture(true)
//                                            .captureStrategy(new CaptureStrategy(true, "com.gsven.simpledemo.fileprovider"))
//                                            .maxSelectable(1)
////                                            .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                                            .gridExpectedSize(
//                                                    getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
//                                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
//                                            .thumbnailScale(0.85f)
//                                            .imageEngine(new Glide4Engine())    // for glide-V4
//                                            .setOnSelectedListener(new OnSelectedListener() {
//                                                @Override
//                                                public void onSelected(
//                                                        @NonNull List<Uri> uriList, @NonNull List<String> pathList) {
//                                                    // DO SOMETHING IMMEDIATELY HERE
//                                                    KLog.e("onSelected", "onSelected: pathList=" + pathList);
//
//                                                }
//                                            })
//                                            .originalEnable(true)
//                                            .maxOriginalSize(10)
//                                            .setOnCheckedListener(new OnCheckedListener() {
//                                                @Override
//                                                public void onCheck(boolean isChecked) {
//                                                    // DO SOMETHING IMMEDIATELY HERE
//                                                    KLog.e("isChecked", "onCheck: isChecked=" + isChecked);
//                                                }
//                                            })
//                                            .forResult(ConstantsCode.REQUEST_CODE_CHOOSE);
//
//
//                                } else {
//                                    Toasty.normal(_mActivity, "未赋予权限").show();
//                                }
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//
//                            }
//
//                            @Override
//                            public void onComplete() {
//
//                            }
//                        });
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case ConstantsCode.REQUEST_CODE_CHOOSE:
                    KLog.e("uri=" + Matisse.obtainResult(data) + ";url=" + Matisse.obtainPathResult(data));

                    ImageLoadUtils.load(_mActivity, Matisse.obtainPathResult(data).get(0), new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            Bitmap originalBitmap = ImageUtils.drawableToBitmap(resource);
                            if (originalBitmap != null) {
//                              ImageUtils.drawableToBitmap(resource),
//                                        Matisse.obtainPathResult(data).get(0);
//                                Uri originalUri = data.getData(); // 获得图片的uri
//                                String path = Utils.getRealPathFromUri(_mActivity,originalUri);
                            } else {
                                Toasty.normal(_mActivity, "获取图片失败").show();
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
                    break;
            }
        }
    }
}
