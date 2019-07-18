package com.gsven.library.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberUtils {

    public static String setPrecision(String numStr, int scale) {
        return setPrecision(NumberUtils.doubleValueOfStr(numStr), scale);
    }

    /**
     * String 转 double，转不了就返回0
     *
     * @param doubleStr 要被转成double的String
     * @return 转后的double
     */
    public static double doubleValueOfStr(String doubleStr) {
        double result = 0;
        try {
            result = Double.valueOf(doubleStr);
        } catch (NumberFormatException | NullPointerException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 设置小数位精度
     *
     * @param num
     * @param scale 保留几位小数
     */
    public static String setPrecision(Double num, int scale) {
        BigDecimal bigDecimal = new BigDecimal(num);
        return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }


    /**
     * 保留小数点后两位
     *
     * @param num 处理前的小数
     * @return num保留两位小数后的结果
     */
    public static String formatDouble(Double num) {
        //保留小数点后两位
        DecimalFormat df = new DecimalFormat("##0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(num);
    }

    /**
     * 格式化一个数字，每逢三位加一个逗号，且保留小数点后两位
     *
     * @param data 被格式化的数字
     * @return 格式化后，带逗号的数字
     */
    public static String formatToSeparate(String data) {
        String result = data;
        if (!TextUtils.isEmpty(data)) {
            try {
                double numberData = Double.parseDouble(data);
                if (numberData >= 1000) {
                    DecimalFormat df = new DecimalFormat("#,###.00");
                    result = df.format(numberData);
                } else {
                    DecimalFormat df = new DecimalFormat("#0.00");
                    result = df.format(numberData);
                }
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
                result = data;
            } catch (Exception e) {
                e.printStackTrace();
                result = data;
            }
        }
        return result;
    }
}
