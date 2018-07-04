package com.bocop.jxplatform.bean;

public class Advertisement {

	private String title;  //标题
	private String adPicId;  //图片Id
	private String content;  //内容  可以为商品ID或网页URL
	private String imageUrl;  //图片URL
	private int imageRes;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAdPicId() {
		return adPicId;
	}
	public void setAdPicId(String adPicId) {
		this.adPicId = adPicId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public int getImageRes() {
		return imageRes;
	}
	public void setImageRes(int imageRes) {
		this.imageRes = imageRes;
	}
	
}
