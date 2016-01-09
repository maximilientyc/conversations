package com.tipi.conversations.domain.model.conversations;

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

	public List<Participant> getParticipants() {
		return participants;
	}

	public String getConversationId() {
		return conversationId;
	}

	public Conversation addParticipant(Participant participant) {
		this.participants.add(participant);
		return this;
	}

}
