package com.tipi.conversations.execution.conversations;

import com.tipi.conversations.domain.conversations.Conversation;
import com.tipi.conversations.domain.conversations.ConversationRepository;
import com.tipi.conversations.domain.conversations.ConversationService;

/**
 * Created by Maximilien on 07/02/2016.
 */
public class CreateConversationCommand {

	private final Conversation conversation;
	private final ConversationRepository conversationRepository;

	public CreateConversationCommand(Conversation conversation, ConversationRepository conversationRepository) {
		this.conversation = conversation;
		this.conversationRepository = conversationRepository;
	}

	public void execute() {
		boolean conversationExists = conversationRepository.exists(conversation);
		if (conversationExists) {
			throw new IllegalArgumentException("Cannot create conversation, reason: a conversation with id '"+ conversation.getConversationId() +"' already exists.");
		}
		conversationRepository.add(conversation);
	}
}
