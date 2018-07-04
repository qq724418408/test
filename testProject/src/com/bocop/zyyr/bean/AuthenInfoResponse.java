package com.bocop.zyyr.bean;

import com.bocop.yfx.bean.BaseResponse;
import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 认证资料实体
 * @author rd
 *
 */
@XStreamAlias("UTILITY_PAYMENT")
public class AuthenInfoResponse extends BaseResponse {
	
	@XStreamAlias("DATA_AREA")
	private AuthenInfoBean authenInfoBean;

	public AuthenInfoBean getAuthenInfoBean() {
		return authenInfoBean;
	}

	public void setAuthenInfoBean(AuthenInfoBean authenInfoBean) {
		this.authenInfoBean = authenInfoBean;
	}
}
