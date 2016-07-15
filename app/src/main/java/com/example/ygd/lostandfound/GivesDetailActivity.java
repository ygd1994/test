package com.example.ygd.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ygd.lostandfound.dao.Gives;
import com.example.ygd.lostandfound.util.ImageLoaderUtil;

public class GivesDetailActivity extends AppCompatActivity {
    private ImageView iv;
    private TextView tv;
    private WebView wv;
    private Gives gives;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gives_detail);
        iv= (ImageView) findViewById(R.id.iv);
        tv= (TextView) findViewById(R.id.tv);
        wv= (WebView) findViewById(R.id.wv);
        Intent intent=getIntent();
        gives=intent.getParcelableExtra("gives");
        String imgUrl=gives.getGImgUrl();
        ImageLoaderUtil.display(imgUrl,iv);
        tv.setText(gives.getGDescription());

        WebSettings settings=wv.getSettings();
        settings.setJavaScriptEnabled(true);
        wv.setWebChromeClient(new WebChromeClient());
        wv.setWebViewClient(new WebViewClient());
        String url="http://m.amap.com/?q="+gives.getGLatitude()+","+gives.getGLongitude()+"&name="+gives.getGLocation()+"&view=detail"+"&key=5c8577b8ad3565cc7ea726272888efca";
        wv.loadUrl(url);
    }
}
