package com.tipi.conversations.domains.conversations.repositories;

import com.tipi.conversations.domains.conversations.Conversation;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Maximilien on 03/01/2016.
 */
public class InMemoryConversationsRepository implements CrudRepository<Conversation, String> {

	private List<Conversation> conversations = new ArrayList<>();

	@Override
	public <S extends Conversation> S save(S conversation) {
		conversations.add(conversation);
		return null;
	}

	@Override
	public <S extends Conversation> Iterable<S> save(Iterable<S> conversations) {
		return null;
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
	public boolean exists(String s) {
		return false;
	}

	@Override
	public Iterable<Conversation> findAll() {
		return conversations;
	}

	@Override
	public Iterable<Conversation> findAll(Iterable<String> stringIterable) {
		return null;
	}

	@Override
	public long count() {
		return 0;
	}

	@Override
	public void delete(String s) {

	}

	@Override
	public void delete(Conversation entity) {

	}

	@Override
	public void delete(Iterable<? extends Conversation> entities) {

	}

	@Override
	public void deleteAll() {

	}
}
