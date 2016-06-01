package com.xyh.easywashcar.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 向阳湖 on 2016/5/28.
 *
 *
 * Handler异步加载
 */
public class ImageLoaderByHandler {
    private static final String TAG = "ImageLoaderByHandler";
    private LruCache<String, Bitmap> mCaches;
    private String imgUrl;
    private ImageView img;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //让数据刷新时不显示之前convertview里面的图片,保证数据统一
            if (img.getTag().equals(imgUrl)) {
                img.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };

    public ImageLoaderByHandler() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 4;
        mCaches = new LruCache<String, Bitmap>(cacheMemory){
            //每次存入缓存就会调用,默认返回元素个数,修改后返回实际图片的大小.
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    //添加图片到缓存中
    public void addImageToCache(String imgUrl, Bitmap bitmap) {
        if (getImageFromUrl(imgUrl) == null) {
            mCaches.put(imgUrl, bitmap);
        }

    }

    //从缓存中获取图片
    public Bitmap getImageFromCache(String imgUrl) {
        return mCaches.get(imgUrl);
    }

    //加不加final, setTag不起作用,还是出现图片时序问题
    public void showImageByThread(final String imgUrl, ImageView img) {
        this.imgUrl = imgUrl;
        this.img = img;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //从缓存中获取图片
                Bitmap cacheBitmap = getImageFromCache(imgUrl);

                //如果缓存中没有则从网络中下载图片
                if (cacheBitmap == null) {
                    Bitmap bitmap = getImageFromUrl(imgUrl);
                    //将刚下载的图片加入到缓存中,并显示出来
                    if (bitmap != null) {
                        Message message = Message.obtain();
                        message.obj = bitmap;
                        mHandler.sendMessage(message);
                        addImageToCache(imgUrl, bitmap);
                    }
                } else {
                    //如果缓存中有则直接设置图片
                    Message message = Message.obtain();
                    message.obj = cacheBitmap;
                    mHandler.sendMessage(message);
                }

            }
        }).start();

    }

    private Bitmap getImageFromUrl(String mUrl) {
        Bitmap bitmap = null;
        Log.d(TAG, "-----getImageFromUrl: 执行了吗");
        InputStream is = null;
        try {
            URL url = new URL(mUrl);
            Logger.d("-------"+mUrl);
            Log.d(TAG, "----loadImageHttp() called with: " + mUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);
            is = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            connection.disconnect();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
