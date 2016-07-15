package com.example.ygd.lostandfound;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ygd.lostandfound.adapter.PubListAdapter;
import com.example.ygd.lostandfound.application.MyApplication;
import com.example.ygd.lostandfound.dao.Informations;
import com.example.ygd.lostandfound.util.SingleVolleyRequestQueue;
import com.example.ygd.lostandfound.util.UrlManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MyPubActivity extends AppCompatActivity {
    private ListView lv;
    private String s_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pub);
        MyApplication app= (MyApplication) getApplication();
        s_id=app.getUser().get_id();
        lv= (ListView) findViewById(R.id.lv);
        stringRequestGet();
    }
    public void stringRequestGet(){
        String url= UrlManager.SERVLET_URL+"InformationsServlet?tag=0&s_id="+s_id;
        StringRequest sr=new StringRequest(url, new Response.Listener<String>() {   //响应成功监听接口
            @Override
            public void onResponse(String json) {
                Gson gson= new Gson();
                json=json.trim();
                List<Informations> list=gson.fromJson(json,new TypeToken<ArrayList<Informations>>(){}.getType());
                if(list!=null&list.size()>0){
                    PubListAdapter pla=new PubListAdapter(list,getBaseContext());
                    lv.setAdapter(pla);
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
}
