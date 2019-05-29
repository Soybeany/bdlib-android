package com.soybeany.bdlib.android.web.dialog;

import com.soybeany.bdlib.android.util.dialog.DialogNotifier;
import com.soybeany.bdlib.android.util.dialog.msg.DialogNotifierMsg.PopMsg;
import com.soybeany.bdlib.android.util.dialog.msg.IDialogMsg;
import com.soybeany.bdlib.core.util.notify.IConnectLogic;
import com.soybeany.bdlib.web.okhttp.notify.RequestNotifier;
import com.soybeany.bdlib.web.okhttp.notify.RequestNotifierMsg.Cancel;
import com.soybeany.bdlib.web.okhttp.notify.RequestNotifierMsg.OnFinish;

import static com.soybeany.bdlib.android.util.dialog.msg.DialogNotifierMsg.OnDismissDialog;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogNotifierMsg.ShowMsg;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogNotifierMsg.ToProgress;
import static com.soybeany.bdlib.web.okhttp.notify.OkHttpNotifierUtils.IConnectorSetter.invoke;
import static com.soybeany.bdlib.web.okhttp.notify.RequestNotifierMsg.OnDownload;
import static com.soybeany.bdlib.web.okhttp.notify.RequestNotifierMsg.OnStart;
import static com.soybeany.bdlib.web.okhttp.notify.RequestNotifierMsg.OnUpload;

/**
 * 标准弹窗逻辑（默认）
 * <br>Created by Soybeany on 2019/5/28.
 */
public class DialogLogicSetter implements DialogClientPart.ILogicSetter {
    @Override
    public void onSetup(IConnectLogic.IApplier<RequestNotifier, DialogNotifier> applier, IDialogMsgProvider msgProvider) {
        // 请求部分
        IDialogMsg dialogMsg = msgProvider.getMsg(ShowMsg.class);
        applier.addLogic(OnStart.class, (msg, rn, dn) -> invoke(dn, new ShowMsg(dialogMsg)));
        ToProgress progress = new ToProgress();
        applier.addLogic(OnUpload.class, (msg, rn, dn) -> invoke(dn, progress.toPercent(msg.getData())));
        applier.addLogic(OnDownload.class, (msg, rn, dn) -> invoke(dn, progress.toPercent(msg.getData())));
        applier.addLogic(OnFinish.class, (msg, rn, dn) -> invoke(dn, new PopMsg(dialogMsg)));
        // 弹窗部分
        applier.addLogic(OnDismissDialog.class, (msg, rn, dn) -> invoke(rn, new Cancel()));
    }
}
