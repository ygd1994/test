package com.example.ygd.lostandfound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PubSuccActivity extends AppCompatActivity {
    Button retn;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_succ);
        retn = (Button) findViewById(R.id.retn);
        retn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳回主界面
                Intent intent = new Intent(getBaseContext(),MainActivityTest.class);
                startActivity(intent);
            }
        });
    }
}
