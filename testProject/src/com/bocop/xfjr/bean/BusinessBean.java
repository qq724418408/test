package com.bocop.xfjr.bean;

import java.io.Serializable;
import java.util.List;

public class BusinessBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * applyMoney : 33
	 * approveMoney :
	 * busilogList :
	 * [{"flow":"04","result":"0"},{"flow":"01","result":"0"},{"flow":"00",
	 * "result":"0"}]
	 * businessId : BUS171027174009000001
	 * channelId : 10002
	 * custCardId :
	 * custCardIdThrid :
	 * customerName : 潘建七
	 * financeCertify : N
	 * idCard : 452224199110033013
	 * incomeCertify : N
	 * instalmentApply : N
	 * loadMoney :
	 * merchantId : 0910
	 * merchantName : 四方精创
	 * merchantStatus : Y
	 * productId : 10004
	 * productName : 演唱会
	 * telephone : 18620314103
	 */

	private String applyMoney; // 申请金额
	private String approveMoney; // 预审金额
//	private String tariffMoney; // 放款金额
	private String loadMoney; // 放款金额
	private String businessId;
	private String channelId;
	private String custCardId;
	private String custCardIdThrid;
	private String customerName;
	private String financeCertify;
	private String idCard;
	private String incomeCertify;
	private String instalmentApply;
	private String merchantId;
	private String merchantName;
	private String merchantStatus;
	private String productId;
	private String productName;
	private String telephone;
	private String periods = "0";
	private List<BusilogListBean> busilogList;
	private String category;
	private int stat;

	public BusinessBean(String businessId, String category, String customerName,
			String applyMoney) {
		super();
		this.customerName = customerName;
		this.category = category;
		this.applyMoney = applyMoney;
		this.businessId = businessId;
	}
	
	public String getPeriods() {
		return periods;
	}

	public void setPeriods(String periods) {
		this.periods = periods;
	}

//	public String getTariffMoney() {
//		return tariffMoney;
//	}
//
//	public void setTariffMoney(String tariffMoney) {
//		this.tariffMoney = tariffMoney;
//	}
	
	public BusinessBean() {
		super();
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

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getCustCardId() {
		return custCardId;
	}

	public void setCustCardId(String custCardId) {
		this.custCardId = custCardId;
	}

	public String getCustCardIdThrid() {
		return custCardIdThrid;
	}

	public void setCustCardIdThrid(String custCardIdThrid) {
		this.custCardIdThrid = custCardIdThrid;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getFinanceCertify() {
		return financeCertify;
	}

	public void setFinanceCertify(String financeCertify) {
		this.financeCertify = financeCertify;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
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

	public String getLoadMoney() {
		return loadMoney;
	}

	public void setLoadMoney(String loadMoney) {
		this.loadMoney = loadMoney;
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

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public List<BusilogListBean> getBusilogList() {
		return busilogList;
	}

	public void setBusilogList(List<BusilogListBean> busilogList) {
		this.busilogList = busilogList;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getStat() {
		return stat;
	}

	public void setStat(int stat) {
		this.stat = stat;
	}

	public static class BusilogListBean {
		/**
		 * flow : 04
		 * result : 0
		 */

		private String flow;
		private String result;

		public String getFlow() {
			return flow;
		}

		public void setFlow(String flow) {
			this.flow = flow;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}
	}
}