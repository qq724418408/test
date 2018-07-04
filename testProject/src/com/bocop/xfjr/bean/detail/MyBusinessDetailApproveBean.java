package com.bocop.xfjr.bean.detail;

import java.io.Serializable;

/**
 * 
 * @author wujunliu
 *
 */
public class MyBusinessDetailApproveBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String tips; // 进度提示文本
	private String time; // 时间long
	//00:新建进件模块	
	//01:申请人授权书上传模块 	
	//02:手机验证模块	
	//03:人脸识别模块	
	//04:征信风险验证模块	
	//05:银联信用卡卡验证模块 	
	//06:第三方征信数据核验模块	
	//07:风险预决策模块	
	//08;补充资料模块	
	//09:业务状态认为变更模块	
	private String flow; //步骤:	
	//0, 通过
	//1, 待审批
	//2, 拒绝
	//3：验证中
	private String result; // 结果

	public MyBusinessDetailApproveBean() {
		super();
	}

	public MyBusinessDetailApproveBean(String tips, String time) {
		super();
		this.tips = tips;
		this.time = time;
	}

	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
