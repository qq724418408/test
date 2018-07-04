package com.bocop.xfjr.activity;

import java.util.ArrayList;
import java.util.List;

import com.boc.jx.baseUtil.view.annotation.ContentView;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.adapter.recycleradapter.CommonRecyclerAdapter;
import com.bocop.xfjr.adapter.recycleradapter.MultiTypeSupport;
import com.bocop.xfjr.adapter.recycleradapter.RecyclerViewHolder;
import com.bocop.xfjr.base.XfjrBaseActivity;
import com.bocop.xfjr.bean.BusinessDetailBean;
import com.bocop.xfjr.bean.BusinessDetailBean.AuditBean;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.util.FullyLinearLayoutManager;
import com.bocop.xfjr.util.TextUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

@ContentView(R.layout.xfjr_layout_search_result)
public class XFJRSearchResultActivity extends XfjrBaseActivity {
	// private VerticalStepView mSetpview0;
	@ViewInject(R.id.tvHomePage)
	private TextView tvBack;
	@ViewInject(R.id.rvstepView)
	private RecyclerView rvstepView;
	// 名称
	@ViewInject(R.id.tvSearchDetailName)
	private TextView tvSearchDetailName;
	// 申请金额
	@ViewInject(R.id.tvSearchDetailMoney)
	private TextView tvSearchDetailMoney;
	// 放款金额
	@ViewInject(R.id.tvSearchDetailhadMoney)
	private TextView tvSearchDetailhadMoney;
	// 分期类型
	@ViewInject(R.id.tvSearchDetailstyle)
	private TextView tvSearchDetailstyle;
	// 手机号码
	@ViewInject(R.id.tvSearchDetailPhone)
	private TextView tvSearchDetailPhone;
	// 客户尽力名称
	@ViewInject(R.id.tvSearchDetailManager)
	private TextView tvSearchDetailManager;

	private CommonRecyclerAdapter<AuditBean> mAdapter;
	// 必传参数
	private String businessId;
	
	// 是否是客服经理跳转的页面
	private boolean isManager;

//	@Override
//	protected int getLoyoutId() {
//		return R.layout.xfjr_layout_search_result;
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}
	
	public static void startActivity(Context context, String businessId,boolean isManager) {
		Intent intent = new Intent(context, XFJRSearchResultActivity.class);
		intent.putExtra("businessId", businessId);
		intent.putExtra("isManager", isManager);
		context.startActivity(intent);
	}

	@OnClick(R.id.tvHomePage)
	private void OnClick(View view) {
		finish();
	}

	@SuppressLint("NewApi")
//	@Override
	protected void initView() {
		FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(this);
		rvstepView.setLayoutManager(linearLayoutManager);
		try {
			rvstepView.setNestedScrollingEnabled(false);
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

//	@Override
	protected void initData() {
		businessId = getIntent().getStringExtra("businessId");
		isManager = getIntent().getBooleanExtra("isManager", isManager);
		if(XfjrMain.isNet){
			getSearchDetail();
		}else{
			List<AuditBean> audit=new ArrayList<AuditBean>();
			for(int i = 1; i < 5; i++){
				audit.add(new AuditBean("2017-09-11", "测试数据，测试测试测试测试测试测试测试测试测试测试"));
			}
			initRecyclerAdapter(audit);
		}
		
	}

	private void getSearchDetail() {
		HttpRequest.getSearchDetail(this,isManager, businessId, new IHttpCallback<BusinessDetailBean>() {

			@Override
			public void onSuccess(String url, BusinessDetailBean result) {
				bindDetailInfo(result);
				initRecyclerAdapter(result.getAudit());
			}

			@Override
			public void onError(String url, Throwable e) {
				
			}

			@Override
			public void onFinal(String url) {
				
			}
		});
	}

	private void bindDetailInfo(BusinessDetailBean bean) {
		// 申请人
		tvSearchDetailName.setText(bean.getCustomerName());
		// 申请金额
		tvSearchDetailMoney.setText(TextUtil.money$Format(bean.getApplyMoney()));
		// 预审金额
		tvSearchDetailhadMoney.setText(TextUtil.money$Format(bean.getApproveMoney()));
		// 分期类型
		tvSearchDetailstyle.setText(getBusinessType(bean.getProductName(), bean.getPeriodsName()));
		// 手机号码
		tvSearchDetailPhone.setText(bean.getPhoneNum());
		// 管理员名称
		tvSearchDetailManager.setText(bean.getManager());

	}

	private String getBusinessType(String type, String periods) {
		switch (type) {
		case "0":
			type = "汽车分期";
			break;
		case "1":
			type = "汽车快易贷";
			break;
		case "2":
			type = "家装分期";
			break;
		case "3":
			type = "车位分期";
			break;
		case "4":
			type = "旅游分期";
			break;
		case "5":
			type = "3C分期";
			break;
		case "6":
			type = "教育分期";
			break;
		case "7":
			type = "其他小额";
			break;
		default:
			break;
		}
		if(!TextUtils.isEmpty(periods)){
			if(periods.contains("12期一次性还款")||periods.endsWith("期")){
				type = type+"、"+periods;
			}else{
				type = type+"、"+periods+"期";
			}
		}
		return type;
	}

	private void initRecyclerAdapter(final List<AuditBean> audit) {
		mAdapter = new CommonRecyclerAdapter<AuditBean>(this, audit, new MultiTypeSupport<AuditBean>() {

			@Override
			public int getLayoutId(AuditBean item, int position) {
				if (position == 0) {
					return R.layout.xfjr_item_search_result_top;
				} else if (position == audit.size() - 1) {
					return R.layout.xfjr_item_search_result_button;
				}
				return R.layout.xfjr_item_search_result_between;
			}
		}) {

			@Override
			public void convert(RecyclerViewHolder holder, AuditBean itemData, int position) {
				if (position == 0) {
					holder.setText(R.id.tv_top_tips, itemData.getTips());
					holder.setText(R.id.tv_top_time, itemData.getTime());
				} else if (position == audit.size() - 1) {
					holder.setText(R.id.tv_buttom_tips, itemData.getTips());
					holder.setText(R.id.tv_buttom_time, itemData.getTime());
				} else {
					holder.setText(R.id.tv_item_tips, itemData.getTips());
					holder.setText(R.id.tv_item_time, itemData.getTime());
				}

			}
		};
		rvstepView.setAdapter(mAdapter);
	}

}
