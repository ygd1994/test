package com.example.ygd.lostandfound.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ygd.lostandfound.GivesDetailActivity;
import com.example.ygd.lostandfound.MyGivesActivity;
import com.example.ygd.lostandfound.R;
import com.example.ygd.lostandfound.adapter.GivesListAdapter;
import com.example.ygd.lostandfound.application.MyApplication;
import com.example.ygd.lostandfound.dao.Gives;
import com.example.ygd.lostandfound.dao.GivesDao;
import com.example.ygd.lostandfound.dao.Students;
import com.example.ygd.lostandfound.util.SingleVolleyRequestQueue;
import com.example.ygd.lostandfound.util.UrlManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyGiveFragment extends Fragment {
    private Button bt1,bt2;
    private ListView lv;
    private List<Gives> givesList;
    private GivesDao givesDao;
    private GivesListAdapter gla;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            gla.notifyDataSetChanged();
        }
    };
    public MyGiveFragment() {}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my_give, container, false);
        givesDao= MyApplication.getInstance().getDaoSession(getActivity()).getGivesDao();
        bt1= (Button) view.findViewById(R.id.bt1);
        bt2= (Button) view.findViewById(R.id.bt2);
        lv= (ListView) view.findViewById(R.id.lv);
        givesList=new ArrayList();
        loadLvList();
        gla=new GivesListAdapter(givesList,getContext());
        lv.setAdapter(gla);
        bt1.setOnClickListener(new View.OnClickListener() { //刷新
            @Override
            public void onClick(View v) {
                stringRequestGet();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() { //管理我的物品
            @Override
            public void onClick(View v) {
                stringRequestGet();
                Intent intent=new Intent(getActivity(), MyGivesActivity.class);
                startActivity(intent);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), GivesDetailActivity.class);
                intent.putExtra("gives",givesList.get(position));
                startActivity(intent);
            }
        });


        registerForContextMenu(lv); //注册上下文菜单
        return view;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,1,1,"拨打电话");
        menu.add(0,2,2,"发送短信");
        menu.add(0,3,3,"添加QQ");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position= info.position;
        String id=givesList.get(position).getS_id();
        studentsInfoRequestGet(UrlManager.SERVLET_URL+"GivesServlet?tag=2&id="+id,item.getItemId());
        return true;
    }
    private void loadLvList() {
        givesList=givesDao.loadAll();
        if(givesList==null||givesList.size()==0){
            stringRequestGet();
        }

    }
    public void stringRequestGet(){
        String url=UrlManager.SERVLET_URL+"GivesServlet?tag=0";
        StringRequest sr=new StringRequest(url, new Response.Listener<String>() {   //响应成功监听接口
            @Override
            public void onResponse(String json) {
                Gson gson= new Gson();
                json=json.trim();
//                Log.d("===json===",json);
                List<Gives> list=gson.fromJson(json,new TypeToken<ArrayList<Gives>>(){}.getType());
                givesDao.deleteAll();
                for (int i=0;i<list.size();i++){
                    Gives gives=list.get(i);
                    givesDao.insertOrReplace(gives);
                }
                List temp =givesDao.loadAll();
                givesList.clear();
                givesList.addAll(temp);
                mHandler.sendEmptyMessage(0);
            }
        }, new Response.ErrorListener() {   //响应错误监听接口
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //提示网络异常
                Toast.makeText(getContext(), "网络连接异常", Toast.LENGTH_SHORT).show();
            }
        });
        SingleVolleyRequestQueue.getInstance(getActivity()).addToRequestQueue(sr);
    }

    public void studentsInfoRequestGet(String url, final int itemId){
        StringRequest sr=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                Gson gson= new Gson();
                json=json.trim();
                Students stuInfo=gson.fromJson(json,new TypeToken<Students>(){}.getType());
                switch (itemId){
                    case 1:
                        Uri uri1=Uri.parse("tel:"+stuInfo.getSTel());
                        Intent it1=new Intent(Intent.ACTION_DIAL,uri1);
                        startActivity(it1);
                        break;
                    case 2:
                        Uri uri2=Uri.parse("smsto:"+stuInfo.getSTel());
                        Intent it2=new Intent(Intent.ACTION_SENDTO,uri2);
                        it2.putExtra("sms_body","你好，您发在网上的东西像是我丢的，请问可以联系一下吗？");
                        startActivity(it2);
                        break;
                    case 3:
                        String qqNumber=stuInfo.getSQQ();
                        String url11 = "mqqwpa://im/chat?chat_type=wpa&uin="+qqNumber;
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url11)));
                        Toast.makeText(getContext(), ""+stuInfo.getSQQ(), Toast.LENGTH_SHORT).show();
                    default:
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), "网络连接异常", Toast.LENGTH_SHORT).show();
            }
        });
        SingleVolleyRequestQueue.getInstance(getActivity()).addToRequestQueue(sr);
    }

}
