package com.github.maximilientyc.conversations.commands;

import com.github.maximilientyc.conversations.domain.services.UserService;

import java.util.Collection;

/**
 * Created by @maximilientyc on 22/05/2016.
 */
public abstract class ConversationCommand {

	private final Collection<String> userIds;
	private final UserService userService;

	public ConversationCommand(Collection<String> userIds, UserService userService) {
		this.userIds = userIds;
		this.userService = userService;
	}

	public void validate() {
		validateCorrectNumberOfParticipants();
		validateLoggedInUserIsAParticipant();
	}

	public Collection<String> getUserIds() {
		return userIds;
	}

	// validator collection
	private void validateLoggedInUserIsAParticipant() {
		String loggedInUserId = userService.getLoggedInUserId();
		if (!userIds.contains(loggedInUserId)) {
			throw new IllegalArgumentException("Current logged in user '" + loggedInUserId + "' is not a conversation member.");
		}
	}

	private void validateCorrectNumberOfParticipants() {
		if (userIds.size() < 2) {
			throw new IllegalArgumentException("Cannot create conversation, reason: not enough participants.");
		}
	}
}
