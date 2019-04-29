package com.soybeany.bdlib.android.template.interfaces;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.template.lifecycle.ButterKnifeObserver;
import com.soybeany.bdlib.android.util.dialog.AbstractDialog;
import com.soybeany.bdlib.android.util.dialog.DialogKeyProvider;
import com.soybeany.bdlib.android.util.system.PermissionRequester;

/**
 * 定义通用的基础功能
 * <br>Created by Soybeany on 2019/4/18.
 */
public interface IBaseFunc {

    AbstractDialog getDialog();

    DialogKeyProvider getDialogKeys();

    boolean requestPermissions(@NonNull PermissionRequester.IPermissionCallback callback, @Nullable String... permissions);

    // //////////////////////////////////启动器//////////////////////////////////

    interface IStarter extends IBaseFunc {
        void checkPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
    }

    // //////////////////////////////////拓展实现//////////////////////////////////

    /**
     * 数据提供者，使用onGet开头
     */
    interface IStarterDataProvider extends IBaseFunc, IExtendPlugin {

        default String[] onGetEssentialPermissions() {
            return null;
        }

        PermissionRequester.IPermissionCallback onGetEPermissionCallback();

        @Nullable
        default LifecycleObserver[] onGetExObservers() {
            return null;
        }

        Lifecycle onGetLifecycle();

        @Nullable
        default IExtendPlugin[] onGetNewPlugins() {
            return null;
        }

        AbstractDialog onGetNewDialog();

        PermissionRequester onGetNewPermissionRequester();
    }

    interface IEx<Source> extends IStarterDataProvider, IDevTemplate, IVMProvider, ButterKnifeObserver.ICallback<Source> {

        void onSetContentView();

    }
}
