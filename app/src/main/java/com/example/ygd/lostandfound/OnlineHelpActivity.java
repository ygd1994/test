package com.example.ygd.lostandfound;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OnlineHelpActivity extends AppCompatActivity {
    private Button bt1,bt2,bt3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_help);
        bt1= (Button) findViewById(R.id.bt1);
        bt2= (Button) findViewById(R.id.bt2);
        bt3= (Button) findViewById(R.id.bt3);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url1 = "mqqwpa://im/chat?chat_type=wpa&uin="+754023957;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url1)));
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url2 = "mqqwpa://im/chat?chat_type=wpa&uin="+642657652;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url2)));
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="http://blog.csdn.net/ygd1994";
                Intent intent=new Intent(OnlineHelpActivity.this,MyCSDNActivity.class);
                intent.putExtra("url",url);
                startActivity(intent);

            }
        });
    }
}
