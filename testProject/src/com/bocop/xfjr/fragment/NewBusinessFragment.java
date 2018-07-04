package com.bocop.xfjr.fragment;


import java.util.ArrayList;
import java.util.List;

import com.boc.jx.baseUtil.view.annotation.CheckNet;
import com.boc.jx.baseUtil.view.annotation.Duplicate;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.activity.customer.XfjrMainActivity;
import com.bocop.xfjr.base.BaseCheckProcessFragment;
import com.bocop.xfjr.bean.BusinessBean;
import com.bocop.xfjr.bean.SystemBasicInfo;
import com.bocop.xfjr.bean.SystemBasicInfo.PeriodListBean;
import com.bocop.xfjr.bean.add.AddBusinessParamBean;
import com.bocop.xfjr.bean.add.ChannelBean;
import com.bocop.xfjr.bean.add.MerchantBean;
import com.bocop.xfjr.bean.add.ProductBean;
import com.bocop.xfjr.bean.add.ProductBean.QzBean;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.constant.XFJRConstant;
import com.bocop.xfjr.observer.StepObserver;
import com.bocop.xfjr.observer.StepSubject;
import com.bocop.xfjr.util.LimitInputTextWatcher;
import com.bocop.xfjr.util.PatternUtils;
import com.bocop.xfjr.util.PreferencesUtil;
import com.bocop.xfjr.util.RateTextWatcher;
import com.bocop.xfjr.util.TextUtil;
import com.bocop.xfjr.util.XFJRUtil;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil.ChannelSelectDialogClick;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil.MerchantSelectDialogClick;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil.PeriodSelectDialogClick;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil.ProductSelectDialogClick;
import com.bocop.xfjr.util.dialog.XfjrDialog;
import com.bocop.xfjr.util.file.SystemBasicJSONWRUtil;
import com.bocop.xfjr.view.XFJRClearEditText;
import com.bocop.xfjr.view.XFJRMoneyClearEditText;
import com.bocop.yfx.utils.ToastUtils;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 如果页面是从首页进来的，数据是空的
 * 
 * 如果页面是从我的业务或者业务详情的页面过来的，页面是有数据的
 * 
 * description： 新增进件
 */
public class NewBusinessFragment extends BaseCheckProcessFragment implements StepSubject {

	private ProductBean product;
	private StepObserver mObserver;
	@ViewInject(R.id.tvTip)
	private TextView tvTip;
	@ViewInject(R.id.tvCommercialName)
	private TextView tvCommercialName;
	@ViewInject(R.id.tvCommercialStatus)
	private TextView tvCommercialStatus;
	@ViewInject(R.id.tvChannel)
	private TextView tvChannel;
	@ViewInject(R.id.tvProduct)
	private TextView tvProduct;
	@ViewInject(R.id.tvInstallmentNo)
	private TextView tvInstallmentNo; // 分期期数
	@ViewInject(R.id.etClientName)
	private EditText etClientName;
	@ViewInject(R.id.etClientPhone)
	private XFJRClearEditText etClientPhone;
	@ViewInject(R.id.etOrderTotal)
	private XFJRMoneyClearEditText etOrderTotal;
	@ViewInject(R.id.etAplyAmount)
	private XFJRMoneyClearEditText etAplyAmount;
	@ViewInject(R.id.etRate)
	private EditText etRate;
	@ViewInject(R.id.btnLeft)
	private Button btnLeft;
	@ViewInject(R.id.btnRight)
	private Button btnRight;
	private List<MerchantBean> merchantList = new ArrayList<>();
	private List<ChannelBean> channelList = new ArrayList<>();
	private List<ProductBean> productList = new ArrayList<>();
	private List<PeriodListBean> periodNameList = new ArrayList<>();
	private AddBusinessParamBean params = new AddBusinessParamBean();
	private boolean isCorrectPhone = false;
	private String commercialNameId;
	private String channelId;
	private String productId;
	private String periodsId; // 分期期数
	private String periods;

	@Override
	protected void initView() {
		super.initView();
		setVisibility(tvReset, View.VISIBLE);
		//setVisibility(tvSave, View.VISIBLE);
	}

