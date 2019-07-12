package com.soybeany.bdlib.android.template.plugins.core;

import android.arch.lifecycle.LifecycleOwner;
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
 * 在Attach时创建，需额外自行调用{}{@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
 * {@link #setUserVisibleHint(boolean)}、{@link #onSaveInstanceState(Bundle)}
 * <br>Created by Soybeany on 2019/5/5.
 */
public class FragmentDevelopPlugin extends StdDevelopPlugin {
    private static final String KEY_TARGET_COUNT = "bd_target_count";

    @Nullable
    private LayoutInflater mInflater;
    private View mContentV;

    private int mPreparedCount; // 已准备好的位置的计数
    private int mTargetCount; // 目标计数，默认为0

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 还原计数
        Optional.ofNullable(savedInstanceState).ifPresent(state -> mTargetCount = state.getInt(KEY_TARGET_COUNT));
    }

    @Override
    public void init(@Nullable FragmentActivity activity, @Nullable LifecycleOwner owner, @Nullable PermissionRequester.IPermissionDealer dealer, @Nullable StdDevelopPlugin.ICallback callback) {
        super.init(activity, owner, dealer, callback);
        invokeOnNotNull(activity, a -> mInflater = a.getLayoutInflater());
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mContentV;
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        // // 保存计数
        outState.putInt(KEY_TARGET_COUNT, mTargetCount);
    }

    public View getContentView() {
        return mContentV;
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        // 有ViewPager等时则计算上限变为1
        mTargetCount = 1;
        // 执行回调
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
        if (mPreparedCount > mTargetCount) {
            return;
        } else if (mPreparedCount == mTargetCount) {
            invokeOnNotNull(mCallback, callback -> invokeInUiThread(() -> callback.doBusiness(mIsNew)));
        }
        mPreparedCount++;
    }

    public interface IInvoker extends StdDevelopPlugin.IInvoker {
        View getContentView();
    }

    public interface ICallback extends StdDevelopPlugin.ICallback {
        void onEssentialPermissionsPass(boolean isNew);
    }
}
