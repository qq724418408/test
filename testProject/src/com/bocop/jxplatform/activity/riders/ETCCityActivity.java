package com.bocop.jxplatform.activity.riders;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.boc.jx.baseUtil.asynchttpclient.JsonHttpResponseHandler;
import com.boc.jx.baseUtil.asynchttpclient.RequestParams;
import com.boc.jx.common.util.AesUtils;
import com.boc.jx.common.util.MD5Utils;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.activity.riders.ETCNetworkAppointmentActivity.MapListAdapter;
import com.bocop.jxplatform.activity.riders.ETCNetworkAppointmentActivity.MapListAdapter.HolderView;
import com.bocop.jxplatform.bean.etc.ETCMapBean;
import com.bocop.jxplatform.http.RestTemplate;

/**
 * ETC城市
 * 
 * @author gengjunying 2016-03-26
 * 
 */
public class ETCCityActivity extends Activity {
	private TextView tv_titleName;
	//返回按钮
	private ImageView imageview_title_left;
	//城市列表
	private ListView listview_etc_city;
	
	String[] cityname = {"南昌市","九江市","景德镇市","抚州市","上饶市","宜春市","新余市","鹰潭市","萍乡市","吉安市","赣州市"};
	String[] citycode = {"3601","3604","3602","3625","3623","3622","3605","3606","3603","3624","3621"};
	String[] citycheck = {"1","0","0","0","0","0","0","0","0","0","0"};
	
	List<Map<String, String>> citylist = new ArrayList<Map<String, String>>();
	private CityListAdapter adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_etc_city);
		initView();
		setListener();
		
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		//中间title
		tv_titleName = (TextView) findViewById(R.id.tv_titleName);
		tv_titleName.setText("选择城市");
				
		//返回按钮
		imageview_title_left = (ImageView) findViewById(R.id.iv_title_left);
		//城市列表
		listview_etc_city = (ListView) findViewById(R.id.listview_etc_city);
		
		initcity();
		
		adapter = new CityListAdapter();
		listview_etc_city.setAdapter(adapter);
		adapter.notifyDataSetChanged();		
	}

	private void initcity(){
		String str = getIntent().getExtras().getString("cityname");
		
		citylist.clear();
		for(int i = 0; i < cityname.length; i++){
			Map<String, String> map = new HashMap<String,String>();
			map.put("cityname", cityname[i]);
			map.put("citycode", citycode[i]);
			if(cityname[i].equals(str)){
				map.put("citycheck", "1");
			}else{
				map.put("citycheck", "0");
			}
			
			citylist.add(map);
			
		}
	}
	
	//ETC 城n查询列表适配器
		class CityListAdapter extends BaseAdapter {
			@Override
			public int getCount() {
				return citylist.size();
			}

			@Override
			public Object getItem(int position) {
				return citylist.get(position);
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
					convertView = LayoutInflater.from(ETCCityActivity.this).inflate(R.layout.item_city_list, null);				
					holderView.textview_cityname = (TextView) convertView.findViewById(R.id.textview_cityname);	
					holderView.textview_citycode = (TextView) convertView.findViewById(R.id.textview_citycode);	
					holderView.textview_citycheck = (TextView) convertView.findViewById(R.id.textview_citycheck);	
					convertView.setTag(holderView);
				} else {
					holderView = (HolderView) convertView.getTag();
				}
				
				holderView.textview_cityname.setText(citylist.get(position).get("cityname"));
				holderView.textview_citycode.setText(citylist.get(position).get("citycode"));
				
				
				if("1".equals(citylist.get(position).get("citycheck"))){
					holderView.textview_citycheck.setVisibility(View.VISIBLE);
				}else{
					holderView.textview_citycheck.setVisibility(View.INVISIBLE);
				}
				
				return convertView;
			}

			public class HolderView {
				public TextView textview_cityname;
				public TextView textview_citycode;
				public TextView textview_citycheck;
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
		
		//选择城市
		listview_etc_city.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				for(int i = 0; i < citylist.size(); i++){
					citylist.get(i).put("citycheck", "0");
				}
				
				citylist.get(arg2).put("citycheck", "1");
				adapter.notifyDataSetChanged();
				
				Intent intent = new Intent();
				intent.putExtra("cityname_selected", citylist.get(arg2).get("cityname"));
				intent.putExtra("citycode_selected", citylist.get(arg2).get("citycode"));
				ETCCityActivity.this.setResult(1, intent);
				finish();
			}
		});
		
	}


	
}
