package com.example.ygd.lostandfound;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.ygd.lostandfound.application.MyApplication;
import com.example.ygd.lostandfound.dao.Students;
import com.example.ygd.lostandfound.util.SingleVolleyRequestQueue;
import com.example.ygd.lostandfound.util.StringPostRequest;
import com.example.ygd.lostandfound.util.UrlManager;
import com.google.gson.Gson;


public class MyActivity extends AppCompatActivity {
    private Button mysave;
    private Students user;
    private TextView sid;
    private EditText sname,stel,sqq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        MyApplication app= (MyApplication) this.getApplication();
        user=app.getUser();
        mysave = (Button) findViewById(R.id.mysave);
        sid= (TextView) findViewById(R.id.sid);
        sname= (EditText) findViewById(R.id.sname);
        stel= (EditText) findViewById(R.id.stel);
        sqq= (EditText) findViewById(R.id.sqq);
        sid.setText(user.get_id());
        sname.setText(user.getSName());
        stel.setText(user.getSTel());
        sqq.setText(user.getSQQ());
        mysave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在这里把填写的信息重新写入数据库
                user.setSName(sname.getText().toString());
                user.setSTel(stel.getText().toString());
                user.setSQQ(sqq.getText().toString());
                String json=new Gson().toJson(user);
                stringRequestPost(json);

            }
        });
    }
    public void stringRequestPost(String json){
        String url= UrlManager.SERVLET_URL+"StudentsServlet";
        StringPostRequest spr=new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(MyActivity.this, s, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("json",json);
        SingleVolleyRequestQueue.getInstance(this).addToRequestQueue(spr);

    }
}
