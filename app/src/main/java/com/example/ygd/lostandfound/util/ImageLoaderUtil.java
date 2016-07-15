package com.example.ygd.lostandfound.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.ygd.lostandfound.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * Created by ygd on 2016/5/6.
 */
public class ImageLoaderUtil {
    private static DisplayImageOptions options=
            new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.ic_launcher).showImageOnFail(R.mipmap.ic_launcher)
    .showImageForEmptyUri(R.mipmap.ic_launcher).cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).resetViewBeforeLoading(true).build();

    public static void display(String uri, ImageView imageView){    //设置
        ImageLoader.getInstance().displayImage(uri,imageView,options);
    }

    public static void imgLoaderInit(Context context){  //初始化
        ImageLoaderConfiguration config=
                new ImageLoaderConfiguration.Builder(context).denyCacheImageMultipleSizesInMemory().threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY).memoryCacheSize((int) (Runtime.getRuntime().maxMemory()/8))
                .diskCacheSize(50*1024*1024).diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()).imageDownloader(new BaseImageDownloader(context,50000,50000))
                .diskCache(new UnlimitedDiskCache(FileUitlity.getInstance(context).makeDir("imgCache"))).build();
        ImageLoader.getInstance().init(config);
    }
}
