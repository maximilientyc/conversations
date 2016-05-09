package com.github.maximilientyc.conversations.commands;

import com.github.maximilientyc.conversations.domain.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
		conversationRepository.add(conversation);

		return conversation;
	}

	private void validate() {
		validateLoggedInUserIsAParticipant();
		validateCorrectNumberOfParticipants();
	}

	private void validateLoggedInUserIsAParticipant() {
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

	private void validateCorrectNumberOfParticipants() {
		List<String> userIdList = new ArrayList<String>();
		Iterator<String> iterator = userIds.iterator();
		iterator.forEachRemaining(userIdList::add);
		if (userIdList.size() < 2) {
			throw new IllegalArgumentException("Cannot create conversation, reason: not enough participants.");
		}
	}
}
