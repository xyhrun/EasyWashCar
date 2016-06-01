package com.xyh.easywashcar.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xyh.easywashcar.R;
import com.xyh.easywashcar.adapter.RecommendNewsAdapter;
import com.xyh.easywashcar.base.MyAppcation;
import com.xyh.easywashcar.model.NewsContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 向阳湖 on 2016/5/25.
 */
public class InformationCommend extends Fragment {

    @Bind(R.id.news_listView_id)
    ListView mListView;
    private static final String URL = "http://www.imooc.com/api/teacher?type=4&num=30";
//    @Bind(R.id.news_content_id)
//    TextView content;
    private static final String TAG = "InformationCommend";
    private Handler handler = new Handler();
    private String apikey = "6b956c46fa58a90ab9bdb8c55c1b70f1";
    private String httpPrefixUrl = "http://apis.baidu.com/showapi_open_bus/channel_news/search_news";
    //汽车频道
    private String httpArg = "channelId=5572a109b3cdc86cf39001e5&channelName=%E5%9B%BD%E5%86%85%E6%9C%80%E6%96%B0&title=%E4%B8%8A%E5%B8%82&page=1&needContent=0&needHtml=0";
    private String url = httpPrefixUrl + "?" + httpArg;
    private String response;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<NewsContent> newsContentList;
    private RecommendNewsAdapter recommendNewsAdapter;
    public InformationCommend(Context context) {
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.information_commend, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Logger.v(response);
//                content.setText(response);
                dealData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("apikey",apikey);
                return map;
            }
        };

        MyAppcation.getRequestQueue().add(stringRequest);

    }

    private void dealData(String response) {
        newsContentList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(response);
            JSONObject object1 = object.getJSONObject("showapi_res_body");
            JSONObject object2 = object1.getJSONObject("pagebean");
            JSONArray contentArray = object2.getJSONArray("contentlist");

            for (int i = 0; i < contentArray.length(); i++) {
                String newsImgUrl;
                //获得文本主体
                JSONObject contentObject = contentArray.getJSONObject(i);
                NewsContent newsContent = new NewsContent();
                //获得图片数组,最终只获取第一张图片,并对解析的图片数据加以判断
                JSONArray imgArray = contentObject.getJSONArray("imageurls");
                if (imgArray.length() > 0) {
                    JSONObject imgObject = imgArray.getJSONObject(0);
                    newsImgUrl = imgObject.getString("url");
                    Log.d(TAG, "------dealData: "+newsImgUrl);
                    newsContent.setImgUrl(newsImgUrl);
                }

                //从解析出来的数据中获取更精准的数据
                String newsTitle = contentObject.getString("title");
                String newsDesc = contentObject.getString("desc");
                String newsPubDate = contentObject.getString("pubDate");
                String newsResource = contentObject.getString("source");
                //设置数据
                newsContent.setTitle(newsTitle);
                newsContent.setDesc(newsDesc);
                newsContent.setPubDate(newsPubDate);
                newsContent.setResource(newsResource);
//                Log.d(TAG, "dealData: "+newsTitle);
                newsContentList.add(newsContent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //显示数据
        recommendNewsAdapter = new RecommendNewsAdapter(mContext, newsContentList, mListView);
        mListView.setAdapter(recommendNewsAdapter);
    }

}
