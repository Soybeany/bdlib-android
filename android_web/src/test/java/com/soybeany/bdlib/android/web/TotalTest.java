package com.soybeany.bdlib.android.web;

import com.soybeany.bdlib.android.util.dialog.msg.IDialogHint;
import com.soybeany.bdlib.android.web.dialog.DialogMsg;
import com.soybeany.bdlib.android.web.okhttp.RequestFinishReason;
import com.soybeany.bdlib.android.web.okhttp.RequestMsg;
import com.soybeany.connector.ITarget;
import com.soybeany.connector.MsgManager;
import com.soybeany.connector.MsgSender;

import org.junit.Test;

import java.util.List;

/**
 * <br>Created by Soybeany on 2020/4/1.
 */
public class TotalTest {

    @Test
    public void test() throws Exception {
        // 创建目标实例
        Dialog dialog = new Dialog();
        RequestUtils.get()
                .dNotifier(dialog.dNotifier)
//                .hint(new IDialogHint.Impl().hint("测2试"))
                .startRequest();
    }

    // //////////////////////////////////请求框架//////////////////////////////////

    private static class RequestUtils {

        public static Builder get() {
            return new Builder();
        }

    }

    private static class Builder {
        private DialogNotifier mDNotifier;
        private IDialogHint mHint;

        public Builder dNotifier(DialogNotifier notifier) {
            mDNotifier = notifier;
            return this;
        }

        public Builder hint(IDialogHint hint) {
            mHint = hint;
            return this;
        }

        public void startRequest() {
            Request request = new Request();
            // 绑定
            RequestNotifier rNotifier = new RequestNotifier(mHint);
            request.bind(rNotifier);
            // 若有弹窗，则绑定
            if (null != mDNotifier) {
                MsgSender.connect(mDNotifier, rNotifier);
            }
            // 模拟发送
            request.sendMsg(new RequestMsg.OnStart());
            request.sendMsg(new RequestMsg.OnFinish(RequestFinishReason.NORM));
        }
    }

    // //////////////////////////////////请求Target//////////////////////////////////

    private static class Request implements ITarget<RequestMsg.Invoker> {
        private final MsgManager<RequestMsg.Invoker, RequestMsg.Callback> mManager = new MsgManager<>();

        @Override
        public void onSetupMsgProcessors(List<MsgProcessor<? extends RequestMsg.Invoker>> msgProcessors) {
            msgProcessors.add(new MsgProcessor<>(RequestMsg.Cancel.class, msg -> {
                System.out.println("取消请求");
                mManager.sendMsg(new RequestMsg.OnFinish(RequestFinishReason.CANCEL));
            }));
        }

        void bind(RequestNotifier sender) {
            mManager.bind(this, sender);
        }

        void sendMsg(RequestMsg.Callback msg) {
            mManager.sendMsg(msg);
        }
    }

    // //////////////////////////////////弹窗Target//////////////////////////////////

    private static class Dialog implements ITarget<DialogMsg.Invoker> {
        final DialogNotifier dNotifier = new DialogNotifier();
        private final MsgManager<DialogMsg.Invoker, DialogMsg.Callback> mManager = new MsgManager<>();

        {
            mManager.bind(this, dNotifier);
        }

        @Override
        public void onSetupMsgProcessors(List<MsgProcessor<? extends DialogMsg.Invoker>> msgProcessors) {
            msgProcessors.add(new MsgProcessor<>(DialogMsg.ShowMsg.class, msg -> System.out.println("显示弹窗:" + getHintFromMsg(msg))));
            msgProcessors.add(new MsgProcessor<>(DialogMsg.PopMsg.class, msg -> {
                System.out.println("关闭弹窗:" + getHintFromMsg(msg));
                mManager.sendMsg(new DialogMsg.OnDismissDialog(null));
            }));
        }

        private String getHintFromMsg(DialogMsg.Invoker<IDialogHint> msg) {
            IDialogHint data = msg.getData();
            return null != data ? data.hint() : "默认消息";
        }
    }

}