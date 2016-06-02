package com.xyh.easywashcar.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xyh.easywashcar.R;
import com.xyh.easywashcar.model.NewsContent;
import com.xyh.easywashcar.net.ImageLoaderByAsyncTask;

import java.util.List;

/**
 * Created by 向阳湖 on 2016/5/28.
 */
public class RecommendNewsAdapter extends BaseAdapter implements AbsListView.OnScrollListener{
    private static final String TAG = "RecommendNewsAdapter";
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<NewsContent> newsContentList;
    private NewsContentViewHolder newsContentViewHolder;
    private int mStart, mEnd;
    private ImageLoaderByAsyncTask imageLoaderByAsyncTask;
    private ListView mListView;
    public static String[] urls;
    private boolean firstIn;

    public RecommendNewsAdapter(Context context, List<NewsContent> newsContentList, ListView mListView) {
        this.mContext = context;
        this.newsContentList = newsContentList;
        this.mListView = mListView;
        mListView.setOnScrollListener(this);
        //保证只有一个imageLoaderByAsyncTask,为了优化缓存.
        imageLoaderByAsyncTask = new ImageLoaderByAsyncTask(mListView);
        urls = new String[newsContentList.size()];
        firstIn = true;
        for (int i = 0; i < newsContentList.size(); i++) {
            String url = newsContentList.get(i).getImgUrl();
            if (url != null) {
                urls[i] = url;
            } else {
                urls[i] = "http://news.xinhuanet.com/auto/2016-05/31/129029190_14646548423041n.jpg";
            }
        }
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        Log.d(TAG, "getCount: "+newsContentList.size());
        return newsContentList.size();
    }

    @Override
    public Object getItem(int position) {

        return newsContentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "++getView: 执行了吗");
        newsContentViewHolder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_news, parent, false);
            newsContentViewHolder = new NewsContentViewHolder(convertView);
            convertView.setTag(newsContentViewHolder);
        } else {
            newsContentViewHolder = (NewsContentViewHolder) convertView.getTag();
        }

        NewsContent newsContent = newsContentList.get(position);
        String title = newsContent.getTitle();
//        String desc = newsContent.getDesc();
        String pubDate = newsContent.getPubDate();
        String resource = newsContent.getResource();
        String imgUrl = newsContent.getImgUrl();

        newsContentViewHolder.newsTitle.setText(title);
//        newsContentViewHolder.newsDesc.setText(desc);
        newsContentViewHolder.newsPubDate.setText(pubDate);
        newsContentViewHolder.newsResource.setText(resource);
//        先设置默认图片
        newsContentViewHolder.newsImg.setImageResource(R.mipmap.ic_launcher);
        if (imgUrl == null) {
            imgUrl = "http://news.xinhuanet.com/auto/2016-05/31/129029190_14646548423041n.jpg";
        }
            Log.d(TAG, "-------getView: "+imgUrl);
            //对图片设置标记
            newsContentViewHolder.newsImg.setTag(imgUrl);
            //加载解析图片的方法
            imageLoaderByAsyncTask.showImageByAsyncTask(imgUrl, newsContentViewHolder.newsImg);

        return convertView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //对滑动状态做处理
        if (scrollState == SCROLL_STATE_IDLE) {
            //没有滑动则加载数据
            imageLoaderByAsyncTask.loadImages(mStart, mEnd);
        } else {
            //滑动中不加载数据
            imageLoaderByAsyncTask.cancelAllTasks();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = mStart + visibleItemCount;
        //visibleItemCount会调用多次,第一次调用为0
        if (firstIn && visibleItemCount > 0) {
            imageLoaderByAsyncTask.loadImages(mStart, mEnd);
        }
        firstIn = false;
    }

    class NewsContentViewHolder {
        private ImageView newsImg;
        private TextView newsTitle;
//        private TextView newsDesc;
        private TextView newsPubDate;
        private TextView newsResource;

        public NewsContentViewHolder(View view) {
            newsImg = (ImageView) view.findViewById(R.id.news_img_id);
            newsTitle = (TextView)view.findViewById(R.id.news_title_id);
//            newsDesc = (TextView)view.findViewById(R.id.news_desc_id);
            newsPubDate = (TextView)view.findViewById(R.id.news_pubDate_id);
            newsResource = (TextView) view.findViewById(R.id.news_resource_id);
        }
    }

}
