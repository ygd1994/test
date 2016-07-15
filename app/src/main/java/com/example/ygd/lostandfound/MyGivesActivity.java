package com.example.ygd.lostandfound;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ygd.lostandfound.adapter.GivesListAdapter;
import com.example.ygd.lostandfound.application.MyApplication;
import com.example.ygd.lostandfound.dao.Gives;
import com.example.ygd.lostandfound.dao.GivesDao;
import com.example.ygd.lostandfound.util.FileUitlity;
import com.example.ygd.lostandfound.util.ImageLoaderUtil;
import com.example.ygd.lostandfound.util.LocationMainUtil;
import com.example.ygd.lostandfound.util.SingleVolleyRequestQueue;
import com.example.ygd.lostandfound.util.StringPostRequest;
import com.example.ygd.lostandfound.util.UrlManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyGivesActivity extends AppCompatActivity {
    private Button bt;
    private ListView lv;
    private List<Gives> myData;
    private GivesDao givesDao;
    private String s_id;
    private GivesListAdapter gla;
    private LocationMainUtil locationMainUtil;
    private double gLatitude;
    private double gLongitude;
    private String capturePath;
    private final int TAKE_PHOTO=0;
    private final int RESULT_PHOTO=2;
    private  final int PHONE_PHOTO=1;
    private PopupWindow popupWindow;
    private ImageView iv1;
    private String imgUrl;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            gla.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gives);
        locationMainUtil=new LocationMainUtil(this);
        givesDao= MyApplication.getInstance().getDaoSession(this).getGivesDao();
        MyApplication app= (MyApplication) getApplication();
        s_id=app.getUser().get_id();
        bt= (Button) findViewById(R.id.bt);
        lv= (ListView) findViewById(R.id.lv);
        myData=givesDao.queryBuilder().where(GivesDao.Properties.S_id.eq(s_id)).list();
        gla=new GivesListAdapter(myData,this);
        lv.setAdapter(gla);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlertDialog();
            }
        });
        registerForContextMenu(lv); //注册上下文菜单
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,1,1,"增加");
        menu.add(0,2,2,"修改");
        menu.add(0,3,3,"删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position=info.position;
        long id=myData.get(position).getId();
        switch (item.getItemId()){
            case 1:
                setAlertDialog();
                break;
            case 2:
                setAlertDialog(position);
                break;
            case 3:
                givesDao.deleteByKey(id);
                notifyLv();
                String url=UrlManager.SERVLET_URL+"GivesServlet?tag=4&_id="+id;
                pubRequestGet(url);
                break;
            default:
                break;
        }
        return true;
    }


    //设置添加信息的弹出窗口
    public void setAlertDialog(){
        final TableLayout pubDialog= (TableLayout) LayoutInflater.from(this).inflate(R.layout.pub_gives,null);
        final EditText et1= (EditText) pubDialog.findViewById(R.id.et1);
        final EditText et2= (EditText) pubDialog.findViewById(R.id.et2);
        final EditText et3= (EditText) pubDialog.findViewById(R.id.et3);
        Button bt1= (Button) pubDialog.findViewById(R.id.bt1);
        final TextView tv5= (TextView) pubDialog.findViewById(R.id.tv5);
        iv1= (ImageView) pubDialog.findViewById(R.id.iv1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location=locationMainUtil.getLocation();
                List<Address> addressList=locationMainUtil.getAddress(location);
                if(addressList!=null&&addressList.size()>0){
                    Address as=addressList.get(0);
                    gLatitude=as.getLatitude();
                    gLongitude=as.getLongitude();
                    tv5.setText("纬度："+gLatitude+" 经度"+gLongitude);
                }
            }
        });
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.setWidth(pubDialog.getWidth());
                int height=getResources().getDisplayMetrics().heightPixels/3;
                popupWindow.setHeight(height);
                popupWindow.showAtLocation(v, Gravity.BOTTOM,0,0);
                popupWindow.showAsDropDown(v,0,0);
                backgroundAlpha(0.7f);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });
            }
        });
        View view=getLayoutInflater().inflate(R.layout.pop_layout,null);
        popupWindow=new PopupWindow(view);
        popupWindow.setFocusable(true);
        ColorDrawable cd=new ColorDrawable();
        popupWindow.setBackgroundDrawable(cd);
        Button bt_Camera= (Button) view.findViewById(R.id.bt_camera);
        Button bt_Photo= (Button) view.findViewById(R.id.bt_photo);
        Button bt_qx= (Button) view.findViewById(R.id.bt_qx);
        bt_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用相机
                Intent camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File parent=FileUitlity.getInstance(getApplicationContext()).makeDir("gives_img");
                capturePath=parent.getPath()+File.separatorChar+System.currentTimeMillis()+".jpg";
                camera.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(capturePath)));
                camera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
                startActivityForResult(camera,TAKE_PHOTO);
                popupWindow.dismiss();
            }
        });
        bt_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用相册
                phone_photo();
                popupWindow.dismiss();
            }
        });
        bt_qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("物品信息").setView(pubDialog)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long time =System.currentTimeMillis();
                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                        String date = format.format(new Date(time));
                        if(et1.getText()!=null&&et2.getText()!=null){
                            String gName=et1.getText().toString();
                            String gLocation=et2.getText().toString();
                            String gDescription=et3.getText().toString();
                            String url= UrlManager.SERVLET_URL+"GivesServlet?tag=1&s_id="+s_id+"&gDate="+date
                                    +"&gName="+gName+"&gLocation="+gLocation+"&gDescription="+gDescription+"&gImgUrl="+imgUrl
                                    +"&gLatitude="+gLatitude+"&gLongitude="+gLongitude;
                            Gives gives=new Gives(null,s_id,gName,gLocation,date,gDescription,imgUrl,gLatitude,gLongitude);
                            givesDao.insertWithoutSettingPk(gives);
                            pubRequestGet(url);
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create().show();
    }

    //设置修改信息的弹出窗口
    public void setAlertDialog(final int position){
        final TableLayout pubDialog= (TableLayout) LayoutInflater.from(this).inflate(R.layout.pub_gives,null);
        final EditText et1= (EditText) pubDialog.findViewById(R.id.et1);
        final EditText et2= (EditText) pubDialog.findViewById(R.id.et2);
        final EditText et3= (EditText) pubDialog.findViewById(R.id.et3);
        final TextView tv5= (TextView) pubDialog.findViewById(R.id.tv5);
        iv1= (ImageView) pubDialog.findViewById(R.id.iv1);
        Button bt1= (Button) pubDialog.findViewById(R.id.bt1);
        Gives gives=myData.get(position);
        et1.setText(gives.getGName());
        et2.setText(gives.getGLocation());
        et3.setText(gives.getGDescription());
        tv5.setText("纬度："+gives.getGLatitude()+" 经度"+gives.getGLongitude());
        bt1.setText("重新定位");
        imgUrl=gives.getGImgUrl();
        ImageLoaderUtil.display(imgUrl,iv1);


        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location=locationMainUtil.getLocation();
                List<Address> addressList=locationMainUtil.getAddress(location);
                if(addressList!=null&&addressList.size()>0){
                    Address as=addressList.get(0);
                    gLatitude=as.getLatitude();
                    gLongitude=as.getLongitude();
                    tv5.setText("纬度："+gLatitude+" 经度"+gLongitude);
                }
            }
        });
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.setWidth(pubDialog.getWidth());
                int height=getResources().getDisplayMetrics().heightPixels/3;
                popupWindow.setHeight(height);
                popupWindow.showAtLocation(v, Gravity.BOTTOM,0,0);
                popupWindow.showAsDropDown(v,0,0);
                backgroundAlpha(0.7f);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });
            }
        });
        View view=getLayoutInflater().inflate(R.layout.pop_layout,null);
        popupWindow=new PopupWindow(view);
        popupWindow.setFocusable(true);
        ColorDrawable cd=new ColorDrawable();
        popupWindow.setBackgroundDrawable(cd);
        Button bt_Camera= (Button) view.findViewById(R.id.bt_camera);
        Button bt_Photo= (Button) view.findViewById(R.id.bt_photo);
        Button bt_qx= (Button) view.findViewById(R.id.bt_qx);
        bt_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用相机
                Intent camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File parent=FileUitlity.getInstance(getApplicationContext()).makeDir("gives_img");
                capturePath=parent.getPath()+File.separatorChar+System.currentTimeMillis()+".jpg";
                camera.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(capturePath)));
                camera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
                startActivityForResult(camera,TAKE_PHOTO);
                popupWindow.dismiss();
            }
        });
        bt_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用相册
                phone_photo();
                popupWindow.dismiss();
            }
        });
        bt_qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("物品信息").setView(pubDialog)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long time =System.currentTimeMillis();
                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                        String date = format.format(new Date(time));
                        if(et1.getText()!=null&&et2.getText()!=null){
                            String gName=et1.getText().toString();
                            String gLocation=et2.getText().toString();
                            String gDescription=et3.getText().toString();
                            long _id=myData.get(position).getId();
                            String url= UrlManager.SERVLET_URL+"GivesServlet?tag=3&_id="+_id+"&s_id="+s_id+"&gDate="+date
                                    +"&gName="+gName+"&gLocation="+gLocation+"&gDescription="+gDescription+"&gImgUrl="+imgUrl
                                    +"&gLatitude="+gLatitude+"&gLongitude="+gLongitude;
                            Gives gives=new Gives(null,s_id,gName,gLocation,date,gDescription,imgUrl,gLatitude,gLongitude);
                            givesDao.insertWithoutSettingPk(gives);
                            pubRequestGet(url);
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create().show();
    }


