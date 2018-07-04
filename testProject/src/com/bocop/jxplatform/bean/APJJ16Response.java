/**
 * 
 */
package com.bocop.jxplatform.bean;

import java.util.List;

import com.bocop.xms.bean.ConstHead;
import com.bocop.xms.bean.MessageCostType;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/** 
 * @author luoyang  E-mail: luoyang8714@163.com
 * @version 创建时间：2016-11-28 下午3:11:25 
 * 类说明 
 */
/**
 * @author zhongye
 *
 */
@XStreamAlias("UTILITY_PAYMENT")
public class APJJ16Response {

	@XStreamAlias("CONST_HEAD")
	private ConstHead constHead;
	
	@XStreamAlias("DATA_AREA")
	private APJJ16DataArea dataArea;
	
	@XStreamAlias("DATA_LIST")
	private List<APJJ16DataList> dataList;

	public ConstHead getConstHead() {
		return constHead;
	}

	public void setConstHead(ConstHead constHead) {
		this.constHead = constHead;
	}

	public APJJ16DataArea getDataArea() {
		return dataArea;
	}

	public void setDataArea(APJJ16DataArea dataArea) {
		this.dataArea = dataArea;
	}

	public List<APJJ16DataList> getDataList() {
		return dataList;
	}

	public void setDataList(List<APJJ16DataList> dataList) {
		this.dataList = dataList;
	}
	
	
	

}
