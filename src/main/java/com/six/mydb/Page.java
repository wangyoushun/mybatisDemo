package com.six.mydb;

import java.util.List;

public class Page {
	private long total;
	private int page;
	private int pageSize;
	private int totalPage;
	private List<Object> result;

	public Page() {
		this.page = 1;
		this.pageSize = 10;
	}

	public Page(long total, int pageSize) {
		super();
		this.total = total;
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return (page - 1) * pageSize;
	}

	public int getTotalPage() {
		return (totalPage - 1) / pageSize + 1;
	}

	public void setTotalPage() {
		this.totalPage = (totalPage - 1) / pageSize + 1;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
		setTotalPage();
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<Object> getResult() {
		return result;
	}

	public void setResult(List<Object> result) {
		this.result = result;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

}
