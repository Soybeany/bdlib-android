package com.soybeany.bdlib.android.util.dialog;

import com.soybeany.bdlib.android.util.BDContext;
import com.soybeany.bdlib.core.util.notify.MessageCenter;

/**
 * <br>Created by Soybeany on 2019/4/15.
 */
public class DialogKeyProvider {
    private final String mUid = BDContext.getUID(); // 用于后续的监听Key

    // //////////////////////////////////通知用KEY//////////////////////////////////

    /**
     * 在{@link MessageCenter}中使用该Key，用于显示信息，data为{@link DialogMsg}
     */
    public final String showMsgKey = "DialogVM-showMsg:" + mUid;

    /**
     * 在{@link MessageCenter}中使用该Key，用于取消显示信息，data为{@link DialogMsg}
     */
    public final String cancelMsgKey = "DialogVM-cancelMsg:" + mUid;

    /**
     * 在{@link MessageCenter}中使用该Key，用于弹窗信息，data为{@link DialogMsg}
     */
    public final String popMsgKey = "DialogVM-popMsg:" + mUid;

    /**
     * 在{@link MessageCenter}中使用该Key，用于关闭弹窗，data为{@link DialogViewModel.Reason}
     */
    public final String dismissDialogKey = "DialogVM-dismissDialog:" + mUid;

    // //////////////////////////////////监听用KEY//////////////////////////////////

    /**
     * 可以在{@link MessageCenter}中监听该Key，获得显示信息时的通知，data为{@link DialogMsg}
     */
    public final String onShowMsgKey = "DialogVM-onShowMsg:" + mUid;

    /**
     * 可以在{@link MessageCenter}中监听该Key，获得重新显示信息时的通知，data为{@link DialogMsg}
     */
    public final String onReShowMsgKey = "DialogVM-onReShowMsg:" + mUid;

    /**
     * 可以在{@link MessageCenter}中监听该Key，获得弹出信息时的通知，data为{@link DialogMsg}
     */
    public final String onPopMsgKey = "DialogVM-onPopMsg:" + mUid;

    /**
     * 可以在{@link MessageCenter}中监听该Key，获得弹窗打开时的通知，data为null
     */
    public final String onShowDialogKey = "DialogVM-onShowDialog:" + mUid;

    /**
     * 可以在{@link MessageCenter}中监听该Key，获得弹窗关闭时的通知，data为{@link DialogViewModel.Reason}
     */
    public final String onDismissDialogKey = "DialogVM-onDismissDialog:" + mUid;
}
