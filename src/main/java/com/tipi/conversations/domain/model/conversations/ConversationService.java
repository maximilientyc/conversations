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
		if (conversation.getParticipants().size() < 2) {
			throw new IllegalArgumentException("Cannot add conversation, reason: not enough participants.");
		}
		conversationRepository.save(conversation);
	}

	public void update(Conversation conversation) {
		if (conversation.getParticipants().size() < 2) {
			throw new IllegalArgumentException("Cannot update conversation, reason: not enough participants.");
		}
		conversationRepository.save(conversation);
	}

	public Conversation getByConversationId(String conversationId) {
		return conversationRepository.findOne(conversationId);
	}

	public String getNextConversationId() {
		return UUID.randomUUID().toString();
	}


}
