package com.bocop.xfjr.bean.login;

import com.boc.jx.httptools.network.base.RootRsp;

public class LoginBean extends RootRsp {

	private String accessToken;
	private CustomerInfoBean customerInfo;
	private MerchantInfo merchantInfo;
	private String publicKey;
	private String role;

	public LoginBean() {

	}

	public LoginBean(String role, CustomerInfoBean customerInfo) {
		super();
		this.role = role;
		this.customerInfo = customerInfo;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public CustomerInfoBean getCustomerInfo() {
		return customerInfo;
	}

	public void setCustomerInfo(CustomerInfoBean customerInfo) {
		this.customerInfo = customerInfo;
	}

	public MerchantInfo getMerchantInfo() {
		return merchantInfo;
	}

	public void setMerchantInfo(MerchantInfo merchantInfo) {
		this.merchantInfo = merchantInfo;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public static class MerchantInfo extends RootRsp {

		private String merchantAddress;
		private String merchantId;
		private String merchantName;

		public String getMerchantAddress() {
			return merchantAddress;
		}

		public void setMerchantAddress(String merchantAddress) {
			this.merchantAddress = merchantAddress;
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

	}

	public static class CustomerInfoBean extends RootRsp {
		/**
		 * ehr : yinlu name : yinlu organId : 00015 organName : 中国银行江西省分行
		 */

		private String ehr;
		private String name;
		private String organId;
		private String organName;

		public String getEhr() {
			return ehr;
		}

		public void setEhr(String ehr) {
			this.ehr = ehr;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getOrganId() {
			return organId;
		}

		public void setOrganId(String organId) {
			this.organId = organId;
		}

		public String getOrganName() {
			return organName;
		}

		public void setOrganName(String organName) {
			this.organName = organName;
		}
	}
}
