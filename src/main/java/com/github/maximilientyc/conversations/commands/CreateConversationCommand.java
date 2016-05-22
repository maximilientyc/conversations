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
public class CreateConversationCommand extends ConversationCommand {

	private final ConversationFactory conversationFactory;
	private final ParticipantFactory participantFactory;
	private final ConversationRepository conversationRepository;

	public CreateConversationCommand(Collection<String> userIds, ConversationFactory conversationFactory, ParticipantFactory participantFactory, ConversationRepository conversationRepository, UserService userService) {
		super(userIds, userService);
		this.conversationFactory = conversationFactory;
		this.participantFactory = participantFactory;
		this.conversationRepository = conversationRepository;
		validate();
	}

	public Conversation execute() {
		Conversation conversation = conversationFactory.buildConversation();
		for (String userId : getUserIds()) {
			conversation.addParticipant(participantFactory.buildParticipant(userId));
		}
		conversationRepository.add(conversation);

		return conversation;
	}
}
