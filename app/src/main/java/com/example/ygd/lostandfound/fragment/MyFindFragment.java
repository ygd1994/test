package com.example.ygd.lostandfound.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ygd.lostandfound.NewsDetailActivity;
import com.example.ygd.lostandfound.R;
import com.example.ygd.lostandfound.adapter.NewsListAdapter;
import com.example.ygd.lostandfound.adapter.ViewPagerAdapter;
import com.example.ygd.lostandfound.application.MyApplication;
import com.example.ygd.lostandfound.dao.News;
import com.example.ygd.lostandfound.dao.NewsDao;
import com.example.ygd.lostandfound.entity.WeatherBean;
import com.example.ygd.lostandfound.util.ImageLoaderUtil;
import com.example.ygd.lostandfound.util.SingleVolleyRequestQueue;
import com.example.ygd.lostandfound.util.UrlManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFindFragment extends Fragment {
    private LinearLayout indicatorView;
    private List vpList;
    private List<News> lvList;
    private List<News> newsList;
    private ImageView imageView;
    private PullToRefreshListView pv;
    private NewsDao newsDao;
    private ViewPagerAdapter viewPagerAdapter;
    private NewsListAdapter newsListAdapter;
    private ViewPager viewPager;
    private ImageView wea_img;
    private TextView wea_title;
    private TextView wea_date;
    private String s_id;
    private int count=8;
    private Context context=this.getActivity();

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setVpList();
            setLvList();
            viewPagerAdapter.notifyDataSetChanged();
            newsListAdapter.notifyDataSetChanged();
            pv.onRefreshComplete();
        }
    };

    public MyFindFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MyApplication app= (MyApplication) getActivity().getApplication();
        s_id=app.getUser().get_id();
        newsDao= MyApplication.getInstance().getDaoSession(context).getNewsDao();
        newsList=newsDao.queryBuilder().orderDesc(NewsDao.Properties.NAgree).list();
        View view=inflater.inflate(R.layout.fragment_my_find, container, false);
        View v=inflater.inflate(R.layout.viewpager_layout,container,false);
        wea_img= (ImageView) v.findViewById(R.id.iv_img);
        wea_title= (TextView) v.findViewById(R.id.tv_title);
        wea_date= (TextView) v.findViewById(R.id.tv_date);
        viewPager= (ViewPager) v.findViewById(R.id.vp);
        indicatorView=(LinearLayout)v.findViewById(R.id.ll);
        vpList=new ArrayList();
        loadVpList();
        viewPagerAdapter=new ViewPagerAdapter(vpList);
        viewPager.setAdapter(viewPagerAdapter);
        for(int i=0;i<vpList.size();i++){
            imageView =new ImageView(getContext());
            if(i==0){
                imageView.setImageResource(R.mipmap.image_indicator_focus);
            }else{
                imageView.setImageResource(R.mipmap.image_indicator);
            }
            indicatorView.addView(imageView);
        }
        indicatorView.setGravity(Gravity.CENTER);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                indicatorView.removeAllViews();
                for(int i=0;i<vpList.size();i++){
                    imageView =new ImageView(getContext());
                    if(i==position){
                        imageView.setImageResource(R.mipmap.image_indicator_focus);
                    }else{
                        imageView.setImageResource(R.mipmap.image_indicator);
                    }
                    indicatorView.addView(imageView);
                }
                indicatorView.setGravity(Gravity.CENTER);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        pv= (PullToRefreshListView) view.findViewById(R.id.pv);
        lvList=new ArrayList();
        loadLvList();
        newsListAdapter=new NewsListAdapter(lvList,this.getContext());
        pv.setAdapter(newsListAdapter);
        pv.setMode(PullToRefreshBase.Mode.BOTH);
        pv.getLoadingLayoutProxy(false,true).setPullLabel("上拉刷新");
        pv.getLoadingLayoutProxy(false,true).setReleaseLabel("放开刷新");
        pv.getLoadingLayoutProxy(false,true).setRefreshingLabel("加载数据");
        pv.getRefreshableView().addHeaderView(v);
        pv.getLoadingLayoutProxy(true,false).setPullLabel("下拉刷新");
        pv.getLoadingLayoutProxy(true,false).setReleaseLabel("放开刷新");
        pv.getLoadingLayoutProxy(true,false).setRefreshingLabel("加载数据");


        pv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                stringRequestGet();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new MyJob().execute();
            }
        });

        pv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(),"position:"+position, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(), "id:"+id, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("news", lvList.get(position-2));
                startActivity(intent);
            }
        });


        return view;
    }




    public void loadVpList(){
        String url_weather="https://api.heweather.com/x3/weather?cityid=CN101120501&key=4203346ef5854d52bb9396996da0f218";
        weatherRequestGet(url_weather);

        if(newsList==null||newsList.size()==0){
            stringRequestGet();
        }else{
            setVpList();
        }


    }

    public void setVpList(){
        if(vpList!=null&&vpList.size()>0){
            vpList.clear();
        }

        for (int i=0;i<4;i++){
            View v= LayoutInflater.from(this.getContext()).inflate(R.layout.news_vplist_item,null);
            ImageView ivImg= (ImageView) v.findViewById(R.id.iv_img);
            TextView tvTitle= (TextView) v.findViewById(R.id.tv_title);
            final News news= newsList.get(i);
            tvTitle.setText(news.getNTitle());
            String url=UrlManager.BASE_URL+news.getNImg();
            ImageLoaderUtil.display(url,ivImg);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), NewsDetailActivity.class);
                    intent.putExtra("news",news);
                    startActivity(intent);
                }
            });


            vpList.add(v);
        }
    }

    public void stringRequestGet(){
        String url=UrlManager.SERVLET_URL+"NewsServlet";
        StringRequest sr=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                Gson gson= new Gson();
                json=json.trim();
                Log.d("===json===",json);
                List<News> list=gson.fromJson(json,new TypeToken<ArrayList<News>>(){}.getType());
                for (int i=0;i<list.size();i++){
                    News news=list.get(i);
                    newsDao.insertOrReplace(news);
                }
                List temp=newsDao.queryBuilder().orderDesc(NewsDao.Properties.NAgree).list();
                newsList.clear();
                newsList.addAll(temp);
                handler.sendEmptyMessage(0);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "网络连接异常", Toast.LENGTH_SHORT).show();
            }
        });
        SingleVolleyRequestQueue.getInstance(context).addToRequestQueue(sr);
    }

    public void weatherRequestGet(String url){
        StringRequest sr=new StringRequest(url, new Response.Listener<String>() {   //响应成功监听接口
            @Override
            public void onResponse(String json) {
//                Log.d("===json===",json);
                json=json.replaceAll(" ","");
                json=json.replaceFirst("3.0","");
//                Log.d("===json1===",json);
                Gson gson=new Gson();
                try {
                    JSONObject jsonObject=new JSONObject(json);
                    String jsonStr=jsonObject.getString("HeWeatherdataservice");
//                    Log.d("===jsonStr===",jsonStr);
                    JSONArray jsonArray=new JSONArray(jsonStr);
                    JSONObject jo= (JSONObject) (jsonArray.get(0));
                    WeatherBean weatherBean=gson.fromJson(jo.toString(),WeatherBean.class);
//                    Log.d("===now-tmp===",weatherBean.getNow().getTmp());
//                    Log.d("===now-cond===",weatherBean.getNow().getCond().getTxt());
                    wea_title.setText("今日天气："+weatherBean.getNow().getCond().getTxt()+"，"+weatherBean.getNow().getTmp()+"℃，"
                            +weatherBean.getNow().getWind().getDir()+weatherBean.getNow().getWind().getSc()+"级");
                    wea_date.setText("穿衣指数："+weatherBean.getSuggestion().getDrsg().getBrf()+"，运动指数："+weatherBean.getSuggestion().getSport().getBrf());
                    String imgUrl="http://files.heweather.com/cond_icon/"+weatherBean.getNow().getCond().getCode()+".png";
                    ImageLoaderUtil.display(imgUrl,wea_img);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {   //响应错误监听接口
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //提示网络异常
                Toast.makeText(getContext(), "网络连接异常", Toast.LENGTH_SHORT).show();
            }
        });
        SingleVolleyRequestQueue.getInstance(context).addToRequestQueue(sr);
    }

    public void loadLvList(){
        if(newsList==null||newsList.size()==0){
            stringRequestGet();
        }else{
            setLvList();
        }
    }

    public void setLvList(){
        if(lvList!=null){
            lvList.clear();
        }
        if(newsList.size()>=8){
            for(int i=4;i<8;i++){
                lvList.add(newsList.get(i));
            }
        }

    }


    public class MyJob extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            count+=4;
            if(newsList.size()>=count){
                lvList.clear();
                for(int i=4;i<count;i++){
                    lvList.add(newsList.get(i));
                }
            }else{
                lvList.clear();
                for(int i=4;i<newsList.size();i++){
                    lvList.add(newsList.get(i));
                }
                Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
            }
            newsListAdapter.notifyDataSetChanged();
            pv.onRefreshComplete();
        }
    }

}
