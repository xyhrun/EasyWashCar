package com.xyh.easywashcar.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.xyh.easywashcar.R;
import com.xyh.easywashcar.activity.NewsContentActivity;
import com.xyh.easywashcar.adapter.MarketAdapter1;
import com.xyh.easywashcar.base.MyAppcation;
import com.xyh.easywashcar.base.RefreshListView;
import com.xyh.easywashcar.model.MarketItem;
import com.xyh.easywashcar.model.MarketItem1;
import com.xyh.easywashcar.overlayutil.PoiOverlay;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 向阳湖 on 2016/5/20.
 */
public class MarketFragment extends Fragment implements RefreshListView.IRefreshListener, View.OnClickListener {
    @Bind(R.id.market_listView_id)
    RefreshListView market_listView;
    @Bind(R.id.no_netWork_tip_id1)
    RelativeLayout tip1;
    @Bind(R.id.location_bdMapView_id)
    MapView mMapView;
    @Bind(R.id.market_btn_searchSome_id)
    Button btn_search;
    @Bind(R.id.market_togBtn_changMode_id)
    ToggleButton togBtn_changMode;

    private TextView dialog_detailInfo;
//    @Bind(R.id.market_swipeRefreshLayout_id)
//    SwipeRefreshLayout marketSwipeRefresh;
//    @Bind(R.id.wash_car_pay_id)
//    Button pay;

    //    private boolean isRefresh = false;
    private static final String TAG = "MarketFragment";
    private ArrayList<MarketItem> marketItems;
//    private MarketAdapter marketAdapter;
    private Context context;

    //百度地图
    private BaiduMap mBaiduMap;
    private static PoiSearch mPoiSearch;
    private MyLocationListener myListener = new MyLocationListener();
    private LocationClient mLocationClient;
    private String currentCity;
    private LatLng mCurrentLatLng;

    private ArrayList<String> uidList = new ArrayList<>();
    private View dialog;
    private ArrayList<MarketItem1> marketItem1List = new ArrayList<>();
    public MarketFragment(Context context) {
        this.context = context;
    }
    private AlertDialog mAlertDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "------MarketFragment onCreateView: ");
        SDKInitializer.initialize(MyAppcation.getContext());
        View view = inflater.inflate(R.layout.fragment_market, container, false);
        ButterKnife.bind(this, view);
        mBaiduMap = mMapView.getMap();
        mLocationClient = new LocationClient(context);
        mPoiSearch = PoiSearch.newInstance();

        if (isNetworkConnected(context)) {
            tip1.setVisibility(View.GONE);
            market_listView.setVisibility(View.VISIBLE);
        }else{
            tip1.setVisibility(View.VISIBLE);
            market_listView.setVisibility(View.GONE);
        }
        initData();

