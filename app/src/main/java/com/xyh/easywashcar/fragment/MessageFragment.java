package com.xyh.easywashcar.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xyh.easywashcar.R;

/**
 * Created by 向阳湖 on 2016/5/20.
 */
public class MessageFragment extends Fragment {
    private static final String TAG = "MessageFragment";
    private Context context;
    private LayoutInflater inflater;
    public MessageFragment(Context context) {
        this.context = context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "------ MessageFragment onStop: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "------ MessageFragment onDestroy: ");
    }
}
