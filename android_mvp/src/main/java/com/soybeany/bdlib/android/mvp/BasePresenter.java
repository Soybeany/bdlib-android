package com.soybeany.bdlib.android.mvp;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.core.java8.function.Consumer;
import com.soybeany.bdlib.core.util.thread.ObjectStorage;

/**
 * <br>Created by Soybeany on 2019/4/11.
 */
public abstract class BasePresenter implements IObserver, IUidOwner {
    private final ObjectStorage mStorage = new ObjectStorage();

    public BasePresenter(@NonNull IView... views) {
        for (IView view : views) {
            mStorage.put(IUidOwner.getKey(view.getClass(), view.getUid()), view);
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        mStorage.clear();
    }

    protected IView getView(Class<? extends IView> clazz) {
        return getView(clazz, IView.DEFAULT_UID);
    }

    protected IView getView(Class<? extends IView> clazz, String uid) {
        return (IView) mStorage.get(IUidOwner.getKey(clazz, uid));
    }

    protected <T extends IView> boolean invoke(Class<T> clazz, Consumer<T> consumer) {
        return invoke(clazz, IView.DEFAULT_UID, consumer);
    }

    protected <T extends IView> boolean invoke(Class<T> clazz, String uid, Consumer<T> consumer) {
        return mStorage.invoke(IUidOwner.getKey(clazz, uid), consumer, clazz);
    }
}
