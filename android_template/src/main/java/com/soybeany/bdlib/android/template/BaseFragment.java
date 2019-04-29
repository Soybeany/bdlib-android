package com.soybeany.bdlib.android.template;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soybeany.bdlib.android.util.dialog.AbstractDialog;
import com.soybeany.bdlib.android.util.dialog.DialogKeyProvider;
import com.soybeany.bdlib.android.util.dialog.ProgressDialogImpl;
import com.soybeany.bdlib.android.util.system.PermissionRequester;

/**
 * <br>Created by Soybeany on 2019/3/19.
 */
public abstract class BaseFragment extends Fragment implements IBaseFunc.IEx<View> {
    private IBaseFunc mFuncImpl = new BaseFuncImpl(this);
    private View mContentV;
    private int mPreparedCount; // 已准备好的位置的计数
    private boolean mIsNew;

    // //////////////////////////////////官方方法重写//////////////////////////////////

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsNew = (null == savedInstanceState);
        signalBeforeSetContentView();
        mContentV = getLayoutInflater().inflate(setupLayoutResId(), null, false);
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
            tryToSignalDoBusiness();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkPermissionResult(requestCode, permissions, grantResults);
    }

    // //////////////////////////////////自定义方法重写//////////////////////////////////

    @Override
    public View onGetButterKnifeSource() {
        return mContentV;
    }

    @Override
    public void signalBeforeSetContentView() {
        beforeSetupContentView();
    }

    @Override
    public AbstractDialog onGetNewDialog() {
        return new ProgressDialogImpl(this);
    }

    @Override
    public AbstractDialog getDialog() {
        return mFuncImpl.getDialog();
    }

    @Override
    public DialogKeyProvider getDialogKeys() {
        return mFuncImpl.getDialogKeys();
    }

    @Override
    public <T extends ViewModel> T getViewModel(Class<T> modelClass) {
        return ViewModelProviders.of(this).get(modelClass);
    }

    @Override
    public boolean requestPermissions(@NonNull PermissionRequester.IPermissionCallback callback, @Nullable String... permissions) {
        return mFuncImpl.requestPermissions(callback, permissions);
    }

    @Override
    public void checkPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mFuncImpl.checkPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    public PermissionRequester onGetNewPermissionRequester() {
        return new PermissionRequester(getActivity(), (activity, permissions, requestCode) -> requestPermissions(permissions, requestCode));
    }

    @Override
    public PermissionRequester.IPermissionCallback getEssentialPermissionCallback() {
        return this::tryToSignalDoBusiness;
    }

    // //////////////////////////////////拓展方法//////////////////////////////////

    @Nullable
    public <T extends ViewModel> T getActivityViewModel(Class<T> modelClass) {
        FragmentActivity activity = getActivity();
        return null != activity ? ViewModelProviders.of(activity).get(modelClass) : null;
    }

    // //////////////////////////////////内部方法//////////////////////////////////

    private void tryToSignalDoBusiness() {
        if (mPreparedCount > 1) {
            return;
        } else if (mPreparedCount == 1) {
            mContentV.post(() -> doBusiness(mIsNew));
        }
        mPreparedCount++;
    }
}
