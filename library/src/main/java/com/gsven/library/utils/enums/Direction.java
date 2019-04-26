package com.gsven.library.utils.enums;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * @author Gsven
 * @Date 2019/4/26 16:16
 * @Desc 使用注解代替枚举类型
 * <p>
 * 方向
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Direction {
    int LEFT = 0;
    int TOP = 1;
    int RIGHT = 2;
    int BOTTOM = 3;
}
