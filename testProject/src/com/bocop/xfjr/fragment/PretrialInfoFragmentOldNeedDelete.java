//package com.bocop.xfjr.fragment;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.boc.jx.baseUtil.view.annotation.ViewInject;
//import com.boc.jx.baseUtil.view.annotation.event.OnClick;
//import com.boc.jx.httptools.http.callback.IHttpCallback;
//import com.boc.jx.tools.LogUtils;
//import com.bocop.jxplatform.R;
//import com.bocop.xfjr.XfjrMain;
//import com.bocop.xfjr.activity.XfjrMainActivity;
//import com.bocop.xfjr.adapter.CustomerTypeListAdapter;
//import com.bocop.xfjr.adapter.CustomerTypeListAdapter.OnClickDelDataListener;
//import com.bocop.xfjr.base.BaseCheckProcessFragment;
//import com.bocop.xfjr.bean.SystemBasicInfo;
//import com.bocop.xfjr.bean.SystemBasicInfo.HouseTypeBean;
//import com.bocop.xfjr.bean.SystemBasicInfo.JobLevelListBean;
//import com.bocop.xfjr.bean.pretrial.CustomType;
//import com.bocop.xfjr.bean.pretrial.PretrialParamBean;
//import com.bocop.xfjr.bean.pretrial.PretrialParamBean.DecisionOrdinaryListBean;
//import com.bocop.xfjr.bean.pretrial.PretrialParamBean.DescB;
//import com.bocop.xfjr.bean.pretrial.PretrialParamBean.DescC;
//import com.bocop.xfjr.bean.pretrial.PretrialParamBean.DescD;
//import com.bocop.xfjr.bean.pretrial.PretrialParamBean.DescE;
//import com.bocop.xfjr.bean.pretrial.PretrialResultBean;
//import com.bocop.xfjr.callback.CommunicationCallBack;
//import com.bocop.xfjr.config.HttpRequest;
//import com.bocop.xfjr.config.UrlConfig;
//import com.bocop.xfjr.constant.XFJRConstant;
//import com.bocop.xfjr.util.XFJRUtil;
//import com.bocop.xfjr.util.dialog.MaxDatePickerDialog;
//import com.bocop.xfjr.util.dialog.MaxDatePickerDialog.TimePickerDialogInterface;
//import com.bocop.xfjr.util.dialog.XFJRDialogUtil;
//import com.bocop.xfjr.util.dialog.XFJRDialogUtil.CustomTypeSelectDialogClick;
//import com.bocop.xfjr.util.dialog.XFJRDialogUtil.DialogClick;
//import com.bocop.xfjr.util.dialog.XFJRDialogUtil.HouseTypeSelectDialogClick;
//import com.bocop.xfjr.util.dialog.XFJRDialogUtil.JobLevelListSelectDialogClick;
//import com.bocop.xfjr.util.dialog.XFJRDialogUtil.StringSelectDialogClick;
//import com.bocop.xfjr.util.dialog.XfjrDialog;
//import com.bocop.xfjr.util.file.SystemBasicJSONWRUtil;
//import com.bocop.xfjr.view.MyListView;
//import com.bocop.xfjr.view.XFJRMoneyClearEditText;
//import com.bocop.yfx.utils.ToastUtils;
//
//import android.annotation.SuppressLint;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.CheckedTextView;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RadioGroup.OnCheckedChangeListener;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//public class PretrialInfoFragmentOldNeedDelete extends BaseCheckProcessFragment
//		implements OnClickListener {
//
//	@ViewInject(R.id.llContainer)
//	private LinearLayout llContainer;
//	@ViewInject(R.id.scrollView)
//	private ScrollView scrollView;
//	@ViewInject(R.id.iv_flow_add)
//	private ImageView ivFlowAdd;
//	@ViewInject(R.id.iv_flow_top)
//	private ImageView ivFlowTop;
//	private TextView tvVerificationMethod0;
//	private TextView tvCommonTime0;
//	private TextView tvVerificationMethod1;
//	private XFJRMoneyClearEditText etDownPayment;
//	private EditText etPayYear;
//	private TextView tvCommonTime1;
//	private MyListView rvCommonHead;
//	private View selectView;// 最开始的选择视图
//	private View commonView, socialView, homeView, ordinaryView, housingLoanView;
//	private TextView tvHousePropId;// 房产类型ID housePropId
//	private TextView tvCityId;// 房产所在城市cityId
//	// 房产信息核查方式houseCheck 1征信报告未结清房贷信息、2备案后的购房合同、3查档的房产证
//	private TextView tvHouseCheck;
//	private TextView tvHousePropId2;// 房产类型ID housePropId
//	private TextView tvCityId2;// 房产所在城市cityId
//	// 房产信息核查方式houseCheck 1征信报告未结清房贷信息、2备案后的购房合同、3查档的房产证
//	private TextView tvHouseCheck2;
//	private RadioGroup rgHasBondsMan, rgBondsManStatus, rgIsMatch, rgIsMatchSj;
//	private MaxDatePickerDialog mTimePickerDialog;
//	private CommunicationCallBack communicationCallBack;
//	private PretrialParamBean pretrialParamBean;
////	private DecisionGroupListBean decisionGroupListBeanHousingLoan; // 客群类
////	private DecisionGroupListBean decisionGroupListBeanCommon; // 客群类
//	private DecisionOrdinaryListBean decisionOrdinaryListBean00; // 普通类(00:申请人)
//	private DecisionOrdinaryListBean decisionOrdinaryListBean01; // 普通类(01:申请人配偶)
//	private DecisionOrdinaryListBean decisionOrdinaryListBean02; // 普通类(02:保证人)
//	private DecisionOrdinaryListBean decisionOrdinaryListBean03; // 普通类(03:保证人配偶)
//	private DescB decisionGroupListBeanHousingLoan; // 房贷类资料
//	private DescC decisionSceneListBeanCommon; // 公积金资料
//	private DescD decisionSceneListBeanSocial; // 社保资料
//	private DescE decisionSceneListBeanHome; // 房产类资料
////	private DecisionSceneListBean decisionSceneListBeanCommon; // 场景类
////	private DecisionSceneListBean decisionSceneListBeanSocial; // 场景类
////	private DecisionSceneListBean decisionSceneListBeanHome; // 场景类
//	private CustomType selectType = new CustomType(-1, "未选择", R.layout.xfjr_item_pretrial_new);
//	private List<CustomType> listData = new ArrayList<>();// 已选择的类型集合
//	private boolean marriedStatus = false; // 前一个fragment传过来的结婚状态
//	private boolean hasBonds;// 有无保证人
//	private boolean isBondsMarried;// 保证人是否已婚
//	private boolean isSelectProvidentFundInfo;// SelectProvidentFundInfo
//	private boolean isSocialCharityInfo;// SocialCharityInfo
//	private boolean isHousePropertyInfo;// HousePropertyInfo
////	private boolean ifReset = true;// ifReset
//	private String socialCheckId; // 社保核验方式id
//	private String gjCheckId; // 公积金核验方式id
//	private String jobId00; // 职位等级
//	private String jobId02; // 职位等级
//	private String housePropId;
//	private String housePropId2;
//	private String houseCheck;
//	private String houseCheck2;
//	private String commonTime0;
//	private String commonTime1;
//	private String isMatch = "N";// 通用类布局的 与客户单位是否匹配
//	private String isMatchSj = "N";// 社保类 与客户单位是否匹配
//
//	private static String fxModel;
//	private static List<CustomType> typeList = new ArrayList<>();// 保存所有类型及布局
//	private static LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
//			LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//	static {
//		// int random = (int) (SystemClock.currentThreadTimeMillis() % 3);
//		lParams.setMargins(0, 20, 0, 0);
//	}
//	
//	@Override
//	protected void initView() {
//		super.initView();
//		setVisibility(tvReset, View.VISIBLE);
//		//setVisibility(tvSave, View.VISIBLE);
//	}
//
//	@Override
//	protected boolean resetClick(View view) {
//		resetHousingLoanView();
//		resetSocialView();
//		resetOrdinaryViewEmpty();
//		resetHomeView();
//		resetCommonView();
//		return false;
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		return view = inflater.inflate(R.layout.xfjr_fragment_pretrial_customer_type_info,
//				container, false);
//	}
//
//	@Override
//	public void onHiddenChanged(boolean hidden) {
//		LogUtils.e("onHiddenChanged()-->" + hidden);
//		if (!hidden) {
//			marriedStatus = getArguments().getBoolean(XFJRConstant.KEY_MARRIED_STATUS);
//			pretrialParamBean = PretrialBasicInfoFragment.pretrialParamBean;
//		}
//		super.onHiddenChanged(hidden);
//	}
//
//	@Override
//	protected void initData() {
//		super.initData();
//		typeList.clear();
//		marriedStatus = getArguments().getBoolean(XFJRConstant.KEY_MARRIED_STATUS);
//		pretrialParamBean = PretrialBasicInfoFragment.pretrialParamBean;
//		initSelectView(); // 初始化初始选择视图
//		Fragment fragment = getParentFragment();
//		if (fragment instanceof CommunicationCallBack) {
//			communicationCallBack = (CommunicationCallBack) fragment;
//		}
//		try {
//			fxModel = ((XfjrMainActivity) getActivity()).productBean.getFxModel().trim();
//			//List<CustTypeBean> custTypeList = ((XfjrMainActivity) getActivity()).productBean.getCustType();
//		} catch (Exception e) {
//			fxModel = "ALL";
//		}
////			typeList.add(new CustomType(30, "普通类", R.layout.xfjr_item_pretrial_ordinary_new)); // descA普通客户资料 
////			typeList.add(new CustomType(10, "房贷类", R.layout.xfjr_item_pretrial_housing_loan)); // descB房贷类资料
////			typeList.add(new CustomType(11, "优质类", R.layout.xfjr_item_pretrial_common_new)); // 优质类
//			typeList.add(new CustomType(12, "公积金类", R.layout.xfjr_item_pretrial_common_new)); // descC公积金资料
////			typeList.add(new CustomType(20, "中行存量类", R.layout.xfjr_item_pretrial_common_new)); // 中行存量类
//			typeList.add(new CustomType(23, "社保类", R.layout.xfjr_item_pretrial_social)); // descD社保资料
//			typeList.add(new CustomType(24, "房产类", R.layout.xfjr_item_pretrial_home_new)); // descE房产类资料
////		switch (fxModel) {
////		case "ALL": 
////			typeList.add(new CustomType(30, "普通类", R.layout.xfjr_item_pretrial_ordinary_new)); // descA普通客户资料 
////			typeList.add(new CustomType(10, "房贷类", R.layout.xfjr_item_pretrial_housing_loan)); // descB房贷类资料
//////			typeList.add(new CustomType(11, "优质类", R.layout.xfjr_item_pretrial_common_new)); // 优质类
////			typeList.add(new CustomType(12, "公积金类", R.layout.xfjr_item_pretrial_common_new)); // descC公积金资料
//////			typeList.add(new CustomType(20, "中行存量类", R.layout.xfjr_item_pretrial_common_new)); // 中行存量类
////			typeList.add(new CustomType(23, "社保类", R.layout.xfjr_item_pretrial_social)); // descD社保资料
////			typeList.add(new CustomType(24, "房产类", R.layout.xfjr_item_pretrial_home_new)); // descE房产类资料
////			break;
////		case "1":
////			// fxModel = 1; // 1：客群类
////			typeList.add(new CustomType(10, "房贷类", R.layout.xfjr_item_pretrial_housing_loan)); // descB房贷类资料
//////			typeList.add(new CustomType(11, "优质类", R.layout.xfjr_item_pretrial_common_new)); // 优质类
////			typeList.add(new CustomType(12, "公积金类", R.layout.xfjr_item_pretrial_common_new)); // descC公积金资料
////			break;
////		case "2":
////			// fxModel = 2; // 2：场景类
//////			typeList.add(new CustomType(20, "中行存量类", R.layout.xfjr_item_pretrial_common_new)); // 中行存量类
////			typeList.add(new CustomType(21, "公积金类", R.layout.xfjr_item_pretrial_common_new)); // descC公积金资料
//////			typeList.add(new CustomType(22, "优质类", R.layout.xfjr_item_pretrial_common_new)); // 优质类
////			typeList.add(new CustomType(23, "社保类", R.layout.xfjr_item_pretrial_social)); // descD社保资料
////			typeList.add(new CustomType(24, "房产类", R.layout.xfjr_item_pretrial_home_new)); // descE房产类资料
////			break;
////		case "3":
////			// fxModel = 3; // 3：普通类 
////			typeList.add(new CustomType(30, "普通类", R.layout.xfjr_item_pretrial_ordinary_new)); // descA普通客户资料 
////			addTypeView(typeList.get(0)); // 直接显示，并且隐藏删除该类型的按钮
////			setFlowVisibility();
////			findView(ordinaryView, R.id.lltOrdinaryHead).setVisibility(View.GONE);
////			findView(ordinaryView, R.id.tvDelOrdinaryType).setVisibility(View.GONE);
////			findView(ordinaryView, R.id.lineBondsManInfo).setVisibility(View.GONE);
////			break;
////		default:
////			typeList.add(new CustomType(30, "普通类", R.layout.xfjr_item_pretrial_ordinary_new)); // descA普通客户资料 
////			typeList.add(new CustomType(10, "房贷类", R.layout.xfjr_item_pretrial_housing_loan)); // descB房贷类资料
//////			typeList.add(new CustomType(11, "优质类", R.layout.xfjr_item_pretrial_common_new)); // 优质类
////			typeList.add(new CustomType(12, "公积金类", R.layout.xfjr_item_pretrial_common_new)); // descC公积金资料
//////			typeList.add(new CustomType(20, "中行存量类", R.layout.xfjr_item_pretrial_common_new)); // 中行存量类
////			typeList.add(new CustomType(23, "社保类", R.layout.xfjr_item_pretrial_social)); // descD社保资料
////			typeList.add(new CustomType(24, "房产类", R.layout.xfjr_item_pretrial_home_new)); // descE房产类资料
////			break;
////		}
//	}
//
//	private void initSelectView() {
//		listData.add(selectType);
//		if (selectView == null) {
//			selectView = LayoutInflater.from(getActivity()).inflate(listData.get(0).getResid(), llContainer, false);// 最开始的选择视图
//			selectView.findViewById(R.id.tvSelectedType).setOnClickListener(this);
//			TextView tvSelectProvidentFundInfo = findTextView(selectView, R.id.tvSelectProvidentFundInfo);
//			View ivSelectProvidentFundInfo = findView(selectView, R.id.ivSelectProvidentFundInfo);
//			tvSelectProvidentFundInfo.setOnClickListener(this);
//			TextView tvSocialCharityInfo = findTextView(selectView, R.id.tvSocialCharityInfo);
//			View ivSocialCharityInfo = findView(selectView, R.id.ivSocialCharityInfo);
//			tvSocialCharityInfo.setOnClickListener(this);
//			TextView tvHousePropertyInfo = findTextView(selectView, R.id.tvHousePropertyInfo);
//			View ivHousePropertyInfo = findView(selectView, R.id.ivHousePropertyInfo);
//			tvHousePropertyInfo.setOnClickListener(this);
//			selectInfo(tvSelectProvidentFundInfo, ivSelectProvidentFundInfo);
//			selectInfo(tvSocialCharityInfo, ivSocialCharityInfo);
//			selectInfo(tvHousePropertyInfo, ivHousePropertyInfo);
//		}
//		setFlowVisibility();// 控制底部悬浮按钮显示隐藏
//		llContainer.addView(selectView);// 添加选择类型视图
//	}
//
//	private void selectInfo(final TextView tv, final View iv) {
//		tv.setOnClickListener(new OnClickListener() {
//
//			@SuppressLint("NewApi")
//			@SuppressWarnings("deprecation")
//			@Override
//			public void onClick(View view) {
//				iv.setVisibility(iv.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
//				tv.setBackground(iv.getVisibility() == View.VISIBLE ? getResources().getDrawable(R.drawable.xfjr_shape_circle_corner_red_tv) : getResources().getDrawable(R.drawable.xfjr_shape_circle_corner_gray_tv));
//				tv.setTextColor(iv.getVisibility() == View.VISIBLE ? getResources().getColor(R.color.xfjr_red) : getResources().getColor(R.color.xfjr_black6));
//				List<CustomType> types = new ArrayList<>();
//				types.addAll(typeList);
//				// 去掉已选择的类型
//				if (listData != null) {
//					for (CustomType type : listData) {
//						types.remove(type);
//					}
//				}
//				switch (view.getId()) {
//					case R.id.tvSelectProvidentFundInfo: // 公积金信息
//						if (iv.getVisibility() == View.INVISIBLE) {
//							addTypeView(types.get(0));
//						} else {
//							confirmDialog(R.id.tvDelCommonType);
//						}
//						setFlowVisibility();
//						break;
//					case R.id.tvSocialCharityInfo: // 社保信息
//						if (iv.getVisibility() == View.INVISIBLE) {
//							addTypeView(types.get(1));
//						} else {
//							confirmDialog(R.id.tvDelSocialType);
//						}
//						setFlowVisibility();
//						break;
//					case R.id.tvHousePropertyInfo: // 房产信息
//						if (iv.getVisibility() == View.INVISIBLE) {
//							addTypeView(types.get(2));
//						} else {
//							confirmDialog(R.id.tvDelHomeType);
//						}
//						setFlowVisibility();
//						break;
//				}
//			}
//		});
//	}
//	
//	@OnClick({ R.id.iv_flow_add, R.id.iv_flow_top })
//	@Override
//	public void onClick(View view) {
//		switch (view.getId()) {
//		case R.id.tvSelectedType:
//		case R.id.iv_flow_add:
//			flowAdd();
//			break;
//		case R.id.tvDelSocialType:// 删除社保类view
//			confirmDialog(R.id.tvDelSocialType);
//			break;
//		case R.id.tvDelCommonType:// 删除通用类view
//			confirmDialog(R.id.tvDelCommonType);
//			break;
//		case R.id.tvDelHomeType:// 删除房产类view
//			confirmDialog(R.id.tvDelHomeType);
//			break;
//		case R.id.tvDelOrdinaryType:// 删除普通客户类view
//			confirmDialog(R.id.tvDelOrdinaryType);
//			break;
//		case R.id.tvDelHousingLoanType:// 删除房貸类view
//			confirmDialog(R.id.tvDelHousingLoanType);
//			break;
//		case R.id.iv_flow_top:
//			scrollView.scrollTo(0, 0);
//			break;
//		default:
//			break;
//		}
//	}
//
//	private void flowAdd() {
//		List<CustomType> types = new ArrayList<>();
//		types.addAll(typeList);
//		// 去掉已选择的类型
//		if (listData != null) {
//			for (CustomType type : listData) {
//				types.remove(type);
//			}
//		}
//		alertSelectDialog("", types, new CustomTypeSelectDialogClick() {
//
//			@Override
//			public void onClick(View view, XfjrDialog dialog, CustomType type) {
//				CheckedTextView ctvContent = (CheckedTextView) view;
//				if (!ctvContent.isChecked()) {
//					ctvContent.setChecked(true);
//					addTypeView(type);
//					setFlowVisibility();
//					dialog.cancel();
//				}
//			}
//
//		});
//	}
//
//	/**
//	 * 删除view
//	 */
//	private void confirmDialog(final int id) {
//		XFJRDialogUtil.confirmDialog(getActivity(), "是否确认删除?", new DialogClick() {
//
//			@Override
//			public void onOkClick(View view, XfjrDialog dialog) {
//				switch (id) {
//				case R.id.tvDelSocialType:
//					deleteSeletedView(socialView);
//					break;
//				case R.id.tvDelCommonType:
//					deleteSeletedView(commonView);
//					break;
//				case R.id.tvDelHomeType:
//					deleteSeletedView(homeView);
//					break;
//				case R.id.tvDelOrdinaryType:
//					deleteSeletedView(ordinaryView);
//					break;
//				case R.id.tvDelHousingLoanType:
//					deleteSeletedView(housingLoanView);
//					break;
//				}
//				setFlowVisibility();
//				dialog.dismiss();
//			}
//
//			@Override
//			public void onCancelClick(View view, XfjrDialog dialog) {
//				dialog.dismiss();
//			}
//		}).show();
//	}
//
//	private void deleteSeletedView(View view) {
//		List<CustomType> tmpList = new ArrayList<>();
//		tmpList.addAll(listData);
//		for (CustomType customType : tmpList) {
//			int cvId = View.inflate(getActivity(), customType.getResid(), null).getId();
//			if (cvId == view.getId()) {
//				listData.remove(customType);
//				LogUtils.e("delete:" + customType.gettName());
//				if (view != commonView)
//					break;
//			}
//		}
//		llContainer.removeView(view);
//		view = null;
//		decisionGroupListBeanHousingLoan = null; // 客群类1
////		decisionGroupListBeanCommon = null; // 客群类2-->相同内容，不同类型key
//		decisionSceneListBeanCommon = null; // 场景类1-->相同内容，不同类型key
//		decisionSceneListBeanSocial = null; // 场景类2
//		decisionSceneListBeanHome = null; // 场景类3
//		decisionOrdinaryListBean00 = null; // 普通类1
//		decisionOrdinaryListBean01 = null; // 普通类2
//		decisionOrdinaryListBean02 = null; // 普通类3
//		decisionOrdinaryListBean03 = null; // 普通类4
//		if (view == commonView && null != rvCommonHead) {
//			CustomerTypeListAdapter headAdapter = (CustomerTypeListAdapter) rvCommonHead
//					.getAdapter();
//			headAdapter.notifyDataSetChanged();
//		}
//
//	}
//
//	/**
//	 * 增加选择的种类视图
//	 * 
//	 * @param type
//	 */
//	protected void addTypeView(CustomType type) {
//		if (listData.size() == 1 && listData.get(0).getTid() == -1) {
//			llContainer.removeAllViews();
//			listData.remove(0);
//		}
//		// 已存在相同view的不在添加view
//		List<CustomType> tmpCustomTypes = new ArrayList<>();
//		tmpCustomTypes.addAll(listData);
//		for (CustomType customType : tmpCustomTypes) {
//			if (customType.getResid() == type.getResid()) {
//				listData.add(type);
//				((CustomerTypeListAdapter) rvCommonHead.getAdapter()).notifyDataSetChanged();
//				return;
//			}
//		}
//		listData.add(type);
//		switch (type.getResid()) {
//		case R.layout.xfjr_item_pretrial_common_new:// 综合commonView
//			initCommonView(type);
//			break;
//		case R.layout.xfjr_item_pretrial_social:// 社保类客户 socialView
//			initSocialView(type);
//			socialView.setTag(type.getTid());
//			break;
//		case R.layout.xfjr_item_pretrial_home_new:// 房产类客户homeView
//			initHomeView(type);
//			homeView.setTag(type.getTid());
//			break;
//		case R.layout.xfjr_item_pretrial_ordinary_new:// 普通类客户ordinaryView
//			initOrdinaryView(type);
//			ordinaryView.setTag(type.getTid());
//			break;
//		case R.layout.xfjr_item_pretrial_housing_loan:// 普通类客户ordinaryView
//			initHousingLoanView(type);
//			housingLoanView.setTag(type.getTid());
//			break;
//		}
//
//	}
//
//	/**
//	 * 房贷类
//	 * 
//	 * @param type
//	 */
//	private void initHousingLoanView(CustomType type) {
//		housingLoanView = LayoutInflater.from(getActivity()).inflate(type.getResid(), llContainer,
//				false);//
//		findTextView(housingLoanView, R.id.tvCustormType).setText(type.gettName());
//		setDrawableRight(findTextView(housingLoanView, R.id.ivOnOff),
//				findView(housingLoanView, R.id.ivSocialView));
//		findView(housingLoanView, R.id.tvDelHousingLoanType).setOnClickListener(this);
//		etDownPayment = (XFJRMoneyClearEditText) findView(housingLoanView, R.id.etDownPayment); // 首付款金额
//		etPayYear = (EditText) findView(housingLoanView, R.id.etPayYear); // 已缴纳年限
//		llContainer.addView(housingLoanView);
//	}
//
//	/**
//	 * 加载普通类型view
//	 * 
//	 * @param type
//	 */
//	private void initOrdinaryView(CustomType type) {
//		ordinaryView = LayoutInflater.from(getActivity()).inflate(type.getResid(), llContainer,
//				false);//
//		findTextView(ordinaryView, R.id.tvOrdinaryCustormType).setText(type.gettName());
//		setDrawableRight(findTextView(ordinaryView, R.id.ivOrdinaryOnOff),
//				findView(ordinaryView, R.id.llOrdinaryView));
//		findView(ordinaryView, R.id.tvDelOrdinaryType).setOnClickListener(this);
//		setDrawableRight2(findTextView(ordinaryView, R.id.ivHideCustomWorkStub),
//				findView(ordinaryView, R.id.llCustomWorkStub));
//		setDrawableRight2(findTextView(ordinaryView, R.id.ivHideCustomHomeStub),
//				findView(ordinaryView, R.id.llCustomHomeStub));
//		setDrawableRight2(findTextView(ordinaryView, R.id.ivHideBondsStub),
//				findView(ordinaryView, R.id.llBondsStub));
//		findTextView(ordinaryView, R.id.tvJobId00).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				chooseJobId(findTextView(ordinaryView, R.id.tvJobId00));
//			}
//		});
//		findTextView(ordinaryView, R.id.tvJobId01).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				chooseJobId(findTextView(ordinaryView, R.id.tvJobId01));
//			}
//		});
//		findTextView(ordinaryView, R.id.tvJobId02).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				chooseJobId(findTextView(ordinaryView, R.id.tvJobId02));
//			}
//		});
//		findTextView(ordinaryView, R.id.tvJobId03).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				chooseJobId(findTextView(ordinaryView, R.id.tvJobId03));
//			}
//		});
//		changeOrdinarySpouseView(type);
//		setDrawableRight2(findTextView(ordinaryView, R.id.ivHideBondsSpouseStub),
//				findView(ordinaryView, R.id.llBondsSpouseStub));
//		setDrawableRight2(findTextView(ordinaryView, R.id.ivHideBondsHomeStub),
//				findView(ordinaryView, R.id.llBondsHomeStub));
//		rgHasBondsMan = (RadioGroup) ordinaryView.findViewById(R.id.rgHasBondsMan);
//		rgBondsManStatus = (RadioGroup) ordinaryView.findViewById(R.id.rgBondsManStatus);
//		final RadioButton rgBondsManNoMarried = (RadioButton) ordinaryView
//				.findViewById(R.id.rgBondsManNoMarried);
//		final LinearLayout llShowWhenHasBonds = (LinearLayout) findView(ordinaryView,
//				R.id.llShowWhenHasBonds);
//		final LinearLayout llBondsManHouseInfo = (LinearLayout) findView(ordinaryView,
//				R.id.llBondsManHouseInfo);
//		final LinearLayout llBondsManSuposeInfo = (LinearLayout) findView(ordinaryView,
//				R.id.llBondsManSuposeInfo);
//		initOrdinaryRadioGroupChange(rgHasBondsMan, new ChangeCallBack() {
//
//			@Override
//			public void onChange(RadioGroup group, int checkedId) {
//				switch (checkedId) {
//				case R.id.rbHasBondsMan:
//					hasBonds = true;
//					llShowWhenHasBonds.setVisibility(View.VISIBLE);
//					llBondsManHouseInfo.setVisibility(View.VISIBLE);
//					break;
//
//				case R.id.rbNoBondsMan:
//					hasBonds = false;
//					isBondsMarried = false;
//					rgBondsManNoMarried.setChecked(true);
//					llBondsManSuposeInfo.setVisibility(View.GONE);
//					llShowWhenHasBonds.setVisibility(View.GONE);
//					llBondsManHouseInfo.setVisibility(View.GONE);
//					break;
//				}
//
//			}
//		});
//		initOrdinaryRadioGroupChange(rgBondsManStatus, new ChangeCallBack() {
//
//			@Override
//			public void onChange(RadioGroup group, int checkedId) {
//				switch (checkedId) {
//				case R.id.rgBondsManMarried:
//					isBondsMarried = true;
//					llBondsManSuposeInfo.setVisibility(View.VISIBLE);
//					break;
//
//				case R.id.rgBondsManNoMarried:
//					llBondsManSuposeInfo.setVisibility(View.GONE);
//					isBondsMarried = false;
//					break;
//				}
//
//			}
//		});
//		llContainer.addView(ordinaryView);
//	}
//
//	/**
//	 * 选择房产类型
//	 * 
//	 * @param tv
//	 */
//	protected void chooseHouseType(final TextView tv) {
//		final List<HouseTypeBean> list = new ArrayList<>();
//		if (XfjrMain.isNet) {
//			list.clear();
//			SystemBasicInfo info = SystemBasicJSONWRUtil.readSystemBasicInfo(getActivity());
//			if (null != info) {
//				if (null != info.getJobLevelList()) {
//					list.addAll(info.getHouseTypeList());
//				} else {
//					list.clear();
//					list.add(new HouseTypeBean("1", "类型1"));
//					list.add(new HouseTypeBean("2", "类型2"));
//				}
//			} else {
//				list.clear();
//				list.add(new HouseTypeBean("1", "类型1"));
//				list.add(new HouseTypeBean("2", "类型2"));
//			}
//		} else {
//			list.clear();
//			list.add(new HouseTypeBean("1", "类型1"));
//			list.add(new HouseTypeBean("2", "类型2"));
//		}
//		XFJRDialogUtil.showHouseTypeDialog(getActivity(), "", list,
//				new HouseTypeSelectDialogClick() {
//
//					@Override
//					public void onClick(View view, XfjrDialog dialog, HouseTypeBean bean) {
//						CheckedTextView ctvContent = (CheckedTextView) view;
//						if (!ctvContent.isChecked()) {
//							ctvContent.setChecked(true);
//							switch (tv.getId()) {
//							case R.id.tvHousePropId:
//								housePropId = bean.getHousePropId();
//								break;
//							case R.id.tvHousePropId2:
//								housePropId2 = bean.getHousePropId();
//								break;
//							}
//							selected(tv, bean.getHousePropName());
//							dialog.cancel();
//						}
//					}
//				});
//	}
//
//	/**
//	 * 选择职位等级
//	 * 
//	 * @param tv
//	 */
//	protected void chooseJobId(final TextView tv) {
//		final List<JobLevelListBean> list = new ArrayList<>();
//		if (XfjrMain.isNet) {
//			list.clear();
//			SystemBasicInfo info = SystemBasicJSONWRUtil.readSystemBasicInfo(getActivity());
//			if (null != info) {
//				if (null != info.getJobLevelList()) {
//					list.addAll(info.getJobLevelList());
//				} else {
//					list.clear();
//					list.add(new JobLevelListBean("1", "职位1"));
//					list.add(new JobLevelListBean("2", "职位2"));
//				}
//			}
//		} else {
//			list.clear();
//			list.add(new JobLevelListBean("1", "职位1"));
//			list.add(new JobLevelListBean("2", "职位2"));
//		}
//		XFJRDialogUtil.showJobLevelListDialog(getActivity(), "", list,
//				new JobLevelListSelectDialogClick() {
//
//					@Override
//					public void onClick(View view, XfjrDialog dialog, JobLevelListBean bean) {
//						CheckedTextView ctvContent = (CheckedTextView) view;
//						if (!ctvContent.isChecked()) {
//							ctvContent.setChecked(true);
//							switch (tv.getId()) {
//							case R.id.tvJobId00:
//								jobId00 = bean.getJobId();
//								break;
//							case R.id.tvJobId02:
//								jobId02 = bean.getJobId();
//								break;
//							}
//							selected(tv, bean.getJobName());
//							dialog.cancel();
//						}
//
//					}
//				});
//	}
//
//	/**
//	 * 设置radiogroup变化监听
//	 * 
//	 * @param radioGroup
//	 * @param callBack
//	 */
//	private void initOrdinaryRadioGroupChange(RadioGroup radioGroup,
//			final ChangeCallBack callBack) {
//		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				callBack.onChange(group, checkedId);
//			}
//		});
//	}
//
//	/**
//	 * 添加房产类view
//	 * 
//	 * @param type
//	 */
//	private void initHomeView(CustomType type) {
//		homeView = LayoutInflater.from(getActivity()).inflate(type.getResid(), llContainer, false);//
//		findTextView(homeView, R.id.tvHomeCustormType).setText(type.gettName());
//		setDrawableRight(findTextView(homeView, R.id.ivHomeOnOff),
//				findView(homeView, R.id.ivHomeView));
//		tvHousePropId = findTextView(homeView, R.id.tvHousePropId);
//		tvHousePropId.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				chooseHouseType(tvHousePropId);
//			}
//		});
//		tvCityId = findTextView(homeView, R.id.tvCityId);
//		tvCityId.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				final List<String> list = new ArrayList<>();
//				list.add("深圳市罗湖区");
//				list.add("深圳市南山区");
//				alertSelectDialog("", list, new StringSelectDialogClick() {
//
//					@Override
//					public void onClick(View view, XfjrDialog dialog) {
//						CheckedTextView ctvContent = (CheckedTextView) view;
//						int position = (int) ctvContent.getTag();
//						if (!ctvContent.isChecked()) {
//							ctvContent.setChecked(true);
//							selected(tvCityId, list.get(position));
//							dialog.cancel();
//						}
//					}
//				});
//			}
//		});
//		tvHouseCheck = findTextView(homeView, R.id.tvHouseCheck);
//		tvHouseCheck.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				final List<String> list = new ArrayList<>();
//				list.addAll(Arrays.asList(getActivity().getResources().getStringArray(R.array.selected_house_check)));
//				alertSelectDialog("", list, new StringSelectDialogClick() {
//
//					@Override
//					public void onClick(View view, XfjrDialog dialog) {
//						CheckedTextView ctvContent = (CheckedTextView) view;
//						int position = (int) ctvContent.getTag();
//						if (!ctvContent.isChecked()) {
//							ctvContent.setChecked(true);
//							houseCheck = (position + 1) + "";
//							selected(tvHouseCheck, list.get(position));
//							dialog.cancel();
//						}
//					}
//				});
//			}
//		});
////		tvHousePropId2 = findTextView(homeView, R.id.tvHousePropId2);
////		tvHousePropId2.setOnClickListener(new OnClickListener() {
////
////			@Override
////			public void onClick(View v) {
////				chooseHouseType(tvHousePropId2);
////			}
////		});
////		tvCityId2 = findTextView(homeView, R.id.tvCityId2);
////		tvCityId2.setOnClickListener(new OnClickListener() {
////
////			@Override
////			public void onClick(View v) {
////				final List<String> list = new ArrayList<>();
////				list.add("深圳市罗湖区");
////				list.add("深圳市南山区");
////				alertSelectDialog("", list, new StringSelectDialogClick() {
////
////					@Override
////					public void onClick(View view, XfjrDialog dialog) {
////						CheckedTextView ctvContent = (CheckedTextView) view;
////						int position = (int) ctvContent.getTag();
////						if (!ctvContent.isChecked()) {
////							ctvContent.setChecked(true);
////							selected(tvCityId2, list.get(position));
////							dialog.cancel();
////						}
////					}
////				});
////			}
////		});
////		tvHouseCheck2 = findTextView(homeView, R.id.tvHouseCheck2);
////		tvHouseCheck2.setOnClickListener(new OnClickListener() {
////
////			@Override
////			public void onClick(View v) {
////				final List<String> list = new ArrayList<>();
////				list.addAll(Arrays.asList(getActivity().getResources().getStringArray(R.array.selected_house_check)));
////				alertSelectDialog("", list, new StringSelectDialogClick() {
////
////					@Override
////					public void onClick(View view, XfjrDialog dialog) {
////						CheckedTextView ctvContent = (CheckedTextView) view;
////						int position = (int) ctvContent.getTag();
////						if (!ctvContent.isChecked()) {
////							ctvContent.setChecked(true);
////							houseCheck2 = (position + 1) + "";
////							selected(tvHouseCheck2, list.get(position));
////							dialog.cancel();
////						}
////					}
////				});
////			}
////		});
////		findView(homeView, R.id.tvDelHomeType).setOnClickListener(this);
//		findView(homeView, R.id.llAddHomeInfo).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				setViewVisibility(homeView, R.id.llSecondHomeInfo,
//						findView(homeView, R.id.llSecondHomeInfo).getVisibility() == View.VISIBLE
//								? View.GONE : View.VISIBLE);
//				v.setVisibility(v.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
//			}
//		});
//		findView(homeView, R.id.ivClose).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				setViewVisibility(homeView, R.id.llAddHomeInfo,
//						findView(homeView, R.id.llAddHomeInfo).getVisibility() == View.VISIBLE
//								? View.GONE : View.VISIBLE);
//				setViewVisibility(homeView, R.id.llSecondHomeInfo,
//						findView(homeView, R.id.llSecondHomeInfo).getVisibility() == View.VISIBLE
//								? View.GONE : View.VISIBLE);
//
//			}
//		});
//		llContainer.addView(homeView);
//	}
//
//	/**
//	 * 添加社保类view
//	 * 
//	 * @param type
//	 */
//	private void initSocialView(CustomType type) {
//		socialView = LayoutInflater.from(getActivity()).inflate(type.getResid(), llContainer,
//				false);//
//		findTextView(socialView, R.id.tvSocialCustormType).setText(type.gettName());
//		setDrawableRight(findTextView(socialView, R.id.ivSocialOnOff),
//				findView(socialView, R.id.ivSocialView));
//		findView(socialView, R.id.tvDelSocialType).setOnClickListener(this);
//		tvVerificationMethod1 = (TextView) findView(socialView, R.id.tvSocialVerificationMethod); // 核验方式
//		tvVerificationMethod1.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				final List<String> list = new ArrayList<>();
//				list.addAll(Arrays.asList(getActivity().getResources().getStringArray(R.array.selected_social_verification_method)));
//				alertSelectDialog("", list, new StringSelectDialogClick() {
//
//					@Override
//					public void onClick(View view, XfjrDialog dialog) {
//						CheckedTextView ctvContent = (CheckedTextView) view;
//						int position = (int) ctvContent.getTag();
//						if (!ctvContent.isChecked()) {
//							ctvContent.setChecked(true);
//							socialCheckId = (position + 1) + "";
//							selected(tvVerificationMethod1, list.get(position));
//							dialog.cancel();
//						}
//					}
//				});
//			}
//		});
//		tvCommonTime1 = (TextView) findView(socialView, R.id.tvSocialTime); // 缴纳起始日
//		tvCommonTime1.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				mTimePickerDialog = new MaxDatePickerDialog(baseActivity,
//						new TimePickerDialogInterface() {
//
//							@Override
//							public void positiveListener() {
//								int y = mTimePickerDialog.getYear();
//								int m = mTimePickerDialog.getMonth();
//								int d = mTimePickerDialog.getDay();
//								commonTime1 = y + "-" + m + "-" + d;
//								selected(tvCommonTime1, commonTime1);
//							}
//
//							@Override
//							public void negativeListener() {
//
//							}
//						});
//				mTimePickerDialog.showDatePickerDialog();
//			}
//		});
//		rgIsMatchSj = (RadioGroup) socialView.findViewById(R.id.rgIsMatch);
//		initOrdinaryRadioGroupChange(rgIsMatchSj, new ChangeCallBack() {
//			
//			@Override
//			public void onChange(RadioGroup group, int checkedId) {
//				switch (checkedId) {
//				case R.id.rbYes:
//					isMatchSj = "Y";
//					break;
//					
//				case R.id.rbNo:
//					isMatchSj = "N";
//					break;
//				}
//				
//			}
//		});
//		llContainer.addView(socialView);
//	}
//
//	/**
//	 * 添加通用类view
//	 * 
//	 * @param type
//	 */
//	private void initCommonView(CustomType type) {
//		commonView = LayoutInflater.from(getActivity()).inflate(type.getResid(), llContainer,
//				false);//
//		setDrawableRight(findTextView(commonView, R.id.ivCommonOnOff),
//				findView(commonView, R.id.ivCommonView));
//		rgIsMatch = (RadioGroup) commonView.findViewById(R.id.rgIsMatch);
//		initOrdinaryRadioGroupChange(rgIsMatch, new ChangeCallBack() {
//
//			@Override
//			public void onChange(RadioGroup group, int checkedId) {
//				switch (checkedId) {
//				case R.id.rbYes:
//					isMatch = "Y";
//					break;
//
//				case R.id.rbNo:
//					isMatch = "N";
//					break;
//				}
//
//			}
//		});
//		findView(commonView, R.id.tvDelCommonType).setOnClickListener(this);
//		rvCommonHead = (MyListView) commonView.findViewById(R.id.rvCommonHead);
//		CustomerTypeListAdapter customerTypeListAdapter = new CustomerTypeListAdapter(getActivity(),
//				listData);
//		rvCommonHead.setAdapter(customerTypeListAdapter);
//		customerTypeListAdapter.setOnClickDelDataListener(new OnClickDelDataListener() {
//
//			@Override
//			public void onClickDelData(int position) {
//				delCommonData(position);
//			}
//		});
//		tvVerificationMethod0 = findTextView(commonView, R.id.tvCheck); // 核验方式
//		tvVerificationMethod0.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				final List<String> list = new ArrayList<>();
//				list.addAll(Arrays.asList(getActivity().getResources().getStringArray(R.array.selected_gjj_verification_method)));
//				alertSelectDialog("", list, new StringSelectDialogClick() {
//
//					@Override
//					public void onClick(View view, XfjrDialog dialog) {
//						CheckedTextView ctvContent = (CheckedTextView) view;
//						int position = (int) ctvContent.getTag();
//						if (!ctvContent.isChecked()) {
//							ctvContent.setChecked(true);
//							gjCheckId = "" + (position + 1);
//							selected(tvVerificationMethod0, list.get(position));
//							dialog.cancel();
//						}
//					}
//				});
//			}
//		});
//		tvCommonTime0 = findTextView(commonView, R.id.tvCommonTime); // 缴纳起始日
//		tvCommonTime0.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				mTimePickerDialog = new MaxDatePickerDialog(baseActivity,
//						new TimePickerDialogInterface() {
//
//							@Override
//							public void positiveListener() {
//								int y = mTimePickerDialog.getYear();
//								int m = mTimePickerDialog.getMonth();
//								int d = mTimePickerDialog.getDay();
//								commonTime0 = y + "-" + m + "-" + d;
//								selected(tvCommonTime0, commonTime0);
//							}
//
//							@Override
//							public void negativeListener() {
//
//							}
//						});
//				mTimePickerDialog.showDatePickerDialog();
//			}
//		});
//		llContainer.addView(commonView);
//	}
//
//	/**
//	 * // 初始化以及点击事件缩小打开几个模块时候的图片切换，以及显示隐藏 大视图 即客户类型视图的显示隐藏切换
//	 * 
//	 * @param TextView
//	 * @param layout
//	 */
//	private void setDrawableRight(final TextView tv, final View layout) {
//		tv.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				@SuppressWarnings("deprecation")
//				Drawable drawable = getResources()
//						.getDrawable(layout.getVisibility() == View.VISIBLE
//								? R.drawable.xfjr_pretrial_close : R.drawable.xfjr_pretrial_open);
//				// drawable.getMinimumWidth() drawable.getMinimumHeight()
//				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//				tv.setCompoundDrawables(null, null, drawable, null);
//				layout.setVisibility(
//						layout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
//			}
//		});
//	}
//
//	private void setDrawableRight2(final TextView tv, final View layout) {
//		tv.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				@SuppressWarnings("deprecation")
//				Drawable drawable = getResources().getDrawable(
//						layout.getVisibility() == View.VISIBLE ? R.drawable.xfjr_pretrial_tranclose
//								: R.drawable.xfjr_pretrial_tranopen);
//				// drawable.getMinimumWidth() drawable.getMinimumHeight()
//				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//				tv.setCompoundDrawables(null, null, drawable, null);
//				layout.setVisibility(
//						layout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
//			}
//		});
//	}
//
//	/**
//	 * 选择框
//	 * 
//	 * @param selected
//	 * @param list
//	 * @param dialogClick
//	 */
//	@SuppressLint("NewApi")
//	private void alertSelectDialog(final String selected, List<String> list,
//			final StringSelectDialogClick dialogClick) {
//		LogUtils.e("集合：" + list.toString());
//		XFJRDialogUtil.alertStringSelectDialog(getActivity(), selected, list, dialogClick);
//	}
//
//	/**
//	 * 设置布局显示隐藏
//	 * 
//	 * @param view
//	 * @param id
//	 * @param visibility
//	 */
//	private void setViewVisibility(View view, int id, int visibility) {
//		findView(view, id).setVisibility(visibility);
//	}
//
//	/**
//	 * 找TextView
//	 * 
//	 * @param view
//	 * @param id
//	 * @return
//	 */
//	private TextView findTextView(View view, int id) {
//		return (TextView) view.findViewById(id);
//	}
//
//	/**
//	 * 找TextView
//	 * 
//	 * @param view
//	 * @param id
//	 * @return
//	 */
//	private EditText findEditText(View view, int id) {
//		return (EditText) view.findViewById(id);
//	}
//
//	/**
//	 * 找控件
//	 * 
//	 * @param view
//	 * @param id
//	 * @return
//	 */
//	private View findView(View view, int id) {
//		return view.findViewById(id);
//	}
//
//	/**
//	 * // 判断下面flow的两个iamgeview的显示与隐藏 一个是添加类型的按钮 另一个是到顶 同时判断
//	 * 当删除了所有客户类型视图时，添加未选择的视图
//	 */
//	private void setFlowVisibility() {
//		LogUtils.e("setFlowVisibility:listData size:" + listData.size());
//		if (listData.size() == 0) {
//			initSelectView();
//		}
////		if (listData.contains(selectType) || listData.size() >= typeList.size()) {
////			ivFlowAdd.setVisibility(View.GONE);
////		} else {
////			ivFlowAdd.setVisibility(View.VISIBLE);
////		}
////		if (listData.size() >= 3) {
////			ivFlowTop.setVisibility(View.VISIBLE);
////		} else {
////			ivFlowTop.setVisibility(View.GONE);
////		}
//	}
//
//	private boolean checkEmpty() {
//		boolean isEmpty = false;
//		if (listData != null && listData.size() != 0) {
//			if (listData.get(0).getTid() == -1) { // 未选择
//				ToastUtils.show(getActivity(), getString(R.string.please_select_applicant_type), 0);
//				return true;
//			}
//			boolean isCommonViewExist = false;
//			for (int i = 0; i < llContainer.getChildCount(); i++) {
//				View child = llContainer.getChildAt(i);
//				// int childViewId = child.getId();
//				if (commonView == child && !isCommonViewExist) {// common
//					LogUtils.e("commonView" + i);
//					isCommonViewExist = true;
//					isEmpty = checkedCommonViewEmpty();
//					if (isEmpty)
//						break;
//				} else if (homeView == child) {// 房产
//					LogUtils.e("homeView" + i);
//					isEmpty = checkedHomeViewEmpty();
//					if (isEmpty)
//						break;
//				} else if (ordinaryView == child) {// 普通
//					LogUtils.e("ordinaryView" + i);
//					isEmpty = checkedOrdinaryViewEmpty();
//					if (isEmpty)
//						break;
//				} else if (socialView == child) {// 社保
//					LogUtils.e("socialView" + i);
//					isEmpty = checkedSocialViewEmpty();
//					if (isEmpty)
//						break;
//				} else if (housingLoanView == child) {// 房贷
//					LogUtils.e("housingLoanView" + i);
//					isEmpty = checkedHousingLoanViewEmpty();
//					if (isEmpty)
//						break;
//				}
//				LogUtils.e(isEmpty + "----->");
//			}
//		}
//		return isEmpty;
//	}
//
//	private void showEmptyTips(TextView textView) {
//		ToastUtils.show(getActivity(), textView.getHint().toString(), 0);
//	}
//
//	private boolean checkedHousingLoanViewEmpty() {
//		if (null != housingLoanView) {
//			String downPayment = etDownPayment.getTextString();
//			if (TextUtils.isEmpty(downPayment)) {
//				showEmptyTips(etDownPayment);
//				return true;
//			}
//			String payYear = etPayYear.getText().toString().trim();
//			if (TextUtils.isEmpty(payYear)) {
//				showEmptyTips(etPayYear);
//				return true;
//			}
//			if (null == decisionGroupListBeanHousingLoan) {
//				decisionGroupListBeanHousingLoan = new DescB();
//			}
//			decisionGroupListBeanHousingLoan.setFirPayPay(downPayment);
//			decisionGroupListBeanHousingLoan.setRepayYear(payYear);
////			decisionGroupListBeanHousingLoan.setCustomerType("" + housingLoanView.getTag());
//		} else {
//			return false;
//		}
//		return false;
//	}
//
//	private void resetHousingLoanView() {
//		if (null != housingLoanView) {
//			etDownPayment.setText("");
//			etPayYear.setText("");
//			etDownPayment.clearFocus();
//			etPayYear.clearFocus();
//		}
//	}
//
//	private boolean checkedSocialViewEmpty() {
//		if (null != socialView) {
//			// fxModel = 2; // 2：场景类
//			String verificationMethod1 = findTextView(socialView, R.id.tvSocialVerificationMethod).getText().toString();
//			String commonCompany1 = findEditText(socialView, R.id.etSocialCompany).getText().toString();
//			String monthPay = ((XFJRMoneyClearEditText)findEditText(socialView, R.id.etPreMonthPay)).getTextString();
//			String monthRate = findEditText(socialView, R.id.etPreMonthRate).getText().toString();
//			String comMonthPay = ((XFJRMoneyClearEditText)findEditText(socialView, R.id.etComMonthPay)).getTextString();
//			String comMonthRate = findEditText(socialView, R.id.etComMonthRate).getText().toString();
//			if (TextUtils.isEmpty(verificationMethod1)) {
//				showEmptyTips(findTextView(socialView, R.id.tvSocialVerificationMethod));
//				return true;
//			}
//			if (TextUtils.isEmpty(commonCompany1)) {
//				showEmptyTips(findEditText(socialView, R.id.etSocialCompany));
//				return true;
//			}
//			if (TextUtils.isEmpty(commonTime1)) {
//				showEmptyTips(tvCommonTime1);
//				return true;
//			}
//			if (TextUtils.isEmpty(monthPay)) {
//				showEmptyTips(findEditText(socialView, R.id.etPreMonthPay));
//				return true;
//			}
//			if (TextUtils.isEmpty(monthRate)) {
//				showEmptyTips(findEditText(socialView, R.id.etPreMonthRate));
//				return true;
//			} 
//			if (TextUtils.isEmpty(comMonthPay)) {
//				showEmptyTips(findEditText(socialView, R.id.etComMonthPay));
//				return true;
//			}
//			if (TextUtils.isEmpty(comMonthRate)) {
//				showEmptyTips(findEditText(socialView, R.id.etComMonthRate));
//				return true;
//			} 
//			if (null == decisionSceneListBeanSocial) {
//				decisionSceneListBeanSocial = new DescD();
//			}
////			decisionSceneListBeanSocial.setCustomerType(socialView.getTag() + "");
//			decisionSceneListBeanSocial.setSjChect(socialCheckId);
//			decisionSceneListBeanSocial.setSjPayCom(commonCompany1); //社保金缴存单位
//			decisionSceneListBeanSocial.setSjPayStart(commonTime1);
//			decisionSceneListBeanSocial.setSjMatch(isMatchSj);
//			decisionSceneListBeanSocial.setSjPreMonthPay(monthPay);
//			decisionSceneListBeanSocial.setSjPreMonthRate(monthRate);
//			decisionSceneListBeanSocial.setSjComMonthPay(comMonthPay);
//			decisionSceneListBeanSocial.setSjComMonthRate(comMonthRate);
//		} else {
//			return false;
//		}
//		return false;
//	}
//
//	private void resetSocialView() {
//		if (null != socialView) {
//			commonTime1 = null;
//			unSelected(tvVerificationMethod1, null);
//			unSelected(tvCommonTime1, null);
//			EditText commonCompany1 = findEditText(socialView, R.id.etSocialCompany);
//			EditText monthPay = findEditText(socialView, R.id.etPreMonthPay);
//			EditText monthRate = findEditText(socialView, R.id.etPreMonthRate);
//			EditText comMonthPay = findEditText(socialView, R.id.etComMonthPay);
//			EditText comMonthRate = findEditText(socialView, R.id.etComMonthRate);
//			commonCompany1.setText("");
//			commonCompany1.clearFocus();
//			monthPay.setText("");
//			monthPay.clearFocus();
//			monthRate.setText("");
//			monthRate.clearFocus();
//			comMonthPay.setText("");
//			comMonthPay.clearFocus();
//			comMonthRate.setText("");
//			comMonthRate.clearFocus();
//		}
//	}
//
//	private boolean checkedOrdinaryViewEmpty() {
//		if (null != ordinaryView) {
//			String comName00 = findEditText(ordinaryView, R.id.etComName00).getText().toString();
//			String etWorkYear00 = findEditText(ordinaryView, R.id.etWorkYear00).getText()
//					.toString();
//			String jobId000 = findTextView(ordinaryView, R.id.tvJobId00).getText().toString();
//			String monthIncome00 = ((XFJRMoneyClearEditText)findEditText(ordinaryView, R.id.etMonthIncome00)).getTextString();
//			String houseAddress00 = findEditText(ordinaryView, R.id.etHouseAddress00).getText()
//					.toString();
//			String houseArea00 = findEditText(ordinaryView, R.id.etHouseArea00).getText()
//					.toString();
//			String houseValue00 = ((XFJRMoneyClearEditText)findEditText(ordinaryView, R.id.etHouseValue00)).getTextString();
//			if (TextUtils.isEmpty(comName00)) {
//				showEmptyTips(findEditText(ordinaryView, R.id.etComName00));
//				return true;
//			}
//			if (TextUtils.isEmpty(etWorkYear00)) {
//				showEmptyTips(findEditText(ordinaryView, R.id.etWorkYear00));
//				return true;
//			}
//			if (TextUtils.isEmpty(jobId000)) {
//				showEmptyTips(findTextView(ordinaryView, R.id.tvJobId00));
//				return true;
//			}
//			if (TextUtils.isEmpty(monthIncome00)) {
//				showEmptyTips(findEditText(ordinaryView, R.id.etMonthIncome00));
//				return true;
//			}
//			if (TextUtils.isEmpty(houseAddress00)) {
//				showEmptyTips(findEditText(ordinaryView, R.id.etHouseAddress00));
//				return true;
//			}
//			if (TextUtils.isEmpty(houseArea00)) {
//				showEmptyTips(findEditText(ordinaryView, R.id.etHouseArea00));
//				return true;
//			}
//			if (TextUtils.isEmpty(houseValue00)) {
//				showEmptyTips(findEditText(ordinaryView, R.id.etHouseValue00));
//				return true;
//			}
//			if (null == decisionOrdinaryListBean00) {
//				decisionOrdinaryListBean00 = new DecisionOrdinaryListBean();
//			}
//			decisionOrdinaryListBean00.setCustomerType("" + ordinaryView.getTag());
//			decisionOrdinaryListBean00.setPeoType("00");
//			decisionOrdinaryListBean00.setComName(comName00);
//			decisionOrdinaryListBean00.setWorkYear(etWorkYear00);
//			decisionOrdinaryListBean00.setJobId(jobId00);
//			decisionOrdinaryListBean00.setMonthIncome(monthIncome00);
//			decisionOrdinaryListBean00.setHouseAddress(houseAddress00);
//			decisionOrdinaryListBean00.setHouseArea(houseArea00);
//			decisionOrdinaryListBean00.setHouseValue(houseValue00);
//			if (marriedStatus) {
//				String comName01 = findEditText(ordinaryView, R.id.etComName01).getText()
//						.toString();
//				String etWorkYear01 = findEditText(ordinaryView, R.id.etWorkYear01).getText()
//						.toString();
//				String jobId01 = findTextView(ordinaryView, R.id.tvJobId01).getText().toString();
//				String monthIncome01 = ((XFJRMoneyClearEditText)findEditText(ordinaryView, R.id.etMonthIncome01)).getTextString();
//				if (TextUtils.isEmpty(comName01)) {
//					showEmptyTips(findEditText(ordinaryView, R.id.etComName01));
//					return true;
//				}
//				if (TextUtils.isEmpty(etWorkYear01)) {
//					showEmptyTips(findEditText(ordinaryView, R.id.etWorkYear01));
//					return true;
//				}
//				if (TextUtils.isEmpty(jobId01)) {
//					showEmptyTips(findTextView(ordinaryView, R.id.tvJobId01));
//					return true;
//				}
//				if (TextUtils.isEmpty(monthIncome01)) {
//					showEmptyTips(findEditText(ordinaryView, R.id.etMonthIncome01));
//					return true;
//				}
//				if (null == decisionOrdinaryListBean01) {
//					decisionOrdinaryListBean01 = new DecisionOrdinaryListBean();
//				}
//				decisionOrdinaryListBean01.setCustomerType("" + ordinaryView.getTag());
//				decisionOrdinaryListBean01.setPeoType("01");
//				decisionOrdinaryListBean01.setComName(comName01);
//				decisionOrdinaryListBean01.setWorkYear(etWorkYear01);
//				decisionOrdinaryListBean01.setJobId(jobId01);
//				decisionOrdinaryListBean01.setMonthIncome(monthIncome01);
//			}
//
//			if (hasBonds) {
//				String comName02 = findEditText(ordinaryView, R.id.etComName02).getText()
//						.toString();
//				String etWorkYear02 = findEditText(ordinaryView, R.id.etWorkYear02).getText()
//						.toString();
//				String jobId002 = findTextView(ordinaryView, R.id.tvJobId02).getText().toString();
//				String monthIncome02 = ((XFJRMoneyClearEditText)findEditText(ordinaryView, R.id.etMonthIncome02)).getTextString();
//				String houseAddress02 = findEditText(ordinaryView, R.id.etHouseAddress02).getText()
//						.toString();
//				String houseArea02 = findEditText(ordinaryView, R.id.etHouseArea02).getText()
//						.toString();
//				String houseValue02 = ((XFJRMoneyClearEditText)findEditText(ordinaryView, R.id.etHouseValue02)).getTextString();
//				if (TextUtils.isEmpty(comName02)) {
//					showEmptyTips(findEditText(ordinaryView, R.id.etComName02));
//					return true;
//				}
//				if (TextUtils.isEmpty(etWorkYear02)) {
//					showEmptyTips(findEditText(ordinaryView, R.id.etWorkYear02));
//					return true;
//				}
//				if (TextUtils.isEmpty(jobId002)) {
//					showEmptyTips(findTextView(ordinaryView, R.id.tvJobId02));
//					return true;
//				}
//				if (TextUtils.isEmpty(monthIncome02)) {
//					showEmptyTips(findEditText(ordinaryView, R.id.etMonthIncome02));
//					return true;
//				}
//				if (null == decisionOrdinaryListBean02) {
//					decisionOrdinaryListBean02 = new DecisionOrdinaryListBean();
//				}
//				decisionOrdinaryListBean02.setCustomerType("" + ordinaryView.getTag());
//				decisionOrdinaryListBean02.setPeoType("02");
//				decisionOrdinaryListBean02.setComName(comName02);
//				decisionOrdinaryListBean02.setWorkYear(etWorkYear02);
//				decisionOrdinaryListBean02.setJobId(jobId02);
//				decisionOrdinaryListBean02.setMonthIncome(monthIncome02);
//				if (isBondsMarried) {
//					String comName03 = findEditText(ordinaryView, R.id.etComName03).getText()
//							.toString();
//					String etWorkYear03 = findEditText(ordinaryView, R.id.etWorkYear03).getText()
//							.toString();
//					String jobId03 = findTextView(ordinaryView, R.id.tvJobId03).getText()
//							.toString();
//					String monthIncome03 = ((XFJRMoneyClearEditText)findEditText(ordinaryView, R.id.etMonthIncome03)).getTextString();
//					if (TextUtils.isEmpty(comName03)) {
//						showEmptyTips(findEditText(ordinaryView, R.id.etComName03));
//						return true;
//					}
//					if (TextUtils.isEmpty(etWorkYear03)) {
//						showEmptyTips(findEditText(ordinaryView, R.id.etWorkYear03));
//						return true;
//					}
//					if (TextUtils.isEmpty(jobId03)) {
//						showEmptyTips(findEditText(ordinaryView, R.id.tvJobId03));
//						return true;
//					}
//					if (TextUtils.isEmpty(monthIncome03)) {
//						showEmptyTips(findEditText(ordinaryView, R.id.etMonthIncome03));
//						return true;
//					}
//					if (null == decisionOrdinaryListBean03) {
//						decisionOrdinaryListBean03 = new DecisionOrdinaryListBean();
//					}
//					decisionOrdinaryListBean03.setCustomerType("" + ordinaryView.getTag());
//					decisionOrdinaryListBean03.setPeoType("03");
//					decisionOrdinaryListBean03.setComName(comName03);
//					decisionOrdinaryListBean03.setWorkYear(etWorkYear03);
//					decisionOrdinaryListBean03.setJobId(jobId03);
//					decisionOrdinaryListBean03.setMonthIncome(monthIncome03);
//				}
//				if (TextUtils.isEmpty(houseAddress02)) {
//					showEmptyTips(findEditText(ordinaryView, R.id.etHouseAddress02));
//					return true;
//				}
//				if (TextUtils.isEmpty(houseArea02)) {
//					showEmptyTips(findEditText(ordinaryView, R.id.etHouseArea02));
//					return true;
//				}
//				if (TextUtils.isEmpty(houseValue02)) {
//					showEmptyTips(findEditText(ordinaryView, R.id.etHouseValue02));
//					return true;
//				}
//				decisionOrdinaryListBean02.setHouseAddress(houseAddress02);
//				decisionOrdinaryListBean02.setHouseArea(houseArea02);
//				decisionOrdinaryListBean02.setHouseValue(houseValue02);
//			}
//		} else {
//			return false;
//		}
//		return false;
//	}
//
//	private void resetOrdinaryViewEmpty() {
//		if (null != ordinaryView) {
//			TextView jobId000 = findTextView(ordinaryView, R.id.tvJobId00);
//			EditText comName00 = findEditText(ordinaryView, R.id.etComName00);
//			EditText etWorkYear00 = findEditText(ordinaryView, R.id.etWorkYear00);
//			EditText monthIncome00 = findEditText(ordinaryView, R.id.etMonthIncome00);
//			EditText houseAddress00 = findEditText(ordinaryView, R.id.etHouseAddress00);
//			EditText houseArea00 = findEditText(ordinaryView, R.id.etHouseArea00);
//			EditText houseValue00 = findEditText(ordinaryView, R.id.etHouseValue00);
//			unSelected(jobId000, "");
//			comName00.setText("");
//			etWorkYear00.setText("");
//			monthIncome00.setText("");
//			houseAddress00.setText("");
//			houseArea00.setText("");
//			houseValue00.setText("");
//			comName00.clearFocus();
//			etWorkYear00.clearFocus();
//			monthIncome00.clearFocus();
//			houseAddress00.clearFocus();
//			houseArea00.clearFocus();
//			houseValue00.clearFocus();
//			if (marriedStatus) {
//				TextView jobId01 = findTextView(ordinaryView, R.id.tvJobId01);
//				EditText comName01 = findEditText(ordinaryView, R.id.etComName01);
//				EditText etWorkYear01 = findEditText(ordinaryView, R.id.etWorkYear01);
//				EditText monthIncome01 = findEditText(ordinaryView, R.id.etMonthIncome01);
//				unSelected(jobId01, "");
//				comName01.setText("");
//				etWorkYear01.setText("");
//				monthIncome01.setText("");
//				comName01.clearFocus();
//				etWorkYear01.clearFocus();
//				monthIncome01.clearFocus();
//			}
//			if (hasBonds) {
//				TextView jobId002 = findTextView(ordinaryView, R.id.tvJobId02);
//				EditText comName02 = findEditText(ordinaryView, R.id.etComName02);
//				EditText etWorkYear02 = findEditText(ordinaryView, R.id.etWorkYear02);
//				EditText monthIncome02 = findEditText(ordinaryView, R.id.etMonthIncome02);
//				EditText houseAddress02 = findEditText(ordinaryView, R.id.etHouseAddress02);
//				EditText houseArea02 = findEditText(ordinaryView, R.id.etHouseArea02);
//				EditText houseValue02 = findEditText(ordinaryView, R.id.etHouseValue02);
//				unSelected(jobId002, "");
//				comName02.setText("");
//				etWorkYear02.setText("");
//				monthIncome02.setText("");
//				houseAddress02.setText("");
//				houseArea02.setText("");
//				houseValue02.setText("");
//				comName02.clearFocus();
//				etWorkYear02.clearFocus();
//				monthIncome02.clearFocus();
//				houseAddress02.clearFocus();
//				houseArea02.clearFocus();
//				houseValue02.clearFocus();
//				if (isBondsMarried) {
//					TextView jobId03 = findTextView(ordinaryView, R.id.tvJobId03);
//					EditText comName03 = findEditText(ordinaryView, R.id.etComName03);
//					EditText etWorkYear03 = findEditText(ordinaryView, R.id.etWorkYear03);
//					EditText monthIncome03 = findEditText(ordinaryView, R.id.etMonthIncome03);
//					unSelected(jobId03, "");
//					comName03.setText("");
//					etWorkYear03.setText("");
//					monthIncome03.setText("");
//					comName03.clearFocus();
//					etWorkYear03.clearFocus();
//					monthIncome03.clearFocus();
//				}
//			}
//		}
//	}
//
//	/**
//	 * 房产类客户输入信息判空
//	 * 
//	 * @return
//	 */
//	private boolean checkedHomeViewEmpty() {
//		if (homeView != null) {
//			String houseOwner = findEditText(homeView, R.id.etHouseOwner).getText().toString();// 房产业主houseOwner
//			String houseArea = findEditText(homeView, R.id.etHouseArea).getText().toString();// 房产面积houseArea
//			String houseProp = findTextView(homeView, R.id.tvHousePropId).getText().toString();// 房产类型ID、housePropId
//			String cityId = findTextView(homeView, R.id.tvCityId).getText().toString();// 房产所在城市cityId
//			// 房产信息核查方式houseCheck、1征信报告未结清房贷信息、2备案后的购房合同、3查档的房产证
//			String houseCheckStr = findTextView(homeView, R.id.tvHouseCheck).getText().toString();
//			if (TextUtils.isEmpty(houseOwner)) {
//				showEmptyTips(findEditText(homeView, R.id.etHouseOwner));
//				return true;
//			}
//			if (TextUtils.isEmpty(houseArea)) {
//				showEmptyTips(findEditText(homeView, R.id.etHouseArea));
//				return true;
//			}
//			if (TextUtils.isEmpty(houseProp)) {
//				showEmptyTips(findTextView(homeView, R.id.tvHousePropId));
//				return true;
//			}
//			if (TextUtils.isEmpty(cityId)) {
//				showEmptyTips(findTextView(homeView, R.id.tvCityId));
//				return true;
//			}
//			if (TextUtils.isEmpty(houseCheckStr)) {
//				showEmptyTips(findTextView(homeView, R.id.tvHouseCheck));
//				return true;
//			}
//			if (null == decisionSceneListBeanHome) {
//				decisionSceneListBeanHome = new DescE();
//			}
////			decisionSceneListBeanHome.setCustomerType("" + homeView.getTag());
//			decisionSceneListBeanHome.setHouseOwner(houseOwner);
//			decisionSceneListBeanHome.setHouseArea(houseArea);
//			decisionSceneListBeanHome.setHousePropId(housePropId);
//			decisionSceneListBeanHome.setCityId(cityId);
//			decisionSceneListBeanHome.setHouseCheck(houseCheck);
//			if (findView(homeView, R.id.llSecondHomeInfo).getVisibility() == View.VISIBLE) {
//				String houseOwner2 = findEditText(homeView, R.id.etHouseOwner2).getText()
//						.toString();// 房产业主houseOwner
//				String houseArea2 = findEditText(homeView, R.id.etHouseArea2).getText().toString();// 房产面积houseArea
//				String houseProp2 = findTextView(homeView, R.id.tvHousePropId2).getText()
//						.toString();
//				String cityId2 = findTextView(homeView, R.id.tvCityId2).getText().toString();
//				String houseCheckStr2 = findTextView(homeView, R.id.tvHouseCheck2).getText()
//						.toString();
//				if (TextUtils.isEmpty(houseOwner2)) {
//					showEmptyTips(findEditText(homeView, R.id.etHouseOwner2));
//					return true;
//				}
//				if (TextUtils.isEmpty(houseArea2)) {
//					showEmptyTips(findEditText(homeView, R.id.etHouseArea2));
//					return true;
//				}
//				if (TextUtils.isEmpty(houseProp2)) {
//					showEmptyTips(findTextView(homeView, R.id.tvHousePropId2));
//					return true;
//				}
//				if (TextUtils.isEmpty(cityId2)) {
//					showEmptyTips(findTextView(homeView, R.id.tvCityId2));
//					return true;
//				}
//				if (TextUtils.isEmpty(houseCheckStr2)) {
//					showEmptyTips(findTextView(homeView, R.id.tvHouseCheck2));
//					return true;
//				}
//				decisionSceneListBeanHome.setHouseOwner2(houseOwner2);
//				decisionSceneListBeanHome.setHouseArea2(houseArea2);
//				decisionSceneListBeanHome.setHousePropId2(housePropId2);
//				decisionSceneListBeanHome.setCityId2(cityId2);
//				decisionSceneListBeanHome.setHouseCheck2(houseCheck2);
//			} else {
//				return false; // 不判断
//			}
//		} else {
//			return false; // 不判断
//		}
//		return false;
//	}
//
//	private void resetHomeView() {
//		if (homeView != null) {
//			EditText houseOwner = findEditText(homeView, R.id.etHouseOwner);// 房产业主houseOwner
//			EditText houseArea = findEditText(homeView, R.id.etHouseArea);// 房产面积houseArea
//			TextView houseProp = findTextView(homeView, R.id.tvHousePropId);// 房产类型ID、housePropId
//			TextView cityId = findTextView(homeView, R.id.tvCityId);// 房产所在城市cityId
//			TextView houseCheckStr = findTextView(homeView, R.id.tvHouseCheck);
//			houseOwner.setText("");
//			houseArea.setText("");
//			unSelected(houseProp, null);
//			unSelected(cityId, null);
//			unSelected(houseCheckStr, null);
//			houseOwner.clearFocus();
//			houseArea.clearFocus();
//			if (findView(homeView, R.id.llSecondHomeInfo).getVisibility() == View.VISIBLE) {
//				EditText houseOwner2 = findEditText(homeView, R.id.etHouseOwner2);// 房产业主houseOwner
//				EditText houseArea2 = findEditText(homeView, R.id.etHouseArea2);// 房产面积houseArea
//				TextView houseProp2 = findTextView(homeView, R.id.tvHousePropId2);
//				TextView cityId2 = findTextView(homeView, R.id.tvCityId2);
//				TextView houseCheckStr2 = findTextView(homeView, R.id.tvHouseCheck2);
//				houseOwner2.setText("");
//				houseArea2.setText("");
//				houseOwner2.clearFocus();
//				houseArea2.clearFocus();
//				unSelected(houseProp2, null);
//				unSelected(cityId2, null);
//				unSelected(houseCheckStr2, null);
//			}
//		}
//	}
//
//	private boolean checkedCommonViewEmpty() {
//		if (null != commonView) {
//			String check = tvVerificationMethod0.getText().toString();
//			String payCom = findEditText(commonView, R.id.etCommonCompany).getText().toString();
//			String monthPay = ((XFJRMoneyClearEditText)findEditText(commonView, R.id.etPreMonthPay)).getTextString();
//			String monthRate = findEditText(commonView, R.id.etPreMonthRate).getText().toString();
//			String comMonthPay = ((XFJRMoneyClearEditText)findEditText(commonView, R.id.etComMonthPay)).getTextString();
//			String comMonthRate = findEditText(commonView, R.id.etComMonthRate).getText().toString();
//			if (TextUtils.isEmpty(check)) {
//				showEmptyTips(tvVerificationMethod0);
//				return true;
//			}
//			if (TextUtils.isEmpty(payCom)) {
//				showEmptyTips(findEditText(commonView, R.id.etCommonCompany));
//				return true;
//			}
//			if (TextUtils.isEmpty(commonTime0)) {
//				showEmptyTips(tvCommonTime0);
//				return true;
//			}
//			if (TextUtils.isEmpty(monthPay)) {
//				showEmptyTips(findEditText(commonView, R.id.etPreMonthPay));
//				return true;
//			}
//			if (TextUtils.isEmpty(monthRate)) {
//				showEmptyTips(findEditText(commonView, R.id.etPreMonthRate));
//				return true;
//			} else if (Double.parseDouble(monthRate) >= 100) {
//				ToastUtils.show(baseActivity, getString(R.string.pre_month_rate_can_not_exceed_percent100), 0);
//				return true;
//			}
//			if (TextUtils.isEmpty(comMonthPay)) {
//				showEmptyTips(findEditText(commonView, R.id.etComMonthPay));
//				return true;
//			}
//			if (TextUtils.isEmpty(comMonthRate)) {
//				showEmptyTips(findEditText(commonView, R.id.etComMonthRate));
//				return true;
//			} else if (Double.parseDouble(comMonthRate) >= 100) {
//				ToastUtils.show(baseActivity, getString(R.string.com_month_rate_can_not_exceed_percent100), 0);
//				return true;
//			}
////			switch (fxModel) {
////			case "1":
////				// fxModel = 1; // 1：客群类
////				if (null == decisionGroupListBeanCommon) {
////					decisionGroupListBeanCommon = new DecisionGroupListBean();
////				}
////				decisionGroupListBeanCommon.setGjCheck(gjCheckId);
////				decisionGroupListBeanCommon.setGjPayCom(payCom);
////				decisionGroupListBeanCommon.setGjPayStart(commonTime0);
////				decisionGroupListBeanCommon.setGjMatch(isMatch);
////				decisionGroupListBeanCommon.setGjPreMonthPay(monthPay);
////				decisionGroupListBeanCommon.setGjPreMonthRate(monthRate);
////				decisionGroupListBeanCommon.setGjComMonthPay(comMonthPay);
////				decisionGroupListBeanCommon.setGjComMonthRate(comMonthRate);
////				decisionGroupListBeanCommon.setGjComMonthPay(comMonthPay);
////				break;
////			case "2":
////				// fxModel = 2; // 2：场景类
////				if (null == decisionSceneListBeanCommon) {
////					decisionSceneListBeanCommon = new DecisionSceneListBean();
////				}
////				decisionSceneListBeanCommon.setGjCheck(gjCheckId);
////				decisionSceneListBeanCommon.setGjComMonthPay(comMonthPay);
////				decisionSceneListBeanCommon.setGjPayStart(commonTime0);
////				decisionSceneListBeanCommon.setGjMatch(isMatch);
////				decisionSceneListBeanCommon.setGjPreMonthPay(monthPay);
////				decisionSceneListBeanCommon.setGjPreMonthRate(monthRate);
////				decisionSceneListBeanCommon.setGjComMonthPay(comMonthPay);
////				decisionSceneListBeanCommon.setGjComMonthRate(comMonthRate);
////				decisionSceneListBeanCommon.setGjPayCom(payCom);
////				break;
////			}
//			if (null == decisionSceneListBeanCommon) {
//				decisionSceneListBeanCommon = new DescC();
//			}
//			decisionSceneListBeanCommon.setGjCheck(gjCheckId);
//			decisionSceneListBeanCommon.setGjComMonthPay(comMonthPay);
//			decisionSceneListBeanCommon.setGjPayStart(commonTime0);
//			decisionSceneListBeanCommon.setGjMatch(isMatch);
//			decisionSceneListBeanCommon.setGjPreMonthPay(monthPay);
//			decisionSceneListBeanCommon.setGjPreMonthRate(monthRate);
//			decisionSceneListBeanCommon.setGjComMonthPay(comMonthPay);
//			decisionSceneListBeanCommon.setGjComMonthRate(comMonthRate);
//			decisionSceneListBeanCommon.setGjPayCom(payCom);
//		} else {
//			return false;
//		}
//		return false;
//	}
//
//	private void resetCommonView() {
//		if (null != commonView) {
//			commonTime0 = null;
//			unSelected(tvVerificationMethod0, null);
//			unSelected(tvCommonTime0, null);
//			findEditText(commonView, R.id.etCommonCompany).setText("");
//			findEditText(commonView, R.id.etPreMonthPay).setText("");
//			findEditText(commonView, R.id.etPreMonthRate).setText("");
//			findEditText(commonView, R.id.etComMonthPay).setText("");
//			findEditText(commonView, R.id.etComMonthRate).setText("");
//			findEditText(commonView, R.id.etCommonCompany).clearFocus();
//			findEditText(commonView, R.id.etPreMonthPay).clearFocus();
//			findEditText(commonView, R.id.etPreMonthRate).clearFocus();
//			findEditText(commonView, R.id.etComMonthPay).clearFocus();
//			findEditText(commonView, R.id.etComMonthRate).clearFocus();
//		}
//	}
//
//	// 左边按钮
//	@OnClick(R.id.btnLeft)
//	private void clickStep(View view) {
//		if (communicationCallBack != null) {
//			communicationCallBack.backCallBack();
//		}
//	}
//
//	// 弹出选择种类框
//	@SuppressLint("NewApi")
//	private void alertSelectDialog(final String selected, List<CustomType> list,
//			final CustomTypeSelectDialogClick dialogClick) {
//		LogUtils.e("集合：" + list.toString());
//		XFJRDialogUtil.alertCustomTypeSelectDialog(getActivity(), selected, list, dialogClick);
//	}
//
//	/**
//	 * radiogroup的切换监听回调
//	 * 
//	 * @author formssi
//	 *
//	 */
//	private interface ChangeCallBack {
//		void onChange(RadioGroup group, int checkedId);
//	}
//
//	private void delCommonData(int position) {
//		final CustomerTypeListAdapter headAdapter = (CustomerTypeListAdapter) rvCommonHead
//				.getAdapter();
//		final List<CustomType> dataSets = headAdapter.getDataSets();
//		if (headAdapter.getCount() == 1) {
//			XFJRDialogUtil.confirmDialog(getActivity(), "是否确认删除?", new DialogClick() {
//
//				@Override
//				public void onOkClick(View view, XfjrDialog dialog) {
//					listData.remove(dataSets.get(0));
//					headAdapter.notifyDataSetChanged();
//					deleteSeletedView(commonView);
//					setFlowVisibility();
//					dialog.dismiss();
//				}
//
//				@Override
//				public void onCancelClick(View view, XfjrDialog dialog) {
//					dialog.dismiss();
//				}
//			}).show();
//		} else {
//			listData.remove(dataSets.get(position));
//			headAdapter.notifyDataSetChanged();
//			setFlowVisibility();
//		}
//	}
//
//	private void submitPretrialInfo() {
////		List<DecisionGroupListBean> decisionGroupList = new ArrayList<>(); // 1：客群类
////		List<DecisionSceneListBean> decisionSceneList = new ArrayList<>(); // 2、场景类
////		List<DecisionOrdinaryListBean> decisionOrdinaryList = new ArrayList<>(); // 3：普通类
////		if (null != decisionGroupListBeanHousingLoan) {
////			decisionGroupList.add(decisionGroupListBeanHousingLoan);
////		}
////		if (null != decisionGroupListBeanCommon) {
////			// decisionGroupList.add(decisionGroupListBeanCommon);
////			List<CustomType> tmpList = ((CustomerTypeListAdapter) rvCommonHead.getAdapter())
////					.getDataSets();
////			for (int i = 0; i < tmpList.size(); i++) {
////				DecisionGroupListBean tmp = new Gson().fromJson(
////						new Gson().toJson(decisionGroupListBeanCommon),
////						DecisionGroupListBean.class);
////				tmp.setCustomerType(tmpList.get(i).getTid() + "");
////				decisionGroupList.add(tmp);
////			}
////		}
////		if (null != decisionSceneListBeanCommon) {
////			// decisionSceneList.add(decisionSceneListBeanCommon);
////			List<CustomType> tmpList = ((CustomerTypeListAdapter) rvCommonHead.getAdapter())
////					.getDataSets();
////			for (int i = 0; i < tmpList.size(); i++) {
////				DecisionSceneListBean tmp = new Gson().fromJson(
////						new Gson().toJson(decisionSceneListBeanCommon),
////						DecisionSceneListBean.class);
////				tmp.setCustomerType(tmpList.get(i).getTid() + "");
////				decisionSceneList.add(tmp);
////			}
////		}
////		if (null != decisionSceneListBeanSocial) {
////			decisionSceneList.add(decisionSceneListBeanSocial);
////		}
////		if (null != decisionSceneListBeanHome) {
////			decisionSceneList.add(decisionSceneListBeanHome);
////		}
////		if (null != decisionGroupList && decisionGroupList.size() > 0) {
////			pretrialParamBean.setDecisionGroupList(decisionGroupList); // 1：客群类
////		}
////		if (null != decisionSceneList && decisionSceneList.size() > 0) {
////			pretrialParamBean.setDecisionSceneList(decisionSceneList); // 2、场景类
////		}
////		if (null != decisionOrdinaryList && decisionOrdinaryList.size() > 0) {
////			pretrialParamBean.setDecisionOrdinaryList(decisionOrdinaryList); // :3：普通类
////		}
//		List<DecisionOrdinaryListBean> decisionOrdinaryList = new ArrayList<>(); // 普通客户资料
//		if (null != decisionOrdinaryListBean00) {
//			decisionOrdinaryList.add(decisionOrdinaryListBean00);
//		}
//		if (null != decisionOrdinaryListBean01) {
//			decisionOrdinaryList.add(decisionOrdinaryListBean01);
//		}
//		if (null != decisionOrdinaryListBean02) {
//			decisionOrdinaryList.add(decisionOrdinaryListBean02);
//		}
//		if (null != decisionOrdinaryListBean03) {
//			decisionOrdinaryList.add(decisionOrdinaryListBean03);
//		}
//		if (null != decisionGroupListBeanHousingLoan) { // 房贷类资料
//			pretrialParamBean.setDescB(decisionGroupListBeanHousingLoan);
//		}
//		if (null != decisionSceneListBeanCommon) { // 公积金资料
//			pretrialParamBean.setDescC(decisionSceneListBeanCommon);
//		}
//		if (null != decisionSceneListBeanSocial) { // 社保资料
//			pretrialParamBean.setDescD(decisionSceneListBeanSocial);
//		}
//		if (null != decisionSceneListBeanHome) { // 房产类资料
//			pretrialParamBean.setDescE(decisionSceneListBeanHome);
//		}
//		pretrialParamBean.setBusinessId(XfjrMain.businessId);
//		if (XfjrMain.isNet) {
//			HttpRequest.submitPretrialInfo(getActivity(), pretrialParamBean,
//					PretrialBasicInfoFragment.photoFile, new IHttpCallback<PretrialResultBean>() {
//
//						@Override
//						public void onSuccess(String url, PretrialResultBean result) {
//							// status 0通过 1是不通过（转人工）补充资料
//							String status = result.getStatus();
//							String creditLine = result.getLimit();
//							boolean isPass = false;
//							LogUtils.e("提交成功");
//							Intent intent = new Intent(XFJRConstant.ACTION_UPDATE_DATA);
//							switch (status) {
//							case "0": // 待预审
//								isPass = true;
//								XfjrMain.businessStatus = "3";
//								LogUtils.e("结束评估发送广播更新列表,此时列表应该在待审批里面");
//								intent.putExtra(XFJRConstant.KEY_BUSINESS_STATUS, 0);
//								break;
//
//							case "1": // 转人工
//								isPass = false;
//								intent.putExtra(XFJRConstant.KEY_BUSINESS_STATUS, 1);
//								LogUtils.e("结束评估发送广播更新列表,此时列表应该在转人工里面");
//								XfjrMain.businessStatus = "1";
//								break;
//							}
//							intent.putExtra(XFJRConstant.ACTION_FLAG_INT, ((XfjrMainActivity) getActivity()).productBean.getFrom());
//							getActivity().sendBroadcast(intent);
//							if (communicationCallBack != null) {
//								PretrialBasicInfoFragment.photoFile = null;
//								Map<String, Object> map = new HashMap<>();
//								map.put(XFJRConstant.KEY_PRETRIAL_RESULT, isPass);
//								map.put(XFJRConstant.KEY_CREDIT_LINE, creditLine);
//								communicationCallBack.nextCallBack(map);
//							}
//						}
//
//						@Override
//						public void onError(String url, Throwable e) {
//							UrlConfig.showErrorTips(getActivity(), e);
////							String code = e.getMessage();
////							if (code != null) {
////								LogUtils.e("提交不成功：" + e.getMessage());
////								if (code.equals(UrlConfig.dynamicUrlExceptionCode)) {
////									LogUtils.e("dynamic url exception:don't find this APP_ID");
////									XFJRUtil.autoLogin();
////								}
////							}
//						}
//
//						@Override
//						public void onFinal(String url) {
//
//						}
//					});
//		} else {
//			XfjrMain.businessStatus = "3";
//			if (communicationCallBack != null) {
//				PretrialBasicInfoFragment.photoFile = null;
//				Map<String, Object> map = new HashMap<>();
//				map.put(XFJRConstant.KEY_PRETRIAL_RESULT, true);
//				map.put(XFJRConstant.KEY_CREDIT_LINE, "10000.00");
//				communicationCallBack.nextCallBack(map);
//			}
//		}
//	}
//
//	// 右边按钮
//	@OnClick(R.id.btnRight)
//	private void clickNext(View view) {
//		if (checkEmpty()) {
//			return;
//		}
//		submitPretrialInfo();
//	}
//
//	private BroadcastReceiver br;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		br = new MarriedStatusChangeBroadcastReceiver();
//		getActivity().registerReceiver(br,
//				new IntentFilter(XFJRConstant.ACTION_MARRIED_STATUS_CHANGE));
//		super.onCreate(savedInstanceState);
//	}
//
//	@Override
//	public void onDestroy() {
//		getActivity().unregisterReceiver(br);
//		PretrialBasicInfoFragment.photoFile = null;
//		super.onDestroy();
//	}
//
//	class MarriedStatusChangeBroadcastReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			pretrialParamBean = (PretrialParamBean) intent.getSerializableExtra(XFJRConstant.KEY_PRETRIAL_BEAN);
//			marriedStatus = intent.getBooleanExtra(XFJRConstant.KEY_MARRIED_STATUS, marriedStatus);
//			changeOrdinarySpouseView(typeList.get(0));
//		}
//	}
//
//	private void changeOrdinarySpouseView(CustomType customType) {
//		if (marriedStatus) {
//			setViewVisibility(ordinaryView, R.id.llSpouseInfo, View.VISIBLE);
//			setDrawableRight2(findTextView(ordinaryView, R.id.ivHideSpouseStub), findView(ordinaryView, R.id.llSpouseStub));
//		} else {
//			findEditText(ordinaryView, R.id.etComName01).setText("");
//			findEditText(ordinaryView, R.id.etWorkYear01).setText("");
//			findTextView(ordinaryView, R.id.tvJobId01).setText("");
//			findEditText(ordinaryView, R.id.etMonthIncome01).setText("");
//			decisionOrdinaryListBean01 = null; // 未婚或者离异
//			setViewVisibility(ordinaryView, R.id.llSpouseInfo, View.GONE);
//		}
//	}
//
//	/**
//	 * 空白处点击事件
//	 * 
//	 * @param view
//	 */
//	@OnClick(R.id.lltBlank)
//	private void clickBlank(View view) {
//		XFJRUtil.hideSoftInput(view);
//	}
//
//}