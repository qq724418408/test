package com.bocop.zyyr.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class KeyAndValue {

	@XStreamAlias("PARA_KEY")
	private String paraKey;
	@XStreamAlias("PARA_VAL")
	private String paraValue;

	public String getParaKey() {
		return paraKey;
	}

	public void setParaKey(String paraKey) {
		this.paraKey = paraKey;
	}

	public String getParaValue() {
		return paraValue;
	}

	public void setParaValue(String paraValue) {
		this.paraValue = paraValue;
	}

	public KeyAndValue(String paraKey, String paraValue) {
		super();
		this.paraKey = paraKey;
		this.paraValue = paraValue;
	}

}
