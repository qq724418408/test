/*
 * Copyright 2014 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bocop.jxplatform.adapter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.CarPeccancyBean;
import com.bocop.jxplatform.bean.PeccancyXmlForPayBean;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.trafficassistant.TrafficPayActivity;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.jxplatform.xml.CspRecForQuickPayQuery;
import com.bocop.jxplatform.xml.CspXmlForQuickPayQuery;
import com.nhaarman.listviewanimations.itemmanipulation.expandablelistitem.ExpandableListItemAdapter;

public class MyPayWaitingAdapter extends
		ExpandableListItemAdapter<Integer> {

	public final Context mContext;
	public List<CarPeccancyBean> mlistDates;
	public String strLicenseNum;

	/**
	 * Creates a new ExpandableListItemAdapter with the specified list, or an
	 * empty list if items == null.
	 */
	public MyPayWaitingAdapter(final Context context,
			List<CarPeccancyBean> listDates) {
		super(context, R.layout.item_mypeccancy,
				R.id.fl_mypeccancytitle,
				R.id.fl_mypeccancycontext);
		mContext = context;
		mlistDates = listDates;

		for (int i = 0; i < mlistDates.size(); i++) {
			add(i);
		}
	}

	@NonNull
	@Override
	public View getTitleView(final int position, View convertView,
			@NonNull final ViewGroup parent) {
		LinearLayout ly = (LinearLayout) convertView;
		if (ly == null) {
			ly = new LinearLayout(mContext);
			ly = (LinearLayout) LayoutInflater.from(mContext).inflate(
					R.layout.item_mypeccancy_list_title, parent, false);
		}
		Log.i("tag", "tv_mypeccancy_num" + mlistDates.get(position).getPeccancyNum());
		((TextView)ly.findViewById(R.id.tv_mypeccancy_num)).setText(mlistDates.get(position).getPeccancyNum());
		Log.i("tag", "tv_mypeccancy_score" + mlistDates.get(position).getPeccancyScore());
		((TextView)ly.findViewById(R.id.tv_mypeccancy_score)).setText(mlistDates.get(position).getPeccancyScore());
		Log.i("tag", "tv_mypeccancy_money" + mlistDates.get(position).getPeccancyMoney());
		((TextView)ly.findViewById(R.id.tv_mypeccancy_money)).setText(mlistDates.get(position).getPeccancyMoney());

		return ly;

	}
	
	@NonNull
	@Override
	public View getContentView(final int position, View convertView,
			@NonNull final ViewGroup parent) {
		Log.d("tag", "content_start");
		View ly = (LinearLayout) convertView;

		if (ly == null) {
			ly = new LinearLayout(mContext);
			ly = (LinearLayout) LayoutInflater.from(mContext).inflate(
					R.layout.item_mypeccancy_list_context, parent, false);
		}
		Log.i("tag", "getPeccancyLicenseNum" + mlistDates.get(position).getPeccancyLicenseNum());
		((TextView)ly.findViewById(R.id.tv_mypeccancy_licensenum)).setText(mlistDates.get(position).getPeccancyLicenseNum());
		
		Log.i("tag", "getPeccancyTime" + mlistDates.get(position).getPeccancyTime());
		((TextView)ly.findViewById(R.id.tv_mypeccancy_time)).setText(mlistDates.get(position).getPeccancyTime());
		
		Log.i("tag", "getPeccancyPlace" + mlistDates.get(position).getPeccancyPlace());
		((TextView)ly.findViewById(R.id.tv_mypeccancy_place)).setText(mlistDates.get(position).getPeccancyPlace());
		
		Log.i("tag", "getPeccancyType" + mlistDates.get(position).getPeccancyType());
		((TextView)ly.findViewById(R.id.tv_mypeccancy_type)).setText(mlistDates.get(position).getPeccancyType());
		
		Log.i("tag", "getPeccancyStatus" + mlistDates.get(position).getPeccancyStatus());
		((TextView)ly.findViewById(R.id.tv_mypeccancy_status)).setText(mlistDates.get(position).getPeccancyStatus());
		
		Log.i("tag", "getBornTime" + mlistDates.get(position).getBornTime());
		((TextView)ly.findViewById(R.id.tv_mypeccancy_borntime)).setText(mlistDates.get(position).getBornTime());
		
		Log.i("tag", "getPeccancyAct" + mlistDates.get(position).getPeccancyAct());
		((TextView)ly.findViewById(R.id.tv_mypeccancy_act)).setText(mlistDates.get(position).getPeccancyAct());
		
		
		
		
		
		
		Bundle bundle = new Bundle();
		bundle.putString("licenseNum", mlistDates.get(position).getPeccancyNum());
		
		Button btWaitPayMyPeccancy = new Button(mContext);
		btWaitPayMyPeccancy = (Button) ly.findViewById(R.id.bt_waitingpay_mypeccancy);
		btWaitPayMyPeccancy.setTag(bundle);
		
		btWaitPayMyPeccancy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle = (Bundle)v.getTag();
				strLicenseNum = bundle.getString("licenseNum");
				Log.i("tag", strLicenseNum);
				requestCspForPayBalance();
//				bundle.putString("payOrder", "9876367765788236");
//				Intent intent = new Intent(mContext, TrafficPayActivity.class);
//				intent.putExtras(bundle);
//				mContext.startActivity(intent);
			}
		});
		
		return ly;
	}

	
	protected void requestCspForPayBalance() {
		// TODO Auto-generated method stub
		try {
			//生成CSP XML报文
			CspXmlForQuickPayQuery cspXmlForQuickPayQuery = new CspXmlForQuickPayQuery(strLicenseNum);
			String strXml = cspXmlForQuickPayQuery.getCspXml();
			//生成MCIS报文
			Mcis mcis = new Mcis(strXml,TransactionValue.EB4034);
			final byte[] byteMessage = mcis.getMcis();
			CspUtil cspUtil = new CspUtil(mContext);
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			cspUtil.postCspLogin(byteMessage, new CallBack() {
				@Override
				public void onSuccess(String responStr) {
					PeccancyXmlForPayBean peccancyXmlForPayBean = null;
					try {
						 peccancyXmlForPayBean = CspRecForQuickPayQuery.readStringXml(responStr);
						 if(!peccancyXmlForPayBean.getErrorcode().equals("00")){
							 Toast.makeText(mContext,peccancyXmlForPayBean.getErrormsg() , Toast.LENGTH_LONG).show();
						 }
						 else{
							 Log.i("tag", "成功接收CSP信息");
								Bundle bundle = new Bundle();
								bundle.putString("peccancyNum", peccancyXmlForPayBean.getPeccancyNum());
								bundle.putString("peccancySum", peccancyXmlForPayBean.getPeccancySum());
								bundle.putString("licenseNum", peccancyXmlForPayBean.getLicenseNum());
								bundle.putString("lateAmt", peccancyXmlForPayBean.getLateAmt());
								bundle.putString("yjAmt", peccancyXmlForPayBean.getYjAmt());
								Intent intent = new Intent(mContext, TrafficPayActivity.class);
								intent.putExtras(bundle);
								mContext.startActivity(intent);
						 }
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void onFinish() {
				}
				
				@Override
				public void onFailure(String responStr) {
					Log.i("tag2", "2onFailure");
					CspUtil.onFailure(mContext, responStr);
				}
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}