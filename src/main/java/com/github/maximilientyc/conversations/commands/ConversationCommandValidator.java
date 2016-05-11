package com.github.maximilientyc.conversations.commands;

import com.github.maximilientyc.conversations.domain.ConversationRepository;
import com.github.maximilientyc.conversations.domain.UserService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by @maximilientyc on 12/05/2016.
 */
public class ConversationCommandValidator {

	public void validate(CreateConversationCommand createConversationCommand) {
		validateLoggedInUserIsAParticipant(createConversationCommand.getUserService(), createConversationCommand.getUserIds());
		validateCorrectNumberOfParticipants(createConversationCommand.getUserIds());
	}

	public void validate(UpdateConversationCommand updateConversationCommand) {
		validateLoggedInUserIsAParticipant(updateConversationCommand.getUserService(), updateConversationCommand.getUserIds());
		validateCorrectNumberOfParticipants(updateConversationCommand.getUserIds());
		validateConversationExists(updateConversationCommand.getConversationId(), updateConversationCommand.getConversationRepository());
	}

	// validator collection
	private void validateLoggedInUserIsAParticipant(UserService userService, Iterable<String> userIds) {
		String loggedInUserId = userService.getLoggedInUserId();
		boolean loggedInUserIsAConversationMember = false;
		Iterator<String> iterator = userIds.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().equals(loggedInUserId)) {
				loggedInUserIsAConversationMember = true;
				break;
			}
		}
		if (!loggedInUserIsAConversationMember) {
			throw new IllegalArgumentException("Current logged in user '" + loggedInUserId + "' is not a conversation member.");
		}
	}

	private void validateCorrectNumberOfParticipants(Iterable<String> userIds) {
		List<String> userIdList = new ArrayList<String>();
		Iterator<String> iterator = userIds.iterator();
		iterator.forEachRemaining(userIdList::add);
		if (userIdList.size() < 2) {
			throw new IllegalArgumentException("Cannot create conversation, reason: not enough participants.");
		}
	}

	private void validateConversationExists(String conversationId, ConversationRepository conversationRepository) {
		boolean conversationExists = conversationRepository.exists(conversationId);
		if (!conversationExists) {
			throw new IllegalArgumentException("Cannot update conversation, reason: a conversation with id '" + conversationId + "' does not exist.");
		}
	}
}
