package com.bocop.qzt;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.constants.Constants;
import com.boc.jx.tools.DialogUtil;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.BocSdkConfig;
import com.bocop.jxplatform.util.IDCard;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.QztRequestWithJsonAndHead;
import com.bocop.jxplatform.util.RegularCheck;
import com.bocop.jxplatform.view.BackButton;
import com.google.gson.Gson;

@ContentView(R.layout.qzt_activity_contactinfo)
public class QztContactInfoActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	@ViewInject(R.id.iv_imageLeft)
	private BackButton backBtn;
	@ViewInject(R.id.tv_titleItem)
	private TextView tv_titleRight;
	@ViewInject(R.id.bt_add_contactInfo)
	private Button btAddContactInfo;

	@ViewInject(R.id.ed_qzt_name)
	EditText edqztName;
	@ViewInject(R.id.ed_qzt_phone)
	EditText edqztPhone;
	@ViewInject(R.id.ed_qzt_cardId)
	EditText edqztCardId;
	@ViewInject(R.id.ed_qzt_mail)
	EditText edqztMail;
	@ViewInject(R.id.ed_qzt_address)
	EditText edqztAddress;

	String strCardId;
	String strName;
	String strPhone;
	String strMail;
	String strAddress;
	String flag;   //有参数，则为新增地址。

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tv_titleName.setText("新增常用联系人");
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null && bundle.getString("flag") == null) {
				tv_titleName.setText("编辑常用联系人");
				tv_titleRight.setVisibility(View.VISIBLE);
				tv_titleRight.setText("删除");
				edqztPhone.setText(bundle.getString("phone"));
				edqztMail.setText(bundle.getString("mail"));
				edqztName.setText(bundle.getString("name"));
				edqztCardId.setText(bundle.getString("cardId"));
				edqztAddress.setText(bundle.getString("address"));
				strCardId = bundle.getString("cardId");
			}else if (bundle != null && bundle.getString("flag").equals("add")){
				flag =  bundle.getString("flag");
				tv_titleName.setText("新增常用联系人");
			}
		} else {
			tv_titleName.setText("新增常用联系人");
		}

	}

	private void deleteContactInfo() {
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();

		map.put("user_id", LoginUtil.getUserId(QztContactInfoActivity.this));
		map.put("cardId", strCardId);

		final String strGson = gson.toJson(map);
		Log.i("tag", strGson);
		QztRequestWithJsonAndHead qztRequestWithJsonAndHead = new QztRequestWithJsonAndHead(
				QztContactInfoActivity.this);
		qztRequestWithJsonAndHead
				.postOpboc(
						strGson,
						BocSdkConfig.qztContactDelete,
						new com.bocop.jxplatform.util.QztRequestWithJsonAndHead.CallBackBoc() {
							@Override
							public void onSuccess(String responStr) {
								Log.i("tag22", responStr);
								Log.i("tag", "已经删除该地址");
								Toast.makeText(QztContactInfoActivity.this, "已经删除该地址", Toast.LENGTH_SHORT).show();
//								Intent intent = new Intent(QztContactInfoActivity.this,QztContactActivity.class);
//								startActivity(intent);
								setResult(RESULT_OK);
								finish();

							}

							@Override
							public void onStart() {
							}

							@Override
							public void onFailure(String responStr) {
								Log.i("tag33", responStr);
								Toast.makeText(QztContactInfoActivity.this,responStr, Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onFinish() {
							}
						});
	}

	@OnClick(R.id.tv_titleItem)
	public void tvDeleteContactInfo(View v) {

		DialogUtil.showWithTwoBtn(this, "提示", "是否删除该联系人", "确定", "取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteContactInfo();
					}
				}, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

	}

	@OnClick(R.id.bt_add_contactInfo)
	public void addContactInfo(View v) {
		strCardId = edqztCardId.getText().toString().trim();
		strName = edqztName.getText().toString().trim();
		strPhone = edqztPhone.getText().toString().trim();
		strMail = edqztMail.getText().toString().trim();
		strAddress = edqztAddress.getText().toString().trim();

		if (!RegularCheck.isName(strName)) {
			Toast.makeText(this, "请输入正确的姓名", Toast.LENGTH_SHORT).show();
			return;
		}
		IDCard cc = new IDCard();
		String strError = cc.IDCardValidate(strCardId);

		if (strError.trim().length() > 0) {
			Toast.makeText(this, strError, Toast.LENGTH_SHORT).show();
			return;
		}
		if (!RegularCheck.isEmail(strMail)) {
			Toast.makeText(this, "请输入正确的邮箱", Toast.LENGTH_SHORT).show();
			return;
		}

		if (!RegularCheck.isMobile(strPhone)) {
			Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
			return;
		}
		if (strAddress.length() < 10) {
			Toast.makeText(this, "请输入正确的收件地址", Toast.LENGTH_SHORT).show();
			return;
		}

		addContactInfo();
	}

	private void addContactInfo() {
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();

		map.put("user_Id", LoginUtil.getUserId(QztContactInfoActivity.this));
		map.put("cardId", strCardId);
		map.put("name", strName);
		map.put("phone", strPhone);
		map.put("mail", strMail);
		map.put("address", strAddress);

		final String strGson = gson.toJson(map);
		Log.i("tag", strGson);
		QztRequestWithJsonAndHead qztRequestWithJsonAndHead = new QztRequestWithJsonAndHead(
				QztContactInfoActivity.this);
		qztRequestWithJsonAndHead
				.postOpboc(
						strGson,
						BocSdkConfig.qztContactAdd,
						new com.bocop.jxplatform.util.QztRequestWithJsonAndHead.CallBackBoc() {
							@Override
							public void onSuccess(String responStr) {
								Log.i("tag22", responStr);
								Log.i("tag", "已经成功添加该地址");
								if(flag == null){
									Toast.makeText(QztContactInfoActivity.this, "已经成功修改该地址", Toast.LENGTH_SHORT).show();
								}else{
									Toast.makeText(QztContactInfoActivity.this, "已经成功添加该地址", Toast.LENGTH_SHORT).show();
								}
								

								setResult(RESULT_OK);
								finish();

							}

							@Override
							public void onStart() {
							}

							@Override
							public void onFailure(String responStr) {
								Log.i("tag33", responStr);
								Toast.makeText(QztContactInfoActivity.this,
										responStr, Toast.LENGTH_SHORT).show();
								// setResult(RESULT_OK);
								// finish();

							}

							@Override
							public void onFinish() {
							}
						});
	}

}
