package com.tipi.conversations.domain.conversations;

/**
 * Created by Maximilien on 07/02/2016.
 */
public interface ConversationRepository {

	public void add(Conversation conversation);

	public void update(Conversation conversation);

	public boolean exists(Conversation conversation);
}
