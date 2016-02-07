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

	public String getConversationId() {
		return conversationId;
	}

	public Conversation addParticipant(Participant participant) {
		this.participants.add(participant);
		return this;
	}

	public int countParticipants() {
		return participants.size();
	}

	public void postMessage(Message message) {
		if (!participants.contains(message.postedBy())) {
			throw new IllegalArgumentException("Cannot post message, reason: not a participant.");
		}
		messages.add(message);
	}
	
	public int countMessages() {
		return messages.size();
	}

	public void removeParticipant(Participant participant) {
		if (countParticipants() == 2) {
			throw new IllegalArgumentException("Cannot leave conversation, reason: not enough participants.");
		}
		participants.remove(participant);
	}

}
