package com.bocop.jxplatform.bean;

import java.util.List;

public class SA0075 {
	
	public String pageno;
	public String rcdcnt;
	
	public List<SA0075Card> saplist;






	public SA0075() {
		super();
		// TODO Auto-generated constructor stub
	}



	public String getPageno() {
		return pageno;
	}



	public void setPageno(String pageno) {
		this.pageno = pageno;
	}



	public String getRcdcnt() {
		return rcdcnt;
	}

	public void setRcdcnt(String rcdcnt) {
		this.rcdcnt = rcdcnt;
	}

	public List<SA0075Card> getSaplist() {
		return saplist;
	}

	public void setSaplist(List<SA0075Card> saplist) {
		this.saplist = saplist;
	}

//	@Override
//	public String toString() {
//		// TODO Auto-generated method stub
//		return "pageno:" + pageno + "rcdcnt:" + rcdcnt + "saplist:" + saplist.toString()  ;
//	}
	
}
