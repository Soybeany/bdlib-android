package com.soybeany.bdlib.android.web.dialog;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg;
import com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg;
import com.soybeany.bdlib.core.util.notify.Notifier;
import com.soybeany.bdlib.web.okhttp.OkHttpUtils;
import com.soybeany.bdlib.web.okhttp.core.OkHttpClientFactory;
import com.soybeany.bdlib.web.okhttp.notify.NotifyCall;

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
        private final RequestDialogConnector mConnector = new RequestDialogConnector();
        private final Set<IBinderOnCallDealer> mDealers = new HashSet<>();
        @Nullable
        private Notifier<DialogInvokerMsg, DialogCallbackMsg> mDialogNotifier;

        DialogRequestPart(OkHttpClient client) {
            super(client);
        }

        @Override
        public NotifyCall newCall(OkHttpUtils.RequestGetter getter) {
            return newCall(getter, true);
        }

        public NotifyCall newCall(OkHttpUtils.RequestGetter getter, boolean addDefaultDealers) {
            if (addDefaultDealers) {
                mConnector.dealers.add(new StdBinderOnCallDealer());
            }
            NotifyCall call = super.newCall(getter);
            mConnector.connect(call.getNotifier(), mDialogNotifier);
            return call;
        }

        public DialogRequestPart configBinder(@NonNull IBinderSetter setter) {
            setter.onSetup(mConnector.dealers);
            return this;
        }

        public DialogRequestPart showDialog(Notifier<DialogInvokerMsg, DialogCallbackMsg> dialogNotifier, IDialogMsgProvider provider) {
            mDialogNotifier = dialogNotifier;
            mConnector.setDialogMsgProvider(provider);
            return this;
        }
    }

    // //////////////////////////////////接口区//////////////////////////////////

    public interface IBinderSetter {
        void onSetup(Set<IBinderOnCallDealer> dealers);
    }
}
