package com.bocop.yfx.activity;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.base.BaseApplication;
import com.boc.jx.baseUtil.cache.CacheBean;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.common.util.ContentUtils;
import com.boc.jx.constants.Constants;
import com.boc.jx.tools.DialogUtil;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.LoginUtil.ILoginListener;
import com.bocop.jxplatform.util.LoginUtil.OnRequestCustCallBack;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.jxplatform.view.BackButton;
import com.bocop.yfx.utils.ToastUtils;
import com.bocop.yfx.xml.CspXmlYfx016;
import com.bocop.yfx.xml.identitycheck.IdentityCheckBean;
import com.bocop.yfx.xml.identitycheck.IdentityCheckResp;
import com.bocop.zyyr.activity.FinanceMainActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 各种类型通入口
 * 
 * @author LH
 *
 */
@ContentView(R.layout.yfx_activity_trains)
public class TrainsActivity extends BaseActivity implements ILoginListener {

	private static final int FLAG_FPT = 1; // 扶贫通
	private static final int FLAG_FNT = 2; // 扶农通
	private static final int FLAG_GJT = 3; // 公积通
	private static final int FLAG_FWT = 4; // 扶微通
	private static final int FLAG_GDT = 5; // 个贷通
	private static final int FLAG_ZDT = 6; // 专贷通
	private static final int FLAG_ZXT = 7; // 中小通
	private static final int FLAG_grfp = 8; // 个人扶贫
	private static final int FLAG_lyfp = 9; // 旅游扶贫

	private int PRO_FLAG = 0;
	private String custType = ""; // 入口类型

