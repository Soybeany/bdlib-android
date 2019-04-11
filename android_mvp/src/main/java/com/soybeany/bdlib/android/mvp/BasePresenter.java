package com.soybeany.bdlib.android.mvp;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.core.java8.function.Consumer;
import com.soybeany.bdlib.core.util.thread.ObjectStorage;

import java.util.LinkedList;

/**
 * <br>Created by Soybeany on 2019/4/11.
 */
public abstract class BasePresenter implements IObserver {
    private final ObjectStorage mStorage = new ObjectStorage();
    private final LinkedList<String> mKeys = new LinkedList<>();

    public BasePresenter(@NonNull IView... views) {
        for (IView view : views) {
            String uid = view.getUid();
            mStorage.put(uid, view);
            mKeys.add(uid);
        }
    }

    protected <T> boolean invokeMain(Consumer<T> consumer, Class<T> clazz) {
        if (mKeys.isEmpty()) {
            return false;
        }
        return invoke(mKeys.peekFirst(), consumer, clazz);
    }

    /**
     * 在指定key
     */
    protected <T> boolean invoke(@NonNull String key, Consumer<T> consumer, Class<T> clazz) {
        return mStorage.invoke(key, consumer, clazz);
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        mStorage.clear();
    }
}
