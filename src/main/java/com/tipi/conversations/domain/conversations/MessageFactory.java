package com.tipi.conversations.domain.conversations;

import java.util.Date;

/**
 * Created by Maximilien on 10/01/2016.
 */
public class MessageFactory {

	private ConversationService conversationService;

	public MessageFactory(ConversationService conversationService) {
		this.conversationService = conversationService;
	}

	public Message buildMessage() {
		Date postedOn = new Date();
		String messageId = conversationService.getNextMessageId();
		Message message = new Message(messageId, postedOn);
		return message;
	}
}
