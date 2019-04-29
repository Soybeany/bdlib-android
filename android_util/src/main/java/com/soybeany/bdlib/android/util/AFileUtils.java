package com.soybeany.bdlib.android.util;

import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import com.soybeany.bdlib.android.util.system.DeviceInfoUtils;
import com.soybeany.bdlib.core.util.file.FileUtils;

import java.io.File;

/**
 * <br>Created by Soybeany on 2019/2/21.
 */
public class AFileUtils extends FileUtils {
    /**
     * SD卡路径
     */
    public static final String SD_PATH = DeviceInfoUtils.EXT_STORAGE_DIR.getAbsolutePath();

    /**
     * SD卡加载失败时的提示
     */
    public static final String SDCARD_MOUNTED_FAIL_TIPS = "获取存储卡失败,请检查存储卡连接状况";


    /**
     * 判断SD卡是否存在
     */
    public static boolean isSdcardMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 得到SD卡剩余空间大小(单位MB)
     */
    public static long getSDFreeSize() {
        StatFs sf = new StatFs(SD_PATH);
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSizeLong();
        // 空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocksLong();
        // 返回SD卡空闲大小
        return (freeBlocks * blockSize) / STD_MB_LENGTH; // 单位MB
    }

    /**
     * 将字节转换为大小文本
     */
    public static String getSizeStr(long sizeBytes) {
        return Formatter.formatFileSize(BDContext.getContext(), sizeBytes);
    }

    /**
     * 获得全部文件(不含目录)的大小
     */
    public static long getSize(File file, boolean needCheck) {
        long size = 0;
        if (!needCheck || isFileValid(file)) {
            for (File aFileList : file.listFiles()) {
                size += aFileList.isDirectory() ? getSize(aFileList, false) : aFileList.length();
            }
        }
        return size;
    }

    /**
     * 获得临时文件保存路径
     */
    public static String getTempFileDir() {
        return DeviceInfoUtils.CACHE_DIR.getAbsolutePath();
    }

    /**
     * 获得临时文件名
     */
    public static String getTempFileName() {
        return getUUID();
    }

    /**
     * 获得临时文件
     */
    public static File getTempFile() {
        return new File(getTempFileDir(), getTempFileName());
    }

}
