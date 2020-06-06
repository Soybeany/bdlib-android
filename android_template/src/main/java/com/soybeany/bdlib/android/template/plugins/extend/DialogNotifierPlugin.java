package com.soybeany.bdlib.android.template.plugins.extend;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.util.dialog.IRealDialog;
import com.soybeany.bdlib.android.web.dialog.DialogInfoManager;
import com.soybeany.bdlib.android.web.dialog.DialogManager;
import com.soybeany.bdlib.android.web.dialog.INotifierProvider;
import com.soybeany.bdlib.android.web.dialog.NotifierDialogVM;
import com.soybeany.bdlib.android.web.msg.DVMsg;
import com.soybeany.bdlib.android.web.notifier.DialogNotifier;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

/**
 * <br>Created by Soybeany on 2019/4/30.
 */
public class DialogNotifierPlugin implements IExtendPlugin, INotifierProvider {

    private final Map<String, DialogManager> mManagerMap = new HashMap<>();
    private final FragmentActivity mActivity;
    private final NotifierDialogVM mVm;
    private final ICallback mCallback;

    public DialogNotifierPlugin(@NonNull FragmentActivity activity, ICallback callback) {
        mActivity = activity;
        mVm = NotifierDialogVM.get(activity);
        mCallback = callback;
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        for (Map.Entry<String, DialogInfoManager> entry : mVm.infoManagerMap.entrySet()) {
            DialogInfoManager info = entry.getValue();
            // 若不需要立刻弹窗，则不作处理
            if (!info.hasHint()) {
                continue;
            }
            // 通知进行显示
            DialogNotifier notifier = getDialogNotifier(entry.getKey());
            if (null != notifier && null != notifier.sender) {
                notifier.sender.sendIMsg(new DVMsg.PushMsg(info.getCurDialogHint()));
            }
        }
    }

    @NonNull
    @Override
    public final String getGroupId() {
        return "DialogNotifier";
    }

    @Override
    public synchronized DialogNotifier getDialogNotifier(String type) {
        DialogManager dialogManager = mManagerMap.get(type);
        DialogInfoManager infoManager = mVm.getInfoManager(type);
        if (null == dialogManager) {
            mManagerMap.put(type, dialogManager = new DialogManager(infoManager, mCallback.onGetNewDialog(type)));
            mActivity.getLifecycle().addObserver(dialogManager);
        }
        return new DialogNotifier(infoManager.dSender, infoManager.dvSender);
    }

    public interface ICallback {
        IRealDialog onGetNewDialog(String type);
    }
}
