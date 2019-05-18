package com.soybeany.bdlib.android.ui.layout;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;

import com.soybeany.bdlib.android.util.dialog.DialogNotifier;
import com.soybeany.bdlib.android.util.dialog.DialogNotifierDelegate;

/**
 * <br>Created by Soybeany on 2019/5/17.
 */
public class NotifySRLDialog implements DialogNotifierDelegate.IRealDialog {
    private final SwipeRefreshLayout mLayout;
    private final DialogNotifierDelegate mDelegate;

    private boolean mIsNotifyDismiss;

    public NotifySRLDialog(FragmentActivity activity, String type, SwipeRefreshLayout layout) {
        mLayout = layout;
        DialogNotifierDelegate.Unbind unbind = DialogNotifierDelegate.getNew(activity, type);
        mDelegate = unbind.bind(this);
    }

    @Override
    public void onInit(boolean isDialogShowing) {
        mLayout.setRefreshing(isDialogShowing);
    }

    @Override
    public void onObserveMsg(@NonNull LiveData<String> hint, @NonNull LiveData<Boolean> cancelable) {
        // 留空
    }

    @Override
    public void realShow() {
        mLayout.setRefreshing(true);
    }

    @Override
    public void realToProgress(float percent) {
        // 留空
    }

    @Override
    public void realCancel() {
        realDismiss();
    }

    @Override
    public void realDismiss() {
        mLayout.setRefreshing(false);
    }

    @Override
    public boolean shouldCancelOnClear() {
        return mIsNotifyDismiss;
    }

    @Nullable
    @Override
    public DialogNotifier getDialogNotifier(String type) {
        return mDelegate.getDialogNotifier(type);
    }

    /**
     * 是否在{@link ViewModel#onCleared()}时发送dismiss通知
     */
    public NotifySRLDialog notifyDismiss(boolean enable) {
        mIsNotifyDismiss = enable;
        return this;
    }
}
