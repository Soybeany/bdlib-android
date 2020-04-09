package com.soybeany.bdlib.android.web.okhttp;

import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.web.msg.RMsg;
import com.soybeany.bdlib.android.web.notifier.DialogNotifier;
import com.soybeany.bdlib.android.web.notifier.RNotifier;
import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.web.okhttp.core.CallWrapper;
import com.soybeany.connector.ITarget;
import com.soybeany.connector.MsgManager;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.internal.annotations.EverythingIsNonNull;

import static com.soybeany.bdlib.android.web.okhttp.RequestFinishReason.CANCEL;
import static com.soybeany.bdlib.android.web.okhttp.RequestFinishReason.ERROR;
import static com.soybeany.bdlib.android.web.okhttp.RequestFinishReason.NORM;


/**
 * 添加Notifier功能
 * <br>Created by Soybeany on 2019/5/7.
 */
@EverythingIsNonNull
public class NotifierCall extends CallWrapper {
    private final RNotifier mNotifier;
    @Nullable
    private final DialogNotifier mDialogNotifier; // 用于通讯的另一个notifier

    public NotifierCall(Call target, RNotifier notifier, @Nullable DialogNotifier dialogNotifier) {
        super(target);
        mNotifier = notifier;
        mDialogNotifier = dialogNotifier;
    }

    @Override
    public void enqueue(Callback callback) {
        if (callback instanceof NotifierCallback) {
            ((NotifierCallback) callback).setNotifier(mNotifier);
        }
        super.enqueue(new CallbackWrapper(callback));
    }

    @Override
    public Call clone() {
        return new NotifierCall(super.clone(), mNotifier, mDialogNotifier);
    }

    public RNotifier getNotifier() {
        return mNotifier;
    }

    private class CallbackWrapper implements Callback, ITarget<RMsg.Invoker> {
        private final MsgManager<RMsg.Invoker, RMsg.Callback> mManager = new MsgManager<>();
        private Callback mTarget;

        CallbackWrapper(Callback target) {
            mTarget = target;
            Optional.ofNullable(mNotifier).ifPresent(this::register);
        }

        @Override
        public void onFailure(Call call, IOException e) {
            mTarget.onFailure(call, e);
            Optional.ofNullable(mNotifier).ifPresent(notifier -> unregister(call.isCanceled() ? CANCEL : ERROR));
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            mTarget.onResponse(call, response);
            Optional.ofNullable(mNotifier).ifPresent(notifier -> unregister(NORM));
        }

        @Override
        public void onSetupMsgProcessors(List<MsgProcessor<? extends RMsg.Invoker>> processors) {
            processors.add(new MsgProcessor<>(RMsg.Cancel.class, msg -> cancel()));
        }

        private void register(RNotifier notifier) {
            if (null != mDialogNotifier) {
                Optional.ofNullable(mDialogNotifier.receiver).ifPresent(receiver -> receiver.connect(mNotifier, false));
                mNotifier.connect(mDialogNotifier.sender, false);
            }
            mManager.bind(this, notifier, false);
            mNotifier.sendCMsg(new RMsg.OnStart());
        }

        private void unregister(RequestFinishReason reason) {
            mNotifier.sendCMsg(new RMsg.OnFinish(reason));
            mManager.unbind(true);
            if (null != mDialogNotifier) {
                Optional.ofNullable(mDialogNotifier.receiver).ifPresent(receiver -> receiver.disconnect(mNotifier, true));
                mNotifier.disconnect(mDialogNotifier.sender, true);
            }
        }
    }
}
