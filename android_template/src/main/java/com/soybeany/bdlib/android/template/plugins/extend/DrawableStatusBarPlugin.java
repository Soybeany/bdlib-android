package com.soybeany.bdlib.android.template.plugins.extend;

import android.app.Activity;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.util.style.DrawableStatusBarUtils;

/**
 * 只在{@link Activity}中使用
 * <br>Created by Soybeany on 2019/5/5.
 */
public class DrawableStatusBarPlugin implements IExtendPlugin {
    private Activity mActivity;
    private ICallback mCallback;

    public DrawableStatusBarPlugin(@NonNull Activity activity, ICallback callback) {
        mActivity = activity;
        mCallback = callback;
    }

    @Override
    public void initAfterSetContentView() {
        IExtendPlugin.invokeOnNotNull(mCallback, callback -> DrawableStatusBarUtils.fullApply(mActivity, callback.onGetStatusBarDrawableRes()));
    }

    @NonNull
    @Override
    public final String getGroupId() {
        return "DrawableStatusBar";
    }

    public interface ICallback {
        @Nullable
        @DrawableRes
        default Integer onGetStatusBarDrawableRes() {
            return null;
        }
    }
}
