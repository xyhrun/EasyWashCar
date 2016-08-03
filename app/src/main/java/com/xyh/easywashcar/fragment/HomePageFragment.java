package com.xyh.easywashcar.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.xyh.easywashcar.R;
import com.xyh.easywashcar.activity.ErWeiMaActivity;
import com.xyh.easywashcar.activity.Gridview_1;
import com.xyh.easywashcar.adapter.GridViewAdapter;
import com.xyh.easywashcar.adapter.ViewPagerAdapter;
import com.xyh.easywashcar.model.GridItem;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 向阳湖 on 2016/5/20.
 */
public class HomePageFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "HomePageFragment";
//    private static final int RESULT_OK = 0;
    private Context context;
    private int i = 0;
    public HomePageFragment(Context context) {
        this.context = context;
        Log.d(TAG, "-------HomePageFragment: "+context);
    }

    @Bind(R.id.gridview_id)
    GridView gridView;
    @Bind(R.id.point_id)
    LinearLayout linearPonit;
    @Bind(R.id.view_pager_inner_id)
    ViewPager viewpager_inner;
    @Bind(R.id.main_erweima_id)
    Button erweima;

    @Bind(R.id.main_bdMapView_id)
    MapView mMapView;
    @Bind(R.id.city_name_id)
    TextView city_name;


    //百度地图
    private BaiduMap mBaiduMap;
    private PoiSearch mPoiSearch;
    private MyLocationListener myListener = new MyLocationListener();
    private LocationClient mLocationClient;
    public static String currentCity;


    //设置网格布局信息
    private ArrayList<GridItem> gridItems = new ArrayList<>();
    private GridViewAdapter gridViewAdapter;

    //设置ViewPager信息
    private ArrayList<ImageView> imageviews;
    private ViewPagerAdapter viewPagerAdapter;
    private int[] imgs = {R.mipmap.wash_car01,R.mipmap.wash_car02,R.mipmap.wash_car03,R.mipmap.wash_car04};
    private int pointIndex;
    private boolean isPlaying = true;
    private ViewPagerListener viewPagerListener;
    private ViewPagerTask viewPagerTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "------HomePageFragment onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        ButterKnife.bind(this, view);
        mBaiduMap = mMapView.getMap();
        mLocationClient = new LocationClient(context);
        mPoiSearch = PoiSearch.newInstance();
        initGridViewData();
        initViewPagerData();
        initAction();
        gridViewAdapter = new GridViewAdapter(gridItems, context);
        gridView.setAdapter(gridViewAdapter);
        setGridViewOnClickListener();
        erweima.setOnClickListener(this);
        initBaiduMap();
        return view;
    }

    private void initBaiduMap() {
        initLocation();
        //设置定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient.registerLocationListener(myListener);
        Log.i(TAG, "------ MarketFragment onStart: ");

        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL
                , true, BitmapDescriptorFactory.fromResource(R.mipmap.situate)));
        mLocationClient.start();
        //每页默认显示10条数据, PgaeNum分页编号
//        Log.i(TAG, "onClick: city = "+currentCity);
        if (currentCity != null) {
            mLocationClient.stop();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "-----HomePageFragment onActivityCreated: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPagerTask = new ViewPagerTask();
        viewPagerTask.execute();
        Log.i(TAG, "-----HomePageFragment onCreate: ");

    }

    private void setGridViewOnClickListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getContext(), Gridview_1.class));
            }
        });
    }

    private void initGridViewData() {
        //不添加判断的话,切换到第三个fragment时候,数据会再增加一倍.
        if (gridItems.size() == 0) {
            Log.d(TAG, "!!-----initGridViewData: 执行了"+ i);
            gridItems.add(new GridItem("上门洗车", R.mipmap.car_1));
            gridItems.add(new GridItem("紧急救援", R.mipmap.car_repair));
            gridItems.add(new GridItem("汽车保养", R.mipmap.protection_1));
            gridItems.add(new GridItem("违章查询", R.mipmap.query));
            gridItems.add(new GridItem("司机交流", R.mipmap.driver));
            gridItems.add(new GridItem("车险推荐", R.mipmap.secuity));
        }

    }

    private void initViewPagerData() {
        imageviews = new ArrayList<>();
        View view;
        ViewGroup.LayoutParams params;
        for (int i = 0; i < imgs.length; i++) {
            //设置图片
            ImageView img = new ImageView(context);
            img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            img.setBackgroundResource(imgs[i]);
            imageviews.add(img);

            //设置小圆点
            view = new View(context);
            params = new ViewGroup.LayoutParams(30, 30);
            view.setBackgroundResource(R.drawable.selector_drawable_point);
            view.setLayoutParams(params);
            view.setEnabled(false);
            //添加圆点
            linearPonit.addView(view);
        }
        viewPagerAdapter = new ViewPagerAdapter(imageviews, context);
        viewpager_inner.setAdapter(viewPagerAdapter);
    }

    private void initAction() {
        viewPagerListener = new ViewPagerListener();
        viewpager_inner.setOnPageChangeListener(viewPagerListener);
        //取中间数来作为起始位置
        int index = (Integer.MAX_VALUE / 2) - (Integer.MAX_VALUE / 2 % imageviews.size());
        //用来出发监听器
        viewpager_inner.setCurrentItem(index);
        linearPonit.getChildAt(pointIndex).setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(context, CaptureActivity.class), 0);
    }

    @Override
    public void onStart() {
        super.onStart();
        city_name.setText(currentCity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            Bundle bundle = data.getExtras();
            String result = bundle.getString("result");
            Intent erWeiMaIntent = new Intent(context, ErWeiMaActivity.class);
            erWeiMaIntent.putExtra("result", result);
            startActivity(erWeiMaIntent);
            Log.i(TAG, "---onActivityResult: "+result);
        }
    }

    class ViewPagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            int currentPosition = position % imgs.length;
            linearPonit.getChildAt(currentPosition).setEnabled(true);
            linearPonit.getChildAt(pointIndex).setEnabled(false);
            pointIndex = currentPosition;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class ViewPagerTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
                while (isPlaying) {
                    //两秒切换一次图片
                    SystemClock.sleep(3000);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewpager_inner.setCurrentItem(viewpager_inner.getCurrentItem()+1);
                        }
                    });
                }
            return null;
        }
    }

    //销毁活动时候异步任务取消
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "------HomePageFragment onDestroy: ");
        viewPagerTask.cancel(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "------ HomePageFragment onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "------ HomePageFragment onDestroyView: ");
    }

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //        获取定位类型: 参考 定位结果描述 相关的字段
//            Log.i(TAG, "onCreate: locType = " + bdLocation.getLocType()); // 161,网络定位结果，网络定位定位成功
            currentCity = bdLocation.getCity();
            city_name.setText(currentCity);
        }
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");
//        设置是否打开gps进行定位
        option.setOpenGps(true);
//        //可选，设置是否需要地址信息，默认不需要.有了它才可以才可以获取到城市和省信息
        option.setIsNeedAddress(true);
//        设置扫描间隔，单位是毫秒 当<1000(1s)时，定时定位无效
        option.setScanSpan(2000);
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setLocationNotify(true);
//        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
//        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
//        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        option.setIgnoreKillProcess(false);
//        //可选，默认false，设置是否收集CRASH信息，默认收集
//        option.SetIgnoreCacheException(false);
//        //可选，默认false，设置是否需要过滤gps仿真结果，默认需要
//        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
    }

    public static String getCurrentCity() {
        if (currentCity != null) {
            return currentCity;
        }
        return null;
    }
}
