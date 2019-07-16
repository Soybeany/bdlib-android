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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 插件驱动
 * <br>需在super的onCreate前调用{@link #beforeOnCreate()}
 * <br>Created by Soybeany on 2019/4/30.
 */
public class PluginDriver implements IExtendPlugin {
    private final ManagerImpl mManager = new ManagerImpl();
    private ICallback mCallback;
    private Lifecycle mLifecycle;

    public PluginDriver(LifecycleOwner owner, ICallback callback) {
        mLifecycle = owner.getLifecycle();
        mCallback = callback;

        mLifecycle.addObserver(this);
    }

    @Override
    public void initBeforeOnCreate() {
        mManager.invoke(IInitTemplate::initBeforeOnCreate);
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
        mManager.unloadAndUnlock();
        mLifecycle.removeObserver(this);
    }

    @NonNull
    @Override
    public String getGroupId() {
        return "PluginDriver";
    }

    /**
     * 需onCreate前主动调用，以触发相应回调
     */
    public void beforeOnCreate() {
        initBeforeOnCreate();
    }

    public interface ICallback extends IInitTemplate {
        void onSetContentView(int resId);

        int setupLayoutResId();

        void onSetupPlugins(IPluginManager manager);
    }

    private static class ManagerImpl implements IPluginManager {
        private final List<IExtendPlugin> mPlugins = new LinkedList<>(); // 需加载的插件，不能用set，否则相同级别的会被覆盖
        private boolean mIsLock;

        public void load(@Nullable IExtendPlugin plugin) {
            checkLock(plugin);
            if (null != plugin) {
                mPlugins.add(plugin);
                plugin.onLoad(this);
            }
        }

        public void unload(@Nullable IExtendPlugin plugin) {
            checkLock(plugin);
            if (null != plugin) {
                plugin.onUnload();
                mPlugins.remove(plugin);
            }
        }

        public void unloadByGroupId(@Nullable String groupId) {
            unload(findByGroupId(groupId));
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

        void unloadAndUnlock() {
            Iterator<IExtendPlugin> iterator = mPlugins.iterator();
            while (iterator.hasNext()) {
                IExtendPlugin plugin = iterator.next();
                plugin.onUnload();
                iterator.remove();
            }
            mIsLock = false;
        }

        private void checkLock(@Nullable IExtendPlugin plugin) {
            if (mIsLock) {
                throw new RuntimeException("插件列表已锁定，不能修改");
            }
        }
    }
}
