package com.tipi.conversations.domain.conversations;

import com.tipi.conversations.domain.users.User;

/**
 * Created by @maximilientyc on 16/01/2016.
 */
public class ParticipantFactory {

	private final ConversationService conversationService;

	public ParticipantFactory(ConversationService conversationService) {
		this.conversationService = conversationService;
	}

	public Participant buildParticipant(User user) {
		return new Participant(user);
	}
}
