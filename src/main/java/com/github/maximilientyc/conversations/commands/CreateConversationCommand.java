package com.github.maximilientyc.conversations.commands;

import com.github.maximilientyc.conversations.domain.*;

import java.util.Iterator;

/**
 * Created by @maximilientyc on 07/02/2016.
 */
public class CreateConversationCommand {

	private final Iterable<String> userIds;
	private final ConversationFactory conversationFactory;
	private final ParticipantFactory participantFactory;
	private final ConversationRepository conversationRepository;
	private final UserService userService;

	public CreateConversationCommand(Iterable<String> userIds, ConversationFactory conversationFactory, ParticipantFactory participantFactory, ConversationRepository conversationRepository, UserService userService) {
		this.userIds = userIds;
		this.conversationFactory = conversationFactory;
		this.participantFactory = participantFactory;
		this.conversationRepository = conversationRepository;
		this.userService = userService;
	}

	public Conversation execute() {
		validate();

		Conversation conversation = conversationFactory.buildConversation();
		Iterator<String> userIdIterator = userIds.iterator();
		while (userIdIterator.hasNext()) {
			conversation.addParticipant(participantFactory.buildParticipant(userIdIterator.next()));
		}

		if (conversation.countParticipants() < 2) {
			throw new IllegalArgumentException("Cannot create conversation, reason: not enough participants.");
		}
		conversationRepository.add(conversation);

		return conversation;
	}

	private void validate() {
		String loggedInUserId = userService.getLoggedInUserId();

		boolean loggedInUserIsAConversationMember = false;
		Iterator<String> iterator = userIds.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().equals(loggedInUserId)) {
				loggedInUserIsAConversationMember = true;
				break;
			}
		}
		if (!loggedInUserIsAConversationMember) {
			throw new IllegalArgumentException("Current logged in user '" + loggedInUserId + "' is not a conversation member.");
		}
	}

}
