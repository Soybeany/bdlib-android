package com.soybeany.bdlib.android.ui.layout;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;

import com.soybeany.bdlib.android.util.dialog.DialogNotifier;
import com.soybeany.bdlib.android.util.dialog.NotifyDialogDelegate;

/**
 * <br>Created by Soybeany on 2019/5/17.
 */
public class NotifySRLHelper implements NotifyDialogDelegate.IRealDialog {

    @Nullable
    private final NotifyDialogDelegate mDelegate;
    private final SwipeRefreshLayout mLayout;

    public NotifySRLHelper(FragmentActivity activity, String type, SwipeRefreshLayout layout) {
        mDelegate = NotifyDialogDelegate.getNew(activity, type);
        mLayout = layout;
        mDelegate.init(this);
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

    @Nullable
    public DialogNotifier getDialogNotifier() {
        return null != mDelegate ? mDelegate.getNotifier() : null;
    }
}