//        showList();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        marketSwipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
//        marketSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        marketSwipeRefresh.setRefreshing(false);
//                    }
//                },2000);
//            }
//        });

    }

    //重新返回该碎片时候数据不变,下拉刷新添加的不会出现
    @Override
    public void onStart() {
        super.onStart();
        initLocation();
        setClick();
        //设置定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient.registerLocationListener(myListener);

        //才开始是false,极短时间后变为true
//        MyAppcation.MyToast(mLocationClient.isStarted() + "");

        //设置定位的点, mode - 定位图层显示方式, 默认为 LocationMode.NORMAL 普通态
//        enableDirection - 是否允许显示方向信息
//        customMarker - 设置用户自定义定位图标，可以为 null
//        罗盘态，显示定位方向圈，保持定位图标在地图中心 COMPASS
//        跟随态，保持定位图标在地图中心 FOLLOWING
//        普通态： 更新定位数据时不对地图做任何操作  NORMAL
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS
                , true, BitmapDescriptorFactory.fromResource(R.mipmap.situate)));
        mLocationClient.start();
        Log.i(TAG, "----onStart: HomePageFragment.getCurrentCity() ="+HomePageFragment.getCurrentCity());
    }

    private void setClick() {
        btn_search.setOnClickListener(this);
        togBtn_changMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    market_listView.setVisibility(View.GONE);
                    mMapView.setVisibility(View.VISIBLE);
                    togBtn_changMode.setTextColor(getResources().getColor(R.color.theme_red));
                } else {
                    mMapView.setVisibility(View.GONE);
                    market_listView.setVisibility(View.VISIBLE);
                    togBtn_changMode.setTextColor(getResources().getColor(R.color.color_black));
                }
            }
        });

        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                PoiOverlay myPoiOverlay = new MyPoiOverlay(mBaiduMap);

                //设置poi点击事件
                mBaiduMap.setOnMarkerClickListener(myPoiOverlay);
                //配置地图状态
                myPoiOverlay.setData(poiResult);
                myPoiOverlay.addToMap();
                myPoiOverlay.zoomToSpan();
                List<PoiInfo> poiInfoList = poiResult.getAllPoi();
                for (int i = 0; i < poiInfoList.size(); i++) {
                    MarketItem1 marketItem1 = new MarketItem1();
//                    Log.i(TAG, "onGetPoiResult: poiInfoList.size = " + poiInfoList.size());
                    PoiInfo mPoiInfo = poiInfoList.get(i);
                    Log.i(TAG, "onGetPoiResult: 名称 = " + mPoiInfo.name + "地址: " + mPoiInfo.address
                            + "电话号码" + mPoiInfo.phoneNum + " 描述:" + mPoiInfo.describeContents());
//                        PoiDetailResult mPoiDetailResult = new PoiDetailResult(SearchResult.ERRORNO.NO_ERROR);
                    LatLng desLatlng = mPoiInfo.location;
                    Log.i(TAG, "onGetPoiResult: 我的坐标"+mCurrentLatLng+"  ,目标坐标 "+desLatlng);
                    double distance = DistanceUtil.getDistance(mCurrentLatLng, desLatlng);
                    double realDistance = Math.round(distance /10) / 100.0;
                    marketItem1.setName(mPoiInfo.name);
                    marketItem1.setAddress(mPoiInfo.address);
                    marketItem1.setDistance(realDistance);
                    marketItem1List.add(marketItem1);
                    uidList.add(mPoiInfo.uid);
                    Log.i(TAG, "onGetPoiResult: 百度类求距离 = "+DistanceUtil.getDistance(mCurrentLatLng, desLatlng));
//                    mPoiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUid(mPoiInfo.uid));
//                        Log.i(TAG, "----onGetPoiResult: 链接"+mPoiDetailResult.getDetailUrl());
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                dialog = View.inflate(context,R.layout.dialog_layout, null);
                dialog_detailInfo = (TextView) dialog.findViewById(R.id.dialog_detailInfo_id);
//                dialog_detailInfo.setText("名称: "+poiDetailResult.getName()+"\n地址: "+poiDetailResult.getAddress()
//                +"\n联系方式: "+poiDetailResult.getTelephone()+"\n评分: "+poiDetailResult.getOverallRating()
//                        +"\n价格: "+poiDetailResult.getPrice()
//                +"\n评论条数: "+poiDetailResult.getCommentNum());
//                Log.i(TAG, "onGetPoiDetailResult: "+dialog_detailInfo.getText().toString());
//                mAlertDialog = new AlertDialog.Builder(context).setTitle("门店详情")
//                        .setView(R.layout.dialog_layout)
//                        .setPositiveButton("网页查看", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        }).setNegativeButton("取消", null).setCancelable(true).show();
                Intent intent = new Intent(context, NewsContentActivity.class);
                intent.putExtra("washCarLink", poiDetailResult.getDetailUrl());
                startActivity(intent);
                mLocationClient.stop();
//                MyAppcation.myLongToast("名称: " + poiDetailResult.getName() + "\n地址: " + poiDetailResult.getAddress()
//                        + "\n联系方式: " + poiDetailResult.getTelephone() + "\n价格: " + poiDetailResult.getPrice());
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });
    }

    //重新返回该碎片时候数据不变,下拉刷新添加的不会出现
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initData();
//        showList(marketItems);
    }

