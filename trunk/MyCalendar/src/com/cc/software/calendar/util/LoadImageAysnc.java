package com.cc.software.calendar.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

public class LoadImageAysnc {

    //线程池
    private BlockingQueue<Runnable> queue;
    private ThreadPoolExecutor executor;
    private HashMap<String, SoftReference<Bitmap>> imageCache;
    private static LoadImageAysnc mInstance;

    public LoadImageAysnc() {
        queue = new LinkedBlockingQueue<Runnable>();
        executor = new ThreadPoolExecutor(1, 5, 180, TimeUnit.SECONDS, queue);
        imageCache = new HashMap<String, SoftReference<Bitmap>>();
    }

    public static LoadImageAysnc getInstance() {
        if (mInstance == null) {
            mInstance = new LoadImageAysnc();
        }
        return mInstance;
    }

    //异步加载图片的方法（其实可以以任何形式得到图片）
    public Bitmap loadImage(final String iconSrc, final ImageCallBack imageCallBack) {

        //如果有缓存则使用缓存中的图片
        if (imageCache.containsKey(iconSrc)) {
            SoftReference<Bitmap> softReference = imageCache.get(iconSrc);
            Bitmap drawable = softReference.get();
            if (drawable != null) {
                return drawable;
            }
        }

        //图片加载完成
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bitmap drawable = (Bitmap) msg.obj;
                //图片加载完成的回调函数，用于更新imageview
                imageCallBack.imageLoaded(drawable, iconSrc);
            }
        };

        //异步图片下载方法
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap drawable = loadImageFromUrl(iconSrc);
                if (drawable != null)
                    imageCache.put(iconSrc, new SoftReference<Bitmap>(drawable));
                Message msg = handler.obtainMessage(0, drawable);
                msg.sendToTarget();

            }
        });
        return null;
    }

    //网络图片下载方法
    public Bitmap loadImageFromUrl(String url) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPurgeable = true;//xuesong: 优化图片占用内存. 12-01-07
        Bitmap bitmap = BitmapFactory.decodeStream(getInputStream(url), null, opts);
        return bitmap;
    }

    //本地图片加载方法
    /*public Drawable loadImageFromLocalStorage(String fileName) {
    	Drawable d = fileOperator.readFromSD2Output(fileName);
    	return d;
    }*/

    //得到网上下载的图片数据流
    public InputStream getInputStream(String url) {
        URL m;
        InputStream i = null;
        try {
            m = new URL(url);
            i = (InputStream) m.getContent();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return i;
    }

    //回调接口
    public interface ImageCallBack {
        public void imageLoaded(Bitmap drawable, String iconSrc);
    }
}
