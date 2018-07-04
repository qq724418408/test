package com.bocop.xfjr.bean;

import java.io.Serializable;

import com.bocop.xfjr.util.TextUtil;

public class TypeBean  implements Serializable{
	private int typeId;
	private String typeName;
	private String number="0";
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public TypeBean( String typeName,int typeId) {
		super();
		this.typeId = typeId;
		this.typeName = typeName;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = TextUtil.more100(number);
	}
	
}
