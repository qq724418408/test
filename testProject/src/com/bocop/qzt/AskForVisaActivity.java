package com.bocop.qzt;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.jxplatform.bean.CarInfoBean;
import com.bocop.jxplatform.bean.CspRecHeaderBean;
import com.bocop.jxplatform.config.TransactionValue;
import com.bocop.jxplatform.util.BocOpUtil;
import com.bocop.jxplatform.util.BocOpUtil.CallBackBoc;
import com.bocop.jxplatform.util.BocOpUtilWithoutDia;
import com.bocop.jxplatform.util.BocOpUtilWithoutDia.CallBackBoc2;
import com.bocop.jxplatform.util.CspUtil;
import com.bocop.jxplatform.util.CspUtil.CallBack;
import com.bocop.jxplatform.util.CustomProgressDialog;
import com.bocop.jxplatform.util.JsonUtils;
import com.bocop.jxplatform.util.LoginUtil;
import com.bocop.jxplatform.util.Mcis;
import com.bocop.jxplatform.view.BackButton;
import com.bocop.jxplatform.view.MyProgressBar;
import com.bocop.jxplatform.xml.CspRecHeader;
import com.bocop.jxplatform.xml.CspXmlAPJJ01;
import com.google.gson.Gson;

/** 
 * @author luoyang  
 * @version 创建时间：2015-6-18 下午3:57:31 
 * 添加车辆绑定信息
 */

@ContentView(R.layout.qzt_activity_apply)
public class AskForVisaActivity extends BaseActivity implements OnClickListener{

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
	
	
	
	/** 短信状态 */
	private static final int LAST_TIME = 1;
	private static final int LESS_TIME = 2;
	private static final int FINISH_TIME = 3;
	
	
	String strOwnerName;
	String strIdNo;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LAST_TIME:
				break;
			case LESS_TIME:
				break;
			case FINISH_TIME:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tv_titleName.setText("签证申请");
	}

	@OnClick({R.id.bt_qztone,R.id.bt_qzttwo,R.id.bt_qztthree})
	public void onClick(View v){
		switch(v.getId()){
		case R.id.bt_qztone:
			break;
		case R.id.bt_qzttwo:
			break;
		case R.id.bt_qztthree:
			break;
		}
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
//								strOwnerName = "罗阳";
//								strIdNo = "362202198702140010";
//								requestCspForCarAdd();
//								CustomProgressDialog.showBocRegisterSetDialog(CarAddActivity.this);
							} else {
								CustomProgressDialog.showBocRegisterSetDialog(AskForVisaActivity.this);
//								Toast.makeText(CarAddActivity.this, "请前往中银开发平台实名认证", Toast.LENGTH_LONG).show();
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
						CspUtil.onFailure(AskForVisaActivity.this, responStr);
					}
				});
	}

}
