package com.bocop.xms.push.bean;

import java.io.Serializable;
/**
 * 
 * @author hch
 *
 */
public class Aps implements Serializable {

	private static final long serialVersionUID = 1L;
	Alert alert;
	String badge;
	String sound;


	public Alert getAlert() {
		return alert;
	}

	public void setAlert(Alert alert) {
		this.alert = alert;
	}

	public String getBadge() {
		return badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

	@Override
	public String toString() {
		return "Aps [alter=" + alert + ", badge=" + badge + ", sound=" + sound + "]";
	}

}
