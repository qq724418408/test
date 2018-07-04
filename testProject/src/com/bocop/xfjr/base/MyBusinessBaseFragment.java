package com.bocop.xfjr.base;

import java.util.ArrayList;
import java.util.List;

import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.activity.customer.XFJRBusinessDetailActivity;
import com.bocop.xfjr.activity.customer.XfjrMainActivity;
import com.bocop.xfjr.adapter.MyBusinessListAdapter;
import com.bocop.xfjr.bean.ErrorBean;
import com.bocop.xfjr.bean.MyBusinessBean;
import com.bocop.xfjr.bean.add.ProductBean;
import com.bocop.xfjr.bean.add.ProductBean.QzBean;
import com.bocop.xfjr.bean.detail.MyBusinessDetailApproveBean;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.constant.XFJRConstant;
import com.bocop.xfjr.util.XFJRUtil;
import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * 业务列表
 */
public abstract class MyBusinessBaseFragment extends XFJRLazyFragment {

	private RecyclerView rvApplicationList;
	private TextView tvEmpty;
	private List<MyBusinessBean> list = new ArrayList<>();
	private MyBusinessListAdapter applicationListAdapter;
	protected int request = 0; // 请求次数

	private void emptyView(String empty){
		initViews();
		setDrawableTop(tvEmpty, /*R.drawable.xfjr_image_error2*/R.drawable.xfjr_image_error1);
		tvEmpty.setText(/*R.string.no_data*/empty);
		tvEmpty.setVisibility(View.VISIBLE);
		rvApplicationList.setVisibility(View.GONE);
	}
	
	private void errorView(String error) {
		initViews();
		setDrawableTop(tvEmpty, R.drawable.xfjr_image_error1);
		tvEmpty.setText(/*R.string.page_error_no_data*/error);
		tvEmpty.setVisibility(View.VISIBLE);
		rvApplicationList.setVisibility(View.GONE);
	}
	
