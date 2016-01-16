package com.tipi.conversations.domain.model.conversations;

/**
 * Created by Maximilien on 16/01/2016.
 */
public class ParticipantFactory {

	private ConversationService conversationService;

	public ParticipantFactory(ConversationService conversationService) {
		this.conversationService = conversationService;
	}

	public Participant buildParticipant() {
		return new Participant();
	}
}
