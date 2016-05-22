package com.xyh.easywashcar.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xyh.easywashcar.R;
import com.xyh.easywashcar.adapter.MarketAdapter;
import com.xyh.easywashcar.model.MarketItem;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 向阳湖 on 2016/5/20.
 */
public class MarketFragment extends Fragment {
    @Bind(R.id.market_listView_id)
    ListView market_listView;
//    @Bind(R.id.wash_car_pay_id)
//    Button pay;

    private static final String TAG = "MarketFragment";
    private ArrayList<MarketItem> marketItems = new ArrayList<>();
    private MarketAdapter marketAdapter;
    private Context context;

    public MarketFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);
        ButterKnife.bind(this, view);
        initData();
        marketAdapter = new MarketAdapter(marketItems, context);
        market_listView.setAdapter(marketAdapter);
        Log.d(TAG, "!!-----onCreateView: market_listView = " + market_listView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //不工作
//        market_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(context, "123", Toast.LENGTH_SHORT).show();
//            }
//        });
//        pay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "456", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initData() {
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

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        marketItems.clear();
//    }
}
