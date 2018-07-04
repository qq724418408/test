package com.bocop.qzt;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.DialogUtil;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.activity.riders.CyhAdvDetailActivity;
import com.bocop.jxplatform.bean.CarInfoBean;
import com.bocop.jxplatform.bean.CspRecHeaderBean;
import com.bocop.jxplatform.bean.QztAttentionBean;
import com.bocop.jxplatform.bean.QztAttentionListBean;
import com.bocop.jxplatform.bean.QztCountry;
import com.bocop.jxplatform.bean.QztCountryDetail;
import com.bocop.jxplatform.bean.QztDocBean;
import com.bocop.jxplatform.bean.QztDocListBean;
import com.bocop.jxplatform.bean.QztHead;
import com.bocop.jxplatform.bean.QztHeadSuper;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.trafficassistant.CarAddActivity;
import com.bocop.jxplatform.trafficassistant.TrafficAssistantMainActivity;
import com.bocop.jxplatform.trafficassistant.TrafficPayActivity;
import com.bocop.jxplatform.trafficassistant.TrafficQuickPayActivity;
import com.bocop.jxplatform.util.BocOpUtil;
import com.bocop.jxplatform.util.BocOpUtil.CallBackBoc;
import com.bocop.jxplatform.util.BocOpUtilWithoutDia;
import com.bocop.jxplatform.util.BocOpUtilWithoutDia.CallBackBoc2;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.JsonUtils;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.LoginUtil.ILoginListener;
import com.bocop.jxplatform.util.ActivityForResultCode;
import com.bocop.jxplatform.util.IDCard;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.jxplatform.util.QztRequest;
import com.bocop.jxplatform.util.QztRequestWithJson;
import com.bocop.jxplatform.util.QztRequestWithJsonAll;
import com.bocop.jxplatform.util.QztRequestWithJsonAndBody;
import com.bocop.jxplatform.util.RegularCheck;
import com.bocop.jxplatform.util.ShuZu;
import com.bocop.jxplatform.view.BackButton;
import com.bocop.jxplatform.view.MyProgressBar;
import com.bocop.jxplatform.xml.CspRecHeader;
import com.bocop.jxplatform.xml.CspXmlAPJJ01;
import com.bocop.xms.activity.SignContractActivity;
import com.bocop.xms.bean.AddressList;
import com.bocop.xms.utils.KeyboardUtils;
import com.google.gson.Gson;

/** 
 * @author luoyang  
 * @version 创建时间：2015-6-18 下午3:57:31 
 * 添加车辆绑定信息
 */

@ContentView(R.layout.qzt_activity_apply)
public class QztApplyActivity extends BaseActivity implements OnClickListener,ILoginListener{

	@ViewInject(R.id.tv_titleName)
	private TextView  tv_titleName;
	@ViewInject(R.id.iv_imageLeft)
	private BackButton backBtn; 
	
	@ViewInject(R.id.bt_qztone)
	private TextView tv_qztone; 
	@ViewInject(R.id.bt_qzttwo)
	private TextView tv_qzttwo; 
	@ViewInject(R.id.bt_qztthree)
	private TextView tv_qztthree; 
	@ViewInject(R.id.tv_contact)
	private TextView tv_contact; 
	
	@ViewInject(R.id.tv_qztcountry)
	private TextView tvQztCountry; 
	@ViewInject(R.id.tv_qztdes)
	private TextView tvQztDes; 
	@ViewInject(R.id.tv_qzttype)
	private TextView tvQztType; 
	@ViewInject(R.id.tv_qzt_comtel)
	private TextView tvQztComTel; 
	@ViewInject(R.id.tv_qzt_comtel1)
	private TextView tvQztComTel1; 
	@ViewInject(R.id.tv_qzt_comtel2)
	private TextView tvQztComTel2;
	
	
	@ViewInject(R.id.tv_qzttel)
	private EditText tvqztTel; 
	@ViewInject(R.id.tv_qztmail)
	private EditText tvqztMail; 
	@ViewInject(R.id.tv_qztname)
	private EditText tvqztName; 
	@ViewInject(R.id.tv_qztid)
	private EditText tvqztId; 
	@ViewInject(R.id.tv_qztadress)
	private EditText tvqztAdress; 
	
	@ViewInject(R.id.rb_card)
	private CheckBox rdCard; 
	
	@ViewInject(R.id.rb_contact)
	private CheckBox rdContact; 
//	rb_card
	
