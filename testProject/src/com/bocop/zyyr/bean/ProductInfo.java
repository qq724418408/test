package com.bocop.zyyr.bean;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 产品列表
 * 
 * @author lh
 * 
 */
public class ProductInfo implements Serializable {

	@XStreamAlias("PDT_NM")
	private String proName;
	@XStreamAlias("PDT_ID")
	private String proId;
	@XStreamAlias("PDT_DESC")
	private String proDesc;
	@XStreamAlias("PDT_TYPE")
	private String proType;
	@XStreamAlias("PDT_RATE")
	private String rate;
	@XStreamAlias("PDT_LOGO")
	private String imgUrl;

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public String getProType() {
		return proType;
	}

	public void setProType(String proType) {
		this.proType = proType;
	}

	public String getProDesc() {
		return proDesc;
	}

	public void setProDesc(String proDesc) {
		this.proDesc = proDesc;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

}
