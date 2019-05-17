package com.soybeany.bdlib.android.template;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.template.interfaces.IInitTemplate;
import com.soybeany.bdlib.android.template.interfaces.IPluginManager;
import com.soybeany.bdlib.core.java8.function.Consumer;
import com.soybeany.bdlib.core.util.IterableUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 插件驱动
 * <br>Created by Soybeany on 2019/4/30.
 */
public class PluginDriver implements IExtendPlugin {
    private final ManagerImpl mManager = new ManagerImpl();
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
        mManager.invoke(IInitTemplate::initBeforeSetContentView);
    }

    @Override
    public void initAfterSetContentView() {
        mCallback.initAfterSetContentView();
        mManager.invoke(IInitTemplate::initAfterSetContentView);
    }

    @Override
    public void initFinished() {
        mCallback.initFinished();
        mManager.invoke(IInitTemplate::initFinished);
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        mCallback.onSetupPlugins(mManager);
        mManager.lockCheckAndSort();
        mManager.invoke(plugin -> mLifecycle.addObserver(plugin));

        initBeforeSetContentView();
        mCallback.onSetContentView(mCallback.setupLayoutResId());
        initAfterSetContentView();
        IExtendPlugin.invokeInUiThread(this::initFinished);
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        mManager.invoke(plugin -> mLifecycle.removeObserver(plugin));
        mManager.unlockAndClear();
        mLifecycle.removeObserver(this);
    }

    @NonNull
    @Override
    public String getGroupId() {
        return "PluginDriver";
    }

    public interface ICallback extends IInitTemplate {
        void onSetContentView(int resId);

        int setupLayoutResId();

        void onSetupPlugins(IPluginManager manager);
    }

    private static class ManagerImpl implements IPluginManager {
        private final List<IExtendPlugin> mPlugins = new LinkedList<>(); // 需加载的插件，不能用set，否则相同级别的会被覆盖
        private boolean mIsLock;

        public void add(@Nullable IExtendPlugin plugin) {
            if (checkAndLock(plugin)) {
                mPlugins.add(plugin);
            }
        }

        public void remove(@Nullable IExtendPlugin plugin) {
            if (checkAndLock(plugin)) {
                mPlugins.remove(plugin);
            }
        }

        public void removeByGroupId(@Nullable String groupId) {
            remove(findByGroupId(groupId));
        }

        @Nullable
        public IExtendPlugin findByGroupId(@Nullable String groupId) {
            for (IExtendPlugin plugin : mPlugins) {
                if (plugin.getGroupId().equals(groupId)) {
                    return plugin;
                }
            }
            return null;
        }

        void invoke(@NonNull Consumer<IExtendPlugin> consumer) {
            IterableUtils.forEach(mPlugins, (plugin, flag) -> consumer.accept(plugin));
        }

        void lockCheckAndSort() {
            mIsLock = true;
            Set<String> groupIdSet = new HashSet<>();
            for (IExtendPlugin plugin : mPlugins) {
                if (null != plugin && !groupIdSet.add(plugin.getGroupId())) {
                    throw new RuntimeException("不允许加载多个相同GroupId的插件");
                }
            }
            Collections.sort(mPlugins, (o1, o2) -> o1.getLoadOrder() - o2.getLoadOrder()); // order值小的先被加载
        }

        void unlockAndClear() {
            mPlugins.clear();
            mIsLock = false;
        }

        private boolean checkAndLock(@Nullable IExtendPlugin plugin) {
            if (mIsLock) {
                throw new RuntimeException("插件列表已锁定，不能修改");
            }
            return null != plugin;
        }
    }
}
