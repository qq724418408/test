package com.bocop.xfjr.adapter;

import java.util.List;

import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.activity.XFJRSearchResultActivity;
import com.bocop.xfjr.activity.customer.XfjrMainActivity;
import com.bocop.xfjr.adapter.recycleradapter.CommonRecyclerAdapter;
import com.bocop.xfjr.adapter.recycleradapter.RecyclerViewHolder;
import com.bocop.xfjr.bean.BusinessBean;
import com.bocop.xfjr.bean.BusinessBean.BusilogListBean;
import com.bocop.xfjr.bean.add.ProductBean;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.constant.XFJRConstant;
import com.bocop.xfjr.util.ScreenUtils;
import com.bocop.xfjr.util.TextUtil;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 客户经理 搜索列表
 * 
 * @author TIAN FENG
 */
public class BusinessListSearchAdapter extends CommonRecyclerAdapter<BusinessBean> {

	public BusinessListSearchAdapter(Context context, List<BusinessBean> data) {
		super(context, data, R.layout.xfjr_item_my_applicaton_list);
	}

	@Override
	public void convert(final RecyclerViewHolder holder, final BusinessBean itemData, int p) {
		// TODO 逻辑未补充
		holder.setViewVisibility(R.id.tvFurtherInfo, View.GONE)// 补充资料按钮去掉
				.setViewVisibility(R.id.vLine, View.GONE)// 对应分割线去掉
				.setViewVisibility(R.id.ivFlag, View.VISIBLE)// 已拒绝 已通过 的图片显示
				.setText(R.id.tvMerchantName, itemData.getMerchantName())// 商户名称
				.setText(R.id.tvCategory, itemData.getProductName())// 业务类型
				.setText(R.id.tvApplyMoney, TextUtil.money$Format(itemData.getApplyMoney()))// 申请金额
				.setText(R.id.tvCustomerName, itemData.getCustomerName())// 
				.setText(R.id.tvBusinessId,  itemData.getBusinessId());// 编号
		// 根据状态显示图片
		if (getImageResourceByStat(itemData.getStat()) == -1) {
			holder.setViewVisibility(R.id.ivFlag, View.GONE);
		} else {
			holder.setViewVisibility(R.id.ivFlag, View.VISIBLE);
			holder.setImageResource(R.id.ivFlag, getImageResourceByStat(itemData.getStat()));
		}
		if (itemData.getStat() == 2 || itemData.getStat() == 3) {
			holder.setViewVisibility(R.id.lltPreAmount, View.VISIBLE)
					.setText(R.id.tvAproveHint, "预审金额")
					.setText(R.id.tvAproveMoney, TextUtil.money$Format(itemData.getApproveMoney()));
		} else if (itemData.getStat() == 4) {
			holder.setViewVisibility(R.id.lltPreAmount, View.VISIBLE)
					.setText(R.id.tvAproveHint, "放款金额")
					.setText(R.id.tvAproveMoney, TextUtil.money$Format(itemData.getLoadMoney()));
		} else {
			holder.setViewVisibility(R.id.lltPreAmount, View.GONE);
		}

		// .setViewVisibility(R.id.tvFurtherInfo, View.GONE)// 补充资料按钮已经去掉
		// 0 待预审跳转补充资料，1 转人工 3待放款跳转补充资料 其他不显示
		if (itemData.getStat() == 0) {
			holder.setViewVisibility(R.id.tvFurtherInfo, View.VISIBLE)// 补充资料按钮去掉
					.setViewVisibility(R.id.vLine, View.VISIBLE)// 对应分割线去掉
					.setText(R.id.tvFurtherInfo, "继续申请");
		} else if (itemData.getStat() == 1 || itemData.getStat() == 3) {
			holder.setViewVisibility(R.id.tvFurtherInfo, View.VISIBLE)// 补充资料按钮去掉
					.setViewVisibility(R.id.vLine, View.VISIBLE)// 对应分割线去掉
					.setText(R.id.tvFurtherInfo, "补充资料");// 补充资料
		} else {
			holder.setViewVisibility(R.id.tvFurtherInfo, View.GONE)// 补充资料按钮去掉
					.setViewVisibility(R.id.vLine, View.GONE);// 对应分割线去掉
		}

		/**
		 * 查看详情页面
		 */
		holder.setOnClick(R.id.tvMore, new OnClickListener() {

			@Override
			public void onClick(View v) {
				XFJRSearchResultActivity.startActivity(v.getContext(), itemData.getBusinessId(),
						true);
			}
		}) 
		/*
		 * 点击继续申请根据状态跳转
		 */
		.setOnClick(R.id.tvFurtherInfo, new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (itemData.getStat() == 0) {// 继续申请
					onContinueApply(holder, itemData);
				} else if (itemData.getStat() == 1 || itemData.getStat() == 3) {// 补充资料
					// 补充资料
					ProductBean productBean = new ProductBean();
					productBean.setFrom(2);
					productBean.setPeriods(itemData.getPeriods());
					addInformation(v.getContext(), itemData, productBean);
				}
			}
		});
		// 补充资料是否已经提交过资料
		boolean fileCommitted = getFileCommitted(itemData);

		TextView fileSubmit = holder.getView(R.id.tvFurtherInfo);
		// 已经提交不可点击
		if (fileCommitted) {
			fileSubmit.setTextColor(
					fileSubmit.getContext().getResources().getColor(R.color.xfjr_gray));
			fileSubmit.setEnabled(false);
		} else { // 未提交可以点击
			fileSubmit.setTextColor(
					fileSubmit.getContext().getResources().getColor(R.color.xfjr_red));
			fileSubmit.setEnabled(true);
		}
		if(getItemCount() - 1 == p){
			RecyclerView.LayoutParams params = (LayoutParams) holder.itemView.getLayoutParams();
			params.bottomMargin = ScreenUtils.px2dip(mContext, 60);
			holder.itemView.setLayoutParams(params);
			
		}
	}

	/**
	 * 补充资料可点击时的跳转
	 */
	protected void addInformation(Context context, BusinessBean itemData, ProductBean productBean) {
		// 根据状态算
		int type = itemData.getStat();
		XfjrMain.businessStatus = type + ""; // 1或3
		XfjrMainActivity.callMe(context, productBean, XFJRConstant.GO_TO_BCZL); // 补充资料
	}

	/**
	 * 根据补充资料是否已上传文件判断按钮是否可以点击
	 * 
	 * @param bean
	 * @return
	 */
	private boolean getFileCommitted(BusinessBean bean) {
		int type = bean.getStat();
		switch (type) {
		case 1:
			String financeCertify = bean.getFinanceCertify();
			String incomeCertify = bean.getIncomeCertify();
			if (financeCertify.equals("Y") && incomeCertify.equals("Y")) {
				return true;
			} else if (financeCertify.equals("N") || incomeCertify.equals("N")) {
				return false;
			}
			break;

		case 3:
			String instalmentApply = bean.getInstalmentApply();
			if (instalmentApply.equals("Y")) { // 已上传分期申请书
				return true;
			} else if (instalmentApply.equals("N")) { // 未上传分期申请书
				return false;
			}
			break;
		}
		return false;
	}

	/**
	 * 继续申请 TODO
	 */
	private void onContinueApply(final RecyclerViewHolder holder, final BusinessBean itemData) {
		HttpRequest.reqBusinessStatus(holder.itemView.getContext(), itemData.getMerchantId(),
				itemData.getChannelId(), itemData.getProductId(), new IHttpCallback<ProductBean>() {
					@Override
					public void onSuccess(String url, ProductBean result) {
						result.setFrom(-1);
						result.setTelephone(itemData.getTelephone());
						result.setUserName(itemData.getCustomerName());
						XfjrMainActivity.callMe(holder.itemView.getContext(), result,
								getStepStatus(itemData, result));
					}

					@Override
					public void onError(String url, Throwable e) {
						UrlConfig.showErrorTips(holder.itemView.getContext(), e, true);
						LogUtils.e("通过商户编号、渠道编号和产品编号获取欺诈侦测规则和授信模型：" + e.getMessage());
					}

					@Override
					public void onFinal(String url) {

					}
				});
	}

	/**
	 * 返回继续申请要走的步骤
	 * 
	 * @param bean
	 * @return
	 */
	private int getStepStatus(BusinessBean itemData, ProductBean result) {
		// 08;补充资料模块 、09:业务状态认为变更模块
		// 0, 通过 1, 待审批 2, 拒绝 3, 验证中
		int stepStatus = 1;
		List<BusilogListBean> busilogList = itemData.getBusilogList();
		BusilogListBean approveBean = busilogList.get(0);

		if ("0".equals(approveBean.getResult())) { // 通过
			switch (approveBean.getFlow()) {
			case "00": // 00:新建进件模块
				stepStatus = 1;
				break;
			case "01":// 01:申请人授权书上传模块、
				stepStatus = 2;
				break;
			case "02":// 02:手机验证模块、
				stepStatus = 3;
				break;
			case "03":// 03:人脸识别模块、
				stepStatus = 4;
				break;
			case "04":// 04:人行征信风险验证模块、
				stepStatus = 5;
				break;
			case "05":// 05:银联信用卡卡验证模块 、
				stepStatus = 6;
				break;
			case "06":// 06:第三方征信数据核验模块、
				stepStatus = 7;
				break;
			}
		} else if ("3".equals(approveBean.getResult())) { // 验证中
			switch (approveBean.getFlow()) {
			case "01":// 01:申请人授权书上传模块、
				stepStatus = 1;
				break;
			case "02":// 02:手机验证模块、
				stepStatus = 2;
				break;
			case "03":// 03:人脸识别模块、
				stepStatus = 3;
				break;
			case "04":// 04:人行征信风险验证模块、
				result.setPersonalSubmit(true);
				stepStatus = 4;
				break;
			case "05":// 05:银联信用卡卡验证模块 、
				result.setBankCardSubmit(true);
				stepStatus = 5;
				break;
			case "06":// 06:第三方征信数据核验模块、
				result.setThreeSubmit(true);
				stepStatus = 6;
				break;
			case "07":// 07:风险预决策模块、
				stepStatus = 7;
				break;
			}
		}
		return stepStatus;
	}

	/**
	 * 根据状态获取资源
	 */
	private int getImageResourceByStat(int stat) {
		switch (stat) {
		case 0:
			return R.drawable.statu4;// 待提交
		case 1:
			return R.drawable.statu1;//转人工
		case 2:
			return R.drawable.statu2;//R.drawable.statu4;
		case 3:
			return R.drawable.statu3;
		case 4:
			return R.drawable.statu0;
		case 5:
			return R.drawable.statu5;
		}
		return -1;
	}

}
