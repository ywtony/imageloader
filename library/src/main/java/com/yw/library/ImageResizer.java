package com.yw.library;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;

/**
 * 用于图片的压缩
 * create by yangwei
 * on 2020-02-29 20:35
 */
public class ImageResizer {
    private static final String TAG = "ImageResizer";

    public ImageResizer() {
    }

    /**
     * 加载出一张Bitmap
     *
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap decodeBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 获取一个Bitmap
     * @param fd
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap decodeBitmapFromFileDescriptor(FileDescriptor fd, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        //不占用内存
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        //占用内存
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);
        return bitmap;
    }

    /**
     * 对图片进行压缩
     *
     * @param options   获取图片属性的参数
     * @param reqWidth  设定的图片宽度
     * @param reqHeight 设定的图片高度
     * @return
     */
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }
        final int width = options.outWidth;//图片的原始宽度
        final int height = options.outHeight;//图片的原始高度
        //缩放倍数
        int inSampleSize = 1;
        //如果原始宽度大于设定的宽度或者原始高度大于设定的宽度，及对图片进行压缩
        if (height > reqHeight || width > reqWidth) {
            //宽高设定为原始图片的二分之一
            final int halfWidth = width / 2;
            final int halfHeight = height / 2;
            //下面这个循环是为了计算imSampleSize的值
            //如果原始图片的二分之一除以inSampleSize仍然大于设定值的宽和高，就对inSampleSize*2；
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
