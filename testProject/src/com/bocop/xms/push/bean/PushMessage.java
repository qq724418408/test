package com.bocop.xms.push.bean;

import java.io.Serializable;
/**
 * 接收到推送消息格式
 * @author hch
 *
 */
public class PushMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	Aps aps;
	String url;
	Extension extension;

	public Aps getAps() {
		return aps;
	}

	public void setAps(Aps aps) {
		this.aps = aps;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Extension getExtension() {
		return extension;
	}

	public void setExtension(Extension extension) {
		this.extension = extension;
	}

	@Override
	public String toString() {
		return "PushMessage [aps=" + aps + ", url=" + url + ", extension=" + extension
				+ "]";
	}

}
