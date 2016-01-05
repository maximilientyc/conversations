package com.tipi.conversations.infrastructure.sequences;

/**
 * Created by Maximilien on 05/01/2016.
 */
public class Sequences {

	private int currentUserId = 0;
	private int conversationId = 0;

	public String getNextUserId() {
		currentUserId = currentUserId + 1;
		return String.valueOf(currentUserId);
	}

	public String getNextConversationId() {
		conversationId = conversationId + 1;
		return String.valueOf(conversationId);
	}

}
