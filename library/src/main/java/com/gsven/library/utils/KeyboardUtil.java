package com.gsven.library.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
*
*@author Gsven
*@Date 2019/4/26 16:27
*@Desc 
*/
public class KeyboardUtil {
    /**
     * 隐藏系统自带的键盘
     *
     * @param activity 外部调用环境
     */
    public static void hideSystemKeyBoard(Activity activity) {
        // 收起系统键盘
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * 设置让一个EditText获取焦点并弹出键盘
     *
     * @param activity 外部调用的activity
     * @param editText 键盘的目标EditText
     */
    public static void showKeyBoard(final Activity activity, final EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        // 延迟半秒弹出键盘，防止因为界面未加载完而导致键盘弹不出来
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 强行弹出系统键盘的代码
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            }
        }, 500);
    }
}
