package com.bocop.jxplatform.trafficassistant;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.bocop.jxplatform.adapter.MyExpandableListItemAdapter;
import com.bocop.jxplatform.bean.APJJ08ListXmlBean;
import com.bocop.jxplatform.bean.CarPeccancyBean;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.ActivityForResultCode;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.xml.CspRecForAPJJ08;
import com.bocop.jxplatform.xml.CspXmlAPJJ08;
import com.bocop.jxplatform.R;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.readystatesoftware.viewbadger.BadgeView;

;

/**
 * @author luoyang
 * @version 创建时间：2015-6-26 上午10:11:53 类说明
 */
@ContentView(R.layout.activity_mylist)
public class CarPeccancyActivity extends BaseActivity {
	
	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	
	@ViewInject(R.id.tv_pectitle_licensenum)
	TextView tvPecTitleLicenseNum;
	
	@ViewInject(R.id.activity_mylist_listview)
	ListView carPeccancyLv;
	
	//查询无汽车违章信息时显示
	@ViewInject(R.id.ll_nocar_peccancy)
	View llNoCarPeccancy;
	@ViewInject(R.id.tv_nocar_peccancy)
	TextView tvNoCarPeccancy;
	
	//查询有汽车违章信息时显示
	@ViewInject(R.id.ll_carpeccancy_prompt)
	View llCarPeccancyPrompt;
	@ViewInject(R.id.ll_pectitle_licensenum)
	LinearLayout llPectitleLicensenum;
	private BadgeView mBadgeView;
	
	List<CarPeccancyBean> carPecRequssetDate = new ArrayList<CarPeccancyBean>();

	private static final int INITIAL_DELAY_MILLIS = 500;
	private MyExpandableListItemAdapter mExpandableListItemAdapter;
	AlphaInAnimationAdapter alphaInAnimationAdapter;
	
	String strLicenseNum;
	String strNoCarPeccancy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tv_titleName.setText("车辆违法");
		initView();
		// initDate();
		requestCspForCarPeccancyList1();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == ActivityForResultCode.CodeForCarPeccancy && resultCode == RESULT_OK){
			requestCspForCarPeccancyList1();
		}
	}

	private void initView() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			Log.i("tag", "bundle != null");
			strLicenseNum = bundle.getString("licenseNum");
			tvPecTitleLicenseNum.setText(strLicenseNum);
			strNoCarPeccancy = "您号牌为“" + strLicenseNum + "”的车辆目前没有任何违章记录，请继续保持！";//您目前没有任何违章记录，请继续保持！
			Log.i("tag", strNoCarPeccancy);
		}
	}

	private void setListAdapter() {
		mExpandableListItemAdapter = new MyExpandableListItemAdapter(
				CarPeccancyActivity.this, carPecRequssetDate);
		alphaInAnimationAdapter = new AlphaInAnimationAdapter(
				mExpandableListItemAdapter);
		alphaInAnimationAdapter.setAbsListView(carPeccancyLv);
		assert alphaInAnimationAdapter.getViewAnimator() != null;
		alphaInAnimationAdapter.getViewAnimator().setInitialDelayMillis(
				INITIAL_DELAY_MILLIS);
		Log.d("tag", "setInitialDelayMillis");
		carPeccancyLv.setAdapter(alphaInAnimationAdapter);
	}
	
	
	private void requestCspForCarPeccancyList1(){
		try {
			//生成CSP XML报文
			CspXmlAPJJ08 cspXmlAPJJ08 = new CspXmlAPJJ08(strLicenseNum);
			String strXml = cspXmlAPJJ08.getCspXml();
			//生成MCIS报文
			Mcis mcis = new Mcis(strXml,TransactionValue.APJJ08);
			final byte[] byteMessage = mcis.getMcis();
			//发送报文
			Log.i("tag", "发送报文： " + new String(byteMessage, "GBK"));
			CspUtil cspUtil = new CspUtil(this);
			cspUtil.postCspLogin(byteMessage, new CallBack() {
				
				@Override
				public void onSuccess(String responStr) {
					Log.i("tag", "onSuccess");
					APJJ08ListXmlBean listBean = CspRecForAPJJ08.readStringXml(responStr);
					Log.i("tag",listBean.getErrormsg() + "ab" + listBean.getErrorcode());
					if(!listBean.getErrorcode().equals("00")){
						Log.i("tag", "!00");
						if(listBean.getErrorcode().equals("11")){		//没有违法记录
							llPectitleLicensenum.setVisibility(View.GONE);
							llCarPeccancyPrompt.setVisibility(View.GONE);
							llNoCarPeccancy.setVisibility(View.VISIBLE);
							tvNoCarPeccancy.setText(strNoCarPeccancy);
//							tvNoCarPeccancy.setText("");
						}else{
							if(listBean.getErrormsg().length() > 0){
								Toast.makeText(CarPeccancyActivity.this,listBean.getErrormsg(), Toast.LENGTH_SHORT).show();
							}else {
								Toast.makeText(CarPeccancyActivity.this,"虽然很努力，但貌似没查询到任何东东", Toast.LENGTH_SHORT).show();
							}
						}
						
					}
					else{
						Log.i("tag", "!11");
						if(listBean.getCarPeccancyListBean().size() > 0){
							Log.i("tag",String.valueOf( listBean.getCarPeccancyListBean().size()));
							llPectitleLicensenum.setVisibility(View.VISIBLE);
							llCarPeccancyPrompt.setVisibility(View.VISIBLE);
							llNoCarPeccancy.setVisibility(View.GONE);
							if (mBadgeView != null)
							{
								llPectitleLicensenum.removeView(mBadgeView);
							}
							Log.i("tag", "mBadgeView");
							carPecRequssetDate = listBean.getCarPeccancyListBean();
							mBadgeView = new BadgeView(CarPeccancyActivity.this);
							mBadgeView.setText(String.valueOf(carPecRequssetDate.size()));
							llPectitleLicensenum.addView(mBadgeView);
							Log.i("tag", "initCarDates");
							setListAdapter();
						}else{
							llPectitleLicensenum.setVisibility(View.GONE);
							llCarPeccancyPrompt.setVisibility(View.GONE);
							llNoCarPeccancy.setVisibility(View.VISIBLE);
							Log.i("tag", "strNoCarPeccancy");
							tvNoCarPeccancy.setText(strNoCarPeccancy);
						}
						
					}
				}
				
				@Override
				public void onFinish() {
				}
				
				@Override
				public void onFailure(String responStr) {
					if(responStr.length() > 0){
						if(responStr.equals("0")){
							Toast.makeText(CarPeccancyActivity.this, R.string.onFailure, Toast.LENGTH_SHORT).show();
						}else if(responStr.equals("back")){
							getActivityManager().backFinish();
						}
						else{
							tvNoCarPeccancy.setText(strNoCarPeccancy);
							llPectitleLicensenum.setVisibility(View.INVISIBLE);
							llCarPeccancyPrompt.setVisibility(View.INVISIBLE);
							llNoCarPeccancy.setVisibility(View.GONE);
						}
//						Toast.makeText(CarPeccancyActivity.this,responStr, Toast.LENGTH_SHORT).show();
						
					}else {
						Toast.makeText(CarPeccancyActivity.this,"虽然很努力，但貌似没查询到任何东东", Toast.LENGTH_SHORT).show();
					}
				}
			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
