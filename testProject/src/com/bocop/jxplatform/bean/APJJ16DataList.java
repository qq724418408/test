/**
 * 
 */
package com.bocop.jxplatform.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/** 
 * @author luoyang  E-mail: luoyang8714@163.com
 * @version 创建时间：2016-11-28 下午3:24:32 
 * 类说明 
 */
/**
 * @author zhongye
 *
 */
public class APJJ16DataList {

	@XStreamAlias("LicenseNum")
	private String licenseNum;			//车牌号
	
	@XStreamAlias("PeccancyTime")
	private String peccancyTime;			//违法时间
	
	@XStreamAlias("PeccancyPlace")
	private String peccancyPlace;			//违法地点
	
	@XStreamAlias("PeccancyCode")
	private String peccancyCode;			//违法代码
	
	@XStreamAlias("PeccancyAct")
	private String peccancyAct;			//违法行为
	
	@XStreamAlias("PeccancyMoney")
	private String peccancyMoney;			//违法金额
	
	@XStreamAlias("PeccancyScore")
	private String peccancyScore;			//违法记分
	
	@XStreamAlias("PeccancyOrg")
	private String peccancyOrg;			//发现机关
	
	@XStreamAlias("PeccancyFlag")
	private String peccancyFlag;			//处理状态
	
	@XStreamAlias("PeccancySequenceNum")
	private String peccancySequenceNum;			//违章记录号
	
}
