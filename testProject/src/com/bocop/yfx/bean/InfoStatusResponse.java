package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 工薪贷首页查询 资料状态
 * 
 * @author rd
 * 
 */
@XStreamAlias("UTILITY_PAYMENT")
public class InfoStatusResponse extends BaseResponse {
	@XStreamAlias("DATA_AREA")
	private InfoStatus infoState;

	public InfoStatus getInfoState() {
		return infoState;
	}

	public void setInfoState(InfoStatus infoState) {
		this.infoState = infoState;
	}

}
