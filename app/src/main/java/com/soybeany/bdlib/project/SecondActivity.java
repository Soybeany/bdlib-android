package com.soybeany.bdlib.project;

import android.view.View;

import com.soybeany.bdlib.android.mvp.IPresenterProvider;
import com.soybeany.bdlib.android.mvp.MvpPlugin;
import com.soybeany.bdlib.android.template.BaseActivity;
import com.soybeany.bdlib.android.template.interfaces.IPluginManager;
import com.soybeany.bdlib.android.template.plugins.extend.DialogNotifierPlugin;
import com.soybeany.bdlib.android.template.plugins.extend.ThemePlugin;
import com.soybeany.bdlib.android.util.LogUtils;
import com.soybeany.bdlib.android.util.ToastUtils;
import com.soybeany.bdlib.android.util.dialog.DialogFragmentUtils;
import com.soybeany.bdlib.android.util.dialog.IRealDialog;
import com.soybeany.bdlib.android.web.dialog.INotifierProvider;
import com.soybeany.bdlib.android.web.dialog.NotifierDialogFragment;
import com.soybeany.bdlib.android.web.notifier.DVNotifier;

/**
 * <br>Created by Soybeany on 2019/4/15.
 */
public class SecondActivity extends BaseActivity implements ITestView, MvpPlugin.ICallback,
        INotifierProvider, DialogNotifierPlugin.ICallback {

    private ThemePlugin mThemePlugin;
    private DialogNotifierPlugin mDialogNotifierPlugin;

    private TestPresenter mPt;

    @Override
    public int setupLayoutResId() {
        return R.layout.activity_second;
    }

    @Override
    public void onInitPresenters(IPresenterProvider provider) {
        mPt = provider.get(TestPresenter.class, this);
    }

    @Override
    public void showMsg(String desc, String msg) {
        LogUtils.test(desc + "有效果");
        ToastUtils.show(desc + msg);
    }

    public void onClick(View view) {
        mPt.testFile();
//        mPt.testAsync();
//        mThemePlugin.toTheme(ThemeChanger.Info.theme(R.style.NoActionBar));
//        new Thread(() -> {
//            List<IDialogMsg> mDataList = new LinkedList<>();
//            for (int i = 0; i < 5; i++) {
//                mDataList.add(new StdDialogMsg().hint("what" + i));
//                mDialogNotifier.invoker().notifyNow(new DialogInvokerMsg()
//                        .type(TYPE_SHOW_MSG).data(mDataList.get(i)));
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            int[] order = new int[]{3, 2, 4, 0, 1};
//            for (int i : order) {
//                mDialogNotifier.invoker().notifyNow(new DialogInvokerMsg()
//                        .type(TYPE_POP_MSG).data(mDataList.get(i)));
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

    }

    @Override
    public void onSetupPlugins(IPluginManager manager) {
        super.onSetupPlugins(manager);
        manager.load(new MvpPlugin(this, this));
        manager.load(mThemePlugin = new ThemePlugin(this, MainActivity.THEME_DATA));
        manager.load(mDialogNotifierPlugin = new DialogNotifierPlugin(this, this));
    }

    @Override
    public DVNotifier getDialogNotifier(String type) {
        return mDialogNotifierPlugin.getDialogNotifier(type);
    }

    @Override
    public IRealDialog onGetNewDialog(String type) {
        return DialogFragmentUtils.getInstance(this, "test", NotifierDialogFragment::new);
    }
}
