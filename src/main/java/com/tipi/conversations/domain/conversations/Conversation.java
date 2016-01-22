package com.tipi.conversations.domain.conversations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maximilien on 03/01/2016.
 */
public class Conversation {

	private final String conversationId;
	private List<Participant> participants;
	private List<Message> messages;

	public Conversation(String conversationId) {
		this.conversationId = conversationId;
		this.participants = new ArrayList<>();
		this.messages = new ArrayList<>();
	}

	public Conversation addParticipant(Participant participant) {
		this.participants.add(participant);
		return this;
	}

	public int countParticipants() {
		return participants.size();
	}

	public Conversation postMessage(Message message) {
		if (!participants.contains(message.postedBy())) {
			throw new IllegalArgumentException("Cannot post message, reason: " + message.postedBy().getName() + " is not a participant.");
		}
		messages.add(message);
		return this;
	}


	public int countMessages() {
		return messages.size();
	}

	public void removeParticipant(Participant participant) {
		participants.remove(participant);
	}

}
