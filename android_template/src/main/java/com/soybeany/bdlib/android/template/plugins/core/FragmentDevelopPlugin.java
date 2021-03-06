package com.soybeany.bdlib.android.template.plugins.core;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soybeany.bdlib.android.util.system.PermissionRequester;
import com.soybeany.bdlib.core.java8.Optional;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

import static com.soybeany.bdlib.android.template.interfaces.IExtendPlugin.invokeOnNotNull;

/**
 * 在Attach时创建，需额外自行调用{}{@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
 * {@link #setUserVisibleHint(boolean)}、{@link #onSaveInstanceState(Bundle)}
 * <br>由于涉及到Activity，需在{@link Fragment#onAttach(Context)}时调用{@link #activate(FragmentActivity, PermissionRequester.IPermissionDealer)}，
 * 然后在{@link Fragment#onDetach()}时调用{@link #deactivate()}
 * <br>Created by Soybeany on 2019/5/5.
 */
public class FragmentDevelopPlugin extends StdDevelopPlugin {
    private static final String KEY_TARGET_COUNT = "bd_target_count";

    @Nullable
    private LayoutInflater mInflater;
    private View mContentV;

    private int mPreparedCount; // 已准备好的位置的计数
    private int mTargetCount; // 目标计数，默认为0

    public FragmentDevelopPlugin(@Nullable LifecycleOwner owner, @Nullable ICallback callback) {
        super(owner, callback);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 还原计数
        Optional.ofNullable(savedInstanceState).ifPresent(state -> mTargetCount = state.getInt(KEY_TARGET_COUNT));
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mContentV;
    }

    @Override
    protected void onPermissionPassInner() {
        // 默认不作处理
    }

    @Override
    public StdDevelopPlugin activate(@Nullable FragmentActivity activity, @Nullable PermissionRequester.IPermissionDealer dealer) {
        invokeOnNotNull(activity, a -> mInflater = a.getLayoutInflater());
        return super.activate(activity, dealer);
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
            signalDoBusiness(mCallback, mIsNew);
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
