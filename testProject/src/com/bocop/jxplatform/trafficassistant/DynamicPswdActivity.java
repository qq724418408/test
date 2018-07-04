package com.bocop.jxplatform.trafficassistant;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.tools.DialogUtil;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.BocOpUtil;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.yfx.view.SlideSwitch;
import com.bocop.yfx.view.SlideSwitch.SlideListener;
import com.google.gson.Gson;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 动态口令
 * 
 * @author ftl
 *
 */
@ContentView(R.layout.activity_dynamic_pswd)
public class DynamicPswdActivity extends BaseActivity {
	public boolean isBandEtoken;

	@ViewInject(R.id.tv_titleName)
	private TextView tv_titleName;
	@ViewInject(R.id.swDynamicPswd)
	private SlideSwitch swDynamicPswd;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initData();
		requestEtoken();
		
	}

	private void initData() {
		tv_titleName.setText("动态口令");
		swDynamicPswd.setSlideListener(new SlideListener() {

			@Override
			public void open() {
				View view = View.inflate(DynamicPswdActivity.this, R.layout.dialog_dynamic_pswd_bind, null);
				final EditText etDynamicPswd = (EditText) view.findViewById(R.id.etDynamicPswd);
				DialogUtil.showWithView(DynamicPswdActivity.this, view, "绑定", "取消",
						new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String dynamicPswd = etDynamicPswd.getText().toString();
						if ("".equals(dynamicPswd)) {
							Toast.makeText(DynamicPswdActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
							controlDialog(dialog, false);
						} else {
							controlDialog(dialog, true);
							if (getBaseApp().isNetStat()) {
								bindEtoken(dynamicPswd);
							} else {
								swDynamicPswd.setStateNoListener(false);
								CustomProgressDialog.showBocNetworkSetDialog(DynamicPswdActivity.this);
							}
						}
					}
				}, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						controlDialog(dialog, true);
						swDynamicPswd.setStateNoListener(false);
					}
				});
			}

			@Override
			public void close() {
				View view = View.inflate(DynamicPswdActivity.this, R.layout.dialog_dynamic_pswd_untie, null);
				DialogUtil.showWithView(DynamicPswdActivity.this, view, "解绑","取消", 
						new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (getBaseApp().isNetStat()) {
							untieEtoken();
						} else {
							swDynamicPswd.setStateNoListener(true);
							CustomProgressDialog.showBocNetworkSetDialog(DynamicPswdActivity.this);
						}
					}
				}, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						swDynamicPswd.setStateNoListener(true);
					}
				});
			}
		});
	}
	
	/**
	 * 控制对话框
	 * @param dialogInterface
	 * @param flag
	 */
    private void controlDialog(DialogInterface dialogInterface, boolean flag) {  
        try {  
            Field field = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");  
            field.setAccessible(true);  
            field.set(dialogInterface, flag);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  

	/**
	 * 查询Etoken
	 */
	private void requestEtoken() {
		final Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", LoginUtil.getUserId(this));
		final String strGson = gson.toJson(map);

		BocOpUtil bocOpUtil = new BocOpUtil(this);
		bocOpUtil.postOpbocNoDialog(strGson, TransactionValue.QUERY_ETOKEN, new BocOpUtil.CallBackBoc() {

			@Override
			public void onSuccess(String responStr) {
				// 已开启动态口令
				swDynamicPswd.setStateNoListener(true);
				isBandEtoken = true;
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
				// 未开启动态口令
				swDynamicPswd.setStateNoListener(false);
				isBandEtoken = false;
			}
		});
	}

	/**
	 * 绑定Etoken
	 * 
	 * @param tokenSerial
	 */
	private void bindEtoken(String tokenSerial) {
		final Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", LoginUtil.getUserId(this));
		map.put("tokenSerial", tokenSerial);
		final String strGson = gson.toJson(map);

		BocOpUtil bocOpUtil = new BocOpUtil(this);
		bocOpUtil.postOpbocNoDialog(strGson, TransactionValue.BIND_ETOKEN, new BocOpUtil.CallBackBoc() {

			@Override
			public void onSuccess(String responStr) {
				// 绑定成功
				Toast.makeText(DynamicPswdActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
				isBandEtoken = true;
			}

			@Override
			public void onStart() {
				Log.i("tag", "发送GSON数据：" + strGson);
			}

			@Override
			public void onFinish() {
				setSwDynamicShow();
			}

			@Override
			public void onFailure(String responStr) {
				// 绑定失败
				swDynamicPswd.setStateNoListener(false);
			}
		});
	}

	/**
	 * 解绑Etoken
	 */
	private void untieEtoken() {
		final Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", LoginUtil.getUserId(this));
		final String strGson = gson.toJson(map);

		BocOpUtil bocOpUtil = new BocOpUtil(this);
		bocOpUtil.postOpbocNoDialog(strGson, TransactionValue.UNTIE_ETOKEN, new BocOpUtil.CallBackBoc() {

			@Override
			public void onSuccess(String responStr) {
				// 解绑成功
				Toast.makeText(DynamicPswdActivity.this, "解绑成功", Toast.LENGTH_SHORT).show();
				isBandEtoken = false;
			}

			@Override
			public void onStart() {
				Log.i("tag", "发送GSON数据：" + strGson);
			}

			@Override
			public void onFinish() {
				
				setSwDynamicShow();
			}

			@Override
			public void onFailure(String responStr) {
				// 解绑失败
				swDynamicPswd.setStateNoListener(true);
			}
		});
	}
	
	
	/**
	 * 解绑Etoken
	 */
	private void setSwDynamicShow() {
		
		if(isBandEtoken){
			swDynamicPswd.setStateNoListener(true);
		}else {
			swDynamicPswd.setStateNoListener(false);
		}
		
	}
}
