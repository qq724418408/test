/**
 * 
 */
package com.bocop.xms.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/** 
 * @author luoyang  E-mail: luoyang8714@163.com
 * @version 创建时间：2016-5-27 上午11:03:37 
 * 类说明 
 */
/**
 * @author zhongye
 *
 */
public class OverViewType {

	@XStreamAlias("USER_ID")
	private String userId;//用户ID
	@XStreamAlias("USER_NAME")//用户姓名
	private String userName;
	@XStreamAlias("USER_CODE")
	private String userCode;//用户编号
	@XStreamAlias("ORDER_DATE")
	private String orderDate;//推送日期
	@XStreamAlias("SIGN")
	private String sign;//是否签约
	@XStreamAlias("TYPE")
	private String type;//签约类型
	public void OverViewType(){
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	

}
