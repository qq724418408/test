package com.bocop.xms.fragment;

import com.boc.jx.base.BaseFragment;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.constants.Constants;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.activity.CreditCardActivity;
import com.bocop.jxplatform.activity.WebActivity;
import com.bocop.jxplatform.activity.WebViewActivity;
import com.bocop.jxplatform.adapter.XmsItemAdapter;
import com.bocop.jxplatform.util.BocopDialog;
import com.bocop.jxplatform.util.CustomProgressDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * 
 * 第三方服务
 * 
 * @author ftl
 * 
 */
public class ThridServiceFragment extends BaseFragment {

	@ViewInject(R.id.gvJRService)
	private GridView gvJRService;// 金融服务
	@ViewInject(R.id.gvDailyLife)
	private GridView gvDailyLife;// 日常生活
	@ViewInject(R.id.gvNecessary)
	private GridView gvNecessary;// 出门必备

	private XmsItemAdapter gvJRAdapter;
	private XmsItemAdapter dailyLifeAdapter;
	private XmsItemAdapter necessaryAdapter;
	private int flag = 0;
	private BocopDialog dialog;// 免责申明

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = initView(R.layout.xms_fragment_thrid_service);
		return view;
	}

	@Override
	protected void initData() {
		super.initData();
		dailyLifeAdapter = new XmsItemAdapter(baseActivity,
				R.array.xmsotherser, "2");
		gvDailyLife.setAdapter(dailyLifeAdapter);

		necessaryAdapter = new XmsItemAdapter(baseActivity,
				R.array.xmsnecessary, "3");
		gvNecessary.setAdapter(necessaryAdapter);

		gvJRAdapter = new XmsItemAdapter(baseActivity, R.array.xmsjrservice,
				"4");
		gvJRService.setAdapter(gvJRAdapter);

		dialog = new BocopDialog(
				baseActivity,
				"免责声明",
				"\t\t秘书通引用的第三方服务网址、商户及商品信息，意在为客户提供方便快捷的服务链接，所有服务均由第三方服务商提供，相关服务和责任将由第三方承担，如在使用服务的过程中有任何问题和纠纷，请您直接与第三方服务商沟通解决。");
	}

	@Override
	protected void initView() {
		super.initView();
		gvJRService.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				if (baseActivity.getBaseApp().isNetStat()) {
					if (!"".equals(gvJRAdapter.getItem(position).getName())) { 
						if (flag == 1) {
							switchJRService(position);
						} else {
							dialog.setPositiveButton(
									new DialogInterface.OnClickListener() {
	
										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											flag = 1;
											switchJRService(position);
											dialog.cancel();
										}
									}, "同意");
							dialog.setNegativeButton(
									new DialogInterface.OnClickListener() {
	
										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											dialog.cancel();
										}
									}, "不同意");
							if (!baseActivity.isFinishing()) {
								dialog.show();
							}
						}
					}
				} else {
					CustomProgressDialog.showBocNetworkSetDialog(baseActivity);
				}
			}
		});
		gvDailyLife.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				if (baseActivity.getBaseApp().isNetStat()) {
					if (!"".equals(dailyLifeAdapter.getItem(position).getName())) { 
						if (flag == 1) {
							switchDailyLife(position);
						} else {
							dialog.setPositiveButton(
									new DialogInterface.OnClickListener() {
	
										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											flag = 1;
											switchDailyLife(position);
											dialog.cancel();
										}
									}, "同意");
							dialog.setNegativeButton(
									new DialogInterface.OnClickListener() {
	
										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											dialog.cancel();
										}
									}, "不同意");
							if (!baseActivity.isFinishing()) {
								dialog.show();
							}
						}
					}
				} else {
					CustomProgressDialog.showBocNetworkSetDialog(baseActivity);
				}
			}
		});

		gvNecessary.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				if (baseActivity.getBaseApp().isNetStat()) {
					if (!"".equals(necessaryAdapter.getItem(position).getName())) { 
						if (flag == 1) {
							switchNecessary(position);
						} else {
							dialog.setPositiveButton(
									new DialogInterface.OnClickListener() {
	
										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											flag = 1;
											switchNecessary(position);
											dialog.cancel();
										}
									}, "同意");
							dialog.setNegativeButton(
									new DialogInterface.OnClickListener() {
	
										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											dialog.cancel();
										}
									}, "不同意");
							if (!baseActivity.isFinishing()) {
								dialog.show();
							}
						}
					}
				} else {
					CustomProgressDialog.showBocNetworkSetDialog(baseActivity);
				}
			}
		});
	}

	private void switchJRService(int position) {
		switch (position) {
		// ATM分布
		case 0:
			Bundle bundleATM = new Bundle();
			bundleATM.putString("url", Constants.xmsUrlForATM);
			bundleATM.putString("name", "ATM分布");
			Intent intentATM = new Intent(baseActivity, WebViewActivity.class);
			intentATM.putExtras(bundleATM);
			startActivity(intentATM);
			break;
		// 网点查询
		case 1:
			Bundle bundleOrg = new Bundle();
			bundleOrg.putString("url", Constants.xmsUrlForOrg);
			bundleOrg.putString("name", "网点查询");
			Intent intentOrg = new Intent(baseActivity, WebActivity.class);
			intentOrg.putExtras(bundleOrg);
			startActivity(intentOrg);
			break;
		// 网点预约
		case 2:
			Bundle bundle = new Bundle();
			bundle.putString("url", Constants.xmsUrlForDotbooking);
			bundle.putString("name", "网点预约");
			Intent intentDotbook = new Intent(baseActivity, WebActivity.class);
			intentDotbook.putExtras(bundle);
			startActivity(intentDotbook);
			break;
		// 信用卡积分
		case 3:
			Intent intentDebitCard = new Intent(baseActivity,
					CreditCardActivity.class);
			startActivity(intentDebitCard);
			break;
		// 存贷款利率
		case 4:

			Bundle bundleRate = new Bundle();
			bundleRate.putString("url", Constants.xmsUrlForRate);
			bundleRate.putString("name", "存贷款利率");
			Intent intentRate = new Intent(baseActivity, WebActivity.class);
			intentRate.putExtras(bundleRate);
			startActivity(intentRate);
			break;
		// 外币牌价
		case 5:
			Bundle bundleExchange = new Bundle();
			bundleExchange.putString("url", Constants.xmsUrlForExchange);
			bundleExchange.putString("name", "外币牌价");
			Intent intentExchange = new Intent(baseActivity, WebActivity.class);
			intentExchange.putExtras(bundleExchange);
			startActivity(intentExchange);
			break;
		// 金交所代理
		case 6:
			Bundle bundleJJS = new Bundle();
			bundleJJS.putString("url", Constants.xmsUrlForJJS);
			bundleJJS.putString("name", "金交所代理");
			Intent intentJJS = new Intent(baseActivity, WebViewActivity.class);
			intentJJS.putExtras(bundleJJS);
			startActivity(intentJJS);
			break;
		// 中银咨询
		// case 7:
		// Bundle bundleMarket = new Bundle();
		// bundleMarket.putString("url", Constants.xmsUrlForConsult);
		// bundleMarket.putString("name", "中银咨询");
		// Intent intentMarket = new Intent(baseActivity,
		// WebViewActivity.class);
		// intentMarket.putExtras(bundleMarket);
		// startActivity(intentMarket);
		// break;
		}
	}

	private void switchDailyLife(int position) {
		switch (position) {
		// 股市行情
		// case 0:
		// Bundle bundleConsult = new Bundle();
		// bundleConsult.putString("url", Constants.xmsUrlForMarket);
		// bundleConsult.putString("name", "股市行情");
		// Intent intentConsult = new Intent(baseActivity,
		// WebViewActivity.class);
		// intentConsult.putExtras(bundleConsult);
		// startActivity(intentConsult);
		// break;
		// 和讯财经
		case 0:
			Bundle bundleHx = new Bundle();
			bundleHx.putString("url", Constants.xmsUrlForHx);
			bundleHx.putString("name", "和讯财经");
			Intent intentHx = new Intent(baseActivity, WebViewActivity.class);
			intentHx.putExtras(bundleHx);
			startActivity(intentHx);
			break;
		// 天气预报
		case 1:
			Bundle bundleWeather = new Bundle();
			bundleWeather.putString("url", Constants.xmsUrlForWeather);
			bundleWeather.putString("name", "天气预报");
			Intent intentWeather = new Intent(baseActivity,
					WebViewActivity.class);
			intentWeather.putExtras(bundleWeather);
			startActivity(intentWeather);
			break;
		// 美食团购
		case 2:
			Bundle bundleDzdp = new Bundle();
			bundleDzdp.putString("url", Constants.xmsUrlForDzdp);
			bundleDzdp.putString("name", "美食团购");
			Intent intentDzdp = new Intent(baseActivity, WebViewActivity.class);
			intentDzdp.putExtras(bundleDzdp);
			startActivity(intentDzdp);
			break;
		// 家政服务
		case 3:
			Bundle bundle58Home = new Bundle();
			bundle58Home.putString("url", Constants.xmsUrlFor58Home);
			bundle58Home.putString("name", "家政服务");
			Intent intent58Home = new Intent(baseActivity,
					WebViewActivity.class);
			intent58Home.putExtras(bundle58Home);
			startActivity(intent58Home);
			break;
		// 电影票
		case 4:
			Bundle bundleSpider = new Bundle();
			bundleSpider.putString("url", Constants.xmsUrlForSpider);
			bundleSpider.putString("name", "电影票");
			Intent intentSpider = new Intent(baseActivity,
					WebViewActivity.class);
			intentSpider.putExtras(bundleSpider);
			startActivity(intentSpider);
			break;
		// 快递100
		case 5:
			Bundle bundleExpress = new Bundle();
			bundleExpress.putString("url", Constants.xmsUrlForExpress);
			bundleExpress.putString("name", "快递查询");
			Intent intentExpress = new Intent(baseActivity,
					WebViewActivity.class);
			intentExpress.putExtras(bundleExpress);
			startActivity(intentExpress);
			break;
		// 有道翻译
		case 6:
			Bundle bundleTranlate = new Bundle();
			bundleTranlate.putString("url", Constants.xmsUrlForTranlate);
			bundleTranlate.putString("name", "有道翻译");
			Intent intentTranlate = new Intent(baseActivity,
					WebViewActivity.class);
			intentTranlate.putExtras(bundleTranlate);
			startActivity(intentTranlate);
			break;
		}
	}

	private void switchNecessary(int position) {
		switch (position) {
		// 打车服务
		case 0:
			Bundle bundleDidi = new Bundle();
			bundleDidi.putString("url", Constants.xmsUrlForDidi);
			bundleDidi.putString("name", "打车服务");
			Intent intentDidi = new Intent(baseActivity, WebViewActivity.class);
			intentDidi.putExtras(bundleDidi);
			startActivity(intentDidi);
			break;
		// 购机票
		case 1:
			Bundle bundleFlight = new Bundle();
			bundleFlight.putString("url", Constants.xmsUrlForFight);
			bundleFlight.putString("name", "购机票");
			Intent intentFlight = new Intent(baseActivity,
					WebViewActivity.class);
			intentFlight.putExtras(bundleFlight);
			startActivity(intentFlight);
			break;
		// 购火车票
		case 2:
			Bundle bundleTrain = new Bundle();
			bundleTrain.putString("url", Constants.xmsUrlForTrain);
			bundleTrain.putString("name", "购火车票");
			Intent intentTrain = new Intent(baseActivity, WebViewActivity.class);
			intentTrain.putExtras(bundleTrain);
			startActivity(intentTrain);
			break;
		// 购汽车票
		case 3:
			Bundle bundle = new Bundle();
			bundle.putString("url", Constants.xmsUrlForLt100);
			bundle.putString("name", "购汽车票");
			Intent intent = new Intent(baseActivity, WebViewActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		// 地图服务
		case 4:
			Bundle bundleBaidu = new Bundle();
			bundleBaidu.putString("url", Constants.xmsUrlForBaidu);
			bundleBaidu.putString("name", "地图服务");
			Intent intentBaidu = new Intent(baseActivity, WebViewActivity.class);
			intentBaidu.putExtras(bundleBaidu);
			startActivity(intentBaidu);
			break;
		}
	}

}
