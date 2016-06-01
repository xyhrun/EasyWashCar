package com.xyh.easywashcar.net;

import android.os.Handler;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 向阳湖 on 2016/5/27.
 */
public class NewsJsonHttp extends Thread{
    private String apikey = "6b956c46fa58a90ab9bdb8c55c1b70f1";
    private String httpPrefixUrl = "http://apis.baidu.com/showapi_open_bus/channel_news/search_news";
//    private String httpArg = "channelId=5572a109b3cdc86cf39001db&channelName=%E5%9B%BD%E5%86%85%E6%9C%80%E6%96%B0&title=%E4%B8%8A%E5%B8%82&page=1&needContent=0&needHtml=0";
    //汽车频道
    private String httpArg = "channelId=5572a109b3cdc86cf39001e5&channelName=%E5%9B%BD%E5%86%85%E6%9C%80%E6%96%B0&title=%E4%B8%8A%E5%B8%82&page=1&needContent=0&needHtml=0";
    private String url;
    private Handler handler;
    private TextView content;
    private String response;
    public NewsJsonHttp(Handler handler, TextView content) {
        this.handler = handler;
        this.content = content;
    }

        public void run() {
            url = httpPrefixUrl + "?" + httpArg;
            BufferedReader reader = null;
            InputStream inputStream = null;
            StringBuffer sb = new StringBuffer();
            String str = null;
            try {
                URL httpUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(5000);
                //写入apikey到http header
                connection.setRequestProperty("apikey", apikey);
                connection.connect();
                inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                    sb.append("\r\n");
                }
                reader.close();
                response = sb.toString();
                Logger.d(response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        content.setText(response);
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

}


