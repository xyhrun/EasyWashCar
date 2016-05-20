package com.xyh.easywashcar.base;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by 向阳湖 on 2016/5/18.
 */
public class MyAppcation extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static void myToast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}
