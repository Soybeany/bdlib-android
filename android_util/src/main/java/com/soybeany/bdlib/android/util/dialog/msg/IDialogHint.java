package com.soybeany.bdlib.android.util.dialog.msg;

import static com.soybeany.bdlib.android.util.BDContext.getString;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
public interface IDialogHint extends Comparable<IDialogHint> {

    IDialogHint hint(int resId);

    IDialogHint hint(String hint);

    String hint();

    IDialogHint cancelable(boolean flag);

    boolean cancelable();

    IDialogHint timestamp(long time);

    /**
     * 此信息被显示的时间
     */
    long timestamp();

    @Override
    default int compareTo(IDialogHint o) {
        // 由小到大排序
        return (int) (timestamp() - o.timestamp());
    }

    // //////////////////////////////////默认实现//////////////////////////////////

    class Impl implements IDialogHint {
        private String mHint;
        private boolean mCancelable;
        private long mTimestamp;

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
        public IDialogHint timestamp(long time) {
            mTimestamp = time;
            return this;
        }

        @Override
        public long timestamp() {
            return mTimestamp;
        }
    }

}
