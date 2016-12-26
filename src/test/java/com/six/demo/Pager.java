package com.six.demo;

public class Pager {

	private Integer totalCount;
	private Integer currentPage;
	private Integer pageSize;
	private Integer totalPage;

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	@Override
	public String toString() {
		return "Pager [totalCount=" + totalCount + ", currentPage="
				+ currentPage + ", pageSize=" + pageSize + ", totalPage="
				+ totalPage + "]";
	}
}
