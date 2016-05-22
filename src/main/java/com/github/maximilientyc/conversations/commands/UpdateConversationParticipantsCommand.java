package com.github.maximilientyc.conversations.commands;

import com.github.maximilientyc.conversations.domain.Conversation;
import com.github.maximilientyc.conversations.domain.ConversationFactory;
import com.github.maximilientyc.conversations.domain.Participant;
import com.github.maximilientyc.conversations.domain.ParticipantFactory;
import com.github.maximilientyc.conversations.domain.repositories.ConversationRepository;
import com.github.maximilientyc.conversations.domain.services.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by @maximilientyc on 07/02/2016.
 */
public class UpdateConversationParticipantsCommand {

	private final String conversationId;
	private final Collection<String> userIds;
	private final ConversationFactory conversationFactory;
	private final ParticipantFactory participantFactory;
	private final ConversationRepository conversationRepository;
	private final UserService userService;

	public UpdateConversationParticipantsCommand(String conversationId, Collection<String> userIds, ConversationFactory conversationFactory, ParticipantFactory participantFactory, ConversationRepository conversationRepository, UserService userService) {
		this.conversationId = conversationId;
		this.userIds = userIds;
		this.conversationFactory = conversationFactory;
		this.participantFactory = participantFactory;
		this.conversationRepository = conversationRepository;
		this.userService = userService;
		validate();
	}

	public void execute() {
		Conversation conversation = conversationRepository.get(conversationId);

		// add new participants
		for (String userId : userIds) {
			if (!conversation.containsParticipant(userId)) {
				Participant newParticipant = participantFactory.buildParticipant(userId);
				conversation.addParticipant(newParticipant);
			}
		}

		// remove old participants
		List<Participant> toBeDeletedParticipants = new ArrayList<Participant>();
		for (Participant participant : conversation.getParticipants()) {
			String userId = participant.getUser().getUserId();
			if (!userIds.contains(userId)) {
				toBeDeletedParticipants.add(participant);
			}
		}
		for (Participant participant : toBeDeletedParticipants) {
			conversation.removeParticipant(participant);
		}

		conversationRepository.update(conversation);
	}

	public void validate() {
		validateLoggedInUserIsAParticipant();
		validateCorrectNumberOfParticipants();
		validateConversationExists();
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

	private void validateConversationExists() {
		boolean conversationExists = conversationRepository.exists(conversationId);
		if (!conversationExists) {
			throw new IllegalArgumentException("Cannot update conversation, reason: a conversation with id '" + conversationId + "' does not exist.");
		}
	}
}
