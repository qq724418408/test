package com.bocop.jxplatform.bean.app;
/**
 * 描述：首页item信息bean类
 *  
 * @author wangzhongcai
 */
public class AppInfo {

	public int iconId; // 图片资源id
	public String name; // item名称
	public String otherName;//别名
	public String methodName; //方法名
	public Object[] parameter; //方法参数
	public int msgCount; //消息数量

	public int getIconId() {
		return iconId;
	}

	public void setIconId(int iconId) {
		this.iconId = iconId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOtherName() {
		return otherName;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object[] getParameter() {
		return parameter;
	}

	public void setParameter(Object[] parameter) {
		this.parameter = parameter;
	}

	public int getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}
     
}
