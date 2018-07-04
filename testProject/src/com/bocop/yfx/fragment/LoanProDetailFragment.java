package com.bocop.yfx.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.boc.jx.base.BaseFragment;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.DialogUtil;
import com.boc.jx.view.LoadingView;
import com.boc.jx.view.LoadingView.OnRetryListener;
import com.boc.jx.view.indicator.CirclePageIndicator;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.adapter.LoopViewPagerAdapter;
import com.bocop.jxplatform.bean.Advertisement;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.xms.bean.ConstHead;
import com.bocop.xms.utils.FormsUtil;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.yfx.activity.LoanMainActivity;
import com.bocop.yfx.activity.loanprodetail.PersonalInfoActivity;
import com.bocop.yfx.activity.loanprodetail.PreviewAuthentInfoActivity;
import com.bocop.yfx.activity.loanprodetail.RealNameAuthentActivity;
import com.bocop.yfx.bean.InfoStatusResponse;
import com.bocop.yfx.view.JustifyTextView;
import com.bocop.yfx.xml.CspXmlYfx001;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 产品详情
 *
 * @author rd
 */
public class LoanProDetailFragment extends BaseFragment {
	@ViewInject(R.id.reLayout)
	private RelativeLayout reLayout;
	@ViewInject(R.id.llCompleteInfo)
	private LinearLayout llCompleteInfo;// 完善信息框
	@ViewInject(R.id.loadingView)
	private LoadingView loadingView;
	@ViewInject(R.id.scrollView)
	private ScrollView scrollView;
	@ViewInject(R.id.btnApply)
	private Button btnApply;
	private PopupWindow stateDialog;// 客户认证状态窗
	private TextView tvIdentify;// 实名认证
	private TextView tvReplenishInfo;// 补录资料
	private Button btnIdentifyCom;// 实名认证完成标识
	private Button btnReplenishInfoCom;// 补录资料完成标识
	private View vBg;// 认证窗口背景

	private String valStatus; // 资料完成度
	/**
	 * 0 无进度
	 */
	private String STATU_NO = "0";
	/**
	 * 1实名认证已通过
	 */
	private String STATU_REALNAME_COM = "1";
	/**
	 * 2个人信息已通过
	 */
	private String STATU_PERINFO_COM = "2";

	/**
	 * 3处理中
	 */
	private String STATU_DEALING_COM = "3";
	/**
	 * 4已拒绝
	 */
	private String STATU_REFUSED = "4";
	/**
	 * 5已通过
	 */
	private String STATU_lOAN_PASS = "5";
	/**
	 * 6已冻结
	 */
	private String STATU_FREEZE = "6";

	private Dialog dialog;
	public static boolean STATUS_CHANGE_FLAG = false;
	private boolean DIALOG_FLAG = false;

	@ViewInject(R.id.reLayout1)
	private RelativeLayout reLayout1;
	@ViewInject(R.id.svNewUI)
	private ScrollView svNewUI;
	@ViewInject(R.id.JTXTitle)
	private JustifyTextView JTXTitle;
	@ViewInject(R.id.llGAZS)
	private LinearLayout llGAZS;
	@ViewInject(R.id.llFPT)
	private LinearLayout llFPT;
	@ViewInject(R.id.llFNT)
	private LinearLayout llFNT;
	@ViewInject(R.id.tvFptText)
	private TextView tvFptText;
	@ViewInject(R.id.tvFptSubText)
	private TextView tvFptSubText;
	@ViewInject(R.id.llBG)
	private LinearLayout llBG;
	@ViewInject(R.id.ivSteps)
	private ImageView ivSteps;
	@ViewInject(R.id.rlBG)
	private RelativeLayout rlBG;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		if (LoanMainActivity.PRO_FLAG == 0) {
//			view = initView(R.layout.yfx_fragment_loan_pro_detail);
//		} else {
			view = initView(R.layout.yfx_fragment_loan_pro_detail_fpt);
//		}
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		dialog = new AlertDialog.Builder(getActivity()).create();
		requestInfoStatus();// 请求资料完成度
		initText();
	}

