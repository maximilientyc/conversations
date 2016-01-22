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

	public List<Participant> getParticipants() {
		return participants;
	}

	public int countParticipants() {
		return participants.size();
	}

	public Conversation postMessage(Message message) {
		messages.add(message);
		return this;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public int countMessages() {
		return messages.size();
	}

	public Message getMessage(String messageId) {
		for (Message message : messages) {
			if (message.getMessageId().equals(messageId)) {
				return message;
			}
		}
		return null;
	}

	public void removeParticipant(Participant participant) {
		participants.remove(participant);
	}



}
