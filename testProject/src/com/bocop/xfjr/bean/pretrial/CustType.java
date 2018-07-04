package com.bocop.xfjr.bean.pretrial;

import com.boc.jx.httptools.network.base.RootRsp;

public class CustType extends RootRsp {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String typeKey; // 客户类型Key
	private String typeValue; // 客户类型Value

	public String getTypeKey() {
		return typeKey;
	}

	public void setTypeKey(String typeKey) {
		this.typeKey = typeKey;
	}

	public String getTypeValue() {
		return typeValue;
	}

	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
	}

}
