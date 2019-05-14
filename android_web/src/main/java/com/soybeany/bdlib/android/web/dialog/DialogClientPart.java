package com.soybeany.bdlib.android.web.dialog;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg;
import com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg;
import com.soybeany.bdlib.core.util.IterableUtils;
import com.soybeany.bdlib.core.util.notify.Notifier;
import com.soybeany.bdlib.web.okhttp.OkHttpUtils;
import com.soybeany.bdlib.web.okhttp.core.OkHttpClientFactory;
import com.soybeany.bdlib.web.okhttp.notify.NotifyCall;
import com.soybeany.bdlib.web.okhttp.notify.RequestCallbackMsg;
import com.soybeany.bdlib.web.okhttp.notify.RequestInvokerMsg;

import java.util.HashSet;
import java.util.Set;

import okhttp3.OkHttpClient;

/**
 * <br>Created by Soybeany on 2019/5/10.
 */
public class DialogClientPart extends OkHttpUtils.ClientPart {
    @Override
    public DialogClientPart addSetter(OkHttpClientFactory.IClientSetter setter) {
        super.addSetter(setter);
        return this;
    }

    @Override
    public DialogClientPart removeSetter(OkHttpClientFactory.IClientSetter setter) {
        super.removeSetter(setter);
        return this;
    }

    @Override
    public DialogRequestPart newRequest() {
        return new DialogRequestPart(newClient());
    }

    // //////////////////////////////////内部类区//////////////////////////////////

    public static class DialogRequestPart extends OkHttpUtils.RequestPart {
        private final Set<RequestDialogConnector> mConnectors = new HashSet<>();
        @Nullable
        private Notifier<DialogInvokerMsg, DialogCallbackMsg> mDialogNotifier;
        @Nullable
        private IDialogMsgProvider mMsgProvider;

        DialogRequestPart(OkHttpClient client) {
            super(client);
        }

        @Override
        public NotifyCall newCall(OkHttpUtils.RequestGetter getter) {
            return newCall(getter, true);
        }

        public NotifyCall newCall(OkHttpUtils.RequestGetter getter, boolean addDefaultDealers) {
            if (addDefaultDealers) {
                mConnectors.add(new StdConnector());
            }
            NotifyCall call = super.newCall(getter);
            connect(call.getNotifier());
            return call;
        }

        public DialogRequestPart configBinder(@NonNull IBinderSetter setter) {
            setter.onSetup(mConnectors);
            return this;
        }

        public DialogRequestPart showDialog(Notifier<DialogInvokerMsg, DialogCallbackMsg> dialogNotifier, IDialogMsgProvider provider) {
            mDialogNotifier = dialogNotifier;
            mMsgProvider = provider;
            return this;
        }

        private void connect(@Nullable Notifier<RequestInvokerMsg, RequestCallbackMsg> requestNotifier) {
            // 信息不完整则不再继续
            if (mConnectors.isEmpty() || null == requestNotifier || null == mDialogNotifier || null == mMsgProvider) {
                return;
            }
            // 绑定
            IterableUtils.forEach(mConnectors, (dealer, flag) -> {
                requestNotifier.callback().addDealer(dealer);
                mDialogNotifier.callback().addDealer(dealer);
                dealer.onBindNotifier(mDialogNotifier, requestNotifier);
                dealer.onBindDialogMsgProvider(mMsgProvider);
            });
        }
    }

    // //////////////////////////////////接口区//////////////////////////////////

    public interface IBinderSetter {
        void onSetup(Set<RequestDialogConnector> connectors);
    }
}
