package com.example.ygd.lostandfound.application;

import android.app.Application;
import android.content.Context;

import com.example.ygd.lostandfound.dao.DaoMaster;
import com.example.ygd.lostandfound.dao.DaoSession;
import com.example.ygd.lostandfound.dao.Students;
import com.example.ygd.lostandfound.util.ImageLoaderUtil;

public class MyApplication extends Application {
    private DaoSession daoSession;
    private DaoMaster daoMaster;

    private Students user;
    //Application实例对象
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        ImageLoaderUtil.imgLoaderInit(this);
    }
    public static MyApplication getInstance(){
        return instance;
    }
    public DaoMaster getDaoMaster(Context context){
        if(daoMaster==null){
            DaoMaster.OpenHelper helper=new DaoMaster.DevOpenHelper(context,"LostAndFoundDb.db",null);
            daoMaster=new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public DaoSession getDaoSession(Context context){
        if(daoSession==null){
            if(daoSession==null){
                daoMaster=getDaoMaster(context);
            }
            daoSession=daoMaster.newSession();
        }
        return daoSession;
    }

    public Students getUser(){
        return user;
    }
    public void setUser(Students user){
        this.user=user;
    }

}
