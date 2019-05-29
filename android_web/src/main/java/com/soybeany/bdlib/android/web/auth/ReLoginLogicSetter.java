package com.soybeany.bdlib.android.web.auth;

import com.soybeany.bdlib.android.util.dialog.DialogNotifier;
import com.soybeany.bdlib.android.util.dialog.msg.DialogNotifierMsg.PopMsg;
import com.soybeany.bdlib.android.util.dialog.msg.IDialogMsg;
import com.soybeany.bdlib.android.web.auth.ReLoginInterceptor.OnReAuthFinish;
import com.soybeany.bdlib.android.web.auth.ReLoginInterceptor.OnReAuthStart;
import com.soybeany.bdlib.android.web.auth.ReLoginInterceptor.OnReRequestFinish;
import com.soybeany.bdlib.android.web.dialog.DialogClientPart;
import com.soybeany.bdlib.android.web.dialog.IDialogMsgProvider;
import com.soybeany.bdlib.core.util.notify.IConnectLogic;
import com.soybeany.bdlib.web.okhttp.notify.RequestNotifier;

import static com.soybeany.bdlib.android.util.dialog.msg.DialogNotifierMsg.ShowMsg;
import static com.soybeany.bdlib.android.web.auth.ReLoginInterceptor.OnReRequestStart;
import static com.soybeany.bdlib.web.okhttp.notify.OkHttpNotifierUtils.IConnectorSetter.invoke;

/**
 * 重登录弹窗逻辑
 * <br>Created by Soybeany on 2019/5/28.
 */
public class ReLoginLogicSetter implements DialogClientPart.ILogicSetter {
    @Override
    public void onSetup(IConnectLogic.IApplier<RequestNotifier, DialogNotifier> applier, IDialogMsgProvider msgProvider) {
        // 重登录
        IDialogMsg reAuthMsg = msgProvider.getMsg(OnReAuthStart.class);
        applier.addLogic(OnReAuthStart.class, (msg, rn, dn) -> invoke(dn, new ShowMsg(reAuthMsg)));
        applier.addLogic(OnReAuthFinish.class, (msg, rn, dn) -> invoke(dn, new PopMsg(reAuthMsg)));
        // 重请求
        IDialogMsg reRequestMsg = msgProvider.getMsg(OnReRequestStart.class);
        applier.addLogic(OnReRequestStart.class, (msg, rn, dn) -> invoke(dn, new ShowMsg(reRequestMsg)));
        applier.addLogic(OnReRequestFinish.class, (msg, rn, dn) -> invoke(dn, new PopMsg(reRequestMsg)));
    }
}
