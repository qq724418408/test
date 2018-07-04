package com.bocop.xfjr.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.bocop.xfjr.bean.SystemBasicInfo;
import com.bocop.xfjr.bean.SystemBasicInfo.ChildCityBean;
import com.bocop.xfjr.bean.SystemBasicInfo.CityListBean;
import com.bocop.xfjr.bean.pretrial.CustomType;
import com.bocop.xfjr.bean.pretrial.PretrialParamBean;
import com.bocop.xfjr.bean.pretrial.PretrialParamBean.DescC;
import com.bocop.xfjr.bean.pretrial.PretrialParamBean.DescD;
import com.bocop.xfjr.bean.pretrial.PretrialParamBean.DescE;
import com.bocop.xfjr.bean.pretrial.PretrialResultBean;
import com.bocop.xfjr.callback.CommunicationCallBack;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.constant.XFJRConstant;
import com.bocop.xfjr.util.LimitInputTextWatcher;
import com.bocop.xfjr.util.ScreenUtils;
import com.bocop.xfjr.util.TextUtil;
import com.bocop.xfjr.util.XFJRUtil;
import com.bocop.xfjr.util.dialog.MaxDatePickerDialog;
import com.bocop.xfjr.util.dialog.MaxDatePickerDialog.TimePickerDialogInterface;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil.ChildCityListSelectDialogClick;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil.CityListSelectDialogClick;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil.CustomTypeSelectDialogClick;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil.DialogClick;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil.StringSelectDialogClick;
import com.bocop.xfjr.util.dialog.XfjrDialog;
import com.bocop.xfjr.util.file.FileUtil;
import com.bocop.xfjr.util.file.SystemBasicJSONWRUtil;
import com.bocop.xfjr.util.json.PretrialJSONParam;
import com.bocop.xfjr.view.XFJRMoneyClearEditText;
import com.bocop.yfx.utils.ToastUtils;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

public class PretrialCustomTypeInfoFragment extends BaseCheckProcessFragment {

	@ViewInject(R.id.llContainer)
	private LinearLayout llContainer;
	@ViewInject(R.id.scrollView)
	private ScrollView scrollView;
//	private ImageView ivFlowTop;
	@ViewInject(R.id.ivSelectProvidentFundInfo)
	private ImageView ivSelectProvidentFundInfo;
	@ViewInject(R.id.ivSocialCharityInfo)
	private ImageView ivSocialCharityInfo;
	@ViewInject(R.id.ivHousePropertyInfo)
	private ImageView ivHousePropertyInfo;
	@ViewInject(R.id.tvSelectProvidentFundInfo)
	private TextView tvSelectProvidentFundInfo;
	@ViewInject(R.id.tvSocialCharityInfo)
	private TextView tvSocialCharityInfo;
	@ViewInject(R.id.tvHousePropertyInfo)
	private TextView tvHousePropertyInfo;
	@ViewInject(R.id.lltTpye)
	private View lltTpye;
	@ViewInject(R.id.lltSocialCharityInfo)
	private View lltSocialCharityInfo;
	@ViewInject(R.id.fltHousePropertyInfo)
	private View fltHousePropertyInfo;
	private View publicFoundView, socialView, houseView;
	private MaxDatePickerDialog mTimePickerDialog;
	private CommunicationCallBack communicationCallBack;
	private PretrialParamBean pretrialParamBean;
	private DescC publicFoundBean; // 公积金资料
	private DescD socialCharityBean; // 社保资料
	private List<DescE> houseBeanList = new ArrayList<>();; // 房产类资料
	private List<ChildCityBean> childCityList = new ArrayList<>();
	private String isMatch = "N";// 与客户单位是否匹配
	private String isMatchSj = "N";// 社保类 与客户单位是否匹配
	private int houseNo = 2;

	private String fxModel;
	private String applicantName; // 申请人姓名（房产业主姓名）
//	private static List<CustomType> typeList = new ArrayList<>();// 保存所有类型及布局
	
	@Override
	protected void initView() {
		super.initView();
		setVisibility(tvReset, View.VISIBLE);
		LogUtils.e("PretrialCustomTypeInfoFragment----fxModel--->" + fxModel);
		if (fxModel.equals("ALL") && PretrialBasicInfoFragment.hasHouse) {
			//lltSocialCharityInfo.setVisibility(View.INVISIBLE);
			fltHousePropertyInfo.setVisibility(View.VISIBLE);
			selectInfo(tvHousePropertyInfo, ivHousePropertyInfo);
			showView(tvHousePropertyInfo.getId(), tvHousePropertyInfo, ivHousePropertyInfo);
			LogUtils.e("PretrialCustomTypeInfoFragment----授信模型为ALL且有房产，房产信息一定要填，不可取消");
		} else if (fxModel.equals("ALL") && !PretrialBasicInfoFragment.hasHouse) {
			fltHousePropertyInfo.setVisibility(View.GONE);
			deleteView(tvHousePropertyInfo.getId(), tvHousePropertyInfo, ivHousePropertyInfo);
			//selectInfo(tvHousePropertyInfo, ivHousePropertyInfo);
		} else {
			selectInfo(tvHousePropertyInfo, ivHousePropertyInfo);
		}
		selectInfo(tvSelectProvidentFundInfo, ivSelectProvidentFundInfo);
		selectInfo(tvSocialCharityInfo, ivSocialCharityInfo);
		//setVisibility(tvSave, View.VISIBLE);
	}

	@Override
	protected boolean resetClick(View view) {
		resetSocialView();
		resetHouseView();
		resetPublicFoundView();
		return false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return view = inflater.inflate(R.layout.xfjr_fragment_pretrial_customer_type_info, container, false);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		LogUtils.e("PretrialCustomTypeInfoFragment----onHiddenChanged()-->" + hidden);
		if (!hidden) {
			LogUtils.e("PretrialCustomTypeInfoFragment----fxModel--->" + fxModel);
			if (fxModel.equals("ALL") && PretrialBasicInfoFragment.hasHouse) {
				if (houseView == null) {
					showView(tvHousePropertyInfo.getId(), tvHousePropertyInfo, ivHousePropertyInfo);
				}
				LogUtils.e("PretrialCustomTypeInfoFragment----授信模型为ALL且有房产，房产信息一定要填，不可取消");
				fltHousePropertyInfo.setVisibility(View.VISIBLE);
			} else if (fxModel.equals("ALL") && !PretrialBasicInfoFragment.hasHouse) {
				deleteView(tvHousePropertyInfo.getId(), tvHousePropertyInfo, ivHousePropertyInfo);
				fltHousePropertyInfo.setVisibility(View.GONE);
			}
//			marriedStatus = getArguments().getBoolean(XFJRConstant.KEY_MARRIED_STATUS);
			pretrialParamBean = PretrialBasicInfoFragment.pretrialParamBean;
		}
		super.onHiddenChanged(hidden);
	}

