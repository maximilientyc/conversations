package com.tipi.conversations.api;

import com.tipi.conversations.domain.Conversation;
import com.tipi.conversations.domain.ConversationRepository;
import com.tipi.conversations.domain.Message;
import com.tipi.conversations.domain.MessageRepository;

/**
 * Created by @maximilientyc on 26/03/2016.
 */
public class PostMessageCommand {

	private final Message message;
	private final MessageRepository messageRepository;
	private final ConversationRepository conversationRepository;

	public PostMessageCommand(Message message, MessageRepository messageRepository, ConversationRepository conversationRepository) {
		this.message = message;
		this.messageRepository = messageRepository;
		this.conversationRepository = conversationRepository;
	}

	public void execute() {
		Conversation conversation = conversationRepository.get(message.getConversationId());
		boolean conversationContainsMessageParticipant = conversation.getParticipants().contains(message.getPostedBy());
		if (!conversationContainsMessageParticipant) {
			throw new IllegalArgumentException("Cannot post message, reason: not a participant.");
		}

		messageRepository.add(message);
	}
}
