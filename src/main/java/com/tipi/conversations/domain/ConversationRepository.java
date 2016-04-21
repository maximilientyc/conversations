package com.tipi.conversations.domain;

/**
 * Created by @maximilientyc on 07/02/2016.
 */
public interface ConversationRepository {

	void add(Conversation conversation);

	void update(Conversation conversation);

	boolean exists(String conversationId);

	Conversation get(String conversationId);

	long count(ConversationSearchCriteria criteria);
}
