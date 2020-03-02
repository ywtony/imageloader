package com.yw.library;

import android.util.Log;

/**
 * create by yangwei
 * on 2020-02-29 20:36
 */
public class DevLog {
    private static final String TAG = "DevLog";

    public static void e(String e) {
        Log.e(TAG, e);
    }

    public static void e(int e) {
        Log.e(TAG, e + "");
    }
}
