package com.soybeany.bdlib.android.template.interfaces;

import androidx.annotation.Nullable;

/**
 * 插件管理器
 * <br>Created by Soybeany on 2019/5/16.
 */
public interface IPluginManager {

    void load(@Nullable IExtendPlugin plugin);

    void unload(@Nullable IExtendPlugin plugin);

    void unloadByGroupId(@Nullable String groupId);

    @Nullable
    IExtendPlugin findByGroupId(@Nullable String groupId);
}
