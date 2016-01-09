package com.tipi.conversations.domains.conversations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maximilien on 03/01/2016.
 */
public class Conversation {

	private final String conversationId;
	private List<Participant> participants = new ArrayList<>();

	public Conversation(String conversationId) {
		this.conversationId = conversationId;
	}

	public void addParticipant(Participant participant) {
		participants.add(participant);
	}

	public List<Participant> getParticipants() {
		return participants;
	}

	public String getConversationId() {
		return conversationId;
	}
}
