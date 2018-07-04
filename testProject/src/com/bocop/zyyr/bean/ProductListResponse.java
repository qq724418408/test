package com.bocop.zyyr.bean;

import com.bocop.yfx.bean.BaseResponse;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 产品列表
 * 
 * @author lh
 * 
 */
@XStreamAlias("UTILITY_PAYMENT")
public class ProductListResponse extends BaseResponse {
	@XStreamAlias("DATA_AREA")
	private ProductList list;

	public ProductList getList() {
		return list;
	}

	public void setList(ProductList list) {
		this.list = list;
	}
	
	
}
