package com.soybeany.bdlib.android.util.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.soybeany.bdlib.android.util.dialog.msg.IDialogHint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
        mDialog = onGetNewDialog(getContext());
        // 设置默认值
        applyChange();
        return mDialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDialog = null;
    }

    @Override
    public void onDisplayHint(IDialogHint hint) {
        mLastDialogHint = hint;
        applyChange();
    }

    @Override
    public void onChangeCancelable(boolean cancelable) {
        mCancelable = cancelable;
        applyChange();
    }

    @NonNull
    protected ProgressDialog onGetNewDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    private void applyChange() {
        if (null == mDialog) {
            return;
        }
        if (null != mLastDialogHint) {
            mDialog.setMessage(mLastDialogHint.hint());
        }
        setCancelable(mCancelable);
    }
}