	private void initText() {
		// LoanMainActivity.PRO_FLAG=1;
		switch (LoanMainActivity.PRO_FLAG) {
		case 0:// 关爱专属
//			JTXTitle.setText("“中银E贷·关爱专属”消费贷款是面向关爱项目客户的专属信贷服务，期限最长1年，额度最高30万元。");
//			reLayout1.setBackgroundResource(R.drawable.yfx_bg_gazs);
//			llGAZS.setVisibility(View.VISIBLE);
//			llFPT.setVisibility(View.GONE);
//			llFNT.setVisibility(View.GONE);
			setAdHeight(0);
			initAdList(0);
			notifyForAdPic();
			rlBG.setBackgroundResource(R.drawable.yfx_bg_gazs);
			tvFptText.setVisibility(View.GONE);
			tvFptSubText.setVisibility(View.GONE);
			tvFptText.setText("“扶贫通”是助推扶贫攻坚，面向贫困地区特邀客户的信贷服务，凡被邀客户可在线提交申请。");
			tvFptSubText.setText("贷款不得用于法律法规、监管规定、国家政策禁止银行贷款投入的项目、用途。");
			break;
		case 1:// 扶贫通
				// JTXTitle.setText("“扶贫通”是助推扶贫攻坚特设，面向贫困地区特邀客户的信贷服务。");
				// reLayout1.setBackgroundResource(R.drawable.yfx_bg_fpt);
				// llGAZS.setVisibility(View.GONE);
				// llFPT.setVisibility(View.VISIBLE);
				// llFNT.setVisibility(View.GONE);
			setAdHeight(1);
			initAdList(1);
			notifyForAdPic();
			rlBG.setBackgroundResource(R.drawable.yfx_bg_fpt);
			tvFptText.setVisibility(View.GONE);
			tvFptSubText.setVisibility(View.GONE);
			tvFptText.setText("“扶贫通”是助推扶贫攻坚，面向贫困地区特邀客户的信贷服务，凡被邀客户可在线提交申请。");
			tvFptSubText.setText("贷款不得用于法律法规、监管规定、国家政策禁止银行贷款投入的项目、用途。");
			break;
		case 2:// 扶农通
				// JTXTitle.setText("“扶农通”是面向农村地区客户的信贷服务。");
				// reLayout1.setBackgroundResource(R.drawable.yfx_bg_fnt);
				// llGAZS.setVisibility(View.GONE);
				// llFPT.setVisibility(View.GONE);
				// llFNT.setVisibility(View.VISIBLE);

			setAdHeight(2);
			initAdList(2);
			notifyForAdPic();
			rlBG.setBackgroundResource(R.drawable.yfx_bg_fnt);
			tvFptText.setVisibility(View.GONE);
			tvFptSubText.setVisibility(View.GONE);
			llFNT.setVisibility(View.GONE);
			tvFptText.setText("“扶农通”是面向农村地区客户的信贷服务，被邀请客户可在线申请提款。");
			tvFptSubText.setText("贷款不得用于法律法规、监管规定、国家政策禁止银行贷款投入的项目、用途。");
			break;

		case 3:// 公积贷
			setAdHeight(3);
			initAdList(3);
			notifyForAdPic();
			rlBG.setBackgroundResource(R.drawable.icon_gjtbackground);
			tvFptText.setVisibility(View.GONE);
			tvFptSubText.setVisibility(View.GONE);
			tvFptText.setText("公积金联名卡客户尊享，受邀客户均可在线申请，信用贷款无需抵押担保，随借随还实现按日计息");
			tvFptSubText.setText("贷款用于日常消费，不得用于经营以及法律法规、监管规定、国家政策禁止银行贷款投入的项目、用途");
			break;
		case 8:// 公积贷
			setAdHeight(8);
			initAdList(8);
			notifyForAdPic();
			rlBG.setBackgroundResource(R.drawable.yfx_bg_fpt);
			tvFptText.setVisibility(View.GONE);
			tvFptSubText.setVisibility(View.GONE);
			tvFptText.setText("公积金联名卡客户尊享，受邀客户均可在线申请，信用贷款无需抵押担保，随借随还实现按日计息");
			tvFptSubText.setText("贷款用于日常消费，不得用于经营以及法律法规、监管规定、国家政策禁止银行贷款投入的项目、用途");
			break;
		case 9:// 公积贷
			setAdHeight(9);
			initAdList(9);
			notifyForAdPic();
			rlBG.setBackgroundResource(R.drawable.yfx_bg_fpt);
			tvFptText.setVisibility(View.GONE);
			tvFptSubText.setVisibility(View.GONE);
			tvFptText.setText("");
			tvFptSubText.setText("");
			break;
		}
	}

