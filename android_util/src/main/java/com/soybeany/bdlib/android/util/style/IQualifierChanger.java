package com.soybeany.bdlib.android.util.style;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.soybeany.bdlib.android.util.R;


/**
 * 修饰符修改器
 * <br>Created by Soybeany on 2019/4/21.
 */
public interface IQualifierChanger<Data> {

    /**
     * 重新创建界面
     */
    static <Data> void recreate(AppCompatActivity activity, @Nullable Data oldData, @Nullable Data newData, IQualifierChanger<Data> changer) {
        if (null != newData && !newData.equals(oldData)) {
            activity.getWindow().setWindowAnimations(R.style.QualifierChangeAnimation);
            changer.onRecreate(activity);
        }
    }

    /**
     * 应用变化(需要在{@link Activity#setContentView(int)}前调用)
     */
    void applyChange(AppCompatActivity activity, @Nullable Data data);

    /**
     * 重新创建界面
     */
    void recreate(AppCompatActivity activity, @Nullable Data data);

    /**
     * 执行真正的重建
     */
    default void onRecreate(AppCompatActivity activity) {
        activity.recreate();
    }
}
