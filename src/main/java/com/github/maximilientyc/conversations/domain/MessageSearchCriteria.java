package com.github.maximilientyc.conversations.domain;

/**
 * Created by @maximilientyc on 21/03/2016.
 */
public class MessageSearchCriteria {

	private final String conversationId;

	public MessageSearchCriteria(String conversationId) {
		this.conversationId = conversationId;
	}

	public String getConversationId() {
		return conversationId;
	}
}
