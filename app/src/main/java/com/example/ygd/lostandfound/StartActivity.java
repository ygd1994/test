package com.example.ygd.lostandfound;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ygd.lostandfound.application.MyApplication;
import com.example.ygd.lostandfound.dao.Students;
import com.example.ygd.lostandfound.util.SingleVolleyRequestQueue;
import com.example.ygd.lostandfound.util.UrlManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class StartActivity extends AppCompatActivity {
    private Button bt;
    private EditText et1,et2;
    private CheckBox cb1,cb2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();
        et1=(EditText)findViewById(R.id.et1);
        et2=(EditText)findViewById(R.id.et2);
        bt=(Button)findViewById(R.id.bt1);
        cb1= (CheckBox) findViewById(R.id.cb1);
        cb2= (CheckBox) findViewById(R.id.cb2);


        SharedPreferences sp=getSharedPreferences("spTest", Context.MODE_PRIVATE);
        if(sp!=null){
            String id=sp.getString("id","");
            String pwd=sp.getString("pwd","");
            Boolean flag=sp.getBoolean("flag",false);
            et1.setText(id);
            et2.setText(pwd);
            if(flag){
                stringRequestGet();
            }

        }
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringRequestGet();
            }
        });
    }

    public void stringRequestGet(){
        final String  _id=et1.getText().toString();
        final String sPwd=et2.getText().toString();
        String url= UrlManager.SERVLET_URL+"TestServlet?_id="+_id+"&sPwd="+sPwd;
        StringRequest sr=new StringRequest(url, new Response.Listener<String>() {   //响应成功监听接口
            @Override
            public void onResponse(String json) {
                Gson gson= new Gson();
                json=json.trim();
                Students stu=gson.fromJson(json,new TypeToken<Students>(){}.getType());
                if(stu.get_id().equals("error")){
                    Toast.makeText(StartActivity.this, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();

                }else{
                    if(cb1.isChecked()){
                        SharedPreferences sp=getSharedPreferences("spTest", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit=sp.edit();
                        edit.putString("id",_id);
                        edit.putString("pwd",sPwd);
                        edit.putBoolean("flag",cb2.isChecked());
                        edit.commit();
                    }else{
                        SharedPreferences sp=getSharedPreferences("spTest", Context.MODE_PRIVATE);
                        if(sp!=null){
                            SharedPreferences.Editor edit=sp.edit();
                            edit.clear();
                        }
                    }

                    //设置全局变量
                    MyApplication app= (MyApplication) getApplication();
                    app.setUser(stu);

                    Intent it=new Intent(getBaseContext(),MainActivityTest.class);
                    startActivity(it);
                }

            }
        }, new Response.ErrorListener() {   //响应错误监听接口
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //提示网络异常
                Toast.makeText(StartActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();
            }
        });
        SingleVolleyRequestQueue.getInstance(this).addToRequestQueue(sr);
    }

}
