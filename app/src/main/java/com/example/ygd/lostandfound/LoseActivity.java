package com.example.ygd.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.ygd.lostandfound.application.MyApplication;
import com.example.ygd.lostandfound.dao.Informations;
import com.example.ygd.lostandfound.dao.InformationsDao;
import com.example.ygd.lostandfound.dao.Students;
import com.example.ygd.lostandfound.util.SingleVolleyRequestQueue;
import com.example.ygd.lostandfound.util.StringPostRequest;
import com.example.ygd.lostandfound.util.UrlManager;
import com.google.gson.Gson;

public class LoseActivity extends AppCompatActivity {
    Button pub;
    EditText et1,et2,et3,et4;
    private Students user;
    private InformationsDao informationsDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lose);
        MyApplication app= (MyApplication) this.getApplication();
        user=app.getUser();
        pub = (Button) findViewById(R.id.pub);
        et1= (EditText) findViewById(R.id.et1);
        informationsDao= MyApplication.getInstance().getDaoSession(this).getInformationsDao();
        pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 把发布的信息放入数据库
                Informations informations=new Informations();
                informations.setINum(et1.getText().toString());
                informations.setType(1);
                informations.setIIsLost(true);
                informations.setS_id(user.get_id());
                informationsDao.insertWithoutSettingPk(informations);
                String json=new Gson().toJson(informations);
                String url= UrlManager.SERVLET_URL+"LoseInformationsServlet";
                StringPostRequest spr=new StringPostRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(Boolean.parseBoolean(s)){
                            Intent intent = new Intent(getBaseContext(),MyMessageActivity.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(getBaseContext(),PubSuccActivity.class);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(LoseActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();
                    }
                });
                spr.putValue("json",json);
                SingleVolleyRequestQueue.getInstance(getApplication()).addToRequestQueue(spr);
            }
        });

    }
}
