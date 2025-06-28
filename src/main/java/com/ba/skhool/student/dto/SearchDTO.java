package com.ba.skhool.student.dto;

import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SearchDTO implements SortableRequest {

	private int pageSize;

	private int pageNumber;

	private List<FilterCriteria> filters;

	private List<SortField> sortFields;

	@Override
	public List<SortField> getSortFields() {
		return sortFields;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public List<FilterCriteria> getFilters() {
		return filters;
	}

	public void setFilters(List<FilterCriteria> filters) {
		this.filters = filters;
	}

	public void setSortFields(List<SortField> sortFields) {
		this.sortFields = sortFields;
	}

}
