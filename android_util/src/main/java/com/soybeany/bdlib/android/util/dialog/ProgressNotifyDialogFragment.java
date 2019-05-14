package com.soybeany.bdlib.android.util.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.core.java8.Optional;

/**
 * todo 进度监听{@link #realToProgress(float)}
 * <br>Created by Soybeany on 2019/5/8.
 */
public class ProgressNotifyDialogFragment extends NotifyDialogFragment {
    @Nullable
    private ProgressDialog mDialog;

    private final Observer<String> hintObserver = hint -> Optional.ofNullable(mDialog).ifPresent(dialog -> dialog.setMessage(hint));
    private final Observer<Boolean> cancelableObserver = cancelable ->
            Optional.ofNullable(mDialog).ifPresent(dialog -> dialog.setCancelable(null != cancelable && cancelable));

    @Nullable
    private LiveData<String> mHint;
    @Nullable
    private LiveData<Boolean> mCancelable;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDialog = new ProgressDialog(getContext());
        mDialog.setCanceledOnTouchOutside(false);
        // 使用预设置好的信息
        Optional.ofNullable(mHint).ifPresent(hint -> hintObserver.onChanged(hint.getValue()));
        Optional.ofNullable(mCancelable).ifPresent(cancelable -> cancelableObserver.onChanged(cancelable.getValue()));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDialog = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return null != mDialog ? mDialog : super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void realToProgress(float percent) {
    }

    @Override
    public void realCancel() {
        if (null != mDialog) {
            mDialog.cancel();
        }
    }

    @Override
    public boolean isDialogShowing() {
        return null != mDialog && mDialog.isShowing();
    }

    @Override
    public void onObserveMsg(@NonNull LifecycleOwner owner, @NonNull LiveData<String> hint, @NonNull LiveData<Boolean> cancelable) {
        observeMsg(owner, mHint = hint, hintObserver);
        observeMsg(owner, mCancelable = cancelable, cancelableObserver);
    }

    private <T> void observeMsg(@NonNull LifecycleOwner owner, @NonNull LiveData<T> data, @NonNull Observer<T> observer) {
        data.observe(owner, observer);
    }
}
