/**
 * 
 */
package com.bocop.jxplatform.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/** 
 * @author luoyang  E-mail: luoyang8714@163.com
 * @version 创建时间：2016-11-28 下午3:19:24 
 * 类说明 
 */
/**
 * @author zhongye
 *
 */
public class APJJ16DataArea {

	@XStreamAlias("CarNum")
	private String carNum;

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	
	
}
