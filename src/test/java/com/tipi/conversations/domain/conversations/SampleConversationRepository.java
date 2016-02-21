package com.tipi.conversations.domain.conversations;

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
	public boolean exists(Conversation conversation) {
		return false;
	}

	@Override
	public Conversation get(String conversationId) {
		return null;
	}
}
