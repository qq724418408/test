package com.bocop.xfjr.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.location.BDLocation;
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
import com.bocop.xfjr.bean.SystemBasicInfo.IndustryListBean;
import com.bocop.xfjr.bean.SystemBasicInfo.JobLevelListBean;
import com.bocop.xfjr.bean.SystemBasicInfo.PayMethodsListBean;
import com.bocop.xfjr.bean.pretrial.PretrialParamBean;
import com.bocop.xfjr.bean.pretrial.PretrialResultBean;
import com.bocop.xfjr.callback.CommunicationCallBack;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.constant.XFJRConstant;
import com.bocop.xfjr.util.BDLocationUtils;
import com.bocop.xfjr.util.BDLocationUtils.OnLocationListener;
import com.bocop.xfjr.util.BitmapUtils;
import com.bocop.xfjr.util.LimitInputTextWatcher;
import com.bocop.xfjr.util.PatternUtils;
import com.bocop.xfjr.util.ScreenUtils;
import com.bocop.xfjr.util.TextUtil;
import com.bocop.xfjr.util.XFJRUtil;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil.ChildCityListSelectDialogClick;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil.CityListSelectDialogClick;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil.IndustryListSelectDialogClick;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil.JobLevelListSelectDialogClick;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil.PayMethodsListSelectDialogClick;
import com.bocop.xfjr.util.dialog.XFJRDialogUtil.StringSelectDialogClick;
import com.bocop.xfjr.util.dialog.XfjrDialog;
import com.bocop.xfjr.util.file.FileUtil;
import com.bocop.xfjr.util.file.SystemBasicJSONWRUtil;
import com.bocop.xfjr.util.image.ImageLoad;
import com.bocop.xfjr.util.json.PretrialJSONParam;
import com.bocop.xfjr.view.RoundImageView;
import com.bocop.xfjr.view.XFJRDecimalClearEditText;
import com.bocop.xfjr.view.XFJRMoneyClearEditText;
import com.bocop.yfx.utils.ToastUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import jni.HuffmanCompress;

public class PretrialBasicInfoFragment extends BaseCheckProcessFragment {

	// 相机
	private static final int CAMERA_CODE = 8;
	@ViewInject(R.id.lltBasicInfo)
	private View lltBasicInfo; // 基本信息
	@ViewInject(R.id.lltContactsInfo)
	private View lltContactsInfo; // 联系人信息
	@ViewInject(R.id.btnLeft)
	private Button btnLeft;
	@ViewInject(R.id.btnRight)
	private Button btnRight;
	@ViewInject(R.id.scrollView)
	private View scrollView;
	@ViewInject(R.id.lltChoiceHomeChildArea)
	private View lltChoiceHomeChildArea;
	@ViewInject(R.id.tvEducation)
	private TextView tvEducation;// 学历
	@ViewInject(R.id.tvCustomWorkingType)
	private TextView tvCustomWorkingType;// 行业类别
	@ViewInject(R.id.edHouseAddr)
	private EditText edHouseAddr;// 家庭地址
	@ViewInject(R.id.edCustomCompany)
	private EditText edCustomCompany;// 单位名称
	@ViewInject(R.id.etCustomCompanyAddress)
	private EditText etCustomCompanyAddress;// 单位地址
	@ViewInject(R.id.tvChoiceJobGrade)
	private TextView tvChoiceJobGrade;// 选择职位等级
	@ViewInject(R.id.etWorkYear)
	private XFJRDecimalClearEditText etWorkYear;
	@ViewInject(R.id.etMonthIncome)
	private XFJRMoneyClearEditText etMonthIncome;
	@ViewInject(R.id.tvChoiceHomeArea)
	private TextView tvChoiceHomeArea;// 选择地区
	@ViewInject(R.id.tvChoiceHomeChildArea)
	private TextView tvChoiceHomeChildArea;// 选择地区
	@ViewInject(R.id.tvChoiceMaritalStatus)
	private TextView tvChoiceMaritalStatus;// 选择婚姻状态
	@ViewInject(R.id.tvChoicePay)
	private TextView tvChoicePay;// 选择支付方式
	@ViewInject(R.id.tvTime)
	private TextView tvTime;// 
	@ViewInject(R.id.tvAddr)
	private TextView tvAddr;// 
	@ViewInject(R.id.ivGroupPhotoInfo)
	private RoundImageView ivGroupPhotoInfo;
	@ViewInject(R.id.ivDelete)
	private ImageView ivDelete;
	@ViewInject(R.id.ivRefreshLocation)
	private ImageView ivRefreshLocation;
	@ViewInject(R.id.fltLocationSucess)
	private View fltLocationSucess;
	@ViewInject(R.id.lltLocationFail)
	private View lltLocationFail;
	@ViewInject(R.id.etContactsOneName)
	private EditText etContactsOneName;
	@ViewInject(R.id.etContactsOnePhone)
	private EditText etContactsOnePhone;
	@ViewInject(R.id.etContactsTwoName)
	private EditText etContactsTwoName;
	@ViewInject(R.id.etContactsTwoPhone)
	private EditText etContactsTwoPhone;
	@ViewInject(R.id.rgHasHouse)
	private RadioGroup rgHasHouse;
//	private Dialog mScDialog;// 选择照片弹窗
	private String path;
	private String education; // 学历
	private String customWorkingType; // 行业类别
	private String fxModel;
	private String photoPosition; //合影地址
	private String photoTime; //合影时间yyyy-MM-dd HH:mm:ss
	private boolean isMarried;
	private boolean isLocationSuccess = false;
	private CommunicationCallBack communicationCallBack;
	public static PretrialParamBean pretrialParamBean;
	public static boolean hasHouse = false;
	public static File photoFile = null; // 照片
	private Uri temUri;
	private List<ChildCityBean> childCityList = new ArrayList<>();
//	private LocationManager locationManager;
//	private String provider;
	private XfjrDialog loadingDialog;
	private int mTimer = 30;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		
		public void handleMessage(Message msg) {
			if (null != msg) {
				switch (msg.what) {
				case 61:
				case 161:
					String addr = (String) msg.obj;
					isLocationSuccess = true;
					photoPosition = addr;
					onResult();
					break;

				default:
					onNoLocationResult();
					LogUtils.e("未能检测到位置信息");
					break;
				}
			}
		}
	};
	
	private void onNoLocationResult() {
		isLocationSuccess = false;
		ToastUtils.show(getActivity(), "未能检测到位置信息,请重新拍照", 0);
		hideLocation();
		loadingDialog.cancel();
	}
	
	// 开启倒计时
