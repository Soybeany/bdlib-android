package com.soybeany.bdlib.android.util.dialog;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

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
        showNow(mActivity.getSupportFragmentManager(), mUid);
    }

    @Override
    public void onDismissDialog(DialogDismissReason reason) {
        dismissAllowingStateLoss();
    }

    @Override
    public void onToProgress(float percent) {
    }

}
