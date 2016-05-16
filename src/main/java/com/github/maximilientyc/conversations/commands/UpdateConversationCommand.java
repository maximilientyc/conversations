package com.github.maximilientyc.conversations.commands;

import com.github.maximilientyc.conversations.domain.*;
import com.github.maximilientyc.conversations.domain.repositories.ConversationRepository;
import com.github.maximilientyc.conversations.domain.services.UserService;

import java.util.*;

/**
 * Created by @maximilientyc on 07/02/2016.
 */
public class UpdateConversationCommand {

	private final String conversationId;
	private final Iterable<String> userIds;
	private final ConversationFactory conversationFactory;
	private final ParticipantFactory participantFactory;
	private final ConversationRepository conversationRepository;
	private UserService userService;

	public UpdateConversationCommand(String conversationId, Iterable<String> userIds, ConversationFactory conversationFactory, ParticipantFactory participantFactory, ConversationRepository conversationRepository, UserService userService) {
		this.conversationId = conversationId;
		this.userIds = userIds;
		this.conversationFactory = conversationFactory;
		this.participantFactory = participantFactory;
		this.conversationRepository = conversationRepository;
		this.userService = userService;
		new ConversationCommandValidator().validate(this);
	}

	public void execute() {
		Conversation conversation = conversationRepository.get(conversationId);

		// add new participants
		Iterator<String> userIdIterator = userIds.iterator();
		List<String> userIdList = new ArrayList<String>();
		while (userIdIterator.hasNext()) {
			String userId = userIdIterator.next();
			if (!conversation.containsParticipant(userId)) {
				Participant newParticipant = participantFactory.buildParticipant(userId);
				conversation.addParticipant(newParticipant);
			}
			userIdList.add(userId);
		}

		// remove old participants
		Set<Participant> toBeDeletedParticipants = new HashSet<Participant>();
		for (Participant participant : conversation.getParticipants()) {
			String userId = participant.getUser().getUserId();
			if (!userIdList.contains(userId)) {
				toBeDeletedParticipants.add(participant);
			}
		}
		for (Participant participant : toBeDeletedParticipants) {
			conversation.removeParticipant(participant);
		}

		conversationRepository.update(conversation);
	}

	public UserService getUserService() {
		return userService;
	}

	public String getConversationId() {
		return conversationId;
	}

	public Iterable<String> getUserIds() {
		return userIds;
	}

	public ConversationRepository getConversationRepository() {
		return conversationRepository;
	}
}
