package com.soybeany.bdlib.android.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.File;

/**
 * 设备信息
 * <br>Created by Ben on 2016/6/24.
 */
public class DeviceInfoUtils {

    /**
     * 根目录
     */
    public static final File ROOT_DIR = Environment.getRootDirectory();

    /**
     * 数据目录
     */
    public static final File DATA_DIR = Environment.getDataDirectory();

    /**
     * 外部存储路径
     */
    public static final File EXT_STORAGE_DIR = Environment.getExternalStorageDirectory();

    private static final Context CONTEXT = BDContext.getContext(); // 上下文

    /**
     * 文件路径
     */
    public static final File FILES_DIR = CONTEXT.getFilesDir();

    /**
     * 缓存路径
     */
    public static final File CACHE_DIR = CONTEXT.getCacheDir();

    private static final String TAG = DeviceInfoUtils.class.getSimpleName();

    private static SoftwareInfo SOFT_INFO; // 软件信息单例
    private static HardwareInfo HARD_INFO; // 硬件信息单例

    /**
     * 获得软件信息
     */
    public static SoftwareInfo getSoftwareInfo() {
        if (null == SOFT_INFO) {
            SOFT_INFO = createSoftwareInfo();
        }
        return SOFT_INFO;
    }

    /**
     * 获得硬件信息
     */
    public static HardwareInfo getHardwareInfo() {
        if (null == HARD_INFO) {
            HARD_INFO = createHardwareInfo();
        }
        return HARD_INFO;
    }