	/**
	 * 请求资料完成度
	 */
	private void requestInfoStatus() {
		try {
			CspXmlYfx001 cspXmlYfx001 = new CspXmlYfx001(LoginUtil.getUserId(baseActivity));
			String strXml = cspXmlYfx001.getCspXml();
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			final byte[] byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(baseActivity);
			cspUtil.setFLAG_YFX_CSP(true);
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			cspUtil.setTxCode("001001");
			reLayout.setBackgroundColor(getResources().getColor(R.color.background_gray));
			cspUtil.postCspLogin(new String(byteMessage, "GBK"), new CallBack() {

				@Override
				public void onSuccess(String responStr) {
					dealSuccessMessage(responStr);// 处理成功信息
				}

				@Override
				public void onFailure(String responStr) {
					onGetDataFailure(responStr);
				}

				@Override
				public void onFinish() {
				}

			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void dealSuccessMessage(String responStr) {

		InfoStatusResponse infoStatusResponse = XStreamUtils.getFromXML(responStr, InfoStatusResponse.class);
		ConstHead constHead = infoStatusResponse.getConstHead();
		if (constHead != null) {
			if ("00".equals(constHead.getErrCode())) {
	
	//			if (LoanMainActivity.PRO_FLAG == 0) {
	//				// llCompleteInfo.setVisibility(View.VISIBLE);
	//				svNewUI.setVisibility(View.VISIBLE);
	//				reLayout.setBackgroundColor(baseActivity.getResources().getColor(R.color.white));
	//			}
				reLayout1.setVisibility(View.VISIBLE);
				btnApply.setVisibility(View.VISIBLE);
				btnApply.setText("下一步");
				loadingView.setVisibility(View.GONE);
	
				valStatus = infoStatusResponse.getInfoState().getValStatus();
				if (null != valStatus && !"".equals(valStatus)) {
					((LoanMainActivity) baseActivity).status = valStatus;
					// TODO LH
					if (BocSdkConfig.isTest) {
						((LoanMainActivity) baseActivity).status = "5";// 测试
					}
					if (STATU_NO.equals(valStatus)) {
						// 资料未完成时，显示提醒窗口
						if (!dialog.isShowing()) {
							showInfoCompleteDialog();
						}
	
					}
					// if (STATU_lOAN_PASS.equals(valStatus)) {
					// // 申请通过时，显示提醒窗口
					// DialogUtil.showWithTwoBtn(getActivity(),
					// "您的工薪贷申请已通过，要查看相关信息吗？", "是", "不",
					// new DialogInterface.OnClickListener() {
					//
					// @Override
					// public void onClick(DialogInterface dialog, int which) {
					//
					// baseActivity.callMe(PreviewAuthentInfoActivity.class);
					// }
					// }, new DialogInterface.OnClickListener() {
					//
					// @Override
					// public void onClick(DialogInterface dialog, int which) {
					// dialog.dismiss();
					// }
					// });
					// }
				}
			} else if ("50".equals(constHead.getErrCode())) {
				DialogUtil.showWithToMain(baseActivity, constHead.getErrMsg());
			} else {
				onGetDataFailure(constHead.getErrMsg());
			}
		}
	}

	private void onGetDataFailure(String err_msg) {
		CspUtil.onFailure(baseActivity, err_msg);
		if (LoanMainActivity.PRO_FLAG == 0) {
			llCompleteInfo.setVisibility(View.GONE);
			svNewUI.setVisibility(View.GONE);
		}
		reLayout1.setVisibility(View.GONE);
		btnApply.setVisibility(View.GONE);
		loadingView.setVisibility(View.VISIBLE);
		loadingView.setmOnRetryListener(new OnRetryListener() {

			@Override
			public void retry() {
				requestInfoStatus();
			}
		});
	}

	@OnClick({ R.id.llCompleteInfo, R.id.btnApply, })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llCompleteInfo:
			showStateDialog();// 显示客户认证状态窗口
			break;
		case R.id.btnApply:
			if (STATU_NO.equals(valStatus)) {
				baseActivity.callMe(RealNameAuthentActivity.class);
			} else if (STATU_REALNAME_COM.equals(valStatus)) {
				baseActivity.callMe(PersonalInfoActivity.class);
			} else if (STATU_PERINFO_COM.equals(valStatus)) {
				baseActivity.callMe(PreviewAuthentInfoActivity.class);
			} else if (STATU_DEALING_COM.equals(valStatus)) {
				Toast.makeText(baseActivity, R.string.applying, Toast.LENGTH_SHORT).show();
			} else if (STATU_REFUSED.equals(valStatus)) {
				baseActivity.callMe(PreviewAuthentInfoActivity.class);
			} else if (STATU_lOAN_PASS.equals(valStatus)) {
				DIALOG_FLAG = true;
				showInfoCompleteDialog();
			} else if (STATU_FREEZE.equals(valStatus)) {
				Toast.makeText(baseActivity, R.string.freeze, Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	/**
	 * 资料未完成提示窗口
	 */
	private void showInfoCompleteDialog() {

		dialog.show();
		dialog.getWindow().setContentView(R.layout.yfx_dialog_info_uncomplete_warn);
		Button btnNo = (Button) dialog.getWindow().findViewById(R.id.btNo);
		Button btnGoOn = (Button) dialog.getWindow().findViewById(R.id.btGoOn);
		TextView tvContent = (TextView) dialog.getWindow().findViewById(R.id.tvContent);

		if (DIALOG_FLAG) {
			btnNo.setText("重新审批");
			btnGoOn.setText("直接用款");
			tvContent.setText("是否需要重新审批额度");
		}
		btnNo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DIALOG_FLAG) {
					baseActivity.callMe(PreviewAuthentInfoActivity.class);
					dialog.dismiss();
				} else {
					dialog.dismiss();
				}
				DIALOG_FLAG = false;
			}
		});
		btnGoOn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (DIALOG_FLAG) {
					((LoanMainActivity) baseActivity).radioMyLoan.setChecked(true);
					dialog.dismiss();
				} else {
					if (STATU_NO.equals(valStatus)) {
						baseActivity.callMe(RealNameAuthentActivity.class);

					} else if (STATU_REALNAME_COM.equals(valStatus)) {
						baseActivity.callMe(PersonalInfoActivity.class);

					}
				}
				DIALOG_FLAG = false;
			}
		});

	}

	/**
	 * 客户认证状态窗口显示与隐藏
	 */
	private void showStateDialog() {
		showCompleteStatu();// 显示客户认证状态
		if (!stateDialog.isShowing()) {
			stateDialog.showAsDropDown(llCompleteInfo);

		} else {
			stateDialog.dismiss();

		}

	}

	/**
	 * 显示客户认证状态
	 */
	private void showCompleteStatu() {

		// 根据授权状态显示或隐藏勾选图标
		if (STATU_NO.equals(valStatus)) {
			tvIdentify.setVisibility(View.VISIBLE);// 实名认证
			tvReplenishInfo.setVisibility(View.VISIBLE);// 补录资料

			btnIdentifyCom.setVisibility(View.GONE);// 实名认证完成标识
			btnReplenishInfoCom.setVisibility(View.GONE);// 补录资料完成标识

			tvReplenishInfo.setClickable(false);
		} else if (STATU_REALNAME_COM.equals(valStatus)) {
			tvIdentify.setVisibility(View.GONE);
			tvReplenishInfo.setVisibility(View.VISIBLE);

			btnIdentifyCom.setVisibility(View.VISIBLE);
			btnReplenishInfoCom.setVisibility(View.GONE);

			tvReplenishInfo.setBackgroundResource(R.drawable.yfx_shape_stroke_green);
			tvReplenishInfo.setClickable(true);
		} else {
			tvIdentify.setVisibility(View.GONE);
			tvReplenishInfo.setVisibility(View.GONE);
			btnIdentifyCom.setVisibility(View.VISIBLE);
			btnReplenishInfoCom.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 初始化控件
	 */
	@Override
	protected void initView() {
		View root = LoanProDetailFragment.this.getLayoutInflater(null).inflate(R.layout.yfx_dialog_authentication_state,
				null);

		// 资料完成度窗口背景
		LinearLayout bgLayout = (LinearLayout) root.findViewById(R.id.llPopWBg);
		// 实名认证按钮
		tvIdentify = (TextView) root.findViewById(R.id.tvIdentify);
		// 补录资料按钮
		tvReplenishInfo = (TextView) root.findViewById(R.id.tvReplenishInfo);
		// 实名认证完成标识
		btnIdentifyCom = (Button) root.findViewById(R.id.btnIdentifyCom);
		// 补录资料完成标识
		btnReplenishInfoCom = (Button) root.findViewById(R.id.btnReplenishInfoCom);

		bgLayout.getBackground().setAlpha(80);
		vBg = root.findViewById(R.id.vBg);

		setDialogListener();// 认证状态窗口的监听函数
		FormsUtil.getDisplayMetrics(baseActivity);
		stateDialog = new PopupWindow(root, FormsUtil.SCREEN_WIDTH, FormsUtil.SCREEN_HEIGHT);
	}

	/**
	 * 认证状态窗口的监听函数
	 */
	private void setDialogListener() {
		tvIdentify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				baseActivity.callMe(RealNameAuthentActivity.class);
			}
		});
		tvReplenishInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				baseActivity.callMe(PersonalInfoActivity.class);
			}
		});
		vBg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (stateDialog.isShowing()) {
					stateDialog.dismiss();
				}
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		if (STATUS_CHANGE_FLAG) {
			STATUS_CHANGE_FLAG = false;
			requestInfoStatus();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		stateDialog.dismiss();
	}

	@Override
	public void onStop() {
		super.onStop();
		stateDialog.dismiss();
	}

	/** 轮播图功能模块 */
	@ViewInject(R.id.ivSingleImage)
	private ImageView ivSingleImage;
	@ViewInject(R.id.rltAd)
	private RelativeLayout rltAd;
	@ViewInject(R.id.vpAd)
	private ViewPager vpAd;
	@ViewInject(R.id.rlAdRoot)
	private RelativeLayout rlAdRoot;
	@ViewInject(R.id.indicator)
	private CirclePageIndicator indicator;

	private float preX = 0;
	private float preY = 0;
	private float nowX = 0;
	private float nowY = 0;
	private boolean isTouch = false;// 是否正在拖动轮播图

	private List<Advertisement> mAdvList = new ArrayList<Advertisement>();
	private List<View> views = new ArrayList<View>();

	private Handler adHandler = new Handler() { // 启动广告页面自动播放

		@Override
		public void handleMessage(Message msg) {
			if (!isTouch) {
				if (vpAd.getCurrentItem() == views.size() - 1) {
					vpAd.setCurrentItem(0, false);
				} else {
					vpAd.setCurrentItem(vpAd.getCurrentItem() + 1);
				}
				adHandler.sendEmptyMessageDelayed(0, 3000);
			} else {
				adHandler.sendEmptyMessageDelayed(0, 3000);
			}
		};
	};

	/**
	 * 轮播图点击事件
	 * 
	 * @param index
	 */
	private void clickAdPic(int index) {
		if (mAdvList != null && mAdvList.size() != 0) {
			if (mAdvList.size() == index + 1) {
				vpAd.setCurrentItem(0, false);
			}
		}
	}

	/**
	 * 响应轮播图请求
	 * 
	 * @param retCode
	 * @param response
	 */
	public void notifyForAdPic() {

		if (mAdvList != null && mAdvList.size() > 1) {// 多张图片
			views.clear();
			rltAd.setVisibility(View.VISIBLE);
			ivSingleImage.setVisibility(View.GONE);
			for (Advertisement advertisement : mAdvList) {
				View view = LayoutInflater.from(baseActivity).inflate(R.layout.page_ad, null);
				ImageView iv = (ImageView) view.findViewById(R.id.ivAd);
				iv.setImageResource(advertisement.getImageRes());
				views.add(view);
			}
			LoopViewPagerAdapter adapter = new LoopViewPagerAdapter(views);
			vpAd.setAdapter(adapter);
			indicator.setViewPager(vpAd);
			// 启动轮播图
			if (adHandler.hasMessages(0)) {
				adHandler.removeMessages(0);
			}
			adHandler.sendEmptyMessageDelayed(0, 3000);
		} else if (mAdvList != null && mAdvList.size() == 1) {// 单张图片
			rltAd.setVisibility(View.GONE);
			ivSingleImage.setVisibility(View.VISIBLE);
			ivSingleImage.setImageResource(mAdvList.get(0).getImageRes());
		}

	}

	private void initAdList(int flag) {
		mAdvList.clear();

		switch (flag) {
		case 0:
			Advertisement gdt1 = new Advertisement();
			gdt1.setImageRes(R.drawable.yfx_icon_gdt01);
			mAdvList.add(gdt1);
			ivSteps.setImageResource(R.drawable.yfx_trains_steps);
			break;
		case 1:
			Advertisement fpt1 = new Advertisement();
			fpt1.setImageRes(R.drawable.yfx_icon_fpt01);
			mAdvList.add(fpt1);
			ivSteps.setImageResource(R.drawable.yfx_trains_steps);
			break;
		case 2:
			Advertisement fnt1 = new Advertisement();
			fnt1.setImageRes(R.drawable.yfx_icon_fnt);
			mAdvList.add(fnt1);
			ivSteps.setImageResource(R.drawable.yfx_trains_steps);
			break;
		case 3:
			Advertisement gjd1 = new Advertisement();
			gjd1.setImageRes(R.drawable.yfx_icon_gjd01);
			mAdvList.add(gjd1);
			ivSteps.setImageResource(R.drawable.yfx_trains_steps);
			break;
		case 8:
			Advertisement grfp = new Advertisement();
			grfp.setImageRes(R.drawable.yfx_icon_fpt01);
			mAdvList.add(grfp);
			ivSteps.setImageResource(R.drawable.yfx_trains_steps);
			break;
		case 9:
			Advertisement lyfp1 = new Advertisement();
			lyfp1.setImageRes(R.drawable.yfx_icon_lyfp01);
			mAdvList.add(lyfp1);
			ivSteps.setImageResource(R.drawable.yfx_trains_steps);
			break;
		}
	}

	private void setAdHeight(int flag) {
		switch (flag) {
		case 3:
			double height1 = FormsUtil.SCREEN_WIDTH * 0.3;
			LayoutParams layoutParams1 = new LayoutParams(LayoutParams.MATCH_PARENT, (int) height1);
			rlAdRoot.setLayoutParams(layoutParams1);
			break;

		default:
			double height2 = FormsUtil.SCREEN_WIDTH * 0.3;
			LayoutParams layoutParams2 = new LayoutParams(LayoutParams.MATCH_PARENT, (int) height2);
			rlAdRoot.setLayoutParams(layoutParams2);
			break;
		}
	}
}