	@Override
	protected void initData() {
		super.initData();
//		typeList.clear();
		try {
			fxModel = XfjrMainActivity.productBean.getFxModel().trim();
			applicantName = XfjrMainActivity.productBean.getUserName().trim();
			//List<CustTypeBean> custTypeList = ((XfjrMainActivity) getActivity()).productBean.getCustType();
		} catch (Exception e) {
			fxModel = "ALL";
			applicantName = "xxx";
		}
//		marriedStatus = getArguments().getBoolean(XFJRConstant.KEY_MARRIED_STATUS);
		pretrialParamBean = PretrialBasicInfoFragment.pretrialParamBean;
		Fragment fragment = getParentFragment();
		if (fragment instanceof CommunicationCallBack) {
			communicationCallBack = (CommunicationCallBack) fragment;
		}
		LogUtils.e("PretrialCustomTypeInfoFragment----old fxModel--->" + fxModel);
		fxModel = XFJRUtil.getFxModel(fxModel);
		LogUtils.e("PretrialCustomTypeInfoFragment----new fxModel--->" + fxModel);
		switch (fxModel) {
		case "ALL":
			LogUtils.e("PretrialCustomTypeInfoFragment----授信模型：ALL：公积金和房产和社保");
			break;
		case "1": // 1：客群类:公积金和房产
			LogUtils.e("PretrialCustomTypeInfoFragment----授信模型：公积金");
			lltSocialCharityInfo.setVisibility(View.INVISIBLE);
			fltHousePropertyInfo.setVisibility(View.GONE);
			break;
		case "2": // 2：场景类:ALL
			LogUtils.e("PretrialCustomTypeInfoFragment----授信模型：场景类:公积金和房产和社保");
			break;
		case "3": // 3：普通类 :NONE
			LogUtils.e("PretrialCustomTypeInfoFragment----授信模型：普通类：不用选择");
			lltTpye.setVisibility(View.GONE);
			break;
		}
//		typeList.add(new CustomType(12, "公积金类", R.layout.xfjr_layout_public_found_info)); // descC公积金资料
//		typeList.add(new CustomType(23, "社保类", R.layout.xfjr_layout_social_charity_info)); // descD社保资料
//		typeList.add(new CustomType(24, "房产类", R.layout.xfjr_layout_house_property_info)); // descE房产类资料
	}

	private void selectInfo(final TextView tv, final View iv) {
		tv.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View view) {
				if (iv.getVisibility() == View.VISIBLE) {
					LogUtils.e("PretrialCustomTypeInfoFragment----取消选择");
					confirmDialog(view.getId(), tv, iv);
				} else {
					LogUtils.e("PretrialCustomTypeInfoFragment----选中");
					showView(view.getId(), tv, iv);
				}
			}
		});
	}
	
	/**
	 * 公积金信息
	 * 
	 * @param params
	 */
	private void initPublicFoundView(LayoutParams params) {
		publicFoundView = LayoutInflater.from(getActivity()).inflate(R.layout.xfjr_layout_public_found_info, llContainer, false);//
		setDrawableRight(findTextView(publicFoundView, R.id.tvProvidentFundInfo), findView(publicFoundView, R.id.lltProvidentFund));
		final XFJRMoneyClearEditText etPreMonthPay = (XFJRMoneyClearEditText) findEditText(publicFoundView, R.id.etPreMonthPay);
		final XFJRMoneyClearEditText etComMonthPay = (XFJRMoneyClearEditText) findEditText(publicFoundView, R.id.etComMonthPay);
		etPreMonthPay.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				etPreMonthPay.onFocusChange(v, hasFocus); // 不调这个方法就不会显示delete图标
				if (!hasFocus) {
					TextUtil.suffix00(etPreMonthPay);
				}
			}
			
		});
		etComMonthPay.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				etComMonthPay.onFocusChange(v, hasFocus); // 不调这个方法就不会显示delete图标
				if (!hasFocus) {
					TextUtil.suffix00(etComMonthPay);
				}
			}
			
		});
		RadioGroup rgIsMatch = (RadioGroup) publicFoundView.findViewById(R.id.rgIsMatch);
		initOrdinaryRadioGroupChange(rgIsMatch, new ChangeCallBack() {

			@Override
			public void onChange(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rbYes:
					isMatch = "Y";
					break;

				case R.id.rbNo:
					isMatch = "N";
					break;
				}

			}
		});
		final TextView tvPublicCheck = findTextView(publicFoundView, R.id.tvPublicCheck); // 核验方式
		tvPublicCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final List<String> list = new ArrayList<>();
				list.addAll(Arrays.asList(getActivity().getResources().getStringArray(R.array.selected_gjj_verification_method)));
				alertSelectDialog("", list, new StringSelectDialogClick() {

					@Override
					public void onClick(View view, XfjrDialog dialog) {
						CheckedTextView ctvContent = (CheckedTextView) view;
						int position = (int) ctvContent.getTag();
						if (!ctvContent.isChecked()) {
							ctvContent.setChecked(true);
							tvPublicCheck.setTag("" + (position + 1));
							selected(tvPublicCheck, list.get(position));
							dialog.cancel();
						}
					}
				});
			}
		});
		final TextView tvPayStartTime = findTextView(publicFoundView, R.id.tvPayStartTime); // 缴纳起始日
		tvPayStartTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mTimePickerDialog = new MaxDatePickerDialog(baseActivity,
						new TimePickerDialogInterface() {

							@Override
							public void positiveListener() {
								int y = mTimePickerDialog.getYear();
								int m = mTimePickerDialog.getMonth();
								int d = mTimePickerDialog.getDay();
								String payStartTime = y + "-" + m + "-" + d;
								tvPayStartTime.setTag(payStartTime);
								selected(tvPayStartTime, payStartTime);
							}

							@Override
							public void negativeListener() {

							}
						});
				mTimePickerDialog.showDatePickerDialog();
			}
		});
		llContainer.addView(publicFoundView, params);
	}
	
	/**
	 * 添加社保类view
	 * 
	 * @param type
	 */
	private void initSocialCharityView(LayoutParams params) {
		socialView = LayoutInflater.from(getActivity()).inflate(R.layout.xfjr_layout_social_charity_info, llContainer, false);//
		setDrawableRight(findTextView(socialView, R.id.tvSocialCharityInfo), findView(socialView, R.id.llSocialStub));
		final TextView tvSocialCheckMethod = findTextView(socialView, R.id.tvSocialCheckMethod); // 核验方式
		final XFJRMoneyClearEditText etPreMonthPay = (XFJRMoneyClearEditText) findEditText(socialView, R.id.etPreMonthPay);
		final XFJRMoneyClearEditText etComMonthPay = (XFJRMoneyClearEditText) findEditText(socialView, R.id.etComMonthPay);
		etPreMonthPay.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				etPreMonthPay.onFocusChange(v, hasFocus); // 不调这个方法就不会显示delete图标
				if (!hasFocus) {
					TextUtil.suffix00(etPreMonthPay);
				}
			}
			
		});
		etComMonthPay.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				etComMonthPay.onFocusChange(v, hasFocus); // 不调这个方法就不会显示delete图标
				if (!hasFocus) {
					TextUtil.suffix00(etComMonthPay);
				}
			}
			
		});
		tvSocialCheckMethod.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final List<String> list = new ArrayList<>();
				list.addAll(Arrays.asList(getActivity().getResources().getStringArray(R.array.selected_social_verification_method)));
				alertSelectDialog("", list, new StringSelectDialogClick() {

					@Override
					public void onClick(View view, XfjrDialog dialog) {
						CheckedTextView ctvContent = (CheckedTextView) view;
						int position = (int) ctvContent.getTag();
						if (!ctvContent.isChecked()) {
							ctvContent.setChecked(true);
							tvSocialCheckMethod.setTag((position + 1) + "");
							selected(tvSocialCheckMethod, list.get(position));
							dialog.cancel();
						}
					}
				});
			}
		});
		final TextView tvSocialPayStartTime = findTextView(socialView, R.id.tvSocialPayStartTime); // 缴纳起始日
		tvSocialPayStartTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mTimePickerDialog = new MaxDatePickerDialog(baseActivity,
						new TimePickerDialogInterface() {

							@Override
							public void positiveListener() {
								int y = mTimePickerDialog.getYear();
								int m = mTimePickerDialog.getMonth();
								int d = mTimePickerDialog.getDay();
								String socialPayStartTime = y + "-" + m + "-" + d;
								tvSocialPayStartTime.setTag(socialPayStartTime);
								selected(tvSocialPayStartTime, socialPayStartTime);
							}

							@Override
							public void negativeListener() {

							}
						});
				mTimePickerDialog.showDatePickerDialog();
			}
		});
		RadioGroup rgIsMatchSj = (RadioGroup) socialView.findViewById(R.id.rgIsMatch);
		initOrdinaryRadioGroupChange(rgIsMatchSj, new ChangeCallBack() {
			
			@Override
			public void onChange(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rbYes:
					isMatchSj = "Y";
					break;
					
				case R.id.rbNo:
					isMatchSj = "N";
					break;
				}
				
			}
		});
		llContainer.addView(socialView, params);
	}
	
	/**
	 * 添加房产类view
	 * 
	 * @param type
	 */
	private void initHouseChildView(final LinearLayout lltHousePerInfo) {
		final View housePerInfo = LayoutInflater.from(getActivity()).inflate(R.layout.xfjr_item_house_property_info, lltHousePerInfo, false);//
		TextView tvHouseNo = findTextView(housePerInfo, R.id.tvHouseNo);
//		final TextView tvDownPayment = findTextView(housePerInfo, R.id.tvDownPayment); // 房贷金额(选填)
//		final TextView tvPayYear = findTextView(housePerInfo, R.id.tvPayYear); // 房贷已还款年限(选填)
		final TextView tvNoPayOffAmt = findTextView(housePerInfo, R.id.tvNoPayOffAmt); // 未结清住房贷款金额(选填)
		final XFJRMoneyClearEditText etNoPayOffAmt = (XFJRMoneyClearEditText) findEditText(housePerInfo, R.id.etNoPayOffAmt);
		etNoPayOffAmt.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				etNoPayOffAmt.onFocusChange(v, hasFocus); // 不调这个方法就不会显示delete图标
				if (!hasFocus) {
					TextUtil.suffix00(etNoPayOffAmt);
				}
			}
			
		});
		EditText etHouseOwner = findEditText(housePerInfo, R.id.etHouseOwner);
		etHouseOwner.setText(applicantName);
		etHouseOwner.addTextChangedListener(new LimitInputTextWatcher(etHouseOwner, LimitInputTextWatcher.a_zA_Z_CN_REGEX)); // 限制只能输入中文和英文
		tvHouseNo.setText(String.format(getString(R.string.house_number), houseNo++));
		final TextView tvCityId = findTextView(housePerInfo, R.id.tvCityId);
		final TextView tvChildCityId = findTextView(housePerInfo, R.id.tvChildCityId);
		tvCityId.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				chooseHouseCity(housePerInfo, tvCityId);
			}
		});
		tvChildCityId.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				chooseHouseChildCity(tvChildCityId);
			}
		});
		final TextView tvHouseCheck = findTextView(housePerInfo, R.id.tvHouseCheck);
		tvHouseCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final List<String> list = new ArrayList<>();
				list.addAll(Arrays.asList(getActivity().getResources().getStringArray(R.array.selected_house_check)));
				alertSelectDialog("", list, new StringSelectDialogClick() {

					@Override
					public void onClick(View view, XfjrDialog dialog) {
						CheckedTextView ctvContent = (CheckedTextView) view;
						int position = (int) ctvContent.getTag();
						if (!ctvContent.isChecked()) {
							ctvContent.setChecked(true);
							if (position == 0) { // 征信报告未结清房贷信息
//								tvDownPayment.setText("房贷金额(必填)");
//								tvPayYear.setText(" 房贷已还款年限(必填)");
								tvNoPayOffAmt.setText("未结清住房贷款金额(必填)");
								// TODO 添加条件：未结清住房贷款金额，如果选择的区域是地级市区，输入的金额应大于30万，南昌市区及南昌市辖属县城所在镇，输入的金额应大于35万。
								//findTextView(housePerInfo, R.id.lltNoPayOffAmt).setVisibility(View.VISIBLE);
							} else { // 备案后的购房合同 或 查档的房产证
//								tvDownPayment.setText("房贷金额(选填)");
//								tvPayYear.setText(" 房贷已还款年限(选填)");
								tvNoPayOffAmt.setText("未结清住房贷款金额(选填)");
								//findTextView(housePerInfo, R.id.lltNoPayOffAmt).setVisibility(View.GONE);
								// TODO 此时输入的 房产1面积应大于等于70平。
							}
							tvHouseCheck.setTag((position + 1) + "");
							selected(tvHouseCheck, list.get(position));
							dialog.cancel();
						}
					}
				});
			}
		});
