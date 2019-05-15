package com.soybeany.bdlib.android.util.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.soybeany.bdlib.core.java8.Optional;

/**
 * todo 进度监听{@link #realToProgress(float)}
 * <br>Created by Soybeany on 2019/5/8.
 */
public class ProgressNotifyDialogFragment extends NotifyDialogFragment {
    @Nullable
    private ProgressDialog mDialog;

    private final Observer<String> mHintObserver = hint -> Optional.ofNullable(mDialog).ifPresent(dialog -> dialog.setMessage(hint));
    private final Observer<Boolean> mCancelableObserver = cancelable -> setCancelable(null != cancelable && cancelable);

    @Nullable
    private LiveData<String> mHint;
    @Nullable
    private LiveData<Boolean> mCancelable;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mDialog = new ProgressDialog(getContext());
        mDialog.setCanceledOnTouchOutside(false);
        // 使用预设置好的信息
        Optional.ofNullable(mHint).ifPresent(hint -> mHintObserver.onChanged(hint.getValue()));
        Optional.ofNullable(mCancelable).ifPresent(cancelable -> mCancelableObserver.onChanged(cancelable.getValue()));
        return mDialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDialog = null;
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
    public void onObserveMsg(@NonNull LiveData<String> hint, @NonNull LiveData<Boolean> cancelable) {
        mHint = hint;
        mCancelable = cancelable;
    }

    @Override
    protected void onBind(@NonNull FragmentActivity activity, @NonNull NotifyDialogDelegate delegate) {
        super.onBind(activity, delegate);
        Optional.ofNullable(mHint).ifPresent(hint -> hint.observe(activity, mHintObserver));
        Optional.ofNullable(mCancelable).ifPresent(cancelable -> cancelable.observe(activity, mCancelableObserver));
    }
}
