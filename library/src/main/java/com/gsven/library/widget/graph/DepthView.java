package com.gsven.library.widget.graph;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.gsven.library.R;
import com.gsven.library.utils.NumberUtils;
import com.gsven.library.utils.ScreenUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Autor: WGsven
 * @Time: 2019/1/8 10:10
 * @Desc: DepthView
 * <p>
 * 走势控件
 */

public class DepthView extends View {
    private int verticalLineColor = Color.parseColor("#FFB767");//
    private Paint mXCoordinatePaint;
    private Paint mXCoordinatePaintReal;
    private Path xPath;

    //是否显示详情
    private boolean isShowDetail = false;
    //是否长按
    private boolean isLongPress = false;
    //是否显示竖线
    private boolean isShowDetailLine = true;
    //手指单击松开后，数据是否继续显示
    private boolean isShowDetailSingleClick = true;
    //单击松开，数据延时消失，单位毫秒
    private final int DISAPPEAR_TIME = 500;
    //手指长按松开后，数据是否继续显示
    private boolean isShowDetailLongPress = true;
    //长按触发时长，单位毫秒
//    private final int LONG_PRESS_TIME_OUT = 300;
    private final int LONG_PRESS_TIME_OUT = 5;
    //横坐标中间值
    private String abscissaCenterDate;
    private boolean isHorizontalMove;
    private Depth clickDepth;
    private String detailPriceTitle;
    private String detailVolumeTitle;
    private Paint strokePaint, fillPaint, shaderPaint;
    private Rect textRect;
    private Path linePath;
    private List<Depth> sellDataList;
    private double maxPrice, minPrice, avgPriceSpace, avgOrdinateSpace, depthImgHeight;
    private float leftStart, topStart, rightEnd, bottomEnd, longPressDownX, longPressDownY,
            singleClickDownX, singleClickDownY, detailLineWidth, dispatchDownX;
    private int sellLineCol, sellBgCol, ordinateTextCol, ordinateTextSize,
            abscissaTextCol, abscissaTextSize, detailBgCol, detailTextCol, detailTextSize, ordinateNum,
            sellLineStrokeWidth, detailLineCol, detailPointRadius, pricePrecision,
            moveLimitDistance;
    private Runnable longPressRunnable;
    private Runnable singleClickDisappearRunnable;
    private String leftVolumeStr;//x轴起点
    private String rightVolumeStr;//x轴终点
    private int mPadding;

    public DepthView(Context context) {
        this(context, null);
    }

    public DepthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DepthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 设置出售数据
     */
    public void setSellDataList(List<Depth> depthList) {
        number = 0;
        sellDataList.clear();
        sellDataList.addAll(depthList);
        //如果数据是无序的，则按价格进行排序。如果是有序的，则注释掉
//        Collections.sort(sellDataList);
        requestLayout();
        invalidate();
    }

    /**
     * 重置深度数据
     */
    public void resetAllData(List<Depth> sellDataList) {
        number = 0;
        setSellDataList(sellDataList);
        isShowDetail = false;
        isLongPress = false;
        requestLayout();
    }

    /**
     * 设置横坐标中间值
     */
    public void setAbscissaCenterDate(String centerPrice) {
        this.abscissaCenterDate = centerPrice;
    }

    /**
     * 是否显示竖线
     */
    public void setShowDetailLine(boolean isShowLine) {
        this.isShowDetailLine = isShowLine;
    }

    /**
     * 手指单击松开后，数据是否继续显示
     */
    public void setShowDetailSingleClick(boolean isShowDetailSingleClick) {
        this.isShowDetailSingleClick = isShowDetailSingleClick;
    }

    /**
     * 手指长按松开后，数据是否继续显示
     */
    public void setShowDetailLongPress(boolean isShowDetailLongPress) {
        this.isShowDetailLongPress = isShowDetailLongPress;
    }

    /**
     * 设置横坐标价钱小数位精度
     */
    public void setPricePrecision(int pricePrecision) {
        this.pricePrecision = pricePrecision;
    }

    /**
     * 设置数据详情的价钱说明
     */
    public void setDetailPriceTitle(String priceTitle) {
        this.detailPriceTitle = priceTitle;
    }

    /**
     * 设置数据详情的数量说明
     */
    public void setDetailVolumeTitle(String volumeTitle) {
        this.detailVolumeTitle = volumeTitle;
    }

