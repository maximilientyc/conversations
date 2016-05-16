package com.github.maximilientyc.conversations.commands;

import com.github.maximilientyc.conversations.domain.Conversation;
import com.github.maximilientyc.conversations.domain.repositories.ConversationRepository;

import java.util.Date;

/**
 * Created by @maximilientyc on 16/05/2016.
 */
public class UpdateConversationLastActiveCommand {

	private final Conversation conversation;
	private final Date lastActiveOn;
	private final ConversationRepository conversationRepository;

	public UpdateConversationLastActiveCommand(Conversation conversation, Date lastActiveOn, ConversationRepository conversationRepository) {
		this.conversation = conversation;
		this.lastActiveOn = lastActiveOn;
		this.conversationRepository = conversationRepository;
	}

	public void execute() {
		conversation.setLastActiveOn(lastActiveOn);
		conversationRepository.update(conversation);
	}
}
