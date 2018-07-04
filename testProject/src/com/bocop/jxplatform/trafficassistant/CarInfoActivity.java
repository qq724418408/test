package com.bocop.jxplatform.trafficassistant;

import java.io.UnsupportedEncodingException;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.CarInfoBean;
import com.bocop.jxplatform.bean.CspRecHeaderBean;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.jxplatform.view.BackButton;
import com.bocop.jxplatform.xml.CspRecForCarInfo;
import com.bocop.jxplatform.xml.CspRecHeader;
import com.bocop.jxplatform.xml.CspXmlAPJJ03;
import com.bocop.jxplatform.xml.CspXmlAPJJ04;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.activity_trafficcarinfo)
public class CarInfoActivity extends BaseActivity {
	
	@ViewInject(R.id.tv_titleName)
	private TextView  tv_titleName;
	@ViewInject(R.id.iv_imageLeft)
	private BackButton backBtn; 
	
	@ViewInject(R.id.tvlicensenum_query)
	TextView tvln;
	@ViewInject(R.id.tvownername_query)
	TextView tvon;
	@ViewInject(R.id.tvlicensetype_query)
	TextView tvlt;
	@ViewInject(R.id.tvtel_query)
	TextView tvtel;
	@ViewInject(R.id.tvvehiclenum_query)
	TextView tvvn;
	@ViewInject(R.id.tvstate_query)
	TextView tvState;
	@ViewInject(R.id.tvnanl_query)
	TextView tvannual;
	@ViewInject(R.id.llt_carinfo)
	LinearLayout lltCarInfo;

	String strLicenseNum; //接收INTENT传过来的车牌号
	CarInfoBean carInfoBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tv_titleName.setText("车辆详细信息");
		initView();
		requestCspForCarInfo();
	}

	private void requestCspForCarInfo() {
		try {
			//生成CSP XML报文
			CspXmlAPJJ03 cspXmlAPJJ03 = new CspXmlAPJJ03(strLicenseNum);
			String strXml = cspXmlAPJJ03.getCspXml();
			//生成MCIS报文
			Mcis mcis = new Mcis(strXml,TransactionValue.APJJ03);
			final byte[] byteMessage = mcis.getMcis();
			//发送报文
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			CspUtil cspUtil = new CspUtil(this);
			cspUtil.postCspLogin(byteMessage, new CallBack() {
				
				@Override
				public void onSuccess(String responStr) {
					try {
						carInfoBean = CspRecForCarInfo.readStringXml(responStr);
						if(!carInfoBean.getErrorcode().equals("00")){
							Toast.makeText(CarInfoActivity.this,carInfoBean.getErrormsg(), Toast.LENGTH_SHORT).show();
						}
						else{
							Log.i("tag", "initCarDates");
							lltCarInfo.setVisibility(View.VISIBLE);
							setCarInfoText();
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
						Toast.makeText(CarInfoActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
					}else if(responStr.equals("back")){
						getActivityManager().backFinish();
					}
					else{
						Toast.makeText(CarInfoActivity.this, responStr, Toast.LENGTH_SHORT).show();
					}
				}
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			strLicenseNum = bundle.getString("licenseNum");
			tvln.setText(strLicenseNum);
		}
	}

	private void setCarInfoText() {
		tvln.setText(carInfoBean.getLicenseNumber());
		tvon.setText(carInfoBean.getOwnerName());
		tvlt.setText(carInfoBean.getLicenseType());
		tvtel.setText(carInfoBean.getTel());
		tvState.setText(carInfoBean.getState());
		tvannual.setText(carInfoBean.getAnnualDate());
//		tvvn.setText(carInfoBean.getVehicleNum());
	}
	
	@OnClick(R.id.btcargo)
	public void btCargo(View v){
		requestCspForCarGo();
	}

	private void requestCspForCarGo() {
		try {
			//生成CSP XML报文
			CspXmlAPJJ04 cspXmlAPJJ04 = new CspXmlAPJJ04(tvln.getText().toString());
			String strXml = cspXmlAPJJ04.getCspXml();
			//生成MCIS报文
			Mcis mcis = new Mcis(strXml,TransactionValue.APJJ04);
			final byte[] byteMessage = mcis.getMcis();
			//发送报文
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			CspUtil cspUtil = new CspUtil(this);
			cspUtil.postCspLogin(byteMessage, new CallBack() {
				
				@Override
				public void onSuccess(String responStr) {
					Log.i("tag", responStr);
					try {
						CspRecHeaderBean cspRecHeaderBean = CspRecHeader.readStringXml(responStr);
						if(cspRecHeaderBean.getErrorcode().equals("00")){
							Toast.makeText(CarInfoActivity.this, "解除绑定成功", Toast.LENGTH_LONG).show();
							setResult(RESULT_OK);
							finish();
						}
						else{
							Toast.makeText(CarInfoActivity.this, cspRecHeaderBean.getErrormsg(), Toast.LENGTH_LONG).show();
						}
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				@Override
				public void onFinish() {
					
				}
				
				@Override
				public void onFailure(String responStr) {
					if(responStr.equals("0")){
						Toast.makeText(CarInfoActivity.this, "服务器故障，请稍候再试", Toast.LENGTH_SHORT).show();
					}else if(responStr.equals("back")){
						getActivityManager().backFinish();
					}
					else{
						Toast.makeText(CarInfoActivity.this, responStr, Toast.LENGTH_SHORT).show();
					}
				}
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
