package com.thinkive.android.thinkive_invest.ui.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thinkive.android.lczq_invest.R;
import com.thinkive.android.thinkive_invest.beans.DyAction;
import com.thinkive.android.thinkive_invest.ui.view.Tab.widget.MsgView;
import com.thinkive.android.thinkive_invest.ui.widget.seekbars.SignUtils;
import com.thinkive.android.thinkive_invest.utils.KLog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class GiftView extends LinearLayout {
    private static final long TIME_AUTO_POLL_1 = 5000;
    private LayoutInflater inflater;
    private AutoPollTask autoPollTask;
    private int mMaxCounts = 4;

    public GiftView(Context context) {
        this(context, null);
    }

    public GiftView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GiftView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflater = LayoutInflater.from(context);
        autoPollTask = new AutoPollTask(this);
        //底部 并且 水平居中
        setOrientation(VERTICAL);
        setGravity(Gravity.BOTTOM);

        //添加移除动画
        LayoutTransition mLayoutTransition = new LayoutTransition();
        mLayoutTransition.setAnimator(LayoutTransition.APPEARING, getAppearingAnimation());
        mLayoutTransition.setDuration(LayoutTransition.APPEARING, 300);
        mLayoutTransition.setStartDelay(LayoutTransition.APPEARING, 0);//源码中带有默认300毫秒的延时，需要移除，不然view添加效果不好！！
        mLayoutTransition.setAnimator(LayoutTransition.DISAPPEARING, getDisappearingAnimation());
        mLayoutTransition.setDuration(LayoutTransition.DISAPPEARING, 200);
        setLayoutTransition(mLayoutTransition);
    }

    private Animator getAppearingAnimation() {
        AnimatorSet mSet = new AnimatorSet();
        mSet.playTogether(
                ObjectAnimator.ofFloat(null, View.ALPHA, 0.0f, 1.0f),
                ObjectAnimator.ofFloat(null, View.TRANSLATION_Y, 100, 0));
        return mSet;
    }

    private Animator getDisappearingAnimation() {
        AnimatorSet mSet = new AnimatorSet();
        mSet.playTogether(
                ObjectAnimator.ofFloat(null, View.ALPHA, 1.0f, 0.0f),
                ObjectAnimator.ofFloat(null, View.TRANSLATION_Y, 0, -100));
        return mSet;
    }

    @Override
    protected void onDetachedFromWindow() {
        stop();
        super.onDetachedFromWindow();
    }

    static class AutoPollTask implements Runnable {
        private final WeakReference<GiftView> mReference;

        public AutoPollTask(GiftView refrence) {
            this.mReference = new WeakReference<>(refrence);
        }

        @Override
        public void run() {
            final GiftView giftView = mReference.get();
            if (giftView != null) {
                if (giftView.getChildCount() >= giftView.mMaxCounts) {
                    giftView.removeViewAt(0);
                }

                giftView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (giftView.running) {
                            giftView.addGift();
                        }
                        if (giftView.mEmptyIndex < giftView.mMaxCounts) {
                            giftView.postDelayed(giftView.autoPollTask, TIME_AUTO_POLL_1);
                        }
                    }
                }, 200);
            }
        }
    }

    private boolean running; //标示是否正在自动轮询

    //先停止再开始
    public void start() {
        mEmptyIndex = 0;
        if (running)
            stop();
        running = true;
        if (dyActionList.size() > 0) {
            postDelayed(autoPollTask, TIME_AUTO_POLL_1);
        }
    }

    public void stop() {
        mEmptyIndex = 0;
        running = false;
        removeAllViews();
        removeCallbacks(autoPollTask);
    }

    public void pause() {
        running = false;
    }

    public void wake() {
        running = true;
    }

    private int mCurrentIndex;
    private int mEmptyIndex;

    private List<DyAction> dyActionList = new ArrayList<>();

    public void setDatas(List<DyAction> list) {
        KLog.e("list=" + list);
        mCurrentIndex = 0;
        dyActionList.clear();
        removeAllViews();
        if (list != null) {
            this.dyActionList.addAll(list);
        }
    }

    private int colors[] = new int[]{R.color.color_red33,
            R.color.color_red65,
            R.color.color_red64,
            R.color.color_red66
    };

    private boolean mLoopEnable;

    public void setmLoopEnable(boolean enable) {
        this.mLoopEnable = enable;
    }

    private void addGift() {
        if (dyActionList.size() > 0) {
            if (mLoopEnable) {
                mCurrentIndex = mCurrentIndex % this.dyActionList.size();
                addItem();
                mCurrentIndex++;

            } else {
                if (mCurrentIndex < this.dyActionList.size()) {
                    addItem();
                    mCurrentIndex++;
                } else {
                    //添加空的
                    if (mEmptyIndex < mMaxCounts) {
                        addEmptyItem();
                        mEmptyIndex++;
                    }
                }
            }
        }
    }

    private void addItem() {
        DyAction item = dyActionList.get(mCurrentIndex);
        View itemView = inflater.inflate(R.layout.item_bk_douyin_comment, this, false);
        MsgView mvsView = itemView.findViewById(R.id.mvsView);
        mvsView.setBackgroundColor(ContextCompat.getColor(getContext(), colors[mCurrentIndex % colors.length]));
        TextView tvAction = itemView.findViewById(R.id.tvAction);
        mvsView.setText(item.getUserName());
        tvAction.setText(item.getActionText());
        addView(itemView);

        itemView.requestLayout();
        itemView.invalidate();
    }

    private void addEmptyItem() {
        TextView emptyView = new TextView(getContext());
        emptyView.setLayoutParams(new LinearLayout.LayoutParams(0, SignUtils.dp2px(43)));
        addView(emptyView);

        emptyView.requestLayout();
        emptyView.invalidate();
    }

    //仅仅内部刷新
    private boolean shouldLocalIinvalidate = true;

    @Override
    public void requestLayout() {
        if (shouldLocalIinvalidate) {
            localRequestLayout();
        } else {
            super.requestLayout();
        }
    }


    private int mWidthMeasureSpec;
    private int mheightMeasureSpec;
    private int mLeft;
    private int mTop;
    private int mRight;
    private int mBottom;

    @SuppressLint("WrongCall")
    void localRequestLayout() {
        onMeasure(mWidthMeasureSpec, mheightMeasureSpec);
        onLayout(true, mLeft, mTop, mRight, mBottom);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidthMeasureSpec = widthMeasureSpec;
        mheightMeasureSpec = widthMeasureSpec;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLeft = l;
        mTop = t;
        mRight = r;
        mBottom = b;
        super.onLayout(changed, l, t, r, b);
    }

}
