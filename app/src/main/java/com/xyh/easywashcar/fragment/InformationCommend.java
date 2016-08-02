package com.xyh.easywashcar.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xyh.easywashcar.R;
import com.xyh.easywashcar.activity.NewsContentActivity;
import com.xyh.easywashcar.adapter.RecommendNewsAdapter;
import com.xyh.easywashcar.base.MyAppcation;
import com.xyh.easywashcar.model.NewsContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 向阳湖 on 2016/5/25.
 */
public class InformationCommend extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.news_listView_id)
    ListView mListView;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.no_netWork_tip_id)
    RelativeLayout tip;
//    @Bind(R.id.news_content_id)
//    TextView content;

    private static final String TAG = "InformationCommend";
    private String apikey = "6b956c46fa58a90ab9bdb8c55c1b70f1";
    private String httpPrefixUrl = "http://apis.baidu.com/showapi_open_bus/channel_news/search_news";
    //汽车频道
    private String httpArg = "channelId=5572a109b3cdc86cf39001e5&channelName=%E5%9B%BD%E5%86%85%E6%9C%80%E6%96%B0&title=%E4%B8%8A%E5%B8%82&page=1&needContent=0&needHtml=1";
    private String url = httpPrefixUrl + "?" + httpArg;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<NewsContent> newsContentList;
    private RecommendNewsAdapter recommendNewsAdapter;

    //创建newsContent的意图
    private Intent  newsContent;
    public InformationCommend(Context context) {
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
        newsContent = new Intent(mContext, NewsContentActivity.class);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.information_commend, container, false);
        ButterKnife.bind(this, view);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_green_blue);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "++onActivityCreated: 新闻推荐碎片执行了吗");
        if (isNetworkConnected(mContext)) {
            tip.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            getDataFromVolley();
        } else {
            tip.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }
        mListView.setOnItemClickListener(this);
    }

    //判断是否有网络,有则返回true
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    private void getDataFromVolley() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.i(TAG, "onResponse: "+response);
                Log.d(TAG, "++onActivityCreated: volley执行了吗");
                dealData(response);
//                dealDataByGson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            //添加header, 里面存放的是apikey
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
                String resourceLink = contentObject.getString("link");
                String html = contentObject.getString("html");
                //设置数据
                newsContent.setTitle(newsTitle);
                newsContent.setDesc(newsDesc);
                newsContent.setPubDate(newsPubDate);
                newsContent.setResource(newsResource);
                newsContent.setSourceLink(resourceLink);
                newsContent.setHtml(html);
//                Log.d(TAG, "dealData: "+newsTitle);
                newsContentList.add(newsContent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//      最新消息显示在上面
        Collections.sort(newsContentList, new Comparator<NewsContent>() {
            @Override
            public int compare(NewsContent lhs, NewsContent rhs) {
                Date lDate = null;
                Date rDate = null;
                try {
                    lDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lhs.getPubDate());
                    rDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rhs.getPubDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (lDate.before(rDate)) {
                    return 1;
                }
                return -1;
            }
        });
        //显示数据
        recommendNewsAdapter = new RecommendNewsAdapter(mContext, newsContentList, mListView);
        mListView.setAdapter(recommendNewsAdapter);
    }

//    public void dealDataByGson(String response) {
//        List<CarNews.ShowapiResBodyBean.PagebeanBean.ContentlistBean> contentlistBeanList;
//        List<?> imgUrls;
//        Gson gson = new Gson();
//        CarNews carNews = gson.fromJson(response, CarNews.class);
//        CarNews.ShowapiResBodyBean resBodyBean = carNews.getShowapi_res_body();
//        CarNews.ShowapiResBodyBean.PagebeanBean pagebeanBean = resBodyBean.getPagebean();
//        contentlistBeanList = pagebeanBean.getContentlist();
//        Log.i(TAG, "++dealDataByGson: "+contentlistBeanList.get(0).getTitle());
//        //显示数据
////        recommendNewsAdapter = new RecommendNewsAdapter(mContext, contentlistBeanList, mListView);
////        mListView.setAdapter(recommendNewsAdapter);
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String sourceLink = newsContentList.get(position).getSourceLink();
        String html = newsContentList.get(position).getHtml();
        newsContent.putExtra("sourceLink", sourceLink);
//        newsContent.putExtra("html", html);
        startActivity(newsContent);
    }

    @Override
    public void onRefresh() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 1500);
        }
    }
}
