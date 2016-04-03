package com.tipi.conversations.domain;

import java.util.UUID;

/**
 * Created by @maximilientyc on 03/01/2016.
 */
public class ConversationService {

	private final ConversationRepository conversationRepository;
	private final MessageRepository messageRepository;

	public ConversationService(ConversationRepository conversationRepository, MessageRepository messageRepository) {
		this.conversationRepository = conversationRepository;
		this.messageRepository = messageRepository;
	}

	public String getNextConversationId() {
		return UUID.randomUUID().toString();
	}

	public String getNextMessageId() {
		return UUID.randomUUID().toString();
	}

	public long countConversations() {
		return conversationRepository.count(new ConversationSearchCriteria());
	}

	public long countMessages(String conversationId) {
		return messageRepository.count(new MessageSearchCriteria(conversationId));
	}

}
