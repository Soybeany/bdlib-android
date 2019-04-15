package com.soybeany.bdlib.android.util.dialog;

import android.app.ProgressDialog;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.core.util.storage.MessageCenter;


/**
 * <br>Created by Soybeany on 2019/3/4.
 */
public class ProgressDialogImpl extends AbstractDialog implements IObserver {
    private ProgressDialog mDialog;

    public ProgressDialogImpl(FragmentActivity activity) {
        this(activity, DialogViewModel.get(activity));
    }

    public ProgressDialogImpl(Fragment fragment) {
        this(fragment.getContext(), DialogViewModel.get(fragment));
    }

    public ProgressDialogImpl(Context context, DialogViewModel vm) {
        super(vm);
        // 创建弹窗
        mDialog = new ProgressDialog(context);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnCancelListener(d -> MessageCenter.notify(getKeyProvider().getOnDismissDialogKey(), DialogViewModel.Reason.CANCEL, 0));
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        // 若含有信息，直接显示弹窗
        if (mVM.isShowing) {
            showNewestMsg(true);
            onRealShow();
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        onRealDismiss();
    }

    @Override
    protected void onSetupDialog(String hint, boolean cancelable) {
        mDialog.setMessage(hint);
        mDialog.setCancelable(cancelable);
    }

    @Override
    protected void onRealShow() {
        mDialog.show();
    }

    @Override
    protected void onRealCancel() {
        mDialog.cancel();
    }

    @Override
    protected void onRealDismiss() {
        mDialog.dismiss();
    }
}