    /**
     * 移除runnable
     */
    public void cancelCallback() {
        removeCallbacks(longPressRunnable);
        removeCallbacks(singleClickDisappearRunnable);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DepthView);
            sellLineCol = typedArray.getColor(R.styleable.DepthView_dvSellLineCol, 0xffFF5442);
            sellLineStrokeWidth = typedArray.getInt(R.styleable.DepthView_dvSellLineStrokeWidth, 1);
            sellBgCol = typedArray.getColor(R.styleable.DepthView_dvSellBgCol, 0x66FF5442);
            ordinateTextCol = typedArray.getColor(R.styleable.DepthView_dvOrdinateCol, 0xff808F9E);
            ordinateTextSize = typedArray.getInt(R.styleable.DepthView_dvOrdinateTextSize, 8);
            ordinateNum = typedArray.getInt(R.styleable.DepthView_dvOrdinateNum, 5);
            abscissaTextCol = typedArray.getColor(R.styleable.DepthView_dvAbscissaCol, ordinateTextCol);
            abscissaTextSize = typedArray.getInt(R.styleable.DepthView_dvAbscissaTextSize, ordinateTextSize);
            detailBgCol = typedArray.getColor(R.styleable.DepthView_dvDetailBgCol, 0x99F3F4F6);
            detailTextCol = typedArray.getColor(R.styleable.DepthView_dvDetailTextCol, 0xff294058);
            detailTextSize = typedArray.getInt(R.styleable.DepthView_dvDetailTextSize, 10);
            detailLineCol = typedArray.getColor(R.styleable.DepthView_dvDetailLineCol, 0xff828EA2);
            detailLineWidth = typedArray.getFloat(R.styleable.DepthView_dvDetailLineWidth, 0);
            detailPointRadius = typedArray.getInt(R.styleable.DepthView_dvDetailPointRadius, 3);
            detailPriceTitle = typedArray.getString(R.styleable.DepthView_dvDetailPriceTitle);
            detailVolumeTitle = typedArray.getString(R.styleable.DepthView_dvDetailVolumeTitle);
            typedArray.recycle();
        }

        mPadding = (int) ScreenUtil.dpToPx(context, 28);
        sellDataList = new ArrayList<>();

        strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setStyle(Paint.Style.STROKE);

        fillPaint = new Paint();
        fillPaint.setAntiAlias(true);
        fillPaint.setStyle(Paint.Style.FILL);

        shaderPaint = new Paint(fillPaint);

        textRect = new Rect();
        linePath = new Path();

        pricePrecision = 8;

        if (TextUtils.isEmpty(detailPriceTitle)) {
            detailPriceTitle = "七日年化收益率：";
        }
        if (TextUtils.isEmpty(detailVolumeTitle)) {
            detailVolumeTitle = "日期：";
        }

        moveLimitDistance = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        longPressRunnable = new Runnable() {
            @Override
            public void run() {
                isLongPress = true;
                isShowDetail = true;
                getClickDepth(longPressDownX);
                invalidate();
            }
        };
        singleClickDisappearRunnable = new Runnable() {
            @Override
            public void run() {
                //=====长按显示，点击不显示，注释以下代码=====
//                isLongPress = true;
//                isShowDetail = true;
                //=====长按显示，点击不显示，注释以上代码=====
                isShowDetail = false;
                invalidate();
            }
        };


        //x轴坐标虚线
        mXCoordinatePaint = new TextPaint();
        PathEffect effects = new DashPathEffect(new float[]{10, 5,}, 0);
        mXCoordinatePaint.setPathEffect(effects);
        mXCoordinatePaint.setAntiAlias(true);
        mXCoordinatePaint.setStrokeWidth(1);
        mXCoordinatePaint.setStyle(Paint.Style.STROKE);
        mXCoordinatePaint.setColor(verticalLineColor);
        //实线
        mXCoordinatePaintReal = new TextPaint();
        mXCoordinatePaintReal.setStrokeWidth(1);
        mXCoordinatePaintReal.setStyle(Paint.Style.STROKE);
        mXCoordinatePaintReal.setColor(verticalLineColor);
        xPath = new Path();


        //  初始化渐变色
        shadeColors = new int[]{
                Color.argb(100, 255, 86, 86), Color.argb(45, 255, 86, 86),
                Color.argb(0, 255, 86, 86)};
    }

    private int[] shadeColors;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        leftStart = getPaddingLeft() + mPadding + 1;
        topStart = getPaddingTop() + 1;
        rightEnd = getMeasuredWidth() - getPaddingRight() - 1;
        bottomEnd = getMeasuredHeight() - getPaddingBottom() - 1;


        if (!sellDataList.isEmpty()) {
            maxPrice = Collections.max(sellDataList).getPrice();
            minPrice = Collections.min(sellDataList).getPrice();
        }

        resetStrokePaint(abscissaTextCol, abscissaTextSize, 0);

        if (!sellDataList.isEmpty()) {
            leftVolumeStr = sellDataList.get(0).getVolume();
        } else {
            leftVolumeStr = "0";
        }

        if (!sellDataList.isEmpty()) {
            rightVolumeStr = sellDataList.get(sellDataList.size() - 1).getVolume();
        } else {
            rightVolumeStr = "0";
        }

        strokePaint.getTextBounds(leftVolumeStr, 0, leftVolumeStr.length(), textRect);
        depthImgHeight = bottomEnd - topStart - textRect.height() - dp2px(4);
        double avgHeightPerPrice = depthImgHeight / (maxPrice - minPrice);
        double avgWidthPerSize = (rightEnd - leftStart) / sellDataList.size();
