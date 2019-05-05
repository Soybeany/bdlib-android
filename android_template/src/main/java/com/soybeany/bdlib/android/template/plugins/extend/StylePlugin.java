package com.soybeany.bdlib.android.template.plugins.extend;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.util.style.IQualifierChanger;

/**
 * 只在{@link Activity}中使用，适用于{@link IQualifierChanger}的实现类
 * <br>Created by Soybeany on 2019/5/5.
 */
public abstract class StylePlugin<Data> implements IExtendPlugin {
    private final AppCompatActivity mActivity;
    private final IQualifierChanger<Data> mChanger;
    private final MutableLiveData<Data> mData;

    public StylePlugin(@NonNull AppCompatActivity activity, @NonNull IQualifierChanger<Data> changer, @NonNull MutableLiveData<Data> data) {
        mActivity = activity;
        mChanger = changer;
        mData = data;
    }

    @Override
    public void initBeforeSetContentView() {
        mChanger.applyChange(mActivity, getData());
        mData.observe(mActivity, data -> mChanger.recreate(mActivity, data));
    }

    protected void setData(Data data) {
        mData.setValue(data);
    }

    protected Data getData() {
        return mData.getValue();
    }
}
