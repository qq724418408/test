package com.bocop.xfjr.fragment;

import java.util.ArrayList;
import java.util.List;

import com.boc.jx.baseUtil.view.annotation.CheckNet;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.activity.customer.XfjrMainActivity;
import com.bocop.xfjr.adapter.DetailApproveInfoAdapter;
import com.bocop.xfjr.base.XFJRLazyFragment;
import com.bocop.xfjr.bean.ErrorBean;
import com.bocop.xfjr.bean.MyBusinessBean;
import com.bocop.xfjr.bean.add.ProductBean;
import com.bocop.xfjr.bean.add.ProductBean.QzBean;
import com.bocop.xfjr.bean.detail.BusinessBasicInfoBean;
import com.bocop.xfjr.bean.detail.BusinessBasicInfoBean.AuditBean;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.constant.XFJRConstant;
import com.bocop.xfjr.manager.FullyLinearLayoutManager;
import com.bocop.xfjr.observer.SwichFragmentObserver;
import com.bocop.xfjr.observer.SwichFragmentSubject;
import com.bocop.xfjr.util.TextUtil;
import com.google.gson.Gson;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * 业务详情:基本信息
 */
public class BusinessDetailFragment0 extends XFJRLazyFragment implements SwichFragmentSubject {

	protected SwichFragmentObserver mObserver;
	@ViewInject(R.id.rvApproveInfo)
	private RecyclerView rvApproveInfo;
	@ViewInject(R.id.tvMerchantName)
	private TextView tvMerchantName;
	@ViewInject(R.id.tvMerchantStatus)
	private TextView tvMerchantStatus;
	@ViewInject(R.id.tvChannel)
	private TextView tvChannel;
	@ViewInject(R.id.tvCategory)
	private TextView tvCategory;
	@ViewInject(R.id.tvBusinessId)
	private TextView tvBusinessId;
	@ViewInject(R.id.tvCustomerName)
	private TextView tvCustomerName;
	@ViewInject(R.id.tvTelephone)
	private TextView tvTelephone;
	@ViewInject(R.id.tvTotalMoney)
	private TextView tvTotalMoney;
	@ViewInject(R.id.tvApplyMoney)
	private TextView tvApplyMoney;
	@ViewInject(R.id.tvPeriods)
	private TextView tvPeriods;
	@ViewInject(R.id.tvRate)
	private TextView tvRate;
	@ViewInject(R.id.lltAprovedMoney)
	private View lltAprovedMoney; // 预审金额
	@ViewInject(R.id.lltLendMoney)
	private View lltLendMoney; // 放款金额
	@ViewInject(R.id.lltBasicInfo)
	private View lltBasicInfo;
	@ViewInject(R.id.tvAprovedMoney)
	private TextView tvAprovedMoney;
	@ViewInject(R.id.tvLendMoney)
	private TextView tvLendMoney;
	@ViewInject(R.id.tvEmpty)
	private TextView tvEmpty;
	@ViewInject(R.id.btnContinueApply)
	private Button btnContinueApply;
	private List<AuditBean> auditList = new ArrayList<>();
	private DetailApproveInfoAdapter approveInfoAdapter;
	private static String status;
	private static MyBusinessBean myBusinessBean;
	private int request = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.xfjr_fragment_detail_basic_info, container, false);
		return view;
	}

	@Override
	protected void lazyLoad() {
		reqBussinessBasicInfo();// 加载网络数据
	}
	
	@Override
	public void onPause() {
		request = 0;
		super.onPause();
	}
	
	@Override
	public void onResume() {
		LogUtils.e("---onResume---request=" + request);
		if (request == 0) {
			reqBussinessBasicInfo();// 加载网络数据
		}
		super.onResume();
	}
	
	private void initViews() {
		rvApproveInfo = (RecyclerView) view.findViewById(R.id.rvApproveInfo);
		lltBasicInfo = view.findViewById(R.id.lltBasicInfo);
		tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);
	}
	
	private void successView(){
		tvEmpty.setVisibility(View.GONE);
		lltBasicInfo.setVisibility(View.VISIBLE);
	}

