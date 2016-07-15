package com.example.ygd.lostandfound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyCSDNActivity extends AppCompatActivity {
    private WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_csdn);
        wv= (WebView) findViewById(R.id.wv);
        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        WebSettings settings=wv.getSettings();  //获取WebSetting对象
        settings.setJavaScriptEnabled(true);    //设置是否支持JavaScript
        settings.setSupportZoom(true);  //设置是否支持缩放
        settings.setDisplayZoomControls(true);  //设置内置缩放控制
        wv.setWebViewClient(new WebViewClient());   //使用WebViewClient作为客户端
        wv.loadUrl(url); //打开一个页面
    }
}
