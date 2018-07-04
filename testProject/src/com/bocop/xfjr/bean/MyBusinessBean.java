package com.bocop.xfjr.bean;

import java.util.List;

import com.boc.jx.httptools.network.base.RootRsp;
import com.bocop.xfjr.bean.detail.MyBusinessDetailApproveBean;

/**
 * 业务实体
 * 
 * @author wujunliu
 *
 */
public class MyBusinessBean extends RootRsp {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String businessId; // 业务编号
	private String status; // 业务状态:待提交、待决策、待审批、待放款、已放款、已拒绝
	private String applyAmount; // 申请金额
	private String tariffMoney; // 放款金额
	private String merchantId; // 商户id
	private String merchantName; // 商户名称
	private String merchantStatus; // 商户状态
	private String channelId; // channelId编号
	private String productId; // 产品编号
	private String productName; // 产品名称
	private String customerName; // 客户姓名
	private String customerId; // 客户编号
	private String applyMoney; // 申请金额
	private String approveMoney; // 预审金额
	private String stepStatus; // 记录业务当前进度状态
	private String financeCertify; // 财力证明
	private String incomeCertify; // 收入证明
	private String instalmentApply; // 分期申请书
	private String custCardId; // 银联验证卡号
    private String custCardIdThird; // 第三方征信卡号
    private String telephone; // 手机号
    private String idCard; // 身份证
    private String periods; // 分期期数（单位：月）
	private List<MyBusinessDetailApproveBean> busilogList; // 审批信息

	public String getPeriods() {
		return periods;
	}

	public void setPeriods(String periods) {
		this.periods = periods;
	}

	public String getCustCardId() {
		return custCardId;
	}

	public void setCustCardId(String custCardId) {
		this.custCardId = custCardId;
	}

	public String getCustCardIdThird() {
		return custCardIdThird;
	}

	public void setCustCardIdThird(String custCardIdThird) {
		this.custCardIdThird = custCardIdThird;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public List<MyBusinessDetailApproveBean> getBusilogList() {
		return busilogList;
	}

	public void setBusilogList(List<MyBusinessDetailApproveBean> busilogList) {
		this.busilogList = busilogList;
	}

	public String getFinanceCertify() {
		return financeCertify;
	}

	public void setFinanceCertify(String financeCertify) {
		this.financeCertify = financeCertify;
	}

	public String getIncomeCertify() {
		return incomeCertify;
	}

	public void setIncomeCertify(String incomeCertify) {
		this.incomeCertify = incomeCertify;
	}

	public String getInstalmentApply() {
		return instalmentApply;
	}

	public void setInstalmentApply(String instalmentApply) {
		this.instalmentApply = instalmentApply;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getApplyAmount() {
		return applyAmount;
	}

	public void setApplyAmount(String applyAmount) {
		this.applyAmount = applyAmount;
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

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getApplyMoney() {
		return applyMoney;
	}

	public void setApplyMoney(String applyMoney) {
		this.applyMoney = applyMoney;
	}

	public String getApproveMoney() {
		return approveMoney;
	}

	public void setApproveMoney(String approveMoney) {
		this.approveMoney = approveMoney;
	}

	public String getStepStatus() {
		return stepStatus;
	}

	public void setStepStatus(String stepStatus) {
		this.stepStatus = stepStatus;
	}

	public String getTariffMoney() {
		return tariffMoney;
	}

	public void setTariffMoney(String tariffMoney) {
		this.tariffMoney = tariffMoney;
	}

}
