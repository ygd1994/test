package com.example.ygd.lostandfound.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ygd.lostandfound.R;
import com.example.ygd.lostandfound.dao.Informations;

import java.util.List;

/**
 * Created by ygd on 2016/5/30.
 */
public class PubListAdapter extends BaseAdapter{
    private List<Informations> myData;
    private Context context;

    public PubListAdapter(List<Informations> myData, Context context) {
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
        Informations informations=myData.get(position);
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.pub_list_item,null);
            viewHolder.tv1=(TextView)convertView.findViewById(R.id.tv1);
            viewHolder.tv2=(TextView)convertView.findViewById(R.id.tv2);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        if(informations.getIIsLost()){
            viewHolder.tv1.setText("您弄丢了卡：");
        }else{
            viewHolder.tv1.setText("您捡到了卡：");
        }

        viewHolder.tv2.setText(informations.getINum());
        return convertView;
    }
    private class ViewHolder{
        public TextView tv1,tv2;
    }
}
