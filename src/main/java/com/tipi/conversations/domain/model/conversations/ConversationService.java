package com.tipi.conversations.domain.model.conversations;

import java.util.UUID;

/**
 * Created by Maximilien on 03/01/2016.
 */
public class ConversationService {

	private ConversationRepository conversationRepository;

	public ConversationService(ConversationRepository conversationRepository) {
		this.conversationRepository = conversationRepository;
	}

	public void add(Conversation conversation) {
		validateConversation(conversation);
		validateCreateConversation(conversation);
		conversationRepository.save(conversation);
	}

	private void validateCreateConversation(Conversation conversation) {
		if (conversationRepository.exists(conversation.getConversationId())) {
			throw new IllegalArgumentException("Cannot add conversation, reason: conversation already exists.");
		}
	}

	public void update(Conversation conversation) {
		validateConversation(conversation);
		validateUpdateConversation(conversation);
		conversationRepository.save(conversation);
	}

	private void validateUpdateConversation(Conversation conversation) {
		if (!conversationRepository.exists(conversation.getConversationId())) {
			throw new IllegalArgumentException("Cannot update conversation, reason: conversation does not exists.");
		}
	}

	private void validateConversation(Conversation conversation) {
		if (conversation.getParticipants().size() < 2) {
			throw new IllegalArgumentException("Cannot add conversation, reason: not enough participants.");
		}
	}

	public Conversation getByConversationId(String conversationId) {
		return conversationRepository.findOne(conversationId);
	}

	public String getNextConversationId() {
		return UUID.randomUUID().toString();
	}


	public String getNextMessageId() {
		return UUID.randomUUID().toString();
	}

}