	private void initViews() {
		if (null != view) {
			rvApplicationList = (RecyclerView) view.findViewById(R.id.rvApplicationList);
			tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);
		}
	}

	@Override
	protected void initData() {
		initViews();
		applicationListAdapter = new MyBusinessListAdapter(getActivity(), list);
		rvApplicationList.setLayoutManager(new LinearLayoutManager(getActivity()));
		rvApplicationList.setAdapter(applicationListAdapter);
		initListener();
		super.initData();
	}

	/**
	 * 待预审，没有预审金额，继续申请，更多详情
	 */
	protected void loadNetworkData(final int status) {
		if(XfjrMain.isNet){
			HttpRequest.reqBussinessList(getActivity(), status, new IHttpCallback<List<MyBusinessBean>>() {

				@Override
				public void onSuccess(String url, List<MyBusinessBean> result) {
					if (result != null && result.size() > 0) {
						rvApplicationList.setVisibility(View.VISIBLE);
						tvEmpty.setVisibility(View.GONE);
						list.clear();
						for (MyBusinessBean applicationBean : result) {
							applicationBean.setStatus("" + status);
						}
						list.addAll(result);
						//((XFJRMyBusinessActivity)getActivity()).bindIndicator(status, false);
						rvApplicationList.post(new Runnable() {
							public void run() {
								applicationListAdapter.notifyDataSetChanged();
							}
						});
					} else {
						emptyView("查询无数据");
					}
				}

				@Override
				public void onError(String url, Throwable e) {
					LogUtils.e(e.getMessage());
					String json = e.getMessage();
					ErrorBean error  = new Gson().fromJson(json, ErrorBean.class);
					UrlConfig.showErrorTips(getActivity(), e,false);// 不弹出toast
					if(UrlConfig.emptyCode.equals("" + error.code)){
						emptyView(error.msg);
					} else {
						errorView(error.msg);
					}
				}

				@Override
				public void onFinal(String url) {
					LogUtils.e("onFinal"+url);
				}
			});
		} else {
			initLocalData(status); // 模拟数据
		}
		
	}
	
	/**
	 * 初始化本地数据
	 * 
	 * @param status
	 */
	private void initLocalData(int status) {
		initViews();
		List<MyBusinessBean> result = new ArrayList<>();
		rvApplicationList.setVisibility(View.VISIBLE);
		tvEmpty.setVisibility(View.GONE);
		if(XfjrMain.role.equals("0")){ // 商户
			switch (status) {
			case 0: // 处理中，没有预审金额，继续申请，更多详情99+
				for (int i = 0; i < 1; i++) {
					MyBusinessBean bean = new MyBusinessBean();
					bean.setMerchantName("天颂雅苑");
					bean.setCustomerName("欧青");
					bean.setProductName("房屋分期");
					bean.setStatus("" + status);
					bean.setStepStatus("3");
					bean.setApproveMoney((i + "800000"));
					bean.setApplyMoney(i + "10000");
					bean.setBusinessId(String.format("F44567%d", i + 2));
					result.add(bean);
				}
				break;
			case 1: // 已通过，有预审金额，更多详情12
				for (int i = 0; i < 10; i++) {
					MyBusinessBean bean = new MyBusinessBean();
					bean.setMerchantName("长久世达");
					bean.setCustomerName("孟达");
					bean.setProductName("汽车分期");
					bean.setStatus("" + status);
					bean.setStepStatus("8");
					bean.setApproveMoney(i + "90000");
					bean.setApplyMoney(i + "80000");
					bean.setBusinessId(String.format("F44567%d", i + 2));
					result.add(bean);
				}
				break;
			case 2: // 已拒绝，没有有预审金额，更多详情3
				for (int i = 0; i < 1; i++) {
					MyBusinessBean bean = new MyBusinessBean();
					bean.setMerchantName("长久世达");
					bean.setCustomerName("猛打");
					bean.setProductName("汽车分期");
					bean.setStatus("" + status);
					bean.setStepStatus("8");
					bean.setBusinessId(String.format("F44567%d", i + 2));
					result.add(bean);
				}
				break;
			}
		} else if (XfjrMain.role.equals("1")) { // 客户经理
			switch (status) {
			case 0: // 待预审，没有预审金额，继续申请，更多详情99+
				MyBusinessBean bean00 = new MyBusinessBean();
				bean00.setChannelId("1000000");
				bean00.setMerchantName("授信模型：ALL");
				bean00.setCustomerName("欧青");
				bean00.setProductName("房屋分期");
				bean00.setStatus("" + status);
				bean00.setStepStatus("7");
				bean00.setApproveMoney( "800000");
				bean00.setApplyMoney("10000");
				bean00.setBusinessId(String.format("F44567%d", 10010));
				result.add(bean00);
				MyBusinessBean bean11 = new MyBusinessBean();
				bean11.setChannelId("1000001");
				bean11.setMerchantName("授信模型：客群类");
				bean11.setCustomerName("欧青");
				bean11.setProductName("房屋分期");
				bean11.setStatus("" + status);
				bean11.setStepStatus("7");
				bean11.setApproveMoney( "800000");
				bean11.setApplyMoney("10000");
				bean11.setBusinessId(String.format("F44567%d", 10010));
				result.add(bean11);
				MyBusinessBean bean22 = new MyBusinessBean();
				bean22.setChannelId("1000002");
				bean22.setMerchantName("授信模型：场景类");
				bean22.setCustomerName("欧青");
				bean22.setProductName("房屋分期");
				bean22.setStatus("" + status);
				bean22.setStepStatus("7");
				bean22.setApproveMoney( "800000");
				bean22.setApplyMoney("10000");
				bean22.setBusinessId(String.format("F44567%d", 10010));
				result.add(bean22);
				MyBusinessBean bean33 = new MyBusinessBean();
				bean33.setChannelId("1000003");
				bean33.setMerchantName("授信模型:普通类");
				bean33.setCustomerName("欧青");
				bean33.setProductName("房屋分期");
				bean33.setStatus("" + status);
				bean33.setStepStatus("7");
				bean33.setApproveMoney( "800000");
				bean33.setApplyMoney("10000");
				bean33.setBusinessId(String.format("F44567%d", 10010));
				result.add(bean33);
				for (int i = 1; i < 7; i++) {
					MyBusinessBean bean1 = new MyBusinessBean();
					bean1.setChannelId("1001");
					bean1.setMerchantName("继续可到步骤"+i);
					bean1.setCustomerName("欧青");
					bean1.setProductName("房屋分期");
					bean1.setStatus("" + status);
					bean1.setStepStatus(""+i);
					bean1.setApproveMoney( "800000");
					bean1.setApplyMoney("10000");
					bean1.setBusinessId(String.format("F44567%d", 100 + i));
					result.add(bean1);
				}
				for (int i = 0; i < 90; i++) {
					MyBusinessBean bean = new MyBusinessBean();
					bean.setChannelId("1001");
					bean.setMerchantName("天颂雅苑");
					bean.setCustomerName("欧青");
					bean.setProductName("房屋分期");
					bean.setStatus("" + status);
					bean.setStepStatus("7");
					bean.setApproveMoney(i + "800000");
					bean.setApplyMoney(i + "10000");
					bean.setBusinessId(String.format("F44567%d", i + 200));
					result.add(bean);
				}
				break;
			case 1: // 待决策，没有预审金额，补充资料，更多详情10
				for (int i = 0; i < 10; i++) {
					MyBusinessBean bean = new MyBusinessBean();
					bean.setChannelId("1001");
					bean.setMerchantName("长久世达");
					bean.setProductName("汽车分期");
					bean.setStatus("" + status);
					bean.setStepStatus("8");
					bean.setApproveMoney(i + "700000");
					bean.setApplyMoney(i + "40000");
					bean.setBusinessId(String.format("编号  F44567%d", i + 300));
					if(i % 2 == 0){
						bean.setCustomerName("欧阳青");
						bean.setFinanceCertify("N");
						bean.setIncomeCertify("N");
					} else {
						bean.setCustomerName("诸葛孔明");
						bean.setFinanceCertify("Y");
						bean.setIncomeCertify("Y");
					}
					result.add(bean);
				}
				break;
			case 2: // 待审批，有预审金额，更多详情8
				for (int i = 0; i < 8; i++) {
					MyBusinessBean bean = new MyBusinessBean();
					bean.setChannelId("1001");
					bean.setMerchantName("长久世达");
					bean.setCustomerName("老梁");
					bean.setProductName("汽车分期");
					bean.setStatus("" + status);
					bean.setStepStatus("5");
					bean.setApproveMoney(i + "300000");
					bean.setApplyMoney(i + "40000");
					bean.setBusinessId(String.format("F44567%d", i + 400));
					result.add(bean);
				}
				break;
			case 3: // 待放款，有预审金额，补充资料，更多详情10
				for (int i = 0; i < 10; i++) {
					MyBusinessBean bean = new MyBusinessBean();
					bean.setChannelId("1001");
					bean.setMerchantName("长久世达");
					bean.setProductName("汽车分期");
					bean.setStatus("" + status);
					bean.setStepStatus("8");
					bean.setApproveMoney(i + "600000");
					bean.setApplyMoney(i + "50000");
					bean.setBusinessId(String.format("F44567%d", i + 650));
					if(i % 2 == 0){
						bean.setCustomerName("李豆豆");
						bean.setInstalmentApply("N");
					} else {
						bean.setCustomerName("张三");
						bean.setInstalmentApply("Y");
					}
					result.add(bean);
				}
				break;
			case 4: // 已放款，有预审金额，更多详情12
				for (int i = 0; i < 12; i++) {
					MyBusinessBean bean = new MyBusinessBean();
					bean.setChannelId("1001");
					bean.setMerchantName("长久世达");
					bean.setCustomerName("孟达");
					bean.setProductName("汽车分期");
					bean.setStatus("" + status);
					bean.setStepStatus("8");
					bean.setApproveMoney(i + "90000");
					bean.setApplyMoney(i + "80000");
					bean.setBusinessId(String.format("F44567%d", i + 780));
					result.add(bean);
				}
				break;
			case 5: // 已拒绝，没有有预审金额，更多详情3
				for (int i = 0; i < 3; i++) {
					MyBusinessBean bean = new MyBusinessBean();
					bean.setChannelId("1001");
					bean.setMerchantName("长久世达");
					bean.setCustomerName("猛打");
					bean.setProductName("汽车分期");
					bean.setStatus("" + status);
					bean.setStepStatus("8");
					bean.setBusinessId(String.format("F44567%d", i + 770));
					result.add(bean);
				}
				break;
			}
		}
		rvApplicationList.setVisibility(View.VISIBLE);
		tvEmpty.setVisibility(View.GONE);
		//list.clear();
		for (MyBusinessBean applicationBean : result) {
			applicationBean.setStatus("" + status);
		}
		list.addAll(result);
		LogUtils.e("业务数量：" + result.size());
		rvApplicationList.post(new Runnable() {
			public void run() {
				applicationListAdapter.notifyDataSetChanged();
			}
		});
	}
	
	/**
	 * 通过商户编号、渠道编号和产品编号获取欺诈侦测规则和授信模型
	 * 
	 * @param bean
	 */
	private void reqBusinessStatus(final MyBusinessBean bean){
		if (XfjrMain.isNet) {
			HttpRequest.reqBusinessStatus(getActivity(), bean, new IHttpCallback<ProductBean>() {

				@Override
				public void onSuccess(String url, ProductBean result) {
					result.setTelephone(bean.getTelephone());
					result.setUserName(bean.getCustomerName());
					result.setIdCard(bean.getIdCard());
					result.setPeriods(bean.getPeriods()); // 期数：月数
					XfjrMainActivity.callMe(getActivity(), result, getStepStatus(bean, result));
					//XfjrMainActivity.callMe(getActivity(),result ,Integer.parseInt(TextUtils.isEmpty(bean.getStepStatus()) ? "1" : bean.getStepStatus()));
				}

				@Override
				public void onError(String url, Throwable e) {
					UrlConfig.showErrorTips(getActivity(), e, true);
				}

				@Override
				public void onFinal(String url) {
					
				}
			});
		} else {
			ProductBean productBean = new ProductBean();
			switch (bean.getChannelId()) {
			case "1000000":
				productBean.setFxModel("1,2,3");
				break;
			case "1000001":
				productBean.setFxModel("1,3");
				break;
			case "1000002":
				productBean.setFxModel("2,3");
				break;
			case "1000003":
				productBean.setFxModel("3");
				break;
			}
			productBean.setProductId("1");
			productBean.setProductName("汽车分期");
			productBean.setMaxAge("60");
			productBean.setMinAge("18");
			productBean.setMaxMoney("10000");
			productBean.setPercent("80");
			productBean.setQz(new QzBean("90", "Y", "Y", "Y", "Y"));
			productBean.setFrom(2);
			productBean.setTelephone("17607842058");
			productBean.setUserName(bean.getCustomerName());
			XfjrMainActivity.callMe(getActivity(), productBean, getStepStatusByProductBean(bean));
			//XfjrMainActivity.callMe(getActivity(), productBean, Integer.parseInt(TextUtils.isEmpty(bean.getStepStatus()) ? "1" : bean.getStepStatus())); // 继续申请，具体到哪一步
		}
	}
	
	/**
	 * 返回继续申请要走的步骤
	 * 
	 * @param bean
	 * @return
	 */
	private int getStepStatus(MyBusinessBean bean, ProductBean result) {
		//08;补充资料模块 、09:业务状态认为变更模块	
		//0, 通过 1, 待审批 2, 拒绝 3, 验证中
		int stepStatus = 1;
		List<MyBusinessDetailApproveBean> busilogList = bean.getBusilogList();
		MyBusinessDetailApproveBean approveBean = busilogList.get(0);
		if ("0".equals(approveBean.getResult())) { // 通过
			switch (approveBean.getFlow()) {
			case "00": //00:新建进件模块
				stepStatus = 1;
				break;
			case "01"://01:申请人授权书上传模块、
				stepStatus = 2;
				break;
			case "02"://02:手机验证模块、
				stepStatus = 3;
				break;
			case "03"://03:人脸识别模块、
				stepStatus = 4;
				break;
			case "04"://04:人行征信风险验证模块、
				stepStatus = 5;
				break;
			case "05"://05:银联信用卡卡验证模块 、
				stepStatus = 6;
				break;
			case "06"://06:第三方征信数据核验模块、
				stepStatus = 7;
				break;
			}
		} else if ("3".equals(approveBean.getResult())) { // 验证中
			switch (approveBean.getFlow()) {
			case "01"://01:申请人授权书上传模块、
				stepStatus = 1;
				break;
			case "02"://02:手机验证模块、
				stepStatus = 2;
				break;
			case "03"://03:人脸识别模块、
				stepStatus = 3;
				break;
			case "04"://04:人行征信风险验证模块、
				result.setPersonalSubmit(true);
				stepStatus = 4;
				break;
			case "05"://05:银联信用卡卡验证模块 、
				result.setBankCardSubmit(true);
				stepStatus = 5;
				break;
			case "06"://06:第三方征信数据核验模块、
				result.setThreeSubmit(true);
				stepStatus = 6;
				break;
			case "07"://07:风险预决策模块、
				stepStatus = 7;
				break;
			}
		}
		return stepStatus;
	}
	
	/**
	 * 根据具体情况返回继续申请要走的步骤
	 * 
	 * @param bean
	 * @return
	 */
	private int getStepStatusByProductBean(MyBusinessBean bean) {
		int step = Integer.parseInt(bean.getStepStatus());
		return step;
	}

	private void initListener() {
		applicationListAdapter.setOnClickAddDataListener(new MyBusinessListAdapter.OnClickAddDataListener() {
			@Override
			public void onClickAddData(MyBusinessBean bean, int position) {
				XfjrMain.businessId = bean.getBusinessId();
				if (bean.getStatus().equals("" + XFJRConstant.C_STATUS_0_INT)) {
					if (!XFJRUtil.isFastClick()) {
						LogUtils.e("is not FastClick");
						reqBusinessStatus(bean);
					} else {
						LogUtils.e("isFastClick");
					}
				} else if (bean.getStatus().equals("1") || bean.getStatus().equals("3")) {
					ProductBean productBean = new ProductBean();
					if (bean.getStatus().equals("1")) { //  // 11是列表转人工过来的，111是转人工详情过来的
						productBean.setFrom(11); // 11是列表转人工过来的，111是转人工详情过来的，33是列表待放款过来的，333是待放款详情过来的
					} else { // 33是列表待放款过来的，333是待放款详情过来的
						productBean.setFrom(33); // 11是列表转人工过来的，111是转人工详情过来的，33是列表待放款过来的，333是待放款详情过来的
					}
					XfjrMain.businessStatus = bean.getStatus(); // 1或3
					if (!XFJRUtil.isFastClick()) {
						LogUtils.e("is not FastClick");
						XfjrMainActivity.callMe(getActivity(), productBean, XFJRConstant.GO_TO_BCZL); // 补充资料
					} else {
						LogUtils.e("isFastClick");
					}
				}
			}
		});
		applicationListAdapter.setOnClickMoreDataListener(new MyBusinessListAdapter.OnClickMoreDataListener() {
			@Override
			public void onClickMoreData(MyBusinessBean bean, int position) {
				if (!XFJRUtil.isFastClick()) {
					LogUtils.e("is not FastClick");
					XfjrMain.businessId = bean.getBusinessId();
					LogUtils.e("XfjrMain.businessId：" + XfjrMain.businessId);
					Bundle bundle = new Bundle();
					bundle.putSerializable("ApplicationBean", bean);
					Intent intent = new Intent(getActivity(), XFJRBusinessDetailActivity.class);
					intent.putExtra("ApplicationBeanBundle", bundle);
					intent.putExtra("status", bean.getStatus());
					getActivity().startActivity(intent);
				} else {
					LogUtils.e("isFastClick");
				}
			}
		});
	}
	
//	class PretrialCommitBroadcastReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			if (null != intent && null != rvApplicationList && null != applicationListAdapter) {
//				LogUtils.e("MyBusinessBaseFragment收到广播PretrialCommitBroadcastReceiver");
//				rvApplicationList.post(new Runnable() {
//					public void run() {
//						applicationListAdapter.notifyDataSetChanged();
//					}
//				});
//			}
//		}
//	}
//	
//	class AddDataCommitBroadcastReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			if (null != intent && null != rvApplicationList && null != applicationListAdapter) {
//				LogUtils.e("MyBusinessBaseFragment收到广播AddDataCommitBroadcastReceiver");
//				rvApplicationList.post(new Runnable() {
//					public void run() {
//						applicationListAdapter.notifyDataSetChanged();
//					}
//				});
//			}
//		}
//	}
//	
//	private PretrialCommitBroadcastReceiver pretrialCommitBroadcastReceiver;
//	private AddDataCommitBroadcastReceiver addDataCommitBroadcastReceiver;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		pretrialCommitBroadcastReceiver = new PretrialCommitBroadcastReceiver();
//		addDataCommitBroadcastReceiver = new AddDataCommitBroadcastReceiver();
//		getActivity().registerReceiver(pretrialCommitBroadcastReceiver, new IntentFilter(XFJRConstant.ACTION_PRETRIAL_COMMIT));
//		getActivity().registerReceiver(addDataCommitBroadcastReceiver, new IntentFilter(XFJRConstant.ACTION_ADD_DATA_COMMIT));
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onDestroy() {
//		getActivity().unregisterReceiver(pretrialCommitBroadcastReceiver);
//		getActivity().unregisterReceiver(addDataCommitBroadcastReceiver);
		super.onDestroy();
	}

}
