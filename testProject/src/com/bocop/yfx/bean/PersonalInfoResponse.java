package com.bocop.yfx.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 个人信息预览
 * 
 * @author rd
 * 
 */
@XStreamAlias("UTILITY_PAYMENT")
public class PersonalInfoResponse extends BaseResponse {
	@XStreamAlias("DATA_AREA")
	private PersonalInfo personalInfo;

	public PersonalInfo getPersonalInfo() {
		return personalInfo;
	}

	public void setPersonalInfo(PersonalInfo personalInfo) {
		this.personalInfo = personalInfo;
	}

}
