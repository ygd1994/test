package com.example.ygd.lostandfound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class PickFirActivity extends AppCompatActivity {
    Button sure;
    RadioButton r0,r1,r2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_fir);
        sure = (Button) findViewById(R.id.sure);
        r0 = (RadioButton) findViewById(R.id.radioButton);
        r1 = (RadioButton) findViewById(R.id.radioButton2);
        r2 = (RadioButton) findViewById(R.id.radioButton3);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //根据类型跳转 判断哪个按钮是true
                //证件的 跳转 pickcard
                //其他的 跳转 pickelse
                Intent intent;
                intent = new Intent(getBaseContext(),PickCardActivity.class);
                if (r0.isChecked()) {
                    //这里要带一个参数传过去,判断是r0还是r1
                    intent.putExtra("tag",0);
                } else
                if (r1.isChecked()){
                    intent.putExtra("tag",1);
                } else{
                    intent.putExtra("tag",2);
                }
                startActivity(intent);
            }
        });
    }
}
