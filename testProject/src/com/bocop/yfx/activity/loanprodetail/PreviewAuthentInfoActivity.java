package com.bocop.yfx.activity.loanprodetail;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.DialogUtil;
import com.boc.jx.view.LoadingView;
import com.boc.jx.view.LoadingView.OnRetryListener;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.xms.bean.ConstHead;
import com.bocop.xms.utils.KeyboardUtils;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.yfx.bean.CommonResponse;
import com.bocop.yfx.bean.LinkManInfo;
import com.bocop.yfx.bean.PersonalInfo;
import com.bocop.yfx.bean.PersonalInfoResponse;
import com.bocop.yfx.utils.CheckoutUtil;
import com.bocop.yfx.utils.ToastUtils;
import com.bocop.yfx.xml.CspXmlYfx004;
import com.bocop.yfx.xml.CspXmlYfx005;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 预览认证资料
 * 
 * @author rd
 * 
 */
@ContentView(R.layout.yfx_activity_preview_authent_info)
public class PreviewAuthentInfoActivity extends BaseActivity {
	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.tvName)
	private TextView tvName;
	@ViewInject(R.id.tvGender)
	private TextView tvGender;
	@ViewInject(R.id.tvCompanyPhone)
	private TextView tvCompanyPhone;
	@ViewInject(R.id.tvIdentityCard)
	private TextView tvIdentityCard;
	@ViewInject(R.id.tvCity)
	private TextView tvCity;
	@ViewInject(R.id.tvCompany)
	private TextView tvCompany;
	@ViewInject(R.id.tvDetailAdd)
	private TextView tvDetailAdd;
	@ViewInject(R.id.etDetailAdd)
	private EditText etDetailAdd;
	@ViewInject(R.id.btnEnsureUpdate)
	private Button btnEnsureUpdate;
	@ViewInject(R.id.btnCancelUpdate)
	private Button btnCancelUpdate;

	@ViewInject(R.id.tvMyPhoneNum)
	private TextView tvMyPhoneNum;// 手机号
	@ViewInject(R.id.etMyPhoneNum)
	private EditText etMyPhoneNum;
	@ViewInject(R.id.tvFamilyName)
	private TextView tvFamilyName;// 家人姓名
	@ViewInject(R.id.tvFamilyNum)
	private TextView tvFamilyNum;// 家人电话
	@ViewInject(R.id.tvColleagueName)
	private TextView tvColleagueName;// 同事姓名
	@ViewInject(R.id.tvColleagueNum)
	private TextView tvColleagueNum;// 同事电话
	@ViewInject(R.id.llFriend)
	private LinearLayout llFriend;// 朋友布局
	@ViewInject(R.id.tvFriendName)
	private TextView tvFriendName;// 朋友姓名
	@ViewInject(R.id.tvFriendNum)
	private TextView tvFriendNum;// 朋友电话

	@ViewInject(R.id.etFamilyName)
	private EditText etFamilyName;// 家人姓名
	@ViewInject(R.id.etFamilyNum)
	private EditText etFamilyNum;// 家人手机号
	@ViewInject(R.id.etFriendName)
	private EditText etFriendName;// 朋友姓名
	@ViewInject(R.id.etFriendNum)
	private EditText etFriendNum;// 朋友手机号
	@ViewInject(R.id.etColleagueName)
	private EditText etColleagueName;// 同事姓名
	@ViewInject(R.id.etColleagueNum)
	private EditText etColleagueNum;// 同事手机号

	@ViewInject(R.id.llInfo)
	private LinearLayout llInfo;// 资料按钮框
	@ViewInject(R.id.llUpdate)
	private LinearLayout llUpdate;// 修改按钮框
	@ViewInject(R.id.loadingView)
	private LoadingView loadingView;// 加载失败页面
	@ViewInject(R.id.llContent)
	private LinearLayout llContent;// 信息列表界面
	private PersonalInfo personalInfo;
	private List<LinkManInfo> linkManInfos;// 联系人列表
	private LinkManInfo familyInfo;// 家人信息
	private LinkManInfo friendiInfo;// 朋友信息
	private LinkManInfo colleagueiInfo;// 同事信息
	private String FAMILY = "";// 我的家人
	private String FRIEND = "";// 我的朋友
	private String COLLEAGUE = "";// 我的同事

	private boolean isUpdate = false;// 资料是否编辑
	private boolean hasFriend = false;// 是否有我的朋友

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tvTitle.setText(getString(R.string.previewInfo));
		linkManInfos = new ArrayList<>();
		requestPersonalInfo();// 请求个人信息

	}

	/**
	 * 请求个人信息
	 */
	private void requestPersonalInfo() {
		try {
			CspXmlYfx005 cspXmlYfx005 = new CspXmlYfx005(LoginUtil.getUserId(this));
			String strXml = cspXmlYfx005.getCspXml();
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			final byte[] byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(this);
			cspUtil.setFLAG_YFX_CSP(true);
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			cspUtil.setTxCode("001005");
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
					// TODO Auto-generated method stub

				}

			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 处理成功信息
	 * 
	 * @param responStr
	 */
	private void dealSuccessMessage(String responStr) {
		PersonalInfoResponse personalInfoResponse = XStreamUtils.getFromXML(responStr, PersonalInfoResponse.class);
		ConstHead constHead = personalInfoResponse.getConstHead();
		if (constHead != null) {
			if ("00".equals(constHead.getErrCode())) {
				loadingView.setVisibility(View.GONE);
				personalInfo = personalInfoResponse.getPersonalInfo();
				if (null != personalInfo) {
					llContent.setVisibility(View.VISIBLE);
					// 显示个人信息
					showPersonalInfo();
				} else {
					onGetDataFailure(constHead.getErrMsg());
				}
			} else if ("50".equals(constHead.getErrCode())) {
				DialogUtil.showWithToMain(this, constHead.getErrMsg());
			} else {
				onGetDataFailure(constHead.getErrMsg());
			}
		}
	}

	private void onGetDataFailure(String err_msg) {
		CspUtil.onFailure(this, err_msg);
		loadingView.setVisibility(View.VISIBLE);
		loadingView.setmOnRetryListener(new OnRetryListener() {

			@Override
			public void retry() {
				requestPersonalInfo();

			}
		});
	}

	/**
	 * 显示个人信息
	 */
	protected void showPersonalInfo() {
		tvName.setText(personalInfo.getCutName());
		tvGender.setText(personalInfo.getCustGender());
		tvIdentityCard.setText(personalInfo.getIdCard());
		tvCity.setText(personalInfo.getCityName());
		tvCompany.setText(personalInfo.getCompanyName());
		tvCompanyPhone.setText(personalInfo.getCompanyPhone());
		tvDetailAdd.setText(personalInfo.getAddress());
		tvMyPhoneNum.setText(personalInfo.getPhone());
		List<LinkManInfo> list = personalInfo.getLinkManInfos();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				LinkManInfo linkManInfo = list.get(i);
				if (linkManInfo != null) {
					if (i == 0) {
						FAMILY = linkManInfo.getType();
						tvFamilyName.setText(linkManInfo.getName());
						tvFamilyNum.setText(linkManInfo.getPhone());
					} else if (list.size() == 3 && i == 1) {
						FRIEND = linkManInfo.getType();
						hasFriend = true;
						llFriend.setVisibility(View.VISIBLE);
						tvFriendName.setText(linkManInfo.getName());
						tvFriendNum.setText(linkManInfo.getPhone());
					} else {
						COLLEAGUE = linkManInfo.getType();
						tvColleagueName.setText(linkManInfo.getName());
						tvColleagueNum.setText(linkManInfo.getPhone());
					}
				}
			}
		}
	}

	@OnClick({ R.id.btnConfirmInfo, R.id.iv_imageLeft, R.id.btnUpdateInfo, R.id.btnEnsureUpdate, R.id.btnCancelUpdate })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_imageLeft:
			finish();
			break;
		case R.id.btnConfirmInfo:
			Intent intent = new Intent(PreviewAuthentInfoActivity.this, InfoAccreditActivity.class);
			intent.putExtra("phone", personalInfo.getPhone());
			startActivity(intent);
			break;
		case R.id.btnUpdateInfo:
			updateInfo();// 编辑资料
			break;
		case R.id.btnCancelUpdate:
			KeyboardUtils.closeInput(this, btnCancelUpdate);
			showInfo();// 显示资料
			break;
		case R.id.btnEnsureUpdate:
			KeyboardUtils.closeInput(this, btnEnsureUpdate);
			isUpdate = true;// 确认修改
			String myPhoneNum = etMyPhoneNum.getText().toString();
			String address = etDetailAdd.getText().toString();
			String familyName = etFamilyName.getText().toString();
			String familyPhoneNum = etFamilyNum.getText().toString();
			familyInfo = new LinkManInfo(FAMILY, familyName, familyPhoneNum);

			String colleagueName = etColleagueName.getText().toString();
			String colleaguePhoneNum = etColleagueNum.getText().toString();
			colleagueiInfo = new LinkManInfo(COLLEAGUE, colleagueName, colleaguePhoneNum);

			String freindName = etFriendName.getText().toString();
			String freindPhoneNum = etFriendNum.getText().toString();
			friendiInfo = new LinkManInfo(FRIEND, freindName, freindPhoneNum);

			linkManInfos.clear();
			linkManInfos.add(familyInfo);
			if (hasFriend) {
				linkManInfos.add(friendiInfo);
			}
			linkManInfos.add(colleagueiInfo);
			if (TextUtils.isEmpty(address)) {
				ToastUtils.show(this, "请填写居住地址。", Toast.LENGTH_SHORT);
			} else if (!CheckoutUtil.isMobileNo(myPhoneNum)) {
				ToastUtils.show(this, "请填选正确的手机号码。", Toast.LENGTH_SHORT);
			} else if (TextUtils.isEmpty(familyName)) {
				ToastUtils.show(this, "家人姓名不能为空。", Toast.LENGTH_SHORT);
			} else if (hasFriend && TextUtils.isEmpty(freindName)) {
				ToastUtils.show(this, "朋友姓名不能为空。", Toast.LENGTH_SHORT);
			} else if (TextUtils.isEmpty(colleagueName)) {
				ToastUtils.show(this, "同事姓名不能为空。", Toast.LENGTH_SHORT);
			} else if (!CheckoutUtil.isMobileNo(familyPhoneNum)) {
				ToastUtils.show(this, "请填选正确的家人手机号码。", Toast.LENGTH_SHORT);
			} else if (hasFriend && !CheckoutUtil.isMobileNo(freindPhoneNum)) {
				ToastUtils.show(this, "请填选正确的朋友手机号码。", Toast.LENGTH_SHORT);
			} else if (!CheckoutUtil.isMobileNo(colleaguePhoneNum)) {
				ToastUtils.show(this, "请填选正确的同事手机号码。", Toast.LENGTH_SHORT);
			} else {
				requestAddPersonalInfo(myPhoneNum, address, linkManInfos);// 请求补录个人信息
			}
			break;
		}
	}

	// 显示资料
	private void showInfo() {
		llInfo.setVisibility(View.VISIBLE);
		llUpdate.setVisibility(View.GONE);
		tvFamilyName.setVisibility(View.VISIBLE);
		tvFamilyNum.setVisibility(View.VISIBLE);
		tvColleagueName.setVisibility(View.VISIBLE);
		tvColleagueNum.setVisibility(View.VISIBLE);
		tvFriendName.setVisibility(View.VISIBLE);
		tvFriendNum.setVisibility(View.VISIBLE);
		tvMyPhoneNum.setVisibility(View.VISIBLE);
		tvDetailAdd.setVisibility(View.VISIBLE);

		etFamilyName.setVisibility(View.GONE);
		etFamilyNum.setVisibility(View.GONE);
		etFriendName.setVisibility(View.GONE);
		etFriendNum.setVisibility(View.GONE);
		etColleagueName.setVisibility(View.GONE);
		etColleagueNum.setVisibility(View.GONE);
		etMyPhoneNum.setVisibility(View.GONE);
		etDetailAdd.setVisibility(View.GONE);
		if (isUpdate) {
			tvMyPhoneNum.setText(etMyPhoneNum.getText().toString());
			tvDetailAdd.setText(etDetailAdd.getText().toString());
			tvFamilyName.setText(etFamilyName.getText().toString());
			tvFamilyNum.setText(etFamilyNum.getText().toString());
			tvFriendName.setText(etFriendName.getText().toString());
			tvFriendNum.setText(etFriendNum.getText().toString());
			tvColleagueName.setText(etColleagueName.getText().toString());
			tvColleagueNum.setText(etColleagueNum.getText().toString());
		}

	}

	/**
	 * 编辑资料
	 */
	private void updateInfo() {

		llInfo.setVisibility(View.GONE);
		llUpdate.setVisibility(View.VISIBLE);
		tvFamilyName.setVisibility(View.GONE);
		tvFamilyNum.setVisibility(View.GONE);
		tvColleagueName.setVisibility(View.GONE);
		tvColleagueNum.setVisibility(View.GONE);
		tvFriendName.setVisibility(View.GONE);
		tvFriendNum.setVisibility(View.GONE);
		tvMyPhoneNum.setVisibility(View.GONE);
		tvDetailAdd.setVisibility(View.GONE);

		etFamilyName.setVisibility(View.VISIBLE);
		etFamilyNum.setVisibility(View.VISIBLE);
		etFriendName.setVisibility(View.VISIBLE);
		etFriendNum.setVisibility(View.VISIBLE);
		etColleagueName.setVisibility(View.VISIBLE);
		etColleagueNum.setVisibility(View.VISIBLE);
		etMyPhoneNum.setVisibility(View.VISIBLE);
		etDetailAdd.setVisibility(View.VISIBLE);

		etFamilyName.setText(tvFamilyName.getText().toString());
		etFamilyNum.setText(tvFamilyNum.getText().toString());
		etFriendName.setText(tvFriendName.getText().toString());
		etFriendNum.setText(tvFriendNum.getText().toString());
		etColleagueName.setText(tvColleagueName.getText().toString());
		etColleagueNum.setText(tvColleagueNum.getText().toString());
		etMyPhoneNum.setText(tvMyPhoneNum.getText().toString());
		etDetailAdd.setText(tvDetailAdd.getText().toString());
	}

	/**
	 * 请求补录个人信息
	 * 
	 * @param myPhoneNum
	 * @param address
	 * @param linkManInfos2
	 */
	private void requestAddPersonalInfo(String myPhoneNum, String address, List<LinkManInfo> linkManInfos2) {
		try {
			CspXmlYfx004 cspXmlYfx004 = new CspXmlYfx004(LoginUtil.getUserId(this), myPhoneNum, address,
					linkManInfos2);
			String strXml = cspXmlYfx004.getCspXml();
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			final byte[] byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(this);
			cspUtil.setFLAG_YFX_CSP(true);
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			cspUtil.setTxCode("001004");
			cspUtil.postCspLogin(byteMessage, new CallBack() {

				@Override
				public void onSuccess(String responStr) {
					CommonResponse commonResponse = XStreamUtils.getFromXML(responStr, CommonResponse.class);
					ConstHead constHead = commonResponse.getConstHead();
					if (constHead != null) {
						if ("00".equals(constHead.getErrCode())) {
							showInfo();// 显示资料
							isUpdate = false;
							Toast.makeText(PreviewAuthentInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
						} else if ("50".equals(constHead.getErrCode())) {
							DialogUtil.showWithToMain(PreviewAuthentInfoActivity.this, constHead.getErrMsg());
						} else {
							CspUtil.onFailure(PreviewAuthentInfoActivity.this, constHead.getErrMsg());
						}
					}
				}

				@Override
				public void onFailure(String responStr) {
					CspUtil.onFailure(PreviewAuthentInfoActivity.this, responStr);

				}

				@Override
				public void onFinish() {

				}

			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
