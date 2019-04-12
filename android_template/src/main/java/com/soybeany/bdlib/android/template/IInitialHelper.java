package com.soybeany.bdlib.android.template;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;

import com.soybeany.bdlib.core.java8.Optional;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * <br>Created by Soybeany on 2019/2/1.
 */
public interface IInitialHelper {

    int setupLayoutResId();

    default LifecycleObserver[] setupObservers() {
        return null;
    }

    /**
     * 初始化视图、ViewModel等
     */
    default void onInit() {
    }

    /**
     * 处理业务逻辑
     */
    default void doBusiness() {
    }

    class LifecycleHelper {
        private final LinkedList<LifecycleObserver> mObservers = new LinkedList<>();

        /**
         * 一般放在onCreate中调用
         */
        public void addObservers(Lifecycle lifecycle, LifecycleObserver[] observerArr, LifecycleObserver... additionalArr) {
            Optional.ofNullable(observerArr).ifPresent(observers -> mObservers.addAll(Arrays.asList(observers)));
            Optional.ofNullable(additionalArr).ifPresent(additional -> mObservers.addAll(Arrays.asList(additional)));
            // 进行回调
            for (LifecycleObserver observer : mObservers) {
                lifecycle.addObserver(observer);
            }
        }

        /**
         * 一般放在onDestroy中调用
         */
        public void removeObservers(Lifecycle lifecycle) {
            Iterator<LifecycleObserver> iterator = mObservers.iterator();
            while (iterator.hasNext()) {
                lifecycle.removeObserver(iterator.next());
                iterator.remove();
            }
        }
    }
}
