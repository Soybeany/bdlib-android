package com.soybeany.bdlib.android.template.plugins.core;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.template.annotation.BackType;
import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;

/**
 * 只在{@link Activity}中使用，需自行调用{@link #onBackPressed()}
 * <br>Created by Soybeany on 2019/5/5.
 */
public class BackInterceptorPlugin implements IExtendPlugin {
    private final Activity mActivity;
    @Nullable
    private final ICallback mCallback;

    public BackInterceptorPlugin(@NonNull Activity activity, @Nullable ICallback callback) {
        mActivity = activity;
        mCallback = callback;
    }

    @NonNull
    @Override
    public final String getGroupId() {
        return "BackInterceptor";
    }

    public void onBackPressed() {
        wannaBack(BackType.BACK_KEY);
    }

    public void wannaBack(@BackType int backType) {
        if (null == mCallback) {
            mActivity.onBackPressed();
            return;
        }
        if (!mCallback.shouldInterceptBack(backType)) {
            mCallback.onPermitBack(backType);
        }
    }

    public void onPermitBack(@BackType int backType, @NonNull ISuperOnBackPressed callback) {
        switch (backType) {
            case BackType.BACK_KEY:
                callback.onInvoke();
                break;
            case BackType.BACK_ITEM:
            case BackType.BACK_OTHER:
                mActivity.finish();
                break;
        }
    }

    public interface ISuperOnBackPressed {
        void onInvoke();
    }

    public interface IInvoker {
        void wannaBack(@BackType int backType);
    }

    public interface ICallback {
        default boolean shouldInterceptBack(@BackType int backType) {
            return false;
        }

        void onPermitBack(@BackType int backType);
    }
}
