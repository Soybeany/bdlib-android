package com.soybeany.bdlib.android.template.interfaces;

import android.support.annotation.Nullable;

/**
 * 插件管理器
 * <br>Created by Soybeany on 2019/5/16.
 */
public interface IPluginManager {

    void add(@Nullable IExtendPlugin plugin);

    void remove(@Nullable IExtendPlugin plugin);

    void removeByGroupId(@Nullable String groupId);

    @Nullable
    IExtendPlugin findByGroupId(@Nullable String groupId);
}