//		RadioGroup rgIsLoanMortgage = (RadioGroup) findView(housePerInfo, R.id.rgIsLoanMortgage);
//		rgIsLoanMortgage.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				switch (checkedId) {
//				case R.id.rgYes:
//					findView(housePerInfo, R.id.lltLoanMortgage).setVisibility(View.VISIBLE);
//					break;
//
//				case R.id.rgNo:
//					findView(housePerInfo, R.id.lltLoanMortgage).setVisibility(View.GONE);
//					break;
//				}
//				
//			}
//		});
		ImageView ivClose = (ImageView) findView(housePerInfo, R.id.ivClose);
		ivClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				XFJRDialogUtil.confirmDialog(getActivity(), "是否确认删除?", new DialogClick() {

					@Override
					public void onOkClick(View view, XfjrDialog dialog) {
						lltHousePerInfo.removeView(housePerInfo);
						houseNo = 2;
						for (int i = 0; i < lltHousePerInfo.getChildCount(); i++) {
							View childView = lltHousePerInfo.getChildAt(i);
							if (null != childView) {
								TextView tvHouseNo = findTextView(childView, R.id.tvHouseNo);
								tvHouseNo.setText(String.format(getString(R.string.house_number), houseNo++));
							}
						}
						dialog.dismiss();
					}

					@Override
					public void onCancelClick(View view, XfjrDialog dialog) {
						dialog.dismiss();
					}
				}).show();
			}
		});
		lltHousePerInfo.addView(housePerInfo);
	}
	
	/**
	 * 添加房产类view
	 * 
	 * @param type
	 */
	private void initHouseView(LayoutParams params) {
		houseView = LayoutInflater.from(getActivity()).inflate(R.layout.xfjr_layout_house_property_info, llContainer, false);//
		EditText etHouseOwner = findEditText(houseView, R.id.etHouseOwner);
		etHouseOwner.setText(applicantName);
		etHouseOwner.addTextChangedListener(new LimitInputTextWatcher(etHouseOwner, LimitInputTextWatcher.a_zA_Z_CN_REGEX)); // 限制只能输入中文和英文
		setDrawableRight(findTextView(houseView, R.id.tvHouseInfo), findView(houseView, R.id.lltHouseInfo));
		final TextView tvCityId = findTextView(houseView, R.id.tvCityId);
		final TextView tvChildCityId = findTextView(houseView, R.id.tvChildCityId);
//		final TextView tvDownPayment = findTextView(houseView, R.id.tvDownPayment); // 房贷金额(选填)
//		final TextView tvPayYear = findTextView(houseView, R.id.tvPayYear); // 房贷已还款年限(选填)
		final TextView tvNoPayOffAmt = findTextView(houseView, R.id.tvNoPayOffAmt); // 未结清住房贷款金额(选填)
		final XFJRMoneyClearEditText etNoPayOffAmt = (XFJRMoneyClearEditText) findEditText(houseView, R.id.etNoPayOffAmt);
		etNoPayOffAmt.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				etNoPayOffAmt.onFocusChange(v, hasFocus); // 不调这个方法就不会显示delete图标
				if (!hasFocus) {
					TextUtil.suffix00(etNoPayOffAmt);
				}
			}
			
		});
		tvCityId.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				chooseHouseCity(houseView, tvCityId);
			}
		});
		tvChildCityId.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				chooseHouseChildCity(tvChildCityId);
			}
		});
		final TextView tvHouseCheck = findTextView(houseView, R.id.tvHouseCheck);
		tvHouseCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final List<String> list = new ArrayList<>();
				list.addAll(Arrays.asList(getActivity().getResources().getStringArray(R.array.selected_house_check)));
				alertSelectDialog("", list, new StringSelectDialogClick() {

					@Override
					public void onClick(View view, XfjrDialog dialog) {
						CheckedTextView ctvContent = (CheckedTextView) view;
						int position = (int) ctvContent.getTag();
						if (!ctvContent.isChecked()) {
							ctvContent.setChecked(true);
							if (position == 0) { // 征信报告未结清房贷信息
//								tvDownPayment.setText("房贷金额(必填)");
//								tvPayYear.setText("房贷已还款年限(必填)");
								tvNoPayOffAmt.setText("未结清住房贷款金额(必填)");
							} else { // 备案后的购房合同 或 查档的房产证
//								tvDownPayment.setText("房贷金额(选填)");
//								tvPayYear.setText("房贷已还款年限(选填)");
								tvNoPayOffAmt.setText("未结清住房贷款金额(选填)");
							}
							tvHouseCheck.setTag((position + 1) + "");
							selected(tvHouseCheck, list.get(position));
							dialog.cancel();
						}
					}
				});
			}
		});
