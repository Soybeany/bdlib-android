package com.soybeany.bdlib.android.util;

import android.util.Log;

import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.TimeUtils;
import com.soybeany.bdlib.core.util.thread.IExecutable;
import com.soybeany.bdlib.core.util.thread.MessageCenter;

import java.io.File;
import java.io.IOException;


/**
 * 根据全局设置（调试模式下输出）判断是否输出的日志输出类,只输出到控件台
 */
public class LogUtils {

    private static final String KEY_WRITE_LOG = "key_write_log"; // 日志线程
    private static final IExecutable WRITE_LOG_EXECUTABLE = IExecutable.ThreadPoolImpl.getSingleNew();
    private static Class CLAZZ = LogUtils.class;

    /**
     * 是否调试模式
     */
    public static boolean NEED_LOG = true;

    /**
     * 日志文件(默认不指定，若指定则会进行输出)
     */
    public static File LOG_FILE;

    static {
        // 注册日志写入回调
        MessageCenter.register(WRITE_LOG_EXECUTABLE, KEY_WRITE_LOG, data -> {
            directWriteFile((LogInfo) data);
        });
    }

    public static void v(String tag, String msg) {
        v(CLAZZ, tag, msg);
    }

    public static void v(Class clazz, String tag, String msg) {
        if (NEED_LOG) {
            Log.v(tag, getOutputMsg(clazz, msg));
            writeFile("VERB", tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        d(CLAZZ, tag, msg);
    }

    public static void d(Class clazz, String tag, String msg) {
        if (NEED_LOG) {
            Log.d(tag, getOutputMsg(clazz, msg));
            writeFile("DEBUG", tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        i(CLAZZ, tag, msg);
    }

    public static void i(Class clazz, String tag, String msg) {
        if (NEED_LOG) {
            Log.i(tag, getOutputMsg(clazz, msg));
            writeFile("INFO", tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        w(CLAZZ, tag, msg);
    }

    public static void w(Class clazz, String tag, String msg) {
        if (NEED_LOG) {
            Log.w(tag, getOutputMsg(clazz, msg));
            writeFile("WARN", tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        e(CLAZZ, tag, msg);
    }

    public static void e(Class clazz, String tag, String msg) {
        if (NEED_LOG) {
            Log.e(tag, getOutputMsg(clazz, msg));
            writeFile("ERROR", tag, msg);
        }
    }

    public static void system(String string) {
        if (NEED_LOG) {
            System.out.println(string);
            writeFile("SYS", "system", string);
        }
    }

    public static void test(String string) {
        i("test", string);
    }

    /**
     * 写入文件
     */
    public static void f(File file, String log, boolean needAsync) {
        Optional.ofNullable(file).ifPresent(f -> {
            LogInfo info = new LogInfo(file, log);
            if (needAsync) {
                MessageCenter.notify(KEY_WRITE_LOG, info, 0);
            } else {
                directWriteFile(info);
            }
        });
    }

    private static void writeFile(String level, String tag, String msg) {
        String time = TimeUtils.getCurrentTime(TimeUtils.FORMAT_yyyy_MM_dd_HH_mm_ss);
        f(LOG_FILE, time + " [" + level + "][" + tag + "] " + msg, true);
    }

    private static void directWriteFile(LogInfo info) {
        try {
            AFileUtils.writeToFile(info.log + "\n", info.file, null, true, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getOutputMsg(Class clazz, String msg) {
        StackTraceElement element = null;
        boolean shouldTrace = false;
        StackTraceElement[] stackTraceArr = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTraceArr) {
            boolean isLogMethod = stackTraceElement.getClassName().equals(clazz.getName());
            if (shouldTrace && !isLogMethod) {
                element = stackTraceElement;
                break;
            }
            shouldTrace = isLogMethod;
        }
        return msg + "(" + (null != element ? element.getFileName() + ":" + element.getLineNumber() : "无法定位") + ")";
    }

    private static class LogInfo {
        File file;
        String log;

        LogInfo(File file, String log) {
            this.file = file;
            this.log = log;
        }
    }
}
