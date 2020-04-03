package com.soybeany.bdlib.android.util.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.dialog.msg.IDialogHint;

/**
 * todo 进度监听{@link #onToProgress(float)}
 * <br>Created by Soybeany on 2020/4/2.
 */
public class ProgressDialogFragment extends BaseDialogFragment {

    @Nullable
    private ProgressDialog mDialog;
    @Nullable
    private IDialogHint mLastDialogHint;
    private boolean mCancelable;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mDialog = new ProgressDialog(getContext());
        mDialog.setCanceledOnTouchOutside(false);
        // 设置默认值
        setupDialog();
        return mDialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDialog = null;
    }

    @Override
    public boolean isDialogShowing() {
        if (mDialog != null) {
            return mDialog.isShowing();
        }
        return false;
    }

    @Override
    public void onChangeDialogHint(IDialogHint hint, boolean cancelable) {
        mLastDialogHint = hint;
        mCancelable = cancelable;
        setupDialog();
    }

    private void setupDialog() {
        if (null != mDialog && null != mLastDialogHint) {
            mDialog.setMessage(mLastDialogHint.hint());
            setCancelable(mCancelable);
        }
    }
}
