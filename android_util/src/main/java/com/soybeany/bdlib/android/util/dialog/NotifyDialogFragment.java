package com.soybeany.bdlib.android.util.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg;
import com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg;
import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.java8.function.Supplier;
import com.soybeany.bdlib.core.util.notify.Notifier;

/**
 * DialogFragment实现，需在创建后调用{@link #bind(AppCompatActivity)}绑定Activity
 * <br>Created by Soybeany on 2019/5/10.
 */
public abstract class NotifyDialogFragment extends DialogFragment implements NotifyDialogDelegate.IRealDialog {

    @SuppressWarnings("unchecked")
    public static <T extends NotifyDialogFragment> T getFragment(@NonNull FragmentActivity activity, @NonNull NotifyDialogDelegate delegate, @NonNull Supplier<? extends T> other) {
        T fragment = (T) activity.getSupportFragmentManager().findFragmentByTag(delegate.uid);
        return null != fragment ? fragment : other.get();
    }

    @Nullable
    private AppCompatActivity mActivity;
    @Nullable
    private NotifyDialogDelegate mDelegate;

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Optional.ofNullable(mDelegate).ifPresent(NotifyDialogDelegate::onCancel);
    }

    @Override
    public void realShow() {
        if (null != mDelegate) {
            Optional.ofNullable(mActivity).ifPresent(activity -> show(activity.getSupportFragmentManager(), mDelegate.uid));
        }
    }

    @Override
    public void realDismiss() {
        dismiss();
    }

    // //////////////////////////////////外部调用区//////////////////////////////////

    @Nullable
    public Notifier<DialogInvokerMsg, DialogCallbackMsg> getNotifier() {
        return null != mDelegate ? mDelegate.getNotifier() : null;
    }

    /**
     * onCreate中调用
     */
    public NotifyDialogFragment bind(@NonNull AppCompatActivity activity) {
        mActivity = activity;
        mDelegate = ViewModelProviders.of(activity).get(NotifyDialogDelegate.class);
        mDelegate.init(activity, this);
        return this;
    }
}
