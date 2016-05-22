package com.xyh.easywashcar.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.xyh.easywashcar.R;
import com.xyh.easywashcar.base.MyAppcation;
import com.xyh.easywashcar.model.MarketItem;

import java.util.ArrayList;

/**
 * Created by 向阳湖 on 2016/5/22.
 */
public class MarketAdapter extends BaseAdapter {

    private static final String TAG = "MarketAdapter";

    private MarketItem marketItem;
    private MarketItemViewHolder marketItemViewHolder;
    private ArrayList<MarketItem> marketItems;
    private LayoutInflater layoutInflater;
    public MarketAdapter(ArrayList<MarketItem> marketItems, Context context) {
        this.marketItems = marketItems;
        layoutInflater = LayoutInflater.from(context);
//        Log.d(TAG, "!----MarketAdapter: 构造方法执行了吗");  执行了!
    }

    @Override
    public int getCount() {
        Log.d(TAG, "------getCount: "+marketItems.size());
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
        Log.d(TAG, "!!!!!--------getView:执行了吗 ");
        marketItemViewHolder = null;
        if (convertView == null) {
            marketItemViewHolder = new MarketItemViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_market, parent, false);
            marketItemViewHolder.detail = (LinearLayout) convertView.findViewById(R.id.wash_car_detail_id);
            marketItemViewHolder.shop_name = (TextView) convertView.findViewById(R.id.wash_car_shop_name_id);
            marketItemViewHolder.shop_img = (ImageView) convertView.findViewById(R.id.wash_car_shop_img);
            marketItemViewHolder.shop_address = (TextView) convertView.findViewById(R.id.wash_car_shop_address_id);
            marketItemViewHolder.mark = (TextView) convertView.findViewById(R.id.wash_car_mark_id);
            marketItemViewHolder.comment = (TextView) convertView.findViewById(R.id.wash_car_comment_id);
            marketItemViewHolder.distance = (TextView) convertView.findViewById(R.id.wash_car_distance_id);
            marketItemViewHolder.type = (TextView) convertView.findViewById(R.id.wash_car_type_id);
            marketItemViewHolder.price = (TextView) convertView.findViewById(R.id.wash_car_price_id);
            marketItemViewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.wash_car_ratingBar_id);
            marketItemViewHolder.pay = (Button) convertView.findViewById(R.id.wash_car_pay_id);
            convertView.setTag(marketItemViewHolder);
        } else {
            marketItemViewHolder = (MarketItemViewHolder) convertView.getTag();
        }
        marketItem = marketItems.get(position);
        marketItemViewHolder.shop_name.setText(marketItem.getShop_name());
        marketItemViewHolder.shop_img.setBackgroundResource(marketItem.getShop_img());
        marketItemViewHolder.shop_address.setText(marketItem.getShop_address());
        marketItemViewHolder.mark.setText(marketItem.getMark());
        marketItemViewHolder.comment.setText(marketItem.getComment());
        marketItemViewHolder.distance.setText(marketItem.getDistance());
        marketItemViewHolder.type.setText(marketItem.getType());
        marketItemViewHolder.price.setText(marketItem.getPrice());
        marketItemViewHolder.ratingBar.setRating((Float.valueOf(marketItem.getMark())));
        marketItemViewHolder.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAppcation.myToast("你已经预订了第 "+(position+1)+" 单业务, 请在45分钟内完成支付");
            }
        });

        marketItemViewHolder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAppcation.myToast("你点击第 "+(position+1)+" 项");
            }
        });


        Log.d(TAG, "!-----getView:convertView "+convertView);
        return convertView;
    }


    public class MarketItemViewHolder {
        private LinearLayout detail;
        private TextView shop_name;
        private ImageView shop_img;
        private TextView shop_address;
        private TextView  mark;
        private TextView  comment;
        private TextView  distance;
        private TextView  type;
        private TextView  price;
        private RatingBar ratingBar;
        private Button pay;
    }


}

