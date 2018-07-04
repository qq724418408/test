package com.bocop.xms.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * 用户列表
 * @author ftl
 *
 */
public class UserList {
	
	@XStreamImplicit(itemFieldName="list")
	private List<SignContractInfo> list;

	public List<SignContractInfo> getList() {
		return list;
	}

	public void setList(List<SignContractInfo> list) {
		this.list = list;
	}

}
