package com.bocop.yfx.activity.loanprodetail;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.DialogUtil;
import com.bocop.jxplatform.R;
import com.bocop.xms.utils.KeyboardUtils;
import com.bocop.yfx.utils.CheckoutUtil;
import com.bocop.yfx.utils.ToastUtils;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 个人实名认证
 * 
 * @author rd
 * 
 */
@ContentView(R.layout.yfx_activity_real_name_authent)
public class RealNameAuthentActivity extends BaseActivity {
	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.etName)
	private EditText etName;// 姓名
	@ViewInject(R.id.etIdentityCard)
	private EditText etIdentityCard;// 身份证
	@ViewInject(R.id.tvGender)
	private TextView tvGender;// 性别
	@ViewInject(R.id.etPhoneNum)
	private EditText etPhoneNum;// 手机号
	@ViewInject(R.id.btnInputAgain)
	private Button btnInputAgain;
	@ViewInject(R.id.btnOk)
	private Button btnOk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tvTitle.setText("个人实名认证");
		// requestCityAndCompany();// 请求城市与单位信息

	}

	/**
	 * 请求城市与单位信息
	 */
	// private void requestCityAndCompany() {
	// try {
	// CspXmlYfx002 cspXmlYfx002 = new CspXmlYfx002();
	// String strXml = cspXmlYfx002.getCspXml();
	// // 生成MCIS报文
	// Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
	// final byte[] byteMessage = mcis.getMcis();
	// // 发送报文
	// CspUtil cspUtil = new CspUtil(this);
	// cspUtil.setFLAG_YFX_CSP(true);
	// Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
	// cspUtil.setTxCode("001002");
	// cspUtil.postCspLogin(new String(byteMessage, "GBK"), new CallBack() {
	//
	// @Override
	// public void onSuccess(String responStr) {
	// dealSuccessMessage(responStr);// 处理成功信息
	// }
	//
	// @Override
	// public void onFailure(String responStr) {
	// CspUtil.onFailure(RealNameAuthentActivity.this, responStr);
	// }
	//
	// @Override
	// public void onFinish() {
	// // TODO Auto-generated method stub
	// }
	//
	// });
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	//
	// }

	// private void dealSuccessMessage(String responStr) {
	// CityAndCompanyResponse cityAndCompanyResponse =
	// XStreamUtils.getFromXML(responStr,
	// CityAndCompanyResponse.class);
	// ConstHead constHead = cityAndCompanyResponse.getConstHead();
	// if (null != constHead && "00".equals(constHead.getErrCode())) {
	// citys = cityAndCompanyResponse.getCityAndCompany().getCity();
	// } else {
	// Toast.makeText(this, "请求数据失败", Toast.LENGTH_SHORT).show();
	// }
	//
	// }

	@OnClick({ R.id.iv_imageLeft, R.id.btnOk, R.id.btnInputAgain, R.id.tvGender })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_imageLeft:
			finish();
			break;
		case R.id.btnOk:
			KeyboardUtils.closeInput(this, btnOk);
			String name = etName.getText().toString();
			String gender = tvGender.getText().toString();
			String idCard = etIdentityCard.getText().toString();
			String PhoneNum = etPhoneNum.getText().toString();

			if (CheckoutUtil.isEmpty(name)) {

				ToastUtils.show(this, "请输入姓名", Toast.LENGTH_SHORT);
			} else if (CheckoutUtil.isEmpty(idCard)) {
				ToastUtils.show(this, "请输入身份证号", Toast.LENGTH_SHORT);

			} else if (CheckoutUtil.isEmpty(PhoneNum)) {
				ToastUtils.show(this, "请输入手机号", Toast.LENGTH_SHORT);

			} else if ("请选择".equals(tvGender.getText().toString())) {
				ToastUtils.show(this, "请选择性别", Toast.LENGTH_SHORT);

			} else if (!CheckoutUtil.isMobileNo(this, PhoneNum)) {
				ToastUtils.show(this, "请输入正确的手机号码", Toast.LENGTH_SHORT);

			} else if (!CheckoutUtil.isIdentity(this, idCard.toUpperCase(), true)) {
				ToastUtils.show(this, "请输入正确的身份证号", Toast.LENGTH_SHORT);

			} else if (!checkGender(gender, idCard)) {
				ToastUtils.show(this, "性别选择有误", Toast.LENGTH_SHORT);

			} else if (name.length() <= 1 || name.length() > 20) {
				ToastUtils.show(this, "姓名长度必须是2到20字符", Toast.LENGTH_SHORT);

			} else {
				Intent intent = new Intent(this, WorkUnitInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("NAME", name);
				bundle.putString("GENDER", gender);
				bundle.putString("ID_CARD", idCard);
				bundle.putString("PHONE", PhoneNum);
				intent.putExtras(bundle);
				startActivity(intent);
			}

			break;
		case R.id.btnInputAgain:
			KeyboardUtils.closeInput(this, btnInputAgain);
			clearAllinput();
			break;
		case R.id.tvGender:

			KeyboardUtils.closeInput(this, tvGender);
			final String[] genderStr = { "男", "女" };
			DialogUtil.showToSelect(this, "", genderStr, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					tvGender.setText(genderStr[which]);
				}
			});

			break;
		// case R.id.tvCompany:
		//
		// final String[] companyStr = new String[companies.size()];
		// for (int i = 0; i < companies.size(); i++) {
		// companyStr[i] = companies.get(i).getCompanyName();
		// }
		//
		// DialogUtil.showToSelect(this, "", companyStr, new OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// tvCompany.setText(companyStr[which]);
		// companyId = companies.get(which).getCompanyId();
		// }
		// });
		// KeyboardUtils.closeInput(this, tvCompany);
		// break;

		}
	}

	private boolean checkGender(String gender, String idCard) {
		String genderNum = idCard.substring(16, 17);
		int genNum = Integer.parseInt(genderNum);
		if (genNum % 2 == 0) {
			if ("女".equals(gender)) {
				return true;
			} else {
				return false;
			}
		} else {
			if ("男".equals(gender)) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 清空所有已填写信息
	 */
	private void clearAllinput() {

		etName.setText("");
		tvGender.setText("请选择");
		etIdentityCard.setText("");
		etPhoneNum.setText("");
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
	// private void requestRealNameAuthen(String name, String idCard, String
	// phoneNum) {
	// try {
	// CspXmlYfx003 cspXmlYfx003 = new CspXmlYfx003(LoginUtil.getUserId(this),
	// name, idCard, phoneNum, cityId,
	// etCompany.getText().toString());
	// String strXml = cspXmlYfx003.getCspXml();
	// // 生成MCIS报文
	// Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
	// final byte[] byteMessage = mcis.getMcis();
	// // 发送报文
	// CspUtil cspUtil = new CspUtil(this);
	// cspUtil.setFLAG_YFX_CSP(true);
	// cspUtil.setTxCode("001003");
	// Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
	// cspUtil.postCspLogin(byteMessage, new CallBack() {
	//
	// @Override
	// public void onSuccess(String responStr) {
	// CommonResponse commonResponse = XStreamUtils.getFromXML(responStr,
	// CommonResponse.class);
	// ConstHead constHead = commonResponse.getConstHead();
	// // TODO 测试
	// constHead.setErrCode("00");
	// if (null != constHead && "00".equals(constHead.getErrCode())) {
	// ToastUtils.show(RealNameAuthentActivity.this, "成功", 0);
	// LoanProDetailFragment.STATUS_CHANGE_FLAG = true;
	// RealNameAuthentActivity.this.finish();
	// }
	// }
	//
	// @Override
	// public void onFailure(String responStr) {
	// CspUtil.onFailure(RealNameAuthentActivity.this, responStr);
	// }
	//
	// @Override
	// public void onFinish() {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// });
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	//
	// }

}
