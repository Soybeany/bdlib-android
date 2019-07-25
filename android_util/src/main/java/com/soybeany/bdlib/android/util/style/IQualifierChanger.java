package com.soybeany.bdlib.android.util.style;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.soybeany.bdlib.android.util.R;


/**
 * 修饰符修改器
 * <br>Created by Soybeany on 2019/4/21.
 */
public interface IQualifierChanger<Data> {

    /**
     * 应用变化(需要在{@link Activity#setContentView(int)}前调用)
     */
    default void applyChange(AppCompatActivity activity, @Nullable Data data) {
        if (null != data) {
            onApply(activity, data);
        }
    }

    /**
     * 重新创建界面
     */
    default void recreate(AppCompatActivity activity, @Nullable Data data) {
        Data appliedData;
        if (null == data || data.equals(appliedData = getAppliedData(activity))) {
            onNotRecreate(activity);
            return;
        }
        activity.getWindow().setWindowAnimations(R.style.QualifierChangeAnimation);
        onRecreate(activity, appliedData, data);
    }

    /**
     * 执行真正的应用
     */
    void onApply(AppCompatActivity activity, @NonNull Data newData);

    /**
     * 执行真正的重建
     */
    default void onRecreate(AppCompatActivity activity, @Nullable Data appliedData, @NonNull Data newData) {
        activity.recreate();
    }

    /**
     * 不执行重建
     */
    default void onNotRecreate(AppCompatActivity activity) {
    }

    /**
     * 获得应用中的数据
     */
    Data getAppliedData(AppCompatActivity activity);
}
