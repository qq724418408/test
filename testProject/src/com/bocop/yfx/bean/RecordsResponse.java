package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 贷款、还款、申请记录
 * 
 * @author lh
 * 
 */
@XStreamAlias("UTILITY_PAYMENT")
public class RecordsResponse extends BaseResponse {

	@XStreamAlias("DATA_AREA")
	private ListContainer container;

	public ListContainer getContainer() {
		return container;
	}

	public void setContainer(ListContainer container) {
		this.container = container;
	}
	
	

}
