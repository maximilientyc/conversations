package com.github.maximilientyc.conversations.domain;

import com.github.maximilientyc.conversations.infrastructure.searches.SearchCriteria;

/**
 * Created by @maximilientyc on 03/04/2016.
 */
public class ConversationSearchCriteria extends SearchCriteria {

	private String userId;

	public ConversationSearchCriteria(int firstRowNumber, int maxRowCount) {
		super(firstRowNumber, maxRowCount);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
