/**
 * 
 */
package com.bocop.xms.bean;
/** 
 * @author luoyang  E-mail: luoyang8714@163.com
 * @version 创建时间：2016-5-27 上午10:52:23 
 * 类说明 
 */
/**
 * @author zhongye
 *
 */

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("UTILITY_PAYMENT")
public class OverViewResponse {

	@XStreamAlias("CONST_HEAD")
	private ConstHead constHead;
	@XStreamAlias("DATA_AREA")
	private OverViewList overViewList;
	
	public ConstHead getConstHead() {
		return constHead;
	}
	public void setConstHead(ConstHead constHead) {
		this.constHead = constHead;
	}
	public OverViewList getOverViewList() {
		return overViewList;
	}
	public void setOverViewList(OverViewList overViewList) {
		this.overViewList = overViewList;
	}
	

}
