package com.yw.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Looper;
import android.os.StatFs;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 缓存管理类
 * create by yangwei
 * on 2020-03-01 22:13
 */
public class CacheManager {
    private ImageResizer imageResizer = new ImageResizer();
    private Executor THREAD_POOL_EXECUTOR;
    //磁盘缓存的最大容量
    public static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;
    public static final int DISK_CACHE_INDEX = 0;
    private LruCacheManager lruCacheManager;
    private DiskLruCacheManager diskLruCacheManager;
    private MainThreadHandler mainThreadHandler;
    private Context context;
    public CacheManager(Context context,Executor THREAD_POOL_EXECUTOR,MainThreadHandler mainThreadHandler) {
        this.THREAD_POOL_EXECUTOR = THREAD_POOL_EXECUTOR;
        this.mainThreadHandler = mainThreadHandler;
        this.context = context;
        lruCacheManager = new LruCacheManager();
        diskLruCacheManager = new DiskLruCacheManager(context,imageResizer,lruCacheManager);
    }


    public void bindBitmap(final String uri, final View view) {
        bindBitmap(uri, view, 0, 0);
    }

    public void bindBitmap(final String uri, final View view, final int reqWidth, final int reqHeight) {
        view.setTag(MsgConfig.TAG_KEY_URI, uri);
        Bitmap bitmap = loadBitmapFromMemoryCache(uri);
        if (bitmap != null) {
            if (view instanceof ImageView) {
                ((ImageView) view).setImageBitmap(bitmap);
            } else {
                view.setBackground(new BitmapDrawable(bitmap));
            }
            return;
        }
        //利用线程池在后台加载图片，加载成功后进入主线程更新UI
        THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(uri, reqWidth, reqHeight);
                LoaderResult loaderResult = new LoaderResult(view, uri, bitmap);
                //向主线程中发送loaderresult
                mainThreadHandler.obtainMessage(MsgConfig.MESSAGE_POST_RESULT, loaderResult).sendToTarget();
            }
        });
    }

    public Bitmap loadBitmap(String uri, int reqWidth, int reqHeight) {
        Bitmap bitmap = loadBitmapFromMemoryCache(uri);
        if (bitmap != null) {
            return bitmap;
        }
        bitmap = diskLruCacheManager.loadBitmapFromDiskCache(uri, reqHeight, reqHeight);
        if (bitmap != null) {
            return bitmap;
        }
        bitmap = loadBitmapFromHttp(uri, reqWidth, reqHeight);
        if (bitmap != null) {
            return bitmap;
        }
        if (bitmap == null && !diskLruCacheManager.mIsDiskLruCacheCreated) {
            bitmap = DownloadFileUtil.get().downloadBitmapFromUrl(uri);
        }
        return bitmap;
    }

    private Bitmap loadBitmapFromMemoryCache(String url) {
        final String key = Util.hashKeyFromUrl(url);
        Bitmap bitmap = lruCacheManager.getBitmapFromMemoryCache(key);
        return bitmap;
    }

    private Bitmap loadBitmapFromHttp(String url, int reqWidth, int reqHeight) {
        diskLruCacheManager.addBitmapToDiskCache(url);
        return diskLruCacheManager.loadBitmapFromDiskCache(url, reqWidth, reqHeight);
    }

    public static class Builder {
        public Builder(){}

    }

}
