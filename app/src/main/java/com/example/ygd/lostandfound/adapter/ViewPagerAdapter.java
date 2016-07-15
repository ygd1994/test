package com.example.ygd.lostandfound.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ygd on 2016/4/7.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<View> myData;
    public ViewPagerAdapter(List<View> myData) {
        this.myData = myData;
    }

    @Override
    public int getCount() {
        return myData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(myData.get(position));
        return myData.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(myData.get(position));
    }


}