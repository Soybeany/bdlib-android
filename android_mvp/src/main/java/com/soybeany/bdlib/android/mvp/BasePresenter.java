package com.soybeany.bdlib.android.mvp;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.core.java8.function.Consumer;
import com.soybeany.bdlib.core.util.thread.UidOwnerStorage;

/**
 * <br>Created by Soybeany on 2019/4/11.
 */
public abstract class BasePresenter implements IObserver, UidOwnerStorage.IUidOwner {
    private final UidOwnerStorage mStorage = new UidOwnerStorage();

    public BasePresenter(@NonNull IView... views) {
        for (IView view : views) {
            mStorage.put(view);
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        mStorage.clear();
    }

    /**
     * 获得指定视图(不要在调用方法外强引用此返回值，以免影响回收)
     */
    protected <V extends IView> V getView(Class<V> clazz) {
        return mStorage.get(clazz);
    }

    protected <V extends IView> V getView(Class<V> clazz, String uid) {
        return mStorage.get(clazz, uid);
    }

    protected <V extends IView> boolean invoke(Class<V> clazz, Consumer<V> consumer) {
        return mStorage.invoke(clazz, consumer);
    }

    protected <V extends IView> boolean invoke(Class<V> clazz, String uid, Consumer<V> consumer) {
        return mStorage.invoke(clazz, uid, consumer);
    }
}
