package com.yw.library;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载文件的工具类
 * create by yangwei
 * on 2020-03-01 21:33
 */
public class DownloadFileUtil {
    //io缓存区
    private static final int IO_BUFFER_SIZE = 8 * 1024;
    private DownloadFileUtil() {
    }

    private static DownloadFileUtil instance = new DownloadFileUtil();

    public static DownloadFileUtil get() {
        return instance;
    }

    public  boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection httpURLConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(httpURLConnection.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream((outputStream), IO_BUFFER_SIZE);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);

            }
            return true;
        } catch (Exception e) {
            DevLog.e("download image fail");
        } finally {
            try {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    public  Bitmap downloadBitmapFromUrl(String urlString) {
        Bitmap bitmap = null;
        HttpURLConnection httpURLConnection = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(httpURLConnection.getInputStream(), IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            DevLog.e("download image fail");
        } finally {
            try {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return bitmap;
    }
}
