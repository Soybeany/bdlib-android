package com.soybeany.bdlib.android.web.dialog;

import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.dialog.DialogNotifier;
import com.soybeany.bdlib.android.util.dialog.msg.DialogNotifierMsg.OnDismissDialog;
import com.soybeany.bdlib.core.util.notify.IConnectLogic;
import com.soybeany.bdlib.core.util.notify.INotifyMsg;
import com.soybeany.bdlib.web.okhttp.core.OkHttpUtils;
import com.soybeany.bdlib.web.okhttp.notify.OkHttpNotifierUtils;
import com.soybeany.bdlib.web.okhttp.notify.OkHttpNotifierUtils.IConnectorSetter;
import com.soybeany.bdlib.web.okhttp.notify.RequestNotifier;

import java.util.HashSet;
import java.util.Set;

import okhttp3.OkHttpClient;

/**
 * <br>Created by Soybeany on 2019/5/10.
 */
public class DialogClientPart extends OkHttpNotifierUtils.ClientPart<DialogNotifier> {
    private final DialogConnectSetter mDialogConnectSetter = new DialogConnectSetter();
    private boolean mNeedDefaultLogic = true;

    @Override
    public DialogClientPart addSetter(OkHttpUtils.IClientSetter setter) {
        super.addSetter(setter);
        return this;
    }

    @Override
    public DialogClientPart removeSetter(OkHttpUtils.IClientSetter setter) {
        super.removeSetter(setter);
        return this;
    }

    @Override
    public DialogClientPart connector(IConnectorSetter<DialogNotifier> cSetter) {
        throw new RuntimeException("不能重新设置连接器，请使用addLogic/removeLogic处理逻辑");
    }

    @Override
    public DialogRequestPart newNotifierRequest() {
        if (mNeedDefaultLogic) {
            mDialogConnectSetter.addLogicSetter(new DialogLogicSetter());
        }
        return new DialogRequestPart(newClient(), mDialogConnectSetter);
    }

    /**
     * 是否需要默认的弹窗逻辑(默认true)
     */
    public DialogClientPart needDefaultLogic(boolean flag) {
        mNeedDefaultLogic = flag;
        return this;
    }

    public DialogClientPart addLogic(ILogicSetter setter) {
        mDialogConnectSetter.addLogicSetter(setter);
        return this;
    }

    public DialogClientPart removeLogic(ILogicSetter setter) {
        mDialogConnectSetter.removeLogicSetter(setter);
        return this;
    }

    // //////////////////////////////////内部类区//////////////////////////////////

    public static class DialogRequestPart extends OkHttpNotifierUtils.RequestPart<DialogNotifier> {
        private DialogConnectSetter mSetter;

        DialogRequestPart(OkHttpClient client, DialogConnectSetter setter) {
            super(client, setter);
            mSetter = setter;
        }

        public DialogRequestPart showDialog(@Nullable DialogNotifier dialogNotifier, @Nullable IDialogMsgProvider provider) {
            mSetter.setNotifierAndMsgProvider(dialogNotifier, provider);
            return this;
        }
    }

    // //////////////////////////////////接口区//////////////////////////////////

    public interface ILogicSetter {
        void onSetup(IConnectLogic.IApplier<RequestNotifier, DialogNotifier> applier, IDialogMsgProvider msgProvider);
    }

    private static class DialogConnectSetter implements IConnectorSetter<DialogNotifier> {
        private final Set<ILogicSetter> mLogicSet = new HashSet<>();
        @Nullable
        private DialogNotifier mDialogNotifier;
        @Nullable
        private IDialogMsgProvider mMsgProvider;

        @Override
        public DialogNotifier getNewNotifier() {
            return mDialogNotifier;
        }

        @Override
        public Class<? extends INotifyMsg> getDMClass() {
            return OnDismissDialog.class;
        }

        @Override
        public void onSetupLogic(IConnectLogic.IApplier<RequestNotifier, DialogNotifier> applier) {
            if (null == mDialogNotifier || null == mMsgProvider) {
                return;
            }
            for (ILogicSetter setter : mLogicSet) {
                setter.onSetup(applier, mMsgProvider);
            }
        }

        void setNotifierAndMsgProvider(@Nullable DialogNotifier dialogNotifier, @Nullable IDialogMsgProvider provider) {
            mDialogNotifier = dialogNotifier;
            mMsgProvider = provider;
        }

        void addLogicSetter(ILogicSetter setter) {
            mLogicSet.add(setter);
        }

        void removeLogicSetter(ILogicSetter setter) {
            mLogicSet.remove(setter);
        }
    }
}
