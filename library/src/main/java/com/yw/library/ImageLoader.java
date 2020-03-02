package com.yw.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.View;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 一个ImageLoader类用于图片加载
 * create by yangwei
 * on 2020-02-29 20:59
 */
public class ImageLoader {
    private ImageLoader(Context context) {
        mainThreadHandler = new MainThreadHandler();
        cacheManager = new CacheManager(context, THREAD_POOL_EXECUTOR, mainThreadHandler);
    }

    private static final String TAG = "ImageLoader";
    public static final int MESSAGE_POST_RESULT = 1;
    //核心Cpu数量
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;//核心线程数量
    //线程池的最大容量
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIZE = 10l;


    private static final ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "ImageLoader#" + mCount.getAndIncrement());
        }
    };
    //创建一个线程池
    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIZE, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(), threadFactory);
    private Context context;
    private CacheManager cacheManager;
    private MainThreadHandler mainThreadHandler;
    private static ImageLoader imageLoader = null;
    public static ImageLoader getDefault(Context context) {
        synchronized (ImageLoader.class){
            if(imageLoader==null){
                imageLoader = new ImageLoader(context);
            }
        }
        return imageLoader;
    }



    /**
     * 异步加载一张图片并显示在view上
     *
     * @param url
     * @param view
     */
    public void displayView(String url, View view) {

        cacheManager.bindBitmap(url, view);
    }

    /**
     * 根据url获取一个bitmap
     *
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap getBitmap(String url, int reqWidth, int reqHeight) {
        return cacheManager.loadBitmap(url, reqWidth, reqHeight);
    }

    /**
     * 异步加载指定图片的大小加载图片
     *
     * @param url
     * @param view
     * @param reqWidth
     * @param reqHeight
     */
    public void displayView(String url, View view, int reqWidth, int reqHeight) {
        cacheManager.bindBitmap(url, view, reqWidth, reqHeight);
    }


}
