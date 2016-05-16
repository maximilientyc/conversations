package com.github.maximilientyc.conversations.domain.services;

import com.github.maximilientyc.conversations.domain.ConversationSearchCriteria;
import com.github.maximilientyc.conversations.domain.MessageSearchCriteria;
import com.github.maximilientyc.conversations.domain.repositories.ConversationRepository;
import com.github.maximilientyc.conversations.domain.repositories.MessageRepository;

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
		ConversationSearchCriteria conversationSearchCriteria = new ConversationSearchCriteria(0, Integer.MAX_VALUE);
		return conversationRepository.count(conversationSearchCriteria);
	}

	public long countMessages(String conversationId) {
		return messageRepository.count(new MessageSearchCriteria(conversationId));
	}

}
