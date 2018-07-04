package com.bocop.xfjr.util;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.boc.jx.tools.LogUtils;

import android.content.Context;
import android.os.Handler;

/**
 * 百度定位工具
 */
public class BDLocationUtils {

	private static volatile BDLocationUtils mInstance;

	private LocationClient mLocationClient = null;

	private OnLocationListener mOnLocationListener;
	
	private Handler mHandler;

	private BDLocationUtils() {
	}

	public static BDLocationUtils get() {
		if (mInstance == null) {
			synchronized (BDLocationUtils.class) {
				if (mInstance == null) {
					mInstance = new BDLocationUtils();
				}
			}
		}
		return mInstance;
	}

	private MyLocationListener myListener = new MyLocationListener();

	public void onCreate(Context context) {
		if (mLocationClient==null) {
			// 声明LocationClient类
			mLocationClient = new LocationClient(context.getApplicationContext()); // ApplicationContext() 防止百度定位服务泄露
		}
	}

	@SuppressWarnings("deprecation")
	public void startLocation(OnLocationListener listener) {
		mOnLocationListener = listener;
		mHandler = new Handler();
		LocationClientOption option = new LocationClientOption();
		// 可选，是否需要地址信息，默认为不需要，即参数为false
		// 如果开发者需要获得当前点的地址信息，此处必须为true
		option.setIsNeedAddress(true);
        option.setScanSpan(1000);//每隔1秒发起一次定位
//        option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
//        option.setOpenGps(true);//是否打开gps
//        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到该描述，不设置则在4G情况下会默认定位到“天安门广场”
//        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要，不设置则拿不到定位点的省市区信息
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		if (mLocationClient == null) {
			throw new NullPointerException("请先调用onCreat函数");
		}
		mLocationClient.setLocOption(option);
		// 注册监听函数
		mLocationClient.registerLocationListener(myListener);
		mLocationClient.start();
	}

	public void stop(){
		if (null != mLocationClient) {
			mLocationClient.stop();
		}
	}
	
	public void onDestory(){
		if (null != mLocationClient) {
			mLocationClient.stop();
			mLocationClient = null;
			mOnLocationListener = null;
			LogUtils.e("onDestory---->");
		}
//		mOnLocationListener = null;
	}
	
	private class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(final BDLocation location) {
			if(mOnLocationListener !=null){
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						mOnLocationListener.onLocation(location.getLocType(),location.getAddrStr(),location.getCountry(),location.getProvince(),location.getCity(), location.getDistrict(), location.getStreet(), location);
					}
				});
			}
		}

	}
	

	public interface OnLocationListener {
		
		/**
		 * @param errorCode
		 * 61	GPS定位结果，GPS定位成功
		 * 62	无法获取有效定位依据，定位失败，请检查运营商网络或者WiFi网络是否正常开启，尝试重新请求定位
		 * 63	网络异常，没有成功向服务器发起请求，请确认当前测试手机网络是否通畅，尝试重新请求定位
		 * 66	离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果
		 * 68	网络连接失败时，查找本地离线定位时对应的返回结果
		 * 161	网络定位结果，网络定位成功
		 * 162	请求串密文解析失败，一般是由于客户端SO文件加载失败造成，请严格参照开发指南或demo开发，放入对应SO文件
		 * 167	服务端定位失败，请您检查是否禁用获取位置信息权限，尝试重新请求定位
		 * 505	AK不存在或者非法，请按照说明文档重新申请AK
		 * @param addr 详细地址
		 * @param country 国家
		 * @param province 省份
		 * @param city 城市
		 * @param district 区县
		 * @param street 街道
		 * @param location 所有信息
		 */
		void onLocation(int errorCode,String addr ,String country ,String province ,String city, String district , String street, BDLocation location);
	}
}
