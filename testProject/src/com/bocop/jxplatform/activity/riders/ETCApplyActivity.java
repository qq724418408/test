package com.bocop.jxplatform.activity.riders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.baseUtil.asynchttpclient.JsonHttpResponseHandler;
import com.boc.jx.baseUtil.asynchttpclient.RequestParams;
import com.boc.jx.common.util.ContentUtils;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.http.RestTemplate;
import com.bocop.jxplatform.view.riders.ApplySuccesDialog;

/**
 * ETC申请
 * 
 * @author xmtang
 * 
 */
public class ETCApplyActivity extends Activity {
	private TextView tv_titleName;
	private ImageView iv_title_left;
	private Button btnsq;
	private EditText edt_name;// 姓名
	private EditText edt_phonenum;// 手机
	private EditText edt_carnum;// 车牌
	private RadioButton rd_man;// 男
	private RadioButton rd_woman;// 女
	private CheckBox check_hasbankcard;
	private TextView tv_currentpoint;// 当前网点
	private TextView tv_telphone;// 网点联系电话
	AlertDialog.Builder builder;
	ApplySuccesDialog applySuccessDialog;
	private String orgid;// 网点id
	private String pointname;//网点名称
	private String phone;//网点电话
	ProgressDialog progressDialog;// 对话框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_etcapply);
		orgid = getIntent().getStringExtra("orgid");
		pointname=getIntent().getStringExtra("pointname");
		phone=getIntent().getStringExtra("phone");
		initView();
		setListener();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		tv_titleName = (TextView) findViewById(R.id.tv_titleName);
		tv_titleName.setText("申请");
		btnsq = (Button) findViewById(R.id.tv_sq);
		iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
		edt_name = (EditText) findViewById(R.id.edt_name);
		edt_phonenum = (EditText) findViewById(R.id.edt_phonenum);
		edt_carnum = (EditText) findViewById(R.id.edt_carnum);
		rd_man = (RadioButton) findViewById(R.id.rb_man);
		rd_woman = (RadioButton) findViewById(R.id.rb_woman);
		check_hasbankcard = (CheckBox) findViewById(R.id.check_hasbankcard);
		tv_currentpoint = (TextView) findViewById(R.id.tv_currentpoint);
		tv_telphone = (TextView) findViewById(R.id.tv_telephone);
		tv_currentpoint.setText("当前网点："+pointname);
		tv_telphone.setText(phone);
	}

	/**
	 * 设置监听
	 */
	private void setListener() {
		iv_title_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnsq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (validateForm()) {
					ETCApplyPost();
				}
			}
		});
	}

	/**
	 * 表单核对
	 */
	private boolean validateForm() {
		String name = edt_name.getText().toString();
		String phonenum = edt_phonenum.getText().toString();
		String carnum = edt_carnum.getText().toString();
		if (TextUtils.isEmpty(name)) {
			ContentUtils.showMsg(ETCApplyActivity.this, "姓名不能为空");
			return false;
		}
		if (TextUtils.isEmpty(phonenum)) {
			ContentUtils.showMsg(ETCApplyActivity.this, "手机号不能为空");
			return false;
		} else {
			if (!isMobileNo(phonenum)) {
				ContentUtils.showMsg(ETCApplyActivity.this, "请输入正确的手机号");
				return false;
			}
			
		}
		//车牌域为非必输项
//		if (TextUtils.isEmpty(carnum)) {
//			ContentUtils.showMsg(ETCApplyActivity.this, "车牌号不能为空");
//			return false;
//		}
		return true;
	}

	/**
	 * 描述：手机号格式验证.
	 * 
	 * @param str
	 *            指定的手机号码字符串
	 * @return 是否为手机号码格式:是为true，否则false
	 */
	public static Boolean isMobileNo(String str) {
		Boolean isMobileNo = false;
		try {
			Pattern p = Pattern
					.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(14[0-9])|(17[0-9]))\\d{8}$");
			Matcher m = p.matcher(str);
			isMobileNo = m.matches();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isMobileNo;
	}

	/**
	 * 提交申请
	 */
	private void ETCApplyPost() {
		int isCreditUser = 0;
		String sex = "男";
		RestTemplate restTemplate = new RestTemplate(this);
		RequestParams params = new RequestParams();
		String name = edt_name.getText().toString();
		String phonenum = edt_phonenum.getText().toString();
		String carnum = edt_carnum.getText().toString();
		if (check_hasbankcard.isChecked()) {
			isCreditUser = 1;
		}
		if (rd_man.isChecked()) {
			sex = "男";
		}
		if (rd_woman.isChecked()) {
			sex = "女";
		}
		params.put("name", name);// 姓名
		params.put("sex", sex);// 性别
		params.put("carno", carnum);// 车牌号
		params.put("phone", phonenum);// 手机号
		params.put("orgid", orgid);// 网点id
		params.put("iscredituser", isCreditUser);// 是否使用信用卡
		//http://22.220.13.64:8080/manage/etc/saveInfo
		restTemplate.post("http://123.124.191.179/etc/saveInfo",
				params, new JsonHttpResponseHandler("UTF-8") {
					@Override
					public void onStart() {
						progressDialog = new ProgressDialog(
								ETCApplyActivity.this);
						progressDialog.setMessage("正在申请...");
						progressDialog.setCancelable(true);
						progressDialog.show();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						progressDialog.dismiss();
						System.out.println("提交返回结果======" + response.toString());
						try {
							int status = Integer.parseInt(response
									.getString("errorCode"));
							if (status == 10000) {
								alertSuccessDialog(pointname,phone);
							} else {
								ContentUtils.showMsg(ETCApplyActivity.this,
										response.getString("errorMsg"));
							}
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						if (progressDialog != null) {
							progressDialog.dismiss();
							Toast.makeText(ETCApplyActivity.this,
									"申请失败，请稍后重试!", Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	/**
	 * 申请成功对话框
	 * point 当前网点
	 * phone 电话
	 */
	private void alertSuccessDialog(String point,String phone) {
		applySuccessDialog = new ApplySuccesDialog(this,point,phone);
		applySuccessDialog.setListener(new ApplySuccesDialog.setOkListener() {
			@Override
			public void onOkClick() {
				applySuccessDialog.dismiss();
				finish();
			}
		});
		applySuccessDialog.show();
	}
}
