package com.bocop.xfjr.activity.customer;

import java.util.ArrayList;
import java.util.List;

import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.activity.XfjrIndexActivity;
import com.bocop.xfjr.adapter.MainActivityStepAdapter;
import com.bocop.xfjr.base.BaseCheckProcessFragment;
import com.bocop.xfjr.base.XfjrBaseActivity;
import com.bocop.xfjr.bean.add.ProductBean;
import com.bocop.xfjr.config.FragmentConfig;
import com.bocop.xfjr.constant.XFJRConstant;
import com.bocop.xfjr.fragment.FurtherInfoFragment;
import com.bocop.xfjr.fragment.InstalmentPretrialFragment;
import com.bocop.xfjr.fragment.NewBusinessFragment;
import com.bocop.xfjr.fragment.qzzc.FraudDetectionFragment1;
import com.bocop.xfjr.fragment.qzzc.FraudDetectionFragment2;
import com.bocop.xfjr.fragment.qzzc.FraudDetectionFragment3;
import com.bocop.xfjr.fragment.qzzc.FraudDetectionFragment4;
import com.bocop.xfjr.fragment.qzzc.bandcard.FraudDetectionFragment_new5;
import com.bocop.xfjr.fragment.qzzc.three.FraudDetectionFragment_new6;
import com.bocop.xfjr.helper.CheckViewStubHelper;
import com.bocop.xfjr.observer.StepObserver;
import com.bocop.xfjr.observer.StepSubject;
import com.bocop.xfjr.util.FragmentHelper;
import com.bocop.xfjr.util.ScreenUtils;
import com.bocop.xfjr.util.XFJRUtil;
import com.bocop.xfjr.util.dialog.XfjrDialog;
import com.bocop.xfjr.view.stepview.StepView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * description： 消费金融检测页面
 * 
 * 自定义stepview配合fragment联动 fragment通过回退栈管理 上一步下一步通过，拦截的方式在fragment的返回值中判断 进度通过
 * mOutStep 和 mInStep 决定 取值都是从0 -> x 从任意位置跳转进来时，需要将进度解析成 mOutStep 和 mInStep
 * 然后调push函数入栈 返回时调用pop出栈 pop时需要判断，栈中是否有fragment，如果没有step回退2，然后调push函数
 * 
 * Created by TIAN FENG on 2017年8月28日 QQ：27674569 Email: 27674569@qq.com
 * Version：1.0
 */
@ContentView(R.layout.xfjr_activity_application)
public class XfjrMainActivity extends XfjrBaseActivity implements StepObserver {
	
	private XfjrDialog mDialog ;
	public static ProductBean productBean;

	/**
	 * 跳转到此Activity
	 * 
	 * @param context
	 *            上下文
	 * @param step
	 *            进度，一级进度和二级进度在一起 进度位置 = 一级进度 + 二级进度 0 -> 8
	 */
	public static void callMe(Context context, int step) {
		Intent intent = new Intent(context, XfjrMainActivity.class);
		intent.putExtra("step", step);
		context.startActivity(intent);
	}

	public static void callMe(Context context, ProductBean productBean, int step) {
		Intent intent = new Intent(context, XfjrMainActivity.class);
		intent.putExtra("step", step);
		intent.putExtra("ProductBean", productBean);
		XfjrMainActivity.productBean = productBean;
		context.startActivity(intent);
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////我也不想写这么多的判断，需求太操蛋/////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 进度控件
	@ViewInject(R.id.stepView)
	private StepView stepView;
	// title
	@ViewInject(R.id.tvTitle)
	private TextView tvTitle;
	// 首页返回按钮
	@ViewInject(R.id.tvHomePage)
	private TextView tvHomePage;
	// 重置
	@ViewInject(R.id.tvReset)
	private TextView tvReset;
	// 保存
	@ViewInject(R.id.tvSave)
	private TextView tvSave;

	// fragment 切换辅助类
	private FragmentHelper mHelper;
	// 进度一级分类item
	private List<String> mStepDatas;
	// 一级进度
	private int mOutStep = -1;
	// 二级进度
	private int mInStep = 0;
	// 适配器
	private MainActivityStepAdapter mAdapter;
	// 栈顶fragment
	private BaseCheckProcessFragment mStackTopFragment;
	// fragment集合
	private List<BaseCheckProcessFragment> mFragments;
	// 错误页面辅助类
	private CheckViewStubHelper mViewStubHelper;
	
	public boolean isPush = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initErrorView();
	}
	
//	@Override
//	protected void onSaveInstanceState(Bundle b) {
//		// put
//		LogUtils.e("onSaveInstanceState-->put.....");
//		b.putSerializable("productBean", productBean);
//		super.onSaveInstanceState(b);
//	}
//	
//	@Override
//	protected void onRestoreInstanceState(Bundle b) {
//		// get
//		LogUtils.e("onRestoreInstanceState-->get.....");
//		XfjrMainActivity.productBean = (ProductBean) b.getSerializable("productBean");
//		super.onRestoreInstanceState(b);
//	}

