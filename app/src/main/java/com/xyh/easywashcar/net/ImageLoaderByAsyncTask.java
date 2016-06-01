package com.xyh.easywashcar.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import com.xyh.easywashcar.R;
import com.xyh.easywashcar.adapter.RecommendNewsAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

/**
 * Created by 向阳湖 on 2016/6/1.
 */
public class ImageLoaderByAsyncTask {
    private static final String TAG = "ImageLoaderByAsyncTask";

    private LruCache<String,Bitmap> mCaches;
    private ListView mListView;
    private Set<ImageAsyncTask1> tasks;
    public ImageLoaderByAsyncTask(ListView mListView) {
        tasks = new HashSet<>();
        this.mListView = mListView;
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 4;
        mCaches = new LruCache<String, Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    private void addImgToCache(String httpUrl, Bitmap bitmap) {
        if (getImgFromCache(httpUrl) == null) {
            mCaches.put(httpUrl, bitmap);
        }
    }

    private Bitmap getImgFromCache(String httpUrl) {
        Log.d(TAG, "++getImgFromCache: "+httpUrl);
        return mCaches.get(httpUrl);
    }

    public void showImageByAsyncTask(final String httpUrl, ImageView imageView) {
        Bitmap bitmap = getImgFromCache(httpUrl);
        //如果缓存中没有，那么必须下载
        if (bitmap == null)
        {
            imageView.setImageResource(R.mipmap.ic_launcher);
        }else {
            imageView.setImageBitmap(bitmap);
        }
    }

    private Bitmap getImageFromHttp(String httpUrl) {
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            connection.disconnect();
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void loadImages(int mStart, int mEnd) {
        for (int i = mStart; i < mEnd; i++) {
            String url = RecommendNewsAdapter.urls[i];
            Bitmap bitmap = getImgFromCache(url);
            //如果缓存中没有则下载
            if (bitmap == null) {
                Log.d(TAG, "++loadImages: 执行了吗");
                ImageAsyncTask1 task = new ImageAsyncTask1(url);
//                task.execute(url);
                task.executeOnExecutor(Executors.newCachedThreadPool());
                tasks.add(task);
            } else {
                ImageView mImageView = (ImageView) mListView.findViewWithTag(url);
                mImageView.setImageBitmap(bitmap);
            }
        }
    }

    //取消加载项
    public void cancelAllTasks() {
        if (tasks != null) {
            for (ImageAsyncTask1 task : tasks) {
                task.cancel(true);
            }
        }
    }


    public class ImageAsyncTask1 extends AsyncTask<String, Void, Bitmap> {

        String mUrl = null;

        public ImageAsyncTask1(String mUrl) {
            this.mUrl = mUrl;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Log.d(TAG, "++doInBackground: 执行了吗");
            String url = mUrl;
            Bitmap bitmap = getImageFromHttp(mUrl);
            //从网络获取图片并添加到缓存
            if (bitmap != null) {
                addImgToCache(url,bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            Log.d(TAG, "++onPostExecute: 执行了吗");
            ImageView mImageView = (ImageView) mListView.findViewWithTag(mUrl);

            if (mImageView != null && bitmap != null) {
                mImageView.setImageBitmap(bitmap);
            }

            tasks.remove(this);
        }
    }

}
