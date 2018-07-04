package com.boc.jx.base;

import java.io.Serializable;

/** 
 * @author luoyang  
 * @version 创建时间：2015-7-3 下午8:36:29 
 * 类说明 
 */

public class CardInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// 银行卡号
	private String bankCardNo;
	// 银行卡号（星号屏蔽）
	private String bankCardNoEncrypt;
	// 卡归属地
	private String bankCardCsp;
	// 卡归属地名称
	private String bankCardCspName;
	// 卡别名
	private String bankCardAlias;

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getBankCardNoEncrypt() {
		return bankCardNoEncrypt;
	}

	public void setBankCardNoEncrypt(String bankCardNoEncrypt) {
		this.bankCardNoEncrypt = bankCardNoEncrypt;
	}

	public String getBankCardCsp() {
		return bankCardCsp;
	}

	public void setBankCardCsp(String bankCardCsp) {
		this.bankCardCsp = bankCardCsp;
	}

	public String getBankCardCspName() {
		return bankCardCspName;
	}

	public void setBankCardCspName(String bankCardCspName) {
		this.bankCardCspName = bankCardCspName;
	}

	public String getBankCardAlias() {
		return bankCardAlias;
	}

	public void setBankCardAlias(String bankCardAlias) {
		this.bankCardAlias = bankCardAlias;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
