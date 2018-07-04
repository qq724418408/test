/** 
 * @author luoyang  
 * @version 创建时间：2015-6-18 上午10:56:31 
 * 类说明 
 */

package com.bocop.jxplatform.bean;

public class CarInfoBean {
	
	private String idCard; 			//身份证
	private String userId;			//开放平台用户ID
	private String licenseNumber;	//车牌号码
	private String ownerName;		//车主姓名
	private String licenseType;		//车牌类型
	private String tel;				//手机号码
	private String vehicleNum;		//车架号后六位
	private String fileNUm;			//档案编号
	private String channelFlag;		//渠道
	private String state;		//车辆状态
	private String annualDate;		//年审日期
	
	
	private String errorcode;
	private String errormsg;
	
	public CarInfoBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public String getFileNUm() {
		return fileNUm;
	}


	public void setFileNUm(String fileNUm) {
		this.fileNUm = fileNUm;
	}


	public String getChannelFlag() {
		return channelFlag;
	}


	public void setChannelFlag(String channelFlag) {
		this.channelFlag = channelFlag;
	}


	public CarInfoBean(String userId,String licenseType, String licenseNumber,
			String ownerName, String tel, String vehicleNum,String idCard,String channelFlag) {
		super();
		this.idCard = idCard;
		this.userId = userId;
		this.licenseNumber = licenseNumber;
		this.ownerName = ownerName;
		this.licenseType = licenseType;
		this.tel = tel;
		this.vehicleNum = vehicleNum;
		this.channelFlag = channelFlag;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getVehicleNum() {
		return vehicleNum;
	}

	public void setVehicleNum(String vehicleNum) {
		this.vehicleNum = vehicleNum;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getAnnualDate() {
		return annualDate;
	}


	public void setAnnualDate(String annualDate) {
		this.annualDate = annualDate;
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
