package com.soybeany.bdlib.android.util.system;

import com.soybeany.bdlib.android.util.AFileUtils;
import com.soybeany.bdlib.android.util.LogUtils;
import com.soybeany.bdlib.core.util.IterableUtils;
import com.soybeany.bdlib.core.util.TimeUtils;
import com.soybeany.bdlib.core.util.file.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.Nullable;

/**
 * 在程序崩溃时记录崩溃情况
 * <br>Created by Ben on 2016/7/8.
 */
public class EmergencyHandler implements Thread.UncaughtExceptionHandler {

    private static final EmergencyHandler INSTANCE = new EmergencyHandler();
    private static final Thread.UncaughtExceptionHandler TARGET;
    private static final Set<ICallback> CALLBACKS = new HashSet<>();
    private static final DeviceInfoUtils.SoftwareInfo SOFTWARE_INFO = DeviceInfoUtils.getSoftwareInfo();
    private static final DeviceInfoUtils.HardwareInfo HARDWARE_INFO = DeviceInfoUtils.getHardwareInfo();
    private static final ICallback DEFAULT_CALLBACK = new StdCallback("/" + SOFTWARE_INFO.appLabel + "/crash", true);

    static {
        TARGET = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(INSTANCE);
    }

    private EmergencyHandler() {
    }

    /**
     * 初始化
     *
     * @param callback 可为null，将使用{@link StdCallback}
     */
    public static void init(@Nullable ICallback callback) {
        CALLBACKS.add(null == callback ? DEFAULT_CALLBACK : callback);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        IterableUtils.forEach(CALLBACKS, (callback, flag) -> callback.onProcessException(t, e));
        TARGET.uncaughtException(t, e);
    }

    public interface ICallback {
        void onProcessException(Thread t, Throwable e);
    }

    public static class StdCallback implements ICallback {
        private String mPath; // 路径

        public StdCallback(String path, boolean addSdPrefix) {
            mPath = addSdPrefix ? (AFileUtils.SD_PATH + path) : path;
        }

        @Override
        public void onProcessException(Thread t, Throwable e) {
            try {
                File file = new File(mPath, getFileName());
                onWrite(file, getContent(e, SOFTWARE_INFO, HARDWARE_INFO));
                onFinish(file);
            } catch (final Exception ex) {
                ex.printStackTrace();
            }
        }

        // //////////////////////////////////配置区//////////////////////////////////

        protected String getFileName() {
            return "/android_crash_" + TimeUtils.getCurrentTime(TimeUtils.FORMAT_yyyy_MM_dd_HH_mm_ss2) + ".txt";
        }

        protected String getStackTraceInfo(Throwable th) {
            Writer result = new StringWriter();
            try (PrintWriter printWriter = new PrintWriter(result)) {
                th.printStackTrace(printWriter);
                return result.toString();
            }
        }

        @SuppressWarnings("SameParameterValue")
        protected String getContent(Throwable th, DeviceInfoUtils.SoftwareInfo sInfo, DeviceInfoUtils.HardwareInfo hInfo) {
            return "手机型号: " + hInfo.devModel +
                    "  CPU型号: " + hInfo.devCPU +
                    "  系统版本: " + hInfo.sysVerName +
                    "  应用版本号: " + sInfo.versionName +
                    "\n\n" + getStackTraceInfo(th);
        }

        // //////////////////////////////////回调区//////////////////////////////////

        protected void onWrite(File file, String content) throws IOException {
            FileUtils.writeToFile(content, file, false, null);
        }

        protected void onFinish(File file) {
            LogUtils.i("System", "崩溃日志已生成: " + file.getName());
        }
    }
}
