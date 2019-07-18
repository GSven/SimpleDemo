package com.thinkive.android.thinkive_invest.ui.view;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.PopupWindow;
/**
*
*@author Gsven
*@Date 2019/7/18 19:15
*@Desc
 *
 * 方法是给当前的activity覆盖一个宽度为0，高度为match_parent的PopupWindow，设置PopupWindow的mSoftInputMode为SOFT_INPUT_ADJUST_RESIZE，键盘弹出后，
 * 根据PopupWindow内容区高度的变化，来计算键盘弹出的高度。
 *
 * https://www.jianshu.com/p/ea7fb3387168
 *
 * oncreate()方法中
 *  new HeightProvider(this).init().setHeightListener(new HeightProvider.HeightListener() {
 *             @Override
 *             public void onHeightChanged(int height) {
 *                 etBottom.setTranslationY(-height);
 *             }
 *         });
 *
 *
 * activity--androidmanifest.xml---> android:windoSoftInputMode = "adjustNothing"
 *
*/
public class SoftInputHeightProvider extends PopupWindow implements ViewTreeObserver.OnGlobalLayoutListener {

    private Activity mActivity;
    private View rootView;
    private HeightListener listener;
    private int heightMax; // 记录popup内容区的最大高度

    public SoftInputHeightProvider(Activity activity) {
        super(activity);
        this.mActivity = activity;

        // 基础配置
        rootView = new View(activity);
        setContentView(rootView);

        // 监听全局Layout变化
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        setBackgroundDrawable(new ColorDrawable(0));

        // 设置宽度为0，高度为全屏
        setWidth(0);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        // 设置键盘弹出方式
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
    }

    public SoftInputHeightProvider init() {
        if (!isShowing()) {
            final View view = mActivity.getWindow().getDecorView();
            // 延迟加载popupwindow，如果不加延迟就会报错
            view.post(new Runnable() {
                @Override
                public void run() {
                    showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
                }
            });
        }
        return this;
    }

    public SoftInputHeightProvider setHeightListener(HeightListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onGlobalLayout() {
        Rect rect = new Rect();
        rootView.getWindowVisibleDisplayFrame(rect);
        if (rect.bottom > heightMax) {
            heightMax = rect.bottom;
        }

        // 两者的差值就是键盘的高度
        int keyboardHeight = heightMax - rect.bottom;
        if (listener != null) {
            listener.onHeightChanged(keyboardHeight);
        }
    }

    public interface HeightListener {
        void onHeightChanged(int height);
    }
}
