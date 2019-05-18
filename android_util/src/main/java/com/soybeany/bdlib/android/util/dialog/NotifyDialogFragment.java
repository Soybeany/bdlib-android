package com.soybeany.bdlib.android.util.dialog;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.java8.function.Supplier;

import static com.soybeany.bdlib.android.util.dialog.DialogDismissReason.CANCEL;

/**
 * DialogFragment实现，使用{@link #getFragment(FragmentActivity, String, Supplier)}获得实例
 * <br>Created by Soybeany on 2019/5/10.
 */
public abstract class NotifyDialogFragment extends DialogFragment implements DialogNotifierDelegate.IRealDialog {
    @Nullable
    private FragmentActivity mActivity;
    @Nullable
    private DialogNotifierDelegate mDelegate;

    @SuppressWarnings("unchecked")
    public static <T extends NotifyDialogFragment> T getFragment(@NonNull FragmentActivity activity, String type, @NonNull Supplier<? extends T> other) {
        DialogNotifierDelegate.Unbind unbind = DialogNotifierDelegate.getNew(activity, type);
        T fragment = (T) activity.getSupportFragmentManager().findFragmentByTag(unbind.uid());
        if (null == fragment) {
            fragment = other.get();
        }
        fragment.onBindRealDialogToDelegate(activity, unbind);
        return fragment;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Optional.ofNullable(mDelegate).ifPresent(delegate -> delegate.invokeDismissDialog(CANCEL));
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

    @Nullable
    @Override
    public DialogNotifier getDialogNotifier(String type) {
        return null != mDelegate ? mDelegate.getDialogNotifier(type) : null;
    }

    // //////////////////////////////////内部方法//////////////////////////////////

    protected void onBindRealDialogToDelegate(@NonNull FragmentActivity activity, @NonNull DialogNotifierDelegate.Unbind unbind) {
        mActivity = activity;
        mDelegate = unbind.bind(this);
    }
}
