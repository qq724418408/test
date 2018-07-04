package com.bocop.jxplatform.trafficassistant;

import java.io.UnsupportedEncodingException;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.CspRecHeaderBean;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.BocopDialog;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.jxplatform.xml.CspRecHeader;
import com.bocop.jxplatform.xml.CspXmlAPJJ09;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author luoyang
 * @version 创建时间：2015-7-7 下午2:00:42 类说明
 */
@ContentView(R.layout.activity_trafficviolation)
public class TrafficViolationActivity extends BaseActivity {
	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	
	@ViewInject(R.id.tv_violation_sequence)
	TextView tvViolationSequence;
	@ViewInject(R.id.tv_violation_time)
	TextView tvViolationTime;
	@ViewInject(R.id.tv_violation_place)
	TextView tvViolationPlace;
	@ViewInject(R.id.tv_violation_act)
	TextView tvViolationAct;
	@ViewInject(R.id.tv_violation_money)
	TextView tvViolationMoney;
	@ViewInject(R.id.tv_violation_score)
	TextView tvViolationScore;
	@ViewInject(R.id.tv_violation_org)
	TextView tvViolationOrg;
	@ViewInject(R.id.tv_violation_license)
	TextView tvViolationLicense;
	@ViewInject(R.id.tv_violation_code)
	TextView tvViolationCode;
	@ViewInject(R.id.tv_violation_licensenum)
	TextView tvViolationLicenseNum;
	@ViewInject(R.id.checkbox)
	CheckBox checkBox;
	@ViewInject(R.id.tv_agreement)
	TextView tvAgreement;
	

	@ViewInject(R.id.bt_violation)
	Button btViolation;
	
	String strViolationSequence;
	String strViolationLicense;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		tv_titleName.setText("办理违法");
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			strViolationSequence = bundle.getString("violationSequence");
			tvViolationSequence.setText(strViolationSequence);
			tvViolationTime.setText(bundle.getString("violationTime"));
			tvViolationPlace.setText(bundle.getString("violationPlace"));
			tvViolationAct.setText(bundle.getString("violationAct"));
			tvViolationMoney.setText(bundle.getString("violationMoney"));
			tvViolationScore.setText(bundle.getString("violationScore"));
			tvViolationOrg.setText(bundle.getString("violationOrg"));
			strViolationLicense = bundle.getString("violationLicenseNum");
			tvViolationLicense.setText(bundle.getString("violationDriveNUm"));
			tvViolationCode.setText(bundle.getString("violationCode"));
			tvViolationLicenseNum.setText(bundle.getString("violationLicenseNum"));
		}

	}

	@OnClick(R.id.bt_violation)
	public void btClick(View view) {
//		requestCspForViolation();
//		if(checkBox.isChecked()){
//			requestCspForViolation();
//		}else{
//			Toast.makeText(TrafficViolationActivity.this, "请您阅读交通简易认罚协议并且同意",
//			Toast.LENGTH_LONG).show();
//		}
		BocopDialog dialog = new BocopDialog(TrafficViolationActivity.this,
				"提示", "根据交管部门的最新要求，申请处理交通违法时，必须提供驾驶证证芯编号（驾驶证条码后六位），如您前期在绑定驾驶证时未输入证芯编号，请到“我的驾驶证”中将驾驶证解绑后重新绑定，否则将无法进行违法处理。 ");
		dialog.setPositiveButton(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				requestCspForViolation();
//				Toast.makeText(TrafficViolationActivity.this, "跳转认罚成功界面",
//						Toast.LENGTH_SHORT).show();
			}
		}, "继续处理");
		dialog.setNegativeButton(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}, "取消");
		dialog.show();
	}
	@OnClick(R.id.tv_agreement)
	public void tvAgreementClick(View view){
		Intent intent = new Intent(TrafficViolationActivity.this, TrafficAgrementActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 请求认罚
	 */

	protected void requestCspForViolation() {
		try {
			// 生成CSP XML报文
			CspXmlAPJJ09 cspXmlAPJJ09 = new CspXmlAPJJ09(strViolationLicense, LoginUtil.getUserId(TrafficViolationActivity.this), strViolationSequence);
			String strXml = cspXmlAPJJ09.getCspXml();
			// 生成MCIS报文
			Mcis mcis = new Mcis(strXml,TransactionValue.APJJ18);
			final byte[] byteMessage = mcis.getMcis();
			// 发送报文
			CspUtil cspUtil = new CspUtil(this);
			//发送报文
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			cspUtil.postCspLogin(byteMessage, new CallBack() {
				
				@Override
				public void onSuccess(String responStr) {
					Log.i("tag", responStr);
					try {
						CspRecHeaderBean cspRecHeaderBean = CspRecHeader.readStringXml(responStr);
						if(cspRecHeaderBean.getErrorcode().equals("00")){
							Toast.makeText(TrafficViolationActivity.this, "您的申请已受理，请稍后在“我的违章”查询罚单信息并于处罚决定书生成后十五日内完成缴费。", Toast.LENGTH_LONG).show();
							setResult(RESULT_OK);
							finish();
						}
						else{
							Toast.makeText(TrafficViolationActivity.this, cspRecHeaderBean.getErrormsg(), Toast.LENGTH_LONG).show();
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					
				}
				
				@Override
				public void onFinish() {
					
				}
				
				@Override
				public void onFailure(String responStr) {
					if(responStr.equals("0")){
						Toast.makeText(TrafficViolationActivity.this, "服务器错误，请稍候再试", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(TrafficViolationActivity.this, responStr, Toast.LENGTH_SHORT).show();
					}
				}
			});
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
