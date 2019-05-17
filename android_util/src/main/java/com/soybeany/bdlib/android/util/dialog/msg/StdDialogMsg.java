package com.soybeany.bdlib.android.util.dialog.msg;

import com.soybeany.bdlib.android.util.R;
import com.soybeany.bdlib.android.util.StdHintUtils;
import com.soybeany.bdlib.core.java8.Optional;

import static com.soybeany.bdlib.android.util.BDContext.getString;

/**
 * <br>Created by Soybeany on 2019/5/9.
 */
public class StdDialogMsg extends IDialogMsg.Impl {
    private static final String DEFAULT_HINT = getString(R.string.bd_dialog_default_hint);

    private String mPrefix = StdHintUtils.loadingPrefix();
    private String mSuffix = StdHintUtils.loadingSuffix2();

    @Override
    public String hint() {
        String hint = super.hint();
        return mPrefix + (null != hint ? hint : DEFAULT_HINT) + mSuffix;
    }

    public StdDialogMsg prefix(String text) {
        Optional.ofNullable(text).ifPresent(t -> mPrefix = t);
        return this;
    }

    public StdDialogMsg suffix(String text) {
        Optional.ofNullable(text).ifPresent(t -> mSuffix = t);
        return this;
    }
}
