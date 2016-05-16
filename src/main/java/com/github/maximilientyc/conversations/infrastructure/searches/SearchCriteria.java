package com.github.maximilientyc.conversations.infrastructure.searches;

public abstract class SearchCriteria {

	private int firstRowNumber;
	private int maxRowCount;
	private String sortCriteria;
	private String sortDirection;

	public SearchCriteria(int firstRowNumber, int maxRowCount) {
		super();
		this.firstRowNumber = firstRowNumber;
		this.maxRowCount = maxRowCount;
	}

	public SearchCriteria(int firstRowNumber, int maxRowCount, String sortCriteria, String sortDirection) {
		super();
		this.firstRowNumber = firstRowNumber;
		this.maxRowCount = maxRowCount;
		this.sortCriteria = sortCriteria;
		this.sortDirection = sortDirection;
	}

	public int getFirstRowNumber() {
		return firstRowNumber;
	}

	public void setFirstRowNumber(int firstRowNumber) {
		this.firstRowNumber = firstRowNumber;
	}

	public int getMaxRowCount() {
		return maxRowCount;
	}

	public void setMaxRowCount(int maxRowCount) {
		this.maxRowCount = maxRowCount;
	}

	public String getSortCriteria() {
		return sortCriteria;
	}

	public void setSortCriteria(String sortCriteria) {
		this.sortCriteria = sortCriteria;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

}
