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
public class UpdateConversationParticipantsCommand extends ConversationCommand {

	private final String conversationId;
	private final ConversationFactory conversationFactory;
	private final ParticipantFactory participantFactory;
	private final ConversationRepository conversationRepository;

	public UpdateConversationParticipantsCommand(String conversationId, Collection<String> userIds, ConversationFactory conversationFactory, ParticipantFactory participantFactory, ConversationRepository conversationRepository, UserService userService) {
		super(userIds, userService);
		this.conversationId = conversationId;
		this.conversationFactory = conversationFactory;
		this.participantFactory = participantFactory;
		this.conversationRepository = conversationRepository;
		validate();
	}

	public void execute() {
		Conversation conversation = conversationRepository.get(conversationId);

		// add new participants
		for (String userId : getUserIds()) {
			if (!conversation.containsParticipant(userId)) {
				Participant newParticipant = participantFactory.buildParticipant(userId);
				conversation.addParticipant(newParticipant);
			}
		}

		// remove old participants
		List<Participant> toBeDeletedParticipants = new ArrayList<Participant>();
		for (Participant participant : conversation.getParticipants()) {
			String userId = participant.getUser().getUserId();
			if (!getUserIds().contains(userId)) {
				toBeDeletedParticipants.add(participant);
			}
		}
		for (Participant participant : toBeDeletedParticipants) {
			conversation.removeParticipant(participant);
		}

		conversationRepository.update(conversation);
	}

	@Override
	public void validate() {
		validateConversationExists();
		super.validate();
	}

	// validators collection
	private void validateConversationExists() {
		boolean conversationExists = conversationRepository.exists(conversationId);
		if (!conversationExists) {
			throw new IllegalArgumentException("Cannot update conversation, reason: a conversation with id '" + conversationId + "' does not exist.");
		}
	}
}
