package com.tipi.conversations.domains.conversations;

import com.tipi.conversations.domains.users.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Maximilien on 03/01/2016.
 */
public class Conversation {

	private final String conversationId;
	private List<User> participants = new ArrayList<>();

	public Conversation(String conversationId) {
		this.conversationId = conversationId;
	}

	public void addParticipant(User participant) {
		participants.add(participant);
	}

	public List<User> getParticipants() {
		return participants;
	}

	public String getConversationId() {
		return conversationId;
	}
}
