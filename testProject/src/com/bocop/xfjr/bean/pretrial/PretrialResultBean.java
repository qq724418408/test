package com.bocop.xfjr.bean.pretrial;

import com.boc.jx.httptools.network.base.RootRsp;

public class PretrialResultBean extends RootRsp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// "status":"1","limit":"666666"
	private String status; // 状态 0：待审核 1：转人工
	private String limit; // 额度
	private String model; // 使用的场景
	private String type; // 使用的客户类型

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
