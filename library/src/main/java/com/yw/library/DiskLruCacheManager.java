package com.yw.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Looper;
import android.os.StatFs;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * 磁盘缓存管理
 * create by yangwei
 * on 2020-03-01 22:30
 */
public class DiskLruCacheManager {
    private DiskLruCache mDiskLruCache = null;
    private Context context;
    public boolean mIsDiskLruCacheCreated = false;
    private ImageResizer imageResizer;
    private LruCacheManager lruCacheManager;

    /**
     * 初始化参数
     *
     * @param context
     * @param imageResizer
     * @param lruCacheManager
     */
    public DiskLruCacheManager(Context context, ImageResizer imageResizer, LruCacheManager lruCacheManager) {
        this.context = context;
        this.imageResizer = imageResizer;
        this.lruCacheManager = lruCacheManager;
        //创建磁盘缓存路径
        File diskCacheDir = FileUtil.createDiskCacheDir(context, "bitmap");
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }
        if (getUsableSpace(diskCacheDir) > CacheManager.DISK_CACHE_SIZE) {
            try {
                mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, CacheManager.DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加Bitmap到磁盘
     *
     * @param url
     */
    public void addBitmapToDiskCache(String url) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("can not visit network form Ui Thread");
        }
        try {
            String key = Util.hashKeyFromUrl(url);
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(CacheManager.DISK_CACHE_INDEX);
                if (DownloadFileUtil.get().downloadUrlToStream(url, outputStream)) {
                    editor.commit();
                } else {
                    editor.abort();
                }
                mDiskLruCache.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从磁盘中加载图片
     *
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap loadBitmapFromDiskCache(String url, int reqWidth, int reqHeight) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("can not load bitmap from UI Thread");
        }
        if (mDiskLruCache == null) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            String key = Util.hashKeyFromUrl(url);
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot != null) {
                FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(CacheManager.DISK_CACHE_INDEX);
                FileDescriptor fileDescriptor = fileInputStream.getFD();
                bitmap = imageResizer.decodeBitmapFromFileDescriptor(fileDescriptor, reqWidth, reqHeight);
                try {
                    if (bitmap.getHeight() > 0) {
                        if (bitmap != null) {
                            lruCacheManager.addBitmapToMemoryCache(key, bitmap);
                        }
                    }
                } catch (Exception e) {
                    return null;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public long getUsableSpace(File path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        }
        final StatFs statFs = new StatFs(path.getPath());
        return statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();
    }

}
