package com.bocop.yfx.bean;

/**
 * 
 * 中银平台用户卡号信息
 * 
 * @author lh
 * 
 */
public class SA0075Card {

	public String userid;
	public String accno;
	public String alias;
	public String trntyp;
	public String lmtamt;
	public String time;
	public String ifncal;
	public String probank;
	public String accdef;

	public SA0075Card() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getAccno() {
		return accno;
	}

	public void setAccno(String accno) {
		this.accno = accno;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getTrntyp() {
		return trntyp;
	}

	public void setTrntyp(String trntyp) {
		this.trntyp = trntyp;
	}

	public String getLmtamt() {
		return lmtamt;
	}

	public void setLmtamt(String lmtamt) {
		this.lmtamt = lmtamt;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getIfncal() {
		return ifncal;
	}

	public void setIfncal(String ifncal) {
		this.ifncal = ifncal;
	}

	public String getProbank() {
		return probank;
	}

	public void setProbank(String probank) {
		this.probank = probank;
	}

	public String getAccdef() {
		return accdef;
	}

	public void setAccdef(String accdef) {
		this.accdef = accdef;
	}

	// @Override
	// public String toString() {
	// return "CardList [userid=" + userid + ",accno=" + accno + ",alias=" +
	// alias + ",trntyp=" + trntyp + ",lmtamt=" + lmtamt + ",time=" + time +
	// ",ifncal=" + ifncal + ",probank=" + probank + ",accdef=" + accdef;
	// }

}
