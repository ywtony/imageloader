package com.yw.library;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 工具类
 * create by yangwei
 * on 2020-02-29 21:27
 */
public class FileUtil {
    /**
     * 创建磁盘缓存路径
     *
     * @param context
     * @param dirName
     * @return
     */
    public static File createDiskCacheDir(Context context, String dirName) {
        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if (externalStorageAvailable) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + dirName);

    }
}
