package com.tipi.conversations.commands;

import com.tipi.conversations.domain.Conversation;
import com.tipi.conversations.domain.ConversationRepository;

/**
 * Created by @maximilientyc on 07/02/2016.
 */
public class UpdateConversationCommand {

	private final Conversation conversation;
	private final ConversationRepository conversationRepository;

	public UpdateConversationCommand(Conversation conversation, ConversationRepository conversationRepository) {
		this.conversation = conversation;
		this.conversationRepository = conversationRepository;
	}

	public void execute() {
		boolean conversationExists = conversationRepository.exists(conversation.getConversationId());
		if (!conversationExists) {
			throw new IllegalArgumentException("Cannot update conversation, reason: a conversation with id '" + conversation.getConversationId() + "' does not exist.");
		}
		conversationRepository.update(conversation);
	}
}
