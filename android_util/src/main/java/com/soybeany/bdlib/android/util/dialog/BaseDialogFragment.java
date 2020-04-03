package com.soybeany.bdlib.android.util.dialog;

import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

/**
 * <br>Created by Soybeany on 2020/4/3.
 */
public abstract class BaseDialogFragment extends DialogFragment implements IRealDialog {

    @Nullable
    protected FragmentActivity mActivity;
    @Nullable
    protected String mUid;

    @Override
    public void onShowDialog() {
        if (isAdded()) {
            return;
        }
        if (null == mActivity || null == mUid) {
            throw new RuntimeException("mActivity或mUid不能为null");
        }
        show(mActivity.getSupportFragmentManager(), mUid);
    }

    @Override
    public void onDismissDialog(DialogDismissReason reason) {
        dismiss();
    }

    @Override
    public void onToProgress(float percent) {
    }

}
