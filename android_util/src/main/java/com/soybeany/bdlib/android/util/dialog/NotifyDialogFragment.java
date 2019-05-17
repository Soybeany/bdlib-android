package com.soybeany.bdlib.android.util.dialog;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.java8.function.Supplier;

/**
 * DialogFragment实现，使用{@link #getFragment(FragmentActivity, String, Supplier)}获得实例
 * <br>Created by Soybeany on 2019/5/10.
 */
public abstract class NotifyDialogFragment extends DialogFragment implements NotifyDialogDelegate.IRealDialog {
    @Nullable
    private FragmentActivity mActivity;
    @Nullable
    private NotifyDialogDelegate mDelegate;

    @SuppressWarnings("unchecked")
    public static <T extends NotifyDialogFragment> T getFragment(@NonNull FragmentActivity activity, String type, @NonNull Supplier<? extends T> other) {
        NotifyDialogDelegate delegate = NotifyDialogDelegate.getNew(activity, type);
        T fragment = (T) activity.getSupportFragmentManager().findFragmentByTag(delegate.uid);
        if (null == fragment) {
            fragment = other.get();
        }
        fragment.onBind(activity, delegate);
        return fragment;
    }

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

    // //////////////////////////////////内部方法//////////////////////////////////

    protected void onBind(@NonNull FragmentActivity activity, @NonNull NotifyDialogDelegate delegate) {
        mActivity = activity;
        mDelegate = delegate.init(this);
    }

    // //////////////////////////////////外部调用区//////////////////////////////////

    @Nullable
    public DialogNotifier getNotifier() {
        return null != mDelegate ? mDelegate.getNotifier() : null;
    }
}