//    public void setAlertDialog(int position,final long id){
//        TableLayout pubDialog= (TableLayout) LayoutInflater.from(this).inflate(R.layout.pub_gives,null);
//        final EditText et1= (EditText) pubDialog.findViewById(R.id.et1);
//        final EditText et2= (EditText) pubDialog.findViewById(R.id.et2);
//
//        et1.setText(myData.get(position).getGName());
//        et2.setText(myData.get(position).getGLocation());
//        new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("物品信息").setView(pubDialog)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        long time =System.currentTimeMillis();
//                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//                        String date = format.format(new Date(time));
//                        if(et1.getText()!=null&&et2.getText()!=null){
//                            String gName=et1.getText().toString();
//                            String gLocation=et2.getText().toString();
//                            String url= UrlManager.SERVLET_URL+"GivesServlet?tag=3&_id="+id+"&s_id="+s_id+"&gDate="+date
//                                    +"&gName="+gName+"&gLocation="+gLocation;
//                            Gives gives=new Gives(id,s_id,gName,gLocation,date);
//                            givesDao.update(gives);
//                            notifyLv();
//                            pubRequestGet(url);
//                        }
//                    }
//                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        }).create().show();
//    }


    public void pubRequestGet(String url){
        StringRequest sr=new StringRequest(url, new Response.Listener<String>() {   //响应成功监听接口
            @Override
            public void onResponse(String json) {
                Toast.makeText(getBaseContext(), "操作成功", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getBaseContext(), "网络连接异常", Toast.LENGTH_SHORT).show();
            }
        });
        SingleVolleyRequestQueue.getInstance(this).addToRequestQueue(sr);
    }


    public void notifyLv(){
        List temp =givesDao.loadAll();
        myData.clear();
        myData.addAll(temp);
        mHandler.sendEmptyMessage(0);
    }



    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
    public void phone_photo(){
        Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,PHONE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        //相机返回结果，调用系统裁剪
        if(requestCode==TAKE_PHOTO){
            startPhoneZoom(Uri.fromFile(new File(capturePath)));
        }
        else if (requestCode==PHONE_PHOTO){
            Cursor cursor=this.getContentResolver().query(data.getData(),new String[]{MediaStore.Images.Media.DATA},null,null,null);
            cursor.moveToFirst();
            String  capturePath=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
            startPhoneZoom(Uri.fromFile(new File(capturePath)));
        }
        //裁剪返回位图
        else if (requestCode==RESULT_PHOTO){
            Bundle bundle=data.getExtras();
            if(bundle!=null){
                Bitmap bitmap=bundle.getParcelable("data");
                iv1.setImageBitmap(bitmap);
                updatePhoto(convertBitmap(bitmap));
            }
        }
    }


    private void updatePhoto(String photoStr) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("正在上传，请稍后……");
        if (pd != null){
            pd.show();
        }
        // 加载网络数据
        String url = UrlManager.SERVLET_URL+"GivesImgUpload";
        StringPostRequest request = new StringPostRequest(url,new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(pd!=null && pd.isShowing())
                    pd.dismiss();
                imgUrl = s;
                Log.d("===imgUrl===",s);
                Toast.makeText(getApplicationContext(), "头像上传成功！", Toast.LENGTH_SHORT).show();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(pd!=null && pd.isShowing())
                    pd.dismiss();
                Toast.makeText(getApplicationContext(), "头像上传失败！", Toast.LENGTH_SHORT).show();
            }
        });
        request.putValue("uhead", photoStr);
        SingleVolleyRequestQueue.getInstance(this).addToRequestQueue(request);
    }


    //Bitmap转换成字符串
    public String convertBitmap(Bitmap b){
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG,100,out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buffer=out.toByteArray();
        byte[] encode= Base64.encode(buffer,Base64.DEFAULT);
        return new String(encode);
    }



    //调用系统裁剪
    public void startPhoneZoom(Uri uri){
        Intent intent=new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");
        //设置是否可以裁剪
        intent.putExtra("crop","true");
        //设置宽度高度比例
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        //设置图片的高度宽度
        intent.putExtra("outputX",150);
        intent.putExtra("outputY",150);

        intent.putExtra("return-data",true);

        startActivityForResult(intent,RESULT_PHOTO);


    }


}