	private String countrysNum;
	private String crodsNum;
	private String pruposesNum;
	private String strQztName;
	private String strQztId;
	private String strQztTel;
	private String strQztMail;
	private String strCard;
	private String strContact;
	private String strQztAdress;
	private List<QztCountryDetail> countrysList = new ArrayList<QztCountryDetail>();
	private List<QztCountryDetail> crodsList = new ArrayList<QztCountryDetail>();
	private List<QztCountryDetail> pruposesList = new ArrayList<QztCountryDetail>();
	
	
	
	
	private static final int REQUEST_FORUSER_SUCCESS = 1;
	private static final int REQUEST_FORUSER_FAIL = 2;
	private static final int APPLY_FORVISA_SUCCESS = 3;
	private static final int APPLY_FORVISA_FAIL = 4;
	private static final int APPLY_FORCOUNTRY_SUCCESS = 5;
	private static final int APPLY_FORCOUNTRY_FAIL = 6;
	
	private String orderNum;
	private String amt;
	
	String strGson;
	
	
	String strOwnerName;
	String strIdNo;
	
	String strPuopose;
	String strCountry;
	String strCrowdId;
	
	String countryId;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_FORUSER_SUCCESS:
				tvqztName.setText(strOwnerName);
//				tvqztName.setEnabled(false);
				tvqztId.setText(strIdNo);
//				tvqztId.setEnabled(false);
				break;
			case APPLY_FORCOUNTRY_SUCCESS:
				QztCountry qztCountry = (QztCountry)msg.obj;
				countrysList = qztCountry.getCountrys();
				crodsList = qztCountry.getCrowds();
				pruposesList = qztCountry.getPruposes();
				if(countrysList != null && countrysList != null && countrysList != null ){
					tvQztCountry.setText(countrysList.get(0).getName());
					countrysNum = countrysList.get(0).getId();
					tvQztDes.setText(pruposesList.get(0).getName());
					pruposesNum = pruposesList.get(0).getId();
					tvQztType.setText(crodsList.get(0).getName());
					crodsNum = crodsList.get(0).getId();
					
					strCountry = countrysList.get(0).getName();
					strPuopose = pruposesList.get(0).getName();
					strCrowdId = crodsList.get(0).getName();
					
					if(countryId != null){
						Log.i("tag", "countryId" + countryId);
						final String[] idString = new String[countrysList.size()];
						final String[] areaString = new String[countrysList.size()];
						int position = 0;
						for (int i = 0; i < areaString.length; i++) {
							areaString[i] = countrysList.get(i).getName();
							idString[i] = countrysList.get(i).getId();
						}
						position = ShuZu.getIndex(countryId, idString);
						
//						position = Arrays.binarySearch(idString, countryId);
						Log.i("tag", "position:" + position);
						tvQztCountry.setText(areaString[position]);
						
						countrysNum = idString[position];
						strCountry = areaString[position];
						Log.i("tag", "countrysNum:" + countrysNum);
						Log.i("tag", "strCountry:" + countrysNum);
					}
					
				}
				else{
					tvQztCountry.setText("数据异常");
					tvQztDes.setText("数据异常");
					tvQztType.setText("数据异常");
					Toast.makeText(QztApplyActivity.this, "连接错误，请稍候再试。", Toast.LENGTH_LONG).show();
				}
				break;
			case APPLY_FORCOUNTRY_FAIL:
				tvQztCountry.setText("数据异常");
				tvQztDes.setText("数据异常");
				tvQztType.setText("数据异常");
				Toast.makeText(QztApplyActivity.this, "连接错误，请稍候再试。", Toast.LENGTH_LONG).show();
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tv_titleName.setText("签证申请");
		
