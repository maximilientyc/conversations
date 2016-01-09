package com.tipi.conversations.domains.conversations;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by Maximilien on 03/01/2016.
 */
public class ConversationService {

	private CrudRepository<Conversation, String> conversationRepository;

	public ConversationService(CrudRepository conversationRepository) {
		this.conversationRepository = conversationRepository;
	}

	public void add(Conversation conversation) {
		if (conversation.getParticipants().size() < 2) {
			throw new IllegalArgumentException("Cannot add conversation, reason: not enough participants.");
		}
		conversationRepository.save(conversation);
	}

	public Conversation getByConversationId(String conversationId) {
		return conversationRepository.findOne(conversationId);
	}

}
