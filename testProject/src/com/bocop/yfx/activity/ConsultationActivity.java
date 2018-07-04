package com.bocop.yfx.activity;

import com.boc.jx.base.BaseActivity;
import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.bocop.jxplatform.R;

import android.os.Bundle;
import android.widget.TextView;

@ContentView(R.layout.yfx_activity_cousulation)
public class ConsultationActivity extends BaseActivity{
	@ViewInject(R.id.tvContent)
	private TextView tvContent;
	@ViewInject(R.id.tv_titleName)
	private TextView title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String content="1.哪些客户属于“个贷通”受邀客户？  \n中国银行关爱专属客户、公务员类客户、优质行业的国有企业、事业单位客户、行业内处于领先地位的民营企业客户、全球500强企业及其分支机构。 \n2.贷款用途该如何选择?  \n您需要根据您贷款的实际用途进行选择。贷款用途仅用于个人合法合理的消费支出，借款人不得将贷款用于购房、投资经营和无指定用途的个人支出，不得用于任何法律法规、监管规定、国家政策禁止银行贷款投入的项目、用途，包括基金、理财、股票证劵投资等。";
	    tvContent.setText(content);
	    title.setText("常见问题");
	}

}
