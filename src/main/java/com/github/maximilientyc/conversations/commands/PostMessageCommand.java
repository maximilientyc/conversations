package com.github.maximilientyc.conversations.commands;

import com.github.maximilientyc.conversations.domain.Conversation;
import com.github.maximilientyc.conversations.domain.Message;
import com.github.maximilientyc.conversations.domain.repositories.ConversationRepository;
import com.github.maximilientyc.conversations.domain.repositories.MessageRepository;

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
		validate();
	}

	public void execute() {
		Conversation conversation = conversationRepository.get(message.getConversationId());

		messageRepository.add(message);

		UpdateConversationLastActiveCommand updateConversationLastActiveCommand = new UpdateConversationLastActiveCommand(conversation, message.getPostedOn(), conversationRepository);
		updateConversationLastActiveCommand.execute();
	}

	private void validate() {
		validateConversationContainsPostedBy();
	}

	private void validateConversationContainsPostedBy() {
		Conversation conversation = conversationRepository.get(message.getConversationId());
		boolean conversationContainsMessageParticipant = conversation.getParticipants().contains(message.getPostedBy());
		if (!conversationContainsMessageParticipant) {
			throw new IllegalArgumentException("Cannot post message, reason: not a participant.");
		}
	}
}
