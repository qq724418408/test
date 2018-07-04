package com.bocop.xms.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.DialogUtil;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.trafficassistant.TrafficAgrementActivity;
import com.bocop.jxplatform.trafficassistant.TrafficViolationActivity;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.xms.bean.AddressDetail;
import com.bocop.xms.bean.AddressList;
import com.bocop.xms.bean.AddressResponse;
import com.bocop.xms.bean.AreaResponse;
import com.bocop.xms.bean.ConstHead;
import com.bocop.xms.bean.SignContractInfo;
import com.bocop.xms.bean.SignResponse;
import com.bocop.xms.bean.UserInfoResponse;
import com.bocop.xms.utils.KeyboardUtils;
import com.bocop.xms.utils.XStreamUtils;
import com.bocop.xms.view.SignContractInfoDialog;
import com.bocop.xms.view.SignContractInfoDialog.NegativeOnClickListener;
import com.bocop.xms.view.SignContractInfoDialog.PositiveOnClickListener;
import com.bocop.xms.xml.CspXmlXms002;
import com.bocop.xms.xml.CspXmlXms006;

@ContentView(R.layout.xms_activity_finance_sign_contract)
public class FinanceSignContractActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	@ViewInject(R.id.iv_imageLeft)
	private ImageView iv_imageLeft;
	@ViewInject(R.id.tv_finagreement)
	TextView tvAgreement;
	@ViewInject(R.id.fincheckbox)
	CheckBox ckFin;
	
	@ViewInject(R.id.btn_FinApply)
	private Button btnApply;
	
	@ViewInject(R.id.tv_CusNo)
	private TextView tv_cusNo;
	@ViewInject(R.id.tv_CusName)
	private TextView tv_cusName;

	private String costType;
	private String typeCode;
	private String cusName;
	private String idNO;
	private String cusNO;
	private SignContractInfo contractInfo = new SignContractInfo();
	private static final int SIGN_SAVE_SUCCESS = 2;
	private static final int SIGN_FAILED = 3;
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SIGN_SAVE_SUCCESS:
				callMe(SignContractCompleteActivity.class);
				break;
			case SIGN_FAILED:
				String responStr = (String) msg.obj;
				CspUtil.onFailure(FinanceSignContractActivity.this, responStr);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String str = getIntent().getBundleExtra("BUNDLE").getString("TITLE");
		if (!TextUtils.isEmpty(str)) {
			tv_titleName.setText(str);
		}
		costType = getIntent().getBundleExtra("BUNDLE").getString("COST_TYPE");
		typeCode = getIntent().getBundleExtra("BUNDLE").getString("TYPE_CODE");
		cusName = getIntent().getBundleExtra("BUNDLE").getString("CUS_NAME");
		idNO = getIntent().getBundleExtra("BUNDLE").getString("ID_NO");
		cusNO = getIntent().getBundleExtra("BUNDLE").getString("CUS_ID");
		tv_cusNo.setText(cusNO);
		tv_cusName.setText(cusName);
	}
	
	@OnClick({R.id.btn_FinApply,R.id.tv_finagreement})
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_FinApply:
//			if (!ckFin.isChecked()) {
//				Toast.makeText(this, "请仔细阅读协议，并勾选", Toast.LENGTH_SHORT).show();
//			}else{
				contractInfo.setUserCode(cusNO);
				contractInfo.setCostType(typeCode);
				contractInfo.setUserId(LoginUtil.getUserId(this));
				contractInfo.setSysId("15");//系统编号 金融资产
				contractInfo.setName(cusName);
				contractInfo.setArea(idNO);
				contractInfo.setUnit("0791");
				contractInfo.setTrnCode("0791");
				contractInfo.setAreaId("079100");
				contractInfo.setSubscriberno("");
				contractInfo.setPinpaiN("");
				contractInfo.setServicetype("");
				contractInfo.setDevTyp("");
				contractInfo.setOrderDate("");
				contractInfo.setServId("");
				requestSignSave(contractInfo);
//			} 
			break;
			
		case R.id.tv_finagreement:
			Intent intent = new Intent(FinanceSignContractActivity.this, FinanceAgrementActivity.class);
			startActivity(intent);
			break;
		}
		
	}
	
	/**
	 * 请求签约保存
	 */
	private void requestSignSave(SignContractInfo contractInfo) {
		try {

			// 生成CSP XML报文
			CspXmlXms002 cspXmlXms002 = new CspXmlXms002(contractInfo.getCostType(), contractInfo.getUserId(),
					contractInfo.getName(), "", contractInfo.getArea(), contractInfo.getUnit(),
					contractInfo.getUserCode(), "09", contractInfo.getSysId(),
					contractInfo.getTrnCode(), contractInfo.getAreaId(), "00010002");
			cspXmlXms002.setServicetype(contractInfo.getServicetype());
			cspXmlXms002.setSubscriberno(contractInfo.getSubscriberno());
			cspXmlXms002.setPinpaiN(contractInfo.getPinpaiN());
			cspXmlXms002.setDevTyp(contractInfo.getDevTyp());
			cspXmlXms002.setServId(contractInfo.getServId());
			Log.i("tag", "setServId");
			String strXml = cspXmlXms002.getCspXml();
			Log.i("tag", "getCspXml");
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml, TransactionValue.CSPSZF);
			Log.i("tag", "Mcis");
			final byte[] byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(this);
//			cspUtil.setTest(true);
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			cspUtil.postCspLogin(byteMessage, new CallBack() {
				@Override
				public void onSuccess(String responStr) {
					SignResponse response = XStreamUtils.getFromXML(responStr, SignResponse.class);
					if ("50".equals(response.getConstHead().getErrCode())) {
						DialogUtil.showWithOneBtn(FinanceSignContractActivity.this, "用户已存在",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								});
					} else {
						mHandler.sendEmptyMessage(SIGN_SAVE_SUCCESS);
					}

				}

				@Override
				public void onFinish() {

				}

				@Override
				public void onFailure(String responStr) {
					Message msg = new Message();
					msg.what = SIGN_FAILED;
					msg.obj = responStr;
					mHandler.sendMessage(msg);
				}
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
