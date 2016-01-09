package com.tipi.conversations.domain.model.conversations;

import java.util.List;

/**
 * Created by Maximilien on 09/01/2016.
 */
public class ConversationFactory {

	private ConversationService conversationService;

	public ConversationFactory(ConversationService conversationService) {
		this.conversationService = conversationService;
	}

	public Conversation createConversation() {
		String conversationId = conversationService.getNextConversationId();
		Conversation conversation = new Conversation(conversationId);
		return conversation;
	}

}
