package com.tipi.conversations.domain.model.conversations;

/**
 * Created by Maximilien on 10/01/2016.
 */
public class MessageFactory {

	private ConversationService conversationService;

	public MessageFactory(ConversationService conversationService) {
		this.conversationService = conversationService;
	}

	public Message buildMessage() {
		Message message = new Message(conversationService.getNextMessageId());
		return message;
	}
}
