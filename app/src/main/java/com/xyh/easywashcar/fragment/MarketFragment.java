package com.xyh.easywashcar.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.xyh.easywashcar.R;
import com.xyh.easywashcar.activity.NewsContentActivity;
import com.xyh.easywashcar.adapter.MarketAdapter1;
import com.xyh.easywashcar.base.MyAppcation;
import com.xyh.easywashcar.base.RefreshListView;
import com.xyh.easywashcar.model.MarketItem1;
import com.xyh.easywashcar.overlayutil.PoiOverlay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 向阳湖 on 2016/5/20.
 */
public class MarketFragment extends android.support.v4.app.Fragment implements RefreshListView.IRefreshListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.market_listView_id)
    ListView market_listView;
    @Bind(R.id.no_netWork_tip_id1)
    RelativeLayout tip1;
    @Bind(R.id.location_bdMapView_id)
    MapView mMapView;
    @Bind(R.id.market_btn_searchSome_id)
    Button btn_search;
    @Bind(R.id.market_togBtn_changMode_id)
    ToggleButton togBtn_changMode;
    @Bind(R.id.market_distance_id)
    Spinner market_distance;
    @Bind(R.id.market_swipeRefreshLayout_id)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private TextView dialog_detailInfo;
    private MarketAdapter1 marketAdapter1 = null;
    private int selectDistance;
    private int preSelectDistance = 6;

    //  private boolean isRefresh = false;
    private static final String TAG = "MarketFragment";

    //门店的数量
    private int itemSize;
    private Context context;

    //百度地图
    private BaiduMap mBaiduMap;
    private static PoiSearch mPoiSearch;
    private MyLocationListener myListener = new MyLocationListener();
    private LocationClient mLocationClient;
    private LatLng mCurrentLatLng;
    private String currentCity;

    //是否来自刷新,是的话就在后面添加数据
