package com.github.maximilientyc.conversations.commands;

import com.github.maximilientyc.conversations.domain.*;

import java.util.Iterator;

/**
 * Created by @maximilientyc on 07/02/2016.
 */
public class UpdateConversationCommand {

	private final String conversationId;
	private final Iterable<String> userIds;
	private final ConversationFactory conversationFactory;
	private final ParticipantFactory participantFactory;
	private final ConversationRepository conversationRepository;

	public UpdateConversationCommand(String conversationId, Iterable<String> userIds, ConversationFactory conversationFactory, ParticipantFactory participantFactory, ConversationRepository conversationRepository) {
		this.conversationId = conversationId;
		this.userIds = userIds;
		this.conversationFactory = conversationFactory;
		this.participantFactory = participantFactory;
		this.conversationRepository = conversationRepository;
	}

	public void execute() {
		boolean conversationExists = conversationRepository.exists(conversationId);
		if (!conversationExists) {
			throw new IllegalArgumentException("Cannot update conversation, reason: a conversation with id '" + conversationId + "' does not exist.");
		}
		Conversation conversation = conversationRepository.get(conversationId);

		// add new participants
		Iterator<String> userIdIterator = userIds.iterator();
		while (userIdIterator.hasNext()) {
			String userId = userIdIterator.next();
			if (!conversation.containsParticipant(userId)) {
				Participant newParticipant = participantFactory.buildParticipant(userId);
				conversation.addParticipant(newParticipant);
			}
		}

		conversationRepository.update(conversation);
	}


}
