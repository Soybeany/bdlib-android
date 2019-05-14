package com.soybeany.bdlib.project;

import com.soybeany.bdlib.android.util.BDApplication;
import com.squareup.leakcanary.LeakCanary;

/**
 * <br>Created by Soybeany on 2019/5/15.
 */
public class AppApplication extends BDApplication {

    @Override
    public void onCreate() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
        super.onCreate();
    }
}
