package com.example.ygd.lostandfound.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ygd.lostandfound.R;
import com.example.ygd.lostandfound.dao.News;
import com.example.ygd.lostandfound.util.ImageLoaderUtil;
import com.example.ygd.lostandfound.util.UrlManager;

import java.util.List;

/**
 * Created by ygd on 2016/5/18.
 */
public class NewsListAdapter extends BaseAdapter{
    private List<News> newsList;
    private Context context;

    public NewsListAdapter(List<News> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return newsList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        News news=newsList.get(position);
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=LayoutInflater.from(context).inflate(R.layout.news_list_item,null);
            viewHolder.ivImg=(ImageView)convertView.findViewById(R.id.iv_img);
            viewHolder.tvTitle=(TextView)convertView.findViewById(R.id.tv_title);
            viewHolder.tvDate=(TextView)convertView.findViewById(R.id.tv_date);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTitle.setText(news.getNTitle());
        viewHolder.tvDate.setText(news.getNDate());
        String url= UrlManager.BASE_URL+news.getNImg();
        ImageLoaderUtil.display(url,viewHolder.ivImg);
        return convertView;
    }
    private class ViewHolder{
        public ImageView ivImg;
        public TextView tvTitle,tvDate;
    }
}