		init();
		//请求姓名，身份证号
		if(LoginUtil.isLog(QztApplyActivity.this)){
			requestBocopForUseridQuery();
		}
		requestBocopForcountry();
	}
	
	private void init(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			countryId = bundle.getString("id");
			Log.i("tag", bundle.getString("id"));
		}
	}

	@OnClick({R.id.bt_qztone,R.id.bt_qzttwo,R.id.bt_qztthree,R.id.tv_qztcountry,R.id.tv_qztdes,R.id.tv_qzttype,R.id.bt_qztapply,R.id.tv_qzt_comtel,R.id.tv_qzt_comtel1,R.id.tv_qzt_comtel2,R.id.tv_contact})
	public void onClick(View v){
		switch(v.getId()){
		case R.id.bt_qztapply:
			if (LoginUtil.isLog(QztApplyActivity.this)) {
				strQztName = tvqztName.getText().toString().trim();
				strQztId = tvqztId.getText().toString().trim();
				strQztTel = tvqztTel.getText().toString().trim();
				strQztMail = tvqztMail.getText().toString().trim();
				strQztAdress = tvqztAdress.getText().toString().trim();
				if(!RegularCheck.isName(strQztName)){
					Toast.makeText(this, "请输入正确的姓名", Toast.LENGTH_SHORT).show();
					return;
				} 
				IDCard cc = new IDCard();
			    String strError = cc.IDCardValidate(strQztId);
			    
				if(strError.trim().length() > 0){
				Toast.makeText(this, strError, Toast.LENGTH_SHORT).show();
				return;
				} 
				if(!RegularCheck.isEmail(strQztMail)){
					Toast.makeText(this, "请输入正确的邮箱", Toast.LENGTH_SHORT).show();
					return;
				} 
				
				if(!RegularCheck.isMobile(strQztTel)){
					Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
					return;
				} 
				if(strQztAdress.length()<6){
					Toast.makeText(this, "请输入正确的收件地址", Toast.LENGTH_SHORT).show();
					return;
				} 
				if(countrysNum == null){
					Toast.makeText(this, "数据异常，请稍候再试", Toast.LENGTH_SHORT).show();
					return;
				} 
				if(rdCard.isChecked()){
					strCard = "1";
				}
				else{
					strCard = "0";
				}
				
				if(rdContact.isChecked()){
					strContact = "1";
				}
				else{
					strContact = "0";
				}
				requestBocopForQztSubmit();
				break;
			} else {
			LoginUtil.authorize(QztApplyActivity.this,QztApplyActivity.this);
			return;
			}
			
			//办签流程
		case R.id.bt_qztone:
			Intent intent = new Intent(QztApplyActivity.this,
					QztProcessActivity.class);
			startActivity(intent);
			break;
		case R.id.bt_qzttwo:
			if(countrysNum == null){
				Toast.makeText(this, "数据异常，请稍候再试", Toast.LENGTH_SHORT).show();
				return;
			} 
			requestBocopForQztAttention();
			break;
		case R.id.bt_qztthree:
			if(countrysNum == null){
				Toast.makeText(this, "数据异常，请稍候再试", Toast.LENGTH_SHORT).show();
				return;
			} 
			requestBocopForQztDocuments();
			break;
		case R.id.tv_qztcountry:
			final String[] areaString = new String[countrysList.size()];
			for (int i = 0; i < areaString.length; i++) {
				areaString[i] = countrysList.get(i).getName();
			}
			KeyboardUtils.closeInput(QztApplyActivity.this, tvQztCountry);
			DialogUtil.showToSelect(QztApplyActivity.this, "", areaString, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					tvQztCountry.setText(areaString[which]);
					countrysNum = countrysList.get(which).getId();
					strCountry = areaString[which];
					Log.i("tag", strCountry);
//					Toast.makeText(QztApplyActivity.this, countrysNum, Toast.LENGTH_SHORT).show();
				}
			});
			break;
		case R.id.tv_qzttype:
			Log.i("tag", "crodsList2:" + crodsList.size());
			final String[] desString = new String[crodsList.size()];
			Log.i("tag", "crodsList:" + crodsList.size());
			for (int i = 0; i < desString.length; i++) {
				desString[i] = crodsList.get(i).getName();
			}
			KeyboardUtils.closeInput(QztApplyActivity.this, tvQztType);
			DialogUtil.showToSelect(QztApplyActivity.this, "", desString, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					tvQztType.setText(desString[which]);
					crodsNum = crodsList.get(which).getId();
					strCrowdId = desString[which];
					
//					Toast.makeText(QztApplyActivity.this, crodsNum, Toast.LENGTH_SHORT).show();
				}
			});
			break;
		case R.id.tv_qztdes:
			final String[] pruposesString = new String[pruposesList.size()];
			for (int i = 0; i < pruposesString.length; i++) {
				pruposesString[i] = pruposesList.get(i).getName();
			}
			KeyboardUtils.closeInput(QztApplyActivity.this, tvQztDes);
			DialogUtil.showToSelect(QztApplyActivity.this, "", pruposesString, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					tvQztDes.setText(pruposesString[which]);
					pruposesNum = pruposesList.get(which).getId();
//					Toast.makeText(QztApplyActivity.this, pruposesNum, Toast.LENGTH_SHORT).show();
					strPuopose = pruposesString[which];
					Log.i("tag", strPuopose);
				}
			});
			break;
		case R.id.tv_qzt_comtel:
			Intent intentTel = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + tvQztComTel.getText().toString()));  
			intentTel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			startActivity(intentTel);
			break;
		case R.id.tv_qzt_comtel1:
			Intent intentTel1 = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + tvQztComTel1.getText().toString()));  
			intentTel1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			startActivity(intentTel1);
			break;
		case R.id.tv_qzt_comtel2:
			Intent intentTel2 = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + tvQztComTel2.getText().toString()));  
			intentTel2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			startActivity(intentTel2);
			break;
			
		case R.id.tv_contact:
			if(LoginUtil.isLog(this)){
				Intent intentContact = new Intent(QztApplyActivity.this,
						QztContactActivity.class);  
				startActivityForResult(intentContact,ActivityForResultCode.CodeForQztContactAdd);
			}else{
				Toast.makeText(this, "亲，请先登陆再添加常用联系人哦。", Toast.LENGTH_SHORT).show();
			}
			
			break;
		}
	}
