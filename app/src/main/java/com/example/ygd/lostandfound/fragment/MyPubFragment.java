package com.example.ygd.lostandfound.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ygd.lostandfound.LoseActivity;
import com.example.ygd.lostandfound.PickFirActivity;
import com.example.ygd.lostandfound.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPubFragment extends Fragment {


    public MyPubFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_pub, container, false);
        Button lose,pick;
        lose = (Button) view.findViewById(R.id.lose);
        lose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoseActivity.class);
                startActivity(intent);
            }
        });
        pick = (Button) view.findViewById(R.id.pick);
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),PickFirActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
