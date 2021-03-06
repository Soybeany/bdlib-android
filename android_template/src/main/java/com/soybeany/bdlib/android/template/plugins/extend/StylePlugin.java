package com.soybeany.bdlib.android.template.plugins.extend;

import android.app.Activity;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.util.style.IQualifierChanger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

/**
 * 只在{@link Activity}中使用，适用于{@link IQualifierChanger}的实现类
 * <br>Created by Soybeany on 2019/5/5.
 */
public abstract class StylePlugin<Data> implements IExtendPlugin {
    protected final AppCompatActivity mActivity;
    private final IQualifierChanger<Data> mChanger;
    private final MutableLiveData<Data> mData;

    public StylePlugin(@NonNull AppCompatActivity activity, @NonNull IQualifierChanger<Data> changer, @NonNull MutableLiveData<Data> data) {
        mActivity = activity;
        mChanger = changer;
        mData = data;
    }

    @Override
    public void initBeforeOnCreate() {
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
