package com.github.maximilientyc.conversations.commands;

import com.github.maximilientyc.conversations.domain.Conversation;
import com.github.maximilientyc.conversations.domain.ConversationFactory;
import com.github.maximilientyc.conversations.domain.ParticipantFactory;
import com.github.maximilientyc.conversations.domain.repositories.ConversationRepository;
import com.github.maximilientyc.conversations.domain.services.UserService;

import java.util.Collection;

/**
 * Created by @maximilientyc on 07/02/2016.
 */
public class CreateConversationCommand {

	private final Collection<String> userIds;
	private final ConversationFactory conversationFactory;
	private final ParticipantFactory participantFactory;
	private final ConversationRepository conversationRepository;
	private final UserService userService;

	public CreateConversationCommand(Collection<String> userIds, ConversationFactory conversationFactory, ParticipantFactory participantFactory, ConversationRepository conversationRepository, UserService userService) {
		this.userIds = userIds;
		this.conversationFactory = conversationFactory;
		this.participantFactory = participantFactory;
		this.conversationRepository = conversationRepository;
		this.userService = userService;
		validate();
	}

	public Conversation execute() {
		Conversation conversation = conversationFactory.buildConversation();
		for (String userId : userIds) {
			conversation.addParticipant(participantFactory.buildParticipant(userId));
		}
		conversationRepository.add(conversation);

		return conversation;
	}

	public void validate() {
		validateLoggedInUserIsAParticipant();
		validateCorrectNumberOfParticipants();
	}

	// validators collection
	private void validateLoggedInUserIsAParticipant() {
		String loggedInUserId = userService.getLoggedInUserId();
		if (!userIds.contains(loggedInUserId)) {
			throw new IllegalArgumentException("Current logged in user '" + loggedInUserId + "' is not a conversation member.");
		}
	}

	private void validateCorrectNumberOfParticipants() {
		if (userIds.size() < 2) {
			throw new IllegalArgumentException("Cannot create conversation, reason: not enough participants.");
		}
	}
}
