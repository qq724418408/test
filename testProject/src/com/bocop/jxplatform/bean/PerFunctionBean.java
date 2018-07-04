package com.bocop.jxplatform.bean;

public class PerFunctionBean {
	
	private String functionId;
	private int imageRes;
	private String title;
	
	/*
	 * 初始化PerFunctionBean
	 * ID，图片，标题
	 */
	public PerFunctionBean()
	{}
	public PerFunctionBean(String functionId,int imageRes,String title)
	{
		this.functionId = functionId;
		this.imageRes = imageRes;
		this.title = title;
		
	}
	public String getFunctionId() {
		return functionId;
	}
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	public int getImageRes() {
		return imageRes;
	}
	public void setImageRes(int imageRes) {
		this.imageRes = imageRes;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

}