//	private void requestBocopForQztSubmit(){
//		Gson gson = new Gson();
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("countryId", countrysNum);
//		map.put("purposeId", pruposesNum);
//		map.put("crowdId", crodsNum);
//		map.put("name", strQztName);
//		map.put("cardId", strQztId);
//		map.put("phone", strQztTel);
//		map.put("mail", strQztMail);
//		final String strGson = gson.toJson(map);
//		QztRequestWithJson qztRequestWithJson = new QztRequestWithJson(this);
//		qztRequestWithJson.postOpboc(strGson, BocSdkConfig.qztSubmitUrl, new com.bocop.jxplatform.util.QztRequestWithJson.CallBackBoc() {
//			
//			@Override
//			public void onSuccess(String responStr) {
//				Log.i("tag1", responStr);
//				Intent intent = new Intent(QztApplyActivity.this,
//						QztApplyCompleteActivity.class);
//				startActivity(intent);
//			}
//			
//			@Override
//			public void onStart() {
//				// TODO Auto-generated method stub
//				Log.i("tag", "发送JSON报文" + strGson);
//			}
//			
//			@Override
//			public void onFinish() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onFailure(String responStr) {
//				Toast.makeText(QztApplyActivity.this, responStr, Toast.LENGTH_LONG).show();
//				
//			}
//		});
//	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// 选择常用联系人
		if (resultCode == 0 && requestCode == ActivityForResultCode.CodeForQztContactAdd && data != null) {
			Intent intent = data;
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				tvqztTel.setText(bundle.getString("phone"));
				tvqztMail.setText(bundle.getString("mail"));
				tvqztName.setText(bundle.getString("name"));
				tvqztId.setText(bundle.getString("cardId"));
				tvqztAdress.setText(bundle.getString("address"));
			}
			else{
				Log.i("tag", "bundle is null");
			}
		}
		else{
			Log.i("tag", "requestCode == ActivityForResultCode.CodeForLicenseAdd && resultCode == 0");
		}
	}
	
	
