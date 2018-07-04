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

import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.CarPeccancyBean;
import com.bocop.jxplatform.bean.LicenseInfoBean;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.trafficassistant.LicenseInfoActivity;
import com.bocop.jxplatform.trafficassistant.TrafficViolationActivity;
import com.bocop.jxplatform.util.ActivityForResultCode;
import com.bocop.jxplatform.util.BocopDialog;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.jxplatform.xml.CspRecForSimpleLicenseInfo;
import com.bocop.jxplatform.xml.CspXmlAPJJ12;
import com.nhaarman.listviewanimations.itemmanipulation.expandablelistitem.ExpandableListItemAdapter;
import com.readystatesoftware.viewbadger.BadgeView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyExpandableListItemAdapter extends
		ExpandableListItemAdapter<Integer> {

	public final Context mContext;
	public List<CarPeccancyBean> mlistDates;
	LicenseInfoBean licenseInfoBean;
	String strFlag = "未明";
	String flag = "";
	String flag1 = "";

	/**
	 * Creates a new ExpandableListItemAdapter with the specified list, or an
	 * empty list if items == null.
	 */
	public MyExpandableListItemAdapter(final Context context,
			List<CarPeccancyBean> listDates) {
		// super(context, R.layout.peccancy_list_item, R.id.fl_car_peccancy_fir,
		// R.id.fl_car_peccancy_sec);
		super(context, R.layout.activity_expandablelistitem_card,
				R.id.activity_expandablelistitem_card_title,
				R.id.activity_expandablelistitem_card_content);
		mContext = context;
		mlistDates = listDates;

		for (int i = 0; i < mlistDates.size(); i++) {
			add(i);
		}
		Log.d("tag", "for" + String.valueOf(mlistDates.size()));
	}

	@NonNull
	@Override
	public View getTitleView(final int position, View convertView,
			@NonNull final ViewGroup parent) {
		Log.d("tag", "title_start");
		LinearLayout ly = (LinearLayout) convertView;
		// llt_timeforbadgeview
		BadgeView mBadgeView;
		LinearLayout badgeLy = null;

		if (ly == null) {
			ly = new LinearLayout(mContext);
			ly = (LinearLayout) LayoutInflater.from(mContext).inflate(
					R.layout.peccancy_list_fir_item, parent, false);
		}
		// badgeLy = (LinearLayout) ly.findViewById(R.id.llt_timeforbadgeview);
		badgeLy = (LinearLayout) ly.findViewById(R.id.fl_car_peccancy_fir);

		((TextView) ly.findViewById(R.id.tv_peccancy_time)).setText(mlistDates
				.get(position).getPeccancyTime());
		mBadgeView = new BadgeView(mContext, badgeLy);
		mBadgeView.setText("点我");
		((TextView) ly.findViewById(R.id.tv_peccancy_place)).setText(mlistDates
				.get(position).getPeccancyPlace());
		((TextView) ly.findViewById(R.id.tv_peccancy_code)).setText(mlistDates
				.get(position).getPeccancyType());

		flag = mlistDates.get(position).getPeccancyFlag().trim(); // 0:不可以处理,1:可以处理
		flag1 = mlistDates.get(position).getPeccancyFlag1().trim(); // 0:未处理,1:已处理
		if (flag.equals("1") && flag1.equals("0")) {
			Log.i("tag", "add view");
			// if (mBadgeView != null)
			// {
			// badgeLy.removeView(mBadgeView);
			// Log.i("tag", "removeView");
			// }
			// badgeLy.addView(mBadgeView);
			// Log.i("tag", "addView");
			mBadgeView.show();
		}
		return ly;

	}

	@NonNull
	@Override
	public View getContentView(final int position, View convertView,
			@NonNull final ViewGroup parent) {
		Log.d("tag", "content_start");
		LinearLayout ly = (LinearLayout) convertView;
		// llt_car_per

		if (ly == null) {
			ly = new LinearLayout(mContext);
			ly = (LinearLayout) LayoutInflater.from(mContext).inflate(
					R.layout.peccancy_list_sec_item, parent, false);
		}
		((TextView) ly.findViewById(R.id.tv_peccancy_act)).setText(String
				.valueOf(mlistDates.get(position).getPeccancyAct()));
		((TextView) ly.findViewById(R.id.tv_peccancy_money)).setText(String
				.valueOf(mlistDates.get(position).getPeccancyMoney()));
		((TextView) ly.findViewById(R.id.tv_peccancy_score)).setText(String
				.valueOf(mlistDates.get(position).getPeccancyScore()));
		((TextView) ly.findViewById(R.id.tv_peccancy_org)).setText(mlistDates
				.get(position).getPeccancyOrg());
		Log.i("tag", "switch");
		flag = mlistDates.get(position).getPeccancyFlag().trim(); // 0:不可以处理,1:可以处理
		flag1 = mlistDates.get(position).getPeccancyFlag1().trim(); // 0:未处理,1:已处理
		switch (Integer.valueOf(flag1)) {
		case 0:
			strFlag = "未处理";
			break;
		case 1:
			strFlag = "已处理";
			break;
		default:
			strFlag = "状态未明";
		}
		((TextView) ly.findViewById(R.id.tv_peccancy_flag)).setText(strFlag);
		Bundle bundle = new Bundle();
		bundle.putString("violationSequence", mlistDates.get(position)
				.getPeccancySequenceNum());
		bundle.putString("violationTime", mlistDates.get(position)
				.getPeccancyTime());
		bundle.putString("violationPlace", mlistDates.get(position)
				.getPeccancyPlace());
		bundle.putString("violationAct", mlistDates.get(position)
				.getPeccancyAct());
		bundle.putString("violationScore", mlistDates.get(position)
				.getPeccancyScore());
		bundle.putString("violationMoney", mlistDates.get(position)
				.getPeccancyMoney());
		bundle.putString("violationOrg", mlistDates.get(position)
				.getPeccancyOrg());
		bundle.putString("violationCode", mlistDates.get(position)
				.getPeccancyType());
		bundle.putString("violationLicenseNum", mlistDates.get(position)
				.getPeccancyLicenseNum());

		LinearLayout lltBtAndText = (LinearLayout) ly
				.findViewById(R.id.llt_btandtext);
		Button bt = (Button) ly.findViewById(R.id.bt_car_pec_add);
		bt.setTag(bundle);
		TextView tv = (TextView) ly.findViewById(R.id.tv_car_pec_att1);

		// 给按钮增加监听事件
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Bundle bundleTag = (Bundle) v.getTag();
				try {// 生成CSP XML报文
					CspXmlAPJJ12 cspXmlForCarList = new CspXmlAPJJ12(LoginUtil
							.getUserId(mContext));
					String strXml = cspXmlForCarList.getCspXml();
					// 生成MCIS报文
					Mcis mcis = new Mcis(strXml, TransactionValue.APJJ12);
					final byte[] byteMessage = mcis.getMcis();
					// 发送报文
					CspUtil cspUtil = new CspUtil(mContext);
					Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
					cspUtil.postCspLogin(byteMessage, new CallBack() {

						@Override
						public void onSuccess(String responStr) {
							// 模拟已经绑定驾驶证，跳转认罚阶段，如未绑定，跳转绑定驾驶证。
							try {
								licenseInfoBean = CspRecForSimpleLicenseInfo
										.readStringXml(responStr);
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							if (!licenseInfoBean.getErrorcode().equals("00")) {
								BocopDialog dialog = new BocopDialog(mContext,
										"提示", "您还没有绑定驾驶证，是否现在去绑定？");
								dialog.setPositiveButton(
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
												Intent intent = new Intent(
														mContext,
														LicenseInfoActivity.class);
												((FragmentActivity) mContext)
														.startActivityForResult(
																intent,
																ActivityForResultCode.CodeForCarPeccancy);
											}
										}, "绑定");
								dialog.setNegativeButton(
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
											}
										}, "取消");
								dialog.show();
							} else {
								String strLicenseNum = licenseInfoBean
										.getDrivenum();
								bundleTag.putString("violationDriveNUm",
										strLicenseNum);
								Intent intent = new Intent(mContext,
										TrafficViolationActivity.class);
								intent.putExtras(bundleTag);
								mContext.startActivity(intent);
							}
						}

						@Override
						public void onFinish() {

						}

						@Override
						public void onFailure(String responStr) {
							if (responStr.equals("0")) {
								Toast.makeText(mContext, "网络不给力",
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(mContext, responStr,
										Toast.LENGTH_SHORT).show();
							}
						}
					});
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});
		// 判断是否符合交通违规简易处罚天剑
		// int score =
		// Integer.valueOf(mlistDates.get(position).getPeccancyScore());
		// if (score > 6) {
		// bt.setVisibility(View.GONE);
		// tv.setText("此条违章信息请您到就近交警大队进行处理");
		// }
		// flag 0:不可以处理,1:可以处理
		// flag1 0:未处理,1:已处理
		if (flag.equals("0") && flag1.equals("1")) {
			// lltBtAndText.setVisibility(View.GONE);
			bt.setVisibility(View.GONE);
			tv.setText("此违法记录已经处理，请在“违章缴费”或“我的违章”处缴纳罚款");
		} else if (flag.equals("0") && flag1.equals("0")) {
			bt.setVisibility(View.GONE);
			tv.setText("此条违章信息请您到就近交警大队进行处理");
		} else {

		}
		return ly;
	}

}