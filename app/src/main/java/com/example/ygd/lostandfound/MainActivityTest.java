package com.example.ygd.lostandfound;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.ygd.lostandfound.adapter.MyFragmentViewPagerAdapter;
import com.example.ygd.lostandfound.fragment.MyElseFragment;
import com.example.ygd.lostandfound.fragment.MyFindFragment;
import com.example.ygd.lostandfound.fragment.MyGiveFragment;
import com.example.ygd.lostandfound.fragment.MyPubFragment;
import com.example.ygd.lostandfound.util.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;

public class MainActivityTest extends AppCompatActivity {


    private ViewPager vp;
    private FragmentTabHost fragmentTabHost;
    private List<Fragment> list;
    private MyFragmentViewPagerAdapter mvp;
    private String[] str={"发现","发布·卡","发布·其他","我的"};
    private int[] imgRes={R.drawable.tab_home,R.drawable.tab_message,R.drawable.tab_selfinfo,R.drawable.tab_more};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentTabHost=(FragmentTabHost) findViewById(R.id.ft);
        vp=(ViewPager)findViewById(R.id.vp);
        init();
    }
    public void init(){
        list=new ArrayList();
        fragmentTabHost.setup(this,getSupportFragmentManager());
        for(int i=0;i<str.length;i++){
            TabHost.TabSpec tabSpec=fragmentTabHost.newTabSpec(str[i]).setIndicator(getIndicatorView(i));
            Fragment fragment;
            switch (i){
                case 0:
                    fragment=new MyFindFragment();
                    break;
                case 1:
                    fragment=new MyPubFragment();
                    break;
                case 2:
                    fragment=new MyGiveFragment();
                    break;
                case 3:
                    fragment=new MyElseFragment();
                    break;
                default:
                    fragment=new MyFindFragment();
            }
            fragmentTabHost.addTab(tabSpec,fragment.getClass(),null);
            list.add(fragment);
        }
        mvp=new MyFragmentViewPagerAdapter(getSupportFragmentManager(),list,null);
        vp.setAdapter(mvp);
        vp.setPageTransformer(true,new ZoomOutPageTransformer());
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                //设置ViewPager选中
                vp.setCurrentItem(fragmentTabHost.getCurrentTab());
            }
        });
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                fragmentTabHost.setCurrentTab(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    private View getIndicatorView(int i){
        View view=getLayoutInflater().inflate(R.layout.tab_host_layout,null);
        ImageView imageView=(ImageView)view.findViewById(R.id.iv);
        TextView textView=(TextView)view.findViewById(R.id.tv);
        imageView.setImageResource(imgRes[i]);
        textView.setText(str[i]);
        return view;
    }
}
