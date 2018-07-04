package com.bocop.qzt;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;

@ContentView(R.layout.qzt_activity_attention)
public class QztAttentionActivity extends BaseActivity {

	@ViewInject(R.id.tv_titleName)
	private TextView tvTitleName;
	
	@ViewInject(R.id.tv_attention)
	private TextView tvinfo;
	
	String strInfo;
	String countryId;
	String purposeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tvTitleName.setText("注意事项");
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			
			strInfo = bundle.getString("info");
			countryId = bundle.getString("countryId");
			purposeId = bundle.getString("purposeId");
			String strSplit = "\\d、";
			Log.i("tag", strInfo);
			Log.i("tag", strSplit);
			String[] arr = strInfo.split(strSplit);
			Log.i("tag", String.valueOf(arr.length));
			if(arr.length <3){
				strSplit = "\\d.";
				arr = strInfo.split(strSplit);
			}
			Log.i("tag", String.valueOf(arr.length));
			String info = "<h2 align=\"center\"><b>" +  countryId + ">" + purposeId + "</b></h2>";
			info = info +  "<h4 align=\"center\"><b>" +  arr[0] + "</b></h4>";
			for(int i=1;i<arr.length;i++)
			{
				info = info +  "<p>"  + i + "、" + arr[i] + "</p>";
			}
			tvinfo.setText(Html.fromHtml(info));
			
		}
	}

	@OnClick({  R.id.btn_Back })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_Back:
			Intent intent = new Intent(QztAttentionActivity.this, QztFristActivity.class);
			startActivity(intent);
			break;
		}
	}

}
