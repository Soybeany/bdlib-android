package com.soybeany.bdlib.android.util.dialog;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.java8.function.Supplier;
import com.soybeany.bdlib.core.util.notify.INotifyMsg;

import static com.soybeany.bdlib.android.util.dialog.DialogDismissReason.CANCEL;

/**
 * DialogFragment实现，使用{@link #get(FragmentActivity, String, Supplier)}获得实例
 * <br>Created by Soybeany on 2019/5/10.
 */
public abstract class NotifyDialogFragment extends DialogFragment implements DialogNotifier.IDialog, DialogNotifierDelegate.IRealDialog {
    private final DialogNotifierDelegate mDelegate = new DialogNotifierDelegate(this);
    private FragmentActivity mActivity;

    @SuppressWarnings("unchecked")
    public static <T extends NotifyDialogFragment> NotifyDialogFragment get(@NonNull FragmentActivity activity, @NonNull String uid, @NonNull Supplier<? extends T> supplier) {
        T fragment = Optional.ofNullable((T) activity.getSupportFragmentManager().findFragmentByTag(uid)).orElseGet(supplier);
        fragment.onBindFragmentActivity(activity);
        return fragment;
    }

    @Override
    public void onCall(INotifyMsg msg) {
        mDelegate.onCall(msg);
    }

    @Override
    public void onBindNotifier(String type, DialogNotifier notifier) {
        mDelegate.onBindNotifier(type, notifier);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        mDelegate.invokeDismissDialog(CANCEL);
    }

    @Override
    public void realShow() {
        DialogNotifier notifier = mDelegate.getDialogNotifier();
        if (null != notifier) {
            show(mActivity.getSupportFragmentManager(), notifier.uid);
        }
    }

    @Override
    public void realDismiss() {
        dismiss();
    }

    protected void onBindFragmentActivity(FragmentActivity activity) {
        mActivity = activity;
    }
}
