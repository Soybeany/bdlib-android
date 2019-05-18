package com.soybeany.bdlib.android.template.plugins.extend;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.util.dialog.DialogNotifier;
import com.soybeany.bdlib.android.util.dialog.NotifyDialogFragment;

import java.util.HashSet;
import java.util.Set;

/**
 * 只在{@link Activity}中使用
 * <br>Created by Soybeany on 2019/4/30.
 */
public class DialogFragmentPlugin implements IExtendPlugin, DialogNotifier.IProvider {
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
    @Override
    public DialogNotifier getDialogNotifier(String type) {
        if (null == mCallback) {
            return null;
        }
        mUsedTypes.add(type);
        return NotifyDialogFragment.getFragment(mActivity, type, () -> mCallback.onGetNewDialogFragment(type)).getDialogNotifier();
    }

    public interface IInvoker extends DialogNotifier.IProvider {
    }

    public interface ICallback {
        NotifyDialogFragment onGetNewDialogFragment(String type);
    }

    public static class TypeVM extends ViewModel {
        final Set<String> usedTypes = new HashSet<>();
    }
}
