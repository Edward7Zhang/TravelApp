package com.example.weilin.travelapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.MenuBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.example.weilin.travelapp.bean.Sense;
import com.example.weilin.travelapp.tools.MyTextUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mob.tools.utils.UIHandler;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;


public class MainActivity extends Activity implements PlatformActionListener, MenuBuilder.Callback, Callback, AMapLocationListener, LocationSource {

    private String text = "分享旅游信息";
    private String imageurl = "http://h.hiphotos.baidu.com/image/pic/item/ac4bd11373f082028dc9b2a749fbfbedaa641bca.jpg";
    private String title = "旅游景点";
    private String url = "www.baidu.com";
    private TextView comment;
    private TextView reservation;
    public static String SENSE_KEY = "sense";
    /**
     * viewpager的数据源
     */
    private List<String> vpUrlList = new ArrayList<>();
    private List<String> pictureList;
    private ImageView shareImage;

    private SharePopupWindow share;

    private Button mCallButton;

    private int price = 120;//价格数据
    private ViewPager mImageVp;//viwepager
    private ViewPagerAdapter mAdapter;//适配器
    private TextView priceTv, indexTv;//价格和页脚
    private Button colt_bt;//收藏按钮
    private boolean flag = false;//收藏按钮的标志
    private Timer mTimer;//viewpager定时器
    private int currentItem;//viewpager定时器的循环标志

    TextureMapView mMapView = null;
    AMap aMap;
    LocationSource.OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;
    Button bt_route;
    static double Latitude;//获取纬度
    static double Longitude;//获取经度
    /**
     * 将下面两个参数传入注意纬度经度的顺序
     ********************************/

    static double inLatitude;//传入纬度         /*******/
    static double inLongitude;//传入经度       /*******/
    /*******************************************************************/
    private UiSettings mUiSettings;
    private CameraUpdate mUpdata;
    //    private TextView doComment_tv;
    private TextView name_tv;
    private TextView address_tv;
    private TextView phnumber_tv;
    private TextView openTime_tv;
    private Sense sense;
    private ImageView centerIMG;

    private Timer timer;
    private LinearLayout viewArea;

    private Button seeAll_bt;
    private TextView senceIntrudec_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        getJsonFromNet();//从网络下载数据
        ShareSDK.initSDK(this);

