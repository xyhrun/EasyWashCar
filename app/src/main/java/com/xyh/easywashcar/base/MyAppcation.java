package com.xyh.easywashcar.base;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by 向阳湖 on 2016/5/18.
 */
public class MyAppcation extends Application {
    private static Context context;
    private static RequestQueue mRequestQueue;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static void myToast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static Context getContext() {
        return context;
    }

    public static RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

}