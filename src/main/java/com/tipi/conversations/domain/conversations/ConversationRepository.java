package com.tipi.conversations.domain.conversations;

import java.util.List;

/**
 * Created by Maximilien on 09/01/2016.
 */
public interface ConversationRepository {

	void save(Conversation conversation);

	Conversation findOne(String conversationId);

	boolean exists(String conversationId);

	List<Conversation> findAll();
}
