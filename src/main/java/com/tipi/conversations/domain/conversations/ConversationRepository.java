package com.tipi.conversations.domain.conversations;

/**
 * Created by @maximilientyc on 07/02/2016.
 */
public interface ConversationRepository {

	void add(Conversation conversation);

	void update(Conversation conversation);

	boolean exists(Conversation conversation);

	Conversation get(String conversationId);
}
