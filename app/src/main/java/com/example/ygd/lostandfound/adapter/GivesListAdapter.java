package com.example.ygd.lostandfound.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ygd.lostandfound.R;
import com.example.ygd.lostandfound.dao.Gives;
import com.example.ygd.lostandfound.util.ImageLoaderUtil;

import java.util.List;

/**
 * Created by ygd on 2016/5/24.
 */
public class GivesListAdapter extends BaseAdapter{
    private List<Gives> myData;
    private Context context;

    public GivesListAdapter(List<Gives> myData, Context context) {
        this.myData = myData;
        this.context = context;
    }

    @Override
    public int getCount() {
        return myData.size();
    }

    @Override
    public Object getItem(int position) {
        return myData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return myData.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Gives gives=myData.get(position);
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.gives_list_item,null);
            viewHolder.tv1=(TextView)convertView.findViewById(R.id.tv1);
            viewHolder.tv2=(TextView)convertView.findViewById(R.id.tv2);
            viewHolder.tv3=(TextView)convertView.findViewById(R.id.tv3);
            viewHolder.iv=(ImageView)convertView.findViewById(R.id.iv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tv1.setText(gives.getGName());
        viewHolder.tv2.setText(gives.getGLocation());
        viewHolder.tv3.setText(gives.getGDate());
        String url=gives.getGImgUrl();
        ImageLoaderUtil.display(url,viewHolder.iv);
        return convertView;
    }
    private class ViewHolder{
        public TextView tv1,tv2,tv3;
        public ImageView iv;
    }

}
