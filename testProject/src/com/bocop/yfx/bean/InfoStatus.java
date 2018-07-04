package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 资料状态
 * 
 * @author rd
 * 
 */
public class InfoStatus {

	@XStreamAlias("WLS_VAL_STATUS")
	private String valStatus;

	public InfoStatus(String valStatus) {
		super();
		this.valStatus = valStatus;
	}

	public InfoStatus() {
		super();
	}

	public String getValStatus() {
		return valStatus;
	}

	public void setValStatus(String valStatus) {
		this.valStatus = valStatus;
	}

}
