package com.soybeany.bdlib.android.template.plugins.extend;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.template.annotation.BackType;
import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.template.interfaces.IPluginManager;
import com.soybeany.bdlib.android.template.plugins.core.BackInterceptorPlugin;
import com.soybeany.bdlib.android.util.system.DoubleClickChecker;
import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.java8.function.Supplier;

/**
 * 双击检测，需先加载{@link BackInterceptorPlugin}
 * <br>Created by Soybeany on 2019/5/18.
 */
public class DoubleClickCheckPlugin implements IExtendPlugin {
    private DoubleClickChecker mChecker = new DoubleClickChecker();
    private final Supplier<String> mHint;
    private final Runnable mOnDoubleClick;
    private BackInterceptorPlugin mBackPlugin;

    @Nullable
    private BackInterceptorPlugin.IOnPermitInterceptor mInterceptor;

    public DoubleClickCheckPlugin(@NonNull Supplier<String> hint, @NonNull Runnable onDoubleClick) {
        mHint = hint;
        mOnDoubleClick = onDoubleClick;
    }

    @Override
    public void onLoad(IPluginManager manager) {
        mBackPlugin = (BackInterceptorPlugin) manager.findByGroupId(BackInterceptorPlugin.GROUP_ID);
        Optional.ofNullable(mBackPlugin).orElseThrow(() -> new RuntimeException("请先加载BackInterceptorPlugin"));
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        mBackPlugin.addInterceptor(mInterceptor = backType -> {
            if (BackType.BACK_KEY == backType) {
                mChecker.check(mHint, mOnDoubleClick);
                return true;
            }
            return false;
        });
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        mBackPlugin.removeInterceptor(mInterceptor);
    }

    @NonNull
    @Override
    public String getGroupId() {
        return "DoubleClickExit";
    }

    public DoubleClickChecker getChecker() {
        return mChecker;
    }

    public static class Finish extends DoubleClickCheckPlugin {
        public Finish(@NonNull Activity activity) {
            super(DoubleClickChecker::getFinishHint, activity::finish);
        }
    }
}
