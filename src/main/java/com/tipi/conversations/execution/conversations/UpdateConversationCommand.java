package com.tipi.conversations.execution.conversations;

import com.tipi.conversations.domain.conversations.Conversation;
import com.tipi.conversations.repository.conversations.ConversationRepository;

/**
 * Created by Maximilien on 07/02/2016.
 */
public class UpdateConversationCommand {

	private final Conversation conversation;
	private final ConversationRepository conversationRepository;

	public UpdateConversationCommand(Conversation conversation, ConversationRepository conversationRepository) {
		this.conversation = conversation;
		this.conversationRepository = conversationRepository;
	}

	public void execute() {
		boolean conversationExists = conversationRepository.exists(conversation);
		if (!conversationExists) {
			throw new IllegalArgumentException("Cannot update conversation, reason: a conversation with id '"+ conversation.getConversationId() +"' does not exist.");
		}
		conversationRepository.update(conversation);
	}
}
