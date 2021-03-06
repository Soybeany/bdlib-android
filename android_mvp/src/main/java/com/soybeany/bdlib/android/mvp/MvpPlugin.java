package com.soybeany.bdlib.android.mvp;


import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

/**
 * <br>Created by Soybeany on 2019/4/29.
 */
public class MvpPlugin implements IExtendPlugin {

    private final IPresenterProviderS mProviderS; // 单例提供者
    private final IPresenterProvider mProvider; // 普通提供者
    private final ICallback mCallback;

    @Nullable
    private IPresenterProvider mActivityProvider; // Fragment中使用的Activity提供者

    public MvpPlugin(@NonNull FragmentActivity activity, ICallback callback) {
        this(new ViewModelProvider(activity), activity.getLifecycle(), callback);
    }

    public MvpPlugin(@NonNull Fragment fragment, IFragmentCallback callback) {
        this(new ViewModelProvider(fragment), fragment.getLifecycle(), callback);
        mActivityProvider = new IPresenterProvider() {
            @Override
            public <V extends IPresenterView, T extends BasePresenter<V>> T get(Class<T> clazz, V v) {
                FragmentActivity activity = Objects.requireNonNull(fragment.getActivity());
                return PresenterUtils.get(new ViewModelProvider(activity), clazz, fragment.getLifecycle(), v);
            }
        };
    }

    private MvpPlugin(ViewModelProvider provider, Lifecycle lifecycle, ICallback callback) {
        mProviderS = new PresenterProviderSImpl(lifecycle);
        mProvider = new PresenterProviderImpl(provider, lifecycle);
        mCallback = callback;
    }

    @Override
    public void initAfterSetContentView() {
        mCallback.onInitSingletonPresenters(mProviderS);
        mCallback.onInitPresenters(mProvider);
        // Fragment下额外回调
        if (null != mActivityProvider && mCallback instanceof IFragmentCallback) {
            ((IFragmentCallback) mCallback).onInitActivityPresenters(mActivityProvider);
        }
    }

    @NonNull
    @Override
    public String getGroupId() {
        return "MVP";
    }

    // //////////////////////////////////内部实现//////////////////////////////////

    private static class PresenterProviderSImpl implements IPresenterProviderS {
        private Lifecycle mViewLifecycle;

        PresenterProviderSImpl(Lifecycle viewLifecycle) {
            mViewLifecycle = viewLifecycle;
        }

        @Override
        public <V extends IPresenterView, T extends BasePresenter<V>> T get(Class<T> clazz, V v, String type) {
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
    }

    private static class PresenterProviderImpl implements IPresenterProvider {
        private ViewModelProvider mProvider;
        private Lifecycle mViewLifecycle;

        PresenterProviderImpl(ViewModelProvider provider, Lifecycle viewLifecycle) {
            mProvider = provider;
            mViewLifecycle = viewLifecycle;
        }

        @Override
        public <V extends IPresenterView, T extends BasePresenter<V>> T get(Class<T> clazz, V v) {
            return PresenterUtils.get(mProvider, clazz, mViewLifecycle, v);
        }
    }


    // //////////////////////////////////外部API//////////////////////////////////

    public interface ICallback {
        default void onInitPresenters(IPresenterProvider provider) {
        }

        default void onInitSingletonPresenters(IPresenterProviderS provider) {
        }
    }

    public interface IFragmentCallback extends ICallback {
        default void onInitActivityPresenters(IPresenterProvider provider) {
        }
    }
}
