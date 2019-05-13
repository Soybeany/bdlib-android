package com.soybeany.bdlib.android.util.dialog.msg;

import static com.soybeany.bdlib.android.util.BDContext.getResources;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
public interface IDialogMsg extends Comparable<IDialogMsg> {

    IDialogMsg hint(int resId);

    IDialogMsg hint(String hint);

    String hint();

    IDialogMsg cancelable(boolean flag);

    boolean cancelable();

    IDialogMsg showTime(long time);

    /**
     * 此信息被显示的时间
     */
    long showTime();

    @Override
    default int compareTo(IDialogMsg o) {
        return (int) (showTime() - o.showTime());
    }

    class Impl implements IDialogMsg {
        private String mHint;
        private boolean mCancelable;
        private long mShowTime;

        @Override
        public IDialogMsg hint(int resId) {
            return hint(getResources().getString(resId));
        }

        @Override
        public IDialogMsg hint(String hint) {
            mHint = hint;
            return this;
        }

        @Override
        public String hint() {
            return mHint;
        }

        @Override
        public IDialogMsg cancelable(boolean flag) {
            mCancelable = flag;
            return this;
        }

        @Override
        public boolean cancelable() {
            return mCancelable;
        }

        @Override
        public IDialogMsg showTime(long time) {
            mShowTime = time;
            return this;
        }

        @Override
        public long showTime() {
            return mShowTime;
        }
    }

}
