package com.github.maximilientyc.conversations.infrastructure.searches;

import java.util.ArrayList;
import java.util.List;

public class PaginatedList<E> {

	private int totalRowCount;
	private List<E> itemList = new ArrayList<E>();

	public PaginatedList(int totalRowCount, List<E> itemList) {
		super();
		this.totalRowCount = totalRowCount;
		this.itemList = itemList;
	}

	public int getTotalRowCount() {
		return totalRowCount;
	}

	public void setTotalRowCount(int totalRowCount) {
		this.totalRowCount = totalRowCount;
	}

	public List<E> getItemList() {
		return itemList;
	}

	public void setItemList(List<E> itemList) {
		this.itemList = itemList;
	}

}
