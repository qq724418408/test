package com.bocop.qzt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
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
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.tools.DialogUtil;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.QztContactBean;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.ActivityForResultCode;
import com.bocop.jxplatform.util.JsonUtils;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.QztRequestWithJsonAndBody;
import com.bocop.jxplatform.view.BackButton;
import com.google.gson.Gson;

@ContentView(R.layout.qzt_activity_contact)
public class QztContactActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	@ViewInject(R.id.iv_imageLeft)
	private BackButton backBtn;
	@ViewInject(R.id.lv_qztcontact)
	ListView qztContactLV;
	
	@ViewInject(R.id.ll_qzt_nocontact)
	private LinearLayout llQztNocontact;
	

	String strId;
	QztContactListAdapter qztContactListAdapter;
	List<QztContactBean> qztContactList;
	List<Map<String, Object>> mapList;

	QztContactBean QztContactBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tv_titleName.setText("常用联系人");
		requestBocopForQztContact();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// 选择常用联系人
		if (resultCode == RESULT_OK) {
			requestBocopForQztContact();
		}
		else{
			Log.i("tag", "requestCode");
		}
	}
	
	@OnClick(R.id.bt_contactorder)
	public void btAddContactInfo(View v) {
		Bundle bundle = new Bundle();
		bundle.putString("flag", "add");
		Intent intent = new Intent(QztContactActivity.this,QztContactInfoActivity.class);
		intent.putExtras(bundle);
		startActivityForResult(intent, ActivityForResultCode.CodeForQztContactAddOne);
	}

	

	private void requestBocopForQztContact() {

		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", LoginUtil.getUserId(QztContactActivity.this));
		final String strGson = gson.toJson(map);
		QztRequestWithJsonAndBody qztRequestWithJsonAndBody = new QztRequestWithJsonAndBody(
				this);
		qztRequestWithJsonAndBody
				.postOpboc(
						strGson,
						BocSdkConfig.qztContactListUrl,
						new com.bocop.jxplatform.util.QztRequestWithJsonAndBody.CallBackBoc() {
							@Override
							public void onSuccess(String responStr) {
								Log.i("tag22", responStr);
								mapList = new ArrayList<Map<String, Object>>();
								qztContactList = new ArrayList<QztContactBean>();
								// List<Map<String, Object>> list = new
								// ArrayList<Map<String, Object>>();
								try {
									// qztOrderListBean =
									// JsonUtils.getObject(responStr,
									// QztOrderListBean.class);
									mapList = JsonUtils.getListMap(responStr);
									// qztOrderListBean.getHead()
									// qztOrderList =
									// qztOrderListBean.getBody();
										llQztNocontact.setVisibility(View.GONE);
										for (int i = 0; i < mapList.size(); i++) {
											QztContactBean qztContactBean = new QztContactBean();
											qztContactBean.setName(mapList.get(i)
													.get("name").toString());
											qztContactBean.setCardId(mapList.get(i)
													.get("cardId").toString());
											qztContactBean.setAddress(mapList
													.get(i).get("address")
													.toString());
											qztContactBean.setMail(mapList.get(i)
													.get("mail").toString());
											qztContactBean.setPhone(mapList.get(i)
													.get("phone").toString());
											qztContactList.add(qztContactBean);
										}
										Log.i("tag22", "list comp");
										qztContactListAdapter = new QztContactListAdapter(
												QztContactActivity.this,
												qztContactList);
										qztContactLV
												.setAdapter(qztContactListAdapter);
									
								} catch (Exception e) {
									e.printStackTrace();
								}

							}

							@Override
							public void onStart() {
								// TODO Auto-generated method stub

							}

							@Override
							public void onFailure(String responStr) {
								Log.i("tag", "qztfailure");
								if(responStr.equals("暂无数据")){
									llQztNocontact.setVisibility(View.VISIBLE);
								}
								qztContactList.clear();
								qztContactListAdapter = new QztContactListAdapter(
										QztContactActivity.this,
										qztContactList);
								qztContactLV
										.setAdapter(qztContactListAdapter);
								Toast.makeText(QztContactActivity.this,
										responStr, Toast.LENGTH_SHORT).show();

							}

							@Override
							public void onFinish() {
								// TODO Auto-generated method stub

							}
						});
	}
}
