//package com.soybeany.bdlib.android.web.dialog;
//
//import android.arch.lifecycle.ViewModelProviders;
//import android.support.v4.app.FragmentActivity;
//
//import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
//import com.soybeany.bdlib.android.web.msg.DialogMsg;
//import com.soybeany.bdlib.android.web.notifier.DialogNotifier;
//import com.soybeany.connector.ITarget;
//import com.soybeany.connector.MsgManager;
//
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * <br>Created by Soybeany on 2020/4/2.
// */
//// TODO: 2020/4/6
////  1.将Manager也放入这里管理，以避免Activity切换期间，请求完成了，但没有监听器接收到信息
////  2.若中途直接关闭Activity，剩余的msg未pop，dismissDialog未调用
//public class NotifierDialogInfoVM extends DialogInfoVM {
//
//    public final Map<String, DialogNotifier> notifiers = new ConcurrentHashMap<>();
//    private final MsgManager<DialogMsg.Invoker, DialogMsg.Callback> mManager = new MsgManager<>();
//
//    public static NotifierDialogInfoVM get(FragmentActivity activity) {
//        return ViewModelProviders.of(activity).get(NotifierDialogInfoVM.class);
//    }
//
//    @Override
//    protected void onCleared() {
//        super.onCleared();
//        for (DialogNotifier notifier : notifiers.values()) {
//            notifier.sendCMsgWithDefaultUid(new DialogMsg.OnDismissDialog(DialogDismissReason.CLEAR));
//        }
//        mManager.unbind(false);
//    }
//
//    public DialogNotifier getNotifier(String type, DialogInfoManager.IFactory factory) {
//        DialogNotifier notifier = notifiers.get(type);
//        if (null == notifier) {
//            notifiers.put(type, notifier = new DialogNotifier());
//            mManager.bind(new InnerTarget(getInfoManager(type, factory)), notifier, false);
//        }
//        return notifier;
//    }
//
//    // //////////////////////////////////内部类//////////////////////////////////
//
//
//    private static class InnerTarget implements ITarget<DialogMsg.Invoker> {
//
//        private DialogInfoManager mInfo;
//
//        InnerTarget(DialogInfoManager info) {
//            mInfo = info;
//        }
//
//        @Override
//        public void onSetupMsgProcessors(List<MsgProcessor<? extends DialogMsg.Invoker>> list) {
//            list.add(new MsgProcessor<>(DialogMsg.PushMsg.class, msg -> mInfo.pushMsg(msg.senderUid, msg.data)));
//            list.add(new MsgProcessor<>(DialogMsg.PopMsg.class, msg -> mInfo.popMsg(msg.senderUid, msg.data)));
////            list.add(new MsgProcessor<>(DialogMsg.ToProgress.class, msg -> popMsg(msg.senderUid, msg.data)));
//        }
//    }
//
//}
