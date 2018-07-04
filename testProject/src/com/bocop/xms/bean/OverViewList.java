/**
 * 
 */
package com.bocop.xms.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/** 
 * @author luoyang  E-mail: luoyang8714@163.com
 * @version 创建时间：2016-5-27 上午10:53:34 
 * 类说明 
 */
/**
 * @author zhongye
 *
 */
public class OverViewList {

	@XStreamImplicit(itemFieldName="registerInfo")
	private List<OverViewType> list;

	public List<OverViewType> getList() {
		return list;
	}

	public void setList(List<OverViewType> list) {
		this.list = list;
	}

}
