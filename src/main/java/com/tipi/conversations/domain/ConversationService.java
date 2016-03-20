package com.tipi.conversations.domain;

import java.util.UUID;

/**
 * Created by @maximilientyc on 03/01/2016.
 */
public class ConversationService {

	private final ConversationRepository conversationRepository;

	public ConversationService(ConversationRepository conversationRepository) {
		this.conversationRepository = conversationRepository;
	}

	public String getNextConversationId() {
		return UUID.randomUUID().toString();
	}

	public String getNextMessageId() {
		return UUID.randomUUID().toString();
	}

	public void postMessage(Message message) {
		Conversation conversation = conversationRepository.get(message.getConversationId());
		boolean conversationContainsMessageParticipant = conversation.getParticipants().contains(message.getPostedBy());
		if (!conversationContainsMessageParticipant) {
			throw new IllegalArgumentException("Cannot post message, reason: not a participant.");
		}
	}
}