	protected void init() {
		// fragment 回退栈管理类
		mHelper = new FragmentHelper(getSupportFragmentManager(), R.id.fltApplication);
		// fragment保存对象 回退栈只与事物相关
		mFragments = new ArrayList<>();
		// 获取传过来的进度
		int step = getIntent().getIntExtra("step", 0);
		productBean = (ProductBean) getIntent().getSerializableExtra("ProductBean");
		// 解析进度
		initStep(step);
		LogUtils.e("mOutStep" + mOutStep + " , mInStep" + mInStep + ",step = " + step);
		/**
		 * 判断进度是否在二级进度 因为，在push的时候，会将进度++，所以这里讲进度位置后撤一步
		 */
		if (mOutStep == FragmentConfig.XFJR_FRAUD_DETECTION_FRAGMENT) {
			mInStep--;
			mOutStep--;
		} else if (mOutStep != -1) {
			mOutStep--;
		}

		// 添加进度器
		addStep();
	}

	/**
	 * 初始化错误页面
	 */
	private void initErrorView() {
		mViewStubHelper = new CheckViewStubHelper(this);
		mViewStubHelper.setOnErrorClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mStackTopFragment != null) {
					mStackTopFragment.errorClick();
				}
			}
		});
	}

	/**
	 * 容器fragment获取辅助类
	 */
	public CheckViewStubHelper getHelper() {
		return mViewStubHelper;
	}

	private void addStep() {
		mStepDatas = new ArrayList<>();
		mStepDatas.add(getString(R.string.new_application));
		mStepDatas.add(getString(R.string.fraud_detection));
		mStepDatas.add(getString(R.string.instalment_pre_trial));
		mStepDatas.add(getString(R.string.further_info));// 现在不要补充资料
		mAdapter = new MainActivityStepAdapter(mStepDatas, this);
		stepView.setAdapter(mAdapter);
		// 入回退栈
		pushBackStack();
	}

	/**
	 * 添加fragment
	 */
	private void addFragment(Fragment fragment) {
		// 入栈是栈顶即为入栈的fragment
		mStackTopFragment = (BaseCheckProcessFragment) fragment;
		StepSubject subject = (StepSubject) fragment;
		// 注册观察者
		subject.register(this);
		// 添加到回退栈
		mHelper.addToBackStack(fragment);
		// 添加到集合管理
		mFragments.add(mStackTopFragment);
		// 显示两个按钮
		showSaveAndRegistView();
	}

	/**
	 * 入栈
	 */
	@Override
	public void pushBackStack() {
		LogUtils.e("***********************************************************************************************");
		try {
			LogUtils.e(productBean.getQz().toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		LogUtils.e("***********************************************************************************************");
		// 入栈判断是否是在二级进度
		if (mOutStep == mAdapter.INNER_POSITION && mInStep < mAdapter.INNER_STEP_COUNT) {
			mInStep++;
		} else {
			mOutStep++;
		}
		LogUtils.e("pushBackStack mOutStep" + mOutStep + ", mInStep" + mInStep);
		// 保证不为0，防止加入为空
		mInStep = mInStep == -1 ? 0 : mInStep;
		// 根据进度添加对应的fragment
		switch (mOutStep) {
		case FragmentConfig.XFJR_NEW_APPLICATION_FRAGMENT:
//			isPush = true; // 用来控制欺诈证测第一个页面是否显示上一步按钮
			addFragment(new NewBusinessFragment());
			LogUtils.e("NewApplicationFragment");
			break;
		case FragmentConfig.XFJR_FRAUD_DETECTION_FRAGMENT:
			pushInnerBackStack();
			break;
		case FragmentConfig.XFJR_INSTALMENT_PRETRIAL_FRAGMENT:
			addFragment(new InstalmentPretrialFragment());
			break;
		case FragmentConfig.XFJR_FURTHER_INFO_FRAGMENT:
			addFragment(new FurtherInfoFragment());
			break;
		}
		// 设置进度
		if (mOutStep != FragmentConfig.BACK) {
			stepView.setFocusPosition(mOutStep);
			stepView.setInnerStatePosition(mInStep);
		}
		setTitleName();
	}

	/**
	 * 将二级进度加入栈
	 */
	private void pushInnerBackStack() {
		switch (mInStep) {
		case FragmentConfig.XFJR_FRAUD_DETECTION_FRAGMENT1:
			addFragment(new FraudDetectionFragment1());
			break;
		case FragmentConfig.XFJR_FRAUD_DETECTION_FRAGMENT2:
			if (isNeedFraudDetectionFragment2()) {
				addFragment(new FraudDetectionFragment2());
			} else {
				// 不需要手机验证
				pushBackStack();
			}
			break;
		case FragmentConfig.XFJR_FRAUD_DETECTION_FRAGMENT3:
			if (isNeedFraudDetectionFragment3()) {
				addFragment(new FraudDetectionFragment3());
			} else {
				// 不需要人脸识别
				pushBackStack();
			}
			break;
		case FragmentConfig.XFJR_FRAUD_DETECTION_FRAGMENT4:
			addFragment(new FraudDetectionFragment4());
			break;
		case FragmentConfig.XFJR_FRAUD_DETECTION_FRAGMENT5:
			if (isNeedFraudDetectionFragment4()) {
				addFragment(new FraudDetectionFragment_new5());
			} else {
				// 不需要银联验证
				pushBackStack();
			}
			break;
		case FragmentConfig.XFJR_FRAUD_DETECTION_FRAGMENT6:
			if (isNeedFraudDetectionFragment5()) {
				addFragment(new FraudDetectionFragment_new6());
			} else {
				// 不需要第三方侦测
				pushBackStack();
			}
			break;
		}
		if (mInStep != FragmentConfig.BACK) {
			stepView.setInnerStatePosition(mInStep);
		}
	}

	/*
	 * 出栈
	 */
	@Override
	public void popBackStack() {
		/**
		 * 出栈需要先判断是否拦截返回键和上一步监听事件
		 */
		if (mStackTopFragment != null && mStackTopFragment.onBackClick()) {
			return;
		}
		/**
		 * 存在直接跳转某一个位置所以如果栈里只有一个
		 * 就是直接跳转是push的当前页，那么我们需要将当前的fragment，pop出栈然后将前一个push入栈
		 */
		if (mFragments.size() == 1) {
			/*
			 * 控制位置 push是会加一，这里需要减一，那么push时会push到当前位置，不会到前一位置
			 * 递归pop时会再减一，固能达到减一位置
			 */
			if (mOutStep == mAdapter.INNER_POSITION && mInStep <= mAdapter.INNER_STEP_COUNT) {
				mInStep--;
				if (mInStep == -1) {
					mOutStep--;
				}
			} else {
				mOutStep--;
			}
			// 移出栈中的一个碎片
			mFragments.remove(mFragments.size() - 1);
			// 递归pop
			popBackStack();
			// 移出后push
			pushBackStack();
			return;
		}
		// 是否在二级位置
		if (mOutStep == mAdapter.INNER_POSITION && mInStep != 0) {
			mInStep--;
		} else {
			mOutStep--;
		}
		// 防止-1抛出的异常
		if (mInStep >= 0 && mOutStep >= 0) {
			stepView.setInnerStatePosition(mInStep);
			stepView.setFocusPosition(mOutStep);
		}
		if (mOutStep < -1) {
			finish();
			return;
		}
		mHelper.popToBackStack();
		// 移出最后一个
		if (mFragments.size() - 1 >= 0) {
			mFragments.remove(mFragments.size() - 1);
			/**
			 * pop需要拿栈顶的fragmet的显示状态
			 */
			initStackTopFragment();
		} else {
			mStackTopFragment = null;
		}
		setTitleName();
		if (mStackTopFragment != null) {
			showSaveAndRegistView();
		}
	}

	/**
	 * 初始化栈顶碎片
	 */
	private void initStackTopFragment() {
		mStackTopFragment = mFragments.get(mFragments.size() - 1);
	}

	/**
	 * 显示保存和重置
	 */
	private void showSaveAndRegistView() {
		tvReset.setVisibility(mStackTopFragment.isShowReset ? View.VISIBLE : View.GONE);
		tvSave.setVisibility(mStackTopFragment.isShowSave ? View.VISIBLE : View.GONE);
	}

	/**
	 * 设置title的名称 重置保存可以在这里处理
	 */
	private void setTitleName() {
		// switch (mOutStep) {
		// case FragmentConfig.XFJR_NEW_APPLICATION_FRAGMENT:
		// tvTitle.setText(R.string.new_application);
		// break;
		// case FragmentConfig.XFJR_FRAUD_DETECTION_FRAGMENT:
		// tvTitle.setText(R.string.fraud_detection);
		// break;
		// case FragmentConfig.XFJR_INSTALMENT_PRETRIAL_FRAGMENT:
		// tvTitle.setText(R.string.instalment_pre_trial);
		// break;
		// case FragmentConfig.XFJR_FURTHER_INFO_FRAGMENT:
		// tvTitle.setText(R.string.further_info);
		// break;
		// }
		tvTitle.setText(R.string.xfjr_to_examine);
	}

	@Override
	public void onBackPressed() {
		LogUtils.e("XfjrMainActivity--->onBackPressed");
		if (mOutStep < 0) {
			finish();
			return;
		}
		popBackStack();
	}

	/**
	 * 首页按钮点击
	 */
	@OnClick(R.id.tvHomePage)
	private void homeClick(View view) {
		// finish();// 或跳转首页，请将首页栈上的activity pop出栈
//		startActivity(new Intent(this, XfjrIndexActivity.class));
		// XfjrIndexActivity.goMe(getApplication(), 0);

		mDialog = new XfjrDialog.Builder(this)
			.setContentView(R.layout.xfjr_dialog_go_home)
			.setWidthAndHeight(ScreenUtils.dip2px(this, 300), -2)
			.setOnClickListener(R.id.tvOk, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mDialog.dismiss();
					startActivity(new Intent(XfjrMainActivity.this, XfjrIndexActivity.class));
				}
			})
			.setOnClickListener(R.id.tvCancel, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mDialog.dismiss();
				}
			})
			.show();

	}

	/**
	 * 保存按钮
	 */
	@OnClick(R.id.tvSave)
	private void saveClick(View view) {
		if (mStackTopFragment != null) {
			mStackTopFragment.onSaveClick(view);
		}
	}

	/**
	 * 重置按钮
	 */
	@OnClick(R.id.tvReset)
	private void resetClick(View view) {
		if (mStackTopFragment != null) {
			mStackTopFragment.onResetClick(view);
		}
	}

	/**
	 * 根据指定进度，调整内外进度
	 */
	public void initStep(int step) {
		switch (step) {
		case 0:// 新增进件
			mOutStep = -1;
			mInStep = 0;
			break;
		case 1:// 欺诈 1
			mOutStep = 1;
			mInStep = 1;
			break;
		case 2:// 手机验证
			mOutStep = 1;
			mInStep = 2;
			break;
		case 3:// 人脸识别
			mOutStep = 1;
			mInStep = 3;
			break;

		case 4://人行
			mOutStep = 1;
			mInStep = 4;
			break;
		case 5:// 银联
			mOutStep = 1;
			mInStep = 5;
			break;
		case 6:// 第三方
			mOutStep = 1;
			mInStep = 6;
			break;
		// case 8:
		// mOutStep = 1;
		// mInStep = 7;
		// break;
		case 7://风险
			mOutStep = 2;
			mInStep = 6;
			break;
		case 8:// 提交资料
			mOutStep = 3;
			mInStep = 6;
			break;
		default:
			throw new IllegalStateException("请给出正确的进度，当前进度 step = " + step);
		}

	}

	public void sendBR(int status, int flag) {
		Intent intent = new Intent(XFJRConstant.ACTION_UPDATE_DATA);
		intent.putExtra(XFJRConstant.ACTION_FLAG_INT, flag);
		intent.putExtra(XFJRConstant.KEY_BUSINESS_STATUS, status);
		sendBroadcast(intent);
	}
	
	public void sendBR(int status) {
//		if (productBean.getFrom() == 2) {
//			LogUtils.e("发送广播回列表更新");
//		} else if (productBean.getFrom() == 1) {
//			LogUtils.e("发送广播回详情更新");
//		} else if (productBean.getFrom() == -1) {
//			LogUtils.e("发送广播回首页更新");
//		}
//		Intent intent = new Intent(XFJRConstant.ACTION_UPDATE_DATA);
//		intent.putExtra(XFJRConstant.ACTION_FLAG_INT, productBean.getFrom());
//		intent.putExtra(XFJRConstant.KEY_BUSINESS_STATUS, status);
//		sendBroadcast(intent);
	}

	private boolean isNeedFraudDetectionFragment2() {
//		return false; // 是否使用手机侦测（Y，N）
		return productBean.getQz().getZcMobile().equals("Y"); // 是否使用手机侦测（Y，N）
	}

	private boolean isNeedFraudDetectionFragment3() {
//		return false; // 是否使用活体侦测（Y，N）
		return productBean.getQz().getZcFace().equals("Y"); // 是否使用活体侦测（Y，N）
	}

	private boolean isNeedFraudDetectionFragment4() {
//		return false; 
		return productBean.getQz().getZcUnion().equals("Y"); // 是否使用银联卡识别（Y，N）
	}

	private boolean isNeedFraudDetectionFragment5() {
//		return false; 
		return productBean.getQz().getZcCredit().equals("Y"); // 是否使用第三方征信（Y，N）
	}

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