//    private void showList(ArrayList<MarketItem> marketItems) {
////        if (marketAdapter == null) {
////            Logger.d("-----if marketAdapter == null");
//            market_listView.setInterface(this);
//            MarketAdapter1 = new MarketAdapter1(marketItems, context);
//            market_listView.setAdapter(marketAdapter);
//        }
//        else {
//            Logger.d("------else onDataChange");
//            marketAdapter.onDateChange(marketItems);
//        }
//    }

    private void initData() {
        marketItems = new ArrayList<>();
        //不添加判断的话,再次滑到market页面,数据会再增加.导致几倍的数据
        if (marketItems.size() == 0) {
            MarketItem marketItem1 = new MarketItem("车爵士汽车服务(保利心语店)", R.mipmap.car_service_shop01, "洪山区马湖村8号保利心语七期",
                    "5.0", "57条评论", "1.10km", "普通洗车-5座轿车", "￥18");
            MarketItem marketItem2 = new MarketItem("吉米靓车(江南村店)", R.mipmap.car_service_shop02, "南湖花园江南村小区2-1",
                    "4.8", "377条评论", "1.20km", "普通洗车-5座轿车", "￥18");
            MarketItem marketItem3 = new MarketItem("邓禄普轮胎(卓凯店)", R.mipmap.car_service_shop03, "洪山区南湖大道马湖村还建楼",
                    "4.2", "46条评论", "1.63km", "普通洗车-5座轿车", "￥22");
            MarketItem marketItem4 = new MarketItem("吉米靓车(宝安中海小区店)", R.mipmap.car_service_shop04, "洪山区南湖宝安中海小区c-104",
                    "5.0", "223条评论", "1.65km", "普通洗车-5座轿车", "￥25");
            MarketItem marketItem5 = new MarketItem("车酷汽车养护(保利心语店)", R.mipmap.car_service_shop05, "洪山区野芷湖西路保利心语7栋34号",
                    "5.0", "156条评论", "1.80km", "普通洗车-5座轿车", "￥20");
            MarketItem marketItem6 = new MarketItem("锦湖轮胎车视界(江南村店)", R.mipmap.car_service_shop06, "南湖村宝安江南村(简朴寨旁)",
                    "5.0", "43条评论", "2.21km", "普通洗车-5座轿车", "￥20");
            MarketItem marketItem7 = new MarketItem("米其林轮胎", R.mipmap.car_service_shop07, "武昌区南湖花园瑞安街刘胖子旁",
                    "4.0", "11条评论", "2.00km", "普通洗车-5座轿车", "￥22");
            MarketItem marketItem8 = new MarketItem("普利司通轮胎(华锦花园店)", R.mipmap.car_service_shop08, "武昌区平安路11号",
                    "4.3", "33条评论", "2.42km", "普通洗车-5座轿车", "￥20");

            marketItems.add(marketItem1);
            marketItems.add(marketItem2);
            marketItems.add(marketItem3);
            marketItems.add(marketItem4);
            marketItems.add(marketItem5);
            marketItems.add(marketItem6);
            marketItems.add(marketItem7);
            marketItems.add(marketItem8);

        }

    }

    private void setRefreshData() {
        MarketItem marketItem1 = new MarketItem("车酷汽车养护(保利心语店)", R.mipmap.car_service_shop05, "洪山区野芷湖西路保利心语7栋34号",
                "5.0", "156条评论", "1.80km", "普通洗车-5座轿车", "￥20");
        MarketItem marketItem2 = new MarketItem("邓禄普轮胎(卓凯店)", R.mipmap.car_service_shop03, "洪山区南湖大道马湖村还建楼",
                "4.2", "46条评论", "1.63km", "普通洗车-5座轿车", "￥22");
        marketItems.add(marketItem1);
        marketItems.add(marketItem2);
    }
    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //加载刷新数据
                setRefreshData();
                //显示界面
//                showList(marketItems);
                //通知listview 刷新数据完毕；
                market_listView.refreshComplete();
            }
        }, 2000);

    }


    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "------ MarketFragment onStop: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        Log.i(TAG, "------ MarketFragment onDestroy: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "------ MarketFragment onDestroyView: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onResume();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
                //每页默认显示10条数据, PgaeNum分页编号
            case R.id.market_btn_searchSome_id:
                if (currentCity != null) {
                    PoiCitySearchOption mPoiCitySearchOption = new PoiCitySearchOption().keyword("洗车").city(HomePageFragment.getCurrentCity());
                    mLocationClient.stop();
                    mPoiSearch.searchInCity(mPoiCitySearchOption);
                    MarketAdapter1 marketAdapter1 = new MarketAdapter1(marketItem1List, context, uidList, mPoiSearch);
                    market_listView.setAdapter(marketAdapter1);
                }
                break;
        }

    }

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //        获取定位类型: 参考 定位结果描述 相关的字段
//            Log.i(TAG, "onCreate: locType = " + bdLocation.getLocType()); // 161,网络定位结果，网络定位定位成功
            currentCity = bdLocation.getCity();
            mCurrentLatLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()); //维度,经度
//            Log.i(TAG, "onReceiveLocation: " + bdLocation.getAddrStr());
            MapStatus mMapStatus = new MapStatus.Builder().target(mCurrentLatLng).zoom(18).build();
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//            设置定位数据, 只有先允许定位图层后设置数据才会生效，参见 setMyLocationEnabled(boolean)
            mBaiduMap.setMyLocationData(new MyLocationData.Builder().latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build());
            mBaiduMap.setMapStatus(mapStatusUpdate);
        }
    }

    //获取距离
    public static double getDistance(double lat_a, double lng_a, double lat_b, double lng_b){
        double pk = 180 / 3.14169;
        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;
        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);
        return 6371000 * tt;
    }

    class MyPoiOverlay extends PoiOverlay {
        /**
         * 构造函数
         *
         * @param baiduMap 该 PoiOverlay 引用的 BaiduMap 对象
         */
        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int i) {
            PoiInfo mPoiInfo = this.getPoiResult().getAllPoi().get(i);
            //只能触发一次即使在for循环也不行
            mPoiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUid(mPoiInfo.uid));
            return super.onPoiClick(i);
        }
    }

    public static void showDetailInfo(String uid) {
        mPoiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUid(uid));
    }
}
