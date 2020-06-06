package com.soybeany.bdlib.android.template.plugins.extend;

import android.app.Activity;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.util.style.DrawableStatusBarUtils;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 只在{@link Activity}中使用
 * <br>Created by Soybeany on 2019/5/5.
 */
public class DrawableStatusBarPlugin implements IExtendPlugin {
    @NonNull
    private final Activity mActivity;
    @Nullable
    private final ICallback mCallback;

    public DrawableStatusBarPlugin(@NonNull Activity activity, @Nullable ICallback callback) {
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
