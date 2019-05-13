package com.soybeany.bdlib.android.util.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
public class ProgressNotifyDialogFragment extends NotifyDialogFragment {
    @Nullable
    private ProgressDialog mDialog;

    private String mHint;
    private boolean mCancelable;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mDialog = new ProgressDialog(getContext());
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setMessage(mHint);
        mDialog.setCancelable(mCancelable);
        return mDialog;
    }

    @Override
    public void signalSetupMsg(String hint, boolean cancelable) {
        mHint = hint;
        mCancelable = cancelable;
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
}
