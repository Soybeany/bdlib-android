package com.soybeany.bdlib.android.util.style;

import android.app.Activity;

import com.soybeany.bdlib.android.util.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * 修饰符修改器
 * <br>Created by Soybeany on 2019/4/21.
 */
public interface IQualifierChanger<Data> {

    /**
     * 应用变化(需要在{@link Activity#setContentView(int)}前调用)
     */
    void applyChange(AppCompatActivity activity, @Nullable Data data);

    /**
     * 重新创建界面
     */
    void recreate(AppCompatActivity activity, @Nullable Data data);

    /**
     * 执行真正的应用
     */
    void onApply(AppCompatActivity activity, @NonNull Data newData);

    /**
     * 执行真正的重建
     */
    void onRecreate(AppCompatActivity activity, @Nullable Data appliedData, @NonNull Data newData);

    /**
     * 获得使用中的数据
     */
    Data getAppliedData(AppCompatActivity activity);


    abstract class Impl<Data> implements IQualifierChanger<Data> {
        private boolean mNeedAnimation = true;

        @Override
        public void applyChange(AppCompatActivity activity, @Nullable Data data) {
            if (null != data) {
                onApply(activity, data);
            }
        }

        @Override
        public void recreate(AppCompatActivity activity, @Nullable Data data) {
            Data appliedData;
            if (null == data || data.equals(appliedData = getAppliedData(activity))) {
                return;
            }
            if (mNeedAnimation) {
                activity.getWindow().setWindowAnimations(R.style.QualifierChangeAnimation);
            }
            onRecreate(activity, appliedData, data);
        }

        @Override
        public void onRecreate(AppCompatActivity activity, @Nullable Data appliedData, @NonNull Data newData) {
            activity.recreate();
        }

        /**
         * 是否使用切换动画(只在安卓7.0以下生效)
         */
        public void needAnimation(boolean flag) {
            mNeedAnimation = flag;
        }
    }
}
