package com.soybeany.bdlib.android.template.plugins.core;

import android.app.Activity;

import com.soybeany.bdlib.android.template.annotation.BackType;
import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;

import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 只在{@link Activity}中使用，需自行调用{@link #onBackPressed()}
 * <br>Created by Soybeany on 2019/5/5.
 */
public class BackInterceptorPlugin implements IExtendPlugin {
    public static final String GROUP_ID = "BackInterceptor";

    private final Activity mActivity;
    @Nullable
    private final ICallback mCallback;

    private final Set<IOnPermitInterceptor> mInterceptors = new HashSet<>();

    public BackInterceptorPlugin(@NonNull Activity activity, @Nullable ICallback callback) {
        mActivity = activity;
        mCallback = callback;
    }

    @NonNull
    @Override
    public final String getGroupId() {
        return GROUP_ID;
    }

    public void addInterceptor(@Nullable IOnPermitInterceptor interceptor) {
        if (null != interceptor) {
            mInterceptors.add(interceptor);
        }
    }

    public void removeInterceptor(@Nullable IOnPermitInterceptor interceptor) {
        if (null != interceptor) {
            mInterceptors.remove(interceptor);
        }
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
        // 拦截操作
        for (IOnPermitInterceptor interceptor : mInterceptors) {
            if (interceptor.onPermitBack(backType)) {
                return;
            }
        }
        // 默认操作
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

    public interface IOnPermitInterceptor {
        /**
         * @return 是否拦截后续操作
         */
        boolean onPermitBack(@BackType int backType);
    }
}
