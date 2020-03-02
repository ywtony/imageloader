package com.yw.library;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

/**
 * 加载结果实体
 * create by yangwei
 * on 2020-02-29 21:14
 */
class LoaderResult {
    private View view;
    private String uri;
    private Bitmap bitmap;

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public LoaderResult() {
    }

    public LoaderResult(View view, String uri, Bitmap bitmap) {
        this.view = view;
        this.uri = uri;
        this.bitmap = bitmap;
    }
}
