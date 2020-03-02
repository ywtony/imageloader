package com.yw.library;

import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

/**
 * 创建一个用来管理主线程的类
 * create by yangwei
 * on 2020-03-01 21:44
 */
public class MainThreadHandler extends Handler {
    public MainThreadHandler() {
        super(Looper.getMainLooper());
    }

    @Override
    public void handleMessage(Message msg) {
        LoaderResult loaderResult = (LoaderResult) msg.obj;
        View view = loaderResult.getView();
        String uri = (String) view.getTag(MsgConfig.TAG_KEY_URI);
        if (!uri.equals(loaderResult.getUri())) {
            DevLog.e("set image bitmap,but url has changed,ignored");
        }
        if(view instanceof ImageView){
            ImageView imageView = (ImageView) view;
            imageView.setImageBitmap(loaderResult.getBitmap());
        }else{
            view.setBackground(new BitmapDrawable(loaderResult.getBitmap()));
        }

    }

}