        comment = (TextView) findViewById(R.id.doComment_tv);
        reservation = (TextView) findViewById(R.id.doShop_tv);
        reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), loginActivity.class);
                startActivity(intent);
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), CommentActivity.class);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        //获取地图控件引用
        mMapView = (TextureMapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        bt_route = (Button) findViewById(R.id.bt_route);
        bt_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AMapNavi_View.class);
                startActivity(intent);
            }
        });
        initUI();
        intitHrs();
    }

    private void intitHrs() {
        viewArea = (LinearLayout) findViewById(R.id.hrsv);
        final Handler myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    if (viewArea.getWidth() != 0) {
                        Log.i("LinearLayout", viewArea.getWidth() + "");
                        Log.i("LinearLayout", viewArea.getHeight() + "");
                        ViewGroup.LayoutParams para;
                        HorizontalScrollView h = (HorizontalScrollView) findViewById(R.id.hrsv_hs);
                        para = h.getLayoutParams();
                        para.width = viewArea.getWidth();
                        h.setLayoutParams(para);

                        ImageView iv = (ImageView) findViewById(R.id.live_iv);
                        para = iv.getLayoutParams();
                        para.width = viewArea.getWidth() / 4;
                        iv.setLayoutParams(para);
                        iv = (ImageView) findViewById(R.id.play_iv);
                        para = iv.getLayoutParams();
                        para.width = viewArea.getWidth() / 4;
                        iv.setLayoutParams(para);
                        iv = (ImageView) findViewById(R.id.eat_iv);
                        para = iv.getLayoutParams();
                        para.width = viewArea.getWidth() / 4;
                        iv.setLayoutParams(para);
                        iv = (ImageView) findViewById(R.id.villager_iv);
                        para = iv.getLayoutParams();
                        para.width = viewArea.getWidth() / 4;
                        iv.setLayoutParams(para);
                        timer.cancel();

                    }
                }
            }
        };
        timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                Message message = new Message();
                message.what = 1;
                myHandler.sendMessage(message);
            }
        };
        //延迟每次延迟10 毫秒 隔1秒执行一次
        timer.schedule(task, 10, 1000);

    }

    private void initMap() {


        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);
        mUpdata = CameraUpdateFactory.newCameraPosition(
                //15是缩放比例，0是倾斜度，30显示比例
                new CameraPosition(new LatLng(inLatitude, inLongitude), 15, 0, 30));//这是地理位置，就是经纬度。
        aMap.moveCamera(mUpdata);//定位的方法
        drawMarkers();
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    public void drawMarkers() {

        Marker marker = aMap.addMarker(new MarkerOptions()
                .position(new LatLng(inLatitude, inLongitude))
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .draggable(true));
        marker.showInfoWindow();// 设置默认显示一个infowinfow
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            ///设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            mLocationOption.setOnceLocationLatest(true);

        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                //mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                Latitude = amapLocation.getLatitude();//获取纬度
                Longitude = amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                amapLocation.getAoiName();//获取当前定位点的AOI信息
                amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                amapLocation.getFloor();//获取当前室内定位的楼层
                amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
                //获取定位时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * 初始化界面
     */
    public void initUI() {

        name_tv = (TextView) findViewById(R.id.name_tv);
        address_tv = (TextView) findViewById(R.id.address_tv);
        phnumber_tv = (TextView) findViewById(R.id.phnumber_tv);
        openTime_tv = (TextView) findViewById(R.id.openTime_tv);
        centerIMG = (ImageView) findViewById(R.id.oneOfSence_img);
        /**
         *分享share的初始化
         */
        shareImage = (ImageView) findViewById(R.id.share_iv);
        shareImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                share = new SharePopupWindow(MainActivity.this);
                share.setPlatformActionListener(MainActivity.this);
                ShareModel model = new ShareModel();
                model.setImageUrl(imageurl);
                model.setText(text);
                model.setTitle(title);
                model.setUrl(url);
                share.initShareParams(model);
                share.showShareWindow();
                share.showAtLocation(MainActivity.this.findViewById(R.id.main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        mCallButton = (Button) findViewById(R.id.btn_call);
        mCallButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + sense.getSensePhnumber()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        });
        seeAll_bt = (Button) findViewById(R.id.seeAll_btn);
        senceIntrudec_tv = (TextView)findViewById(R.id.senseDetail_tv);
        seeAll_bt.setOnClickListener(mClickListener);
    }

    /**
     * 设置viewpager数据源
     *
     * @param list
     */
    public void setVpUrlList(List<String> list) {
        vpUrlList = list;
    }

    public void initvp() {
        /**
         * 控件的实例化viewpager，页脚文本框，价格文半框，收藏按钮
         */
        mImageVp = (ViewPager) findViewById(R.id.banner_vp);
        indexTv = (TextView) findViewById(R.id.index_tv);
        priceTv = (TextView) findViewById(R.id.price_tv);
        colt_bt = (Button) findViewById(R.id.colt_bt);

        /**
         * 生成适配器，绑定到viewpager上
         */
        mAdapter = new ViewPagerAdapter(this, vpUrlList);
        mImageVp.setAdapter(mAdapter);

        /**
         * viewpager，价格，页脚的初始化设置
         */

        indexTv.setText(1 + "/" + (vpUrlList.size() - 2));
        priceTv.setText("￥" + price);
        mImageVp.setCurrentItem(1);
        /**
         * 设置收藏按钮的监听
         */
        colt_bt.setOnClickListener(mClickListener);

        /**
         * 设置ViewPager的监听器
         */
        mImageVp.addOnPageChangeListener(mPageChangeListener);
        //初始化定时器
        initTimer();
    }

    /**
     * viewpager定时器初始化
     */
    public void initTimer() {
        mTimer = new Timer();
        mTimer.schedule(mTask, 5 * 1000, 5 * 1000);
    }

    /**
     * 定时器向线程发送消息
     */
    private TimerTask mTask = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.arg1 = 0;
            mHandler.sendMessage(message);
        }
    };
    /**
     * 开启线程更换图片
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 0) {
                currentItem++;
                if (currentItem > (vpUrlList.size() - 2)) {
                    currentItem = 1;
                    mImageVp.setCurrentItem(currentItem, false);
                } else
                    mImageVp.setCurrentItem(currentItem);
            } else if (msg.arg1 == 1) {
                sense = pullJason((String) msg.obj);
                inLatitude = Double.valueOf(sense.getSenseLatitude());
                inLongitude = Double.valueOf(sense.getSenseLongitude());
                initMap();
                //设置viewpager的数据源
                setVpUrlList(sense.getSensePictures());
                //绑定viewpager的相关的操作
                initvp();
                x.image().bind(centerIMG, sense.getSensePictures().get(1));
                mAdapter.notifyDataSetChanged();
                name_tv.setText(sense.getSenseName());
                phnumber_tv.setText("电话:" + sense.getSensePhnumber());
                address_tv.setText("地址:" + sense.getSenseAddress());
                openTime_tv.setText("开放时间:" + sense.getSenseOpenTime() + "~" + sense.getSenseCloseTime());
            }
        }

    };

    private Sense pullJason(String jsondata) {
        Gson gson = new Gson();
        List<Sense> senseList = gson.fromJson(jsondata, new TypeToken<List<Sense>>() {
        }.getType());
        System.out.println(senseList.get(0));
        return senseList.get(0);

    }

    /**
     * 收藏按钮的监听
     */
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.colt_bt)
            {
                if (flag) {
                    //设置收藏按钮的背景图片高亮
                    colt_bt.setBackgroundResource(R.drawable.favorite2);
                    flag = false;
                } else {
                    //设置收藏按钮的背景图
                    colt_bt.setBackgroundResource(R.drawable.favorite);
                    flag = true;
                }
            }
            if(view.getId() == R.id.seeAll_btn)
            {
                if(flag){
                    flag = false;
                    senceIntrudec_tv.setEllipsize(null); // 展开
                    senceIntrudec_tv.setSingleLine(flag);
                    seeAll_bt.setText("收起");
                }else{
                    flag = true;
                    senceIntrudec_tv.setEllipsize(TextUtils.TruncateAt.valueOf("END")); // 收缩
                    senceIntrudec_tv.setLines(5);
                    seeAll_bt.setText("查看所有");
                }
            }
        }

    };

    /**
     * 对viewpager的监听
     */
    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * viewpager的循环和页脚的文本设置
         * @param position
         */
        @Override
        public void onPageSelected(int position) {
            if (position < 1) { //首位之前，跳转到末尾（N）
                position = vpUrlList.size() - 2;
                indexTv.setText(position + 1 + "/" + (vpUrlList.size() - 2));
                mImageVp.setCurrentItem(position, false);
            } else if (position > (vpUrlList.size() - 2)) { //末位之后，跳转到首位（1）
                mImageVp.setCurrentItem(1, false); //false:不显示跳转过程的动画
                position = 1;
                indexTv.setText(position + "/" + (vpUrlList.size() - 2));
            }
            indexTv.setText(position + "/" + (vpUrlList.size() - 2));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (share != null) {
            share.dismiss();
        }
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
        mMapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onCancel(Platform arg0, int arg1) {
        Message msg = new Message();
        msg.what = 0;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onComplete(Platform plat, int action,
                           HashMap<String, Object> res) {
        Message msg = new Message();
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = plat;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {
        Message msg = new Message();
        msg.what = 1;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        int what = msg.what;
        if (what == 1) {
            Toast.makeText(this, "您没有注册", Toast.LENGTH_SHORT).show();
        }
        if (share != null) {
            share.dismiss();
        }
        return false;
    }

    @Override
    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
        return false;
    }

    @Override
    public void onMenuModeChange(MenuBuilder menu) {

    }

    /**
     * 从网络中下载json数据
     */
    public void getJsonFromNet() {
        RequestParams params = new RequestParams("http://115.159.150.136:8080/travel/Eservlet");
        params.setHeader("Content-Type", "text/html;charset=utf-8");
        params.addParameter("SenseCode", "1235");

        x.http().get(params, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Message message = new Message();
                message.arg1 = 1;
                message.obj = result;
                System.out.println(result);
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("下载失败......");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                System.out.println("下载程序完成........");
            }
        });
    }

    /**
     * 查看更多评论,跳转
     *
     * @param view
     */
    public void seeAllComment(View view) {
        Intent seeall = new Intent(MainActivity.this, AllComment.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SENSE_KEY, sense);
        seeall.putExtras(bundle);
        System.out.println("comment数据为:" + sense.getSenseComments());
        startActivity(seeall);
    }

    /**
     * 查看所有详情的介绍
     *
     * @param v
     */
    public void seeAlldetails(View v) {
        TextView senseDetail_tv = (TextView) findViewById(R.id.senseDetail_tv);
        String text = (String) senseDetail_tv.getText();
        String s = MyTextUtils.toDBC(text);
        senseDetail_tv.setText(s);
    }

}
