package com.example.ygd.lostandfound;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.ygd.lostandfound.application.MyApplication;
import com.example.ygd.lostandfound.dao.News;
import com.example.ygd.lostandfound.dao.NewsDao;

import java.util.List;

public class MyAgreeActivity extends AppCompatActivity {
    private ListView lv;
    private List<News> list;
    private NewsDao newsDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_agree);
        newsDao= MyApplication.getInstance().getDaoSession(this).getNewsDao();
        lv= (ListView) findViewById(R.id.lv);

    }
}
