package com.soybeany.bdlib.android.template.plugins.extend;

import android.app.Activity;
import android.view.MenuItem;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.util.system.KeyboardUtils;

/**
 * 只在{@link Activity}中使用，需自行调用{@link #onOptionsItemSelected(MenuItem, ISuperOnOptionsItemSelected)}
 * <br>Created by Soybeany on 2019/4/29.
 */
public class ToolbarPlugin implements IExtendPlugin {
    private Activity mActivity;
    private ITemplate mTemplate;

    public ToolbarPlugin(Activity activity, ITemplate template) {
        mActivity = activity;
        mTemplate = template;
    }

    public boolean onOptionsItemSelected(MenuItem item, ISuperOnOptionsItemSelected callback) {
        if (android.R.id.home == item.getItemId()) {
            KeyboardUtils.closeKeyboard(mActivity);
            mTemplate.onBackItemPressed();
        }
        return callback.onInvoke(item);
    }

    public interface ISuperOnOptionsItemSelected {
        boolean onInvoke(MenuItem item);
    }

    public interface ITemplate {
        void onBackItemPressed();
    }
}
