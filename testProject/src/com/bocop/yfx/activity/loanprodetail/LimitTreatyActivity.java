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
 * 中国银行贷款合同
 * 
 * @author rd
 * 
 */
@ContentView(R.layout.yfx_activity_limit_treaty)
public class LimitTreatyActivity extends BaseActivity {
	@ViewInject(R.id.tv_titleName)
	private TextView tvTitle;
	@ViewInject(R.id.tvlimitTreaty)
	private TextView tvlimitTreaty;
	@ViewInject(R.id.tvlimitTreatySubTitle4)
	private TextView tvlimitTreatySubTitle4;
	@ViewInject(R.id.tvlimitTreatyContent7)
	private TextView tvlimitTreatyContent7;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tvTitle.setText(getString(R.string.limitTreat));
		tvlimitTreatySubTitle4.setText(Html.fromHtml("<u>中国银行股份有限公司 </u>")); 
		tvlimitTreatyContent7.setText(Html.fromHtml("<B>\t\t借款人自主支付：</B>贷款人将贷款划入借款人在提款申请时指定的个人银行结算账户，由借款人自主支付给符合本合同约定用途的借款人交易对手。<br>\t\t因贷款资金划付产生的费用由借款人承担，除贷款人的过错外，错划、无法划入指定账户产生的法律后果均由借款人承担，不影响其履行本合同项下的所有义务。")); 
	}

	@OnClick({ R.id.iv_imageLeft, R.id.btnAgreeTreaty })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_imageLeft:
			finish();
			break;

		case R.id.btnAgreeTreaty:
			InfoAccreditActivity.FLAG_LIMIT_TREATY = true;
			finish();
			break;

		}
	}
}
