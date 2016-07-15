package com.example.ygd.lostandfound.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by ygd on 2016/5/11.
 */
public class LocationMainUtil {
    private Context context;
    private LocationManager locationManager;

    public LocationMainUtil(Context context) {
        this.context = context;
    }

    public Location getLocation() {
        //获取位置管理
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //服务信息
        Criteria criteria = new Criteria();
        //定位精度
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //海拔信息
        criteria.setAltitudeRequired(false);
        //方位信息
        criteria.setBearingRequired(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(provider,2000,50,locationListener);

        return location;
    }
    private final LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if(location!=null){
                Log.v("=====纬度",""+location.getLatitude());
                Log.v("=====经度",""+location.getLongitude());
            }else{
                Log.v("=====","获取定位失败");
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public List<Address> getAddress(Location location){
        List<Address> result=null;
        if(location!=null){
            Geocoder gc=new Geocoder(context, Locale.getDefault());
            try {
                result=gc.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
