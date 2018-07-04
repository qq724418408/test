package com.bocop.zyyr.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * 产品列表
 * 
 * @author lh
 * 
 */
public class ProductList {

	@XStreamImplicit(itemFieldName = "PROLIST")
	private List<ProductInfo> list;
	@XStreamAlias("PAGEDATA")
	private CommonResponse commonResponse;

	public CommonResponse getCommonResponse() {
		return commonResponse;
	}

	public void setCommonResponse(CommonResponse commonResponse) {
		this.commonResponse = commonResponse;
	}

	public List<ProductInfo> getList() {
		return list;
	}

	public void setList(List<ProductInfo> list) {
		this.list = list;
	}

}