//    private boolean isRefresh = false;

    private ArrayList<String> uidList = new ArrayList<>();
    private View dialog;
    private ArrayList<MarketItem1> marketItem1List = new ArrayList<>();
    private int pageNum;

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
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_green_blue);
        mBaiduMap = mMapView.getMap();
        mLocationClient = new LocationClient(context);
        mPoiSearch = PoiSearch.newInstance();

        if (isNetworkConnected(context)) {
            tip1.setVisibility(View.GONE);
            market_listView.setVisibility(View.VISIBLE);
        } else {
            tip1.setVisibility(View.VISIBLE);
            market_listView.setVisibility(View.GONE);
        }
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                , true, null));
        mLocationClient.start();

        //延迟1.5秒,否则出现定位还没成功,则开始搜索.造成 java.lang.IllegalArgumentException: option or location or keyworld can not be null
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefreshData();
                    Log.i(TAG, "run: 应用启动获得门店的个数"+marketItem1List.size());
                    Log.i(TAG, "run: 数据加载了吗");
                }
        }, 1500);
    }

    //重新返回该碎片时候数据不变,下拉刷新添加的不会出现
    @Override
    public void onStart() {
        super.onStart();
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
//                若是范围小于之前的 就清除原数据,
                if (selectDistance < preSelectDistance) {
                    marketItem1List.clear();
                }
                preSelectDistance = selectDistance;
                //设置poi点击事件
                mBaiduMap.setOnMarkerClickListener(myPoiOverlay);
                //配置地图状态
                myPoiOverlay.setData(poiResult);
                myPoiOverlay.addToMap();
                myPoiOverlay.zoomToSpan();
                List<PoiInfo> poiInfoList = poiResult.getAllPoi();
                if (poiInfoList == null) {
                    return;
                }
                Log.i(TAG, "onGetPoiResult: 搜索出来门店的数量" + poiInfoList.size());
                for (int i = 0; i < poiInfoList.size(); i++) {
                    MarketItem1 marketItem1 = new MarketItem1();
//                    Log.i(TAG, "onGetPoiResult: poiInfoList.size = " + poiInfoList.size());
                    PoiInfo mPoiInfo = poiInfoList.get(i);
                    //避免重复项
                    if (uidList.contains(mPoiInfo.uid)) {
                        continue;
                    }
                    Log.i(TAG, "onGetPoiResult: 名称 = " + mPoiInfo.name + "地址: " + mPoiInfo.address
                            + "电话号码" + mPoiInfo.phoneNum + " 描述:" + mPoiInfo.describeContents());
//                        PoiDetailResult mPoiDetailResult = new PoiDetailResult(SearchResult.ERRORNO.NO_ERROR);
                    LatLng desLatlng = mPoiInfo.location;
//                    Log.i(TAG, "onGetPoiResult: 我的坐标" + mCurrentLatLng + "  ,目标坐标 " + desLatlng);
                    double distance = DistanceUtil.getDistance(mCurrentLatLng, desLatlng);
                    double realDistance = Math.round(distance / 10) / 100.0;
                    marketItem1.setName(mPoiInfo.name);
                    marketItem1.setAddress(mPoiInfo.address);
                    marketItem1.setDistance(realDistance);
                    marketItem1.setUid(mPoiInfo.uid);
                    marketItem1List.add(marketItem1);
                    uidList.add(mPoiInfo.uid);
                }
                Log.i(TAG, "onGetPoiResult: 添加到集合门店的数量 ="+marketItem1List.size());

                //listview按照距离升序排列
                Collections.sort(marketItem1List, new Comparator<MarketItem1>() {
                    /**
                     *
                     * @param lhs
                     * @param rhs
                     * @return an integer < 0 if lhs is less than rhs, 0 if they are
                     *         equal, and > 0 if lhs is greater than rhs,比较数据大小
                     */
                    @Override
                    public int compare(MarketItem1 lhs, MarketItem1 rhs) {
                        Double rScore = lhs.getDistance();
                        Double lScore = rhs.getDistance();
                        //对于枚举类型的enum1.compareTo（enum2）是按照枚举类型值在定义时的先后顺序比较的，越后面的越大
                        return (rScore.compareTo(lScore));
                    }
                });
                if (marketAdapter1 == null) {
                    marketAdapter1 = new MarketAdapter1(marketItem1List, context, uidList, mPoiSearch);
                    market_listView.setAdapter(marketAdapter1);
                } else {
                    marketAdapter1.notifyDataSetChanged();
                }

            }

            @Override
            public void onGetPoiDetailResult(final PoiDetailResult poiDetailResult) {
                dialog = View.inflate(context, R.layout.dialog_layout, null);
                dialog_detailInfo = (TextView) dialog.findViewById(R.id.dialog_detailInfo_id);
                dialog_detailInfo.setText(
                        "名称 :  " + poiDetailResult.getName()
                                + "\n评分 :  " + poiDetailResult.getOverallRating()
                                + "\n价格 :  " + poiDetailResult.getPrice()
                                + "\n手机 :  " + poiDetailResult.getTelephone()
                                + "\n评论 :  " + poiDetailResult.getCommentNum()
                                + "\n地址 :  " + poiDetailResult.getAddress());
//                Log.i(TAG, "onGetPoiDetailResult: " + dialog_detailInfo.getText().toString());
                mAlertDialog = new AlertDialog.Builder(context).setView(dialog)
                        .setPositiveButton("网页查看", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(context, NewsContentActivity.class);
                                intent.putExtra("washCarLink", poiDetailResult.getDetailUrl());
                                startActivity(intent);
                            }
                        }).setNegativeButton("取消", null).setCancelable(true).show();
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });

        //选择城市触发这方法,条件成立不执行搜索城市
        market_distance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemSelected: posiontion" + position);
                String distance = getResources().getStringArray(R.array.distance)[position].trim();
                //利用正则表达式,在字符串中只保留数字
                String regEx = "[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(distance);
                String DistanceStr = m.replaceAll("").trim();
                //默认5km搜索
                int finalDistance = 5;
                if (DistanceStr != "") {
                    finalDistance = Integer.valueOf(DistanceStr);
                }
                selectDistance = finalDistance;

                //条件成立为什么不执行!!!!!
