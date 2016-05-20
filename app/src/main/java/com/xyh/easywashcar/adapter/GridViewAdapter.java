package com.xyh.easywashcar.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xyh.easywashcar.R;
import com.xyh.easywashcar.model.GridItem;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {
        private static final String TAG = "GridViewAdapter";
        private List<GridItem> gridItems;
        private GridViewHolder gridViewHolder;
        private LayoutInflater inflater;

        public GridViewAdapter(List<GridItem> gridItems, Context context) {
            this.gridItems = gridItems;
            Log.d(TAG, "------构造方法GridViewAdapter: "+context);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return gridItems.size();
        }

        @Override
        public Object getItem(int position) {
            return gridItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            gridViewHolder = null;
            if (convertView == null) {
                gridViewHolder = new GridViewHolder();
    //            Log.d(TAG, "------getView: "+context);
                convertView = inflater.inflate(R.layout.item_grid, parent, false);
                gridViewHolder.img = (ImageView) convertView.findViewById(R.id.item_grid_img_id);
                gridViewHolder.title = (TextView) convertView.findViewById(R.id.item_grid_title_id);
                convertView.setTag(gridViewHolder);
            } else {
                gridViewHolder = (GridViewHolder) convertView.getTag();
            }
            GridItem gridItem = gridItems.get(position);
//            Log.d(TAG, "----getView: title"+gridItem.getTitle());
            gridViewHolder.title.setText(gridItem.getTitle());
            gridViewHolder.img.setImageResource(gridItem.getImgID());
            Log.d(TAG, "------getView: convertView"+convertView);
            return convertView;
        }

        class GridViewHolder {
            public ImageView img;
            public TextView title;
        }

    }