//        avgPriceSpace = maxPrice / ordinateNum;//(maxPrice - minPrice)/ordnateNum
        avgPriceSpace = (maxPrice - minPrice) / ordinateNum;
        avgOrdinateSpace = depthImgHeight / ordinateNum;

        for (int i = 0; i < sellDataList.size(); i++) {
            sellDataList.get(i).setX(leftStart + (float) avgWidthPerSize * i);
            sellDataList.get(i).setY(topStart + (float) ((maxPrice - sellDataList.get(i).getPrice()) * avgHeightPerPrice));
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (sellDataList.isEmpty()) {
            return;
        }
        drawLineAndBg(canvas);
        drawCoordinateValue(canvas);
        drawDetailData(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            longPressDownX = event.getX();
            longPressDownY = event.getY();
            dispatchDownX = event.getX();
            isLongPress = false;
            postDelayed(longPressRunnable, LONG_PRESS_TIME_OUT);

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //长按控制
            float dispatchMoveX = event.getX();
            float dispatchMoveY = event.getY();
            float diffDispatchMoveX = Math.abs(dispatchMoveX - longPressDownX);
            float diffDispatchMoveY = Math.abs(dispatchMoveY - longPressDownY);
            float moveDistanceX = Math.abs(event.getX() - dispatchDownX);

            getParent().requestDisallowInterceptTouchEvent(true);

            if (isHorizontalMove || (diffDispatchMoveX > diffDispatchMoveY + dp2px(5)
                    && diffDispatchMoveX > moveLimitDistance)) {
                isHorizontalMove = true;
                removeCallbacks(longPressRunnable);

                if (isLongPress && moveDistanceX > 2) {
                    getClickDepth(event.getX());
                    if (clickDepth != null) {
                        invalidate();
                    }
                }
                dispatchDownX = event.getX();
                return isLongPress || super.dispatchTouchEvent(event);

            } else if (!isHorizontalMove && diffDispatchMoveY > diffDispatchMoveX + dp2px(5)
                    && diffDispatchMoveY > moveLimitDistance) {
                removeCallbacks(longPressRunnable);
                getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            isHorizontalMove = false;
            removeCallbacks(longPressRunnable);
            if (!isShowDetailLongPress) {
                isShowDetail = false;
                invalidate();
            }
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        return isLongPress || super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                singleClickDownX = event.getX();
                singleClickDownY = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                float diffTouchMoveX = event.getX() - singleClickDownX;
                float diffTouchMoveY = event.getY() - singleClickDownY;
                if (diffTouchMoveY < moveLimitDistance && diffTouchMoveX < moveLimitDistance) {
                    isShowDetail = true;
                    getClickDepth(singleClickDownX);
                    if (clickDepth != null) {
                        invalidate();
                    }
                }
                if (!isShowDetailSingleClick) {
                    postDelayed(singleClickDisappearRunnable, DISAPPEAR_TIME);
                }
                break;
        }
        return true;
    }

    //获取单击位置数据
    private void getClickDepth(float clickX) {
        clickDepth = null;
        for (int i = 0; i < sellDataList.size(); i++) {
            if (i + 1 < sellDataList.size() && clickX >= sellDataList.get(i).getX()
                    && clickX < sellDataList.get(i + 1).getX()) {
                clickDepth = sellDataList.get(i);
                if (onGetDepthValueListener != null) {
                    onGetDepthValueListener.onGetDepthValueListener(clickDepth);
                }
                break;
            } else if (i == sellDataList.size() - 1 && clickX >= sellDataList.get(i).getX()) {
                clickDepth = sellDataList.get(i);
                if (onGetDepthValueListener != null) {
                    onGetDepthValueListener.onGetDepthValueListener(clickDepth);
                }
                break;
            }
        }
    }


    //坐标轴
    private void drawCoordinateValue(Canvas canvas) {
        //横轴
        resetStrokePaint(abscissaTextCol, abscissaTextSize, 0);

        strokePaint.getTextBounds(rightVolumeStr, 0, rightVolumeStr.length(), textRect);
        //左边价格
        canvas.drawText(leftVolumeStr,
                leftStart,
                bottomEnd + dp2px(6),
                strokePaint);
        //右边价格
        canvas.drawText(rightVolumeStr,
                rightEnd - textRect.width(),
                bottomEnd + dp2px(6),
                strokePaint);
        //中间价格
        if (!TextUtils.isEmpty(abscissaCenterDate)) {
            canvas.drawText(abscissaCenterDate,
                    getWidth() / 2 - strokePaint.measureText(abscissaCenterDate) / 2,
                    bottomEnd + dp2px(6),
                    strokePaint);
        }

        //纵轴
        resetStrokePaint(ordinateTextCol, ordinateTextSize, 0);
        strokePaint.getTextBounds(maxPrice + "", 0, (maxPrice + "").length(), textRect);
        for (int i = 0; i <= ordinateNum; i++) {
            String ordinateStr = NumberUtils.setPrecision(maxPrice - i * avgPriceSpace, 3) + "%";
            //左边绘制
            canvas.drawText(ordinateStr,
                    0,
                    (float) (topStart + textRect.height() + i * avgOrdinateSpace),
                    strokePaint);
            //x轴虚线

            xPath.reset();
            xPath.moveTo(sellDataList.get(0).getX(), (float) (topStart + textRect.height() + i * avgOrdinateSpace));
            xPath.lineTo(rightEnd, (float) (topStart + textRect.height() + i * avgOrdinateSpace));
            if (i == ordinateNum) {
                canvas.drawPath(xPath, mXCoordinatePaintReal);
            } else {
                canvas.drawPath(xPath, mXCoordinatePaint);
            }

        }
        //画一个原点y轴
        xPath.reset();
        xPath.moveTo(leftStart - mPadding / 3, (float) (topStart + textRect.height() + ordinateNum * avgOrdinateSpace));
        xPath.lineTo(leftStart - mPadding / 3, topStart);
        canvas.drawPath(xPath, mXCoordinatePaintReal);
    }

    /**
     * 动态绘制
     */
    private int number;
    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (number < sellDataList.size()) {
                ++number;
                invalidate();
                hander.sendEmptyMessageDelayed(1, 100);
            }
        }
    };


    private void drawLineAndBg(Canvas canvas) {
        //买方背景
        if (!sellDataList.isEmpty()) {
            linePath.reset();
            if (number <= sellDataList.size()) {
                for (int i = 0; i < number; i++) {
                    if (i == 0) {
                        linePath.moveTo(sellDataList.get(i).getX(), sellDataList.get(i).getY());
                    } else {
                        linePath.lineTo(sellDataList.get(i).getX(), sellDataList.get(i).getY());
                    }
                }
                if (!sellDataList.isEmpty() && number <= sellDataList.size()) {
                    if (number > 0) {
                        linePath.lineTo(sellDataList.get(number - 1).getX(), (float) (topStart + textRect.height() + ordinateNum * avgOrdinateSpace));
                    }
                }
                linePath.lineTo(leftStart, (float) (topStart + textRect.height() + ordinateNum * avgOrdinateSpace));
                linePath.close();
                shaderPaint.setColor(sellBgCol);
                //  渐变阴影
                Shader mShader = new LinearGradient(0, 0, 0, getHeight(), shadeColors, null, Shader.TileMode.CLAMP);
                shaderPaint.setShader(mShader);
                //  绘制渐变阴影
                canvas.drawPath(linePath, shaderPaint);

                fillPaint.setColor(sellBgCol);
                //买方线条
                linePath.reset();
                for (int i = 0; i < number; i++) {
                    if (i == 0) {
                        linePath.moveTo(sellDataList.get(i).getX(), sellDataList.get(i).getY());
                    } else {
                        linePath.lineTo(sellDataList.get(i).getX(), sellDataList.get(i).getY());
                    }
                }
                resetStrokePaint(sellLineCol, 0, sellLineStrokeWidth);
                canvas.drawPath(linePath, strokePaint);
            }
        }
        hander.sendEmptyMessage(1);
    }

    /**
     * 本项目不显示详情，仅显示游标
     *
     * @param canvas
     */
    private boolean showYB = false;

    private void drawDetailData(Canvas canvas) {

        if (!isShowDetail || clickDepth == null) {
            return;
        }
        //游标线
        if (isShowDetailLine) {
            resetStrokePaint(detailLineCol, 0, detailLineWidth);
            canvas.drawLine(clickDepth.getX(), topStart, clickDepth.getX(),
                    (float) (topStart + textRect.height() + ordinateNum * avgOrdinateSpace),
//                    topStart + (float) depthImgHeight,
                    strokePaint);
        }
        canvas.drawCircle(clickDepth.getX(), clickDepth.getY(), dp2px(detailPointRadius), fillPaint);
        if (showYB) {
            resetStrokePaint(detailTextCol, detailTextSize, 0);
            fillPaint.setColor(detailBgCol);
            String clickPriceStr = detailPriceTitle + formatNum(clickDepth.getPrice());
            String clickVolumeStr = detailVolumeTitle + clickDepth.getVolume();
            strokePaint.getTextBounds(clickPriceStr, 0, clickPriceStr.length(), textRect);
            int priceStrWidth = textRect.width();
            int priceStrHeight = textRect.height();
            strokePaint.getTextBounds(clickVolumeStr, 0, clickVolumeStr.length(), textRect);
            int volumeStrWidth = textRect.width();
            int maxWidth = Math.max(priceStrWidth, volumeStrWidth);

            float bgLeft, bgTop, bgRight, bgBottom, priceStrX, priceStrY, volumeStrY;

            if (clickDepth.getX() <= maxWidth + dp2px(15)) {
                bgLeft = clickDepth.getX() + dp2px(5);
                bgRight = clickDepth.getX() + dp2px(15) + maxWidth;
                priceStrX = clickDepth.getX() + dp2px(10);

            } else {
                bgLeft = clickDepth.getX() - dp2px(15) - maxWidth;
                bgRight = clickDepth.getX() - dp2px(5);
                priceStrX = clickDepth.getX() - dp2px(10) - maxWidth;
            }

            if (clickDepth.getY() < topStart + dp2px(7) + priceStrHeight) {
                bgTop = topStart;
                bgBottom = topStart + dp2px(14) + priceStrHeight * 2;
                priceStrY = topStart + dp2px(3) + priceStrHeight;
                volumeStrY = topStart + dp2px(7) + priceStrHeight * 2;

            } else if (clickDepth.getY() > topStart + depthImgHeight - dp2px(7) - priceStrHeight) {
                bgTop = topStart + (float) depthImgHeight - dp2px(14) - priceStrHeight * 2;
                bgBottom = topStart + (float) depthImgHeight;
                priceStrY = topStart + (float) depthImgHeight - dp2px(9) - priceStrHeight;
                volumeStrY = topStart + (float) depthImgHeight - dp2px(5);

            } else {
                bgTop = clickDepth.getY() - dp2px(10) - priceStrHeight;
                bgBottom = clickDepth.getY() + dp2px(10) + priceStrHeight;
                priceStrY = clickDepth.getY() - dp2px(2);
                volumeStrY = clickDepth.getY() + priceStrHeight;
            }

            RectF rectF = new RectF(bgLeft, bgTop, bgRight, bgBottom);
            canvas.drawRoundRect(rectF, 6, 6, fillPaint);
            canvas.drawText(clickPriceStr, priceStrX, priceStrY, strokePaint);
            canvas.drawText(clickVolumeStr, priceStrX, volumeStrY, strokePaint);
        }
    }

    /**
     * 设置小数位精度
     *
     * @param num
     * @param scale 保留几位小数
     */
    private String setPrecision(Double num, int scale) {
        BigDecimal bigDecimal = new BigDecimal(num);
        return bigDecimal.setScale(scale, BigDecimal.ROUND_DOWN).toPlainString();
    }

    /**
     * 按量级格式化数量
     */
    private String formatNum(double num) {
        if (num < 1) {
            return setPrecision(num, 6);
        } else if (num < 10) {
            return setPrecision(num, 4);
        } else if (num < 100) {
            return setPrecision(num, 3);
        } else if (num < 10000) {
            return setPrecision(num / 1000, 1) + "K";
        } else {
            return setPrecision(num / 10000, 2) + "万";
        }
    }

    private void resetStrokePaint(int colorId, int textSize, float strokeWidth) {
        strokePaint.setColor(colorId);
        strokePaint.setTextSize(sp2px(textSize));
        strokePaint.setStrokeWidth(dp2px(strokeWidth));
    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public interface OnGetDepthValueListener {
        void onGetDepthValueListener(Depth depth);
    }

    public OnGetDepthValueListener onGetDepthValueListener;

    public void setOnGetDepthValueListener(OnGetDepthValueListener onGetDepthValueListener) {
        this.onGetDepthValueListener = onGetDepthValueListener;
    }
}
