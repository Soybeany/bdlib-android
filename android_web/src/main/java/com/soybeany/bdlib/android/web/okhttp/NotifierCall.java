package com.soybeany.bdlib.android.web.okhttp;

import com.soybeany.bdlib.android.web.msg.RMsg;
import com.soybeany.bdlib.android.web.notifier.DialogNotifier;
import com.soybeany.bdlib.android.web.notifier.RSender;
import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.web.okhttp.core.CallWrapper;
import com.soybeany.connector.ITarget;
import com.soybeany.connector.MsgManager;

import java.io.IOException;
import java.util.List;

import androidx.annotation.Nullable;
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
    private final RSender mSender;
    @Nullable
    private final DialogNotifier mDialogNotifier; // 用于通讯的另一个notifier

    public NotifierCall(Call target, RSender sender, @Nullable DialogNotifier dialogNotifier) {
        super(target);
        mSender = sender;
        mDialogNotifier = dialogNotifier;
    }

    @Override
    public void enqueue(Callback callback) {
        if (callback instanceof NotifierCallback) {
            ((NotifierCallback<?>) callback).setSender(mSender);
        }
        super.enqueue(new CallbackWrapper(callback));
    }

    @Override
    public Call clone() {
        return new NotifierCall(super.clone(), mSender, mDialogNotifier);
    }

    public RSender getSender() {
        return mSender;
    }

    private class CallbackWrapper implements Callback, ITarget<RMsg.Invoker<?>> {
        private final MsgManager<RMsg.Invoker<?>, RMsg.Callback<?>> mManager = new MsgManager<>();
        private Callback mTarget;

        CallbackWrapper(Callback target) {
            mTarget = target;
            Optional.ofNullable(mSender).ifPresent(this::register);
        }

        @Override
        public void onFailure(Call call, IOException e) {
            mTarget.onFailure(call, e);
            Optional.ofNullable(mSender).ifPresent(notifier -> unregister(call.isCanceled() ? CANCEL : ERROR));
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            mTarget.onResponse(call, response);
            Optional.ofNullable(mSender).ifPresent(notifier -> unregister(NORM));
        }

        @Override
        public void onSetupMsgProcessors(List<MsgProcessor<? extends RMsg.Invoker<?>>> processors) {
            processors.add(new MsgProcessor<>(RMsg.Cancel.class, msg -> cancel()));
        }

        private void register(RSender sender) {
            if (null != mDialogNotifier) {
                Optional.ofNullable(mDialogNotifier.receiver).ifPresent(receiver -> receiver.connect(mSender, false));
                mSender.connect(mDialogNotifier.sender, false);
            }
            mManager.bind(this, sender, false);
            mSender.sendCMsg(new RMsg.OnStart());
        }

        private void unregister(RequestFinishReason reason) {
            mSender.sendCMsg(new RMsg.OnFinish(reason));
            mManager.unbind(true);
            if (null != mDialogNotifier) {
                Optional.ofNullable(mDialogNotifier.receiver).ifPresent(receiver -> receiver.disconnect(mSender, true));
                mSender.disconnect(mDialogNotifier.sender, true);
            }
        }
    }
}
