package com.yw.library;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * create by yangwei
 * on 2020-03-01 22:30
 */
public class LruCacheManager {
    private LruCache<String, Bitmap> mMemoryCache = null;

    public LruCacheManager() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //返回bitmap的大小，单位是kb
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    /**
     * 相内存缓存中添加一个Bitmap
     *
     * @param key
     * @param bitmap
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 从内存缓存中取出一个Bitmap
     *
     * @param key
     * @return
     */
    public Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }
}
