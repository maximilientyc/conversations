package com.github.maximilientyc.conversations.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by @maximilientyc on 03/01/2016.
 */
public class Conversation {

	private final String conversationId;
	private List<Participant> participants;
	private Date lastActiveOn;

	public Conversation(String conversationId) {
		this.conversationId = conversationId;
		this.participants = new ArrayList<>();
		this.lastActiveOn = null;
	}

	public String getConversationId() {
		return conversationId;
	}

	public Conversation addParticipant(Participant participant) {
		if (containsParticipant(participant)) {
			throw new IllegalArgumentException("Cannot add participant, reason: already exists.");
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

	public Participant getParticipant(String userId) {
		for (Participant participant : participants) {
			if (participant.getUser().getUserId().equals(userId)) {
				return participant;
			}
		}
		throw new IllegalArgumentException("Cannot find participant, reason: participant with userId '" + userId + "' does not exist.");
	}

	public List<Participant> getParticipants() {
		return participants;
	}

	private boolean containsParticipant(Participant participant) {
		String userId = participant.getUser().getUserId();
		return containsParticipant(userId);
	}

	public boolean containsParticipant(String userId) {
		for (Participant participant : participants) {
			if (participant.getUser().getUserId().equals(userId)) {
				return true;
			}
		}
		return false;
	}

	public Date getLastActiveOn() {
		return lastActiveOn;
	}

	public void setLastActiveOn(Date lastActiveOn) {
		this.lastActiveOn = lastActiveOn;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Conversation that = (Conversation) o;
		return Objects.equals(conversationId, that.conversationId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(conversationId);
	}
}
