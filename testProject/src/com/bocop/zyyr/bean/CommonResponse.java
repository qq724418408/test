package com.bocop.zyyr.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 产品列表、贷款列表公共部分
 * 
 * @author lh
 * 
 */
public class CommonResponse {

	@XStreamAlias("TOTAL_PAGES")//总页数
	private String totalPages; 
	@XStreamAlias("CURRENT_PAGE")//当前页
	private String currentPage;
	@XStreamAlias("PAGE_RECORDEERS")//每页记录数
	private String pageRecords;
	@XStreamAlias("TOTAL_ROWS")//总记录数
	private String totalRows;
	public String getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(String totalPages) {
		this.totalPages = totalPages;
	}
	public String getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
	public String getPageRecords() {
		return pageRecords;
	}
	public void setPageRecords(String pageRecords) {
		this.pageRecords = pageRecords;
	}
	public String getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(String totalRows) {
		this.totalRows = totalRows;
	}
	
	
}
