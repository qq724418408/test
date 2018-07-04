package com.bocop.zyyr.bean;

import com.bocop.yfx.bean.BaseResponse;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 认证资料状态
 * 
 * @author rd
 * 
 */
@XStreamAlias("UTILITY_PAYMENT")
public class AuthentStatusResponse extends BaseResponse {
	@XStreamAlias("DATA_AREA")
	private Status status;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
