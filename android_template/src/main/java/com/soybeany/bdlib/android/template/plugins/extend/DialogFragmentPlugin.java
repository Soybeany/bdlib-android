package com.soybeany.bdlib.android.template.plugins.extend;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.util.dialog.NotifyDialogFragment;
import com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg;
import com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg;
import com.soybeany.bdlib.core.util.notify.Notifier;

import java.util.HashSet;
import java.util.Set;

/**
 * todo 使用liveData记录所需的弹窗信息
 * <br>Created by Soybeany on 2019/4/30.
 */
public class DialogFragmentPlugin implements IExtendPlugin {
    @NonNull
    private final FragmentActivity mActivity;
    @Nullable
    private final ICallback mCallback;

    private final Set<String> mUsedTypes;

    public DialogFragmentPlugin(@NonNull FragmentActivity activity, @Nullable ICallback callback) {
        mActivity = activity;
        mCallback = callback;
        mUsedTypes = ViewModelProviders.of(activity).get(TypeVM.class).usedTypes;
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        // 激活已存在的Fragment，适用于横竖屏切换后等情况
        for (String type : mUsedTypes) {
            getDialogNotifier(type);
        }
    }

    @NonNull
    @Override
    public final String getGroupId() {
        return "Dialog";
    }

    @Nullable
    public Notifier<DialogInvokerMsg, DialogCallbackMsg> getDialogNotifier() {
        return getDialogNotifier(IInvoker.TYPE_DEFAULT);
    }

    @Nullable
    public Notifier<DialogInvokerMsg, DialogCallbackMsg> getDialogNotifier(String type) {
        if (null == mCallback) {
            return null;
        }
        mUsedTypes.add(type);
        return NotifyDialogFragment.getFragment(mActivity, type, () -> mCallback.onGetNewDialogFragment(type)).getNotifier();
    }

    public interface IInvoker {
        String TYPE_DEFAULT = "default";

        @Nullable
        Notifier<DialogInvokerMsg, DialogCallbackMsg> getDialogNotifier();

        @Nullable
        Notifier<DialogInvokerMsg, DialogCallbackMsg> getDialogNotifier(String type);
    }

    public interface ICallback {
        NotifyDialogFragment onGetNewDialogFragment(String type);
    }

    public static class TypeVM extends ViewModel {
        final Set<String> usedTypes = new HashSet<>();
    }
}
