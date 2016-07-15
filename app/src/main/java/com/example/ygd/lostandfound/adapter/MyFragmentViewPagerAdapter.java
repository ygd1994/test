package com.example.ygd.lostandfound.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by ygd on 2016/4/18.
 */
public class MyFragmentViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> myList;
    private List<String> myTitle;
    public MyFragmentViewPagerAdapter(FragmentManager fm, List<Fragment> list, List<String> title) {
        super(fm);
        this.myList=list;
        this.myTitle=title;
    }

    @Override
    public Fragment getItem(int position) {
        return myList.get(position);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return myTitle.get(position);
    }
}
