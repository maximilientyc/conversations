package com.github.maximilientyc.conversations.domain;

import com.github.maximilientyc.conversations.infrastructure.searches.SearchCriteria;

/**
 * Created by @maximilientyc on 21/03/2016.
 */
public class MessageSearchCriteria extends SearchCriteria {

	private String conversationId;

	public MessageSearchCriteria(int firstRowNumber, int maxRowCount) {
		super(firstRowNumber, maxRowCount);
	}

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
}
