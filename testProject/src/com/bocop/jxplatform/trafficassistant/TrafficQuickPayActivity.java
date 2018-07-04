package com.bocop.jxplatform.trafficassistant;

import java.io.UnsupportedEncodingException;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.PeccancyXmlForPayBean;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.jxplatform.xml.CspRecForQuickPayQuery;
import com.bocop.jxplatform.xml.CspXmlForQuickPayQuery;
import com.bocop.xms.activity.MessageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author luoyang
 * @version 创建时间：2015-6-29 下午9:44:37 类说明
 */

@ContentView(R.layout.activity_trafficquickpay)
public class TrafficQuickPayActivity extends BaseActivity{

	@ViewInject(R.id.tv_titleName)
	private TextView  tv_titleName;
	@ViewInject(R.id.et_penalty_num)
	EditText etPenaltyNumber;
	@ViewInject(R.id.bt_next_quick_pay)
	Button btNextQuickPay;
	
	String strPenaltyNum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tv_titleName.setText("快速缴费");
		initEvent();
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		btNextQuickPay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				strPenaltyNum = etPenaltyNumber.getText().toString().trim();
				if(strPenaltyNum.trim().length() != 16){
					Toast.makeText(TrafficQuickPayActivity.this, "请输入16位处罚确定书编号", Toast.LENGTH_SHORT).show();
				}
				else{
					requestCspForPayBalance();
				}
			}
		});
	}

	protected void requestCspForPayBalance() {
		// TODO Auto-generated method stub
		try {
			//生成CSP XML报文
			CspXmlForQuickPayQuery cspXmlForQuickPayQuery = new CspXmlForQuickPayQuery(strPenaltyNum.trim());
			String strXml = cspXmlForQuickPayQuery.getCspXml();
			//生成MCIS报文
			Mcis mcis = new Mcis(strXml,TransactionValue.EB4034);
			final byte[] byteMessage = mcis.getMcis();
			CspUtil cspUtil = new CspUtil(this);
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			cspUtil.postCspLogin(byteMessage, new CallBack() {
				@Override
				public void onSuccess(String responStr) {
					PeccancyXmlForPayBean peccancyXmlForPayBean = null;
					try {
						 peccancyXmlForPayBean = CspRecForQuickPayQuery.readStringXml(responStr);
						 if(!peccancyXmlForPayBean.getErrorcode().equals("00")){
							 Toast.makeText(TrafficQuickPayActivity.this,peccancyXmlForPayBean.getErrormsg() , Toast.LENGTH_LONG).show();
						 }
						 else{
							 Log.i("tag", "成功接收CSP信息");
								Bundle bundle = new Bundle();
								bundle.putString("peccancyNum", peccancyXmlForPayBean.getPeccancyNum());
								bundle.putString("peccancySum", peccancyXmlForPayBean.getPeccancySum());
								bundle.putString("licenseNum", peccancyXmlForPayBean.getLicenseNum());
								bundle.putString("lateAmt", peccancyXmlForPayBean.getLateAmt());
								bundle.putString("yjAmt", peccancyXmlForPayBean.getYjAmt());
								Intent intent = new Intent(TrafficQuickPayActivity.this, TrafficPayActivity.class);
								intent.putExtras(bundle);
								startActivity(intent);
						 }
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void onFinish() {
//					Intent intent = new Intent(TrafficQuickPayActivity.this, TrafficPayActivity.class);
//					startActivity(intent);
				}
				
				@Override
				public void onFailure(String responStr) {
					Log.i("tag2", "2onFailure");
					CspUtil.onFailure(TrafficQuickPayActivity.this, responStr);
				}
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	
}
