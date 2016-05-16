package com.github.maximilientyc.conversations.commands;

import com.github.maximilientyc.conversations.domain.*;
import com.github.maximilientyc.conversations.domain.repositories.ConversationRepository;
import com.github.maximilientyc.conversations.domain.services.UserService;

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
		new ConversationCommandValidator().validate(this);
	}

	public Conversation execute() {
		Conversation conversation = conversationFactory.buildConversation();
		Iterator<String> userIdIterator = userIds.iterator();
		while (userIdIterator.hasNext()) {
			conversation.addParticipant(participantFactory.buildParticipant(userIdIterator.next()));
		}
		conversationRepository.add(conversation);

		return conversation;
	}

	public UserService getUserService() {
		return userService;
	}

	public Iterable<String> getUserIds() {
		return userIds;
	}

}
