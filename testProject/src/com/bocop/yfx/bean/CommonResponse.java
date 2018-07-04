package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 申请工薪贷返回实体
 * 
 * @author rd
 * 
 */
@XStreamAlias("UTILITY_PAYMENT")
public class CommonResponse extends BaseResponse {
	@XStreamAlias("DATA_AREA")
	private String data;
}
