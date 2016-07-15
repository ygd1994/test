package com.example.ygd.lostandfound.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ygd.lostandfound.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabHostFragment extends Fragment {
    public TabHostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_tab_host, container, false);
        TextView tv= (TextView) v.findViewById(R.id.tv);
        Bundle bundle=getArguments();
        if(bundle!=null){
            String arg=bundle.getString("arg");
            tv.setText(arg);
        }
        return v;
    }

}
