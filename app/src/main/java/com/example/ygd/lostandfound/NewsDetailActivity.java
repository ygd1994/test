package com.example.ygd.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ygd.lostandfound.application.MyApplication;
import com.example.ygd.lostandfound.dao.News;
import com.example.ygd.lostandfound.dao.NewsDao;
import com.example.ygd.lostandfound.util.SingleVolleyRequestQueue;
import com.example.ygd.lostandfound.util.UrlManager;

public class NewsDetailActivity extends AppCompatActivity {
    private WebView wv;
    private ImageButton ib;
    private TextView tv,tv1;
    private String s_id;
    private News news;
    private NewsDao newsDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        wv= (WebView) findViewById(R.id.wv);
        ib= (ImageButton) findViewById(R.id.ib);
        tv= (TextView) findViewById(R.id.tv);
        tv1= (TextView) findViewById(R.id.tv1);
        newsDao= MyApplication.getInstance().getDaoSession(this).getNewsDao();
        MyApplication app= (MyApplication) getApplication();
        s_id=app.getUser().get_id();
        final Intent intent=getIntent();
        news=intent.getParcelableExtra("news");
        tv.setText("人气："+news.getNAgree());
        isRequest();
        String url=news.getNUrl();
        WebSettings settings=wv.getSettings();  //获取WebSetting对象
        settings.setJavaScriptEnabled(true);    //设置是否支持JavaScript
        settings.setSupportZoom(true);  //设置是否支持缩放
        settings.setDisplayZoomControls(true);  //设置内置缩放控制
        wv.setWebViewClient(new WebViewClient());   //使用WebViewClient作为客户端
        wv.loadUrl(url); //打开一个页面

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertRequest();
            }
        });
    }


    public void isRequest(){
        String url=UrlManager.SERVLET_URL+"UpvoteServlet?tag=0&s_id="+s_id+"&n_id="+news.getId();
        StringRequest sr=new StringRequest(url, new Response.Listener<String>() {   //响应成功监听接口
            @Override
            public void onResponse(String json) {
                if(Boolean.parseBoolean(json)){
                    ib.setImageResource(R.mipmap.aft);
                    tv1.setText("取消");
                }else{
                    ib.setImageResource(R.mipmap.afv);
                    tv1.setText("赞");
                }
            }
        }, new Response.ErrorListener() {   //响应错误监听接口
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //提示网络异常
                Toast.makeText(getBaseContext(), "网络连接异常", Toast.LENGTH_SHORT).show();
            }
        });
        SingleVolleyRequestQueue.getInstance(this).addToRequestQueue(sr);
    }


    public void insertRequest(){
        String url=UrlManager.SERVLET_URL+"UpvoteServlet?tag=1&s_id="+s_id+"&n_id="+news.getId();
        StringRequest sr=new StringRequest(url, new Response.Listener<String>() {   //响应成功监听接口
            @Override
            public void onResponse(String json) {
                int nAgree=news.getNAgree();
                if(Boolean.parseBoolean(json)){
                    ib.setImageResource(R.mipmap.aft);
                    tv1.setText("取消");
                    tv.setText("人气："+(nAgree+1));
                    news.setNAgree(nAgree+1);
                    newsDao.insertOrReplace(news);
                }else{
                    ib.setImageResource(R.mipmap.afv);
                    tv1.setText("赞");
                    tv.setText("人气："+(nAgree-1));
                    news.setNAgree(nAgree-1);
                    newsDao.insertOrReplace(news);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getBaseContext(), "网络连接异常", Toast.LENGTH_SHORT).show();
            }
        });
        SingleVolleyRequestQueue.getInstance(this).addToRequestQueue(sr);
    }

}
