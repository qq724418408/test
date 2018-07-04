package com.bocop.xfjr.bean.detail;

import com.boc.jx.httptools.network.base.RootRsp;

/**
 * 商户实体
 * 
 * @author wujunliu
 *
 */
public class CustomerBean extends RootRsp {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String customerName;// 客户姓名
	private String telephone;// 手机号
	private String applyMoney;// 申请金额
	private String totalMoney;// 订单总价
	private String periods;// 分期期数
	private String rate;// 费率
	private String aprovedMoney;// 预审金额
	private String lendMoney;// 放款金额

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

	public String getApplyMoney() {
		return applyMoney;
	}

	public void setApplyMoney(String applyMoney) {
		this.applyMoney = applyMoney;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
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

	public String getAprovedMoney() {
		return aprovedMoney;
	}

	public void setAprovedMoney(String aprovedMoney) {
		this.aprovedMoney = aprovedMoney;
	}

	public String getLendMoney() {
		return lendMoney;
	}

	public void setLendMoney(String lendMoney) {
		this.lendMoney = lendMoney;
	}

}
