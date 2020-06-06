package com.soybeany.bdlib.android.template.plugins.core;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

/**
 * ViewModel
 * <br>Created by Soybeany on 2019/4/30.
 */
public class ViewModelPlugin implements IExtendPlugin {
    public static final String GROUP_ID = "ViewModel";

    @Nullable
    private final ICallback mCallback;
    private final ViewModelProvider mProvider;

    @Nullable
    private Fragment mFragment;

    public ViewModelPlugin(@NonNull FragmentActivity activity, @Nullable ICallback callback) {
        mProvider = new ViewModelProvider(activity);
        mCallback = callback;
    }

    public ViewModelPlugin(@NonNull Fragment fragment, @Nullable IFragmentCallback callback) {
        mProvider = new ViewModelProvider(mFragment = fragment);
        mCallback = callback;
    }

    @Override
    public void initBeforeSetContentView() {
        IExtendPlugin.invokeOnNotNull(mCallback, callback -> callback.onInitViewModels(mProvider));
        // Fragment下额外回调
        if (null != mFragment && mCallback instanceof IFragmentCallback) {
            FragmentActivity activity = Objects.requireNonNull(mFragment.getActivity());
            ((IFragmentCallback) mCallback).onInitActivityViewModels(new ViewModelProvider(activity));
        }
    }

    @NonNull
    @Override
    public final String getGroupId() {
        return GROUP_ID;
    }

    /**
     * 默认范围
     */
    public interface ICallback {
        default void onInitViewModels(ViewModelProvider provider) {
        }
    }

    /**
     * Fragment中的Activity范围
     */
    public interface IFragmentCallback extends ICallback {
        default void onInitActivityViewModels(ViewModelProvider provider) {
        }
    }
}
