package com.soybeany.bdlib.android.ui.style;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.soybeany.bdlib.android.ui.R;

/**
 * 修饰符修改器(需要在{@link Activity#setContentView(int)}前调用)
 * <br>Created by Soybeany on 2019/4/21.
 */
public interface IQualifierChanger<Data> {

    /**
     * 重新创建界面
     */
    static <Data> void recreate(AppCompatActivity activity, @NonNull Data oldData, @Nullable Data newData) {
        if (!oldData.equals(newData)) {
            activity.getWindow().setWindowAnimations(R.style.QualifierChangeAnimation);
            activity.recreate();
        }
    }

    /**
     * 应用变化
     */
    void change(AppCompatActivity activity, @Nullable Data data);

    /**
     * 重新创建界面
     */
    void recreate(AppCompatActivity activity, @Nullable Data data);
}
