package com.tipi.conversations.domain.model.conversations;

/**
 * Created by Maximilien on 09/01/2016.
 */
public interface ConversationRepository {

	void save(Conversation conversation);

	Conversation findOne(String conversationId);

	Iterable<Conversation> findAll();
}
