package com.soybeany.bdlib.android.util.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg;
import com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg;
import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.notify.Notifier;

/**
 * DialogFragment实现
 * <br>Created by Soybeany on 2019/5/10.
 */
public abstract class NotifyDialogFragment extends DialogFragment implements NotifyDialogDelegate.IRealDialog {
    @Nullable
    private NotifyDialogDelegate mDelegate;
    @Nullable
    private AppCompatActivity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Optional.ofNullable(mDelegate).ifPresent(NotifyDialogDelegate::onCancel);
    }

    @Override
    public void realShow() {
        Optional.ofNullable(mActivity).ifPresent(activity -> show(activity.getSupportFragmentManager(), null));
    }

    @Override
    public void realDismiss() {
        dismiss();
    }

    public Notifier<DialogInvokerMsg, DialogCallbackMsg> getNotifier() {
        return null != mDelegate ? mDelegate.getNotifier() : null;
    }

    public void bind(@NonNull AppCompatActivity activity) {
        mActivity = activity;
        mDelegate = new NotifyDialogDelegate(activity, this);
    }
}
