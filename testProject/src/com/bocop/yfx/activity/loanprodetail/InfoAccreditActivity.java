package com.bocop.yfx.activity.loanprodetail;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.cache.CacheBean;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.DialogUtil;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.BocOpUtil;
import com.bocop.jxplatform.util.BocOpUtil.CallBackBoc;
import com.bocop.jxplatform.util.BocOpUtilWithoutDia;
import com.bocop.jxplatform.util.BocOpUtilWithoutDia.CallBackBoc2;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.xms.bean.ConstHead;
import com.bocop.xms.utils.KeyboardUtils;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.yfx.bean.CommonResponse;
import com.bocop.yfx.utils.CheckoutUtil;
import com.bocop.yfx.utils.TimeCountUtil;
import com.bocop.yfx.utils.ToastUtils;
import com.bocop.yfx.xml.CspXmlYfx006;
import com.google.gson.Gson;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 信息授权列表
 *
 * @author rd
 */

@ContentView(R.layout.yfx_activity_info_accredit)
public class InfoAccreditActivity extends BaseActivity {
	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.llLimitTreaty)
	private LinearLayout llLimitTreaty;// 征信授权选择框
	@ViewInject(R.id.llAccredit)
	private LinearLayout llAccredit;// 额度协议选择框
	@ViewInject(R.id.etCheckCode)
	private EditText etCheckCode;// 验证码
	@ViewInject(R.id.ivAccredit)
	private ImageView ivAccredit;// 征信授权选中状态图
	@ViewInject(R.id.ivLimitTreaty)
	private ImageView ivLimitTreaty;// 额度协议选中状态图
	@ViewInject(R.id.tvSendCheckCode)
	private TextView tvSendCheckCode;
	@ViewInject(R.id.btnOk)
	private Button btnOk;

	private String phone;// 手机号
	private String checkCode;// 验证码

	public static boolean FLAG_LIMIT_TREATY = false;
	public static boolean FLAG_REFERENCE_ACCREDIT = false;
	private CustomProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tvTitle.setText(getString(R.string.infoAccredit));
		phone = getIntent().getStringExtra("phone");
		readAccreditInfo();
		dialog = new CustomProgressDialog(this, "...正在加载...", R.anim.frame);
	}

	/**
	 * 返回时读取授权信息
	 */
	@Override
	protected void onResume() {
		super.onResume();
		readAccreditInfo();
	}

	/**
	 * 读取已通过授权的信息列表
	 */
	private void readAccreditInfo() {
		// 根据授权状态显示或隐藏勾选图标
		if (FLAG_REFERENCE_ACCREDIT) {
			ivAccredit.setVisibility(View.VISIBLE);
		} else {
			ivAccredit.setVisibility(View.GONE);
		}
		if (FLAG_LIMIT_TREATY) {
			ivLimitTreaty.setVisibility(View.VISIBLE);
		} else {
			ivLimitTreaty.setVisibility(View.GONE);
		}
	}

	@OnClick({ R.id.llLimitTreaty, R.id.llAccredit, R.id.btnOk, R.id.tvSendCheckCode, R.id.iv_imageLeft })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llLimitTreaty:
			callMe(LimitTreatyActivity.class);
			break;
		case R.id.llAccredit:
			callMe(ReferenceAccreditActivity.class);
			break;
		case R.id.btnOk:
			checkCode = etCheckCode.getText().toString();
			KeyboardUtils.closeInput(this, btnOk);
			if (CheckoutUtil.isEmpty(checkCode)) {
				ToastUtils.show(this, getString(R.string.inputCheckC), Toast.LENGTH_SHORT);
			} else if (!FLAG_REFERENCE_ACCREDIT) {
				ToastUtils.show(this, "您尚未同意征信查询授权书", Toast.LENGTH_SHORT);
			} else if (!FLAG_LIMIT_TREATY) {
				ToastUtils.show(this, "您尚未同意中国银行贷款合同", Toast.LENGTH_SHORT);
			} else {
				 dialog.show();
				 requestBocopForCheckMsg();// 验证短信验证码
//				requestApplyGxd();
			}
			break;
		case R.id.tvSendCheckCode:
			KeyboardUtils.closeInput(this, tvSendCheckCode);
			tvSendCheckCode.setBackgroundResource(R.drawable.yfx_shape_count_check_code);
			TimeCountUtil time = new TimeCountUtil(tvSendCheckCode, 60);
			time.countTime();
			requestBocForTelMsg();// 获取验证码
			break;
		case R.id.iv_imageLeft:
			finish();
			break;

		}
	}

	/**
	 * 请求申请工薪贷
	 */
	private void requestApplyGxd() {
		try {
			CspXmlYfx006 cspXmlYfx006 = new CspXmlYfx006(getCacheBean().get(CacheBean.CUST_ID).toString(),
					LoginUtil.getUserId(this));
			String strXml = cspXmlYfx006.getCspXml();
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			final byte[] byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(this);
			cspUtil.setFLAG_YFX_CSP(true);
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			cspUtil.setTxCode("001006");
			cspUtil.postCspLogin(new String(byteMessage, "GBK"), new CallBack() {

				@Override
				public void onSuccess(String responStr) {
					CommonResponse applyGxdResponse = XStreamUtils.getFromXML(responStr, CommonResponse.class);
					ConstHead constHead = applyGxdResponse.getConstHead();
					if (constHead != null) {
						if ("00".equals(constHead.getErrCode())) {
							callMe(InfoSubmitResultActivity.class);
						} else if ("50".equals(constHead.getErrCode())) {
							DialogUtil.showWithToMain(InfoAccreditActivity.this, constHead.getErrMsg());
						} else {
							CspUtil.onFailure(InfoAccreditActivity.this, constHead.getErrMsg());
						}
					}
					dialog.dismiss();
				}

				@Override
				public void onFailure(String responStr) {
					dialog.dismiss();
					CspUtil.onFailure(InfoAccreditActivity.this, responStr);
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub

				}

			}, false);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取验证码
	 */
	private void requestBocForTelMsg() {
		BocOpUtilWithoutDia bocOpUtil = new BocOpUtilWithoutDia(this);
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		map.put("usrid", LoginUtil.getUserId(this));
		map.put("usrtel", phone);
		map.put("randtrantype", TransactionValue.messageType);
		final String strGson = gson.toJson(map);

		bocOpUtil.postOpboc(strGson, TransactionValue.SA7114, new CallBackBoc2() {

			@Override
			public void onSuccess(String responStr) {

				try {
					// MsgCode msgCode = JsonUtils.getObject(responStr,
					// MsgCode.class);
					String phone1 = phone.substring(0, 3) + "****" + phone.substring(7);
					ToastUtils.show(InfoAccreditActivity.this, "短信已发送至" + phone1 + "，请查收", Toast.LENGTH_LONG);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(String responStr) {
				CspUtil.onFailure(InfoAccreditActivity.this, responStr);

			}

			@Override
			public void onStart() {
				Log.i("tag", getString(R.string.sendJson) + strGson);
			}
		});
	}

	/**
	 * 验证短信验证码
	 */
	private void requestBocopForCheckMsg() {

		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		map.put("usrid", LoginUtil.getUserId(this));
		map.put("usrtel", phone);
		map.put("randtrantype", TransactionValue.messageType);
		map.put("mobcheck", checkCode);
		final String strGson = gson.toJson(map);

		BocOpUtil bocOpUtil = new BocOpUtil(this);
		bocOpUtil.postOpbocNoDialog(strGson, TransactionValue.SA7115, new CallBackBoc() {

			@Override
			public void onSuccess(String responStr) {
				requestApplyGxd();
				dialog.dismiss();
			}

			@Override
			public void onStart() {
				dialog.dismiss();
				Log.i("tag", getString(R.string.sendJson) + strGson);
			}

			@Override
			public void onFinish() {
				dialog.dismiss();
			}

			@Override
			public void onFailure(String responStr) {
				dialog.dismiss();
				if ("0".equals(responStr) || "1".equals(responStr)) {
					Toast.makeText(InfoAccreditActivity.this, R.string.onFailure, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(InfoAccreditActivity.this, responStr, Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	@Override
	protected void onDestroy() {
		FLAG_LIMIT_TREATY = false;
		FLAG_REFERENCE_ACCREDIT = false;
		super.onDestroy();
	}
}