	@Override
	protected boolean resetClick(View view) {
		tvCommercialStatus.setVisibility(View.GONE);
		commercialNameId = null;
		channelId = null;
		productId = null;
		periodsId = null;
		periods = null;
//		merchantList.clear();
//		channelList.clear();
//		productList.clear();
//		periodsList.clear();
		unSelected(tvCommercialName, null);
		unSelected(tvChannel, null);
		unSelected(tvProduct, null);
		unSelected(tvInstallmentNo, null);
		etClientName.setText(""); // 客户姓名
		etClientPhone.setText(""); // 客户手机号
		etOrderTotal.setText(""); // 订单总价
		etAplyAmount.setText(""); // 申请金额
		etRate.setText(""); // 费率
		etClientName.clearFocus();
		etClientPhone.clearFocus();
		etOrderTotal.clearFocus();
		etAplyAmount.clearFocus();
		etRate.clearFocus();
//		reqMerchantInfo();// 获取商户信息
		return false;
	}
	
	@Override
	public void onResume() {
		//reqMerchantInfo();// 获取商户信息
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.xfjr_fragment_new_application, container, false);
		return view;
	}

	@Override
	protected void initData() {
		super.initData();
		reqMerchantInfo();// 获取商户信息
		params.setEhrId((String)PreferencesUtil.get(XFJRConstant.KEY_EHR_ID, ""));
		setRightBtnText(getString(R.string.start_assessing));
		initListener();
	}

	private void reqMerchantInfo() {
		if(XfjrMain.isNet){
			HttpRequest.reqMerchantInfo(getActivity(), new IHttpCallback<List<MerchantBean>>() {

				@Override
				public void onSuccess(String url, List<MerchantBean> result) {
					merchantList.clear();
					if (result.size() <= 0) {
						ToastUtils.show(getActivity(), "没有商户数据", 0);//TODO
					} else {
						merchantList.addAll(result);
					}
				}

				@Override
				public void onFinal(String url) {
					LogUtils.e(url);
				}

				@Override
				public void onError(String url, Throwable e) {
					UrlConfig.showErrorTips(getActivity(), e, true);
				}
			});
		} else {
			initMerchantInfo();
		}
	}
	
	private void reqChannelAndProductList(String merchantId) {
		if(XfjrMain.isNet){
			HttpRequest.reqChannelProductListInfo(getActivity(), merchantId, new IHttpCallback<List<ChannelBean>>() {
				
				@Override
				public void onSuccess(String url, List<ChannelBean> result) {
					channelList.clear();
					channelList.addAll(result);
				}
				
				@Override
				public void onFinal(String url) {
					LogUtils.e(url);
				}
				
				@Override
				public void onError(String url, Throwable e) {
					UrlConfig.showErrorTips(getActivity(), e, true);
//					String code =  e.getMessage();
//					if(code != null){
//						ToastUtils.show(getActivity(), "获取渠道列表失败，请重试", 0);
//						LogUtils.e(url + "原因：" + code);
//						if (code.equals(UrlConfig.dynamicUrlExceptionCode)) {
//							XFJRUtil.autoLogin(getActivity());
//						}
//					}
				}
			});
		} else {
			chooseChannelAndProductList(commercialNameId);
		}
	}

	private void chooseChannelAndProductList(String merchantId){
		channelList.clear();
		ProductBean productBean1 = new ProductBean();
		ProductBean productBean2 = new ProductBean();
		ProductBean productBean3 = new ProductBean();
		productBean1.setProductId("1");
		productBean1.setProductName("客群类产品");
		productBean1.setMaxAge("60");
		productBean1.setMinAge("18");
		productBean1.setMaxMoney("10000");
		productBean1.setPercent("80");
		productBean1.setFxModel("1");
		productBean1.setQz(new QzBean("90", "Y", "Y", "Y", "Y"));
		productBean2.setProductId("2");
		productBean2.setProductName("场景类产品");
		productBean2.setMaxAge("60");
		productBean2.setMinAge("25");
		productBean2.setMaxMoney("20000");
		productBean2.setPercent("70");
		productBean2.setFxModel("2");
		productBean2.setQz(new QzBean("80", "Y", "Y", "Y", "N"));
		productBean3.setProductId("3");
		productBean3.setProductName("普通类产品");
		productBean3.setMaxAge("60");
		productBean3.setMinAge("30");
		productBean3.setMaxMoney("8000");
		productBean3.setPercent("90");
		productBean3.setFxModel("3");
		productBean3.setQz(new QzBean("85", "Y", "Y", "Y", "N"));
		switch (merchantId) {
		case "0911": // 商户1
			List<ProductBean> childProductList111 = new ArrayList<>();
			List<ProductBean> childProductList112 = new ArrayList<>();
			ChannelBean channelBean11 = new ChannelBean();
			ChannelBean channelBean12 = new ChannelBean();
			channelBean11.setChannelId("11");			
			channelBean11.setChannelName("渠道11");
			childProductList111.add(productBean1);
			childProductList111.add(productBean2);
			channelBean11.setProductList(childProductList111);
			channelBean12.setChannelId("12");			
			channelBean12.setChannelName("渠道12");
			childProductList112.add(productBean2);
			childProductList112.add(productBean3);
			channelBean12.setProductList(childProductList112);
			channelList.add(channelBean11);
			channelList.add(channelBean12);
			break;
		case "0912": // 商户2
			List<ProductBean> childProductList121 = new ArrayList<>();
			List<ProductBean> childProductList122 = new ArrayList<>();
			List<ProductBean> childProductList123 = new ArrayList<>();
			ChannelBean channelBean21 = new ChannelBean();
			ChannelBean channelBean22 = new ChannelBean();
			ChannelBean channelBean23 = new ChannelBean();
			channelBean21.setChannelId("21");
			channelBean21.setChannelName("渠道21");
			childProductList121.add(productBean1);
			childProductList121.add(productBean2);
			childProductList121.add(productBean3);
			channelBean21.setProductList(childProductList121);
			channelBean22.setChannelId("22");
			channelBean22.setChannelName("渠道22");
			childProductList122.add(productBean2);
			childProductList122.add(productBean3);
			channelBean22.setProductList(childProductList122);
			channelBean23.setChannelId("23");
			channelBean23.setChannelName("渠道23");
			childProductList123.add(productBean1);
			childProductList123.add(productBean2);
			channelBean23.setProductList(childProductList123);
			channelList.add(channelBean21);
			channelList.add(channelBean22);
			channelList.add(channelBean23);
			break;
		case "0913": // 商户3
			List<ProductBean> childProductList131 = new ArrayList<>();
			List<ProductBean> childProductList132 = new ArrayList<>();
			ChannelBean channelBean31 = new ChannelBean();
			ChannelBean channelBean32 = new ChannelBean();
			channelBean31.setChannelId("31");
			channelBean31.setChannelName("渠道31");
			childProductList131.add(productBean1);
			childProductList131.add(productBean2);
			channelBean31.setProductList(childProductList131);
			channelBean32.setChannelId("32");
			channelBean32.setChannelName("渠道32");
			childProductList132.add(productBean1);
			childProductList132.add(productBean3);
			channelBean32.setProductList(childProductList132);
			channelList.add(channelBean31);
			channelList.add(channelBean32);
			break;

		default:
			break;
		}
		
	}
	
	private void initMerchantInfo(){
		List<MerchantBean> result = new ArrayList<>();
		MerchantBean merchantBean1 = new MerchantBean();
		MerchantBean merchantBean2 = new MerchantBean();
		MerchantBean merchantBean3 = new MerchantBean();
		merchantBean1.setMerchantId("0911");
		merchantBean1.setMerchantName("联想集团");
		merchantBean2.setMerchantId("0912");
		merchantBean2.setMerchantName("惠普集团");
		merchantBean3.setMerchantId("0913");
		merchantBean3.setMerchantName("索尼集团");
		result.add(merchantBean1);
		result.add(merchantBean2);
		result.add(merchantBean3);
		merchantList.clear();
		merchantList.addAll(result);
	}
	
	@OnClick(R.id.btnRight)
	@CheckNet
	@Duplicate("NewBusinessFragment.java")
	private void clickNext(View view) {
		String customerName = etClientName.getText().toString().trim(); // 客户姓名
		String telephone = etClientPhone.getText().toString().trim(); // 客户手机号
		String totalMoney = etOrderTotal.getTextString(); // 订单总价
		String applyAmount = etAplyAmount.getTextString(); // 申请金额
		String rate = etRate.getText().toString().trim(); // 费率
		etOrderTotal.clearFocus();
		etAplyAmount.clearFocus();
		if(TextUtils.isEmpty(commercialNameId)){
			ToastUtils.show(getActivity(), getString(R.string.please_select_merchant), 0);
			return;
		}
		if(TextUtils.isEmpty(channelId)){
			ToastUtils.show(getActivity(), getString(R.string.please_select_channel), 0);
			return;
		}
		if(TextUtils.isEmpty(productId)){
			ToastUtils.show(getActivity(), getString(R.string.please_select_product), 0);
			return;
		}
		if(TextUtils.isEmpty(customerName)){
			ToastUtils.show(getActivity(), getString(R.string.please_input_customer_name), 0);
			etClientName.requestFocus();
			return;
		}
		if(TextUtils.isEmpty(telephone)){
			ToastUtils.show(getActivity(), getString(R.string.please_input_phone_num), 0);
			etClientPhone.requestFocus();
			return;
		} else if (!isCorrectPhone) {
			ToastUtils.show(getActivity(), getString(R.string.phone_number_format_is_incorrect), 0);
			etClientPhone.requestFocus();
			etClientPhone.setSelection(etClientPhone.getText().length());
			return;
		}
		if(TextUtils.isEmpty(totalMoney)){
			ToastUtils.show(getActivity(), getString(R.string.please_input_order_total), 0);
			etOrderTotal.requestFocus();
			return;
		}
		if(null != product){
			XfjrMainActivity.productBean.setUserName(customerName);
			XfjrMainActivity.productBean.setTelephone(telephone);
			double maxLimitMoney = getMaxLimitMoney(totalMoney, product.getPercent(), product.getMaxMoney());
			if(TextUtils.isEmpty(applyAmount)){
				ToastUtils.show(getActivity(), getString(R.string.please_input_apply_amount), 0);
				etAplyAmount.requestFocus();
				return;
//			} else if (Double.parseDouble(applyAmount) > Double.parseDouble(product.getMaxMoney())) { // 申请金额大于金额上限
			} else if (Double.parseDouble(applyAmount) > maxLimitMoney) { // 申请金额大于金额上限
				String hint = getString(R.string.apply_amount_can_not_exceed_max);
//				ToastUtils.show(getActivity(), String.format(hint, TextUtil.moneyFormat(product.getMaxMoney())), 0);
				ToastUtils.show(getActivity(), String.format(hint, TextUtil.moneyFormat("" +  maxLimitMoney)), 0);
				return;
			} /*else if (XFJRUtil.exceedPercent(totalMoney, applyAmount, Double.parseDouble(product.getPercent()))) {
				String hint = getString(R.string.apply_amount_can_not_exceed_percent);
				ToastUtils.show(getActivity(), String.format(hint, product.getPercent()) + "%", 0);
				etAplyAmount.requestFocus();
				return;
			}*/
			etAplyAmount.clearFocus();
		} else {
			ToastUtils.show(getActivity(), getString(R.string.please_select_product), 0);
			return;
		}
		if(TextUtils.isEmpty(periodsId)){
			ToastUtils.show(getActivity(), getString(R.string.please_input_installment_num), 0);
			return;
		}
		if(TextUtils.isEmpty(rate)){
			ToastUtils.show(getActivity(), getString(R.string.please_input_rate), 0);
			etRate.requestFocus();
			return;
		}
		product.setPeriods(periods);
		params.setMerchantId(commercialNameId);
		params.setChannelId(channelId);
		params.setProductId(productId);
		params.setCustomerName(customerName);
		params.setTelephone(telephone);
		params.setTotalMoney(totalMoney);
		params.setApplyMoney(applyAmount);
		params.setPeriodsId(periodsId);
		params.setRate(rate);
		startAssessing(); // 开始评估
	}
	
	private double getMaxLimitMoney(String applyAmount, String percent, String maxMoney) {
		double applyAmountD = Double.parseDouble(applyAmount);
		double percentD = Double.parseDouble(percent) / 100;
		double MaxLimitMoney = applyAmountD * percentD;
		double maxMoneyD = Double.parseDouble(maxMoney);
		return Math.min(MaxLimitMoney, maxMoneyD);
	}

	/**
	 * 选择商户
	 * 
	 * @param view
	 */
	@OnClick(R.id.tvCommercialName)
	private void clickChooseCommercialName(View view) {
		if (merchantList.size() > 0) {
			XFJRDialogUtil.showMerchantListDialog(getActivity(), "", merchantList, new MerchantSelectDialogClick(){

				@Override
				public void onClick(View view, XfjrDialog dialog, MerchantBean merchantBean) {
					CheckedTextView ctvContent = (CheckedTextView) view;
					if (!ctvContent.isChecked()) {
						ctvContent.setChecked(true);
						if (TextUtils.isEmpty(commercialNameId) || !commercialNameId.equals(merchantBean.getMerchantId())) {
							commercialNameId = merchantBean.getMerchantId();
							channelId = null;
							productId = null;
							channelList.clear();
							// 请求获取渠道列表
							reqChannelAndProductList(merchantBean.getMerchantId());
							selected(tvCommercialName,merchantBean.getMerchantName());
							unSelected(tvChannel, null);
							unSelected(tvProduct, null);
						}
						dialog.cancel();
					}
				}
				
			});
		} else { // 没有商户数据
			//reqMerchantInfo();// 获取商户信息
			ToastUtils.show(getActivity(), "没有商户数据", 0);//TODO
		}
	}

	/**
	 * 选择渠道
	 * 
	 * @param view
	 */
	@OnClick(R.id.tvChannel)
	private void clickChooseChannel(View view) {
		if (TextUtils.isEmpty(commercialNameId)) { // 判断是否已选商户
			ToastUtils.show(getActivity(), getString(R.string.please_select_merchant), 0);
		} else if (channelList.size() <= 0) { // 是否有渠道
			ToastUtils.show(getActivity(), "没有渠道数据", 0);//TODO
			reqChannelAndProductList(commercialNameId);
		} else {
			XFJRDialogUtil.showChannelListDialog(getActivity(), "", channelList, new ChannelSelectDialogClick() {
				
				@Override
				public void onClick(View view, XfjrDialog dialog, ChannelBean channelBean) {
					CheckedTextView ctvContent = (CheckedTextView) view;
					if (!ctvContent.isChecked()) {
						ctvContent.setChecked(true);
						if (TextUtils.isEmpty(channelId) || !channelId.equals(channelBean.getChannelId())) {
							channelId = channelBean.getChannelId();
							productId = null;
							List<ProductBean> list = channelBean.getProductList();
							productList.clear();
							productList.addAll(list);
							selected(tvChannel,channelBean.getChannelName());
							unSelected(tvProduct, null);
						}
						dialog.cancel();
					}
				}
			});
		}
	}
	
	/**
	 * 选择产品
	 * 
	 * @param view
	 */
	@OnClick(R.id.tvProduct)
	private void clickChooseProduct(View view) {
		if (TextUtils.isEmpty(commercialNameId)) { // 判断是否已选商户
			ToastUtils.show(getActivity(), getString(R.string.please_select_merchant), 0);
		} else if (TextUtils.isEmpty(channelId)) { // 判断是否已选渠道
			ToastUtils.show(getActivity(), getString(R.string.please_select_channel), 0);
		} else if (productList.size() <= 0) { // 判断是否有产品
			ToastUtils.show(getActivity(), "没有产品数据", 0);//TODO
		} else {
			XFJRDialogUtil.showProductListDialog(getActivity(), "", productList, new ProductSelectDialogClick() {
				
				@Override
				public void onClick(View view, XfjrDialog dialog, ProductBean productBean) {
					CheckedTextView ctvContent = (CheckedTextView) view;
					if (!ctvContent.isChecked()) {
						ctvContent.setChecked(true);
						productId = productBean.getProductId();
						selected(tvProduct, productBean.getProductName());		
						// 这里处理产品携带的信息
						productBean.setFrom(-1); // 广播更新首页用的flag值-1
						product = productBean;
						// 1. 跳转到对应的fragment
						XfjrMainActivity.productBean = productBean;
						// 2. 传入product 参数
//						ArgumentUtil.get().post(product);
						dialog.cancel();
					}
				}
			});
		}
	}
	
	/**
	 * 选择分期期数
	 * 
	 * @param view
	 */
	@OnClick(R.id.tvInstallmentNo)
	private void clickPeriodsId(View view) {
		etOrderTotal.clearFocus();
		etAplyAmount.clearFocus();
		if (XfjrMain.isNet) { // 根据【API020102】获取系统基本信息返回的分期期数列表选择
			periodNameList.clear(); // 先清空，再读取json获取期期数列表
			SystemBasicInfo info = SystemBasicJSONWRUtil.readSystemBasicInfo(getActivity());
			if(null != info){
				if(null == info.getPeriodList()){ // 测试数据
					periodNameList = new ArrayList<>();
					periodNameList.add(new PeriodListBean("1","3期"));
					periodNameList.add(new PeriodListBean("2","6期"));
					periodNameList.add(new PeriodListBean("3","12期"));
				} else {
					periodNameList = info.getPeriodList();
				}
			}
		} else {
			periodNameList.clear();
			periodNameList.add(new PeriodListBean("1","3期"));
			periodNameList.add(new PeriodListBean("2","6期"));
			periodNameList.add(new PeriodListBean("3","12期"));
		}
		if (periodNameList.size() <= 0) {
			//ToastUtils.show(getActivity(), getString(R.string.please_input_installment_num), 0);
		} else {
			XFJRDialogUtil.showPeriodListDialog(getActivity(), "", periodNameList, new PeriodSelectDialogClick() {
				
				@Override
				public void onClick(View view, XfjrDialog dialog, PeriodListBean bean) {
					CheckedTextView ctvContent = (CheckedTextView) view;
					if (!ctvContent.isChecked()) {
						ctvContent.setChecked(true);
						dialog.cancel();
						periodsId = bean.getPeriodsId();
						periods = bean.getConvNum(); // 传到下个页面的期数
						if (bean.getPeriodsName().contains("期")) {
							selected(tvInstallmentNo, bean.getPeriodsName());
						} else {
							selected(tvInstallmentNo, bean.getPeriodsName() + "期");
						}
					}
				}
			});
		}
	}
	
