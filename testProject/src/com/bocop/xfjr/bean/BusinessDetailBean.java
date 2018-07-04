package com.bocop.xfjr.bean;

import java.io.Serializable;
import java.util.List;

public class BusinessDetailBean implements Serializable{
	 /**
     * applyMoney : 200000
     * aproveMoney : 
     * audit : [{"time":"2017-9-18.17.55. 18. 259063000","tips":""}]
     * businessId : 1
     * channel : 
     * customerName : 郑云
     * manager : liuyang
     * merchantId : 0911
     * merchantName : 联想集团
     * merchantStatus : Y
     * periods : 
     * phoneNum : 15895632345
     * productId : 10010
     * productName : 房贷分期
     * rate : 
     * tariffMoney : 
     * totalPrice : 200000
     */

	private String periodsName;
    public String getPeriodsName() {
		return periodsName;
	}

	public void setPeriodsName(String periodsName) {
		this.periodsName = periodsName;
	}

	private String applyMoney;
    private String approveMoney;
    private String businessId;
    private String channel;
    private String customerName;
    private String manager;
    private String merchantId;
    private String merchantName;
    private String merchantStatus;
    private String periods;
    private String phoneNum;
    private String productId;
    private String productName;
    private String rate;
    private String tariffMoney;
    private String totalPrice;
    private List<AuditBean> audit;

    public String getApplyMoney() {
        return applyMoney;
    }

    public void setApplyMoney(String applyMoney) {
        this.applyMoney = applyMoney;
    }

    public String getApproveMoney() {
        return approveMoney;
    }

    public void setApproveMoney(String aproveMoney) {
        this.approveMoney = aproveMoney;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
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

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTariffMoney() {
        return tariffMoney;
    }

    public void setTariffMoney(String tariffMoney) {
        this.tariffMoney = tariffMoney;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<AuditBean> getAudit() {
        return audit;
    }

    public void setAudit(List<AuditBean> audit) {
        this.audit = audit;
    }

    public static class AuditBean {
        /**
         * time : 2017-9-18.17.55. 18. 259063000
         * tips : 
         */
    	private String tips;
        private String time;
        
        
        public AuditBean() {
			super();
		}

		public AuditBean(String time, String tips) {
			super();
			this.time = time;
			this.tips = tips;
		}

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }
    }
}

