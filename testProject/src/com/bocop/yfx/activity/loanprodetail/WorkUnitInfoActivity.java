package com.bocop.yfx.activity.loanprodetail;

import java.io.UnsupportedEncodingException;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.DialogUtil;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.xms.bean.ConstHead;
import com.bocop.xms.utils.KeyboardUtils;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.yfx.activity.LoanMainActivity;
import com.bocop.yfx.bean.CommonResponse;
import com.bocop.yfx.fragment.LoanProDetailFragment;
import com.bocop.yfx.utils.CheckoutUtil;
import com.bocop.yfx.utils.ToastUtils;
import com.bocop.yfx.xml.CspXmlYfx003;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 工作单位信息 Created by lh on 16-1-15.
 */
@ContentView(R.layout.yfx_activity_work_unit)
public class WorkUnitInfoActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.etUnitName)
	private TextView etUnitName;
	@ViewInject(R.id.tvCity)
	private TextView tvCity;
	@ViewInject(R.id.tvAreaCode)
	private TextView tvAreaCode;
	@ViewInject(R.id.etTelNum)
	private EditText etTelNum;
	@ViewInject(R.id.etExtensionNum)
	private EditText etExtensionNum;
	@ViewInject(R.id.btnInputAgain)
	private Button btnInputAgain;
	@ViewInject(R.id.btnOk)
	private Button btnOk;

	private String[] cities = { "南昌", "九江", "上饶", "抚州", "宜春", "吉安", "赣州", "景德镇", "萍乡", "鹰潭","新余" };
	private String[] areaCodes = { "0791", "0792", "0793", "0794", "0795", "0796", "0797", "0798", "0799", "0701", "0790" };

	private String name;
	private String gender;
	private String idCard;
	private String phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		initView();
	}

	private void initData() {
		if (null != getIntent().getExtras()) {
			name = getIntent().getStringExtra("NAME");
			gender = getIntent().getStringExtra("GENDER");
			idCard = getIntent().getStringExtra("ID_CARD");
			phone = getIntent().getStringExtra("PHONE");
		}
	}

	private void initView() {
		tvTitle.setText("工作单位信息");
	}

	@OnClick({ R.id.tvCity, R.id.btnInputAgain, R.id.btnOk })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvCity:
			KeyboardUtils.closeInput(this, tvCity);
			DialogUtil.showToSelect(this, "", cities, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					tvCity.setText(cities[which]);
					tvAreaCode.setText(areaCodes[which]);
				}
			});
			break;

		case R.id.btnInputAgain:
			KeyboardUtils.closeInput(this, btnInputAgain);
			clearData();
			break;

		case R.id.btnOk:
			KeyboardUtils.closeInput(this, btnOk);
			if (checkData()) {
				requestRealNameAuthen();
			}
			break;
		}
	}

	/**
	 * 申请实名认证
	 * 
	 * @param company
	 * @param city
	 * @param phoneNum
	 * @param idCard
	 * @param name
	 */
	private void requestRealNameAuthen() {
		try {
			String unitTel = tvAreaCode.getText().toString() + etTelNum.getText().toString()
					+ etExtensionNum.getText().toString();
			CspXmlYfx003 cspXmlYfx003 = new CspXmlYfx003(LoginUtil.getUserId(this), name, gender, idCard, phone,
					tvCity.getText().toString(), etUnitName.getText().toString(), unitTel);
			String strXml = cspXmlYfx003.getCspXml();
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			final byte[] byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(this);
			cspUtil.setFLAG_YFX_CSP(true);
			cspUtil.setTxCode("001003");
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			cspUtil.postCspLogin(byteMessage, new CallBack() {

				@Override
				public void onSuccess(String responStr) {
					CommonResponse commonResponse = XStreamUtils.getFromXML(responStr, CommonResponse.class);
					ConstHead constHead = commonResponse.getConstHead();
					if (constHead != null) {
						if ("00".equals(constHead.getErrCode())) {
							ToastUtils.show(WorkUnitInfoActivity.this, "成功", 0);
							LoanProDetailFragment.STATUS_CHANGE_FLAG = true;
							getActivityManager().finishAllWithoutActivity(LoanMainActivity.class);
						} else if ("50".equals(constHead.getErrCode())) {
							DialogUtil.showWithToMain(WorkUnitInfoActivity.this, constHead.getErrMsg());
						} else {
							CspUtil.onFailure(WorkUnitInfoActivity.this, constHead.getErrMsg());
							// ToastUtils.show(WorkUnitInfoActivity.this,
							// constHead.getErrMsg(), 0);
						}
					}
				}

				@Override
				public void onFailure(String responStr) {
					CspUtil.onFailure(WorkUnitInfoActivity.this, responStr);
				}

				@Override
				public void onFinish() {

				}

			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	private boolean checkData() {
		String telNum = etTelNum.getText().toString();
		if (CheckoutUtil.isEmpty(etUnitName.getText().toString())) {
			Toast.makeText(this, "请输入单位名称", Toast.LENGTH_SHORT).show();
			return false;
		} else if (!TextUtils.isEmpty(telNum) && telNum.length() < 7) {
			Toast.makeText(this, "请输入7-8位座机号", Toast.LENGTH_SHORT).show();
			return false;
		} else if ("请选择".equals(tvCity.getText().toString())) {
			Toast.makeText(this, "请选择所在城市", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}
	}

	private void clearData() {
		etUnitName.setText("");
		tvCity.setText("请选择");
		tvAreaCode.setText("");
		etTelNum.setText("");
		etExtensionNum.setText("");
	}

}
