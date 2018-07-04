package com.bocop.xms.xml.sign;

public class SignBean {
	private String type;
	private String isSigned;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIsSigned() {
		return isSigned;
	}
	public void setIsSigned(String isSigned) {
		this.isSigned = isSigned;
	}
	@Override
	public String toString() {
		return "SignBean [type=" + type + ", isSigned=" + isSigned + "]";
	}
	
	
	

}
