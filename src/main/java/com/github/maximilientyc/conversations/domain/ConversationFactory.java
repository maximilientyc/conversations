package com.github.maximilientyc.conversations.domain;

import com.github.maximilientyc.conversations.domain.services.ConversationService;

/**
 * Created by @maximilientyc on 09/01/2016.
 */
public class ConversationFactory {

	private final ConversationService conversationService;

	public ConversationFactory(ConversationService conversationService) {
		this.conversationService = conversationService;
	}

	public Conversation buildConversation() {
		String conversationId = conversationService.getNextConversationId();
		return new Conversation(conversationId);
	}

}
