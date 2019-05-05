package com.soybeany.bdlib.android.template.plugins.core;

import android.app.Activity;

import com.soybeany.bdlib.android.template.annotation.BackType;
import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;

/**
 * 只在{@link Activity}中使用，需自行调用{@link #onBackPressed()}
 * <br>Created by Soybeany on 2019/5/5.
 */
public class BackInterceptorPlugin implements IExtendPlugin {
    private Activity mActivity;
    private ITemplate mTemplate;

    public BackInterceptorPlugin(Activity activity, ITemplate template) {
        mActivity = activity;
        mTemplate = template;
    }

    public void onBackPressed() {
        mTemplate.wannaBack(BackType.BACK_KEY);
    }

    public void onPermitBack(@BackType int backType, ISuperOnBackPressed callback) {
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

    public interface ITemplate {

        default void wannaBack(@BackType int backType) {
            if (!shouldInterceptBack(backType)) {
                onPermitBack(backType);
            }
        }

        default boolean shouldInterceptBack(@BackType int backType) {
            return false;
        }

        void onPermitBack(@BackType int backType);
    }
}
