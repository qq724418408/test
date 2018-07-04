package com.boc.jx.base;

import java.io.Serializable;
import java.util.ArrayList;


public class UserInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	// 证件类型
	private String idenType;
	// 证件号码
	private String idenNo;
	// 客户姓名
	private String custName;
	// 核心客户号
	private String coreCustId;
	// 性别
	private String gender;
	// 手机号码
	private String cellPhone;
	// 电子信箱
	private String email;
	// 卡列表
	private ArrayList<CardInfo> list;

	public String getIdenType() {
		return idenType;
	}

	public void setIdenType(String idenType) {
		this.idenType = idenType;
	}

	public String getIdenNo() {
		return idenNo;
	}

	public void setIdenNo(String idenNo) {
		this.idenNo = idenNo;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCoreCustId() {
		return coreCustId;
	}

	public void setCoreCustId(String coreCustId) {
		this.coreCustId = coreCustId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ArrayList<CardInfo> getList() {
		return list;
	}

	public void setList(ArrayList<CardInfo> list) {
		this.list = list;
	}

}
	
	