//	private void startCutTime() {
//		mTimer--;
//		LogUtils.e("倒计时："+ mTimer);
//		handler.sendEmptyMessageDelayed(10086, 1000);
//	}
	
	@Override
	protected void initView() {
		super.initView();
		if (null == loadingDialog) {
			loadingDialog = XFJRDialogUtil.loadingDialog(getActivity(), "请稍等，正在生成合影图片");
		}
		setVisibility(tvReset, View.VISIBLE);
		//setVisibility(tvSave, View.VISIBLE);
		etMonthIncome.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				etMonthIncome.onFocusChange(v, hasFocus); // 不调这个方法就不会显示delete图标
				if (!hasFocus) {
					TextUtil.suffix00(etMonthIncome);
				}
			}

		});
		rgHasHouse.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rbHasHouse:
					hasHouse = true;
					pretrialParamBean.setHasHouse("Y");
					break;

				case R.id.rbNoHasHouse:
					hasHouse = false;
					pretrialParamBean.setHasHouse("N");
					break;
				}
			}
		});
	}

	@Override
	protected boolean resetClick(View view) {
		reset();
		return false;
	}

	private void reset() {
		education = null;
		customWorkingType = null;
		photoFile = null;
		//startLocation();
		hideLocation();
		unSelected(tvEducation, null);
		unSelected(tvCustomWorkingType, null);
		unSelected(tvChoiceJobGrade, null);
		unSelected(tvChoiceHomeArea, null);
		unSelected(tvChoicePay, null);
		unSelected(tvChoiceMaritalStatus, null);
		tvEducation.setText("");
		tvCustomWorkingType.setText("");
		tvChoiceJobGrade.setText("");
		etWorkYear.setText("");
		etMonthIncome.setText("");
		tvChoiceHomeArea.setText("");
		tvChoicePay.setText("");
		tvChoiceMaritalStatus.setText("");
		edCustomCompany.setText("");
		etCustomCompanyAddress.setText("");
		edHouseAddr.setText("");
		etContactsOneName.setText("");
		etContactsOnePhone.setText("");
		etContactsTwoName.setText("");
		etContactsTwoPhone.setText("");
		edCustomCompany.clearFocus();
		etWorkYear.clearFocus();
		etCustomCompanyAddress.clearFocus();
		edHouseAddr.clearFocus();
		etMonthIncome.clearFocus();
		etContactsOneName.clearFocus();
		etContactsOnePhone.clearFocus();
		etContactsTwoName.clearFocus();
		etContactsTwoPhone.clearFocus();
		ivDelete.setVisibility(View.GONE);
		lltChoiceHomeChildArea.setVisibility(View.GONE);
		ivGroupPhotoInfo.setImageResource(R.drawable.xfjr_pretrial_photo);
	}

	@Override
	public View onCreateView(LayoutInflater i, ViewGroup c, Bundle b) {
		view = i.inflate(R.layout.xfjr_fragment_pretrial_basic_info, c, false);
		return view;
	}

	@Override
	protected void initData() {
		super.initData();
		initListener();
//		startLocation();
//		locationManager = (LocationManager) getActivity()
//				.getSystemService(Context.LOCATION_SERVICE);
		try {
			fxModel = XfjrMainActivity.productBean.getFxModel().trim();
			//List<CustTypeBean> custTypeList = ((XfjrMainActivity) getActivity()).productBean.getCustType();
		} catch (Exception e) {
			fxModel = "ALL";
		}
		LogUtils.e("old fxModel--->" + fxModel);
		fxModel = XFJRUtil.getFxModel(fxModel);
		LogUtils.e("new fxModel--->" + fxModel);
		if (fxModel.equals("3")) {
			setBtnText("", "提交");
		} else {
			setBtnText("上一步", "下一步");
		}
		pretrialParamBean = new PretrialParamBean();
		Fragment fragment = getParentFragment();
		if (fragment instanceof CommunicationCallBack) {
			communicationCallBack = (CommunicationCallBack) fragment;
		}
		// rbUnMarried.setChecked(true);
		path = Environment.getExternalStorageDirectory() + File.separator + "image" + File.separator
				+ "temp";
//		mScDialog = new XfjrDialog.Builder(getActivity())
//				.setContentView(R.layout.xfjr_dialog_sc_picture).setWidthAndHeight(-1, -2)
//				.setOnClickListener(R.id.picture, this).setOnClickListener(R.id.camera, this)
//				.setOnClickListener(R.id.cancle, this).formBottom(true).create();
	}

	private void initListener() {
		etContactsOneName.addTextChangedListener(new LimitInputTextWatcher(etContactsOneName,
				LimitInputTextWatcher.a_zA_Z_CN_REGEX)); // 限制只能输入中文和英文
		etContactsTwoName.addTextChangedListener(new LimitInputTextWatcher(etContactsTwoName,
				LimitInputTextWatcher.a_zA_Z_CN_REGEX)); // 限制只能输入中文和英文
	}

	@OnClick(R.id.tvEducation)
	private void clickChooseEducation(View view) {
		final List<String> educationList = new ArrayList<>();
		educationList.addAll(Arrays
				.asList(getActivity().getResources().getStringArray(R.array.selected_education)));
		XFJRDialogUtil.alertStringSelectDialog(getActivity(), "", educationList,
				new StringSelectDialogClick() {

					@Override
					public void onClick(View view, XfjrDialog dialog) {
						CheckedTextView ctvContent = (CheckedTextView) view;
						int position = (int) ctvContent.getTag();
						if (!ctvContent.isChecked()) {
							ctvContent.setChecked(true);
							dialog.cancel();
							education = educationList.get(position);
							pretrialParamBean.setEducation(education);
							selected(tvEducation, education);
						}

					}
				});
	}

	@OnClick(R.id.tvCustomWorkingType)
	private void clickChooseCustomWorkingType(View view) {
		final List<IndustryListBean> list = new ArrayList<>();
		if (XfjrMain.isNet) {
			list.clear();
			SystemBasicInfo info = SystemBasicJSONWRUtil.readSystemBasicInfo(getActivity());
			if (null != info) {
				if (null != info.getIndustryList()) {
					list.addAll(info.getIndustryList());
				}
			}
		} else {
			list.clear();
			list.add(new IndustryListBean("1", "金融"));
			list.add(new IndustryListBean("2", "教育"));
		}
		XFJRDialogUtil.showIndustryListDialog(getActivity(), "", list,
				new IndustryListSelectDialogClick() {

					@Override
					public void onClick(View view, XfjrDialog dialog, IndustryListBean bean) {
						CheckedTextView ctvContent = (CheckedTextView) view;
						if (!ctvContent.isChecked()) {
							ctvContent.setChecked(true);
							dialog.cancel();
							customWorkingType = bean.getUnitName();
							pretrialParamBean.setUnitId(bean.getUnitId());
							selected(tvCustomWorkingType, customWorkingType);
						}
					}

				});
	}

	@OnClick(R.id.tvChoiceJobGrade)
	private void clickChoiceJobGrade(View view) {
		final List<JobLevelListBean> list = new ArrayList<>();
		if (XfjrMain.isNet) {
			list.clear();
			SystemBasicInfo info = SystemBasicJSONWRUtil.readSystemBasicInfo(getActivity());
			if (null != info) {
				if (null != info.getJobLevelList()) {
					list.addAll(info.getJobLevelList());
				}
			}
		} else {
			list.clear();
			list.add(new JobLevelListBean("1", "职位1"));
			list.add(new JobLevelListBean("2", "职位2"));
		}
		XFJRDialogUtil.showJobLevelListDialog(getActivity(), "", list,
				new JobLevelListSelectDialogClick() {

					@Override
					public void onClick(View view, XfjrDialog dialog, JobLevelListBean bean) {
						CheckedTextView ctvContent = (CheckedTextView) view;
						if (!ctvContent.isChecked()) {
							ctvContent.setChecked(true);
							dialog.cancel();
							pretrialParamBean.setJobId(bean.getJobId());
							selected(tvChoiceJobGrade, bean.getJobName());
						}

					}
				});
	}

	@OnClick(R.id.tvChoiceHomeArea)
	private void clickChoiceHomeArea(View view) {
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
		XFJRDialogUtil.showCityListListDialog(getActivity(), "", cityList,
				new CityListSelectDialogClick() {

					@Override
					public void onClick(View view, XfjrDialog dialog, CityListBean bean) {
						CheckedTextView ctvContent = (CheckedTextView) view;
						if (!ctvContent.isChecked()) {
							ctvContent.setChecked(true);
							tvChoiceHomeArea.setTag(bean);
							if (null != bean.getChildCity() && bean.getChildCity().size() > 0) {
								// 显示二级城市列表
								lltChoiceHomeChildArea.setVisibility(View.VISIBLE);
								childCityList.clear();
								childCityList.addAll(bean.getChildCity());
								selected(tvChoiceHomeChildArea, childCityList.get(0).getCityName());
								pretrialParamBean.setCityId(childCityList.get(0).getCityId());
							} else { // 没有二级城市
								lltChoiceHomeChildArea.setVisibility(View.GONE);
								pretrialParamBean.setCityId(bean.getCityId());
							}
							selected(tvChoiceHomeArea, bean.getCityName());
							dialog.cancel();
						}
					}
				});
	}

	@OnClick(R.id.tvChoiceHomeChildArea)
	private void clickChoiceHomeChildArea(View view) {
		XFJRDialogUtil.showChildCityListDialog(getActivity(), "", childCityList,
				new ChildCityListSelectDialogClick() {

					@Override
					public void onClick(View view, XfjrDialog dialog, ChildCityBean bean) {
						CheckedTextView ctvContent = (CheckedTextView) view;
						if (!ctvContent.isChecked()) {
							ctvContent.setChecked(true);
							pretrialParamBean.setCityId(bean.getCityId());
							selected(tvChoiceHomeChildArea, bean.getCityName());
							dialog.cancel();
						}
					}
				});
	}

	@OnClick(R.id.tvChoicePay)
	private void clickChoicePay(View view) {
		final List<PayMethodsListBean> list = new ArrayList<>();
		if (XfjrMain.isNet) {
			list.clear();
			SystemBasicInfo info = SystemBasicJSONWRUtil.readSystemBasicInfo(getActivity());
			if (null != info) {
				if (null != info.getPayMethodsList()) {
					list.addAll(info.getPayMethodsList());
				}
			}
		} else {
			list.clear();
			list.add(new PayMethodsListBean("1", "首付"));
			list.add(new PayMethodsListBean("2", "全额"));
		}
		XFJRDialogUtil.showPayMethodsListDialog(getActivity(), "", list,
				new PayMethodsListSelectDialogClick() {

					@Override
					public void onClick(View view, XfjrDialog dialog, PayMethodsListBean bean) {

						CheckedTextView ctvContent = (CheckedTextView) view;
						if (!ctvContent.isChecked()) {
							ctvContent.setChecked(true);
							dialog.cancel();
							pretrialParamBean.setPayType(bean.getPayType());
							selected(tvChoicePay, bean.getPayName());
						}
					}
				});
	}

	@OnClick(R.id.tvChoiceMaritalStatus)
	private void clickChoiceMaritalStatus(View view) {
		final List<String> list = new ArrayList<>();
		list.addAll(Arrays.asList(
				getActivity().getResources().getStringArray(R.array.selected_married_status)));
		XFJRDialogUtil.alertStringSelectDialog(getActivity(), "", list,
				new StringSelectDialogClick() {

					@Override
					public void onClick(View view, XfjrDialog dialog) {
						CheckedTextView ctvContent = (CheckedTextView) view;
						int position = (int) ctvContent.getTag();
						if (!ctvContent.isChecked()) {
							ctvContent.setChecked(true);
							dialog.cancel();
							switch (position) {
							case 0: // 未婚
								//isMarriedStatusChage = !"3".equals(pretrialParamBean.getMarried());
								pretrialParamBean.setMarried("3");
								isMarried = false;
								break;
							case 1: // 已婚
								//isMarriedStatusChage = !"1".equals(pretrialParamBean.getMarried());
								pretrialParamBean.setMarried("1");
								isMarried = true;
								break;
							case 2: // 离异
								//isMarriedStatusChage = !"2".equals(pretrialParamBean.getMarried());
								pretrialParamBean.setMarried("2");
								isMarried = false;
								break;
							}
							selected(tvChoiceMaritalStatus, list.get(position));
						}
					}
				});
	}

	@OnClick(R.id.btnRight)
	@CheckNet
	@Duplicate("NewBusinessFragment.java")
	private void clickNext(View view) {
		if (XfjrMain.isNet) {
			if (checkEmpty()) {
				return;
			}
		}
		LogUtils.e("fxModel--->" + fxModel);
		if (fxModel.equals("3")) {
			submitPretrialInfo();
		} else {
			if (communicationCallBack != null) {
				Map<String, Object> map = new HashMap<>();
				map.put(XFJRConstant.KEY_MARRIED_STATUS, isMarried);
//				if ("ALL".equals(fxModel) && isHasHouseStatusChange) {
//					Intent intent = new Intent(XFJRConstant.ACTION_HAS_HOUSE_STATUS_CHANGE);
//					intent.putExtra(XFJRConstant.KEY_HAS_HOUSE_STATUS, hasHouse);
//					getActivity().sendBroadcast(intent);
//				}
				communicationCallBack.nextCallBack(map, fxModel);
			}
		}
	}

	@OnClick(R.id.btnLeft)
	private void clickStep(View view) {
		if (communicationCallBack != null) {
			communicationCallBack.backCallBack();
		}
	}

	@OnClick(R.id.ivRefreshLocation)
	private void clickRefreshLocation(View view) {
//		startLocation();
	}

	@OnClick(R.id.ivDelete)
	private void clickDeletePhoto(View view) {
		photoFile = null;
		hideLocation();
		//startLocation();
		ivDelete.setVisibility(View.GONE);
		ivGroupPhotoInfo.setImageResource(R.drawable.xfjr_pretrial_photo);
	}

	@OnClick(R.id.ivGroupPhotoInfo)
	private void OnClickGroupPhoto(View view) {
		if (ivDelete.getVisibility() == View.GONE) {
			if (XFJRUtil.cameraIsCanUse()) {
				temUri = getTempUri();
				startCamera();
			} else {
				ToastUtils.show(getActivity(), getString(R.string.are_you_sure_camera_can_use), 0);
			}
		} else {
			FileUtil.preview(photoFile.getPath(), getActivity());
			// ToastUtils.show(getActivity(), "预览图片", 0);
		}
	}

	protected void setBtnText(String left, String right) {
		btnLeft.setText(left);
		btnLeft.setVisibility(View.GONE);
		btnRight.setText(right);
	}

	private boolean checkEmpty() {
		etMonthIncome.clearFocus();
		String phone = XfjrMainActivity.productBean.getTelephone();
		if (TextUtils.isEmpty(education)) {// 学历
			ToastUtils.show(getActivity(), getString(R.string.please_select_education), 0);
			return true;
		}
		if (TextUtils.isEmpty(edCustomCompany.getText())) {// 单位名称
			ToastUtils.show(getActivity(), getString(R.string.please_input_work_name), 0);
			return true;
		}
		if (TextUtils.isEmpty(etCustomCompanyAddress.getText())) {// 单位名称
			ToastUtils.show(getActivity(), getString(R.string.please_input_work_address), 0);
			return true;
		}
		if (TextUtils.isEmpty(customWorkingType)) {// 行业类型
			ToastUtils.show(getActivity(), getString(R.string.please_select_industry_type), 0);
			return true;
		}
		CharSequence jobGrade = tvChoiceJobGrade.getText();
		if (TextUtils.isEmpty(jobGrade)) {// 职位等级
			ToastUtils.show(getActivity(), getString(R.string.please_select_job_grade), 0);
			return true;
		}
		CharSequence workYear = etWorkYear.getText();
		if (TextUtils.isEmpty(workYear)) {// 
			ToastUtils.show(getActivity(), getString(R.string.please_input_now_work_life), 0);
			return true;
		}
		CharSequence monthIncome = etMonthIncome.getText();
		if (TextUtils.isEmpty(monthIncome)) {// 
			ToastUtils.show(getActivity(), getString(R.string.please_input_monthly_income), 0);
			return true;
		}
		CharSequence homeArea = tvChoiceHomeArea.getText();
		CharSequence homeChildArea = tvChoiceHomeChildArea.getText();
		if (TextUtils.isEmpty(homeArea)) {// 居住区域
			ToastUtils.show(getActivity(), getString(R.string.please_select_home_area), 0);
			return true;
		}
		CityListBean bean = (CityListBean) tvChoiceHomeArea.getTag();
		if (null != bean.getChildCity() && bean.getChildCity().size() > 0) {
			if (TextUtils.isEmpty(homeChildArea)) {// 居住区域
				ToastUtils.show(getActivity(), getString(R.string.please_select_home_area), 0);
				return true;
			}
		}
		if (TextUtils.isEmpty(edHouseAddr.getText())) {// 详细地址
			ToastUtils.show(getActivity(), getString(R.string.please_input_adress), 0);
			return true;
		}
		if (TextUtils.isEmpty(tvChoicePay.getText())) {// 支付方式
			ToastUtils.show(getActivity(), getString(R.string.please_select_pay_mode), 0);
			return true;
		}
		if (TextUtils.isEmpty(tvChoiceMaritalStatus.getText())) {// 婚姻状况
			ToastUtils.show(getActivity(), getString(R.string.please_select_marital_status), 0);
			return true;
		}
		if (TextUtils.isEmpty(etContactsOneName.getText())) {// 联系人姓名1
			ToastUtils.show(getActivity(), getString(R.string.please_input_contact1_name), 0);
			return true;
		}
		if (TextUtils.isEmpty(etContactsOnePhone.getText())) {// 联系人手机号1
			ToastUtils.show(getActivity(), getString(R.string.please_input_contact1_phone), 0);
			return true;
		} else if (!PatternUtils.isMobile(etContactsOnePhone.getText().toString().trim())) {
			ToastUtils.show(getActivity(), getString(R.string.phone_number_format_is_incorrect), 0);
			return true;
		} else if (phone.equals(etContactsOnePhone.getText().toString().trim())) {
			ToastUtils.show(getActivity(),
					getString(R.string.contact1_phone_cannot_same_as_apply_phone), 0);
			return true;
		}
		if (TextUtils.isEmpty(etContactsTwoName.getText())) {// 联系人姓名2
			ToastUtils.show(getActivity(), getString(R.string.please_input_contact2_name), 0);
			return true;
		}
		if (TextUtils.isEmpty(etContactsTwoPhone.getText())) {// 联系人手机号2
			ToastUtils.show(getActivity(), getString(R.string.please_input_contact2_phone), 0);
			return true;
		} else if (!PatternUtils.isMobile(etContactsTwoPhone.getText().toString().trim())) {
			ToastUtils.show(getActivity(), getString(R.string.phone_number_format_is_incorrect), 0);
			return true;
		} else if (phone.equals(etContactsTwoPhone.getText().toString().trim())) {
			ToastUtils.show(getActivity(),
					getString(R.string.contact2_phone_cannot_same_as_apply_phone), 0);
			return true;
		} else if (etContactsTwoPhone.getText().toString().trim()
				.equals(etContactsOnePhone.getText().toString().trim())) {
			ToastUtils.show(getActivity(),
					getString(R.string.contact2_phone_cannot_same_as_contact2_phone), 0);
			return true;
		}
		pretrialParamBean.setComName(edCustomCompany.getText().toString().trim());
		pretrialParamBean.setComAddress(etCustomCompanyAddress.getText().toString().trim());
		pretrialParamBean.setLiveAddress(edHouseAddr.getText().toString().trim());
		pretrialParamBean.setWorkYear(etWorkYear.getTextString());
		pretrialParamBean.setMonthIncome(etMonthIncome.getTextString());
		pretrialParamBean.setLinkMan1(etContactsOneName.getText().toString().trim());
		pretrialParamBean.setLinkMobile1(etContactsOnePhone.getText().toString().trim());
		pretrialParamBean.setLinkMan2(etContactsTwoName.getText().toString().trim());
		pretrialParamBean.setLinkMobile2(etContactsTwoPhone.getText().toString().trim());
		pretrialParamBean.setPhotoPosition(tvAddr.getText().toString());
		pretrialParamBean.setPhotoTime(tvTime.getText().toString());
		if (ivDelete.getVisibility() == View.GONE) {
			ToastUtils.show(getActivity(), getString(R.string.please_upload_applicant_photo), 0);
			return true;
		}
		return false;
	}

	protected void startLocation() {
		LogUtils.e("startLocation--->loadingDialog.show()");
		loadingDialog.show();
		mTimer = 30;
		BDLocationUtils.get().onCreate(getActivity());
		BDLocationUtils.get().startLocation(new OnLocationListener() {

			@Override
			public void onLocation(int errorCode, String addr, String country, String province,
					String city, String district, String street, BDLocation location) {
				LogUtils.e("errorCode-->" + errorCode);
				LogUtils.e("addr---->" + addr);
				LogUtils.e("country---->" + country);
				LogUtils.e("province---->" + province);
				LogUtils.e("city---->" + city);
				LogUtils.e("district---->" + district);
				LogUtils.e("street---->" + street);
				LogUtils.e("---->" + country + province + city + district + street);
				if(TextUtils.isEmpty(city)){
					mTimer--;
					LogUtils.e("倒计时----->" + mTimer);
					if (mTimer == 0) {
						onNoLocationResult();
						LogUtils.e("超时未能检测到位置信息");
					}
				} else {
					Message message = handler.obtainMessage();
					message.what = errorCode;
					message.obj = addr;
					handler.sendMessage(message);
				}
//				if (errorCode == 61 || errorCode == 161) {
//					//isLocationSuccess = true;
//					//photoPosition = addr;
//				} else {
//					isLocationSuccess = false;
//				}
			}
		});
	}

	/**
	 * 位置信息
	 */
	protected void showLocation() {
		if (isLocationSuccess) {
			fltLocationSucess.setVisibility(View.VISIBLE);
			lltLocationFail.setVisibility(View.GONE);
		} else {
			fltLocationSucess.setVisibility(View.GONE);
			//lltLocationFail.setVisibility(View.VISIBLE);
		}
	}

	protected void hideLocation() {
		photoPosition = null;
		BDLocationUtils.get().stop();
		lltLocationFail.setVisibility(View.GONE);
		fltLocationSucess.setVisibility(View.GONE);
	}

	/**
	 * 下滑选择框
	 * 
	 * @param selected
	 * @param list
	 * @param dialogClick
	 */
	@SuppressLint("NewApi")
	private void alertSelectDialog(final String selected, List<String> list,
			final StringSelectDialogClick dialogClick) {
		LogUtils.e("集合：" + list.toString());
		XFJRDialogUtil.alertStringSelectDialog(getActivity(), selected, list, dialogClick);
	}

	/**
	 * 结果回调
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtils.e("onActivityResult:" + resultCode);
		if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_CODE) {
			startLocation(); // 请求定位
//			startCutTime(); // 开始倒计时
			//onResult();
		}
	}

	/**
	 * 成功结果处理
	 * 
	 * @param requestCode
	 * @param data
	 */
	@SuppressWarnings("deprecation")
	private void onResult() {
		showLocation();
		// 获取图片的选转角度
		int degree = BitmapUtils.readPictureDegree(path);
		// 获取旋转后的图片
		Bitmap bitmap = BitmapUtils.rotaingImage(degree, path);
//		ivGroupPhotoInfo.setImageBitmap(bitmap);
		photoTime = XFJRUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		Bitmap bitmap2 = drawText(bitmap, photoTime, photoPosition);
		if ((android.os.Build.CPU_ABI + android.os.Build.CPU_ABI2).contains("armeabi")) {
			HuffmanCompress.compress(bitmap2, 28, temUri.getPath());
			photoFile = new File(temUri.getPath());
		} else {
			photoFile = BitmapUtils.bitmap2File(bitmap2, temUri.getPath()); 
		}
//		bitmap = BitmapFactory.decodeFile(path);
		bitmap2.recycle();
		ImageLoad.loadImage(getActivity(), photoFile.getPath(), ivGroupPhotoInfo);
		ivDelete.setVisibility(View.VISIBLE);
		tvTime.setText(photoTime);
		tvAddr.setText(photoPosition);
		loadingDialog.cancel();
		BDLocationUtils.get().onDestory();
	}

	/**
	 * 
	 * @param source
	 * @param waterMarkText
	 * @return
	 */
	private Bitmap drawText(Bitmap source, String... waterMarkText) {
		
		if (source == null)
			return null;
		int radius = (int) (Resources.getSystem().getDisplayMetrics().density * 10 + .5f);
		int delta = (int) (Resources.getSystem().getDisplayMetrics().density * 2 + .5f);
//		int textWidth = 0;
//		int textHeight = 0;
		TextPaint textPaint = new TextPaint();
		boolean drawText = waterMarkText != null && waterMarkText.length > 0;
		Rect bounds = new Rect();
		if (drawText) {
			int textSize = ScreenUtils.sp2px(getActivity(), 14);
			LogUtils.e("----->" + textSize);
			textPaint.setTextSize(textSize);
			textPaint.setColor(Color.WHITE);
			textPaint.setAntiAlias(true);
			textPaint.getTextBounds(waterMarkText[0], 0, waterMarkText[0].length(), bounds);
//			textHeight = bounds.height() + 2 * delta;
//			textWidth = bounds.width();
		}
		Bitmap result = Bitmap.createBitmap(source.getWidth(), source.getHeight(),
				Bitmap.Config.ARGB_8888);	
		Canvas canvas = new Canvas(result);
		Paint paint = new Paint();
		paint.setShader(
				new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
		paint.setAntiAlias(true);
		Rect rectF = new Rect(0, 0, source.getWidth(), source.getHeight());
		canvas.drawRect(rectF, paint);
		if (drawText) {
			paint = new Paint();
			paint.setColor(Color.parseColor("#50f2f2f2"));
			bounds.top = canvas.getHeight() - bounds.height() - radius;
			bounds.bottom = canvas.getHeight() - radius + delta;
			int width = bounds.width();
			bounds.left = radius - delta;
			bounds.right = bounds.left + width + delta * 2;
			//canvas.drawRect(bounds, paint);
//			int offsetX = (canvas.getWidth() - textWidth) / 2;
//			int offsetY = canvas.getHeight() - delta;
			for (int i = waterMarkText.length - 1; i >= 0; --i) {
				int y = (int) (canvas.getHeight() - radius
						- (bounds.height() * 1.5) * (waterMarkText.length - 1 - i));
				canvas.drawText(waterMarkText[i], radius, y, textPaint);
			}
		}
		return result;
	}

	/**
	 * 跳相机
	 */
	private void startCamera() {
		//startLocation(); // 请求定位
		// 判断sdcard是否存在
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				// qwe为相片保存的文件夹名
				File dir = new File(
						Environment.getExternalStorageDirectory() + File.separator + "image");
				if (!dir.exists()) {
					dir.mkdirs();
				}
				Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				// asd为保存的相片名
				File f = new File(dir, "temp");// localTempImgDir和localTempImageFileName是自己定义的名字
				Uri u = Uri.fromFile(f);
				try {// 尽可能调用系统相机  
					String cameraPackageName = getCameraPhoneAppInfos(getActivity());
					if (cameraPackageName == null) {
						cameraPackageName = "com.android.camera";
					}
					final Intent intent_camera = getActivity().getPackageManager()
							.getLaunchIntentForPackage(cameraPackageName);
					if (intent_camera != null) {
						intent.setPackage(cameraPackageName);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
				ivGroupPhotoInfo.setEnabled(false);
				startActivityForResult(intent, CAMERA_CODE);
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
				ivGroupPhotoInfo.setEnabled(true);
			}
		}
	}

	// 对使用系统拍照的处理    
	public String getCameraPhoneAppInfos(Activity context) {
		try {
			String strCamera = "";
			List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
			for (int i = 0; i < packages.size(); i++) {
				try {
					PackageInfo packageInfo = packages.get(i);
					String strLabel = packageInfo.applicationInfo
							.loadLabel(context.getPackageManager()).toString();
					// 一般手机系统中拍照软件的名字  
					if ("相机,照相机,照相,拍照,摄像,美图,Camera,camera".contains(strLabel)) {
						strCamera = packageInfo.packageName;
						if ((packageInfo.applicationInfo.flags
								& ApplicationInfo.FLAG_SYSTEM) != 0) {
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (strCamera != null) {
				return strCamera;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 裁剪后保存的uri
	 */
	private Uri getTempUri() {
		String uriPath = "file://" + File.separator + FileUtil.createSaveImagePath(getActivity());
		String fileName = File.separator + XFJRUtil.getCurrentTime() + ".jpg";
		LogUtils.e(uriPath + fileName);
		return Uri.parse(uriPath + fileName);
	}

	/**
	 * 空白处点击事件
	 * 
	 * @param view
	 */
	@OnClick(R.id.lltBlank)
	private void clickBlank(View view) {
		etMonthIncome.clearFocus();
		XFJRUtil.hideSoftInput(view);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ivGroupPhotoInfo.setEnabled(true);
	}
	
	private void submitPretrialInfo() {
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
								XfjrMain.businessStatus = "1";
								break;
							}
//							intent.putExtra(XFJRConstant.ACTION_FLAG_INT,
//									((XfjrMainActivity) getActivity()).productBean.getFrom());
//							getActivity().sendBroadcast(intent);
							if (communicationCallBack != null) {
								FileUtil.delAllImages(getActivity()); // 删除所有图片缓存
								photoFile = null;
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
			LogUtils.e("informationJson:" + informationJson);
			if (communicationCallBack != null) {
				photoFile = null;
				Map<String, Object> map = new HashMap<>();
				map.put(XFJRConstant.KEY_PRETRIAL_RESULT, true);
				map.put(XFJRConstant.KEY_CREDIT_LINE, "10000.00");
				communicationCallBack.nextCallBack(map, fxModel);
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (null != loadingDialog) {
			loadingDialog = null;
		}
		BDLocationUtils.get().onDestory();
	}

}