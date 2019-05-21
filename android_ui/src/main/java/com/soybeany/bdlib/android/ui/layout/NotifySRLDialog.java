package com.soybeany.bdlib.android.ui.layout;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;

import com.soybeany.bdlib.android.util.dialog.DialogNotifier;
import com.soybeany.bdlib.android.util.dialog.DialogNotifierDelegate;
import com.soybeany.bdlib.core.util.notify.INotifyMsg;

/**
 * <br>Created by Soybeany on 2019/5/17.
 */
public class NotifySRLDialog implements DialogNotifier.IDialog, DialogNotifierDelegate.IRealDialog {
    private final DialogNotifierDelegate mDelegate = new DialogNotifierDelegate(this);
    private final SwipeRefreshLayout mLayout;

    @Nullable
    private DialogNotifier mNotifier;

    public NotifySRLDialog(SwipeRefreshLayout layout) {
        mLayout = layout;
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

    /**
     * 是否在{@link ViewModel#onCleared()}时发送dismiss通知
     */
    public NotifySRLDialog notifyDismiss(boolean enable) {
        if (null != mNotifier) {
            mNotifier.needOnClearNotify = enable;
        }
        return this;
    }

    @Override
    public void onBindNotifier(String type, DialogNotifier notifier) {
        mDelegate.onBindNotifier(type, notifier);
        mNotifier = notifier;
        // 设置默认状态
        if (mLayout.isRefreshing() != notifier.isDialogShowing) {
            mLayout.setRefreshing(notifier.isDialogShowing);
        }
    }

    @Override
    public void onCall(INotifyMsg msg) {
        mDelegate.onCall(msg);
    }
}
