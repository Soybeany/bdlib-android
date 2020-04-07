package com.soybeany.bdlib.android.util.dialog.msg;

import java.util.concurrent.atomic.AtomicInteger;

import static com.soybeany.bdlib.android.util.BDContext.getString;

/**
 * <br>Created by Soybeany on 2020/4/7.
 */
public class SimpleDialogHint implements IDialogHint {

    private static AtomicInteger COUNTER = new AtomicInteger(0);

    private String mHint;
    private boolean mCancelable;
    private int mSerialNum;

    {
        mSerialNum = COUNTER.incrementAndGet();
    }

    @Override
    public IDialogHint hint(int resId) {
        return hint(getString(resId));
    }

    @Override
    public IDialogHint hint(String hint) {
        mHint = hint;
        return this;
    }

    @Override
    public String hint() {
        return mHint;
    }

    @Override
    public IDialogHint cancelable(boolean flag) {
        mCancelable = flag;
        return this;
    }

    @Override
    public boolean cancelable() {
        return mCancelable;
    }

    @Override
    public int serialNum() {
        return mSerialNum;
    }
}
