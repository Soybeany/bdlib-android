package com.soybeany.bdlib.android.template;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.template.interfaces.IInitTemplate;
import com.soybeany.bdlib.core.util.IterableUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 插件驱动
 * <br>Created by Soybeany on 2019/4/30.
 */
public class PluginDriver implements IExtendPlugin {
    private final Set<IExtendPlugin> mPlugins = new HashSet<>(); // 需加载的插件
    private ICallback mCallback;
    private Lifecycle mLifecycle;

    public static void install(LifecycleOwner owner, ICallback callback) {
        new PluginDriver(owner, callback);
    }

    private PluginDriver(LifecycleOwner owner, ICallback callback) {
        mLifecycle = owner.getLifecycle();
        mCallback = callback;

        mLifecycle.addObserver(this);
    }

    @Override
    public void initBeforeSetContentView() {
        mCallback.initBeforeSetContentView();
        IterableUtils.forEach(mPlugins, (plugin, flag) -> plugin.initBeforeSetContentView());
    }

    @Override
    public void initAfterSetContentView() {
        mCallback.initAfterSetContentView();
        IterableUtils.forEach(mPlugins, (plugin, flag) -> plugin.initAfterSetContentView());
    }

    @Override
    public void initFinished() {
        mCallback.initFinished();
        IterableUtils.forEach(mPlugins, (plugin, flag) -> plugin.initFinished());
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        mCallback.onSetupPlugins(mPlugins);
        for (IExtendPlugin plugin : mPlugins) {
            mLifecycle.addObserver(plugin);
        }

        initBeforeSetContentView();
        mCallback.onSetContentView(mCallback.setupLayoutResId());
        initAfterSetContentView();
        IExtendPlugin.invokeInUiThread(this::initFinished);
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        for (IExtendPlugin plugin : mPlugins) {
            mLifecycle.removeObserver(plugin);
        }
        mPlugins.clear();
        mLifecycle.removeObserver(this);
    }

    public interface ICallback extends IInitTemplate {
        void onSetContentView(int resId);

        int setupLayoutResId();

        default void onSetupPlugins(Set<IExtendPlugin> plugins) {
        }
    }
}
