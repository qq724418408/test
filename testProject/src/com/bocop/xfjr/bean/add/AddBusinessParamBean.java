package com.bocop.xfjr.bean.add;

import com.boc.jx.httptools.network.base.RootRsp;

/**
 * 基本信息的商户信息
 * 
 * @author wujunliu
 *
 */
public class AddBusinessParamBean extends RootRsp {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String ehrId; // 归属客户经理编号
	private String merchantId; // 商户号
	private String merchantName; // 商户名称
	private String merchantStatus; // 商户状态；正常1，关闭0
	private String channelId; // 渠道号（渠道代码）
	private String channelName; // 渠道名称
	private String productId; // 渠道产品
	private String product; // 渠道产品
	private String customerName; // 客户姓名
	private String telephone; // 客户手机号
	private String totalMoney; // 订单总价
	private String applyMoney; // 申请金额
	private String periods; // 分期期数
	private String periodsId; // 分期期数
	private String rate; // 费率

	public String getPeriodsId() {
		return periodsId;
	}

	public void setPeriodsId(String periodsId) {
		this.periodsId = periodsId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getApplyMoney() {
		return applyMoney;
	}

	public void setApplyMoney(String applyMoney) {
		this.applyMoney = applyMoney;
	}

	public String getPeriods() {
		return periods;
	}

	public void setPeriods(String periods) {
		this.periods = periods;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getEhrId() {
		return ehrId;
	}

	public void setEhrId(String ehrId) {
		this.ehrId = ehrId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantStatus() {
		return merchantStatus;
	}

	public void setMerchantStatus(String merchantStatus) {
		this.merchantStatus = merchantStatus;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

}
