package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 用途
 * 
 * @author lh
 * 
 */
public class UseString {

	@XStreamAlias("WLS_USE")
	private String use;
	@XStreamAlias("WLS_USE_ID")
	private String useID;

	public String getUseID() {
		return useID;
	}

	public void setUseID(String useID) {
		this.useID = useID;
	}

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

}
