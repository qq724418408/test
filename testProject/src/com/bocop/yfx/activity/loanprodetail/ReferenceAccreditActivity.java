package com.bocop.yfx.activity.loanprodetail;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

/**
 * 征信查询授权书
 * 
 * @author rd
 * 
 */
@ContentView(R.layout.yfx_activity_reference_accredit)
public class ReferenceAccreditActivity extends BaseActivity {
	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.tvRefAccredit)
	private TextView tvRefAccredit;
	@ViewInject(R.id.tvContent1)
	private TextView tvContent1;
	@ViewInject(R.id.tvContent2)
	private TextView tvContent2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tvTitle.setText(getString(R.string.referenceAccre));
		tvContent1.setText(Html.fromHtml("\t\t一、本人同意并不可撤销地授权：<B>贵行按照国家相关规定采集并向金融信用信息基础数据库及其他依法成立的征信机构提供本人个人信息和包括信贷信息在内的信用信息（包含本人因未及时履行合同义务产生的不良信息）。</B>"));
		tvContent2.setText(Html.fromHtml("\t\t二、本人同意并不可撤销地授权：<B>贵行根据国家有关规定，在办理涉及本人的业务时，有权向金融信用信息基础数据库及其他依法成立的征信机构查询、打印、保存本人的信用信息，并用于下述用途：</B>"));
	}

	@OnClick({ R.id.iv_imageLeft, R.id.btnAgreeAccredit })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_imageLeft:
			finish();
			break;

		case R.id.btnAgreeAccredit:
			InfoAccreditActivity.FLAG_REFERENCE_ACCREDIT = true;
			finish();
			break;

		}
	}

}
