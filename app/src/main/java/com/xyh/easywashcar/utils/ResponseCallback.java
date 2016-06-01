package com.xyh.easywashcar.utils;

/**
 * Created by 向阳湖 on 2016/5/27.
 */
public interface ResponseCallback {
    void onSuccess(String result);

    void onFail(String failReason);
}
