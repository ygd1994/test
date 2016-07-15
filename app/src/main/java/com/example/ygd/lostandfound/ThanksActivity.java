package com.example.ygd.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ThanksActivity extends AppCompatActivity {
    Button retn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);
        retn = (Button) findViewById(R.id.retn);
        retn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里回到主界面.
                Intent intent = new Intent(getBaseContext(),MainActivityTest.class);
                startActivity(intent);
            }
        });
    }
}
