package com.soybeany.bdlib.android.template.plugins.core;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soybeany.bdlib.android.util.system.PermissionRequester;
import com.soybeany.bdlib.core.java8.Optional;

import static com.soybeany.bdlib.android.template.interfaces.IExtendPlugin.invokeInUiThread;
import static com.soybeany.bdlib.android.template.interfaces.IExtendPlugin.invokeOnNotNull;

/**
 * 需额外自行调用{@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
 * {@link #setUserVisibleHint(boolean, ISuperSetUserVisibleHint)}
 * <br>Created by Soybeany on 2019/5/5.
 */
public class FragmentDevelopPlugin extends StdDevelopPlugin {
    private int mPreparedCount; // 已准备好的位置的计数

    @Nullable
    private LayoutInflater mInflater;
    private View mContentV;

    public FragmentDevelopPlugin(@Nullable FragmentActivity activity, @Nullable PermissionRequester.IPermissionDealer dealer,
                                 @Nullable Bundle savedInstanceState, @Nullable ICallback callback) {
        super(activity, dealer, savedInstanceState, callback);
        invokeOnNotNull(activity, a -> mInflater = a.getLayoutInflater());
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mContentV;
    }

    public void setUserVisibleHint(boolean isVisibleToUser, ISuperSetUserVisibleHint callback) {
        callback.onInvoke(isVisibleToUser);
        if (isVisibleToUser) {
            tryToSignalDoBusiness();
        }
    }

    public void onSetContentView(int resId) {
        Optional.ofNullable(mInflater).ifPresent(inflater -> mContentV = inflater.inflate(resId, null, false));
    }

    public void onEssentialPermissionsPass(boolean isNew) {
        tryToSignalDoBusiness();
    }

    private void tryToSignalDoBusiness() {
        if (mPreparedCount > 1) {
            return;
        } else if (mPreparedCount == 1) {
            invokeOnNotNull(mCallback, callback -> invokeInUiThread(() -> callback.doBusiness(mIsNew)));
        }
        mPreparedCount++;
    }

    public interface ISuperSetUserVisibleHint {
        void onInvoke(boolean isVisibleToUser);
    }

    public interface ICallback extends StdDevelopPlugin.ICallback {
        void onEssentialPermissionsPass(boolean isNew);
    }
}
