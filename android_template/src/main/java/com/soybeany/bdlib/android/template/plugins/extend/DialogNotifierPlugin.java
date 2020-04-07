package com.soybeany.bdlib.android.template.plugins.extend;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.util.dialog.IRealDialog;
import com.soybeany.bdlib.android.web.dialog.DialogInfoManager;
import com.soybeany.bdlib.android.web.dialog.DialogInfoVM;
import com.soybeany.bdlib.android.web.dialog.DialogManager;
import com.soybeany.bdlib.android.web.dialog.INotifierProvider;
import com.soybeany.bdlib.android.web.msg.DialogMsg;
import com.soybeany.bdlib.android.web.notifier.DialogNotifier;

import java.util.HashMap;
import java.util.Map;

/**
 * <br>Created by Soybeany on 2019/4/30.
 */
public class DialogNotifierPlugin implements IExtendPlugin, INotifierProvider {

    private final Map<String, DialogManager> mManagerMap = new HashMap<>();
    private final FragmentActivity mActivity;
    private final DialogInfoVM mVm;
    private final ICallback mCallback;

    public DialogNotifierPlugin(@NonNull FragmentActivity activity, ICallback callback) {
        mActivity = activity;
        mVm = DialogInfoVM.get(activity);
        mCallback = callback;
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        for (Map.Entry<String, DialogInfoManager> entry : mVm.infoManagerMap.entrySet()) {
            DialogInfoManager info = entry.getValue();
            // 若不需要立刻弹窗，则不作处理
            if (!info.needShowDialog()) {
                continue;
            }
            // 通知进行显示
            getDialogNotifier(entry.getKey()).sendIMsg(new DialogMsg.PushMsg(info.getCurDialogHint()));
        }
    }

    @NonNull
    @Override
    public final String getGroupId() {
        return "DialogNotifier";
    }

    @Override
    public synchronized DialogNotifier getDialogNotifier(String type) {
        DialogManager manager = mManagerMap.get(type);
        if (null == manager) {
            // TODO: 2020/4/5 需到主线程中创建
            mManagerMap.put(type, manager = new DialogManager(type, mVm, mCallback.onGetNewDialog(type)));
            mActivity.getLifecycle().addObserver(manager);
        }
        return manager.dNotifier;
    }

    public interface ICallback {
        IRealDialog onGetNewDialog(String type);
    }
}
