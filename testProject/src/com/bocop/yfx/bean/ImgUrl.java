package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 轮播图
 * 
 * @author lh
 * 
 */
public class ImgUrl {

	@XStreamAlias("WLS_IMG_TITLE")
	private String imgTitle;
	@XStreamAlias("WLS_IMG_CONTENT")
	private String imgContent;
	@XStreamAlias("WLS_IMG_ORDER")
	private String imgOrder;
	@XStreamAlias("WLS_IMG_URL")
	private String imgUrl;
	@XStreamAlias("WLS_IMG_TYPE")
	private String imgType;
	public String getImgTitle() {
		return imgTitle;
	}
	public void setImgTitle(String imgTitle) {
		this.imgTitle = imgTitle;
	}
	public String getImgContent() {
		return imgContent;
	}
	public void setImgContent(String imgContent) {
		this.imgContent = imgContent;
	}
	public String getImgOrder() {
		return imgOrder;
	}
	public void setImgOrder(String imgOrder) {
		this.imgOrder = imgOrder;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getImgType() {
		return imgType;
	}
	public void setImgType(String imgType) {
		this.imgType = imgType;
	}
	
	


}
