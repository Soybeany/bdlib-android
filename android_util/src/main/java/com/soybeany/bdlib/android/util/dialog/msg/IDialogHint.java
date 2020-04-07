package com.soybeany.bdlib.android.util.dialog.msg;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
public interface IDialogHint extends Comparable<IDialogHint> {

    IDialogHint hint(int resId);

    IDialogHint hint(String hint);

    String hint();

    IDialogHint cancelable(boolean flag);

    boolean cancelable();

    /**
     * 此信息的序列号
     */
    int serialNum();

    @Override
    default int compareTo(IDialogHint o) {
        // 由小到大排序
        return serialNum() - o.serialNum();
    }

}
