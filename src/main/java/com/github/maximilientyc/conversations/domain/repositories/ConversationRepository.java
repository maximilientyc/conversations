package com.github.maximilientyc.conversations.domain.repositories;

import com.github.maximilientyc.conversations.domain.Conversation;
import com.github.maximilientyc.conversations.domain.ConversationSearchCriteria;
import com.github.maximilientyc.conversations.infrastructure.searches.PaginatedList;

/**
 * Created by @maximilientyc on 07/02/2016.
 */
public interface ConversationRepository {

	void add(Conversation conversation);

	void update(Conversation conversation);

	boolean exists(String conversationId);

	Conversation get(String conversationId);

	PaginatedList<Conversation> find(ConversationSearchCriteria conversationSearchCriteria);

	long count(ConversationSearchCriteria criteria);
}
