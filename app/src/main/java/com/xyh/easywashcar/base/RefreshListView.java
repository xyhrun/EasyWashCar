package com.xyh.easywashcar.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.xyh.easywashcar.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 向阳湖 on 2016/5/24.
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener{
//    @Bind(R.id.header_tip_id)
//    TextView tip;
//    @Bind(R.id.header_refresh_id)
//    ImageView refresh;
//    @Bind(R.id.header_arrow_id)
//    ImageView arrow;

    private View header;
    private LayoutInflater layoutInflater;
    private int headerHeight;

    private IRefreshListener iRefreshListener;
    private int firstVisibleItem;        //第一个可见的项目
    private boolean isTouch; //是否触摸屏幕
    private int startY;  //设置触摸开始点的纵坐标和当前的纵坐标以及之间的差
    private int scrollState;             //滚动状态
    private int state;                  //设置刷新触摸的状态

    private static final int NORMAL = 0;     //正常状态
    private static final int PULL = 1;       //下拉可刷新
    private static final int RELEASE = 2;      //松开刷新
    private static final int REFRESHING = 3;   //正在刷新

    public RefreshListView(Context context) {
        super(context);
        initView(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        header = layoutInflater.from(context).inflate(R.layout.listview_header, null);
        measureView(header);
        headerHeight = header.getMeasuredHeight();
        Logger.d("------"+headerHeight);
        topPadding(-headerHeight);
        this.addHeaderView(header);
        this.setOnScrollListener(this);
    }

    /*
    * 通知父布局，占用的宽，高；
    * */
    private void measureView(View view) {
        ViewGroup.LayoutParams parent = view.getLayoutParams();
        if (parent == null) {
            parent = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, parent.width);
        int height;
        int tempHeight = parent.height;
        if (tempHeight > 0) {
            height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(width, height);
    }

    /*
    * 设置header布局上边距
    * */
    private void topPadding(int topPadding) {
        header.setPadding(header.getPaddingLeft(), topPadding, header.getPaddingRight(), header.getPaddingBottom());
        header.invalidate();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstVisibleItem == 0) {
                    isTouch = true;
                    startY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                onMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                if (state == RELEASE) {
                    state = REFRESHING;
                    refreshViewByState();
                    iRefreshListener.onRefresh();
                } else if (state ==PULL){
                    state = NORMAL;
                    isTouch = false;
                    refreshViewByState();
                }
                break;
        }
        return super.onTouchEvent(ev);

    }

    //判断移动过程
    private void onMove(MotionEvent ev) {
        if (!isTouch) {
            return;
        }

        int currentY = (int) ev.getY();
        int moveDistance = currentY - startY;
        int topPadding = moveDistance - headerHeight;
        switch (state) {
            case NORMAL:
                if (moveDistance > 0) {
                    state = PULL;
                    refreshViewByState();
                }
                break;
            case PULL:
                topPadding(topPadding);
                if (moveDistance > headerHeight + 30 && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    state = RELEASE;
                    refreshViewByState();
                }
                break;
            case RELEASE:
                topPadding(topPadding);
                if (moveDistance < headerHeight + 30) {
                    state = PULL;
                    refreshViewByState();
                } else if (moveDistance <= 0) {
                    state = NORMAL;
                    isTouch = false;
                    refreshViewByState();
                }
                break;
        }
    }

    //根据状态改变view
    private void refreshViewByState() {

        TextView tip = (TextView) header.findViewById(R.id.header_tip_id);
        ImageView arrow = (ImageView) header.findViewById(R.id.header_arrow_id);
        ProgressBar progress = (ProgressBar) header.findViewById(R.id.header_progressbar_id);
        RotateAnimation anim1 = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim1.setDuration(500);
        anim1.setFillAfter(true);
        RotateAnimation anim2 = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim2.setDuration(500);
        anim2.setFillAfter(true);
        switch (state) {
            case NORMAL:
                arrow.clearAnimation();
                topPadding(-headerHeight);
                break;
            case PULL:
                arrow.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                tip.setText("下拉刷新");
                arrow.clearAnimation();
                arrow.setAnimation(anim2);
                break;
            case RELEASE:
                arrow.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                tip.setText("释放立即刷新");
                arrow.clearAnimation();
                arrow.setAnimation(anim1);
                break;
            case REFRESHING:
                arrow.clearAnimation();
                topPadding(20);
                arrow.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                tip.setText("正在刷新...");
                break;
        }
    }

    /**
     * 获取完数据；
     */
    public void refreshComplete() {
        state = NORMAL;
        isTouch = false;
        refreshViewByState();
        TextView lastUpdatetime = (TextView) header
                .findViewById(R.id.header_lastrefresh_time_id);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = format.format(date);
        lastUpdatetime.setText(time);
    }

    public void setInterface(IRefreshListener iRefreshListener){
        this.iRefreshListener = iRefreshListener;
    }
    /**
     * 刷新数据接口
     * @author Administrator
     */
    public interface IRefreshListener{
         void onRefresh();
    }
}
