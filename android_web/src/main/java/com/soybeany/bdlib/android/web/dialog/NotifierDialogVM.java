package com.soybeany.bdlib.android.web.dialog;

import android.os.Looper;

import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.android.web.msg.DVMsg;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import static com.soybeany.bdlib.android.util.BDContext.MAIN_HANDLER;

/**
 * <br>Created by Soybeany on 2020/4/2.
 */
@SuppressWarnings("WeakerAccess")
public class NotifierDialogVM extends ViewModel {

    public final Map<String, DialogInfoManager> infoManagerMap = new ConcurrentHashMap<>();

    /**
     * 将全部操作放到主线程进行，确保线程安全
     */
    public static void exeInMainThread(Runnable task) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            task.run();
        } else {
            MAIN_HANDLER.post(task);
        }
    }

    public static NotifierDialogVM get(FragmentActivity activity) {
        return new ViewModelProvider(activity).get(NotifierDialogVM.class);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        for (DialogInfoManager manager : infoManagerMap.values()) {
            if (manager.hasHint()) {
                manager.dvSender.sendIMsg(new DVMsg.ClearMsg(DialogDismissReason.CLEAR));
            }
            manager.unbind();
        }
    }

    public DialogInfoManager getInfoManager(String type) {
        DialogInfoManager manager = infoManagerMap.get(type);
        if (null == manager) {
            infoManagerMap.put(type, manager = new DialogInfoManager());
            manager.bind();
        }
        return manager;
    }

}
