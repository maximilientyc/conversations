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
		if (exists(conversation.getConversationId())) {
			Conversation storedConversation = findOne(conversation.getConversationId());
			conversations.remove(storedConversation);
		}
		conversations.add(conversation);
	}

	@Override
	public boolean exists(String conversationId) {
		Iterator<Conversation> iterator = findAll().iterator();
		while (iterator.hasNext()) {
			Conversation conversation = iterator.next();
			if (conversation.getConversationId().equals(conversationId)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Conversation findOne(String conversationId) {
		if (conversationId == null) {
			throw new IllegalArgumentException("conversationId parameter should not be null.");
		}
		if (!exists(conversationId)) {
			return null;
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
