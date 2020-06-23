package com.werb.mycalendardemo.alarmsetactivity;

import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.werb.mycalendardemo.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SetLocalActivity extends AppCompatActivity {

    @Bind(R.id.ed_local)
    EditText ed_local;
    @Bind(R.id.bmapview)
    MapView mMapView;
    @Bind(R.id.iv_local)
    ImageButton iv_local;
    private BaiduMap mBaiduMap;
    private boolean isFirstLoc = true;//记录是否是第一次定位
    private MyLocationConfiguration.LocationMode locationMode;//当前定位模式
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @OnClick(R.id.tv_save)
    void saveAndClose() {
        Intent intent = new Intent();
        if (ed_local.getText().toString().equals("")) {
            intent.putExtra("local", "无");
            setResult(2, intent);
            finish();
        } else {
            intent.putExtra("local", ed_local.getText().toString());
            setResult(2, intent);
            finish();
        }

    }

    @OnClick(R.id.left_local_back)
    void finishClose() {
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());//初始化地图SDK
        setContentView(R.layout.activity_set_local);
        mMapView = (MapView) findViewById(R.id.bmapview);
        mBaiduMap = mMapView.getMap();
        //=========================================定位当前位置========================
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //添加权限检查
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //设置每一秒获取一次location信息
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,//GPS定位提供者
                1000,//更新数据时间为1秒
                1,//位置间隔为1米
                //位置监听器
                new LocationListener() {//GPS定位信息发生改变时触发，用于更新位置信息
                    @Override
                    public void onLocationChanged(Location location) {
                        locationUpdates(location);//GPS信息发生改变时，更新位置
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
                }
        );
        //从GPS获取最新的定位信息
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationUpdates(location);//将最新的定位信息传递给创建的locationUpdate（）方法中
        //=========================================定位当前位置结束========================
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //==========================================搜索框获取位置=============================
        iv_local = (ImageButton) findViewById(R.id.iv_local);
        iv_local.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String addressString = ed_local.getText().toString();

                                            //  String address = "address=" + addressString;
                                            /**调用百度地图Web页面
                                             * address=LocaltionAddress&src=YourAppName
                                             */
                                            //  String uristr = "http://api.map.baidu.com/geocoder?" + address
                                            //          + "&output=html&src=MyCalenderDemo";
                                            //  Uri uri = Uri.parse(uristr);
                                            //  Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                            //   startActivity(intent);
                                            //}
                                            try{
                                                Log.i("addr2",addressString);
                                                Object[] o =getCoordinate(addressString);
                                                Log.i("addr2",o[1].toString());
                                                Log.i("addr2", o[0].toString());

                                                //System.out.println(Double.parseDouble(o[1].toString()));//经度
                                                //System.out.println(Double.parseDouble(o[0].toString()));//纬度
                                                LatLng point=new LatLng(Double.parseDouble(o[1].toString()),Double.parseDouble(o[0].toString()));//定义mark坐标点
                                                //定义地图状态
                                                MapStatus mMapStatus = new MapStatus.Builder()
                                                        .target(point)
                                                        .zoom(18)
                                                        .build();
                                                //定义MapStatusUpdate对象，以便描述bai地图状态将要发生的变化
                                                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                                                //改变地图状态
                                                mBaiduMap.setMapStatus(mMapStatusUpdate);
                                                BitmapDescriptor bitmap=BitmapDescriptorFactory.fromResource(R.drawable.destination);//构建Marker坐标
                                                OverlayOptions option=new MarkerOptions().position(point).icon(bitmap);//构建markoption，用于在地图上添加marker
                                                mBaiduMap.addOverlay(option);//在地图上添加marker并显示
                                            }catch(IOException e){

                                            }


                                        }
                                    }

        );




        //==========================================搜索框获取位置结束=============================
        ButterKnife.bind(this);


    }

    public void locationUpdates(Location location) {//获取指定的查询信息
        if (location != null) {//如果location不为空时
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            Log.i("Location", "纬度：" + location.getLatitude() + "| 经度：" + location.getLongitude());
            if (isFirstLoc) {
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);//设置地图位置
                isFirstLoc = false;//取消第一次定位
            }
            //构造定位数据
            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getAccuracy())
                    .direction(100)//设置方向信息
                    .latitude(location.getLatitude())//设置纬度坐标
                    .longitude(location.getLongitude())//设置经度坐标
                    .build();
            mBaiduMap.setMyLocationData(locData);//设置定位数据
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_bg);//设置自定义图标
            locationMode = MyLocationConfiguration.LocationMode.NORMAL;//设置定位模式
            MyLocationConfiguration config = new MyLocationConfiguration(locationMode, true, bitmapDescriptor);//设置构造方式
            mBaiduMap.setMyLocationConfiguration(config);//显示定位图标

        } else {//否则输出空信息
            Log.i("Location", "没有获取到GPS信息");
        }
    }


    public Object[] getCoordinate(String addr) throws IOException {
        String lng = null;//经度
        String lat = null;//纬度
        String address = null;
        try {
            address = java.net.URLEncoder.encode(addr, "UTF-8");
        }catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String key = "ZBpjPdn4Gb4W5ga9mSwP4iPYOARzYUi5";
        String url = String .format("http://api.map.baidu.com/geocoder?address=%s&output=json&key=%s", address, key);
        URL myURL = null;
        URLConnection httpsConn = null;
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStreamReader insr = null;
        BufferedReader br = null;
        try {
            httpsConn = (URLConnection) myURL.openConnection();// 不使用代理
            if (httpsConn != null) {
                insr = new InputStreamReader( httpsConn.getInputStream(), "UTF-8");
                br = new BufferedReader(insr);
                String data = null;
                int count = 1;
                while((data= br.readLine())!=null){
                    if(count==5){
                        lng = (String)data.subSequence(data.indexOf(":")+1, data.indexOf(","));//经度
                        count++;
                    }else if(count==6){
                        lat = data.substring(data.indexOf(":")+1);//纬度
                        count++;
                    }else{
                        count++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(insr!=null){
                insr.close();
            }
            if(br!=null){
                br.close();
            }
        }
        return new Object[]{lng,lat};
    }

    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mMapView = null;
    }

    protected void onStart() {
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);//开启定位图层
    }

    protected void onStop() {
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);//停止定位图层
    }

}

