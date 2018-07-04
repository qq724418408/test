package com.bocop.xfjr.bean.detail;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * 业务实体
 * 
 * @author wujunliu
 *
 */
public class MyBusinessDetailBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	@SerializedName("merchant")
	private MerchantBean merchant;// 商户信息
	@SerializedName("customer")
	private CustomerBean customer;// 客户信息
	private List<MyBusinessDetailApproveBean> audit; // 审批信息

	public MerchantBean getMerchant() {
		return merchant;
	}

	public void setMerchant(MerchantBean merchant) {
		this.merchant = merchant;
	}

	public CustomerBean getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerBean customer) {
		this.customer = customer;
	}

	public List<MyBusinessDetailApproveBean> getAudit() {
		return audit;
	}

	public void setAudit(List<MyBusinessDetailApproveBean> audit) {
		this.audit = audit;
	}
//	private MerchantBean merchant;
//    private CustomerBean customer;
//    private List<AuditBean> audit;
//
//    public MerchantBean getMerchant() {
//        return merchant;
//    }
//
//    public void setMerchant(MerchantBean merchant) {
//        this.merchant = merchant;
//    }
//
//    public CustomerBean getCustomer() {
//        return customer;
//    }
//
//    public void setCustomer(CustomerBean customer) {
//        this.customer = customer;
//    }
//
//    public List<AuditBean> getAudit() {
//        return audit;
//    }
//
//    public void setAudit(List<AuditBean> audit) {
//        this.audit = audit;
//    }
//
//    public static class MerchantBean {
//        /**
//         * merchantId : 104360155110141
//         * merchantName : 长久世达
//         * merchantStatus  : 1
//         * channel : XXXXXX
//         * category : XXXXXX
//         * businessId : 1234567566
//         */
//
//        private String merchantId;
//        private String merchantName;
//        private String merchantStatus;
//        private String channel;
//        private String category;
//        private String businessId;
//
//        public String getMerchantId() {
//            return merchantId;
//        }
//
//        public void setMerchantId(String merchantId) {
//            this.merchantId = merchantId;
//        }
//
//        public String getMerchantName() {
//            return merchantName;
//        }
//
//        public void setMerchantName(String merchantName) {
//            this.merchantName = merchantName;
//        }
//
//        public String getMerchantStatus() {
//            return merchantStatus;
//        }
//
//        public void setMerchantStatus(String merchantStatus) {
//            this.merchantStatus = merchantStatus;
//        }
//
//        public String getChannel() {
//            return channel;
//        }
//
//        public void setChannel(String channel) {
//            this.channel = channel;
//        }
//
//        public String getCategory() {
//            return category;
//        }
//
//        public void setCategory(String category) {
//            this.category = category;
//        }
//
//        public String getBusinessId() {
//            return businessId;
//        }
//
//        public void setBusinessId(String businessId) {
//            this.businessId = businessId;
//        }
//    }
//
//    public static class CustomerBean {
//        /**
//         * customerName : 欧阳青
//         * telephone : 13833885354
//         * applyMoney : 100000.00
//         * totalMoney : 102000.00
//         * periods : 12
//         * rate : 0.04
//         * aprovedMoney : 90000.00
//         * lendMoney : 90000.00
//         * aproveTime : 2018-08-21
//         * loanTime : 2018-08-26
//         */
//
//        private String customerName;
//        private String telephone;
//        private String applyMoney;
//        private String totalMoney;
//        private String periods;
//        private String rate;
//        private String aprovedMoney;
//        private String lendMoney;
//        private String aproveTime;
//        private String loanTime;
//
//        public String getCustomerName() {
//            return customerName;
//        }
//
//        public void setCustomerName(String customerName) {
//            this.customerName = customerName;
//        }
//
//        public String getTelephone() {
//            return telephone;
//        }
//
//        public void setTelephone(String telephone) {
//            this.telephone = telephone;
//        }
//
//        public String getApplyMoney() {
//            return applyMoney;
//        }
//
//        public void setApplyMoney(String applyMoney) {
//            this.applyMoney = applyMoney;
//        }
//
//        public String getTotalMoney() {
//            return totalMoney;
//        }
//
//        public void setTotalMoney(String totalMoney) {
//            this.totalMoney = totalMoney;
//        }
//
//        public String getPeriods() {
//            return periods;
//        }
//
//        public void setPeriods(String periods) {
//            this.periods = periods;
//        }
//
//        public String getRate() {
//            return rate;
//        }
//
//        public void setRate(String rate) {
//            this.rate = rate;
//        }
//
//        public String getAprovedMoney() {
//            return aprovedMoney;
//        }
//
//        public void setAprovedMoney(String aprovedMoney) {
//            this.aprovedMoney = aprovedMoney;
//        }
//
//        public String getLendMoney() {
//            return lendMoney;
//        }
//
//        public void setLendMoney(String lendMoney) {
//            this.lendMoney = lendMoney;
//        }
//
//        public String getAproveTime() {
//            return aproveTime;
//        }
//
//        public void setAproveTime(String aproveTime) {
//            this.aproveTime = aproveTime;
//        }
//
//        public String getLoanTime() {
//            return loanTime;
//        }
//
//        public void setLoanTime(String loanTime) {
//            this.loanTime = loanTime;
//        }
//    }
//
//    public static class AuditBean {
//        /**
//         * time : 1534643114634684616
//         * tips : 发起申请
//         */
//
//        private String time;
//        private String tips;
//
//        public String getTime() {
//            return time;
//        }
//
//        public void setTime(String time) {
//            this.time = time;
//        }
//
//        public String getTips() {
//            return tips;
//        }
//
//        public void setTips(String tips) {
//            this.tips = tips;
//        }
//    }
}
