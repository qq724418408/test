package com.bocop.jxplatform.bean;

/**
 * 根据处罚决定书编号，查询应缴金额、车牌等信息，用户客户主动输入处罚决定书编号，进行缴款。
 * @author zhongye
 *
 */
public class PeccancyXmlForPayBean {

	private String errorcode;
	private String errormsg;
	
	private String peccancyNum;  	//处罚决定书编号
	private String peccancySum;		//罚款金额（本金）
	private String licenseNum;		//车牌号码
	private String ownerName;		//当事人姓名
	private String peccancyDate;	//处理日期
	private String lateAmt;			//滞纳金
	private String yjAmt;			//应缴金额
	public String getPeccancyNum() {
		return peccancyNum;
	}
	public void setPeccancyNum(String peccancyNum) {
		this.peccancyNum = peccancyNum;
	}
	public String getPeccancySum() {
		return peccancySum;
	}
	public void setPeccancySum(String peccancySum) {
		this.peccancySum = peccancySum;
	}
	public String getLicenseNum() {
		return licenseNum;
	}
	public void setLicenseNum(String licenseNum) {
		this.licenseNum = licenseNum;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getPeccancyDate() {
		return peccancyDate;
	}
	public void setPeccancyDate(String peccancyDate) {
		this.peccancyDate = peccancyDate;
	}
	public String getLateAmt() {
		return lateAmt;
	}
	public void setLateAmt(String lateAmt) {
		this.lateAmt = lateAmt;
	}
	public String getYjAmt() {
		return yjAmt;
	}
	public void setYjAmt(String yjAmt) {
		this.yjAmt = yjAmt;
	}
	public String getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	
	
}
