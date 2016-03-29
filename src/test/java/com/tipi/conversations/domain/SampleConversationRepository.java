package com.tipi.conversations.domain;

import com.tipi.conversations.domain.Conversation;
import com.tipi.conversations.domain.ConversationRepository;

/**
 * Created by @maximilientyc on 07/02/2016.
 */
public class SampleConversationRepository implements ConversationRepository {

	@Override
	public void add(Conversation conversation) {

	}

	@Override
	public void update(Conversation conversation) {

	}

	@Override
	public boolean exists(String conversationId) {
		return false;
	}

	@Override
	public Conversation get(String conversationId) {
		return null;
	}
}
