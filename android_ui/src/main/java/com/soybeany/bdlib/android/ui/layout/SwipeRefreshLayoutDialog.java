package com.soybeany.bdlib.android.ui.layout;


import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.android.util.dialog.IRealDialog;
import com.soybeany.bdlib.android.util.dialog.msg.IDialogHint;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * <br>Created by Soybeany on 2019/5/17.
 */
public class SwipeRefreshLayoutDialog implements IRealDialog {
    private final SwipeRefreshLayout mLayout;

    public SwipeRefreshLayoutDialog(SwipeRefreshLayout layout) {
        mLayout = layout;
    }

    @Override
    public void onShowDialog() {
        mLayout.setRefreshing(true);
    }

    @Override
    public void onToProgress(float percent) {
        // 留空
    }

    @Override
    public void onDisplayHint(IDialogHint hint) {
        // 留空
    }

    @Override
    public void onChangeCancelable(boolean cancelable) {
        // 留空
    }

    @Override
    public void onDismissDialog(DialogDismissReason reason) {
        mLayout.setRefreshing(false);
    }
}
