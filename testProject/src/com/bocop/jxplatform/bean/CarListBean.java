package com.bocop.jxplatform.bean;

/**
 * 用于显示车列表LIST
 * @author zhongye
 *
 */
public class CarListBean {
	
	private String  licenseNumber; 	
	private int imageCarRes;		//车辆图片	
	private int imageRightRes;
	private String tbLicenseNumber;	//车牌号
	private String btCarPeccancy;	//查询违章
	
	
	public CarListBean() {
		// TODO Auto-generated constructor stub
	}
	
	
	public CarListBean(String licenseNumber, int imageCarRes, int imageRightRes) {
		this.licenseNumber = licenseNumber;
		this.imageCarRes = imageCarRes;
		this.imageRightRes = imageRightRes;
	}


	public String getLicenseNumber() {
		return licenseNumber;
	}
	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}
	public int getImageCarRes() {
		return imageCarRes;
	}
	public void setImageCarRes(int imageCarRes) {
		this.imageCarRes = imageCarRes;
	}
	public int getImageRightRes() {
		return imageRightRes;
	}
	public void setImageRightRes(int imageRightRes) {
		this.imageRightRes = imageRightRes;
	}


	public String getTbLicenseNumber() {
		return tbLicenseNumber;
	}


	public void setTbLicenseNumber(String tbLicenseNumber) {
		this.tbLicenseNumber = tbLicenseNumber;
	}


	public String getBtCarPeccancy() {
		return btCarPeccancy;
	}


	public void setBtCarPeccancy(String btCarPeccancy) {
		this.btCarPeccancy = btCarPeccancy;
	}

}
