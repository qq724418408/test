package com.bocop.jxplatform.activity.riders;
//

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;

//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.Header;
//import org.json.JSONObject;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONException;
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.baidu.mapapi.map.ItemizedOverlay;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.MyLocationData;
//import com.baidu.mapapi.model.inner.GeoPoint;
//import com.boc.jx.ab.common.pullview.AbPullToRefreshView;
//import com.boc.jx.baseUtil.asynchttpclient.JsonHttpResponseHandler;
//import com.boc.jx.baseUtil.asynchttpclient.RequestParams;
//import com.bocop.jxplatform.R;
//import com.bocop.jxplatform.activity.riders.ETCNetworkAppointmentActivity.MapListAdapter;
//import com.bocop.jxplatform.bean.etc.ETCMapBean;
//import com.bocop.jxplatform.http.RestTemplate;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.webkit.WebView;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.CompoundButton;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.RadioButton;
//import android.widget.TextView;
//import android.widget.Toast;
//
///**
// * 地图列表
// * 
// * 老版本的百度定位缺少64位的so库，face++必须要含有64，新下在的百度地图sdk 7.1 里面包含 离线定位，和基础地图，其他功能暂未下载，使用是请查阅官方文档实现
// * 
// * @author xmtang
// * 
// */

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.boc.jx.base.BaseActivity;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.etc.ETCMapBean;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MapListActivity extends FragmentActivity
		/*
		 * implements CompoundButton.OnCheckedChangeListener
		 */ implements OnClickListener {
	// private WebView webView;
	// private ImageView iv_title_left;
	// private RadioButton rbMap, rbMapList;
	// List<ETCMapBean> mapList = new ArrayList<ETCMapBean>();
	//
	// private enum TAB {
	// TAB_MAP, TAB_MAP_LIST
	// }
	//
	// private TAB tab = TAB.TAB_MAP;
	//
	// ListView mapListView;
	 MapListAdapter adapter;
	//
	// // 定位相关
	// LocationClient mLocClient;
	// MyLocationData locData = null;
	// public MyLocationListenner myListener = new MyLocationListenner();
	//
	// // 定位图层
	//// locationOverlay myLocationOverlay = null;
	// // 弹出泡泡图层
	//// private PopupOverlay pop = null;// 弹出泡泡图层，浏览节点时使用
	// private TextView popupText = null;// 泡泡view
	// private View viewCache = null;
	//
	// // 地图相关，使用继承MapView的MyLocationMapView目的是重写touch事件实现泡泡处理
	// // 如果不处理touch事件，则无需继承，直接使用MapView即可
	// MapView mMapView = null; // 地图View
	//// private MapController mMapController = null;
	//
	// boolean isRequest = false;// 是否手动触发请求定位
	// boolean isFirstLoc = true;// 是否首次定位
	// boolean isLocationClientStop = false;
	//// private MyOverlay mOverlay = null;
	//// private OverlayItem mCurItem = null;
	//// private ArrayList<OverlayItem> mItems = null;
	// private Button button = null;
	// private View popupInfo = null;
	// AbPullToRefreshView pullToRefreshView;
	private String tempJsonData = "{'errorCode':10000,'errorMsg':'操作成功','rows':[{'address':'南昌市福州路３０９号','city':'3601','county':'360102','endDate':null,'faOrganizationID':'00015','fax':null,'latitude':'28.692375','longitude':'115.910148','mobile':'079186207729','order':null,'organizationID':'09606','organizationName':'中国银行南昌市东湖支行','organizationType':'4','postcode':'330006','principal':null,'province':'36','sort':null,'sorts':null,'startDate':null,'state':null},{'address':'南昌市八一大道３７８号','city':'3601','county':null,'endDate':null,'faOrganizationID':'00015','fax':null,'latitude':'28.689289','longitude':'115.909458','mobile':'079186288341','order':null,'organizationID':'09601','organizationName':'中国银行南昌市北湖支行','organizationType':'4','postcode':'330006','principal':null,'province':'36','sort':null,'sorts':null,'startDate':null,'state':null},{'address':'南昌市红谷滩红谷中大道７８８号','city':'3601','county':null,'endDate':null,'faOrganizationID':'00015','fax':null,'latitude':'28.688512','longitude':'115.861379','mobile':'079183950146','order':null,'organizationID':'09661','organizationName':'中国银行南昌市昌北支行','organizationType':'4','postcode':'330038','principal':null,'province':'36','sort':null,'sorts':null,'startDate':null,'state':null},{'address':'南昌市广场南路４１５号','city':'3601','county':null,'endDate':null,'faOrganizationID':'00015','fax':null,'latitude':'28.657326','longitude':'115.898948','mobile':'079186212374','order':null,'organizationID':'09603','organizationName':'中国银行南昌市西湖支行','organizationType':'4','postcode':'330003','principal':null,'province':'36','sort':null,'sorts':null,'startDate':null,'state':null},{'address':'南昌市红谷滩新区红角洲片区岭口路２９９号香域尚城２３＃１１１－１１３','city':'3601','county':null,'endDate':null,'faOrganizationID':'09661','fax':null,'latitude':'28.671147','longitude':'115.84947','mobile':'079183720926','order':null,'organizationID':'09663','organizationName':'中国银行南昌市红角洲支行','organizationType':'5','postcode':'330038','principal':null,'province':'36','sort':null,'sorts':null,'startDate':null,'state':null}],'total':5}";
	//
	private TextureMapView mMapView;
	private BaiduMap mBaiduMap;
	private View rb_map, rb_maplist;
	ListView lv_map_list;
	List<ETCMapBean> mapList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maplist);
		
		findViewById(R.id.iv_title_left).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		initView();
	}

	//
	// /**
	// * 初始化控件
	// */
	private void initView() {
		// iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
		// rbMap = (RadioButton) findViewById(R.id.rb_map);
		// rbMapList = (RadioButton) findViewById(R.id.rb_maplist);
		// mapListView = (ListView) findViewById(R.id.lv_map_list);
		// rbMap.setOnCheckedChangeListener(this);
		// rbMapList.setOnCheckedChangeListener(this);
		// adapter = new MapListAdapter();
		// mapListView.setAdapter(adapter);
		// initMap();
		// getCurrentPoint(locData);
		// setListener();
		mMapView = (TextureMapView) findViewById(R.id.mTexturemap);
		mBaiduMap = mMapView.getMap();

		lv_map_list = (ListView) findViewById(R.id.lv_map_list);
		rb_map = findViewById(R.id.rb_map);
		rb_maplist = findViewById(R.id.rb_maplist);

		rb_maplist.setOnClickListener(this);
		rb_map.setOnClickListener(this);

		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);

		locationClient = new LocationClient(getApplicationContext());
		locationClient.registerLocationListener(new MyLocationListener());
		locationClient.start();

		lv_map_list.setAdapter(new MapListAdapter());
		 test();
	}
	
	
	private void test() {
		 List<ETCMapBean> tempList;
		 try {
		 tempList = JSON.parseArray(new JSONObject(tempJsonData).getString("rows"), ETCMapBean.class);
		

		
		mapList.clear();
		 mapList.addAll(tempList);
		 adapter.notifyDataSetChanged();
		// mOverlay = new MyOverlay(getResources().getDrawable(
		// R.drawable.icon_mark), mMapView);
		 for (int i = 0; i < tempList.size(); i++) {
		 ETCMapBean etcMapBean = tempList.get(i);
		 double mLat = Double.parseDouble(etcMapBean.getLatitude());
		 double mLon = Double.parseDouble(etcMapBean.getLongitude());
		 GeoPoint p = new GeoPoint((int) (mLat * 1E6),
		 (int) (mLon * 1E6));
		 String name = etcMapBean.getOrganizationName();
		 }
		 }catch(Exception e){
		 }
	}

	LocationClient locationClient;
	private boolean isFirstLoc = true;// 是否首次定位

	private class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(0).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			BitmapDescriptor bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.blue_point);
			mBaiduMap.setMyLocationConfigeration(
					new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, bitmapMarker));// marker为null
																													// 默认图标
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(20));
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng LL = new LatLng(location.getLatitude(), location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(LL);
				mBaiduMap.animateMapStatus(u);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rb_map:
			mMapView.setVisibility(View.VISIBLE);
			lv_map_list.setVisibility(View.GONE);
			break;
		case R.id.rb_maplist:
			mMapView.setVisibility(View.GONE);
			lv_map_list.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		locationClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	//
	// /**
	// * 初始化地图相关
	// */
	// private void initMap() {
	// // 地图初始化
	// mMapView = (MapView) findViewById(R.id.bmapView);
	//// mMapController = mMapView.getController();
	//// mMapView.getController().setZoom(14);
	//// mMapView.getController().enableClick(true);
	//// mMapView.setBuiltInZoomControls(true);
	//// 创建 弹出泡泡图层
	// createPaopao();
	//
	// // 定位初始化
	// mLocClient = new LocationClient(this);
	//// locData = new LocationData();
	// mLocClient.registerLocationListener(myListener);
	// LocationClientOption option = new LocationClientOption();
	// option.setOpenGps(true);// 打开gps
	// option.setCoorType("bd09ll"); // 设置坐标类型
	// option.setScanSpan(5000);
	// mLocClient.setLocOption(option);
	// mLocClient.start();
	//
	// // 定位图层初始化
	//// myLocationOverlay = new locationOverlay(mMapView);
	// // 设置定位数据
	//// myLocationOverlay.setData(locData);
	// // 添加定位图层
	//// mMapView.getOverlays().add(myLocationOverlay);
	//// myLocationOverlay.enableCompass();
	// // 修改定位数据后刷新图层生效
	//// mMapView.refresh();
	// }
	//
	// private void setListener() {
	// iv_title_left.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View arg0) {
	// finish();
	// }
	// });
	// mapListView.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	// long arg3) {
	// Intent intent = new Intent(MapListActivity.this,
	// ETCApplyActivity.class);
	// ETCMapBean etcMapBean = mapList.get(arg2);
	// intent.putExtra("orgid", etcMapBean.getOrganizationID());
	// intent.putExtra("pointname", etcMapBean.getOrganizationName());
	// intent.putExtra("phone", etcMapBean.getMobile());
	// startActivity(intent);
	// }
	// });
	// }
	//
	// @Override
	// public void onCheckedChanged(CompoundButton compoundButton, boolean arg1)
	// {
	// if (arg1) {
	// if (compoundButton == rbMap) {
	// tab = TAB.TAB_MAP;
	// mMapView.setVisibility(View.VISIBLE);
	// mapListView.setVisibility(View.GONE);
	// } else if (compoundButton == rbMapList) {
	// tab = TAB.TAB_MAP_LIST;
	// mMapView.setVisibility(View.GONE);
	// mapListView.setVisibility(View.VISIBLE);
	// getCurrentPoint(locData);
	// }
	// }
	// }
	//
	//// 继承MyLocationOverlay重写dispatchTap实现点击处理
	//// public class locationOverlay extends MyLocationOverlay {
	////
	//// public locationOverlay(MapView mapView) {
	//// super(mapView);
	//// }
	////
	//// @Override
	//// protected boolean dispatchTap() {
	//// // TODO Auto-generated method stub
	//// // 处理点击事件,弹出泡泡
	////// popupText.setBackgroundResource(R.drawable.popup);
	////// popupText.setText("当前位置");
	////// pop.showPopup(getBitmapFromView(popupText), new GeoPoint(
	////// (int) (locData.latitude * 1e6),
	////// (int) (locData.longitude * 1e6)), 8);
	//// return true;
	//// }
	////
	//// }
	//
	 class MapListAdapter extends BaseAdapter {

	@Override
	 public int getCount() {
	 return mapList.size();
	 }
	
	 @Override
	 public Object getItem(int position) {
	 return mapList.get(position);
	 }
	
	 @Override
	 public long getItemId(int position) {
	 return position;
	 }
	
	 @Override
	 public View getView(int position, View convertView, ViewGroup parent) {
	 HolderView holderView = null;
	 if (convertView == null) {
	 holderView = new HolderView();
	 convertView = LayoutInflater.from(MapListActivity.this)
	 .inflate(R.layout.item_map_list, null);
	 holderView.tvTelephone = (TextView) convertView
	 .findViewById(R.id.tv_telephone);
	 holderView.tvAddress = (TextView) convertView
	 .findViewById(R.id.tv_address);
	 holderView.tvPointName = (TextView) convertView
	 .findViewById(R.id.tv_pointname);
	 holderView.tvSq = (TextView) convertView.findViewById(R.id.tv_sq);
	 convertView.setTag(holderView);
	 } else {
	 holderView = (HolderView) convertView.getTag();
	 }
	 ETCMapBean ecEtcMapBean = mapList.get(position);
	 holderView.tvTelephone.setText(ecEtcMapBean.getMobile());
	 holderView.tvAddress.setText(ecEtcMapBean.getAddress());
	 holderView.tvPointName.setText(ecEtcMapBean.getOrganizationName());
	 holderView.tvSq.setEnabled(false);
	 return convertView;
	 }
	
	 public class HolderView {
	 public TextView tvPointName;
	 public TextView tvAddress;
	 public TextView tvTelephone;
	 public TextView tvSq;
	 }
	 }
	//
	// /**
	// * 创建弹出泡泡图层
	// */
	// public void createPaopao() {
	// // //泡泡点击响应回调
	//// PopupClickListener popListener = new PopupClickListener() {
	//// @Override
	//// public void onClickedPopup(int index) {
	//// mapListView.setVisibility(View.GONE);
	//// ETCMapBean etcMapBean = mapList.get(mItems.indexOf(mCurItem));
	//// Intent intent = new Intent(MapListActivity.this,
	//// ETCApplyActivity.class);
	//// intent.putExtra("orgid", etcMapBean.getOrganizationID());
	//// intent.putExtra("pointname", etcMapBean.getOrganizationName());
	//// intent.putExtra("phone", etcMapBean.getMobile());
	//// startActivity(intent);
	//// }
	//// };
	//// pop = new PopupOverlay(mMapView, popListener);
	// }
	//
	// /**
	// * 定位SDK监听函数
	// */
	// public class MyLocationListenner implements BDLocationListener {
	//
	// @Override
	// public void onReceiveLocation(BDLocation location) {
	// if (location == null || isLocationClientStop)
	// return;
	//
	//// locData.latitude = location.getLatitude();
	//// locData.longitude = location.getLongitude();
	//// // 如果不显示定位精度圈，将accuracy赋值为0即可
	//// locData.accuracy = 5000.0f;
	//// locData.direction = location.getDerect();
	// // 更新定位数据
	//// myLocationOverlay.setData(locData);
	// // 更新图层数据执行刷新后生效
	//// mMapView.refresh();
	// // 是手动触发请求或首次定位时，移动到定位点
	// // if (isRequest || isFirstLoc) {
	// // 移动地图到定位点
	//// mMapController.animateTo(new GeoPoint(
	//// (int) (locData.latitude * 1e6),
	//// (int) (locData.longitude * 1e6)));
	// isRequest = false;
	// mMapView.setVisibility(View.VISIBLE);
	// // }
	// // 首次定位完成
	// // isFirstLoc = false;
	// //getCurrentPoint(locData);
	//// test();
	//
	// // ETCMapBean etcMapBean = new ETCMapBean();
	// // etcMapBean.setAddress("12333");
	// // etcMapBean.setCity("上海");
	// // mapList.add(etcMapBean);
	// // mapList.add(new ETCMapBean());
	// // mapList.add(new ETCMapBean());
	// // mapList.add(new ETCMapBean());
	// // mapList.add(new ETCMapBean());
	// // adapter.notifyDataSetChanged();
	// mLocClient.stop();
	// }
	//
	// public void onReceivePoi(BDLocation poiLocation) {
	// if (poiLocation == null) {
	// return;
	// }
	// }
	// }
	//
	// /**
	// * 获取当前网点
	// */
	// //
	// http://22.220.13.64:8080/manage/org/getNearbyPOI?lng=115.871059&lat=28.693196&r=5000
	// private void getCurrentPoint(MyLocationData locData) {
	// RestTemplate restTemplate = new RestTemplate(this);
	// RequestParams params = new RequestParams();
	// params.put("lng", locData.longitude);
	// params.put("lat", locData.latitude);
	// params.put("r", 5000);
	// //
	// restTemplate.get("http://123.124.191.179/etc/org/getNearbyPOI", params,
	// new JsonHttpResponseHandler("UTF-8") {
	// @Override
	// public void onStart() {
	// }
	//
	// @Override
	// public void onSuccess(int statusCode, Header[] headers,
	// JSONObject response) {
	// if (null != response) {
	// System.out.println("nihao-======"
	// + response.toString());
	// try {
	// int status = Integer.parseInt(response
	// .getString("errorCode"));
	// switch (status) {
	// case 10000:
	// List<ETCMapBean> tempList = JSON
	// .parseArray(
	// response.getString("rows"),
	// ETCMapBean.class);
	// if (tempList != null && tempList.size() > 0) {
	// mapList.clear();
	// mapList.addAll(tempList);
	// adapter.notifyDataSetChanged();
	//// mOverlay = new MyOverlay(getResources()
	//// .getDrawable(
	//// R.drawable.icon_mark),
	//// mMapView);
	// for (int i = 0; i < tempList.size(); i++) {
	// ETCMapBean etcMapBean = tempList
	// .get(i);
	// double mLat = Double
	// .parseDouble(etcMapBean
	// .getLatitude());
	// double mLon = Double
	// .parseDouble(etcMapBean
	// .getLongitude());
	// GeoPoint p = new GeoPoint(
	// (int) (mLat * 1E6),
	// (int) (mLon * 1E6));
	// String name = etcMapBean
	// .getOrganizationName();
	//// OverlayItem item = new OverlayItem(
	//// p, name, "");
	//// mOverlay.addItem(item);
	// }
	// /**
	// * 保存所有item，以便overlay在reset后重新添加
	// */
	//// mItems = new ArrayList<OverlayItem>();
	//// mItems.addAll(mOverlay.getAllItem());
	// /**
	// * 将overlay 添加至MapView中
	// */
	//// mMapView.getOverlays().add(mOverlay);
	// /**
	// * 刷新地图
	// */
	//// mMapView.refresh();
	// /**
	// * 向地图添加自定义View.
	// */
	// viewCache = getLayoutInflater()
	// .inflate(
	// R.layout.custom_text_view,
	// null);
	// popupInfo = viewCache
	// .findViewById(R.id.popinfo);
	// popupText = (TextView) viewCache
	// .findViewById(R.id.textcache);
	// button = new Button(
	// MapListActivity.this);
	// button.setBackgroundResource(R.drawable.popup);
	// } else {
	// Toast.makeText(MapListActivity.this,
	// "没有数据！", 0).show();
	// }
	// break;
	// default:
	// break;
	// }
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// @Override
	// public void onFailure(int statusCode, Header[] headers,
	// Throwable throwable, JSONObject errorResponse) {
	// super.onFailure(statusCode, headers, throwable,
	// errorResponse);
	// Toast.makeText(MapListActivity.this, "请求失败！", 0).show();
	// }
	// });
	// }
	//
	
	// /**
	// * 保存所有item，以便overlay在reset后重新添加
	// */
	//// mItems = new ArrayList<OverlayItem>();
	//// mItems.addAll(mOverlay.getAllItem());
	// /**
	// * 将overlay 添加至MapView中
	// */
	//// mMapView.getOverlays().add(mOverlay);
	// /**
	// * 刷新地图
	// */
	//// mMapView.refresh();
	// /**
	// * 向地图添加自定义View.
	// */
	// viewCache = getLayoutInflater().inflate(R.layout.custom_text_view,
	// null);
	// popupInfo = viewCache.findViewById(R.id.popinfo);
	// popupText = (TextView) viewCache.findViewById(R.id.textcache);
	// button = new Button(MapListActivity.this);
	// button.setBackgroundResource(R.drawable.popup);
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	//
	//// public class MyOverlay extends ItemizedOverlay {
	//// public MyOverlay(Drawable defaultMarker, MapView mapView) {
	//// super(defaultMarker, mapView);
	//// }
	////
	//// @Override
	//// public boolean onTap(int index) {
	//// OverlayItem item = getItem(index);
	//// mCurItem = item;
	//// popupText.setText(item.getTitle());
	//// Bitmap[] bitMaps = { getBitmapFromView(popupInfo) };
	//// pop.showPopup(bitMaps, item.getPoint(), 32);
	//// mMapView.getController().animateTo(item.getPoint());
	//// return true;
	//// }
	////
	//// @Override
	//// public boolean onTap(GeoPoint pt, MapView mMapView) {
	//// if (pop != null) {
	//// pop.hidePop();
	//// mMapView.removeView(button);
	//// }
	//// return false;
	//// }
	//// }
	//
	// /**
	// * 从view 得到图片
	// *
	// * @param view
	// * @return
	// */
	// public Bitmap getBitmapFromView(View view) {
	// view.destroyDrawingCache();
	// view.measure(View.MeasureSpec.makeMeasureSpec(0,
	// View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
	// .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
	// view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
	// view.setDrawingCacheEnabled(true);
	// Bitmap bitmap = view.getDrawingCache(true);
	// return bitmap;
	// }
	//
	// @Override
	// public void onPause() {
	// isLocationClientStop = true;
	// mMapView.setVisibility(View.INVISIBLE);
	// mMapView.onPause();
	// super.onPause();
	// }
	//
	// @Override
	// public void onResume() {
	// isLocationClientStop = false;
	// if (rbMap.isChecked()) {
	// mMapView.setVisibility(View.VISIBLE);
	// } else {
	// mMapView.setVisibility(View.GONE);
	// mapListView.setVisibility(View.VISIBLE);
	// }
	// mMapView.onResume();
	// super.onResume();
	// }
	//
	// @Override
	// public void onDestroy() {
	// // 退出时销毁定位
	// if (mLocClient != null)
	// mLocClient.stop();
	// isLocationClientStop = true;
	// mMapView.onDestroy();
	// super.onDestroy();
	// }
	//
	// @Override
	// public void onSaveInstanceState(Bundle outState) {
	// super.onSaveInstanceState(outState);
	// mMapView.onSaveInstanceState(outState);
	//
	// }
	//
	// @Override
	// protected void onRestoreInstanceState(Bundle savedInstanceState) {
	// super.onRestoreInstanceState(savedInstanceState);
	//// mMapView.onRestoreInstanceState(savedInstanceState);
	// }
}
