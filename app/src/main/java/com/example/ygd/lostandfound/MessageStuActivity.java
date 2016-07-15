package com.example.ygd.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ygd.lostandfound.dao.Students;
import com.example.ygd.lostandfound.util.SingleVolleyRequestQueue;
import com.example.ygd.lostandfound.util.UrlManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MessageStuActivity extends AppCompatActivity {
    private com.example.ygd.lostandfound.dao.Message message;
    private TextView tv1,tv2;
    private Button bt1;
    private Students stuInfo=new Students();
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv1.setText("电话："+stuInfo.getSTel());
            tv2.setText("QQ："+stuInfo.getSQQ());
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_stu);
        final Intent intent=getIntent();
        message=intent.getParcelableExtra("message");
        String s_id=message.getP_id();
        tv1= (TextView) findViewById(R.id.tv1);
        tv2= (TextView) findViewById(R.id.tv2);
        bt1= (Button) findViewById(R.id.bt1);
        studentsInfoRequestGet(UrlManager.SERVLET_URL+"GivesServlet?tag=2&id="+s_id);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long _id=message.getId();
                String url=UrlManager.SERVLET_URL+"InformationsServlet?tag=1&_id="+_id;
                stringRequestGet(url);
                Intent intent1=new Intent(MessageStuActivity.this,MyMessageActivity.class);
                startActivity(intent1);
            }
        });

    }

    public void stringRequestGet(String url){
        StringRequest sr=new StringRequest(url, new Response.Listener<String>() {   //响应成功监听接口
            @Override
            public void onResponse(String json) {
                Toast.makeText(getBaseContext(), "删除成功", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {   //响应错误监听接口
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getBaseContext(), "网络连接异常", Toast.LENGTH_SHORT).show();
            }
        });
        SingleVolleyRequestQueue.getInstance(this).addToRequestQueue(sr);
    }

    public void studentsInfoRequestGet(String url){
        StringRequest sr=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                Gson gson= new Gson();
                json=json.trim();
                stuInfo=gson.fromJson(json,new TypeToken<Students>(){}.getType());
                if(stuInfo!=null){
                    mHandler.sendEmptyMessage(0);
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
