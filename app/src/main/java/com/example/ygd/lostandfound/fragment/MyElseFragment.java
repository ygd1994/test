package com.example.ygd.lostandfound.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ygd.lostandfound.MyActivity;
import com.example.ygd.lostandfound.MyAgreeActivity;
import com.example.ygd.lostandfound.MyMessageActivity;
import com.example.ygd.lostandfound.MyPubActivity;
import com.example.ygd.lostandfound.OnlineHelpActivity;
import com.example.ygd.lostandfound.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyElseFragment extends Fragment {


    public MyElseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(
                R.layout.fragment_my_else, container, false);
        Button my,pub,online,mynews,myagree;
        my = (Button) view.findViewById(R.id.my);
        my.setOnClickListener(new MyClick());
        pub = (Button) view.findViewById(R.id.pub);
        pub.setOnClickListener(new MyClick());
        online = (Button) view.findViewById(R.id.online);
        online.setOnClickListener(new MyClick());
        mynews = (Button) view.findViewById(R.id.mynews);
        mynews.setOnClickListener(new MyClick());
        myagree= (Button) view.findViewById(R.id.myagree);
        myagree.setOnClickListener(new MyClick());
        return view;
    }

    private class MyClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.my:
                    intent = new Intent(getContext(),MyActivity.class);
                    startActivity(intent);
                    break;
                case R.id.pub:
                    intent = new Intent(getContext(),MyPubActivity.class);
                    startActivity(intent);
                    break;
                case R.id.online:
                    intent=new Intent(getContext(), OnlineHelpActivity.class);
                    startActivity(intent);
                    break;
                case R.id.mynews:
                    intent=new Intent(getContext(), MyMessageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.myagree:
                    intent=new Intent(getContext(), MyAgreeActivity.class);
                    startActivity(intent);
                default:
                    break;
            }
        }
    }

}
