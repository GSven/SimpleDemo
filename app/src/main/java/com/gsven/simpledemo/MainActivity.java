package com.gsven.simpledemo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.gsven.library.arounter.ARouterConstant;
import com.gsven.library.arounter.ARouterUtils;
import com.gsven.library.base.BaseActivity;
import com.gsven.library.utils.KLog;
import com.gsven.simpledemo.animations.Techniques;
import com.gsven.simpledemo.animations.YoYo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//@Route(path = ARouterConstant.ACTIVITY_APP_ACTIVITY)
public class MainActivity extends BaseActivity {
    TextView tvHello;
    TextView testRichEditor;
    @BindView(R.id.demo)
    TextView demo;
    @BindView(R.id.llBig)
    LinearLayout llBig;
    @BindView(R.id.llSmall)
    LinearLayout llSmall;
    @BindView(R.id.tvSmallSee)
    TextView tvSmallSee;
    @BindView(R.id.tvBigSee)
    TextView tvBigSee;
    int height;
    int width;

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        tvHello = findViewById(R.id.tvHello);
        testRichEditor = findViewById(R.id.testRichEditor);


        llBig.post(new Runnable() {
            @Override
            public void run() {
                height = llBig.getHeight();
                width = llBig.getWidth();
                llBig.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void initViews() {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.tvBigSee
            , R.id.tvSmallSee
            , R.id.testRichEditor
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.tvBigSee:
                llBig.setVisibility(View.GONE);
                llSmall.setVisibility(View.VISIBLE);
                break;
            case R.id.tvSmallSee:
                llSmall.setVisibility(View.GONE);
                llBig.setVisibility(View.VISIBLE);
                showBig();
                break;
            case R.id.testRichEditor:
                showPulse();
                break;
        }

    }

    private YoYo.YoYoString pulseRope;

    private void showPulse() {
        if (pulseRope != null) {
            pulseRope.stop(true);
        }
        rope = YoYo.with(Techniques.Pulse)
                .duration(1200)
                .repeat(YoYo.INFINITE)
                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                .interpolate(new AccelerateDecelerateInterpolator())
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(testRichEditor);
    }

    private YoYo.YoYoString rope;

    private void showBig() {
        if (rope != null) {
            rope.stop(true);
        }
        rope = YoYo.with(Techniques.BounceInDown)
                .duration(1200)
                .repeat(0)
                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                .interpolate(new AccelerateDecelerateInterpolator())
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(llBig);

    }


    @Override
    protected void setListeners() {
        tvHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_APP_ACTIVITY_TAOBAO_TWO);
            }
        });

        demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_APP_ACTIVITY_DEMO);
            }
        });
    }

    @Override
    protected void initData() {

    }
}
