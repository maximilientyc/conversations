package com.tipi.conversations.domain.conversations;

import com.tipi.conversations.repository.conversations.ConversationRepository;

/**
 * Created by Maximilien on 07/02/2016.
 */
public class SampleConversationRepository implements ConversationRepository {

	@Override
	public void add(Conversation conversation) {

	}

	@Override
	public void update(Conversation conversation) {

	}

	@Override
	public boolean exists(Conversation conversation) {
		return false;
	}
}
