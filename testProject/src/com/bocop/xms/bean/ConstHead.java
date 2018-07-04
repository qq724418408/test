package com.bocop.xms.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class ConstHead {
	
	@XStreamAlias("REQUEST_TYPE")
	private String prequestType;
	@XStreamAlias("AGENT_CODE")
	private String agentCode;
	@XStreamAlias("TRN_CODE")
	private String tranCode;
	@XStreamAlias("FRONT_TRACENO")
	private String frontTraceNo;
	@XStreamAlias("CSPS_TRACENO")
	private String cspsTraceNo;
	@XStreamAlias("ERR_CODE")
	private String errCode;
	@XStreamAlias("ERR_MSG")
	private String errMsg;
	
	public String getPrequestType() {
		return prequestType;
	}
	public void setPrequestType(String prequestType) {
		this.prequestType = prequestType;
	}
	public String getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	public String getTranCode() {
		return tranCode;
	}
	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}
	public String getFrontTraceNo() {
		return frontTraceNo;
	}
	public void setFrontTraceNo(String frontTraceNo) {
		this.frontTraceNo = frontTraceNo;
	}
	public String getCspsTraceNo() {
		return cspsTraceNo;
	}
	public void setCspsTraceNo(String cspsTraceNo) {
		this.cspsTraceNo = cspsTraceNo;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
}
