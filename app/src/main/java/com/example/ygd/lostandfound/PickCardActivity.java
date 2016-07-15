package com.example.ygd.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ygd.lostandfound.application.MyApplication;
import com.example.ygd.lostandfound.dao.Students;
import com.example.ygd.lostandfound.util.SingleVolleyRequestQueue;
import com.example.ygd.lostandfound.util.UrlManager;

public class PickCardActivity extends AppCompatActivity {
    Button sure;
    EditText cardno;
    TextView cardtype;
    Students user;
    int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_card);
        MyApplication app= (MyApplication) this.getApplication();
        user=app.getUser();
        cardtype = (TextView) findViewById(R.id.cardtype);
        Intent it=getIntent();
        type=it.getIntExtra("tag",0);
        if(type==0){
            cardtype.setText("校园卡卡号：");
        }else
        if (type == 1){
            cardtype.setText("身份证卡号：");
        } else{
            cardtype.setText("银行卡号：");
        }
        cardno = (EditText) findViewById(R.id.cardno);

        sure = (Button) findViewById(R.id.sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里要根据cardtype的类型,
                //从数据库里面找到和cardno一样的 student
                //然后发送一个消息给student 写进数据库  消息表
                //然后转移到thanks画面
                String str=cardno.getText().toString();
                if(type==0){
                    String url= UrlManager.SERVLET_URL+"PickServlet?tag=0&l_id="+str+"&p_id="+user.get_id();
                    stringRequestGet(url);
                } else {
                    //从info ifeature 找更str一样的  如果有就给这个人发消息
                    String url= UrlManager.SERVLET_URL+"PickServlet?tag=1&l_id="+str+"&p_id="+user.get_id();
                    stringRequestGet(url);
                }
                Intent intent = new Intent(getBaseContext(),ThanksActivity.class);
                startActivity(intent);
            }
        });
    }
    public void stringRequestGet(String url){
        StringRequest sr=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                Toast.makeText(PickCardActivity.this, ""+json, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(PickCardActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();
            }
        });
        SingleVolleyRequestQueue.getInstance(this).addToRequestQueue(sr);
    }
}
