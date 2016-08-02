package com.xyh.easywashcar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.poi.PoiSearch;
import com.xyh.easywashcar.R;
import com.xyh.easywashcar.base.MyAppcation;
import com.xyh.easywashcar.fragment.MarketFragment;
import com.xyh.easywashcar.model.MarketItem1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 向阳湖 on 2016/5/22.
 */
public class MarketAdapter1 extends BaseAdapter {

    private static final String TAG = "MarketAdapter";

    private MarketItem1 marketItem;
    private MarketItemViewHolder marketItemViewHolder;
    private ArrayList<MarketItem1> marketItems;
    private LayoutInflater layoutInflater;
    private PoiSearch mPoiSearch;
    private List<String> uidList;
    private Context mContext;
    public void onDateChange(ArrayList<MarketItem1> marketItems) {
        this.marketItems = marketItems;
        this.notifyDataSetChanged();
    }


    public MarketAdapter1(ArrayList<MarketItem1> marketItems, Context context, ArrayList<String> uidList, PoiSearch mPoiSearch) {
        this.marketItems = marketItems;
        layoutInflater = LayoutInflater.from(context);
        this.uidList = uidList;
        this.mPoiSearch = mPoiSearch;
        this.mContext = context;
//        Log.d(TAG, "!----MarketAdapter: 构造方法执行了吗");  执行了!
    }

    @Override
    public int getCount() {
//        Log.d(TAG, "------getCount: "+marketItems.size());
        return marketItems.size();
    }

    @Override
    public Object getItem(int position) {
        return marketItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        Log.d(TAG, "!!!!!--------getView:执行了吗 ");
        marketItemViewHolder = null;
        if (convertView == null) {
            marketItemViewHolder = new MarketItemViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_market1, parent, false);
            marketItemViewHolder.detail = (LinearLayout) convertView.findViewById(R.id.wash_car_detail_id);
            marketItemViewHolder.shop_name = (TextView) convertView.findViewById(R.id.wash_car_shop_name_id);
            marketItemViewHolder.shop_img = (ImageView) convertView.findViewById(R.id.wash_car_shop_img);
            marketItemViewHolder.shop_address = (TextView) convertView.findViewById(R.id.wash_car_shop_address_id);
            marketItemViewHolder.distance = (TextView) convertView.findViewById(R.id.wash_car_distance_id);
            marketItemViewHolder.pay = (Button) convertView.findViewById(R.id.wash_car_pay_id);
            convertView.setTag(marketItemViewHolder);
        } else {
            marketItemViewHolder = (MarketItemViewHolder) convertView.getTag();
        }
        marketItem = marketItems.get(position);
        marketItemViewHolder.shop_name.setText(marketItem.getName());
        marketItemViewHolder.shop_img.setBackgroundResource(R.mipmap.wash_car03);
        marketItemViewHolder.shop_address.setText(marketItem.getAddress());
        marketItemViewHolder.distance.setText(marketItem.getDistance()+"km");


        //店铺买单点击事件
        marketItemViewHolder.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAppcation.myToast("你已经预订了第 "+(position+1)+" 单业务, 请在45分钟内完成支付");
            }
        });

        //店铺详情点击事件
        marketItemViewHolder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarketFragment.showDetailInfo(marketItems.get(position).getUid());
            }
        });


//        Log.d(TAG, "!-----getView:convertView "+convertView);
        return convertView;
    }


    public class MarketItemViewHolder {
        private LinearLayout detail;          //汽车店铺详情
        private TextView shop_name;           //店铺名字
        private ImageView shop_img;           //店铺图片
        private TextView shop_address;        //店铺地址
        private TextView  distance;           //店铺距离目前的距离
        private Button pay;                   //买单按钮
    }


}

