package com.soybeany.bdlib.android.web.dialog;

import com.soybeany.bdlib.android.util.dialog.msg.IDialogMsg;

/**
 * <br>Created by Soybeany on 2019/5/10.
 */
public interface IDialogMsgProvider {
    IDialogMsg getMsg(String when);
}
