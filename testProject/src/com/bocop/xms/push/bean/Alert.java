package com.bocop.xms.push.bean;

import java.io.Serializable;
/**
 * 
 * @author hch
 *
 */
public class Alert implements Serializable {

	private static final long serialVersionUID = 1L;
	String title;
	String body;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "Alter [title=" + title + ", body=" + body + "]";
	}

}
