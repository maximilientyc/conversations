package com.tipi.conversations.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by @maximilientyc on 03/01/2016.
 */
public class Conversation {

	private final String conversationId;
	private final List<Participant> participants;

	public Conversation(String conversationId) {
		this.conversationId = conversationId;
		this.participants = new ArrayList<>();
	}

	public String getConversationId() {
		return conversationId;
	}

	public Conversation addParticipant(Participant participant) {
		for (Participant participant1 : participants) {
			if (participant.equals(participant1)) {
				throw new IllegalArgumentException("Cannot add participant, reason: already exists.");
			}
		}
		this.participants.add(participant);
		return this;
	}

	public int countParticipants() {
		return participants.size();
	}

	public void removeParticipant(Participant participant) {
		if (countParticipants() == 2) {
			throw new IllegalArgumentException("Cannot leave conversation, reason: not enough getParticipants.");
		}
		participants.remove(participant);
	}

	public List<Participant> getParticipants() {
		return participants;
	}

}