//	@ViewInject(R.id.tv_qzttel)
//	private EditText tvqztTel; 
//	@ViewInject(R.id.tv_qztmail)
//	private EditText tvqztMail; 
//	@ViewInject(R.id.tv_qztname)
//	private EditText tvqztName; 
//	@ViewInject(R.id.tv_qztid)
//	private EditText tvqztId; 
//	@ViewInject(R.id.tv_qztadress)
//	private EditText tvqztAdress; 
	
	private void requestBocopForQztSubmit(){
		Gson gson = new Gson();
		Map<String,String> map = new HashMap<String,String>();
		map.put("countryId", countrysNum);
		map.put("purposeId", pruposesNum);
		map.put("crowdId", crodsNum);
		map.put("name", strQztName);
		map.put("cardId", strQztId);
		map.put("phone", strQztTel);
		map.put("mail", strQztMail);
		map.put("address", strQztAdress);
		map.put("cardFlag", strCard);
		map.put("addContact", strContact);
		map.put("user_Id", LoginUtil.getUserId(QztApplyActivity.this));
//		URLDecoder.decode(tranCode + mstrCsp, "UTF-8");
		strGson = gson.toJson(map);
		String strGsonFir = null;
		try {
			strGsonFir = URLDecoder.decode(gson.toJson(map), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(strGsonFir != null){
			strGson = strGsonFir;
		}
		
		QztRequestWithJsonAndBody qztRequestWithJson = new QztRequestWithJsonAndBody(this);
		qztRequestWithJson.postOpboc(strGson, BocSdkConfig.qztSubmitUrl, new com.bocop.jxplatform.util.QztRequestWithJsonAndBody.CallBackBoc() {
			
			@Override
			public void onSuccess(String responStr) {
				Log.i("tag22", responStr);
				Map<String,String> map;
				try {
					map = JsonUtils.getMapStr(responStr);
					orderNum = map.get("orderNum");
					amt = map.get("amt");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Bundle bundle = new Bundle();
				bundle.putString("orderNum", orderNum);
				bundle.putString("amt", amt);
				bundle.putString("strPuopose", strPuopose);
				bundle.putString("strCountry", strCountry);
				bundle.putString("strCrowdId", strCrowdId);
				bundle.putString("strQztName", strQztName);
				bundle.putString("strQztId", strQztId);
				bundle.putString("strQztTel", strQztTel);
				bundle.putString("strQztMail", strQztMail);
				bundle.putString("strCard", strCard);
				bundle.putString("strContact", strContact);
				bundle.putString("strQztAdress", strQztAdress);
				
				Intent intent = new Intent(QztApplyActivity.this,
						QztOrderSureActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				Log.i("tag", "发送JSON报文" + strGson);
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(String responStr) {
				Toast.makeText(QztApplyActivity.this, responStr, Toast.LENGTH_LONG).show();
				
			}
		});
	}
	
	private void requestBocopForQztAttention(){
		Gson gson = new Gson();
		Map<String,String> map = new HashMap<String,String>();
		map.put("countryId", countrysNum);
		map.put("purposeId", pruposesNum);
		final String strGson = gson.toJson(map);
		QztRequestWithJsonAll qztRequestWithJson = new QztRequestWithJsonAll(this);
		qztRequestWithJson.postOpboc(strGson, BocSdkConfig.qztAttentionUrl, new com.bocop.jxplatform.util.QztRequestWithJsonAll.CallBackBoc() {
			
			@Override
			public void onSuccess(String responStr) {
				Log.i("tagg1", responStr);
				if(responStr.contains("stat") && responStr.contains("00")){
					try {
						QztAttentionListBean qztAttentionListBean;
						qztAttentionListBean = JsonUtils.getObject(responStr, QztAttentionListBean.class);
						if(qztAttentionListBean != null){
							if(qztAttentionListBean.getBody() != null && qztAttentionListBean.getBody().size() > 0){
								QztAttentionBean qztAttentionBean = qztAttentionListBean.getBody().get(0);
								String strCountryId = qztAttentionBean.getCountryId();
								String strInfo = qztAttentionBean.getInfo();
								String strPurposeId = qztAttentionBean.getPurposeId();
								Bundle bundle = new Bundle();
								bundle.putString("countryId", strCountry);
								bundle.putString("info", strInfo);
								bundle.putString("purposeId", strPuopose);
								Intent intent = new Intent(QztApplyActivity.this, QztAttentionActivity.class);
								intent.putExtras(bundle);
								startActivity(intent);
							}else{
							}
						}else{
							QztHeadSuper qztHeadSuper =  JsonUtils.getObject(responStr,QztHeadSuper.class);
							QztHead qztHead  = JsonUtils.getObject(qztHeadSuper.getHead().toString(),QztHead.class);
							Toast.makeText(QztApplyActivity.this, qztHead.getResult(), Toast.LENGTH_SHORT).show();
						}
						
						
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(QztApplyActivity.this, "暂不提供该项服务", Toast.LENGTH_SHORT).show();
					}
					
					return;
				} else{
					try {
						QztHeadSuper qztHeadSuper =  JsonUtils.getObject(responStr,QztHeadSuper.class);
						QztHead qztHead  = JsonUtils.getObject(qztHeadSuper.getHead().toString(),QztHead.class);
						Toast.makeText(QztApplyActivity.this, qztHead.getResult(), Toast.LENGTH_SHORT).show();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				Log.i("tag", "发送JSON报文" + strGson);
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(String responStr) {
				Toast.makeText(QztApplyActivity.this, responStr, Toast.LENGTH_LONG).show();
				
			}
		});
	}
	
	private void requestBocopForQztDocuments(){
		Gson gson = new Gson();
		Map<String,String> map = new HashMap<String,String>();
		map.put("countryId", countrysNum);
		map.put("purposeId", pruposesNum);
		map.put("crowdId", crodsNum);
		final String strGson = gson.toJson(map);
		QztRequestWithJsonAll qztRequestWithJson = new QztRequestWithJsonAll(this);
		qztRequestWithJson.postOpboc(strGson, BocSdkConfig.qztDocumentsUrl, new com.bocop.jxplatform.util.QztRequestWithJsonAll.CallBackBoc() {
			
			@Override
			public void onSuccess(String responStr) {
				Log.i("tagg1", responStr);
				if(responStr.contains("stat") && responStr.contains("01")){
					try {
						QztHeadSuper qztHeadSuper =  JsonUtils.getObject(responStr,QztHeadSuper.class);
						QztHead qztHead  = JsonUtils.getObject(qztHeadSuper.getHead().toString(),QztHead.class);
						Toast.makeText(QztApplyActivity.this, qztHead.getResult(), Toast.LENGTH_SHORT).show();
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(QztApplyActivity.this, "暂不提供该项服务", Toast.LENGTH_SHORT).show();
					}
					
					return;
				}
				try {
					Bundle bundle = new Bundle();
					bundle.putString("responStr", responStr);
					bundle.putString("countryId", strCountry);
					bundle.putString("purposeId", strPuopose);
					bundle.putString("crowdId", strCrowdId);
					Intent intent = new Intent(QztApplyActivity.this, QztDocActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				Log.i("tag", "发送JSON报文" + strGson);
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(String responStr) {
				Toast.makeText(QztApplyActivity.this, responStr, Toast.LENGTH_LONG).show();
				
			}
		});
	}
	private void requestBocopForUseridQuery() {
		// TODO Auto-generated method stub
				Gson gson = new Gson();
				Map<String,String> map = new HashMap<String,String>();
				map.put("USRID", LoginUtil.getUserId(this));
				final String strGson = gson.toJson(map);
				
				BocOpUtil bocOpUtil = new BocOpUtil(this);
				bocOpUtil.postOpboc(strGson,TransactionValue.SA0053, new CallBackBoc() {
					
					@Override
					public void onSuccess(String responStr) {
						Log.i("tag1", responStr);
						try {
							
							Map<String,String> map;
							map = JsonUtils.getMapStr(responStr);
							strOwnerName = map.get("cusname").toString();
							strIdNo = map.get("idno");
							Log.i("tag","名字：" + strOwnerName + "，身份证好：" + strIdNo);
							if (strOwnerName.length()>0 && strIdNo.length()>10) {
								Message msg = new Message();
								msg.what = REQUEST_FORUSER_SUCCESS;
								msg.obj = responStr;
								handler.sendMessage(msg);
								} else {
									Message msg = new Message();
									msg.what = REQUEST_FORUSER_FAIL;
									msg.obj = responStr;
									handler.sendMessage(msg);
							}
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					@Override
					public void onStart() {
						Log.i("tag", "发送GSON数据：" + strGson);
					}
					
					@Override
					public void onFinish() {
						
					}
					
					@Override
					public void onFailure(String responStr) {
						CspUtil.onFailure(QztApplyActivity.this, responStr);
					}
				});
	}
	
			private void requestBocopForcountry() {
				QztRequest qztRequest = new QztRequest(QztApplyActivity.this);
				
				qztRequest.postOpboc(BocSdkConfig.qztApplyurl, new com.bocop.jxplatform.util.QztRequest.CallBackBoc() {
					
					@Override
					public void onSuccess(String responStr) {
						Log.i("tag1", responStr);
						try {
							QztCountry qztCountry;
							qztCountry = JsonUtils.getObject(responStr, QztCountry.class);
							Log.i("tag","body 调用完毕。");
							if (qztCountry != null) {
								Message msg = new Message();
								msg.what = APPLY_FORCOUNTRY_SUCCESS;
								msg.obj = qztCountry;
								handler.sendMessage(msg);
								}
							else {
								Message msg = new Message();
								msg.what = APPLY_FORCOUNTRY_FAIL;
								msg.obj = qztCountry;
								handler.sendMessage(msg);
								}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					@Override
					public void onStart() {
						
					}
					
					@Override
					public void onFinish() {
						
					}
					
					@Override
					public void onFailure(String responStr) {
						CspUtil.onFailure(QztApplyActivity.this, responStr);
					}
				});
	}

			@Override
			public void onLogin() {
				// TODO Auto-generated method stub
				requestBocopForUseridQuery();
				Toast.makeText(QztApplyActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onCancle() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onException() {
				// TODO Auto-generated method stub
				
			}
}
