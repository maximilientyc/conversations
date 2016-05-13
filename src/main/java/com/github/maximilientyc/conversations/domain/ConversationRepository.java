package com.github.maximilientyc.conversations.domain;

import java.util.List;

/**
 * Created by @maximilientyc on 07/02/2016.
 */
public interface ConversationRepository {

	void add(Conversation conversation);

	void update(Conversation conversation);

	boolean exists(String conversationId);

	Conversation get(String conversationId);

	List<Conversation> find(ConversationSearchCriteria conversationSearchCriteria);

	long count(ConversationSearchCriteria criteria);
}
