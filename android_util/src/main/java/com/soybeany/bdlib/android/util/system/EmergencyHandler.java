package com.soybeany.bdlib.android.util.system;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.AFileUtils;
import com.soybeany.bdlib.android.util.LogUtils;
import com.soybeany.bdlib.core.util.TimeUtils;
import com.soybeany.bdlib.core.util.file.FileUtils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * 在程序崩溃时记录崩溃情况
 * <br>Created by Ben on 2016/7/8.
 */
public class EmergencyHandler implements Thread.UncaughtExceptionHandler {

    private static final EmergencyHandler mInstance = new EmergencyHandler();
    private static Thread.UncaughtExceptionHandler mHandler;

    private static String PATH; // 日志记录的路径
    private static ICallback CALLBACK; // 回调

    private EmergencyHandler() {
    }

    /**
     * 初始化
     *
     * @param addSdPrefix 是否添加sd卡路径前缀
     */
    public static void init(String path, boolean addSdPrefix, @Nullable ICallback callback) {
        PATH = addSdPrefix ? (AFileUtils.SD_PATH + path) : path;
        CALLBACK = (null == callback ? new ICallback.Std() : callback);
        if (mHandler == null) {
            mHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(mInstance);
        }
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        processException(ex);
        mHandler.uncaughtException(thread, ex);
    }

    /**
     * 捕捉崩溃信息
     */
    private void processException(final Throwable th) {
        try {
            // 获得崩溃信息
            Writer result = new StringWriter();
            PrintWriter printWriter = new PrintWriter(result);
            th.printStackTrace(printWriter);
            String stacktrace = result.toString();
            printWriter.close();
            // 拼接崩溃信息
            DeviceInfoUtils.SoftwareInfo sInfo = DeviceInfoUtils.getSoftwareInfo();
            DeviceInfoUtils.HardwareInfo hInfo = DeviceInfoUtils.getHardwareInfo();
            String sb = "手机型号: " + hInfo.devModel +
                    "  CPU型号: " + hInfo.devCPU +
                    "  系统版本: " + hInfo.sysVerName +
                    "  应用版本号: " + sInfo.versionName +
                    "\n\n" + stacktrace;
            // 输出崩溃信息
            String fileName = CALLBACK.getFileName();
            FileUtils.writeToFile(sb, new File(PATH, fileName), false, null);
            CALLBACK.onFinish(fileName);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public interface ICallback {
        @NonNull
        String getFileName();

        void onFinish(String fileName);

        class Std implements ICallback {
            @NonNull
            @Override
            public String getFileName() {
                return "/android_crash_" + TimeUtils.getCurrentTime(TimeUtils.FORMAT_yyyy_MM_dd_HH_mm_ss2) + ".txt";
            }

            @Override
            public void onFinish(String fileName) {
                LogUtils.i("System", "崩溃日志已生成: " + fileName);
            }
        }
    }
}
