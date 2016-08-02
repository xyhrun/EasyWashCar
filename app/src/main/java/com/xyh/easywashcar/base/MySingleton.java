package com.xyh.easywashcar.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by 向阳湖 on 2016/7/29.
 */
public class MySingleton {
    private static MySingleton mySingleton;
    private ImageLoader mImageLoader;
    private Context mContext;
    private static RequestQueue mRequestQueue;

    private MySingleton(Context mContext) {
        this.mContext = mContext;
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            //建立一个图片缓存池, 初始大小为20
            private final LruCache<String, Bitmap> lruCache = new LruCache<>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return lruCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                lruCache.put(url, bitmap);
            }
        });
    }

    //同步获取实例
    public static synchronized MySingleton getInstance(Context mContext) {
        if (mySingleton == null) {
            mySingleton = new MySingleton(mContext);
        }
        return mySingleton;
    }

    //获取队列
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    //把请求添加到队列
    public <T> void addToRequestQueue(Request<T> mRequest) {
        getRequestQueue().add(mRequest);
    }

    //获取ImageLoader
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
