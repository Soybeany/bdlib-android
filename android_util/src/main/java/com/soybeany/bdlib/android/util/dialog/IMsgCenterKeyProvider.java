package com.soybeany.bdlib.android.util.dialog;

import com.soybeany.bdlib.core.util.storage.MessageCenter;

/**
 * <br>Created by Soybeany on 2019/4/15.
 */
public interface IMsgCenterKeyProvider {
    /**
     * 可以在{@link MessageCenter}中监听该Key，获得显示信息时的通知，data为{@link DialogMsg}
     */
    String getOnShowMsgKey();

    /**
     * 可以在{@link MessageCenter}中监听该Key，获得显示信息时的通知，data为{@link DialogMsg}
     */
    String getOnReShowMsgKey();

    /**
     * 可以在{@link MessageCenter}中监听该Key，获得弹出信息时的通知，data为{@link DialogMsg}
     */
    String getOnPopMsgKey();

    /**
     * 可以在{@link MessageCenter}中监听该Key，获得弹窗打开时的通知，data为null
     */
    String getOnShowDialogKey();

    /**
     * 可以在{@link MessageCenter}中监听该Key，获得弹窗关闭时的通知，data为{@link DialogViewModel.Reason}
     */
    String getOnDismissDialogKey();
}
