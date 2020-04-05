package com.soybeany.bdlib.android.web.okhttp;

import com.soybeany.bdlib.android.web.RequestNotifier;
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
    private final RequestNotifier mNotifier;

    public NotifierCall(Call target, RequestNotifier notifier) {
        super(target);
        mNotifier = notifier;
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
        return new NotifierCall(super.clone(), mNotifier);
    }

    public RequestNotifier getNotifier() {
        return mNotifier;
    }

    private class CallbackWrapper implements Callback, ITarget<RequestMsg.Invoker> {
        private final MsgManager<RequestMsg.Invoker, RequestMsg.Callback> mManager = new MsgManager<>();
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
        public void onSetupMsgProcessors(List<MsgProcessor<? extends RequestMsg.Invoker>> processors) {
            processors.add(new MsgProcessor<>(RequestMsg.Cancel.class, msg -> {
                cancel();
                mNotifier.sendCMsg(msg.senderUid, new RequestMsg.OnFinish(RequestFinishReason.CANCEL));
            }));
        }

        private void register(RequestNotifier notifier) {
            mManager.bind(this, notifier, false);
            mNotifier.sendCMsgWithDefaultUid(new RequestMsg.OnStart());
        }

        private void unregister(RequestFinishReason reason) {
            mNotifier.sendCMsgWithDefaultUid(new RequestMsg.OnFinish(reason));
            mManager.unbind(false);
        }
    }
}