//	private void emptyView(){
//		initViews();
//		setDrawableTop(tvEmpty, R.drawable.xfjr_image_error2);
//		tvEmpty.setText(R.string.no_data);
//		tvEmpty.setVisibility(View.VISIBLE);
//		lltBasicInfo.setVisibility(View.GONE);
//	}
	
	private void errorView(String error) {
		initViews();
		setDrawableTop(tvEmpty, R.drawable.xfjr_image_error1);
		tvEmpty.setText(error);
		tvEmpty.setVisibility(View.VISIBLE);
		lltBasicInfo.setVisibility(View.GONE);
	}
	
	private void localBussinessBasicInfo() {
		auditList.clear();
		initViews();
		successView();
		switch (status) {
		case "0": // 待预审(继续申请)
			auditList.add(new AuditBean("发起申请", "2017-10-11 14:16:17"));
			break;
		case "1": // 待决策
			auditList.add(new AuditBean("请耐心等待人工决策结果", "2017-10-23 18:59:59"));
			auditList.add(new AuditBean("预审批完成，审批金额为￥100,000.00，请耐心等待审批结果", "2017-10-16 12:23:33"));
			auditList.add(new AuditBean("发起申请", "2017-10-10 14:16:17"));
			break;
		case "2": // 待审批
			auditList.add(new AuditBean("预审批完成，审批金额为￥100,000.00，请耐心等待审批结果", "2017-10-16 12:23:33"));
			auditList.add(new AuditBean("发起申请", "2017-10-10 14:16:17"));
			break;
		case "3": // 待放款(补充资料)
			auditList.add(new AuditBean("审批已通过，请耐心等待放款。注意补充《分期申请书》", "2017-10-23 18:59:59"));
			auditList.add(new AuditBean("预审批完成，审批金额为￥100,000.00，请耐心等待审批结果", "2017-10-16 12:23:33"));
			auditList.add(new AuditBean("发起申请", "2017-10-10 14:16:17"));
			break;
		case "4": // 已放款
			auditList.add(new AuditBean("成功放款，放款金额为￥100,000.00","2017-10-27 16:19:09"));
			auditList.add(new AuditBean("审批已通过，请耐心等待放款。注意补充《分期申请书》", "2017-10-23 18:59:59"));
			auditList.add(new AuditBean("预审批完成，审批金额为￥100,000.00，请耐心等待审批结果", "2017-10-16 12:23:33"));
			auditList.add(new AuditBean("发起申请", "2017-10-11 14:16:17"));
			break;
		case "5": // 已拒绝
			auditList.add(new AuditBean("人工决策不通过，已拒绝","2017-10-27 16:19:09"));
			auditList.add(new AuditBean("您的申请不符合预审批标准，请补充相关资料，转入人工决策", "2017-10-23 18:59:59"));
			auditList.add(new AuditBean("预审批完成，审批金额为￥100,000.00，请耐心等待审批结果", "2017-10-16 12:23:33"));
			auditList.add(new AuditBean("发起申请", "2017-10-11 14:16:17"));
			break;
		}
		//Collections.reverse(auditList);
		rvApproveInfo.post(new Runnable() {
			public void run() {
				approveInfoAdapter.notifyDataSetChanged();
			}
		});
	}
	
	private void reqBussinessBasicInfo() {
		request++;
		LogUtils.e("reqBussinessBasicInfo业务编号："+XfjrMain.businessId);
		if(XfjrMain.isNet){
			HttpRequest.reqBussinessBasicInfo(getActivity(), new IHttpCallback<BusinessBasicInfoBean>() {

				@Override
				public void onSuccess(String url, BusinessBasicInfoBean result) {
					LogUtils.e(result.getBusinessId());
					successView();
					showDataView(result);
					auditList.clear();
					auditList.addAll(result.getAudit());
					//Collections.reverse(auditList);
					rvApproveInfo.post(new Runnable() {
						public void run() {
							approveInfoAdapter.notifyDataSetChanged();
						}
					});
				}

				@Override
				public void onError(String url, Throwable e) {
					UrlConfig.showErrorTips(getActivity(), e, false);
					String json = e.getMessage();
					ErrorBean error  = new Gson().fromJson(json, ErrorBean.class);
					LogUtils.e("reqBussinessBasicInfo--onError:" + error.msg);
//					if(UrlConfig.emptyCode.equals("" + error.code)){
//						emptyView();
//					} else {
						errorView(error.msg);
//					}
				}

				@Override
				public void onFinal(String url) {
					LogUtils.e("reqBussinessBasicInfo--onFinal");
				}
			});
		} else {
			localBussinessBasicInfo();
		}
		
	}

	public static BusinessDetailFragment0 getInstance(MyBusinessBean bean) {
		LogUtils.e("getInstance业务编号："+bean.getBusinessId());
		BusinessDetailFragment0 fragment = new BusinessDetailFragment0();
		Bundle bundle = new Bundle();
		bundle.putSerializable("MyBusinessBean", bean);
		fragment.setArguments(bundle);
		status = bean.getStatus();
		XfjrMain.businessId = bean.getBusinessId();
		myBusinessBean = bean;
		return fragment;
	}
	
	@Override
	protected void initData() {
		showDataView(null);
		approveInfoAdapter = new DetailApproveInfoAdapter(getActivity(), auditList);
		rvApproveInfo.setLayoutManager(new FullyLinearLayoutManager(getActivity()));
		rvApproveInfo.setAdapter(approveInfoAdapter);
		super.initData();
	}

	private void showDataView(BusinessBasicInfoBean bean) {
		boolean fileCommitted = getFileCommitted(myBusinessBean);
		if(bean != null){
			tvMerchantName.setText(bean.getMerchantName());
//			if("N".equals(bean.getMerchantStatus())){
//				tvMerchantStatus.setText(R.string.close_status);
//			} else if ("Y".equals(bean.getMerchantStatus())) {
//				tvMerchantStatus.setText(R.string.normal_status);
//			}
			XfjrMain.businessId = bean.getBusinessId();
			tvChannel.setText(bean.getChannel());
			tvCategory.setText(bean.getProductName());
			tvBusinessId.setText(XfjrMain.businessId);
			tvCustomerName.setText(bean.getCustomerName());
			tvTelephone.setText(bean.getPhoneNum());
			tvTotalMoney.setText(TextUtil.money$Format(bean.getTotalPrice()));
			tvApplyMoney.setText(TextUtil.money$Format(bean.getApplyMoney()));
			if (bean.getPeriodsName().contains("期")) {
				tvPeriods.setText(bean.getPeriodsName());
			} else {
				tvPeriods.setText(bean.getPeriodsName() + "期");
			}
			tvRate.setText(bean.getRate() + "%");
		}
		switch (Integer.parseInt(status)) {
		case XFJRConstant.C_STATUS_0_INT: // 待预审，没有预审金额，继续申请，更多详情  
			btnContinueApply.setVisibility(View.VISIBLE);
			btnContinueApply.setText(R.string.continue_apply);
			break;
		case XFJRConstant.C_STATUS_1_INT: // 待决策，没有预审金额，补充资料，更多详情
			btnContinueApply.setVisibility(View.VISIBLE);
			btnContinueApply.setText(R.string.further_info);
			if (fileCommitted) { // 文件已提交(是否需要隐藏还是变成不可点击)
				btnContinueApply.setVisibility(View.GONE);
				//btnContinueApply.setTextColor(getActivity().getResources().getColor(R.color.xfjr_gray));
			}
			break;
		case XFJRConstant.C_STATUS_2_INT: // 待审批，有预审金额，更多详情
//			if(bean != null){
//				lltAprovedMoney.setVisibility(View.VISIBLE);
//				tvAprovedMoney.setText(TextUtil.money$Format(bean.getApproveMoney()));
//			}
			btnContinueApply.setVisibility(View.GONE);
			break;
		case XFJRConstant.C_STATUS_3_INT: // 待放款，有预审金额，补充资料(分期申请书)，更多详情
//			if(bean != null){
//				lltAprovedMoney.setVisibility(View.VISIBLE);
//				tvAprovedMoney.setText(TextUtil.money$Format(bean.getApproveMoney()));
//			}
			btnContinueApply.setVisibility(View.VISIBLE);
			btnContinueApply.setText(R.string.further_info);
			if (fileCommitted) { // 文件已提交(是否需要隐藏还是变成不可点击)
				btnContinueApply.setVisibility(View.GONE);
				//btnContinueApply.setTextColor(getActivity().getResources().getColor(R.color.xfjr_gray));
			}
			break;
		case XFJRConstant.C_STATUS_4_INT: // 已放款，有预审金额，更多详情
			if(bean != null){
//				lltAprovedMoney.setVisibility(View.VISIBLE);
//				tvAprovedMoney.setText(TextUtil.money$Format(bean.getApproveMoney()));
				lltLendMoney.setVisibility(View.VISIBLE);
				tvLendMoney.setText(TextUtil.money$Format(bean.getTariffMoney()));
			}
			btnContinueApply.setVisibility(View.GONE);
			break;
		case XFJRConstant.C_STATUS_5_INT: // 已拒绝，没有有预审金额，更多详情
			btnContinueApply.setVisibility(View.GONE);
			break;
		}
	}
	
	@OnClick(R.id.btnContinueApply)
	@CheckNet
	private void clickNext(View view) {
		switch (Integer.parseInt(status)) {
		case XFJRConstant.C_STATUS_0_INT: // 继续申请
			reqBusinessStatus();
			break;
		case XFJRConstant.C_STATUS_1_INT: // 补充资料
			XfjrMain.businessStatus = "" + XFJRConstant.C_STATUS_1_INT;
			ProductBean productBean1 = new ProductBean();
			productBean1.setFrom(111); // 11是列表转人工过来的，111是转人工详情过来的，33是列表待放款过来的，333是待放款详情过来的
			XfjrMainActivity.callMe(getActivity(), productBean1, XFJRConstant.GO_TO_BCZL); // 补充资料
			break;
		case XFJRConstant.C_STATUS_3_INT: // 补充资料
			XfjrMain.businessStatus = "" + XFJRConstant.C_STATUS_3_INT;
			ProductBean productBean2 = new ProductBean();
			productBean2.setFrom(333); // 11是列表转人工过来的，111是转人工详情过来的，33是列表待放款过来的，333是待放款详情过来的
			XfjrMainActivity.callMe(getActivity(), productBean2, XFJRConstant.GO_TO_BCZL); // 补充资料
			break;
		}
	}
	
	/**
	 * 通过商户编号、渠道编号和产品编号获取欺诈侦测规则和授信模型
	 * 
	 * @param bean
	 */
	private void reqBusinessStatus(){
		if(XfjrMain.isNet){
			HttpRequest.reqBusinessStatus(getActivity(), myBusinessBean, new IHttpCallback<ProductBean>() {

				@Override
				public void onSuccess(String url, ProductBean result) {
					//XfjrMainActivity.callMe(getActivity(), result, Integer.parseInt(TextUtils.isEmpty(myBusinessBean.getStepStatus()) ? "1" : myBusinessBean.getStepStatus()));
					result.setFrom(-1);
					result.setPeriods(myBusinessBean.getPeriods());
					result.setTelephone(myBusinessBean.getTelephone());
					result.setUserName(myBusinessBean.getCustomerName());
					XfjrMainActivity.callMe(getActivity(), result, getStepStatus(auditList.get(0), result));
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
			productBean.setFxModel("3");
			productBean.setProductId("1");
			productBean.setProductName("汽车分期");
			productBean.setMaxAge("60");
			productBean.setMinAge("18");
			productBean.setMaxMoney("10000");
			productBean.setPercent("80");
			productBean.setQz(new QzBean("90", "Y", "N", "Y", "Y")); 
			productBean.setFrom(-1);
			productBean.setPeriods("12");
			productBean.setTelephone("17607842058");
			productBean.setUserName(myBusinessBean.getCustomerName());
			XfjrMainActivity.callMe(getActivity(), productBean, getStepStatusByProductBean(myBusinessBean));
			//XfjrMainActivity.callMe(getActivity(), productBean, Integer.parseInt(TextUtils.isEmpty(myBusinessBean.getStepStatus()) ? "1" : myBusinessBean.getStepStatus())); // 继续申请，具体到哪一步
		}
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
	
	/**
	 * 根据补充资料是否已上传文件判断按钮是否可以点击
	 * 
	 * @param bean
	 * @return
	 */
	private boolean getFileCommitted(MyBusinessBean bean){
//		if (null == bean) {
//			return false;
//		} else {
//			List<AuditBean> list = bean.getAudit();
//			AuditBean auditBean = list.get(0);
//			String f = auditBean.getFlow();
//			String r = auditBean.getResult();
//			if ("08".equals(f) && "0".equals(r)) {
//				return true;
//			}
//		}
		String status = bean.getStatus();
		switch (status) {
		case "1":
			String financeCertify = bean.getFinanceCertify();
			String incomeCertify = bean.getIncomeCertify();
			if (financeCertify.equals("Y") && incomeCertify.equals("Y")) {
				return true;
			} else if (financeCertify.equals("N") || incomeCertify.equals("N")) {
				return false;
			} 
			break;

		case "3":
			String instalmentApply = bean.getInstalmentApply();
			if (instalmentApply.equals("Y")) { // 已上传分期申请书
				return true;
			} else if (instalmentApply.equals("N")) { // 未上传分期申请书
				return false;
			}
			break;
		}
		return false;
	}

	@Override
	public void register(SwichFragmentObserver observer) {
		mObserver = observer;
	}

	/**
	 * 返回继续申请要走的步骤
	 * 
	 * @param bean
	 * @return
	 */
	private int getStepStatus(AuditBean approveBean, ProductBean result) {
		//08;补充资料模块 、09:业务状态认为变更模块	
		//0, 通过 1, 待审批 2, 拒绝 3, 验证中
		int stepStatus = 1;
//		List<MyBusinessDetailApproveBean> busilogList = bean.getBusilogList();
//		MyBusinessDetailApproveBean approveBean = busilogList.get(0);
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
			case "08"://08;补充资料模块 、
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
	
}