    /**
     * 是否支持Material Design(即系统版本是否为5.0及以上)
     */
    public static boolean isSupportMD() {
        return getHardwareInfo().sysVerCode >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * 获得状态栏高度
     */
    public static int getStatusBarHeight() {
        int result = 0;
        Resources resources = BDContext.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 打印设备信息
     */
    public static void print() {
        SoftwareInfo sInfo = getSoftwareInfo();
        HardwareInfo hInfo = getHardwareInfo();
        LogUtils.i(TAG, "＝＝＝＝＝＝＝＝＝＝＝ ［ DeviceInfo ］＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
        LogUtils.i(TAG, sInfo.appLabel + "(" + sInfo.packageName + ")" + " v" + sInfo.versionName + "(" + sInfo.versionCode + ")");
        LogUtils.i(TAG, "Root             dir: " + ROOT_DIR);
        LogUtils.i(TAG, "Data             dir: " + DATA_DIR);
        LogUtils.i(TAG, "External storage dir: " + EXT_STORAGE_DIR);
        LogUtils.i(TAG, "Files            dir: " + FILES_DIR);
        LogUtils.i(TAG, "Cache            dir: " + CACHE_DIR);
        LogUtils.i(TAG, "SYSTEM      : " + hInfo.sysVerName + "(" + hInfo.sysVerCode + ")");
        LogUtils.i(TAG, "BRAND       : " + hInfo.devBrand);
        LogUtils.i(TAG, "CPU         : " + hInfo.devCPU);
        LogUtils.i(TAG, "FINGERPRINT : " + hInfo.devUId);
        LogUtils.i(TAG, "ID          : " + hInfo.devId);
        LogUtils.i(TAG, "SERIAL      : " + hInfo.serial);
        LogUtils.i(TAG, "MODEL       : " + hInfo.devModel);
        LogUtils.i(TAG, "PRODUCT     : " + hInfo.devProduct);
        LogUtils.i(TAG, "RESOLUTION  : " + hInfo.scResolutionX + " × " + hInfo.scResolutionY);
        LogUtils.i(TAG, "PPI         : " + hInfo.scPpiX + " × " + hInfo.scPpiY);
        LogUtils.i(TAG, "DP          : " + hInfo.scDpX + " × " + hInfo.scDpY);
        LogUtils.i(TAG, "SCREEN SIZE : " + hInfo.scSize);
        LogUtils.i(TAG, "＝＝＝＝＝＝＝＝＝＝＝ ［ DeviceInfo ］＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
    }

    /**
     * 创建软件信息
     */
    private static SoftwareInfo createSoftwareInfo() {
        PackageManager pm = CONTEXT.getPackageManager();
        SoftwareInfo info = null;
        try {
            PackageInfo pi = pm.getPackageInfo(CONTEXT.getPackageName(), 0);
            info = new SoftwareInfo(
                    CONTEXT.getString(pi.applicationInfo.labelRes),
                    pi.packageName,
                    pi.versionName,
                    pi.versionCode
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 创建硬件信息
     */
    @SuppressLint("HardwareIds")
    private static HardwareInfo createHardwareInfo() {
        HardwareInfo info = null;
        try {
            DisplayMetrics dm = getCurDM();
            info = new HardwareInfo(
                    Build.VERSION.RELEASE,
                    Build.VERSION.SDK_INT,
                    Build.BRAND,
                    Build.CPU_ABI,
                    Build.FINGERPRINT,
                    Build.ID,
                    Build.SERIAL,
                    Build.MODEL,
                    Build.PRODUCT,
                    dm.widthPixels,
                    dm.heightPixels,
                    dm.xdpi,
                    dm.ydpi,
                    dm.widthPixels * 160 / dm.xdpi,
                    dm.heightPixels * 160 / dm.ydpi,
                    getScreenInches(dm)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 获得当前屏幕的信息
     */
    private static DisplayMetrics getCurDM() {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) CONTEXT.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 获得屏幕尺寸
     */
    private static double getScreenInches(DisplayMetrics dm) {
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        return Math.sqrt(x + y);
    }

    /**
     * 软件信息对象
     */
    public static class SoftwareInfo {

        /**
         * 应用名称
         */
        public final String appLabel;

        /**
         * 应用包名
         */
        public final String packageName;

        /**
         * 应用版本名称
         */
        public final String versionName;

        /**
         * 应用版本号
         */
        public final int versionCode;

        SoftwareInfo(String appLabel, String packageName, String versionName, int versionCode) {
            this.appLabel = appLabel;
            this.packageName = packageName;
            this.versionName = versionName;
            this.versionCode = versionCode;
        }
    }

    /**
     * 硬件信息对象
     */
    public static class HardwareInfo {

        /**
         * 系统版本名称
         */
        public final String sysVerName;

        /**
         * 系统版本号
         */
        public final int sysVerCode;

        /**
         * 设备品牌
         */
        public final String devBrand;

        /**
         * 设备CPU
         */
        public final String devCPU;

        /**
         * 设备唯一标识
         */
        public final String devUId;

        /**
         * 设备版本号
         */
        public final String devId;

        /**
         * 设备序列号
         */
        public final String serial;

        /**
         * 设备型号
         */
        public final String devModel;

        /**
         * 设备名称
         */
        public final String devProduct;

        /**
         * 屏幕X轴分辨率(单位:px)
         */
        public final int scResolutionX;

        /**
         * 屏幕Y轴分辨率(单位:px)
         */
        public final int scResolutionY;

        /**
         * 屏幕X轴像素密度
         */
        public final float scPpiX;

        /**
         * 屏幕Y轴像素密度
         */
        public final float scPpiY;

        /**
         * 屏幕X轴分辨率(单位:dp)
         */
        public final float scDpX;

        /**
         * 屏幕Y轴分辨率(单位:dp)
         */
        public final float scDpY;

        /**
         * 屏幕尺寸
         */
        public final double scSize;

        public HardwareInfo(String sysVerName, int sysVerCode, String devBrand, String devCPU,
                            String devUId, String devId, String serial, String devModel, String devProduct,
                            int scResolutionX, int scResolutionY, float scPpiX, float scPpiY, float scDpX,
                            float scDpY, double scSize) {
            this.sysVerName = sysVerName;
            this.sysVerCode = sysVerCode;
            this.devBrand = devBrand;
            this.devCPU = devCPU;
            this.devUId = devUId;
            this.devId = devId;
            this.serial = serial;
            this.devModel = devModel;
            this.devProduct = devProduct;
            this.scResolutionX = scResolutionX;
            this.scResolutionY = scResolutionY;
            this.scPpiX = scPpiX;
            this.scPpiY = scPpiY;
            this.scDpX = scDpX;
            this.scDpY = scDpY;
            this.scSize = scSize;
        }
    }

}
