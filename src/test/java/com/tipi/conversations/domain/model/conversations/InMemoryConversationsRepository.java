package com.tipi.conversations.domain.model.conversations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Maximilien on 03/01/2016.
 */
public class InMemoryConversationsRepository implements ConversationRepository {

	private List<Conversation> conversations = new ArrayList<>();

	@Override
	public void save(Conversation conversation) {
		conversations.add(conversation);
	}

	@Override
	public Conversation findOne(String conversationId) {
		if (conversationId == null) {
			throw new IllegalArgumentException("conversationId parameter should not be null.");
		}
		Iterator<Conversation> iterator = findAll().iterator();
		while (iterator.hasNext()) {
			Conversation currentConversation = iterator.next();
			if (currentConversation.getConversationId().equals(conversationId)) {
				return currentConversation;
			}
		}
		return null;
	}

	@Override
	public List<Conversation> findAll() {
		return conversations;
	}

}
