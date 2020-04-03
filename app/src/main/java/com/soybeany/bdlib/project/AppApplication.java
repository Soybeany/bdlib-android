package com.soybeany.bdlib.project;

import com.soybeany.bdlib.android.util.BDApplication;
import com.soybeany.bdlib.android.util.system.EmergencyHandler;

/**
 * <br>Created by Soybeany on 2019/5/15.
 */
public class AppApplication extends BDApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        EmergencyHandler.init(null);
    }
}
