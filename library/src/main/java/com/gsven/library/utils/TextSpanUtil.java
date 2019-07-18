package com.gsven.library.utils;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.gsven.library.utils.enums.Direction;

public class TextSpanUtil {
    /**
     * @param mTextView
     * @param mDrawable
     * @param direction 左 上 右 下
     */
    public static void setTextDrawable(TextView mTextView, Drawable mDrawable,
                                      @Direction int direction) {
        mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
        switch (direction) {
            case Direction.LEFT://坐
                mTextView.setCompoundDrawables(mDrawable, mTextView.getCompoundDrawables()[1], mTextView.getCompoundDrawables()[2], mTextView.getCompoundDrawables()[3]);
                break;
            case Direction.TOP://上
                mTextView.setCompoundDrawables(mTextView.getCompoundDrawables()[0], mDrawable, mTextView.getCompoundDrawables()[2], mTextView.getCompoundDrawables()[3]);
                break;
            case Direction.RIGHT://右
                mTextView.setCompoundDrawables(mTextView.getCompoundDrawables()[0], mTextView.getCompoundDrawables()[1], mDrawable, mTextView.getCompoundDrawables()[3]);
                break;
            case Direction.BOTTOM://下
                mTextView.setCompoundDrawables(mTextView.getCompoundDrawables()[0], mTextView.getCompoundDrawables()[1], mTextView.getCompoundDrawables()[2], mDrawable);
                break;
        }
    }
}
