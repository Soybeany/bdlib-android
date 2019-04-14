package com.soybeany.bdlib.android.util.dialog;

import com.soybeany.bdlib.core.util.storage.MessageCenter;

/**
 * <br>Created by Soybeany on 2019/4/14.
 */
public interface IDialog {

    void show(DialogMsg msg);

    void cancelAll();

    void cancel(DialogMsg msg);

    void dismissAll();

    void dismiss(DialogMsg msg);

    /**
     * 获得弹窗在{@link MessageCenter}中注册时用的Key
     */
    String getMsgCenterKey();

    /**
     * 获得当前显示中的{@link DialogMsg}
     */
    DialogMsg curMsg();
}
