package com.soybeany.bdlib.android.template;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soybeany.bdlib.android.template.lifecycle.ButterKnifeObserver;

/**
 * <br>Created by Soybeany on 2019/3/19.
 */
public abstract class BaseFragment extends Fragment
        implements IInitialHelper, ButterKnifeObserver.ICallback<View> {
    private LifecycleHelper mLifecycleHelper = new LifecycleHelper();
    private View mContentV;
    private int mPreparedCount; // 已准备好的位置的计数

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentV = getLayoutInflater().inflate(setupLayoutResId(), null, false);
        mLifecycleHelper.addObservers(getLifecycle(), setupObservers(), new ButterKnifeObserver(this));
        onInit();
        trySignalDoBusiness();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mContentV;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            trySignalDoBusiness();
        }
    }

    @Override
    public View onGetButterKnifeSource() {
        return mContentV;
    }

    private void trySignalDoBusiness() {
        if (mPreparedCount > 1) {
            return;
        } else if (mPreparedCount == 1) {
            doBusiness();
        }
        mPreparedCount++;
    }
}