package com.tipi.conversations.repository.conversations;

import com.tipi.conversations.domain.conversations.Conversation;

/**
 * Created by Maximilien on 07/02/2016.
 */
public interface ConversationRepository {

	public void add(Conversation conversation);

	public void update(Conversation conversation);

	public boolean exists(Conversation conversation);
}
