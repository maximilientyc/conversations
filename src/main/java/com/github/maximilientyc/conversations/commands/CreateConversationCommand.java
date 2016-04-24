package com.github.maximilientyc.conversations.commands;

import com.github.maximilientyc.conversations.domain.Conversation;
import com.github.maximilientyc.conversations.domain.ConversationFactory;
import com.github.maximilientyc.conversations.domain.ConversationRepository;
import com.github.maximilientyc.conversations.domain.ParticipantFactory;

import java.util.Iterator;

/**
 * Created by @maximilientyc on 07/02/2016.
 */
public class CreateConversationCommand {

	private final Iterable<String> userIds;
	private final ConversationFactory conversationFactory;
	private final ParticipantFactory participantFactory;
	private final ConversationRepository conversationRepository;

	public CreateConversationCommand(Iterable<String> userIds, ConversationFactory conversationFactory, ParticipantFactory participantFactory, ConversationRepository conversationRepository) {
		this.userIds = userIds;
		this.conversationFactory = conversationFactory;
		this.participantFactory = participantFactory;
		this.conversationRepository = conversationRepository;
	}

	public Conversation execute() {
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
}