	public BaseApplication baseApplication = BaseApplication.getInstance();
	protected BaseActivity baseActivity;

	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	@ViewInject(R.id.iv_imageLeft)
	private BackButton backBtn;
	@ViewInject(R.id.ivImage)
	private ImageView ivImage;
	@ViewInject(R.id.ivHeader)
	private ImageView ivHeader;
	@ViewInject(R.id.ivFooter)
	private ImageView ivFooter;
	@ViewInject(R.id.ivText)
	private ImageView ivText;
	@ViewInject(R.id.btnApply)
	private Button btnApply;
	@ViewInject(R.id.btnApply2)
	private Button btnApply2;
	@ViewInject(R.id.llApply2)
	private LinearLayout llApply2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		initListener();
	}

	@SuppressLint("NewApi")
	private void initView() {
		PRO_FLAG = getIntent().getExtras().getInt("PRO_FLAG");
		switch (PRO_FLAG) {
		case FLAG_FPT:
			initImageView(true);
			ivImage.setImageResource(R.drawable.yfx_trains_bg_fpt);
			tv_titleName.setText("扶贫通");
			custType = "FP";
			break;
		case FLAG_FNT:
			initImageView(false);
			ivText.setImageResource(R.drawable.yfx_trains_bg_fnt_text);
			ivHeader.setImageResource(R.drawable.yfx_trains_bg_fnt_header);
			tv_titleName.setText("扶农通");
			custType = "FN";
			break;
		case FLAG_GJT:
			initImageView(false);
			btnApply.setBackgroundResource(R.drawable.yfx_trains_btn_gjt1);
			btnApply2.setBackgroundResource(R.drawable.yfx_trains_btn_apply);

			llApply2.setVisibility(View.VISIBLE);
			ivHeader.setImageResource(R.drawable.yfx_trains_bg_gjt_header);
			ivText.setImageResource(R.drawable.yfx_trains_bg_gjt_text);
			tv_titleName.setText("公积通");
			custType = "GJ";
			break;
		case FLAG_FWT:
			initImageView(true);
			ivImage.setImageResource(R.drawable.yfx_trains_bg_fwt);
			tv_titleName.setText("扶微通");
			break;
		case FLAG_GDT:
			initImageView(false);
			btnApply.setBackgroundResource(R.drawable.yfx_trains_btn_apply);
			btnApply2.setBackgroundResource(R.drawable.yfx_trains_btn_gak);

			llApply2.setVisibility(View.VISIBLE);
			ivText.setImageResource(R.drawable.yfx_trains_bg_gdt_text);
			ivHeader.setImageResource(R.drawable.yfx_trains_bg_gdt_header);
			tv_titleName.setText("个贷通");
			custType = "GD";
			break;
		case FLAG_ZDT:
			initImageView(true);
			ivImage.setImageResource(R.drawable.yfx_trains_bg_zdt);
			tv_titleName.setText("专贷通");
			break;
		case FLAG_ZXT:
			ivImage.setImageResource(R.drawable.yfx_trains_bg_zxt);
			tv_titleName.setText("中小通");
			break;
		case FLAG_grfp:
			initImageView(false);
			ivText.setImageResource(R.drawable.yfx_trains_bg_grfpd_text);
			ivHeader.setImageResource(R.drawable.yfx_trains_bg_grfpd_header);
			tv_titleName.setText("个人扶贫");
			custType = "FP";
			break;
		case FLAG_lyfp:
			initImageView(false);
			ivText.setImageResource(R.drawable.yfx_trains_bg_lyfp_text);
			ivHeader.setImageResource(R.drawable.yfx_trains_bg_lyfp_header);
			tv_titleName.setText("旅游扶贫");
			custType = "FP";
			break;
		}

	}

	private void initImageView(boolean isOneImage) {
		if (isOneImage) {
			ivFooter.setVisibility(View.GONE);
			ivHeader.setVisibility(View.GONE);
			ivText.setVisibility(View.GONE);
			ivImage.setVisibility(View.VISIBLE);
		} else {
			ivFooter.setVisibility(View.VISIBLE);
			ivHeader.setVisibility(View.VISIBLE);
			ivText.setVisibility(View.VISIBLE);
			ivImage.setVisibility(View.GONE);
		}
	}

	private void initListener() {
		btnApply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!LoginUtil.isLog(TrainsActivity.this)) {
					LoginUtil.authorize(TrainsActivity.this, TrainsActivity.this);
					return;
				}
				if (PRO_FLAG == FLAG_ZDT) { // 专贷通
					Intent intent = new Intent(TrainsActivity.this, FinanceMainActivity.class);
					startActivity(intent);
				} else if (PRO_FLAG == FLAG_GJT) {
					if (LoginUtil.isLog(TrainsActivity.this)) { // 公积金查询
						Intent intent = new Intent(TrainsActivity.this, FundQueryActivity.class);
						startActivity(intent);
					} else {
						LoginUtil.authorize(TrainsActivity.this, TrainsActivity.this);
					}
				} else {
					goToZZYD();
				}
			}
		});

		btnApply2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * if (PRO_FLAG == FLAG_GDT || PRO_FLAG == FLAG_GJT) {
				 * goToZZYD(); }
				 */
				if (PRO_FLAG == FLAG_GJT) {
					goToZZYD();
				} else if (PRO_FLAG == FLAG_GDT) {
					// 跳转到新页面
					startActivity(new Intent(TrainsActivity.this, ConsultationActivity.class));
				}
			}
		});
	}

	private void goToZhzyyd() {
		if (!LoginUtil.isLog(this)) {
			LoginUtil.authorize(this, TrainsActivity.this);
			return;
		}
		Bundle bundle = new Bundle();
		bundle.putInt("PRO_FLAG", 0);
		Intent intent = new Intent(TrainsActivity.this, LoanActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	/**
	 * 跳转到中银E贷界面
	 */
	private void goToZZYD() {

		if (!LoginUtil.isLog(this)) {
			LoginUtil.authorize(this, TrainsActivity.this);
			return;
		}

		final String cardId = ContentUtils.getSharePreStr(this, Constants.CUSTOM_PREFERENCE_NAME,
				Constants.CUSTOM_ID_NO);
		if (!BocSdkConfig.isTest) {
			if (null != CacheBean.getInstance().get(CacheBean.CUST_ID)
					&& !TextUtils.isEmpty(CacheBean.getInstance().get(CacheBean.CUST_ID).toString())
					&& !TextUtils.isEmpty(cardId)) {
				// if (checkTime()) {
				requestIdentityCheck(cardId);
				// Bundle bundle = new Bundle();
				// bundle.putInt("PRO_FLAG", PRO_FLAG == FLAG_GDT ? 0 :
				// PRO_FLAG);
				// callMe(LoanMainActivity.class, bundle);
				// } else {
				// Toast.makeText(this, "温馨提示：每日 07:00 -- 21:00 提供服务。",
				// Toast.LENGTH_SHORT).show();
				// }
			} else {
				LoginUtil.requestBocopForCustid(this, true, new OnRequestCustCallBack() {

					@Override
					public void onSuccess() {
						requestIdentityCheck(cardId);
						// Bundle bundle = new Bundle();
						// bundle.putInt("PRO_FLAG", PRO_FLAG == FLAG_GDT ? 0 :
						// PRO_FLAG);
						// callMe(LoanMainActivity.class, bundle);
					}
				});
			}
		} else {
			CacheBean.getInstance().put(CacheBean.CUST_ID, "");
			Bundle bundle = new Bundle();
			bundle.putInt("PRO_FLAG", 0);
			callMe(LoanMainActivity.class, bundle);
		}
	}

	/**
	 * 请求身份验证
	 */
	private void requestIdentityCheck(String cardId) {
		try {
			CspXmlYfx016 cspXmlYfx016 = new CspXmlYfx016(cardId, custType);
			String strXml = cspXmlYfx016.getCspXml();
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			final byte[] byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(this);
			cspUtil.setFLAG_YFX_CSP(true);
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			cspUtil.postCspLogin(new String(byteMessage, "GBK"), new CallBack() {

				@Override
				public void onSuccess(String responStr) {
					IdentityCheckBean identityCheckBean = IdentityCheckResp.readStringXml(responStr);
					if (identityCheckBean != null) {
						if ("00".equals(identityCheckBean.getErrorcode())) {
							String type = identityCheckBean.getCustType();
							if ("ZH".equals(type)) {
								if ("GD".equals(custType)) {
									goToZhzyyd();
								} else {
									DialogUtil.showWithToMain(TrainsActivity.this, "您不是中银受邀客户!");
								}
							} else {
								Bundle bundle = new Bundle();
								bundle.putInt("PRO_FLAG", PRO_FLAG == FLAG_GDT ? 0 : PRO_FLAG);
								callMe(LoanMainActivity.class, bundle);
							} 
						} else if ("50".equals(identityCheckBean.getErrorcode())) {
							DialogUtil.showWithToMain(TrainsActivity.this, identityCheckBean.getErrormsg());
						} else {
							CspUtil.onFailure(TrainsActivity.this, identityCheckBean.getErrormsg());
						}
					}
				}

				@Override
				public void onFailure(String responStr) {
					ToastUtils.show(TrainsActivity.this, responStr, Toast.LENGTH_SHORT);
				}

				@Override
				public void onFinish() {

				}

			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检验时间是否在规定区间内
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	private boolean checkTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");

		int nowTime = Integer.parseInt(sdf.format(new Date()));
		if (nowTime >= 70000 && nowTime <= 210000) {
			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bocop.jxplatform.util.LoginUtil.ILoginListener#onLogin()
	 */
	@Override
	public void onLogin() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bocop.jxplatform.util.LoginUtil.ILoginListener#onCancle()
	 */
	@Override
	public void onCancle() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bocop.jxplatform.util.LoginUtil.ILoginListener#onError()
	 */
	@Override
	public void onError() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bocop.jxplatform.util.LoginUtil.ILoginListener#onException()
	 */
	@Override
	public void onException() {
		// TODO Auto-generated method stub

	}
}
