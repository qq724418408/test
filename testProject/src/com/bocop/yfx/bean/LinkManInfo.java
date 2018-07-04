package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 联系人实体
 * 
 * @author rd
 * 
 */
public class LinkManInfo {
	@XStreamAlias("WLS_TYPE")
	private String type;// 类型
	@XStreamAlias("WLS_NAME")
	private String name;// 姓名
	@XStreamAlias("WLS_PHONE")
	private String phone;// 联系电话

	public LinkManInfo(String type, String name, String phone) {
		super();
		this.type = type;
		this.name = name;
		this.phone = phone;
	}

	public LinkManInfo() {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phonr) {
		this.phone = phonr;
	}

}