//                if (HomePageFragment.getCurrentCity() != "" && distance == "城市") {
//                    MyAppcation.myLongToast("城市搜索");
//                    PoiCitySearchOption mPoiCitySearchOption = new PoiCitySearchOption()
//                            .city(HomePageFragment.getCurrentCity()).keyword("洗车").pageCapacity(10);  //半径单位m
//                    mPoiSearch.searchInCity(mPoiCitySearchOption);
//                    mLocationClient.stop();
//                    if (marketAdapter1 == null) {
//                        marketAdapter1 = new MarketAdapter1(marketItem1List, context, uidList, mPoiSearch);
//                    }
//                    market_listView.setAdapter(marketAdapter1);
//                }

                if (mCurrentLatLng != null && DistanceStr != "") {
                    PoiNearbySearchOption mPoiNearbySearchOption = new PoiNearbySearchOption().location(mCurrentLatLng)
                            .radius(selectDistance * 1000 + 100).keyword("洗车").pageCapacity(10).pageNum(pageNum++);  //半径单位m
                    mPoiSearch.searchNearby(mPoiNearbySearchOption);
                    mLocationClient.stop();
                }

                Log.i(TAG, "---onItemSelected: currentCity = " + currentCity);
                Log.i(TAG, "---onItemSelected: 来自home = " + HomePageFragment.getCurrentCity());
                Log.i(TAG, "onItemSelected: 距离范围" + distance);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    private void setRefreshData() {
        Log.i(TAG, "setRefreshData: 坐标"+mCurrentLatLng);
        PoiNearbySearchOption mPoiNearbySearchOption = new PoiNearbySearchOption().location(mCurrentLatLng)
                .radius(selectDistance * 1000 + 100).keyword("洗车").pageCapacity(10).pageNum(pageNum++);  //半径单位m
        mPoiSearch.searchNearby(mPoiNearbySearchOption);
        mLocationClient.stop();
    }

    @Override
    public void onRefresh() {
        //延迟一秒,否则出现定位还没成功,则开始搜索.造成 java.lang.IllegalArgumentException: option or location or keyworld can not be null
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefreshData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
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
                if (mCurrentLatLng != null) {
                    //门店的个数
                    itemSize = marketItem1List.size();
                    Log.i(TAG, "onClick: itemsize = "+itemSize);
                    //默认6km搜索. 由于四舍五入导致19.5~19.9这种数据无法加载到范围为20km里面.故此在原搜索范围上+0.1km
                    PoiNearbySearchOption mPoiNearbySearchOption = new PoiNearbySearchOption().location(mCurrentLatLng)
                            .radius(selectDistance * 1000 + 100).keyword("洗车").pageCapacity(10).pageNum(pageNum++);  //半径单位m
                    mPoiSearch.searchNearby(mPoiNearbySearchOption);
                    Log.i(TAG, "onClick: 搜索之后的门店数量 = "+marketItem1List.size());
                    mLocationClient.stop();
                }
                break;
        }

    }

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //        获取定位类型: 参考 定位结果描述 相关的字段
//            Log.i(TAG, "onCreate: locType = " + bdLocation.getLocType()); // 161,网络定位结果，网络定位定位成功
            mCurrentLatLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()); //维度,经度
            currentCity = bdLocation.getCity();
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
//    public static double getDistance(double lat_a, double lng_a, double lat_b, double lng_b){
//        double pk = 180 / 3.14169;
//        double a1 = lat_a / pk;
//        double a2 = lng_a / pk;
//        double b1 = lat_b / pk;
//        double b2 = lng_b / pk;
//        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
//        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
//        double t3 = Math.sin(a1) * Math.sin(b1);
//        double tt = Math.acos(t1 + t2 + t3);
//        return 6371000 * tt;
//    }

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