//		RadioGroup rgIsLoanMortgage = (RadioGroup) findView(houseView, R.id.rgIsLoanMortgage);
//		rgIsLoanMortgage.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				switch (checkedId) {
//				case R.id.rgYes:
//					findView(houseView, R.id.lltLoanMortgage).setVisibility(View.VISIBLE);
//					break;
//
//				case R.id.rgNo:
//					findView(houseView, R.id.lltLoanMortgage).setVisibility(View.GONE);
//					break;
//				}
//				
//			}
//		});
		final LinearLayout lltHousePerInfo = (LinearLayout) findView(houseView, R.id.lltHousePerInfo);
		findTextView(houseView, R.id.tvAddHomeInfo).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				initHouseChildView(lltHousePerInfo);
			}
		});
		llContainer.addView(houseView,params);
	}
	
	/**
	 * 显示信息
	 * 
	 * @param type
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void showView(int resId, TextView tv, View iv) {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		int margin = ScreenUtils.dip2px(getActivity(), 10);
		params.setMargins(0, 0, 0, margin);
		switch (resId) {
		case R.id.tvSelectProvidentFundInfo:
			initPublicFoundView(params);
			break;
		case R.id.tvSocialCharityInfo:
			initSocialCharityView(params);
			break;
		case R.id.tvHousePropertyInfo:
			initHouseView(params);
			break;
		default:
			break;
		}
		iv.setVisibility(View.VISIBLE);
		tv.setBackground(getResources().getDrawable(R.drawable.xfjr_shape_circle_corner_red_tv));
		tv.setTextColor(getResources().getColor(R.color.xfjr_red));
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void deleteView(int resId, TextView tv, View iv) {
		switch (resId) {
		case R.id.tvSelectProvidentFundInfo:
			llContainer.removeView(publicFoundView);
			publicFoundView = null;
			publicFoundBean = null;
			break;
		case R.id.tvSocialCharityInfo:
			llContainer.removeView(socialView);
			socialView = null;
			socialCharityBean = null;
			break;
		case R.id.tvHousePropertyInfo:
			llContainer.removeView(houseView);
			houseNo = 2;
			houseView = null;
			break;
		default:
			break;
		}
		iv.setVisibility(View.INVISIBLE);
		tv.setBackground(getResources().getDrawable(R.drawable.xfjr_shape_circle_corner_gray_tv));
		tv.setTextColor(getResources().getColor(R.color.xfjr_black6));
	}
	
	/**
	 * 删除view提示对话框
	 */
	private void confirmDialog(final int id, final TextView tv, final View iv) {
		if (fxModel.equals("ALL") && PretrialBasicInfoFragment.hasHouse && id == R.id.tvHousePropertyInfo) {
			LogUtils.e("PretrialCustomTypeInfoFragment----授信模型为ALL且有房产，房产信息一定要填，不可取消");
		} else {
			XFJRDialogUtil.confirmDialog(getActivity(), "是否确认取消?", new DialogClick() {
				
				@Override
				public void onOkClick(View view, XfjrDialog dialog) {
					deleteView(id, tv, iv);
					dialog.dismiss();
				}
				
				@Override
				public void onCancelClick(View view, XfjrDialog dialog) {
					dialog.dismiss();
				}
			}).show();
		}
	}

	/**
	 * 选择房产类型
	 * 
	 * @param tv
	 */
	protected void chooseHouseCity(final View rootView, final TextView tv) {
		final List<CityListBean> cityList = new ArrayList<>();
		if (XfjrMain.isNet) {
			cityList.clear();
			SystemBasicInfo info = SystemBasicJSONWRUtil.readSystemBasicInfo(getActivity());
			if (null != info) {
				if (null != info.getCityList()) {
					cityList.addAll(info.getCityList());
				}
			}
		} else {
			cityList.clear();
			CityListBean cityListBean1 = new CityListBean("1", "深圳市");
			CityListBean cityListBean2 = new CityListBean("2", "广州市");
			CityListBean cityListBean3 = new CityListBean("3", "一级城市1");
			CityListBean cityListBean4 = new CityListBean("4", "一级城市2");
			ChildCityBean childCityBean1 = new ChildCityBean();
			childCityBean1.setCityId("21");
			childCityBean1.setCityName("二级城市1");
			ChildCityBean childCityBean2 = new ChildCityBean();
			childCityBean2.setCityId("41");
			childCityBean2.setCityName("二级城市2");
			List<ChildCityBean> childCity1 = new ArrayList<>();
			List<ChildCityBean> childCity2 = new ArrayList<>();
			childCity1.add(childCityBean1);
			childCity2.add(childCityBean2);
			cityListBean3.setChildCity(childCity1);
			cityListBean4.setChildCity(childCity2);
			cityList.add(cityListBean1);
			cityList.add(cityListBean2);
			cityList.add(cityListBean3);
			cityList.add(cityListBean4);
		}
		XFJRDialogUtil.showCityListListDialog(getActivity(), "", cityList, new CityListSelectDialogClick() {

			@Override
			public void onClick(View view, XfjrDialog dialog, CityListBean bean) {
				CheckedTextView ctvContent = (CheckedTextView) view;
				if (!ctvContent.isChecked()) {
					ctvContent.setChecked(true);
					tv.setTag(bean); 
					if (null != bean.getChildCity() && bean.getChildCity().size() > 0) {
						// 显示二级城市列表
						findView(rootView, R.id.lltChildCity).setVisibility(View.VISIBLE);
						childCityList.clear();
						childCityList.addAll(bean.getChildCity());
						selected(findTextView(rootView, R.id.tvChildCityId), childCityList.get(0).getCityName());
						findTextView(rootView, R.id.tvChildCityId).setTag(childCityList.get(0));
					} else {
						findView(rootView, R.id.lltChildCity).setVisibility(View.GONE);
					}
					selected(tv, bean.getCityName());
					dialog.cancel();
				}
			}
		});
	}
	
	/**
	 * 选择二级城市列表
	 * 
	 * @param tv
	 */
	protected void chooseHouseChildCity(final TextView tv) {
		XFJRDialogUtil.showChildCityListDialog(getActivity(), "", childCityList, new ChildCityListSelectDialogClick() {
			
			@Override
			public void onClick(View view, XfjrDialog dialog, ChildCityBean bean) {
				CheckedTextView ctvContent = (CheckedTextView) view;
				if (!ctvContent.isChecked()) {
					ctvContent.setChecked(true);
					dialog.cancel();
					tv.setTag(bean); 
					selected(tv, bean.getCityName());
				}
			}
		});
	}

	/**
	 * 设置radiogroup变化监听
	 * 
	 * @param radioGroup
	 * @param callBack
	 */
	private void initOrdinaryRadioGroupChange(RadioGroup radioGroup,
			final ChangeCallBack callBack) {
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				callBack.onChange(group, checkedId);
			}
		});
	}

	/**
	 * // 初始化以及点击事件缩小打开几个模块时候的图片切换，以及显示隐藏 大视图 即客户类型视图的显示隐藏切换
	 * 
	 * @param TextView
	 * @param layout
	 */
	private void setDrawableRight(final TextView tv, final View layout) {
		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				@SuppressWarnings("deprecation")
				Drawable drawable = getResources()
						.getDrawable(layout.getVisibility() == View.VISIBLE
								? R.drawable.xfjr_pretrial_close : R.drawable.xfjr_pretrial_open);
				// drawable.getMinimumWidth() drawable.getMinimumHeight()
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				tv.setCompoundDrawables(null, null, drawable, null);
				layout.setVisibility(
						layout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
			}
		});
	}

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

	/**
	 * 选择框
	 * 
	 * @param selected
	 * @param list
	 * @param dialogClick
	 */
	@SuppressLint("NewApi")
	private void alertSelectDialog(final String selected, List<String> list, final StringSelectDialogClick dialogClick) {
		XFJRDialogUtil.alertStringSelectDialog(getActivity(), selected, list, dialogClick);
	}

	/**
	 * 找TextView
	 * 
	 * @param view
	 * @param id
	 * @return
	 */
	private TextView findTextView(View view, int id) {
		return (TextView) view.findViewById(id);
	}

	/**
	 * 找TextView
	 * 
	 * @param view
	 * @param id
	 * @return
	 */
	private EditText findEditText(View view, int id) {
		return (EditText) view.findViewById(id);
	}

	/**
	 * 找控件
	 * 
	 * @param view
	 * @param id
	 * @return
	 */
	private View findView(View view, int id) {
		return view.findViewById(id);
	}

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

	private boolean checkEmpty() {
		/*if (ivSelectProvidentFundInfo.getVisibility() == View.INVISIBLE
				&& ivSocialCharityInfo.getVisibility() == View.INVISIBLE
				&& ivHousePropertyInfo.getVisibility() == View.INVISIBLE) {
			ToastUtils.show(getActivity(), "请选择至少一种信息填写", 0);
			return true;
		} else */
		for (int i = 0; i < llContainer.getChildCount(); i++) {
			View child = llContainer.getChildAt(i);
			if (houseView == child) { // 房产
				LogUtils.e("PretrialCustomTypeInfoFragment----checkHouseEmpty()-->" + checkHouseEmpty());
				LogUtils.e("PretrialCustomTypeInfoFragment----checkHouseChildEmpty()-->" + checkHouseChildEmpty());
				if (checkHouseEmpty()) return true; // 房产1
				if (checkHouseChildEmpty()) return true; // 其他房产
			}
			if (publicFoundView == child) { // 房产
				LogUtils.e("PretrialCustomTypeInfoFragment----checkPublicFoundViewEmpty()-->" + checkPublicFoundViewEmpty());
				if (checkPublicFoundViewEmpty()) return true;
			}
			if (socialView == child) { // 房产
				LogUtils.e("PretrialCustomTypeInfoFragment----checkPublicFoundViewEmpty()-->" + checkPublicFoundViewEmpty());
				if (checkSocialViewEmpty()) return true;
			}
		}
//		if (checkHouseEmpty()) { // 房产
//			return true;
//		} else if (checkHouseChildEmpty()) { // 其他房产
//			return true;
//		} else if (checkPublicFoundViewEmpty()) { // 公积金
//			return true;
//		} else if (checkSocialViewEmpty()) { // 社保
//			return true;
//		}
		return false;
	}

	private void showEmptyTips(TextView textView) {
		ToastUtils.show(getActivity(), textView.getHint().toString(), 0);
	}

	private boolean checkSocialViewEmpty() {
		if (null != socialView) {
			String verificationMethod = findTextView(socialView, R.id.tvSocialCheckMethod).getText().toString();
			String commonCompany = findEditText(socialView, R.id.etSocialCompany).getText().toString();
			String socialTime = findTextView(socialView, R.id.tvSocialPayStartTime).getText().toString();
			String monthPay = ((XFJRMoneyClearEditText)findEditText(socialView, R.id.etPreMonthPay)).getTextString();
			String comMonthPay = ((XFJRMoneyClearEditText)findEditText(socialView, R.id.etComMonthPay)).getTextString();
			findEditText(socialView, R.id.etPreMonthPay).clearFocus();
			findEditText(socialView, R.id.etComMonthPay).clearFocus();
			if (TextUtils.isEmpty(verificationMethod)) {
				showEmptyTips(findTextView(socialView, R.id.tvSocialCheckMethod));
				return true;
			}
			if (TextUtils.isEmpty(commonCompany)) {
				showEmptyTips(findEditText(socialView, R.id.etSocialCompany));
				return true;
			}
			if (TextUtils.isEmpty(socialTime)) {
				showEmptyTips(findTextView(socialView, R.id.tvSocialPayStartTime));
				return true;
			}
			if (TextUtils.isEmpty(monthPay)) {
				showEmptyTips(findEditText(socialView, R.id.etPreMonthPay));
				return true;
			}
			if (TextUtils.isEmpty(comMonthPay)) {
				showEmptyTips(findEditText(socialView, R.id.etComMonthPay));
				return true;
			}
			if (null == socialCharityBean) {
				socialCharityBean = new DescD();
			}
			verificationMethod = (String) findTextView(socialView, R.id.tvSocialCheckMethod).getTag();
			socialCharityBean.setSjChect(verificationMethod);
			socialCharityBean.setSjPayCom(commonCompany); //社保金缴存单位
			socialCharityBean.setSjPayStart(socialTime);
			socialCharityBean.setSjMatch(isMatchSj);
			socialCharityBean.setSjPreMonthPay(monthPay);
			socialCharityBean.setSjComMonthPay(comMonthPay);
		} else {
			return false;
		}
		return false;
	}

	private void resetSocialView() {
		if (null != socialView) {
			unSelected(findTextView(socialView, R.id.tvSocialCheckMethod), null);
			unSelected(findTextView(socialView, R.id.tvSocialPayStartTime), null);
			EditText commonCompany1 = findEditText(socialView, R.id.etSocialCompany);
			EditText monthPay = findEditText(socialView, R.id.etPreMonthPay);
			EditText comMonthPay = findEditText(socialView, R.id.etComMonthPay);
			commonCompany1.setText("");
			monthPay.setText("");
			comMonthPay.setText("");
			commonCompany1.clearFocus();
			monthPay.clearFocus();
			comMonthPay.clearFocus();
			socialCharityBean = null;
		}
	}

	/**
	 * 房产类客户输入信息判空
	 * 
	 * @return
	 */
	private boolean checkHouseEmpty() {
		if (houseView != null) {
			EditText etHouseOwner = findEditText(houseView, R.id.etHouseOwner);// 房产业主houseOwner
			EditText etHouseArea = findEditText(houseView, R.id.etHouseArea);// 房产面积houseArea
			TextView tvCityId = findTextView(houseView, R.id.tvCityId);// 房产所在城市cityId
			TextView tvChildCityId = findTextView(houseView, R.id.tvChildCityId);// 房产所在城市cityId
			// 房产信息核查方式houseCheck、1征信报告未结清房贷信息、2备案后的购房合同、3查档的房产证
			TextView tvHouseCheck = findTextView(houseView, R.id.tvHouseCheck);
			XFJRMoneyClearEditText etNoPayOffAmt = (XFJRMoneyClearEditText) findEditText(houseView, R.id.etNoPayOffAmt);//
			etNoPayOffAmt.clearFocus();
			if (TextUtils.isEmpty(etHouseOwner.getText())) {
				showEmptyTips(etHouseOwner);
				return true;
			}
			if (TextUtils.isEmpty(etHouseArea.getText())) {
				showEmptyTips(etHouseArea);
				return true;
			}
			if (TextUtils.isEmpty(tvCityId.getText())) {
				showEmptyTips(tvCityId);
				return true;
			}
			if (TextUtils.isEmpty(tvHouseCheck.getText())) {
				showEmptyTips(tvHouseCheck);
				return true;
			}
			String houseOwner = etHouseOwner.getText().toString().trim();
			String houseArea = etHouseArea.getText().toString().trim();
			CityListBean city = (CityListBean) tvCityId.getTag();
			String cityId = city.getCityId();
			if (null != city.getChildCity() && city.getChildCity().size() > 0) {
				if (TextUtils.isEmpty(tvChildCityId.getText())) {
					ToastUtils.show(getActivity(), tvChildCityId.getHint().toString(), 0);
					return true;
				}
				ChildCityBean childCityBean = (ChildCityBean) tvChildCityId.getTag();
				cityId = childCityBean.getCityId();
			}
			String isMortgage = "N"; // 是否按揭贷款(Y/N)
			String houseCheck = (String) tvHouseCheck.getTag();
			String noPayOffAmt = etNoPayOffAmt.getTextString();
			if ("1".equals(houseCheck)) {
				isMortgage = "Y"; // 是否按揭贷款(Y/N)
//				XFJRMoneyClearEditText etDownPayment = (XFJRMoneyClearEditText) findEditText(houseView, R.id.etDownPayment);//
//				XFJRDecimalClearEditText etPayYear = (XFJRDecimalClearEditText) findEditText(houseView, R.id.etPayYear);//
//				if (TextUtils.isEmpty(etDownPayment.getText())) {
//					ToastUtils.show(getActivity(), etDownPayment.getHint().toString(), 0);
//					return true;
//				}
//				if (TextUtils.isEmpty(etPayYear.getText())) {
//					ToastUtils.show(getActivity(), etPayYear.getHint().toString(), 0);
//					return true;
//				}
				if (TextUtils.isEmpty(etNoPayOffAmt.getText())) {
					ToastUtils.show(getActivity(), etNoPayOffAmt.getHint().toString(), 0);
					return true;
				}
//				if (Double.parseDouble(noPayOffAmt) < 300000) {
//					ToastUtils.show(getActivity(), "未结清住房贷款金额不可小于30万", 0);
//					return true;
//				}
//				String firPayPay = etDownPayment.getTextString();
//				String repayYear = etPayYear.getTextString();
				houseBeanList.clear();
				houseBeanList.add(new DescE(houseOwner, cityId, houseArea, houseCheck, isMortgage, noPayOffAmt));
			} else {
//				if (Double.parseDouble(houseArea) < 70) {
//					ToastUtils.show(getActivity(), "房产面积不可小于70平方米", 0);
//					return true;
//				}
				isMortgage = "N";  // 是否按揭贷款(Y/N)
				//houseOwner, cityId, houseArea, housePropId, isMortgage, firPayPay, repayYear
				houseBeanList.clear();
				if (TextUtils.isEmpty(etNoPayOffAmt.getText())) {
					houseBeanList.add(new DescE(houseOwner, cityId, houseArea, houseCheck, isMortgage));
				} else {
					houseBeanList.add(new DescE(houseOwner, cityId, houseArea, houseCheck, isMortgage, noPayOffAmt));
				}
			}
			return false; // 不判断
		} else {
			return false; // 不判断
		}
	}

	private void resetHouseView() {
		if (houseView != null) {
			findView(houseView, R.id.lltChildCity).setVisibility(View.GONE);
//			EditText houseOwner = findEditText(houseView, R.id.etHouseOwner);// 房产业主houseOwner
			EditText houseArea = findEditText(houseView, R.id.etHouseArea);// 房产面积houseArea
			TextView tvCityId = findTextView(houseView, R.id.tvCityId);// 房产所在城市cityId
			TextView tvChildCityId = findTextView(houseView, R.id.tvChildCityId);// 房产所在城市cityId
			TextView houseCheckStr = findTextView(houseView, R.id.tvHouseCheck);
//			houseOwner.setText("");
			houseArea.setText("");
			CityListBean city = (CityListBean) tvCityId.getTag();
			if (null != city) {
				if (null != city.getChildCity() && city.getChildCity().size() > 0) {
					unSelected(tvChildCityId, null);
				}
			}
			unSelected(tvCityId, null);
			unSelected(houseCheckStr, null);
//			houseOwner.clearFocus();
			houseArea.clearFocus();
			EditText etNoPayOffAmt = findEditText(houseView, R.id.etNoPayOffAmt);// 房产面积houseArea
			TextView tvNoPayOffAmt = findTextView(houseView, R.id.tvNoPayOffAmt);
			tvNoPayOffAmt.setText("未结清住房贷款金额(选填)");
			etNoPayOffAmt.setText("");
			etNoPayOffAmt.clearFocus();
//			if (findView(houseView, R.id.lltLoanMortgage).getVisibility() == View.VISIBLE) {
//				findEditText(houseView, R.id.etDownPayment).setText("");//
//				findEditText(houseView, R.id.etPayYear).setText("");//
//				findEditText(houseView, R.id.etDownPayment).clearFocus();//
//				findEditText(houseView, R.id.etPayYear).clearFocus();//
//			}
			//resetHouseChildView(); // TODO 清空数据，不删视图
			removeAllHouseChildView(); // TODO 清空数据，删视图
			houseBeanList.clear();
		}
	}
	
	/**
	 * 第二、三...套房产
	 * 
	 * @return
	 */
	private boolean checkHouseChildEmpty() {
		if (null != houseView) {
			LinearLayout lltHousePerInfo = (LinearLayout) findView(houseView, R.id.lltHousePerInfo);
			for (int i = 0; i < lltHousePerInfo.getChildCount(); i++) {
				View childView = lltHousePerInfo.getChildAt(i);
				if (null != childView) {
					EditText etHouseOwner = findEditText(childView, R.id.etHouseOwner);//
					EditText etHouseArea = findEditText(childView, R.id.etHouseArea);//
					TextView tvCityId = findTextView(childView, R.id.tvCityId);//
					TextView tvChildCityId = findTextView(childView, R.id.tvChildCityId);//
					XFJRMoneyClearEditText etNoPayOffAmt = (XFJRMoneyClearEditText) findEditText(childView, R.id.etNoPayOffAmt);//
					etNoPayOffAmt.clearFocus();
					if (TextUtils.isEmpty(etHouseOwner.getText())) {
						ToastUtils.show(getActivity(), etHouseOwner.getHint().toString(), 0);
						return true;
					}
					if (TextUtils.isEmpty(etHouseArea.getText())) {
						ToastUtils.show(getActivity(), etHouseArea.getHint().toString(), 0);
						return true;
					}
					if (TextUtils.isEmpty(tvCityId.getText())) {
						ToastUtils.show(getActivity(), tvCityId.getHint().toString(), 0);
						return true;
					}
					String houseOwner = etHouseOwner.getText().toString().trim();
					String houseArea = etHouseArea.getText().toString().trim();
					CityListBean city = (CityListBean) tvCityId.getTag(); // 
					String cityId = city.getCityId();
					if (null != city.getChildCity() && city.getChildCity().size() > 0) {
						if (TextUtils.isEmpty(tvChildCityId.getText())) {
							ToastUtils.show(getActivity(), tvChildCityId.getHint().toString(), 0);
							return true;
						}
						ChildCityBean childCityBean = (ChildCityBean) tvChildCityId.getTag();
						cityId = childCityBean.getCityId();
					}
//					ChildCityBean childCity = (ChildCityBean) tvChildCityId.getTag(); // 
					String isMortgage = "N"; // 是否按揭贷款(Y/N)
					TextView tvHouseCheck = findTextView(childView, R.id.tvHouseCheck);//
					if (TextUtils.isEmpty(tvHouseCheck.getText())) {
						ToastUtils.show(getActivity(), tvHouseCheck.getHint().toString(), 0);
						return true;
					}
					String houseCheck = (String) tvHouseCheck.getTag();
					String noPayOffAmt = etNoPayOffAmt.getTextString();
					if ("1".equals(houseCheck)) {
						isMortgage = "Y"; // 是否按揭贷款(Y/N)
//						XFJRMoneyClearEditText etDownPayment = (XFJRMoneyClearEditText) findEditText(childView, R.id.etDownPayment);//
//						XFJRDecimalClearEditText etPayYear = (XFJRDecimalClearEditText) findEditText(childView, R.id.etPayYear);//
//						if (TextUtils.isEmpty(etDownPayment.getText())) {
//							ToastUtils.show(getActivity(), etDownPayment.getHint().toString(), 0);
//							return true;
//						}
//						if (TextUtils.isEmpty(etPayYear.getText())) {
//							ToastUtils.show(getActivity(), etPayYear.getHint().toString(), 0);
//							return true;
//						}
//						String firPayPay = etDownPayment.getTextString();
//						String repayYear = etPayYear.getTextString();
						if (TextUtils.isEmpty(etNoPayOffAmt.getText())) {
							ToastUtils.show(getActivity(), etNoPayOffAmt.getHint().toString(), 0);
							return true;
						}
//						if (Double.parseDouble(noPayOffAmt) < 300000) {
//							ToastUtils.show(getActivity(), "未结清住房贷款金额不可小于30万", 0);
//							return true;
//						}
						houseBeanList.add(new DescE(houseOwner, cityId, houseArea, houseCheck, isMortgage, noPayOffAmt));
					} else {
//						if (Double.parseDouble(houseArea) < 70) {
//							ToastUtils.show(getActivity(), "房产面积不可小于70平方米", 0);
//							return true;
//						}
						isMortgage = "N";  // 是否按揭贷款(Y/N)
						if (TextUtils.isEmpty(etNoPayOffAmt.getText())) {
							houseBeanList.add(new DescE(houseOwner, cityId, houseArea, houseCheck, isMortgage));
						} else {
							houseBeanList.add(new DescE(houseOwner, cityId, houseArea, houseCheck, isMortgage, noPayOffAmt));
						}
						//houseOwner, cityId, houseArea, housePropId, isMortgage, firPayPay, repayYear
					}
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
		return false;
	}
	
	private void removeAllHouseChildView() {
		if (null != houseView) {
			LinearLayout lltHousePerInfo = (LinearLayout) findView(houseView, R.id.lltHousePerInfo);
			lltHousePerInfo.removeAllViews();
			houseNo = 2;
		}
	}
	
//	private void resetHouseChildView() {
//		if (null != houseView) {
//			LinearLayout lltHousePerInfo = (LinearLayout) findView(houseView, R.id.lltHousePerInfo);
//			for (int i = 0; i < lltHousePerInfo.getChildCount(); i++) {
//				View childView = lltHousePerInfo.getChildAt(i);
//				if (null != childView) {
//					findView(childView, R.id.lltChildCity).setVisibility(View.GONE);
//					TextView tvCityId = findTextView(childView, R.id.tvCityId);//
//					TextView tvChildCityId = findTextView(childView, R.id.tvChildCityId);//
//					TextView tvHouseCheck = findTextView(childView, R.id.tvHouseCheck);//
////					findEditText(childView, R.id.etHouseOwner).setText("");
//					findEditText(childView, R.id.etHouseArea).setText("");
////					findEditText(childView, R.id.etHouseOwner).clearFocus();
//					findEditText(childView, R.id.etHouseArea).clearFocus();
//					CityListBean city = (CityListBean) tvCityId.getTag(); // 
//					unSelected(tvCityId, null);
//					if (null != city) {
//						if (null != city.getChildCity() && city.getChildCity().size() > 0) {
//							unSelected(tvChildCityId, null);
//						}
//					}
//					unSelected(tvHouseCheck, null);
//					findEditText(childView, R.id.etNoPayOffAmt).setText("");//
//					findEditText(childView, R.id.etNoPayOffAmt).clearFocus();//
////					if (findView(childView, R.id.lltLoanMortgage).getVisibility() == View.VISIBLE) {
////						findEditText(childView, R.id.etDownPayment).setText("");//
////						findEditText(childView, R.id.etPayYear).setText("");//
////						findEditText(childView, R.id.etDownPayment).clearFocus();//
////						findEditText(childView, R.id.etPayYear).clearFocus();//
////					}
//				}
//			}
//		}
//	}

	private boolean checkPublicFoundViewEmpty() {
		if (null != publicFoundView) {
			String check = findTextView(publicFoundView, R.id.tvPublicCheck).getText().toString();
			String payCom = findEditText(publicFoundView, R.id.etCommonCompany).getText().toString();
			String payStartTime = findTextView(publicFoundView, R.id.tvPayStartTime).getText().toString();
			String monthPay = ((XFJRMoneyClearEditText)findEditText(publicFoundView, R.id.etPreMonthPay)).getTextString(); // 注意金额符号
			String monthRate = findEditText(publicFoundView, R.id.etPreMonthRate).getText().toString();
			String comMonthPay = ((XFJRMoneyClearEditText)findEditText(publicFoundView, R.id.etComMonthPay)).getTextString();
			String comMonthRate = findEditText(publicFoundView, R.id.etComMonthRate).getText().toString();
			findEditText(publicFoundView, R.id.etPreMonthPay).clearFocus();
			findEditText(publicFoundView, R.id.etComMonthPay).clearFocus();
			if (TextUtils.isEmpty(check)) {
				showEmptyTips(findTextView(publicFoundView, R.id.tvPublicCheck));
				return true;
			}
			if (TextUtils.isEmpty(payCom)) {
				showEmptyTips(findEditText(publicFoundView, R.id.etCommonCompany));
				return true;
			}
			if (TextUtils.isEmpty(payStartTime)) {
				showEmptyTips(findTextView(publicFoundView, R.id.tvPayStartTime));
				return true;
			}
			if (TextUtils.isEmpty(monthPay)) {
				showEmptyTips(findEditText(publicFoundView, R.id.etPreMonthPay));
				return true;
			}
			if (TextUtils.isEmpty(monthRate)) {
				showEmptyTips(findEditText(publicFoundView, R.id.etPreMonthRate));
				return true;
			}
			if (TextUtils.isEmpty(comMonthPay)) {
				showEmptyTips(findEditText(publicFoundView, R.id.etComMonthPay));
				return true;
			}
			if (TextUtils.isEmpty(comMonthRate)) {
				showEmptyTips(findEditText(publicFoundView, R.id.etComMonthRate));
				return true;
			}
			if (null == publicFoundBean) {
				publicFoundBean = new DescC();
			}
			check = (String) findTextView(publicFoundView, R.id.tvPublicCheck).getTag();
			publicFoundBean.setGjCheck(check);
			publicFoundBean.setGjPayCom(payCom);
			publicFoundBean.setGjPayStart(payStartTime);
			publicFoundBean.setGjMatch(isMatch);
			publicFoundBean.setGjPreMonthPay(monthPay);
			publicFoundBean.setGjPreMonthRate(monthRate);
			publicFoundBean.setGjComMonthPay(comMonthPay);
			publicFoundBean.setGjComMonthRate(comMonthRate);
		} else {
			return false;
		}
		return false;
	}

	private void resetPublicFoundView() {
		if (null != publicFoundView) {
			unSelected(findTextView(publicFoundView, R.id.tvPublicCheck), null);
			unSelected(findTextView(publicFoundView, R.id.tvPayStartTime), null);
			findEditText(publicFoundView, R.id.etCommonCompany).setText("");
			findEditText(publicFoundView, R.id.etPreMonthPay).setText("");
			findEditText(publicFoundView, R.id.etPreMonthRate).setText("");
			findEditText(publicFoundView, R.id.etComMonthPay).setText("");
			findEditText(publicFoundView, R.id.etComMonthRate).setText("");
			findEditText(publicFoundView, R.id.etCommonCompany).clearFocus();
			findEditText(publicFoundView, R.id.etPreMonthPay).clearFocus();
			findEditText(publicFoundView, R.id.etPreMonthRate).clearFocus();
			findEditText(publicFoundView, R.id.etComMonthPay).clearFocus();
			findEditText(publicFoundView, R.id.etComMonthRate).clearFocus();
			publicFoundBean = null;
		}
	}

	// 左边按钮
	@OnClick(R.id.btnLeft)
	private void clickStep(View view) {
		if (communicationCallBack != null) {
			communicationCallBack.backCallBack();
		}
	}

	// 弹出选择种类框
	@SuppressLint("NewApi")
	private void alertSelectDialog(final String selected, List<CustomType> list,
			final CustomTypeSelectDialogClick dialogClick) {
		LogUtils.e("集合：" + list.toString());
		XFJRDialogUtil.alertCustomTypeSelectDialog(getActivity(), selected, list, dialogClick);
	}

	/**
	 * radiogroup的切换监听回调
	 * 
	 * @author formssi
	 *
	 */
	private interface ChangeCallBack {
		void onChange(RadioGroup group, int checkedId);
	}


	private void submitPretrialInfo() {
		if (null != publicFoundBean) { // 公积金资料
			pretrialParamBean.setDescC(publicFoundBean);
		}
		if (null != socialCharityBean) { // 社保资料
			pretrialParamBean.setDescD(socialCharityBean);
		}
		if (null != houseBeanList && houseBeanList.size() > 0) {
			pretrialParamBean.setDescE(houseBeanList);
		}
		pretrialParamBean.setBusinessId(XfjrMain.businessId);
		if (XfjrMain.isNet) {
			HttpRequest.submitPretrialInfo(getActivity(), pretrialParamBean,
					PretrialBasicInfoFragment.photoFile, new IHttpCallback<PretrialResultBean>() {

						@Override
						public void onSuccess(String url, PretrialResultBean result) {
							// status 0通过 1是不通过（转人工）补充资料
							String status = result.getStatus();
							String creditLine = result.getLimit();
							boolean isPass = false;
							LogUtils.e("提交成功");
//							Intent intent = new Intent(XFJRConstant.ACTION_UPDATE_DATA);
							switch (status) {
							case "0": // 待预审
								isPass = true;
//								XfjrMain.businessStatus = "3";
//								LogUtils.e("结束评估发送广播更新列表,此时列表应该在待审批里面");
//								intent.putExtra(XFJRConstant.KEY_BUSINESS_STATUS, 0);
//								intent.putExtra(XFJRConstant.ACTION_FLAG_INT, 70);
								break;

							case "1": // 转人工
								isPass = false;
//								intent.putExtra(XFJRConstant.KEY_BUSINESS_STATUS, 1);
//								intent.putExtra(XFJRConstant.ACTION_FLAG_INT, 71);
//								LogUtils.e("结束评估发送广播更新列表,此时列表应该在转人工里面");
//								XfjrMain.businessStatus = "1";
								break;
							}
//							intent.putExtra(XFJRConstant.ACTION_FLAG_INT, ((XfjrMainActivity) getActivity()).productBean.getFrom());
//							getActivity().sendBroadcast(intent);
							if (communicationCallBack != null) {
								FileUtil.delAllImages(getActivity()); // 删除所有图片缓存
								PretrialBasicInfoFragment.photoFile = null;
								Map<String, Object> map = new HashMap<>();
								map.put(XFJRConstant.KEY_PRETRIAL_RESULT, isPass);
								map.put(XFJRConstant.KEY_CREDIT_LINE, creditLine);
								communicationCallBack.nextCallBack(map, fxModel);
							}
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
			XfjrMain.businessStatus = "3";
			String informationJson = PretrialJSONParam.createJSONParam(pretrialParamBean);
			LogUtils.e("PretrialCustomTypeInfoFragment----informationJson:" + informationJson);
//			if (communicationCallBack != null) {
//				PretrialBasicInfoFragment.photoFile = null;
//				Map<String, Object> map = new HashMap<>();
//				map.put(XFJRConstant.KEY_PRETRIAL_RESULT, true);
//				map.put(XFJRConstant.KEY_CREDIT_LINE, "10000.00");
//				communicationCallBack.nextCallBack(map);
//			}
		}
	}

	// 右边按钮
	@OnClick(R.id.btnRight)
	@CheckNet
	@Duplicate("PretrialCustomTypeInfoFragment.java")
	private void clickNext(View view) {
		if (checkEmpty()) {
			return;
		}
		submitPretrialInfo();
	}

	@Override
	public void onDestroy() {
		PretrialBasicInfoFragment.photoFile = null;
		PretrialBasicInfoFragment.hasHouse = false;
		super.onDestroy();
	}
	
//	class hasHouseBroadcastReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			hasHouse = intent.getBooleanExtra(XFJRConstant.KEY_HAS_HOUSE_STATUS, false);
//			
//		}
//	}

	/**
	 * 空白处点击事件
	 * 
	 * @param view
	 */
	@OnClick(R.id.lltBlank)
	private void clickBlank(View view) {
		XFJRUtil.hideSoftInput(view);
	}

}