//	private void setStatus(String status) {
//		tvCommercialStatus.setVisibility(View.VISIBLE);
//		if("0".equals(status)){
//			tvCommercialStatus.setText(R.string.normal_status);
//			tvCommercialStatus.setTextColor(getActivity().getResources().getColor(R.color.status_normal));
//		} else if("1".equals(status)) {
//			tvCommercialStatus.setText(R.string.close_status);
//			tvCommercialStatus.setTextColor(getActivity().getResources().getColor(R.color.status_close));
//		}
//	}
	
	private void initListener() {
		etRate.addTextChangedListener(new RateTextWatcher(etRate, 2, 2)); // 限制只能输入0-100
		etClientName.addTextChangedListener(new LimitInputTextWatcher(etClientName, LimitInputTextWatcher.a_zA_Z_CN_REGEX)); // 限制只能输入中文和英文
//		etRate.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				if (s.length() > 0) {
//					String rate = s.toString();
//					if (rate.toString().endsWith(".")) {
//						rate = rate.replace(".", "");
//						Double r = Double.parseDouble(rate);
//						isCorrectRate = r < 100;
//					} else {
//						Double r = Double.parseDouble(rate);
//						isCorrectRate = r < 100;
//					}
//					if (!isCorrectRate) {
//						ToastUtils.show(getActivity(), getString(R.string.rate_can_not_exceed_percent100), 0);
//					}
//				}
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//
//			}
//		});
		etClientPhone.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() == 11) {
					if (PatternUtils.isMobile(s.toString())) {
						tvTip.setVisibility(View.GONE);
						isCorrectPhone = true;
					} else {
						// etClientPhone.setShakeAnimation(3);//设置抖动动画，一秒抖几次
						isCorrectPhone = false;
						tvTip.setVisibility(View.VISIBLE);
					}
				} else {
					isCorrectPhone = false;
					tvTip.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		etOrderTotal.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				etOrderTotal.onFocusChange(v, hasFocus); // 不调这个方法就不会显示delete图标
				if (!hasFocus) {
					TextUtil.suffix00(etOrderTotal);
				}
			}
			
		});
		etAplyAmount.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				etAplyAmount.onFocusChange(v, hasFocus); // 不调这个方法就不会显示delete图标
				if (!hasFocus) {
					TextUtil.suffix00(etAplyAmount);
				}
			}
			
		});
		etClientPhone.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				etClientPhone.onFocusChange(v, hasFocus); // 不调这个方法就不会显示delete图标
				if (hasFocus) {
					// 此处为得到焦点时的处理内容
				} else {
					// 此处为失去焦点时的处理内容
					if(!TextUtils.isEmpty(etClientPhone.getText())){
						if (PatternUtils.isMobile(etClientPhone.getText().toString())) {
							tvTip.setVisibility(View.GONE);
							isCorrectPhone = true;
						} else {
							// etClientPhone.setShakeAnimation(3);//设置抖动动画，一秒抖几次
							isCorrectPhone = false;
							tvTip.setVisibility(View.VISIBLE);
						}
					}
				}
			}
		});
	}

	/**
	 * 开始评估
	 */
	private void startAssessing() {
		if (isCorrectPhone) {
			commit(); // 提交网络请求
		} else {
			
		}
	}

	private void localCommit() {
		if (mObserver != null) {
			XfjrMain.businessId = "13141516"; // 获得业务id
			PreferencesUtil.put(XFJRConstant.KEY_CLIENT_PHONE, etClientPhone.getText().toString());
			mObserver.pushBackStack();
			btnRight.setEnabled(false);
		}
	}
	
	/**
	 * 数据提交
	 */
	private void commit() {
		if(XfjrMain.isNet){
			HttpRequest.addBussiness(getActivity(), params, new IHttpCallback<BusinessBean>() {

				@Override
				public void onSuccess(String url, BusinessBean result) {
					LogUtils.e("业务编号："+result);
					//ToastUtils.show(getActivity(), "提交成功", 0);
					if (mObserver != null) {
						((XfjrMainActivity)getActivity()).sendBR(0);
						XfjrMain.businessId = result.getBusinessId(); // 获得业务id
						PreferencesUtil.put(XFJRConstant.KEY_CLIENT_PHONE, etClientPhone.getText().toString());
						mObserver.pushBackStack();
						btnRight.setEnabled(false);
					}
				}

				@Override
				public void onError(String url, Throwable e) {
					UrlConfig.showErrorTips(getActivity(), e, true);
				}

				@Override
				public void onFinal(String url) {
					LogUtils.e(url);
				}
			});
		} else {
			localCommit();
		}
	}

	/**
	 * 设置单个按钮的文字（隐藏左边按钮）
	 * 
	 * @param right
	 */
	private void setRightBtnText(String right) {
		btnLeft.setVisibility(View.GONE);
		btnRight.setText(right);
	}

	@Override
	public void register(StepObserver observer) {
		mObserver = observer;
	}

	/**
	 * 空白处点击事件
	 * 
	 * @param view
	 */
	@OnClick(R.id.lltBlank)
	private void clickBlank(View view) {
		XFJRUtil.hideSoftInput(view);
		etOrderTotal.clearFocus();
		etAplyAmount.clearFocus();
	}
}
