package com.soybeany.bdlib.android.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;

import java.util.Objects;

/**
 * <br>Created by Soybeany on 2019/4/29.
 */
public abstract class MvpPlugin implements IExtendPlugin {

    @NonNull
    @Override
    public String getGroupId() {
        return "MVP";
    }

    // //////////////////////////////////内部实现//////////////////////////////////

    private static class PresenterProviderImpl implements IPresenterProvider {
        private ViewModelProvider mProvider;
        private Lifecycle mViewLifecycle;

        PresenterProviderImpl(@NonNull FragmentActivity activity) {
            this(ViewModelProviders.of(activity), activity.getLifecycle());
        }

        PresenterProviderImpl(ViewModelProvider provider, Lifecycle viewLifecycle) {
            mProvider = provider;
            mViewLifecycle = viewLifecycle;
        }

        @Override
        public <V extends IPresenterView, T extends BasePresenter<V>> T getPresenter(Class<T> clazz, V v) {
            return PresenterUtils.get(mProvider, clazz, mViewLifecycle, v);
        }

        @Override
        public <V extends IPresenterView, T extends BasePresenter<V>> T getSingleton(Class<T> clazz, V v, String type) {
            return PresenterUtils.getSingleton(clazz, type, mViewLifecycle, v);
        }

        @Override
        public <V extends IPresenterView, T extends BasePresenter<V>> boolean release(Class<T> clazz, String type) {
            return PresenterUtils.release(clazz, type);
        }

        @Override
        public <V extends IPresenterView, T extends BasePresenter<V>> boolean releaseAll(Class<T> clazz) {
            return PresenterUtils.releaseAll(clazz);
        }

        Lifecycle getViewLifecycle() {
            return mViewLifecycle;
        }
    }

    private static class FPresenterProviderImpl extends PresenterProviderImpl implements IFPresenterProvider {
        private Fragment mFragment;

        FPresenterProviderImpl(@NonNull Fragment fragment) {
            super(ViewModelProviders.of(fragment), fragment.getLifecycle());
            mFragment = fragment;
        }

        @Override
        public <V extends IPresenterView, T extends BasePresenter<V>> T getActivityPresenter(Class<T> clazz, V v) {
            FragmentActivity activity = Objects.requireNonNull(mFragment.getActivity());
            return PresenterUtils.get(ViewModelProviders.of(activity), clazz, getViewLifecycle(), v);
        }
    }

    // //////////////////////////////////Activity//////////////////////////////////

    /**
     * Activity版本
     */
    public static class AVer extends MvpPlugin {
        private IPresenterProvider mProvider;
        private IActivityCallback mCallback;

        public AVer(FragmentActivity activity, IActivityCallback callback) {
            mProvider = new PresenterProviderImpl(activity);
            mCallback = callback;
        }

        @Override
        public void initAfterSetContentView() {
            mCallback.onInitPresenters(mProvider);
        }
    }

    public interface IActivityCallback {
        default void onInitPresenters(IPresenterProvider provider) {
        }
    }

    // //////////////////////////////////Fragment//////////////////////////////////

    /**
     * Fragment版本
     */
    public static class FVer extends MvpPlugin {
        private IFPresenterProvider mProvider;
        private IFragmentCallback mCallback;

        public FVer(Fragment fragment, IFragmentCallback callback) {
            mProvider = new FPresenterProviderImpl(fragment);
            mCallback = callback;
        }

        @Override
        public void initAfterSetContentView() {
            mCallback.onInitPresenters(mProvider);
        }
    }

    public interface IFragmentCallback {
        default void onInitPresenters(IFPresenterProvider provider) {
        }
    }
}
