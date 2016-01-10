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
		validateConversation(conversation, "Cannot add conversation, reason: not enough participants.");
		if (conversationRepository.exists(conversation.getConversationId())) {
			throw new IllegalArgumentException("Cannot add conversation, reason: conversation already exists.");
		}
		conversationRepository.save(conversation);
	}

	public void update(Conversation conversation) {
		validateConversation(conversation, "Cannot update conversation, reason: not enough participants.");
		if (!conversationRepository.exists(conversation.getConversationId())) {
			throw new IllegalArgumentException("Cannot update conversation, reason: conversation does not exists.");
		}
		conversationRepository.save(conversation);
	}

	private void validateConversation(Conversation conversation, String s) {
		if (conversation.getParticipants().size() < 2) {
			throw new IllegalArgumentException(s);
		}
	}

	public Conversation getByConversationId(String conversationId) {
		return conversationRepository.findOne(conversationId);
	}

	public String getNextConversationId() {
		return UUID.randomUUID().toString();
	}


}
