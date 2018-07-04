package com.bocop.jxplatform.activity.riders;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.boc.jx.baseUtil.asynchttpclient.JsonHttpResponseHandler;
import com.boc.jx.baseUtil.asynchttpclient.RequestParams;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.etc.ETCMapBean;
import com.bocop.jxplatform.http.RestTemplate;

/**
 * ETC网点预约查询
 * 
 * @author gengjunying 2016-03-26
 * 
 */
public class  ETCNetworkAppointmentActivity extends Activity {
	private TextView tv_titleName;
	//返回按钮
	private ImageView imageview_title_left;
	//选择城市
	private TextView textview_title_right;
	//ETC网点列表
	private ListView listview_etc_netwerk;
	//加载进度条
	private ProgressDialog progressDialog;
	
	private String citycode = "3601";
	
	private MapListAdapter adapter;
	List<ETCMapBean> mapList = new ArrayList<ETCMapBean>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_network_appointment);
		initView();
		setListener();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		//中间title
		tv_titleName = (TextView) findViewById(R.id.tv_titleName);
		tv_titleName.setText("江西各市ETC网点查询");
		
		//返回按钮
		imageview_title_left = (ImageView) findViewById(R.id.iv_title_left);
		
		//选择市
		textview_title_right = (TextView) findViewById(R.id.tv_title_right);
		textview_title_right.setVisibility(View.VISIBLE);
		textview_title_right.setText("南昌市");
		textview_title_right.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		textview_title_right.getPaint().setAntiAlias(true);
		
		//ETC网点
		listview_etc_netwerk = (ListView)findViewById(R.id.listview_etc_network);
		
		adapter = new MapListAdapter();
		listview_etc_netwerk.setAdapter(adapter);
		getNearAreaETCNetwork(citycode);
	}
	
	//获取ETC网点信息
	private void getNearAreaETCNetwork(String citycode) {
		RestTemplate restTemplate = new RestTemplate(this);
		RequestParams params = new RequestParams();
		params.put("r", 5000);
		
		restTemplate.get("http://123.124.191.179/etc/org/getPOIByAreaCode?areaCode="+citycode, params,
			new JsonHttpResponseHandler("UTF-8") {			
				@Override
				public void onStart() {
					
				}
	
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					if (null != response) {						
						try {
							int status = Integer.parseInt(response.getString("errorCode"));
							
							switch (status) {
							case 10000:
								List<ETCMapBean> tempList = JSON.parseArray(response.getString("rows"), ETCMapBean.class);							
								if (tempList != null && tempList.size() > 0) {
									mapList.clear();
									mapList.addAll(tempList);
									adapter.notifyDataSetChanged();														
								} else {
									Toast.makeText(ETCNetworkAppointmentActivity.this, "没有数据！", 0).show();
								}
								break;
							default:
								break;
							}
	
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
	
				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					super.onFailure(statusCode, headers, throwable,
							errorResponse);
					Toast.makeText(ETCNetworkAppointmentActivity.this, "请求失败！", 0).show();
				}
			});
	}
	
	
	//ETC 网点查询列表适配器
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
				convertView = LayoutInflater.from(ETCNetworkAppointmentActivity.this).inflate(R.layout.item_map_list, null);				
				holderView.tvTelephone = (TextView) convertView.findViewById(R.id.tv_telephone);				
				holderView.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
				holderView.tvPointName = (TextView) convertView.findViewById(R.id.tv_pointname);
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


	/**
	 * 设置监听
	 */
	private void setListener() {
		imageview_title_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		//城市选择
		textview_title_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ETCNetworkAppointmentActivity.this, ETCCityActivity.class);
				intent.putExtra("cityname", textview_title_right.getText().toString().trim());
				startActivityForResult(intent, 1);
			}
		});
		
		//ETC网点点击事件
		listview_etc_netwerk.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(ETCNetworkAppointmentActivity.this,
						ETCApplyActivity.class);
				ETCMapBean etcMapBean = mapList.get(arg2);
				intent.putExtra("orgid", etcMapBean.getOrganizationID());
				intent.putExtra("pointname", etcMapBean.getOrganizationName());
				intent.putExtra("phone", etcMapBean.getMobile());
				startActivity(intent);
			}
		});
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(null != data){
			textview_title_right.setText(data.getExtras().getString("cityname_selected"));
			getNearAreaETCNetwork(data.getExtras().getString("citycode_selected"));
		}
	}
	
}
