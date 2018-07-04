package com.bocop.xfjr.bean.detail;

import java.util.List;

import com.boc.jx.httptools.network.base.RootRsp;

public class BusinessBasicInfoBean extends RootRsp {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String applyMoney;
    private String approveMoney;
    private String businessId;
    private String channel;
    private String customerName;
    private String manager;
    private String merchantId;
    private String merchantName;
    private String merchantStatus;
    private String periods; // 分期期数（单位：月）
    private String periodsName; // 分期期数（单位：期）
    private String phoneNum;
    private String productId;
    private String productName;
    private String rate;
    private String tariffMoney;
    private String totalPrice;
    private String custCardId; // 银联验证卡号
    private String custCardIdThird; // 第三方征信卡号
    private String telephone; // 手机号
    private String idCard; // 身份证
    private List<AuditBean> audit;

    public String getPeriodsName() {
		return periodsName;
	}

	public void setPeriodsName(String periodsName) {
		this.periodsName = periodsName;
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

    public static class AuditBean extends RootRsp {
      
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * flow : 00
		 * result : 0
		 * time : 1506676826
		 * tips :
		 */
        private String flow;
        private String result;
        private String time;
        private String tips;

        public AuditBean() {
			super();
		}
        
        public AuditBean(String tips, String time) {
        	super();
        	this.time = time;
        	this.tips = tips;
        }

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
