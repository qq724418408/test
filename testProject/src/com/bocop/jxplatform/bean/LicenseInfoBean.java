package com.bocop.jxplatform.bean;

/** 
 * @author luoyang  
 * @version 创建时间：2015-6-19 上午10:34:45 
 * 驾驶证信息类
 */

public class LicenseInfoBean {
	private String idCard; 			//身份证
	private String userId;			//开放平台用户ID
	private String drivenum;		//驾驶证号
	private String filenum;			//文档编号
	private String ownername;		//姓名
	private String tel;				//电话
	private String quasidriving;	//准驾车型
	private String penaltyscore;	//罚分
	private String state;			//状态
	private String replacementdate;	//换证日期
	private String zhengxin;		//证芯编号
	
	private String errorcode;
	private String errormsg;
	
	
	
	public String getZhengxin() {
		return zhengxin;
	}

	public void setZhengxin(String zhengxin) {
		this.zhengxin = zhengxin;
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

	public LicenseInfoBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LicenseInfoBean(String idCard, String userId,
			String filenum,  String tel,String strZhengxin) {
		super();
		this.idCard = idCard;
		this.userId = userId;
		this.filenum = filenum;
		this.tel = tel;
		this.zhengxin = strZhengxin;
	}
	public LicenseInfoBean(String idCard, String userId, String drivenum,
			String filenum, String ownername, String tel, String quasidriving,
			String penaltyscore, String state, String replacementdate) {
		super();
		this.idCard = idCard;
		this.userId = userId;
		this.drivenum = drivenum;
		this.filenum = filenum;
		this.ownername = ownername;
		this.tel = tel;
		this.quasidriving = quasidriving;
		this.penaltyscore = penaltyscore;
		this.state = state;
		this.replacementdate = replacementdate;
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

	public String getDrivenum() {
		return drivenum;
	}

	public void setDrivenum(String drivenum) {
		this.drivenum = drivenum;
	}

	public String getFilenum() {
		return filenum;
	}

	public void setFilenum(String filenum) {
		this.filenum = filenum;
	}

	public String getOwnername() {
		return ownername;
	}

	public void setOwnername(String ownername) {
		this.ownername = ownername;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getQuasidriving() {
		return quasidriving;
	}

	public void setQuasidriving(String quasidriving) {
		this.quasidriving = quasidriving;
	}

	public String getPenaltyscore() {
		return penaltyscore;
	}

	public void setPenaltyscore(String penaltyscore) {
		this.penaltyscore = penaltyscore;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getReplacementdate() {
		return replacementdate;
	}

	public void setReplacementdate(String replacementdate) {
		this.replacementdate = replacementdate;
	}
	
	
}
