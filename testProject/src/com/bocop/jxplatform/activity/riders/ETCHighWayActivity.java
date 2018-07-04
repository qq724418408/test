package com.bocop.jxplatform.activity.riders;

import com.bocop.jxplatform.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 网点预约申请
 * 
 * @author gengjunying 2016-03-27
 *
 */
public class ETCHighWayActivity extends Activity {
		//标题
	    private TextView tvTitle;
	    //返回按钮
	    private ImageView ivLeft;
	    //ETC介绍 
	    private TextView textview_etc_introduction;
	    //ETC优惠活动  
	    private TextView textview_etc_favourable;
	    //附近ETC网点  
	    private TextView textview_etc_network;
	    //江西各市ETC网点预约申请
	    private TextView textview_etc_network_appointment;
	    
	    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
        	setContentView(R.layout.activity_etc_highway);
        	initView();
        	setListener();
        }

		/**
         * 初始化控件
         */
		private void initView() {		
			tvTitle = (TextView) findViewById(R.id.tv_titleName);
			ivLeft = (ImageView) findViewById(R.id.iv_title_left);
			tvTitle.setText("中国银行信用卡+ETC");
			
			//ETC介绍 
		    textview_etc_introduction = (TextView) findViewById(R.id.textview_etc_introduction);
		    //ETC优惠活动  
		    textview_etc_favourable = (TextView) findViewById(R.id.textview_etc_favourable);
		    //附近ETC网点  
		    textview_etc_network = (TextView) findViewById(R.id.textview_etc_network);
		    //江西各市ETC网点预约申请
		    textview_etc_network_appointment = (TextView) findViewById(R.id.textview_etc_network_appointment);
		}
		
	    
	    
	    private void setListener(){
	    	//返回    按钮事件
	    	ivLeft.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
	    	
	    	//ETC介绍    按钮事件
	    	textview_etc_introduction.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(ETCHighWayActivity.this, ETCFavourableIntroducesActivity.class);
					startActivity(intent);
				}
			});
	    	
	    	//ETC优惠活动    按钮事件
	    	textview_etc_favourable.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(ETCHighWayActivity.this, ETCFavourablePromotionActivity.class);
					startActivity(intent);
				}
			});
	    	
	    	//附近ETC网点    按钮事件
	    	textview_etc_network.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(ETCHighWayActivity.this, MapListActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			});
	    	
	    	//江西各市ETC网点预约申请   按钮事件
	    	textview_etc_network_appointment.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(ETCHighWayActivity.this, ETCNetworkAppointmentActivity.class);
					startActivity(intent);
				}
			});
	    }
	    
	    
	    
}








