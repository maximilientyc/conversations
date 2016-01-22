package com.tipi.conversations.domain.conversations;

/**
 * Created by Maximilien on 30/12/2015.
 */
public class Participant {

	private String name;

	public String getName() {
		return name;
	}

	public Participant setName(String name) {
		this.name = name;
		return this;
	}

	public void leaveConversation(Conversation conversation) {
		if (conversation.countParticipants() == 2) {
			throw new IllegalArgumentException("Cannot leave conversation, reason: not enough participants.");
		}
		conversation.removeParticipant(this);
	}

}
