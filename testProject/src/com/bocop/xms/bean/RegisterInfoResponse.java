/**
 * 
 */
package com.bocop.xms.bean;
/** 
 * @author luoyang  E-mail: luoyang8714@163.com
 * @version 创建时间：2016-5-20 下午5:13:47 
 * 类说明 
 */
/**
 * @author zhongye
 *
 */

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("UTILITY_PAYMENT")
public class RegisterInfoResponse {
		@XStreamAlias("CONST_HEAD")
		private ConstHead constHead;
		@XStreamAlias("DATA_AREA")
		private MessageList messageList;
		
		public ConstHead getConstHead() {
			return constHead;
		}
		public void setConstHead(ConstHead constHead) {
			this.constHead = constHead;
		}
		public MessageList getMessageList() {
			return messageList;
		}
		public void setMessageList(MessageList messageList) {
			this.messageList = messageList;
		}
}
