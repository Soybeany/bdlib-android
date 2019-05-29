package com.soybeany.bdlib.android.util.dialog;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.soybeany.bdlib.android.util.BDContext;
import com.soybeany.bdlib.android.util.HandlerThreadImpl;
import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.android.util.dialog.msg.DialogNotifierMsg;
import com.soybeany.bdlib.android.util.dialog.msg.IDialogMsg;
import com.soybeany.bdlib.core.util.notify.IOnCallListener;
import com.soybeany.bdlib.core.util.notify.Notifier;
import com.soybeany.bdlib.core.util.storage.KeyValueStorage;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.soybeany.bdlib.android.util.dialog.DialogDismissReason.OTHER;

/**
 * <br>Created by Soybeany on 2019/5/17.
 */
public class DialogNotifier extends Notifier<DialogNotifierMsg.Invoker, DialogNotifierMsg.Callback> {
    public final String uid = BDContext.getUID();

    public final SortedSet<IDialogMsg> msgSet = new TreeSet<>(); // 收录的弹窗信息
    public final Set<IDialogMsg> unableCancelSet = new HashSet<>(); // 收录的弹窗信息(不可取消)

    public final MutableLiveData<String> hint = new MutableLiveData<>();
    public final MutableLiveData<Boolean> cancelable = new MutableLiveData<>();

    /**
     * 弹窗是否在显示
     */
    public boolean isDialogShowing;

    public DialogNotifier() {
        super(HandlerThreadImpl.UI_THREAD);
    }

    public interface IProvider {
        @Nullable
        DialogNotifier getDialogNotifier();
    }

    public interface IMultiTypeProvider extends IProvider {
        String TYPE_DEFAULT = "default";

        @Nullable
        default DialogNotifier getDialogNotifier() {
            return getDialogNotifier(TYPE_DEFAULT);
        }

        @Nullable
        DialogNotifier getDialogNotifier(String type);
    }

    public interface IDialogProvider {
        @Nullable
        IDialog getNewDialog(String type, String notifierUid);
    }

    public interface IDialog extends IOnCallListener {
        void onBindNotifier(String type, DialogNotifier notifier);
    }

    public static class VM extends ViewModel implements IObserver {
        private final KeyValueStorage<String, DialogNotifier> mNotifierStorage = new KeyValueStorage<>();
        private final KeyValueStorage<String, IDialog> mListenerStorage = new KeyValueStorage<>();

        @Nullable
        private IDialogProvider mInvokerProvider;

        @NonNull
        public static VM initAndGet(FragmentActivity activity, @Nullable IDialogProvider provider) {
            VM vm = getInstance(activity);
            activity.getLifecycle().addObserver(vm);
            vm.mInvokerProvider = provider;
            // 尝试触发未完成的绑定
            for (String key : vm.mNotifierStorage.keys()) {
                vm.getNotifier(key);
            }
            return vm;
        }

        @NonNull
        public static VM getInstance(FragmentActivity activity) {
            return ViewModelProviders.of(activity).get(VM.class);
        }

        @Override
        public void onDestroy(@NonNull LifecycleOwner owner) {
            Iterator<String> iterator = mNotifierStorage.keys().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                DialogNotifier notifier = mNotifierStorage.get(key);
                // 移除监听
                notifier.invoker().removeListener(mListenerStorage.remove(key));
                // 移除空闲的通知者(未在显示)
                if (!notifier.isDialogShowing) {
                    iterator.remove();
                }
            }
            mInvokerProvider = null;
        }

        @Override
        protected void onCleared() {
            super.onCleared();
            mNotifierStorage.invokeAll(notifier -> notifier.callback().notifyNow(new DialogNotifierMsg.OnDismissDialog(OTHER)));
        }

        @NonNull
        public DialogNotifier getNotifier(String type) {
            DialogNotifier notifier = mNotifierStorage.get(type, DialogNotifier::new);
            Invoker<DialogNotifierMsg.Invoker> invoker = notifier.invoker();
            // 若已绑定或不能新绑定，则直接返回
            if (invoker.hasListener() || null == mInvokerProvider) {
                return notifier;
            }
            // 为通知者绑定弹窗
            IDialog dialog = mInvokerProvider.getNewDialog(type, notifier.uid);
            if (null != dialog) {
                dialog.onBindNotifier(type, notifier);
                invoker.addListener(dialog);
                mListenerStorage.put(type, dialog);
            }
            return notifier;
        }
    }
}
