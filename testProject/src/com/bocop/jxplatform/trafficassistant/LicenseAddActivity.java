package com.bocop.jxplatform.trafficassistant;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.bocop.jxplatform.view.BackButton;
import com.bocop.jxplatform.R;

/** 
 * @author luoyang  
 * @version 创建时间：2015-6-19 下午1:51:51 
 * 类说明 
 */
@ContentView(R.layout.activity_trafficlicadd)
public class LicenseAddActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView  tv_titleName;
	@ViewInject(R.id.iv_imageLeft)
	private BackButton backBtn; 
	
	@ViewInject(R.id.eddrivenum_add)
	EditText eddrivenum_add;
	@ViewInject(R.id.edfilenum_add)
	EditText edfilenum_add;
	@ViewInject(R.id.btlicenseadd)
	Button btlicenseadd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
}
