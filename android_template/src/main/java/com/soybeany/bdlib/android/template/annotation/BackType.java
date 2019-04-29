package com.soybeany.bdlib.android.template.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.soybeany.bdlib.android.template.annotation.BackType.BACK_ITEM;
import static com.soybeany.bdlib.android.template.annotation.BackType.BACK_KEY;
import static com.soybeany.bdlib.android.template.annotation.BackType.BACK_OTHER;


/**
 * 返回类型范围限定
 * <br>Created by Soybeany on 2017/3/15.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
@IntDef({BACK_OTHER, BACK_KEY, BACK_ITEM})
public @interface BackType {

    /**
     * 其它方式返回
     */
    int BACK_OTHER = 1;

    /**
     * 物理返回按钮
     */
    int BACK_KEY = 0;

    /**
     * 工具栏上的返回按钮
     */
    int BACK_ITEM = -1;

}
